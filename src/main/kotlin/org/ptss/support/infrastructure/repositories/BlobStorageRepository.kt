package org.ptss.support.infrastructure.repositories

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.IBlobStorageRepository
import org.ptss.support.infrastructure.config.AzureStorageConfig
import java.io.InputStream

@ApplicationScoped
class BlobStorageRepository(private val azureStorageConfig: AzureStorageConfig) : IBlobStorageRepository {

    private val blobServiceClient: BlobServiceClient = BlobServiceClientBuilder()
        .connectionString(azureStorageConfig.connectionString())
        .buildClient()

    private val containerClient: BlobContainerClient = blobServiceClient
        .getBlobContainerClient(azureStorageConfig.containerName())
        .apply { if (!exists()) create() }

    override suspend fun uploadFileToBlobStorage(fileStream: InputStream, fileName: String): String {
        val blobClient = containerClient.getBlobClient(fileName)
        blobClient.upload(fileStream, true)
        return blobClient.blobUrl
    }

    override suspend fun deleteFileFromBlob(blobName: String) {
        val blobClient = containerClient.getBlobClient(blobName)
        blobClient.delete()
    }
}