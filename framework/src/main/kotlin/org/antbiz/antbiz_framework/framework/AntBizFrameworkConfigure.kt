package org.antbiz.antbiz_framework.framework

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import space.mori.dalbodeule.snapadmin.external.annotations.SnapAdminEnabled


@AutoConfiguration
@EnableJpaRepositories(basePackages = ["org.antbiz.antbiz_framework.framework.repository"])
@EntityScan(basePackages = ["org.antbiz.antbiz_framework.framework.model"])
@ComponentScan(basePackages = ["org.antbiz.antbiz_framework.framework"])
@ConditionalOnClass(JpaRepository::class)
@SnapAdminEnabled
class AntBizFrameworkConfigure

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(AntBizFrameworkConfigure::class)
annotation class EnableAntBizFramework

