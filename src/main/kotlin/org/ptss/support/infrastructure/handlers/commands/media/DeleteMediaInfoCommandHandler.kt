package org.ptss.support.infrastructure.handlers.commands.media

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.services.AzureBlobStorageService
import org.ptss.support.domain.commands.media.DeleteMediaInfoCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.infrastructure.repositories.MediaInfoRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class DeleteMediaInfoCommandHandler(
    private val mediaInfoRepository: MediaInfoRepository,
    private val blobStorageService: AzureBlobStorageService
) : ICommandHandler<DeleteMediaInfoCommand, Unit> {

    private val logger = LoggerFactory.getLogger(DeleteMediaInfoCommandHandler::class.java)

    override suspend fun handleAsync(command: DeleteMediaInfoCommand) {
        withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    // Delete media info and return the associated URL
                    val mediaUrl = mediaInfoRepository.delete(command.toolId, command.id)
                        ?: throw APIException(
                            errorCode = ErrorCode.MEDIA_NOT_FOUND,
                            message = "Media with ID ${command.id} not found for tool ${command.toolId}"
                        )

                    // Extract blob name from the URL and delete the blob
                    val blobName = extractBlobName(mediaUrl.url)
                    blobStorageService.deleteFile(blobName)
                },
                logMessage = "Error deleting media ${command.id} for tool ${command.toolId}"
            )
        }
    }

    private fun extractBlobName(url: String): String {
        // Logic to extract blob name from URL (implementation depends on Azure URL format)
        return url.substringAfterLast("/")
    }
}