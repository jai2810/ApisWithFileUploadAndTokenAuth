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

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT) VALUES(NEXTVAL('UM_USERS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";

    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT " +
            "FROM USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD, CREATED_AT, UPDATED_AT " +
            "FROM USERS WHERE EMAIL = ?";

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
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, userRowMapper);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> new User(rs.getInt("USER_ID"),
            rs.getString("USER_NAME"),
            rs.getString("PASSWORD"),
            rs.getString("EMAIL"),
            rs.getLong("CREATED_AT"),
            rs.getLong("UPDATED_AT")));
}
