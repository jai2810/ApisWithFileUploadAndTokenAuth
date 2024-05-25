package com.example.postgres.postgresapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @JsonProperty("user_id")
    private Integer userid;

    @JsonProperty("user_name")
    private String username;

    private String password;

    private String email;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    public User(int userId, String userName, String password, String email, long createdAt, long updatedAt) {
        this.userid = userId;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
