package org.ptss.support.core.controllers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import jakarta.ws.rs.core.Response
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ptss.support.api.controllers.ToolController
import org.ptss.support.api.dtos.requests.tools.CreateToolRequest
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.api.dtos.responses.tools.ToolResponse
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.facades.ToolFacade
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.SortOrder
import java.time.Instant
import java.util.*

@QuarkusTest
class ToolControllerTest {
    private val toolFacade: ToolFacade = mockk()
    private val controller = ToolController(toolFacade)

    private val mockToolResponse = ToolResponse(
        id = UUID.randomUUID().toString(),
        name = "Sample Tool",
        description = "A useful tool",
        createdBy = "test_user",
        createdAt = Instant.now(),
        media = null
    )

    @Test
    fun `getAllTools returns paginated list successfully`() = runTest {
        // Arrange
        val mockPaginationResponse = PaginationResponse(
            data = listOf(mockToolResponse),
            nextCursor = "nextCursor",
            pageSize = 10,
            totalItems = 1,
            totalPages = 1
        )
        coEvery { toolFacade.getAllTools(any(), any(), SortOrder.DESC) } returns mockPaginationResponse

        // Act
        val result = controller.getAllTools("cursor", 10, "desc")

        // Assert
        assertEquals(mockPaginationResponse, result)
        coVerify { toolFacade.getAllTools("cursor", 10, SortOrder.DESC) }
    }

    @Test
    fun `getAllTools throws exception for invalid sortOrder`() = runTest {
        // Arrange
        val invalidSortOrder = "invalid_order"

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.getAllTools("cursor", 10, invalidSortOrder)
        }

        // Verify exception message
        assertEquals("Invalid sort order $invalidSortOrder", exception.message)
        coVerify(exactly = 0) { toolFacade.getAllTools(any(), any(), any()) }
    }

    @Test
    fun `getAllTools returns empty list when no tools are available`() = runTest {
        // Arrange
        val emptyPaginationResponse = PaginationResponse<ToolResponse>(
            data = emptyList(),
            nextCursor = null,
            pageSize = 10,
            totalItems = 0,
            totalPages = 0
        )
        coEvery { toolFacade.getAllTools(any(), any(), SortOrder.DESC) } returns emptyPaginationResponse

        // Act
        val result = controller.getAllTools("cursor", 10, "desc")

        // Assert
        assertEquals(emptyPaginationResponse, result)
        coVerify { toolFacade.getAllTools("cursor", 10, SortOrder.DESC) }
    }

    @Test
    fun `getToolById returns tool successfully`() = runTest {
        // Arrange
        coEvery { toolFacade.getToolById(any()) } returns mockToolResponse

        // Act
        val result = controller.getToolById(mockToolResponse.id)

        // Assert
        assertEquals(mockToolResponse, result)
        coVerify { toolFacade.getToolById(mockToolResponse.id) }
    }

    @Test
    fun `getToolById throws exception for non-existent tool`() = runTest {
        // Arrange
        coEvery { toolFacade.getToolById(any()) } returns null

        // Act & Assert
        val result = controller.getToolById(UUID.randomUUID().toString())
        assertEquals(null, result)
    }

    @Test
    fun `getToolById returns null when tool does not exist`() = runTest {
        // Arrange
        val nonExistentToolId = UUID.randomUUID().toString()
        coEvery { toolFacade.getToolById(nonExistentToolId) } returns null

        // Act
        val result = controller.getToolById(nonExistentToolId)

        // Assert
        assertEquals(null, result)
        coVerify { toolFacade.getToolById(nonExistentToolId) }
    }

    @Test
    fun `createTool creates a tool successfully`() = runTest {
        // Arrange
        val request = CreateToolRequest(
            name = "New Tool",
            description = "New tool description",
            category = listOf("Category1")
        )
        coEvery { toolFacade.createTool(request) } returns mockToolResponse

        // Act
        val result = controller.createTool(request)

        // Assert
        assertEquals(mockToolResponse, result)
        coVerify { toolFacade.createTool(request) }
    }

    @Test
    fun `createTool throws exception for invalid input`() = runTest {
        // Arrange
        val invalidRequest = CreateToolRequest(
            name = "", // Empty name should trigger validation error
            description = "Invalid tool",
            category = listOf("Category1")
        )

        // Mock the exception to be thrown from the facade method
        coEvery { toolFacade.createTool(invalidRequest) } throws APIException(
            errorCode = ErrorCode.VALIDATION_ERROR,
            message = "name must not be empty"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.createTool(invalidRequest)
        }

        // Verify the exception message (contains validation error related to empty name)
        assertTrue(exception.message?.contains("name must not be empty") == true)
        coVerify(exactly = 1) { toolFacade.createTool(invalidRequest) }
    }

    @Test
    fun `createTool throws exception when required fields are missing`() = runTest {
        // Arrange
        val invalidRequest = CreateToolRequest(
            name = "", // Empty name
            description = "", // Empty description
            category = listOf() // Empty category list
        )

        // Mock the exception to be thrown from the facade method
        coEvery { toolFacade.createTool(invalidRequest) } throws APIException(
            errorCode = ErrorCode.VALIDATION_ERROR,
            message = "Name, description, and category are required fields."
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.createTool(invalidRequest)
        }

        // Verify the exception message
        assertTrue(exception.message?.contains("Name, description, and category are required fields.") == true)
        coVerify(exactly = 1) { toolFacade.createTool(invalidRequest) }
    }


    @Test
    fun `deleteTool deletes a tool successfully`() = runTest {
        // Arrange
        val toolId = UUID.randomUUID().toString()
        coEvery { toolFacade.deleteTool(toolId) } returns Unit

        // Act
        val result = controller.deleteTool(toolId)

        // Assert
        assertEquals(Response.noContent().build().status, result.status)
        coVerify { toolFacade.deleteTool(toolId) }
    }

    @Test
    fun `deleteTool throws internal error on unexpected exception`() = runTest {
        // Arrange
        val toolId = UUID.randomUUID().toString()
        coEvery { toolFacade.deleteTool(toolId) } throws RuntimeException("Unexpected error")

        // Act & Assert
        val exception = assertThrows<RuntimeException> {
            controller.deleteTool(toolId)
        }

        // Verify exception message
        assertEquals("Unexpected error", exception.message)
        coVerify { toolFacade.deleteTool(toolId) }
    }

    @Test
    fun `deleteTool throws exception when tool does not exist`() = runTest {
        // Arrange
        val nonExistentToolId = UUID.randomUUID().toString()

        // Mock the behavior of toolFacade to throw an exception when trying to delete a non-existent tool
        coEvery { toolFacade.deleteTool(nonExistentToolId) } throws APIException(
            errorCode = ErrorCode.NOT_FOUND,
            message = "Tool not found"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.deleteTool(nonExistentToolId)
        }

        // Verify the exception message
        assertEquals("Tool not found", exception.message)
        coVerify { toolFacade.deleteTool(nonExistentToolId) }
    }

}

