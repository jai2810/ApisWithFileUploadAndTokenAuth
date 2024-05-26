package com.example.postgres.postgresapi.repository;

import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ImageRepositoryImpl implements ImageRepository{

    private static final String SQL_STORE_IMAGE = "INSERT INTO IMAGE_DATA(IMAGE_ID, IMAGE_NAME, USER_ID, TYPE, DATA) VALUES(NEXTVAL('UM_IMAGE_DATA_SEQ'), ?, ?, ?, ?)";

    private static final String SQL_IMAGE_DATA = "SELECT DATA FROM IMAGE_DATA WHERE IMAGE_NAME = ?";

    private static final String SQL_IMAGE_DATA_FROM_USER_ID = "SELECT DATA FROM IMAGE_DATA WHERE USER_ID = ?";

    private static final String SQL_FIND_IMAGE_ID_BY_USER_ID = "SELECT IMAGE_ID FROM IMAGE_DATA WHERE USER_ID = ?";

    private static final String SQL_UPDATE_IMAGE = "UPDATE IMAGE_DATA SET IMAGE_NAME = ?, TYPE = ?, DATA = ? " +
            "WHERE USER_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public byte[] findImageByName(String fileName) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/usermanagementdb",
                "usermanager", "password");
        PreparedStatement ps = con.prepareStatement(SQL_IMAGE_DATA);
        ps.setString(1, fileName);
        ResultSet rs = ps.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                return imgBytes;
            }
            rs.close();
        }
        ps.close();
        return new byte[0];
    }

    @Override
    public byte[] findImageByUserId(Integer userId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/usermanagementdb",
                "usermanager", "password");
        PreparedStatement ps = con.prepareStatement(SQL_IMAGE_DATA_FROM_USER_ID);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                return imgBytes;
            }
            rs.close();
        }
        ps.close();
        return new byte[0];
    }

    @Override
    public Integer saveImage(String imageName, Integer userId, String type, byte[] data) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_STORE_IMAGE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, imageName);
                ps.setInt(2, userId);
                ps.setString(3, type);
                ps.setBytes(4, data);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("IMAGE_ID");
        } catch (Exception e) {
            throw new WebApplicationException("Failed to upload the image.");
        }
    }

    @Override
    public Integer findImageIdByUserId(Integer userId) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_IMAGE_ID_BY_USER_ID, new Object[]{userId}, Integer.class);
        } catch (Exception e) {
            throw new WebApplicationException("Image not found.");
        }
    }

    @Override
    public void updateImage(String imageName, Integer userId, String type, byte[] data) {
        try {
            jdbcTemplate.update(SQL_UPDATE_IMAGE, imageName, type, data, userId);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid request.");
        }
    }
}
