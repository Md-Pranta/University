package com.university.demo.service;


import com.university.demo.dao.UniversityDao;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UniversityService {
    private static final String dir = "/home/universities/";
    @Autowired
    private UniversityDao universityDao;

    public List<University>getAllUniversities(){
        return (List<University>) universityDao.findAll();
    }

    public University getUniversityById(Long id){
        return universityDao.findById(id).orElse(null);
    }

    @Transactional
    public University saveUniversity(University university) {
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imagePath = saveImage(imageFile);
//            university.setImage(imagePath);
//        }
        return universityDao.save(university);
    }





    public void deleteUniversity(Long id){
        universityDao.deleteById(id);
    }
    @Transactional
    public University updateUniversity(Long id, University updatedUniversity) {
        // Find the existing university
        University existingUniversity = universityDao.findById(id).orElse(null);

        if (existingUniversity != null) {
            // Update fields
            existingUniversity.setName(updatedUniversity.getName());
            existingUniversity.setAddress(updatedUniversity.getAddress());
            existingUniversity.setUniversityType(updatedUniversity.getUniversityType());
            existingUniversity.setRating(updatedUniversity.getRating());
            existingUniversity.setDescription(updatedUniversity.getDescription());
            existingUniversity.setImage(updatedUniversity.getImage());
            existingUniversity.setStartingDate(updatedUniversity.getStartingDate());
            existingUniversity.setCasuallyOpensAt(updatedUniversity.getCasuallyOpensAt());

            // Save the updated entity
//            if (imageFile != null && !imageFile.isEmpty()) {
//                String imagePath = saveImage(imageFile);
//                existingUniversity.setImage(imagePath);
//            } else {
//                existingUniversity.setImage(existingUniversity.getImage());
//            }
            return universityDao.save(existingUniversity);
        }

        // If university does not exist, return null (could throw an exception too)
        return null;
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(dir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath);

        return filePath.toString(); // Return the saved image path
    }
    //search methods
//    public List<University>searchByName(String name){
//        return universityDao.findByNameContaining(name);
//    }
//
//    public List<University>searchByAddress(String address){
//        return universityDao.findByAddressContaining(address);
//    }
//    public List<University> searchByUniversityType(UniversityType type) {
//        return universityDao.findByUniversityType(type);
//    }
//
//    public List<University> searchByRating(Double from, Double to) {
//        return universityDao.findByRatingBetween(from, to);
//    }
//
//    public List<University> searchByDescription(String description) {
//        return universityDao.findByDescriptionContaining(description);
//    }
//
//    public List<University> searchByCasuallyOpensAt(LocalDateTime from, LocalDateTime to) {
//        return universityDao.findByCasuallyOpensAtBetween(from, to);
//    }

    public List<University> searchUniversities(String name, String address, UniversityType type, Double ratingFrom, Double ratingTo, String description, LocalDateTime openFrom, LocalDateTime openTo) {
        if (name != null) {
            return universityDao.findByNameContaining(name);
        } else if (address != null) {
            return universityDao.findByAddressContaining(address);
        } else if (type != null) {
            return universityDao.findByUniversityType(type);
        } else if (ratingFrom != null && ratingTo != null) {
            return universityDao.findByRatingBetween(ratingFrom, ratingTo);
        } else if (description != null) {
            return universityDao.findByDescriptionContaining(description);
        } else if (openFrom != null && openTo != null) {
            return universityDao.findByCasuallyOpensAtBetween(openFrom, openTo);
        }
        return universityDao.findAll();
    }
}
