package org.ptss.support.infrastructure.handlers.commands.media

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.services.BlobStorageService
import org.ptss.support.domain.commands.media.CreateMediaInfoCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.repositories.MediaInfoRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.util.*

@ApplicationScoped
class CreateMediaInfoCommandHandler(
    private val mediaInfoRepository: MediaInfoRepository,
    private val blobStorageService: BlobStorageService
) : ICommandHandler<CreateMediaInfoCommand, MediaInfo> {
    private val logger = LoggerFactory.getLogger(CreateMediaInfoCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateMediaInfoCommand): MediaInfo {
        return withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    if (mediaInfoRepository.hasExistingMedia(command.toolId)) {
                        throw APIException(
                            errorCode = ErrorCode.MEDIA_CREATION_ERROR,
                            message = "Media already exists for tool ${command.toolId}"
                        )
                    }

                    val blobUrl = blobStorageService.uploadFileAsync(
                        command.fileData ?: throw APIException(
                            errorCode = ErrorCode.MEDIA_NOT_FOUND,
                            message = "File data is required"
                        )
                    )

                    val publicUrl = blobStorageService.getPublicBlobUrl(blobUrl.substringAfterLast("/"))

                    val mediaInfo = MediaInfo(
                        id = UUID.randomUUID().toString(),
                        toolId = command.toolId,
                        url = publicUrl,
                        href = command.href ?: publicUrl
                    )

                    mediaInfoRepository.create(mediaInfo)
                },
                logMessage = "Error uploading media for toolId: ${command.toolId}"
            )
        }
    }
}

