package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.media.CreateMediaInfoCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class MediaInfoService(
    private val createMediaHandler: ICommandHandler<CreateMediaInfoCommand, MediaInfo>,
) {
    private val logger = LoggerFactory.getLogger(MediaInfoService::class.java)

    private val maxFileSize = 10L * 1024 * 1024 // 10MB

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

    private suspend fun validateMediaCommand(command: CreateMediaInfoCommand) {
        requireNotNull(command.fileData) { "File data cannot be null" }

        // Validate file size
        val fileSize = command.fileData.available().toLong()
        require(fileSize <= maxFileSize) {
            "File size exceeds maximum allowed size of ${maxFileSize / (1024 * 1024)}MB"
        }
    }
}