package com.university.demo.dao;

import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UniversityDao extends JpaRepository<University,Long> {
    List<University> findByNameContaining(String name);
    List<University> findByAddressContaining(String address);
    List<University> findByUniversityType(UniversityType type);
    List<University> findByRatingBetween(Double from, Double to);
    List<University> findByDescriptionContaining(String description);
    List<University> findByCasuallyOpensAtBetween(LocalDateTime from, LocalDateTime to);
}
