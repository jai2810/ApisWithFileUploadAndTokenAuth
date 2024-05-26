package com.example.postgres.postgresapi.resources;

import com.example.postgres.postgresapi.Constants;
import com.example.postgres.postgresapi.entity.User;
import com.example.postgres.postgresapi.exceptions.WebApplicationException;
import com.example.postgres.postgresapi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        User user = userService.validateUser(email, password);
        Map<String, String> map = new HashMap<>();
        map.put("token", generateJWTToken(user));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);
        } catch (WebApplicationException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser
            (@RequestParam("name") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
        User user = userService.registerUser(username, email, password, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer userId,
            @RequestParam(value = "name", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password) {
        userService.updateUser(userId, username, email, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    private String generateJWTToken(User user) {
        long timeStamp = System.currentTimeMillis();
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timeStamp))
                .setExpiration(new Date(timeStamp + Constants.TOKEN_VALIDITY))
                .claim("user_id", user.getUserid())
                .claim("email", user.getEmail())
                .claim("user_name", user.getUsername())
                .compact();
    }
}
