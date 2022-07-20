package com.example.fever_final.util;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface IUpload {
    String uploadFile(InputStream inputStream,
                      ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

}
