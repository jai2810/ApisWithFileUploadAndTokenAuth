package com.example.postgres.postgresapi.repository;

import java.sql.SQLException;

public interface ImageRepository {
    byte[] findByName(String fileName) throws SQLException;

    Integer save(String imageName, Integer userId, String type, byte[] data);
}
