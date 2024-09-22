package com.university.demo.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import com.university.demo.service.UniversityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/universities")
@CrossOrigin("http://localhost:4200")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @Autowired
    ObjectMapper objectMapper;

    //for handing the upcoming data
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity<?>> createUniversity(
            @RequestPart("university") String universityJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return universityService.parseUniversityJson(universityJson)
                .thenCompose(university -> universityService.createUniversity(university, imageFile))
                .handle((result, err)->{
                    if (err!=null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error processing request: " + err.getMessage());
                    }
                    return ResponseEntity.ok(result);
                });

    }

    //handing update data
    @PutMapping(value = "update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity<?>>updateUniversity(
            @PathVariable Long id,
            @RequestPart("university") String universityJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile){
        return universityService.parseUniversityJson(universityJson)
                .thenCompose(university -> universityService.updateUniversity(id, university, imageFile))
                .handle((result, err)->{
                    if (err!=null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error processing Request: "+err.getMessage());
                    }
                    return ResponseEntity.ok(result);
                });
    }

    //for list of universities
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<University>>> getAllUniversity(){
        return universityService.getAllUniversities()
                .thenApply(ResponseEntity::ok)
                .exceptionally(e->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));

//        return CompletableFuture.supplyAsync(()->universityService.getAllUniversities(), Thread.ofVirtual().factory());
    }

    @GetMapping("/all/{id}")
    public CompletableFuture<ResponseEntity<University>> getUniversityById(@PathVariable Long id){
        return universityService.getUniversityById(id)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e->ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteUniversity(@PathVariable Long id){
        return universityService.deleteUniversity(id)
                .thenApply(v->ResponseEntity.noContent().<Void>build())
                .exceptionally((e)->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }



    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<University>>> searchUniversities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) UniversityType type,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime openFrom,
            @RequestParam(required = false) LocalDateTime openTo) {
        return universityService.searchUniversities(name, address, type, ratingFrom, ratingTo, description, openFrom, openTo)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }
}
