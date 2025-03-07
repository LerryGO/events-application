package com.lapdev.eventos_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
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
        String encodedFileName = fileName.replaceAll(" ", "%20");
        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + encodedFileName;
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

    // Faz o teste para verificar se está configurado corretamente e imprime a lista de buckets
    public void s3ListBucketsTest(){
        S3Client s3 = S3Client.create();
        ListBucketsResponse response = s3.listBuckets();
        response.buckets().forEach(bucket ->
                System.out.println("Bucket: " + bucket.name())
        );
    }
}

