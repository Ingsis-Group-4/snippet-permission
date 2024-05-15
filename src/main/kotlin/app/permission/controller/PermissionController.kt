package app.permission.controller

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.model.dto.SnippetOutput
import app.permission.model.enums.PermissionTypeInput
import app.permission.service.PermissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("permission")
@Validated
class PermissionController(
    @Autowired val permissionService: PermissionService,
) {
    @PostMapping("snippet/create")
    fun createSnippet(
        @Valid @RequestBody input: CreateSnippetInput,
    ): ResponseEntity<Unit> {
        permissionService.createSnippet(input)
        return ResponseEntity.ok().build()
    }

    @PostMapping("snippet/share")
    fun shareSnippet(
        @RequestBody input: ShareSnippetInput,
    ): ResponseEntity<Unit> {
        permissionService.shareSnippet(input)
        return ResponseEntity.ok().build()
    }

    @GetMapping("snippet/all/{userId}")
    fun getAllSnippets(
        @RequestParam("type") permissionTypeInput: PermissionTypeInput?,
        @PathVariable userId: String,
    ): ResponseEntity<List<SnippetOutput>> {
        return ResponseEntity.ok(permissionService.getAllSnippets(userId, permissionTypeInput))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = ex.bindingResult.fieldErrors.map { it.defaultMessage }
        val errorResponse = mapOf("message" to "Validation failed", "errors" to errors)
        return ResponseEntity.badRequest().body(errorResponse)
    }
}
