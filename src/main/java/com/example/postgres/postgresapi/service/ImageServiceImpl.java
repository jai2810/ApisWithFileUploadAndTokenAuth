package com.example.postgres.postgresapi.service;

import com.example.postgres.postgresapi.entity.ImageData;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import com.example.postgres.postgresapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    ImageRepository imageRepository;
    @Override
    public Integer uploadImage(Integer userId, MultipartFile file) throws IOException {
        Integer imageId = imageRepository.save(file.getOriginalFilename(), userId, file.getContentType(), file.getBytes());
        return imageId;
    }

    @Override
    public byte[] downloadImage(String fileName) throws SQLException {
        return imageRepository.findByName(fileName);
    }
}
