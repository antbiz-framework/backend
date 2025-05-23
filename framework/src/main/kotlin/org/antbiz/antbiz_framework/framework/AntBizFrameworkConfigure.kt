package org.antbiz.antbiz_framework.framework

import org.antbiz.antbiz_framework.framework.config.AntBizJpaAutoConfig
import org.antbiz.antbiz_framework.framework.config.JwtConfig
import org.antbiz.antbiz_framework.framework.config.SecurityConfig
import org.antbiz.antbiz_framework.framework.config.SwaggerConfig
import org.antbiz.antbiz_framework.framework.controller.LoginController
import org.antbiz.antbiz_framework.framework.jwt.JwtProvider
import org.antbiz.antbiz_framework.framework.model.RefreshToken
import org.antbiz.antbiz_framework.framework.model.RevokedToken
import org.antbiz.antbiz_framework.framework.model.UserEntity
import org.antbiz.antbiz_framework.framework.security.ExceptionHandlerFilter
import org.antbiz.antbiz_framework.framework.security.JwtAuthenticationFilter
import org.antbiz.antbiz_framework.framework.service.RevokedTokenService
import org.antbiz.antbiz_framework.framework.service.TokenService
import org.antbiz.antbiz_framework.framework.service.UserService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import space.mori.dalbodeule.snapadmin.external.annotations.SnapAdminEnabled


@AutoConfiguration
@EnableJpaRepositories(basePackages = ["org.antbiz.antbiz_framework.framework.repository"])
@EntityScan(basePackages = ["org.antbiz.antbiz_framework.framework.model"])
@Import(
    RefreshToken::class,
    RevokedToken::class,
    TokenService::class,
    RevokedTokenService::class,
    UserEntity::class,
    UserService::class,
    JwtProvider::class,
    ExceptionHandlerFilter::class,
    JwtAuthenticationFilter::class,
    JwtConfig::class,
    SecurityConfig::class,
    SwaggerConfig::class,
    LoginController::class,
    NativeConf::class,
    AntBizJpaAutoConfig::class
)
@SnapAdminEnabled
@ConditionalOnClass(JpaRepository::class)
class AntBizFrameworkConfigure

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(AntBizFrameworkConfigure::class)
annotation class EnableAntBizFramework
