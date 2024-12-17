package com.backend.controller;
import com.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/save")
public class SaveController {
    @Autowired
    private FileService fileService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getSaveFile(@PathVariable String username) {
        try {
            Map<String, Object> saveData = fileService.getUserSaveData(username);
            return ResponseEntity.ok(saveData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading save file");
        }
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> saveData(@PathVariable String username, @RequestBody Map<String, Object> saveData) {
        try {
            fileService.saveUserData(username, saveData);
            return ResponseEntity.ok("Save data successfully for user: " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving data for user: " + username);
        }
    }
}
