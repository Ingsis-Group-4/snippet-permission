package app.permission.controller

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.model.dto.SnippetOutput
import app.permission.model.enums.PermissionTypeInput
import app.permission.service.PermissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PermissionController(
    @Autowired val permissionService: PermissionService,
) : PermissionControllerSpec {
    @PostMapping("snippet/create")
    override fun createSnippet(
        @Valid @RequestBody input: CreateSnippetInput,
    ): ResponseEntity<Unit> {
        permissionService.createSnippet(input)
        return ResponseEntity.ok().build()
    }

    @PostMapping("snippet/share")
    override fun shareSnippet(
        @RequestBody input: ShareSnippetInput,
    ): ResponseEntity<Unit> {
        permissionService.shareSnippet(input)
        return ResponseEntity.ok().build()
    }

    @GetMapping("snippet/all/{userId}")
    override fun getAllSnippets(
        @RequestParam("type") permissionTypeInput: PermissionTypeInput?,
        @PathVariable userId: String,
    ): ResponseEntity<List<SnippetOutput>> {
        return ResponseEntity.ok(permissionService.getAllSnippets(userId, permissionTypeInput))
    }
}
