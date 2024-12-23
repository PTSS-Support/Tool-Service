package org.ptss.support.infrastructure.handlers.commands.media

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.media.UploadMediaCommand
import org.ptss.support.domain.enums.MediaType
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.repositories.MediaRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.UUID

@ApplicationScoped
class UploadMediaCommandHandler(
    private val mediaRepository: MediaRepository
) : ICommandHandler<UploadMediaCommand, MediaInfo> {

    private val logger = LoggerFactory.getLogger(UploadMediaCommandHandler::class.java)

    override suspend fun handleAsync(command: UploadMediaCommand): MediaInfo {
        return withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val mediaInfo = MediaInfo(
                        id = UUID.randomUUID().toString(),
                        url = generateMediaUrl(command.fileData),
                        type = determineMediaType(command.fileData),
                        toolId = command.toolId
                    )
                    mediaRepository.upload(mediaInfo)
                    mediaInfo
                },
                logMessage = "Error uploading media for toolId: ${command.toolId}"
            )
        }
    }

    private fun generateMediaUrl(fileData: InputStream?): String {
        // Replace with actual logic to generate the media's URL, such as an upload to a storage service.
        return "https://www.example.com/media/${UUID.randomUUID()}"
    }

    private fun determineMediaType(fileData: InputStream?): MediaType {
        // Placeholder for determining media type based on file content or metadata.
        return MediaType.IMAGE // Example: Replace with actual logic
    }
}
