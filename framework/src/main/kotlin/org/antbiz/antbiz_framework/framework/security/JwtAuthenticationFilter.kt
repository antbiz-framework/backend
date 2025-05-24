package org.antbiz.antbiz_framework.framework.security

import org.antbiz.antbiz_framework.framework.exceptions.CustomException
import org.antbiz.antbiz_framework.framework.exceptions.JwtExpiredException
import org.antbiz.antbiz_framework.framework.exceptions.ServerErrorException
import org.antbiz.antbiz_framework.framework.exceptions.UnauthorizedException
import org.antbiz.antbiz_framework.framework.jwt.JwtProvider
import org.antbiz.antbiz_framework.framework.service.RevokedTokenService
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.antbiz.antbiz_framework.framework.service.UserService
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

    companion object {
        private val EXCLUDE_PATHS = listOf("/auth/login", "/auth/register", "/auth/refresh")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            authenticateRequest(request)
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

    private fun authenticateRequest(request: HttpServletRequest) {
        val jwt = extractJwtFromRequest(request) ?: return
        val claims = jwtProvider.parseJwts(jwt) ?: return

        if (revokedTokenService.isTokenRevoked(jwt)) {
            logger.warn("Token has been revoked. Token hash: ${jwt.hashCode()}")
            throw JwtExpiredException()
        }
        val userId = claims["userId"] as String
        val userDetails = userService.loadUserById(userId)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (authorizationHeader?.startsWith("Bearer ") == true) {
            authorizationHeader.substring(7)
        } else null
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return EXCLUDE_PATHS.any { request.requestURI.contains(it) }
    }
}