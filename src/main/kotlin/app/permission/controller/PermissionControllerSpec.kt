package app.permission.controller

import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("permission")
interface PermissionControllerSpec {
    @PostMapping("create")
    @Operation(
        summary = "Create a snippet permission",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = CreatePermissionInput::class))]),
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "404",
                ref = "Permission Type not found",
            ),
            ApiResponse(
                responseCode = "409",
                ref = "User has already a permission type for that snippet",
            ),
        ],
    )
    fun createSnippet(input: CreatePermissionInput): ResponseEntity<Unit>

    @GetMapping("all/{userId}")
    @Operation(
        summary = "Get all permissions from a user",
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
        ],
    )
    fun getAllUserPermissions(
        @PathVariable("userId") userId: String,
    ): List<PermissionOutput>
}
