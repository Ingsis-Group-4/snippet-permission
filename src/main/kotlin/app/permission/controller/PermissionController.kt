package app.permission.controller

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.PermissionOutput
import app.permission.service.PermissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PermissionController(
    @Autowired val permissionService: PermissionService,
) : PermissionControllerSpec {
    override fun createSnippet(
        @Valid @RequestBody input: CreateSnippetInput,
    ): ResponseEntity<Unit> {
        permissionService.createPermission(input)
        return ResponseEntity.ok().build()
    }

    override fun getAllUserPermissions(userId: String): List<PermissionOutput> {
        return permissionService.getAllUserPermissions(userId)
    }

    override fun deleteAllPermissionsForSnippet(snippetId: String): ResponseEntity<Unit> {
        permissionService.deleteAllPermissionsForSnippet(snippetId)
        return ResponseEntity.ok().build()
    }
}
