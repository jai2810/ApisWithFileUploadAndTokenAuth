package com.example.postgres.postgresapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageData {

    private String id;

    private String name;

    private String type;

    private byte[] data;
}

