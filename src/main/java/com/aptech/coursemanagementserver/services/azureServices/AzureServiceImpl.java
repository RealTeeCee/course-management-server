package com.aptech.coursemanagementserver.services.azureServices;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobRange;
import com.azure.storage.blob.models.DownloadRetryOptions;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.*;

@Service
public class AzureServiceImpl implements AzureService {

        @Value("${azure.storage.connection.string}")
        private String connectionString;

        @Value("${azure.storage.container.name}")
        private String containerName;

        BlobServiceClient blobServiceClient;

        BlobContainerClient blobContainerClient;

        @Override
        public void uploadFile(MultipartFile file, String folder, String uniqueName) throws IOException {
                blobServiceClient = new BlobServiceClientBuilder()
                                .connectionString(connectionString)
                                .buildClient();
                blobContainerClient = blobServiceClient
                                .getBlobContainerClient(containerName);

                BlobClient blob = blobContainerClient
                                .getBlobClient(Paths.get(folder, folder == VIDEO_FOLDER ? uniqueName
                                                : file.getOriginalFilename()).toString());

                blob.upload(file.getInputStream(),
                                file.getSize(), true);

        }

        @Override
        public void downloadBlobFromAzure(OutputStream outputStream, String blobName) {
                blobServiceClient = new BlobServiceClientBuilder()
                                .connectionString(connectionString)
                                .buildClient();
                blobContainerClient = blobServiceClient
                                .getBlobContainerClient(containerName);

                BlobClient blob = blobContainerClient
                                .getBlobClient(blobName);

                blob.downloadStreamWithResponse(outputStream, null, new DownloadRetryOptions().setMaxRetryRequests(5),
                                null, false, null, Context.NONE);

        }

        @Override
        public BlobClient getBlob(String blobName) {
                blobServiceClient = new BlobServiceClientBuilder()
                                .connectionString(connectionString)
                                .buildClient();
                blobContainerClient = blobServiceClient
                                .getBlobContainerClient(containerName);

                BlobClient blob = blobContainerClient
                                .getBlobClient(Paths.get("videos", blobName).toString());
                return blob;
        }

}
