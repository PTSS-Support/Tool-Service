package org.ptss.support.infrastructure.repositories

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.BlobHttpHeaders
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.IBlobStorageRepository
import org.ptss.support.infrastructure.config.AzureStorageConfig
import org.ptss.support.infrastructure.util.retryWithExponentialBackoff
import java.io.InputStream
import java.time.OffsetDateTime

@ApplicationScoped
class BlobStorageRepository(private val azureStorageConfig: AzureStorageConfig) : IBlobStorageRepository {

    private val blobServiceClient: BlobServiceClient = BlobServiceClientBuilder()
        .connectionString(azureStorageConfig.connectionString())
        .buildClient()

    private val containerClient: BlobContainerClient = blobServiceClient
        .getBlobContainerClient(azureStorageConfig.containerName())
        .apply { if (!exists()) create() }

    override suspend fun uploadFile(fileName: String, fileData: InputStream, contentType: String): String {
        val blobClient = containerClient.getBlobClient(fileName)

        blobClient.upload(fileData, true)

        val headers = BlobHttpHeaders().setContentType(contentType)
        blobClient.setHttpHeaders(headers)

        return blobClient.blobUrl
    }

    override suspend fun deleteFile(blobName: String) {
        val blobClient = containerClient.getBlobClient(blobName)
        blobClient.delete()
    }


    suspend fun getBlobUrlWithSasToken(blobName: String, expiryMinutes: Long = 60): String {
        val blobClient = containerClient.getBlobClient(blobName)

        val sasPermission = BlobSasPermission()
            .setReadPermission(true)
            .setWritePermission(true)
            .setDeletePermission(true)

        val sasValues = BlobServiceSasSignatureValues(
            OffsetDateTime.now().plusMinutes(expiryMinutes),
            sasPermission
        )

        return retryWithExponentialBackoff {
            val sasToken = blobClient.generateSas(sasValues)
            "${blobClient.blobUrl}?$sasToken"
        }
    }
}