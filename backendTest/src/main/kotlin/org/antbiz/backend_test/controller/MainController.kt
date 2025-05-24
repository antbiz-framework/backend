package space.ohmyllm.ohmyllm.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Hidden
class MainController {
    @GetMapping("/")
    fun index(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World!")
    }
}