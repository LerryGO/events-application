package com.lapdev.eventos_api.service;

import com.lapdev.eventos_api.domain.events.Event;
import com.lapdev.eventos_api.domain.events.EventRequestDTO;
import com.lapdev.eventos_api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EventRepository repository;

    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if(data.image() != null){
           imgUrl =  this.uploadImg(data.image());
            System.out.println("Url imagem: " + imgUrl);
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        repository.save(newEvent);
        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            file.delete();
            return s3Service.getPublicUrl (bucketName,fileName);
        }catch (Exception e){
            System.out.println("Erro ao subir arquivo");
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}
