package org.ptss.support.core.services


import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.tika.Tika
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.infrastructure.repositories.BlobStorageRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.*


@ApplicationScoped
class BlobStorageService @Inject constructor(
    private val blobStorageRepository: BlobStorageRepository
) {
    private val logger = LoggerFactory.getLogger(BlobStorageService::class.java)
    private val tika = Tika()

    suspend fun uploadFileToBlobAsync(fileStream: InputStream): String {
        return fileStream.use { stream ->
            val bufferedStream = stream.buffered()
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val fileType = detectFileType(bufferedStream)
                    val fileName = generateFileName(fileType)
                    blobStorageRepository.uploadFileToBlobStorage(bufferedStream, fileName)
                },
                logMessage = "Error uploading file to Azure Blob Storage",
                exceptionHandling = {
                    APIException(
                        errorCode = ErrorCode.MEDIA_CREATION_ERROR,
                        message = "Failed to upload file to Azure Blob Storage"
                    )
                }
            )
        }
    }

    suspend fun deleteFileFromBlobAsync(blobName: String) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                blobStorageRepository.deleteFileFromBlob(blobName)
            },
            logMessage = "Error deleting blob $blobName",
            exceptionHandling = {
                APIException(
                    errorCode = ErrorCode.MEDIA_DELETION_ERROR,
                    message = "Failed to delete blob $blobName"
                )
            }
        )
    }

    private fun detectFileType(fileStream: InputStream): String {
        val bufferedStream = fileStream.buffered()
        bufferedStream.mark(Int.MAX_VALUE)

        val fileType = tika.detect(bufferedStream)
        bufferedStream.reset()

        return when (fileType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "image/webp" -> ".webp"
            "video/mp4" -> ".mp4"
            "video/mpeg" -> ".mpeg"
            "video/quicktime" -> ".mov"
            "video/x-matroska" -> ".mkv"
            "application/pdf" -> ".pdf"
            else -> throw APIException(
                errorCode = ErrorCode.MEDIA_CREATION_ERROR,
                message = "Unsupported file type: $fileType"
            )
        }
    }

    private fun generateFileName(fileType: String): String {
        return "${UUID.randomUUID()}$fileType"
    }
}


