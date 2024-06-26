package app.permission.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Permission type not found")
class PermissionTypeNotFound : RuntimeException()
