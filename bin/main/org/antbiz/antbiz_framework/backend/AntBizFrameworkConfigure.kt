package org.antbiz.antbiz_framework.backend

import org.antbiz.antbiz_framework.backend.config.JwtConfig
import org.antbiz.antbiz_framework.backend.config.SwaggerConfig
import org.antbiz.antbiz_framework.backend.config.SecurityConfig
import org.antbiz.antbiz_framework.backend.controller.LoginController
import org.antbiz.antbiz_framework.backend.jwt.JwtProvider
import org.antbiz.antbiz_framework.backend.model.RefreshToken
import org.antbiz.antbiz_framework.backend.model.RevokedToken
import org.antbiz.antbiz_framework.backend.model.User
import org.antbiz.antbiz_framework.backend.repository.RefreshTokenRepository
import org.antbiz.antbiz_framework.backend.repository.RevokedTokenRepository
import org.antbiz.antbiz_framework.backend.repository.UserRepository
import org.antbiz.antbiz_framework.backend.security.ExceptionHandlerFilter
import org.antbiz.antbiz_framework.backend.security.JwtAuthenticationFilter
import org.antbiz.antbiz_framework.backend.service.RevokedTokenService
import org.antbiz.antbiz_framework.backend.service.TokenService
import org.antbiz.antbiz_framework.backend.service.UserService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Import
import space.mori.dalbodeule.snapadmin.external.SnapAdminAutoConfiguration
import java.lang.annotation.ElementType


@AutoConfiguration
@Import(
    RefreshToken::class,
    RevokedToken::class,
    TokenService::class,
    RevokedTokenService::class,
    User::class,
    UserService::class,
    RefreshTokenRepository::class,
    RevokedTokenRepository::class,
    UserRepository::class,
    JwtProvider::class,
    ExceptionHandlerFilter::class,
    JwtAuthenticationFilter::class,
    NativeConf::class,
    JwtConfig::class,
    SecurityConfig::class,
    SwaggerConfig::class,
    LoginController::class,
)
@ImportAutoConfiguration(classes = [SnapAdminAutoConfiguration::class])
class AntBizFrameworkConfigure

@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Import(AntBizFrameworkConfigure::class)
annotation class EnableAntBizFramework