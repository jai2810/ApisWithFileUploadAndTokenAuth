package com.example.postgres.postgresapi.repository;

import java.sql.SQLException;

public interface ImageRepository {
    byte[] findImageByName(String fileName) throws SQLException;

    Integer saveImage(String imageName, Integer userId, String type, byte[] data);

    Integer findImageIdByUserId(Integer userId);

    void updateImage(String imageName, Integer userId, String type, byte[] data);

    public byte[] findImageByUserId(Integer userId) throws SQLException;
}
