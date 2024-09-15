package com.university.demo.controller;



import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import com.university.demo.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

//    @GetMapping
//    public List<University> getAllUniversities(){
//        return universityService.getAllUniversities();
//    }
    @GetMapping("/{id}")
    public University getUniversityById(@PathVariable Long id){
        return universityService.getUniversityById(id);
    }

//    @RequestParam(value = "image", required = false) MultipartFile imageFile
    @PostMapping("/add")
    public University createUniversity(@RequestBody University university) {
        return universityService.saveUniversity(university);
    }
    @PutMapping("/update/{id}")
    public University updateUniversity(@PathVariable Long id, @RequestBody University university){
        return universityService.updateUniversity(id, university);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUniversity(@PathVariable Long id){
        universityService.deleteUniversity(id);
    }

    @GetMapping
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
