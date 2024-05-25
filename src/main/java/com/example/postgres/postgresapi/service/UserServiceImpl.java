package com.example.postgres.postgresapi.service;

import com.example.postgres.postgresapi.entity.User;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import com.example.postgres.postgresapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageService imageService;
    @Override
    public User validateUser(String email, String password) throws WebApplicationException {
        if (email != null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String username, String email, String password, MultipartFile file) throws WebApplicationException, IOException {
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null) email = email.toLowerCase();
        if (!emailPattern.matcher(email).matches()) {
            throw new WebApplicationException("Invalid email format.");
        }
        Integer emailCount = userRepository.getEmail(email);
        if (emailCount > 0) {
            throw new WebApplicationException("Email already registered.");
        }

        Integer userNameCount = userRepository.findByName(username);
        if (userNameCount > 0) {
            throw new WebApplicationException("Username already registered.");
        }

        Integer userId = userRepository.create(username, email, password, System.currentTimeMillis(), System.currentTimeMillis());
        if (file != null) {
            imageService.uploadImage(userId, file);
        }
        return userRepository.findById(userId);
    }
}
