package org.antbiz.antbiz_framework.framework.config

import org.antbiz.antbiz_framework.framework.security.ExceptionHandlerFilter
import org.antbiz.antbiz_framework.framework.security.JwtAuthenticationFilter
import org.antbiz.antbiz_framework.framework.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = true)
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter,
    @Autowired
    private val userService: UserService,
    @Value("\${antbiz.etc.host}")
    private val host: String,
    @Value("\${antbiz.etc.frontend}")
    private val frontendHost: String
): WebMvcConfigurer {
    private val ALLOWED_HTTP_METHODS = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    private val ALLOWED_HEADERS = listOf("Authorization", "Content-Type", "X-CSRF-TOKEN")

    private fun createCorsConfiguration(host: String, frontendHost: String): CorsConfiguration {
        return CorsConfiguration().apply {
            allowedOrigins = listOf(host, frontendHost).mapNotNull { it.trim() }
            allowedMethods = ALLOWED_HTTP_METHODS
            allowedHeaders = ALLOWED_HEADERS
            allowCredentials = true
        }
    }

    @Bean
    @Order(1)
    open fun jwtSecurityFilterChain(
        http: HttpSecurity,
        @Qualifier("AntbizUserService")
        userDetailsService: UserDetailsService
    ): SecurityFilterChain {
        return http
            .securityMatcher("/api/**")
            .csrf { it.disable() }
            .cors {
                val corsConfiguration = createCorsConfiguration(host, frontendHost)
                val source = UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", corsConfiguration)
                }
                it.configurationSource(source)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(exceptionHandlerFilter, LogoutFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authenticationProvider(authenticationProvider())
            .userDetailsService(userDetailsService)
            .build()
    }

    @Bean
    @Order(2)
    fun basicAuthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/login", "/logout", "/admin/**")
            .csrf { it.disable() }
            .cors {
                val corsConfiguration = createCorsConfiguration(host, frontendHost)
                val source = UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", corsConfiguration)
                }
                it.configurationSource(source)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/admin/**").hasRole("ADMIN")
                auth.requestMatchers("/login", "/logout").permitAll()
                auth.anyRequest().authenticated()
            }
            .formLogin {
                it.loginPage("/login")
                    .defaultSuccessUrl("/admin").permitAll()
            }
            .logout {
                it.logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            }
            .httpBasic(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider())
            .userDetailsService(userService)
        return http.build()
    }

    @Bean
    @Order(3)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/v3/api-docs/**",
                    "/v2/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                it.anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService)
        authProvider.setPasswordEncoder(passwordEncoder())

        return authProvider
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }
}