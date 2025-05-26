package org.antbiz.antbiz_framework.framework

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import space.mori.dalbodeule.snapadmin.external.annotations.SnapAdminEnabled


@AutoConfiguration
@EnableJpaRepositories(basePackages = ["org.antbiz.antbiz_framework.framework.repository"])
@EntityScan(basePackages = ["org.antbiz.antbiz_framework.framework.model"])
@ComponentScan(basePackages = ["org.antbiz.antbiz_framework.framework"])
@ConditionalOnClass(JpaRepository::class)
@SnapAdminEnabled
class AntBizFrameworkConfigure {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return mapper
    }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(AntBizFrameworkConfigure::class)
annotation class EnableAntBizFramework

