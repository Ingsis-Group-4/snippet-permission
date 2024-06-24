package app.permission.controller

import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionListOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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

    @GetMapping("all")
    @Operation(
        summary = "Get all permissions from a user",
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
        ],
    )
    fun getAllUserPermissions(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestParam("page_num") pageNum: Int,
        @RequestParam("page_size") pageSize: Int,
    ): PermissionListOutput

    @DeleteMapping("all/{snippetId}")
    fun deleteAllPermissionsForSnippet(
        @PathVariable("snippetId") snippetId: String,
    ): ResponseEntity<Unit>

    @GetMapping("author/{snippetId}")
    @Operation(
        summary = "Get the author of a snippet",
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
        ],
    )
    fun getAuthorFromSnippetId(
        @PathVariable("snippetId") snippetId: String,
    ): String
}
