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

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public byte[] findByName(String fileName) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/usermanagementdb",
                "usermanager", "password");
        PreparedStatement ps = con.prepareStatement("SELECT DATA FROM IMAGE_DATA WHERE IMAGE_NAME = ?");
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
        //return jdbcTemplate.queryForObject(SQL_IMAGE_DATA, new Object[]{fileName}, byte.class);
        return new byte[0];
    }

    @Override
    public Integer save(String imageName, Integer userId, String type, byte[] data) {
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
}
