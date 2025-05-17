package org.antbiz.antbiz_framework.backend.config

import org.antbiz.antbiz_framework.backend.security.ExceptionHandlerFilter
import org.antbiz.antbiz_framework.backend.security.JwtAuthenticationFilter
import org.antbiz.antbiz_framework.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter,
    @Value("\${antbiz.etc.host}")
    private val host: String
): WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        val origins = mutableListOf("http://localhost:3000", "http://localhost:8080")
        origins.addAll(host.split(",").mapNotNull { it.trim() })

        registry.addMapping("/**")
            .allowedOrigins(*origins.toTypedArray())
            .allowedHeaders("Authorization", "Content-Type", "X-CSRF-TOKEN")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true)
    }

    @Bean
    public fun defaultFilterChain(http: HttpSecurity, userDetailsService: UserDetailsService): DefaultSecurityFilterChain {
        return http
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests {
                it.requestMatchers("/framework/admin/**").hasRole("ADMIN")
                it.requestMatchers("/framework/auth/**").permitAll()
                it.requestMatchers("/framework/api/**").permitAll()
                it.requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/v3/api-docs/**",
                    "/v2/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                it.anyRequest().authenticated()
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
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(exceptionHandlerFilter, LogoutFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .userDetailsService(userDetailsService)
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Autowired
    private lateinit var bc: BCryptPasswordEncoder

    @Bean
    fun authenticationProvider(userService: UserService): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService)
        authProvider.setPasswordEncoder(bc)

        return authProvider
    }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }
}
