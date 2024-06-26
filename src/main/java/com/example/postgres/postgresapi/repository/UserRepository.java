package com.example.postgres.postgresapi.repository;

import com.example.postgres.postgresapi.entity.User;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    Integer create(String username, String email, String password, Long createdAt, Long updatedAt)
            throws WebApplicationException;

    User findByEmailAndPassword(String email, String password) throws WebApplicationException;

    Integer getEmail(String email) throws WebApplicationException;

    Integer findByName (String username) throws WebApplicationException;

    User findById(Integer id) throws WebApplicationException;

    void updateUser(Integer userId, String username, String email, String password);

    void deleteUser(Integer userId);

    List<User> findAllUsers() throws SQLException;
}
