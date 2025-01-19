package org.ptss.support.core.controllers

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import jakarta.ws.rs.core.Response
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ptss.support.api.controllers.CategoryController
import org.ptss.support.api.dtos.requests.categories.CreateCategoryRequest
import org.ptss.support.api.dtos.requests.categories.UpdateCategoryRequest
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.facades.CategoryFacade
import org.ptss.support.domain.enums.ErrorCode
import java.time.Instant
import java.util.*

@QuarkusTest
class CategoryControllerTest {

    private val categoryFacade: CategoryFacade = mockk()
    private val controller = CategoryController(categoryFacade)

    private val mockCategoryResponse = CategoryResponse(
        category = "Sample Category",
        createdAt = Instant.now(),
        groupId = UUID.randomUUID().toString(),
        tools = emptyList() // No tools associated with this category for simplicity
    )

    @Test
    fun `getAllCategories returns categories successfully`() = runTest {
        // Arrange
        coEvery { categoryFacade.getAllCategories() } returns listOf(mockCategoryResponse)

        // Act
        val result = controller.getAllCategories()

        // Assert
        assertEquals(listOf(mockCategoryResponse), result)
        coVerify { categoryFacade.getAllCategories() }
    }

    @Test
    fun `getAllCategories returns empty list when no categories exist`() = runTest {
        // Arrange
        coEvery { categoryFacade.getAllCategories() } returns emptyList()

        // Act
        val result = controller.getAllCategories()

        // Assert
        assertEquals(emptyList<CategoryResponse>(), result)
        coVerify { categoryFacade.getAllCategories() }
    }

    @Test
    fun `createCategory creates a category successfully`() = runTest {
        // Arrange
        val request = CreateCategoryRequest(category = "New Category")
        coEvery { categoryFacade.createCategory(request) } returns mockCategoryResponse

        // Act
        val result = controller.createCategory(request)

        // Assert
        assertEquals(mockCategoryResponse, result)
        coVerify { categoryFacade.createCategory(request) }
    }

    @Test
    fun `deleteCategory deletes a category successfully`() = runTest {
        // Arrange
        val categoryName = "Sample Category"
        coEvery { categoryFacade.deleteCategory(categoryName) } returns Unit

        // Act
        val result = controller.deleteCategory(categoryName)

        // Assert
        assertEquals(Response.noContent().build().status, result.status)
        coVerify { categoryFacade.deleteCategory(categoryName) }
    }

    @Test
    fun `deleteCategory throws exception when category does not exist`() = runTest {
        // Arrange
        val categoryName = "NonExistent Category"
        coEvery { categoryFacade.deleteCategory(categoryName) } throws APIException(
            errorCode = ErrorCode.NOT_FOUND,
            message = "Category not found"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.deleteCategory(categoryName)
        }

        // Verify exception message
        assertEquals("Category not found", exception.message)
        coVerify { categoryFacade.deleteCategory(categoryName) }
    }

    @Test
    fun `deleteCategory throws exception for invalid category name`() = runTest {
        // Arrange
        val invalidCategoryName = "Invalid Category"
        coEvery { categoryFacade.deleteCategory(invalidCategoryName) } throws APIException(
            errorCode = ErrorCode.NOT_FOUND,
            message = "Category does not exist"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.deleteCategory(invalidCategoryName)
        }

        // Verify exception message
        assertEquals("Category does not exist", exception.message)
        coVerify { categoryFacade.deleteCategory(invalidCategoryName) }
    }

    @Test
    fun `updateCategory updates a category successfully`() = runTest {
        // Arrange
        val categoryName = "Sample Category"
        val updateRequest = UpdateCategoryRequest(category = "Updated Category")
        val updatedCategoryResponse = mockCategoryResponse.copy(category = "Updated Category")
        coEvery { categoryFacade.updateCategory(categoryName, updateRequest) } returns updatedCategoryResponse

        // Act
        val result = controller.updateCategory(categoryName, updateRequest)

        // Assert
        assertEquals(updatedCategoryResponse, result)
        coVerify { categoryFacade.updateCategory(categoryName, updateRequest) }
    }

    @Test
    fun `updateCategory throws exception for non-existent category`() = runTest {
        // Arrange
        val categoryName = "NonExistent Category"
        val updateRequest = UpdateCategoryRequest(category = "Updated Category")
        coEvery { categoryFacade.updateCategory(categoryName, updateRequest) } throws APIException(
            errorCode = ErrorCode.NOT_FOUND,
            message = "Category not found"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.updateCategory(categoryName, updateRequest)
        }

        // Verify exception message
        assertEquals("Category not found", exception.message)
        coVerify { categoryFacade.updateCategory(categoryName, updateRequest) }
    }

    @Test
    fun `updateCategory throws exception for invalid update request`() = runTest {
        // Arrange
        val invalidCategoryName = "NonExistent Category"
        val updateRequest = UpdateCategoryRequest(category = "New Name")
        coEvery { categoryFacade.updateCategory(invalidCategoryName, updateRequest) } throws APIException(
            errorCode = ErrorCode.NOT_FOUND,
            message = "Category not found"
        )

        // Act & Assert
        val exception = assertThrows<APIException> {
            controller.updateCategory(invalidCategoryName, updateRequest)
        }

        // Verify exception message
        assertEquals("Category not found", exception.message)
        coVerify { categoryFacade.updateCategory(invalidCategoryName, updateRequest) }
    }
}
