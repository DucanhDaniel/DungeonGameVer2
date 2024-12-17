package com.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class FileService {
    private static final String BASE_PATH = "src/main/resources/users/";
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> getUserSaveData(String username) throws IOException {
        File userFolder = Paths.get(BASE_PATH, username).toFile();

        if (!userFolder.exists() || !userFolder.isDirectory()) {
            throw new IllegalArgumentException("User folder not found: " + username);
        }

        // Đường dẫn tới file save.json
        File saveFile = new File(userFolder, "save.json");
        if (!saveFile.exists() || !saveFile.isFile()) {
            throw new IllegalArgumentException("Save file not found for user: " + username);
        }

        // Đọc file save.json và chuyển đổi sang Map
        return mapper.readValue(saveFile, Map.class);
    }

    public void saveUserData(String username, Map<String, Object> saveData) throws IOException {
        File userFolder = Paths.get(BASE_PATH, username).toFile();
        if (!userFolder.exists()) {
            boolean created = userFolder.mkdirs(); // Tạo folder nếu chưa tồn tại
            if (!created) {
                throw new IOException("Unable to create folder for user: " + username);
            }
        }

        File saveFile = new File(userFolder, "save.json");
        mapper.writeValue(saveFile, saveData); // Ghi dữ liệu vào file JSON
    }
}
