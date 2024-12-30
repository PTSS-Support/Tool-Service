package org.ptss.support.infrastructure.handlers.commands.media

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.services.AzureBlobStorageService
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
    private val blobStorageService: AzureBlobStorageService
) : ICommandHandler<CreateMediaInfoCommand, MediaInfo> {
    private val logger = LoggerFactory.getLogger(CreateMediaInfoCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateMediaInfoCommand): MediaInfo {
        return withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    if (command.fileData == null) {
                        throw BadRequestException("File data is required")
                    }

                    if (mediaInfoRepository.hasExistingMedia(command.toolId)) {
                        throw APIException(
                            errorCode = ErrorCode.MEDIA_CREATION_ERROR,
                            message = "Media already exists for tool ${command.toolId}"
                        )
                    }

                    val fileName = "${UUID.randomUUID()}-${System.currentTimeMillis()}"
                    val url = blobStorageService.uploadFile(command.fileData, fileName)
                    val mediaInfo = MediaInfo(
                        id = UUID.randomUUID().toString(),
                        toolId = command.toolId,
                        url = url,
                        href = command.href ?: url
                    )

                    mediaInfoRepository.create(mediaInfo)
                },
                logMessage = "Error uploading media for toolId: ${command.toolId}"
            )
        }
    }
}
