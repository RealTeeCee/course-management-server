package com.aptech.coursemanagementserver.services.azureServices;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;

public interface AzureService {
    public void uploadFile(MultipartFile file, String folder, String uniqueName) throws IOException;

    public void downloadBlobFromAzure(OutputStream outputStream,
            String blobName);

    public BlobClient getBlob(String blobName);

}
