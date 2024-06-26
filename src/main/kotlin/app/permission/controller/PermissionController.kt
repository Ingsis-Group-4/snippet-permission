package app.permission.controller

import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionListOutput
import app.permission.service.PermissionService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PermissionController(
    @Autowired val permissionService: PermissionService,
) : PermissionControllerSpec {
    override fun createSnippet(
        @Valid @RequestBody input: CreatePermissionInput,
    ): ResponseEntity<Unit> {
        permissionService.createPermission(input)
        return ResponseEntity.ok().build()
    }

    override fun getAllUserPermissions(
        jwt: Jwt,
        pageNum: Int,
        pageSize: Int,
    ): PermissionListOutput {
        return permissionService.getAllUserPermissions(jwt.subject, pageNum, pageSize)
    }

    override fun deleteAllPermissionsForSnippet(snippetId: String): ResponseEntity<Unit> {
        permissionService.deleteAllPermissionsForSnippet(snippetId)
        return ResponseEntity.ok().build()
    }

    override fun getAuthorFromSnippetId(snippetId: String): String {
        return permissionService.getAuthorFromSnippetId(snippetId)
    }
}
