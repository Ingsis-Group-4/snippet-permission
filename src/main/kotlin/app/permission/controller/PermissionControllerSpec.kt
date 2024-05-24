package app.permission.controller

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.model.dto.SnippetOutput
import app.permission.model.enums.PermissionTypeInput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("permission")
interface PermissionControllerSpec {
    @PostMapping("snippet/create")
    @Operation(
        summary = "Create a snippet permission",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = CreateSnippetInput::class))]),
        // TODO: Add error cases
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
        ],
    )
    fun createSnippet(input: CreateSnippetInput): ResponseEntity<Unit>

    @PostMapping("snippet/share")
    @Operation(
        summary = "Share a snippet with another user",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = ShareSnippetInput::class))]),
        // TODO: Add error cases
        responses = [
            ApiResponse(
                responseCode = "200",
            ),
        ],
    )
    fun shareSnippet(input: ShareSnippetInput): ResponseEntity<Unit>

    @GetMapping("snippet/all/{userId}")
    @Operation(
        summary = "Get all snippets keys for my users",
        parameters = [
            Parameter(name = "userId", `in` = ParameterIn.PATH, required = true, schema = Schema(type = "string")),
            Parameter(
                name = "type",
                description = "One of OWNED, SHARED or ALL. Default is OWNED",
                required = false,
                schema = Schema(implementation = PermissionTypeInput::class),
            ),
        ],
    )
    fun getAllSnippets(
        permissionTypeInput: PermissionTypeInput?,
        userId: String,
    ): ResponseEntity<List<SnippetOutput>>
}
