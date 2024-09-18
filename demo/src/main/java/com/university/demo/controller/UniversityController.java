package com.university.demo.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import com.university.demo.service.UniversityService;
import jdk.jfr.Timestamp;
import org.hibernate.boot.jaxb.mapping.ManagedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @Autowired
    ObjectMapper objectMapper;

    //for handing the upcoming data
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createUniversity(@RequestPart("university") String universityJson,
                                              @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            University university = universityService.parseUniversityJson(universityJson);
            University createUniversity = universityService.createUniversity(university, imageFile);
            return ResponseEntity.ok(createUniversity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing Request: "+e.getMessage());
        }
    }

    //handing update data
    @PutMapping(value = "update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?>updateUniversity(@PathVariable Long id,
                                             @RequestPart("university") String universityJson,
                                             @RequestPart(value = "image", required = false) MultipartFile imageFile){
        try {
            University university = universityService.parseUniversityJson(universityJson);
            University updateUniversity = universityService.updateUniversity(id, university, imageFile);
            return ResponseEntity.ok(updateUniversity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing Request: "+e.getMessage());
        }
    }

    //for list of universities
    @GetMapping("/all")
    public List<University> getAllUniversity(){
        return universityService.getAllUniversities();
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id){
        University university = universityService.getUniversityById(id);
        if (university != null)return ResponseEntity.ok(university);
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id){
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/search")
    public List<University> searchUniversities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) UniversityType type,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime openFrom,
            @RequestParam(required = false) LocalDateTime openTo) {
        return universityService.searchUniversities(name, address, type, ratingFrom, ratingTo, description, openFrom, openTo);
    }
}
