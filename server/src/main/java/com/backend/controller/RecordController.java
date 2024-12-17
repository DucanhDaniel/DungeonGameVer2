package com.backend.controller;

import com.backend.model.Record;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/record")
public class RecordController {
    private final String filePath = "src/main/resources/records.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Record> records = new ArrayList<>();

    public RecordController() {
        loadRecords();
    }

    // Load dữ liệu từ file records.json
    private void loadRecords() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                records = objectMapper.readValue(file, new TypeReference<List<Record>>() {});
            } else {
                records = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ghi dữ liệu vào file records.json


    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(filePath), records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // API: Thêm Record mới
    @PostMapping("/save")
    public void saveRecord(@RequestBody Record recordRequest) {
        records.add(recordRequest);
        saveToFile();
    }

    // API: Lấy tất cả records của 1 username và sắp xếp theo thời gian tăng dần
    @GetMapping("/user/{username}")
    public List<Record> getRecordsByUserName(@PathVariable String username) {
        return records.stream()
                .filter(record -> record.username.equals(username))
                .sorted(Comparator.comparingLong(record -> record.time))
                .collect(Collectors.toList());
    }

    // API: Lấy bảng xếp hạng (rank)
    @GetMapping("/rank")
    public List<Record> getRank() {
        Map<String, Record> bestRecords = records.stream()
                .collect(Collectors.toMap(
                        record -> record.username,
                        record -> record,
                        (r1, r2) -> r1.time < r2.time ? r1 : r2
                ));

        return bestRecords.values().stream()
                .sorted(Comparator.comparingLong(record -> record.time))
                .collect(Collectors.toList());
    }
}
