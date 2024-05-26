package com.example.postgres.postgresapi.repository;

import com.example.postgres.postgresapi.entity.User;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT) VALUES(NEXTVAL('UM_USERS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";

    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT " +
            "FROM USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT " +
            "FROM USERS WHERE EMAIL = ?";
    private static final String SQL_USER_UPDATE = "UPDATE USERS SET USER_NAME = ?, EMAIL = ?, PASSWORD = ?, CREATED_AT = ?, UPDATED_AT = ? " +
            "WHERE USER_ID = ?";

    private static final String SQL_USER_DELETE = "DELETE FROM USERS WHERE USER_ID = ?";

    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM USERS";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String username, String email, String password, Long createdAt, Long updatedAt) throws WebApplicationException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setLong(4, createdAt);
                ps.setLong(5, updatedAt);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e) {
            throw new WebApplicationException("Failed to create the user.");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws WebApplicationException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if (!password.equals(user.getPassword())) {
                throw new WebApplicationException("Invalid email/password");
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new WebApplicationException("Invalid email/password");
        }
    }

    @Override
    public Integer getEmail(String email) throws WebApplicationException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public Integer findByName(String username) throws WebApplicationException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, new Object[]{username}, Integer.class);
    }

    @Override
    public User findById(Integer id) throws WebApplicationException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, userRowMapper);
        } catch (Exception e) {
            throw new WebApplicationException("User not found.");
        }
    }

    @Override
    public void updateUser(Integer userId, String username, String email, String password) {
        User user = findById(userId);
        try {
            jdbcTemplate.update(SQL_USER_UPDATE, username, email, password, user.getCreatedAt(), System.currentTimeMillis(), userId);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid request.");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            jdbcTemplate.update(SQL_USER_DELETE, userId);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid request.");
        }
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/usermanagementdb",
                "usermanager", "password");
        List<User> userList = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL_USERS);
        ResultSet rs = ps.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("USER_ID"));
                user.setUsername(rs.getString("USER_NAME"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setEmail(rs.getString("EMAIL"));
                user.setCreatedAt(rs.getLong("CREATED_AT"));
                user.setUpdatedAt(rs.getLong("UPDATED_AT"));
                userList.add(user);
            }
            rs.close();
        }
        ps.close();
        return userList;
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> new User(rs.getInt("USER_ID"),
            rs.getString("USER_NAME"),
            rs.getString("PASSWORD"),
            rs.getString("EMAIL"),
            rs.getLong("CREATED_AT"),
            rs.getLong("UPDATED_AT")));
}
