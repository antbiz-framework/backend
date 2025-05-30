package org.antbiz.backend_test.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.antbiz.antbiz_framework.framework.exceptions.ServerErrorException
import org.antbiz.antbiz_framework.framework.exceptions.UnauthorizedException
import org.antbiz.antbiz_framework.framework.jwt.JwtProvider
import org.antbiz.antbiz_framework.framework.lib.ApiResponseDTO
import org.antbiz.antbiz_framework.framework.model.RefreshToken
import org.antbiz.antbiz_framework.framework.model.UserEntity
import org.antbiz.antbiz_framework.framework.service.TokenService
import org.antbiz.backend_test.model.User
import org.antbiz.backend_test.repository.UserAdditionalRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)
data class TokenRefreshRequest(val refreshToken: String)
data class TokenRefreshResponse(val accessToken: String, val refreshToken: String)

data class LogoutRequest(val accessToken: String, val refreshToken: String)

data class UserInfoResponse(val email: String, val roles: List<String>, val username: String)

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Endpoints for authentication and token management")
class AuthController(
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val userRepository: UserAdditionalRepository,
    @Autowired private val jwtProvider: JwtProvider,
    @Autowired private val tokenService: TokenService
) {
   @PostMapping("/login")
   @Operation(summary = "Authenticate user and generate JWT tokens", description = "Validates user credentials and provides access and refresh tokens.")
   @ApiResponses(
       value = [
           ApiResponse(responseCode = "200", description = "Successfully authenticated", useReturnTypeSchema = true),
           ApiResponse(responseCode = "401", description = "Invalid credentials", content = [Content(schema=Schema(implementation=java.lang.Exception::class))]),
           ApiResponse(responseCode = "400", description = "Bad Request", content = [Content(schema=Schema(implementation=java.lang.Exception::class))]),
       ]
   )
   fun authenticateUser(
       @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Email and password for authentication") 
       @RequestBody loginRequest: LoginRequest
   ): ResponseEntity<ApiResponseDTO<LoginResponse>> {
       val authentication = authenticationManager.authenticate(
           UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
       )

       val user = userRepository.findByEmail(loginRequest.email).get()

       val accessToken = jwtProvider.generateAccessToken(user)
       val refreshToken = jwtProvider.generateRefreshToken(user)

       return ResponseEntity.ok(ApiResponseDTO(data = LoginResponse(accessToken, refreshToken)))
   }


    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT tokens", description = "Generates new access and refresh tokens from a valid refresh token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully refreshed tokens", useReturnTypeSchema = true),
            ApiResponse(responseCode = "400", description = "Invalid or expired refresh token", content = [Content(schema=Schema(implementation=java.lang.Exception::class))])
        ]
    )
    fun refreshToken(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token to generate new tokens")
        @RequestBody tokenRefreshRequest: TokenRefreshRequest,
    ): ResponseEntity<ApiResponseDTO<TokenRefreshResponse>> {
        val requestRefreshToken = tokenRefreshRequest.refreshToken

        return tokenService.findByToken(requestRefreshToken)
            .map { refreshToken ->
                try {
                    processRefreshToken(refreshToken, requestRefreshToken)
                } catch (e: UnauthorizedException) {
                    throw e
                } catch (e: Exception) {
                    throw ServerErrorException()
                }
            }
            .orElseThrow {
                ServerErrorException()
            }
    }

    private fun processRefreshToken(
        refreshToken: RefreshToken,
        requestRefreshToken: String,
    ): ResponseEntity<ApiResponseDTO<TokenRefreshResponse>> {
        // 토큰 만료 검증
        tokenService.verifyExpiration(refreshToken)

        // 사용자 확인
        val user: UserEntity = refreshToken.user ?: throw UnauthorizedException()

        val refresh = refreshToken.expiryDate.isBefore(LocalDateTime.now().plusDays(1))
        // 새 액세스 토큰 생성
        val accessToken = tokenService.generateAccessToken(user)
        val refreshToken = if (refresh) tokenService.generateRefreshToken(user) else requestRefreshToken

        // 응답 생성
        return ResponseEntity.ok(
            ApiResponseDTO(data = TokenRefreshResponse(accessToken, refreshToken))
        )
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidates refresh tokens for the user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully logged out", useReturnTypeSchema = true),
            ApiResponse(responseCode = "400", description = "Invalid refresh token", content = [Content(schema=Schema(implementation=java.lang.Exception::class))])
        ]
    )
    fun logoutUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token to invalidate")
        @RequestBody tokenRevokeRequest: LogoutRequest
    ): ResponseEntity<String> {
        val requestRefreshToken = tokenRevokeRequest.refreshToken
        tokenService.findByToken(requestRefreshToken).ifPresent {refreshToken ->
            refreshToken.user?.let { tokenService.deleteByUser(it) }
        }

        return ResponseEntity.ok("Success")
    }


    @GetMapping("/me")
    @Operation(
        summary = "Get current user's information",
        description = "Retrieves the logged-in user's information using a valid access token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved user information",
                useReturnTypeSchema = true
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Invalid or missing access token",
                content = [Content(schema = Schema(implementation = java.lang.Exception::class))]
            )
        ]
    )
    fun getUserInfo(
        @AuthenticationPrincipal userDetails: User?,
    ): ResponseEntity<ApiResponseDTO<UserInfoResponse>> {
        if(userDetails?.id == null) return ResponseEntity.badRequest().body(null)

        val user = userRepository.findById(userDetails.id ?: "").get()

        val userInfo = UserInfoResponse(
            user.email,
            user.authorities.map { it.authority },
            user.username
        )
        return ResponseEntity.ok(ApiResponseDTO(data = userInfo))
    }
}