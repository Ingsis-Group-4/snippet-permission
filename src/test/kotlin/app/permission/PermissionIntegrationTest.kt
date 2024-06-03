package app.permission

import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionOutput
import app.permission.persistance.entity.Permission
import app.permission.persistance.entity.PermissionType
import app.permission.persistance.repository.PermissionRepository
import app.permission.persistance.repository.PermissionTypeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class PermissionIntegrationTest {
    @Autowired
    private lateinit var permissionTypeRepository: PermissionTypeRepository

    @Autowired
    private lateinit var permissionRepository: PermissionRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val base = "/permission"

    companion object {
        private const val TEST_PERMISSION_TYPE = "TEST"
        private const val AUTHOR_PERMISSION_TYPE = "OWNER"

        @JvmStatic
        @BeforeAll
        fun createPermissionType(
            @Autowired permissionTypeRepository: PermissionTypeRepository,
        ) {
            permissionTypeRepository.save(PermissionType(TEST_PERMISSION_TYPE))
            permissionTypeRepository.save(PermissionType(AUTHOR_PERMISSION_TYPE))
        }
    }

    @Test
    fun `test 001 _ create permission with non existent permission type should return 404`() {
        val createPermissionRequestBody =
            CreatePermissionInput(
                "001",
                "001",
                "NON_EXISTENT",
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        val result =
            mockMvc.perform(
                post("$base/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andReturn()

        Assertions.assertEquals(404, result.response.status)
        Assertions.assertEquals("Permission type not found", result.response.errorMessage)
    }

    @Test
    fun `test 002 _ create valid permission`() {
        val createPermissionRequestBody =
            CreatePermissionInput(
                "002",
                "002",
                TEST_PERMISSION_TYPE,
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        val result =
            mockMvc.perform(
                post("$base/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andReturn()

        Assertions.assertEquals(200, result.response.status)
    }

    @Test
    fun `test 003 _ create already existing permission should return 409`() {
        val createPermissionRequestBody =
            CreatePermissionInput(
                "003",
                "003",
                TEST_PERMISSION_TYPE,
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        mockMvc.perform(
            post("$base/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isOk)

        mockMvc.perform(
            post("$base/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isConflict)
    }

    @Test
    fun `test 004 _ get all permissions for user`() {
        val userId = "004"

        val createPermissionRequestBody =
            CreatePermissionInput(
                "004",
                "004",
                AUTHOR_PERMISSION_TYPE,
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        mockMvc.perform(
            post("$base/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isOk)

        val result =
            mockMvc.perform(
                get("$base/all/$userId"),
            ).andReturn()

        val permissions = objectMapper.readValue<List<PermissionOutput>>(result.response.contentAsString)

        Assertions.assertEquals(1, permissions.size)
        Assertions.assertEquals("004", permissions[0].snippetId)
        Assertions.assertEquals("004", permissions[0].authorId)
    }

    @Test
    fun `test 005 _ get all permissions for user with correct authorId`() {
        val snippetId = "005"
        val authorUserId = snippetId + "_1"
        val sharedUserId = snippetId + "_2"

        val authorPermissionType = permissionTypeRepository.findByType(AUTHOR_PERMISSION_TYPE)
        val testPermissionType = permissionTypeRepository.findByType(TEST_PERMISSION_TYPE)

        val authorPermission =
            Permission(
                snippetId = snippetId,
                userId = authorUserId,
                permissionType = authorPermissionType.get(),
            )

        val sharedUserPermission =
            Permission(
                snippetId = snippetId,
                userId = sharedUserId,
                permissionType = testPermissionType.get(),
            )

        permissionRepository.saveAll(listOf(authorPermission, sharedUserPermission))

        val result =
            mockMvc.perform(
                get("$base/all/$sharedUserId"),
            ).andReturn()

        val permission = objectMapper.readValue<List<PermissionOutput>>(result.response.contentAsString)

        Assertions.assertEquals(1, permission.size)
        Assertions.assertEquals(permission[0].authorId, authorUserId)
    }
}
