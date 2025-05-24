package org.antbiz.antbiz_framework.framework.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.antbiz.antbiz_framework.framework.exceptions.CustomException
import org.antbiz.antbiz_framework.framework.exceptions.ServerErrorException
import org.antbiz.antbiz_framework.framework.lib.ApiResponseDTO
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class ExceptionHandlerFilter(private val objectMapper: ObjectMapper) : OncePerRequestFilter() {
    companion object {
        private const val ERROR_WRITING_RESPONSE = "Error on Exception Handling!"
        private const val JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE
        private val EXCLUDED_PATHS = listOf("/admin", "/admin/**", "/login")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: CustomException) {
            logger.warn("${e.message}, status: ${e.status}")
            writeErrorResponse(response, e)
        } catch (e: RuntimeException) {
            writeErrorResponse(response, ServerErrorException())
        } catch (e: ServletException) {
            val cause = e.cause
            if (cause is CustomException) {
                writeErrorResponse(response, cause)
            } else {
                handleUnknownException(e, response)
            }
        } catch (e: Exception) {
            handleUnknownException(e, response)
        }
    }

    private fun handleUnknownException(e: Exception, response: HttpServletResponse) {
        logExceptionStacktrace(e)
        writeErrorResponse(response, ServerErrorException())
    }

    private fun logExceptionStacktrace(e: Exception) {
        logger.error(e.javaClass.name)
        logger.error(e.stackTraceToString())
    }

    private fun writeErrorResponse(
        response: HttpServletResponse,
        error: CustomException,
    ) {
        response.status = error.status
        response.contentType = JSON_MEDIA_TYPE
        val errorResponse = createErrorResponseDto(error)
        try {
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch(e: IOException) {
            logger.error(ERROR_WRITING_RESPONSE, e)
        }
    }

    private fun createErrorResponseDto(error: CustomException): ApiResponseDTO<String> {
        return ApiResponseDTO(
            error = true,
            message = error.message,
            code = error.status
        )
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return EXCLUDED_PATHS.any { request.requestURI.contains(it) }
    }
}