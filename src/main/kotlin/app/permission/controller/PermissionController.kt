package app.permission.controller

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.service.PermissionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("permission")
class PermissionController {
    @Autowired
    val permissionService: PermissionService? = null

    @PostMapping("snippet/create")
    fun createSnippet(
        @RequestBody input: CreateSnippetInput,
    ) {
        permissionService?.createSnippet(input)
    }

    @PostMapping("snippet/share")
    fun shareSnippet(
        @RequestBody input: ShareSnippetInput,
    ) {
        permissionService?.shareSnippet(input)
    }
}
