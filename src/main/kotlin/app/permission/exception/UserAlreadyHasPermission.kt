package app.permission.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already has a permission for that snippet")
class UserAlreadyHasPermission : RuntimeException()
