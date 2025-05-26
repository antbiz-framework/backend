package org.antbiz.backend_test.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.antbiz.antbiz_framework.framework.lib.ApiResponseDTO
import org.antbiz.backend_test.model.Housing
import org.antbiz.backend_test.repository.HousingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


enum class PageSize(val value: Int) {
    SMALL(10),
    MEDIUM(25),
    LARGE(50),
    EXTRA_LARGE(100);

    companion object {
        fun isValid(size: Int) = PageSize.entries.any { it.value == size }
    }
}

data class ListResponse<T>(
    val data: List<T>,
    val total: Long,
    val page: Int,
    val size: Int,
    val pages: Int,
    val columns: List<String> = listOf()
)

@RestController
@RequestMapping("/napi/housing")
class HousingController(
    @Autowired private val housingRepository: HousingRepository,
) {
    @GetMapping("/list")
    @Operation(
        summary = """
            [en] Get California Housing Prices data with pagination.
            [ko] 캘리포니아 주택 가격 데이터를 페이지네이션으로 조회합니다.
        """,
        description = """
            [en] Retrieve paginated housing data including median house values, location, and demographic information from California census
            [ko] 캘리포니아 인구조사 데이터에서 중간 주택 가격, 위치, 인구통계 정보를 포함한 주택 데이터를 페이지 단위로 가져옵니다
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "[en] Successfully retrieved housing data\n[ko] 주택 데이터를 성공적으로 조회했습니다"
            ),
            ApiResponse(
                responseCode = "400",
                description = "[en] Invalid page or size parameter\n[ko] 페이지 또는 크기 파라미터가 잘못되었습니다"
            )
        ]
    )

    fun getHousingList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<ApiResponseDTO<ListResponse<Housing>>> {
        if (!PageSize.isValid(size)) {
            return ResponseEntity.badRequest().body(
                ApiResponseDTO(
                    false,
                    "Invalid size parameter. Allowed values are: 10, 25, 50, 100"
                )
            )
        }

        return ResponseEntity.ok(
            ApiResponseDTO(
                data = ListResponse(
                    total = housingRepository.count(),
                    page = page,
                    pages = ((housingRepository.count() + size - 1) / size).toInt(),
                    size = size,
                    data = housingRepository.findAll(PageRequest.of(page, size)).content,
                    columns = Housing::class.java.declaredFields
                        .filter { !(it.isSynthetic || it.name.contains("$\$_hibernate")) }
                        .map { it.name }
                )
            )
        )
    }
}
