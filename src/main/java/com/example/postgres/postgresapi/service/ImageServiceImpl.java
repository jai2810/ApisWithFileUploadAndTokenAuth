package com.example.postgres.postgresapi.service;

import com.example.postgres.postgresapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    ImageRepository imageRepository;
    @Override
    public Integer uploadImage(Integer userId, MultipartFile file) throws IOException {
        Integer imageId = imageRepository.saveImage(file.getOriginalFilename(), userId, file.getContentType(), file.getBytes());
        return imageId;
    }

    @Override
    public byte[] downloadImage(String fileName) throws SQLException {
        return imageRepository.findImageByName(fileName);
    }

    @Override
    public byte[] downloadImageByUserId(Integer userId) throws SQLException {
        return imageRepository.findImageByUserId(userId);
    }

    @Override
    public void updateImage(Integer userId, MultipartFile file) throws IOException {
        Integer imageId = imageRepository.findImageIdByUserId(userId);
        imageRepository.updateImage(file.getOriginalFilename(), userId, file.getContentType(), file.getBytes());
    }
}
