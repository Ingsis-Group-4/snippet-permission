package app.permission

import app.permission.model.dto.CreatePermissionInput
import app.permission.persistance.entity.Permission
import app.permission.persistance.entity.PermissionType
import app.permission.persistance.repository.PermissionRepository
import app.permission.persistance.repository.PermissionTypeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@WithMockUser(username = "user", authorities = ["SCOPE_write:snippets"])
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
        // Setup
        val createPermissionRequestBody =
            CreatePermissionInput(
                "001",
                "001",
                "NON_EXISTENT",
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        // Execution
        val result =
            mockMvc.perform(
                post("$base/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andReturn()

        // Assertion
        Assertions.assertEquals(404, result.response.status)
        Assertions.assertEquals("Permission type not found", result.response.errorMessage)
    }

    @Test
    fun `test 002 _ create valid permission`() {
        // Setup
        val createPermissionRequestBody =
            CreatePermissionInput(
                "002",
                "002",
                TEST_PERMISSION_TYPE,
            )

        val requestBody = objectMapper.writeValueAsString(createPermissionRequestBody)

        // Execution
        val result =
            mockMvc.perform(
                post("$base/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andReturn()

        // Assertion
        Assertions.assertEquals(200, result.response.status)
    }

    @Test
    fun `test 003 _ create already existing permission should return 409`() {
        // Setup
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

        // Execution - Assertion
        mockMvc.perform(
            post("$base/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isConflict)
    }

    @Test
    fun `test 004 _ delete all permissions for a snippet`() {
        // Setup
        val snippetId = "006"
        val user1 = snippetId + "_1"
        val user2 = snippetId + "_2"

        val permissionType = permissionTypeRepository.findByType(TEST_PERMISSION_TYPE)

        val permission1 =
            Permission(
                snippetId,
                user1,
                permissionType.get(),
            )

        val permission2 =
            Permission(
                snippetId,
                user2,
                permissionType.get(),
            )

        permissionRepository.saveAll(listOf(permission1, permission2))

        // Execution
        mockMvc.perform(
            delete("$base/all/$snippetId"),
        ).andExpect(status().isOk)

        // Assertion
        val result1 = permissionRepository.findByUserIdAndSnippetId(user1, snippetId)
        val result2 = permissionRepository.findByUserIdAndSnippetId(user2, snippetId)

        Assertions.assertTrue(result1.isEmpty)
        Assertions.assertTrue(result2.isEmpty)
    }
}
