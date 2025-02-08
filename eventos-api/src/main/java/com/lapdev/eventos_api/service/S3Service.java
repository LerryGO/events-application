package com.lapdev.eventos_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${aws.region}")
    private String awsRegion;

    public S3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }


    // Retorna uma URL pública (apenas se o bucket permitir acesso público)
    public String getPublicUrl(String bucketName, String fileName) {
        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + fileName;
    }


     // Retorna uma URL assinada (para acesso temporário a arquivos privados)

    public String getPresignedUrl(String bucketName, String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // URL válida por 10 minutos
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}

