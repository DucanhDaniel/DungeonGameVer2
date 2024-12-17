package com.backend.controller;

import com.backend.dto.LoginDTO;
import com.backend.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final String AUTH_FILE_PATH = "src/main/resources/auth.json";

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            // Đọc dữ liệu từ auth.json
            ObjectMapper mapper = new ObjectMapper();
            List<User> Users = mapper.readValue(new File(AUTH_FILE_PATH), new TypeReference<>() {});

            // Kiểm tra thông tin đăng nhập
            for (User User : Users) {
                if (User.getUsername().equals(username) && User.getPassword().equals(password)) {
                    return "Login successful";
                }
            }
        } catch (IOException e) {
            return "Error reading auth file: " + e.getMessage();
        }

        return "Invalid credentials";
    }
}
