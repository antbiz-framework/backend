package org.antbiz.antbiz_framework.backend.security

import org.antbiz.antbiz_framework.backend.exceptions.CustomException
import org.antbiz.antbiz_framework.backend.exceptions.JwtExpiredException
import org.antbiz.antbiz_framework.backend.exceptions.ServerErrorException
import org.antbiz.antbiz_framework.backend.exceptions.UnauthorizedException
import org.antbiz.antbiz_framework.backend.jwt.JwtProvider
import org.antbiz.antbiz_framework.backend.service.RevokedTokenService
import org.antbiz.antbiz_framework.backend.service.UserService
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    @Autowired private val jwtProvider: JwtProvider,
    @Autowired private val userService: UserService,
    @Autowired private val revokedTokenService: RevokedTokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            getJwtFromRequest(request)?.let { token ->
                val claims = jwtProvider.parseJwts(token) ?: return
                if (revokedTokenService.isTokenRevoked(token)) {
                    logger.warn("Token has been revoked. Token hash: ${token.hashCode()}")
                    throw JwtExpiredException()
                }

                val userId = claims["userId"] as String
                val userDetails = userService.loadUserById(userId)
                val authentication =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: CustomException) {
            throw e
        } catch (e: UsernameNotFoundException) {
            logger.error("User not found: ${e.message}")
            throw UnauthorizedException()
        } catch (e: JwtException) {
            logger.error("Invalid JWT: ${e.message}")
            throw JwtExpiredException()
        } catch (e: Exception) {
            logger.error("Unexpected error during authentication", e)
            throw ServerErrorException()
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken?.startsWith("Bearer ") == true) {
            bearerToken.substring(7)
        } else null
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludePath = listOf("/auth/login", "/auth/register", "/auth/refresh")

        return excludePath.any { request.requestURI.contains(it) }
    }
}