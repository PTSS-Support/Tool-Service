package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.comments.CreateCommentCommand
import org.ptss.support.domain.commands.media.UploadMediaCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class MediaService(
    private val uploadMediaHandler: ICommandHandler<UploadMediaCommand, MediaInfo>,
) {
    private val logger = LoggerFactory.getLogger(MediaService::class.java)

    suspend fun uploadMediaAsync(command: UploadMediaCommand): MediaInfo {
        //validateMediaCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { uploadMediaHandler.handleAsync(command) },
            logMessage = "Error uploading media for tool ${command.toolId}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.COMMENT_CREATION_ERROR,
                    message = "Failed to upload media for tool ${command.toolId}",
                )
            }
        )
    }
}