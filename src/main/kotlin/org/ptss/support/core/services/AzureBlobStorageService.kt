package org.ptss.support.core.services


import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.InputStream
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.infrastructure.config.AzureStorageConfig
import org.slf4j.LoggerFactory


@ApplicationScoped
class AzureBlobStorageService @Inject constructor(
    private val azureStorageConfig: AzureStorageConfig
) {
    private val blobServiceClient: BlobServiceClient = BlobServiceClientBuilder()
        .connectionString(azureStorageConfig.connectionString())
        .buildClient()

    private val containerClient: BlobContainerClient = blobServiceClient
        .getBlobContainerClient(azureStorageConfig.containerName())
        .apply {
            if (!exists()) {
                create()
            }
        }

    suspend fun uploadFile(fileStream: InputStream, fileName: String): String = withContext(Dispatchers.IO) {
        try {
            val blobClient = containerClient.getBlobClient(fileName)
            blobClient.upload(fileStream, true)
            blobClient.blobUrl
        } finally {
            try {
                fileStream.close()
            } catch (e: Exception) {
                // Log error but don't throw
                logger.error("Error closing file stream", e)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AzureBlobStorageService::class.java)
    }
}
