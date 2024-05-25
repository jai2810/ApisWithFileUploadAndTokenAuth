package com.example.postgres.postgresapi.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageService {
    Integer uploadImage(Integer userId, MultipartFile file) throws IOException;

    byte[] downloadImage(String filename) throws SQLException;
}
