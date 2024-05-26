package com.example.postgres.postgresapi.resources;

import com.example.postgres.postgresapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/image")
public class ImageResource {
    @Autowired
    ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> upload(@RequestParam("user_id") Integer userId, @RequestParam("image") MultipartFile file) throws IOException {
        imageService.uploadImage(userId, file);
        Map<String,String> map = new HashMap<>();
        map.put("message", "Image uploaded successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws SQLException {
        byte[] imageData = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> downloadImageByUserId(@PathVariable Integer userId) throws SQLException {
        byte[] imageData = imageService.downloadImageByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
