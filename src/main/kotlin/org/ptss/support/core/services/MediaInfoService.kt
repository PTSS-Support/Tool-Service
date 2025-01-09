package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.media.CreateMediaInfoCommand
import org.ptss.support.domain.commands.media.DeleteMediaInfoCommand
import org.ptss.support.domain.constants.FileConstants
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class MediaInfoService(
    private val createMediaHandler: ICommandHandler<CreateMediaInfoCommand, MediaInfo>,
    private val deleteMediaInfoHandler : ICommandHandler<DeleteMediaInfoCommand, Unit>
) {
    private val logger = LoggerFactory.getLogger(MediaInfoService::class.java)

    suspend fun createMediaInfoAsync(command: CreateMediaInfoCommand): MediaInfo {
        validateMediaCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { createMediaHandler.handleAsync(command) },
            logMessage = "Error uploading media for tool ${command.toolId}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.MEDIA_CREATION_ERROR,
                    message = "Failed to upload media for tool ${command.toolId}",
                )
            }
        )
    }

    suspend fun deleteMediaInfoAsync(toolId: String, mediaId: String) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                deleteMediaInfoHandler.handleAsync(DeleteMediaInfoCommand(toolId, mediaId))
            },
            logMessage = "Error deleting media $mediaId for tool $toolId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.MEDIA_DELETION_ERROR,
                        message = "Unable to delete media with ID $mediaId for tool $toolId",
                    )
                }
            }
        )
    }

    private suspend fun validateMediaCommand(command: CreateMediaInfoCommand) {
        val fileSize = command.fileData?.available()?.toLong() ?: 0

        if (fileSize > FileConstants.MAX_FILE_SIZE) {
            throw APIException(
                errorCode = ErrorCode.FILE_SIZE_EXCEEDED,
                message = "File size exceeds the maximum allowed size of ${FileConstants.MAX_FILE_SIZE / (1024 * 1024)}MB"
            )
        }
    }
}