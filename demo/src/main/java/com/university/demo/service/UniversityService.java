package com.university.demo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.demo.dao.UniversityDao;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class UniversityService {
    @Autowired
    private UniversityDao universityDao;

    @Autowired
    ObjectMapper objectMapper;
    //image will save in this directory
    @Value("${file.upload-dir}")
    private String imageUploadDirectory;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


    //parsing the university
    public CompletableFuture<University> parseUniversityJson(String universityJson){
        return CompletableFuture.supplyAsync(()->{
            try {
                return objectMapper.readValue(universityJson, University.class);
            }catch (JsonProcessingException e){
                throw new RuntimeException("Error parsing university Json, e");
            }
        }, executorService);
    }

    //for handle the save method of a post
    @Transactional
    public CompletableFuture<University> createUniversity(University university, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(()->{
            try {
                if(imageFile != null && !imageFile.isEmpty()){
                    String imagePath = saveImage(imageFile);
                    university.setImage(imagePath);
                }
                return universityDao.save(university);
            }catch (IOException e){
                throw  new RuntimeException("ERROR creating university ", e);
            }
        }, executorService);

    }
    //for updating the data and checking image is present then update
    @Transactional
    public CompletableFuture<University> updateUniversity(Long id, University universityDetails, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                University university = universityDao.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("University not found with id: " + id));

                university.setName(universityDetails.getName());
                university.setAddress(universityDetails.getAddress());
                university.setUniversityType(universityDetails.getUniversityType());
                university.setRating(universityDetails.getRating());
                university.setDescription(universityDetails.getDescription());
                university.setStartingDate(universityDetails.getStartingDate());
                university.setCasuallyOpensAt(universityDetails.getCasuallyOpensAt());
                university.setOtherInformation(universityDetails.getOtherInformation());

                if (imageFile != null && !imageFile.isEmpty()) {
                    String imagePath = saveImage(imageFile);
                    university.setImage(imagePath);
                }
                return universityDao.save(university);
            } catch (IOException e) {
                throw new RuntimeException("Error updating university", e);
            }
        }, executorService);
    }
    //get all the universities as a list
    public CompletableFuture<List<University>>getAllUniversities(){
        return CompletableFuture.supplyAsync(()->universityDao.findAll(), executorService);
    }

    //Find university by id
    public CompletableFuture<University> getUniversityById(Long id){
        return CompletableFuture.supplyAsync(()-> universityDao.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("University not fount with id: "+id)));
//
    }

    //delete university
    @Transactional
    public CompletableFuture<Void> deleteUniversity(Long id){
        return CompletableFuture.runAsync(()->{
            University university = universityDao.findById(id).orElseThrow(()->new ResourceNotFoundException("University not fount with id: "+id));
            if (university.getImage() != null){
                deleteImage(university.getImage());
            }
            universityDao.deleteById(id);
        }, executorService);


    }


    //search in university by Terms
    public CompletableFuture<List<University>> searchUniversities(String name, String address, UniversityType type, Double ratingFrom, Double ratingTo, String description, LocalDateTime openFrom, LocalDateTime openTo) {
        return CompletableFuture.supplyAsync(()->{

        if (name != null && !name.isEmpty()) {
            return universityDao.findByNameContaining(name.toLowerCase());
        } else if (address != null && !address.isEmpty()) {
            return universityDao.findByAddressContaining(address);
        } else if (type != null) {
            return universityDao.findByUniversityType(type);
        } else if (ratingFrom != null && ratingTo != null) {
            return universityDao.findByRatingBetween(ratingFrom, ratingTo);
        } else if (description != null && !description.isEmpty()) {
            return universityDao.findByDescriptionContaining(description);
        } else if (openFrom != null && openTo != null) {
            return universityDao.findByCasuallyOpensAtBetween(openFrom, openTo);
        }
        return universityDao.findAll();
        }, executorService);
    }

    //save the image in the file System
    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID().toString()+"_"+imageFile.getOriginalFilename();
        Path filePath = Paths.get(imageUploadDirectory, fileName);
        Files.copy(imageFile.getInputStream(), filePath);
        return filePath.toString();
    }

    //delete the image if post is delete
    private void deleteImage(String imagePath){
        try {
            Path filePath = Paths.get(imageUploadDirectory+imagePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from file that is missing or else"+e);
        }
    }


}
