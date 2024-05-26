package com.example.postgres.postgresapi.service;

import com.example.postgres.postgresapi.entity.User;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User validateUser(String email, String password) throws WebApplicationException;

    User registerUser(String username, String email, String password, MultipartFile file) throws WebApplicationException, IOException;

    User getUserById(Integer userId);

    void updateUser(Integer userId, String username, String email, String password);

    void deleteUser(Integer userId);

    List<User> getAllUsers() throws SQLException;
}

