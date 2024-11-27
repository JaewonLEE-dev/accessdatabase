package com.gmail.leejaewon264.accessdatabase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class UserInfoController {

    private final UserInfoRepository userInfoRepository;

    public UserInfoController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/download-json")
    public ResponseEntity<?> downloadJsonFile() {
        List<UserInfo> users = userInfoRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No data available in the table.");
        }

        File directory = new File("user_info");
        if (!directory.exists() && !directory.mkdir()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create directory.");
        }

        File jsonFile = new File(directory, "user_info.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(jsonFile, users);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating JSON file: " + e.getMessage());
        }

        try (FileInputStream fileInputStream = new FileInputStream(jsonFile)) {
            byte[] fileContent = fileInputStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_info.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while downloading file: " + e.getMessage());
        }
    }
}

