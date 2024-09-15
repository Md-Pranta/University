package com.university.demo.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "University")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "UniversityType")
    private UniversityType universityType;

    private Double rating;
    private String description;
    private String image;
    private LocalDate startingDate;
    private LocalDateTime casuallyOpensAt;

//    @Column(columnDefinition = "jsonb")
//    private String otherInformation; // Store JSON as a string

//    @Transient
//    private JsonNode otherInformationNode;
//
//    @PostLoad
//    private void postLoad() {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            this.otherInformationNode = mapper.readTree(this.otherInformation);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @PrePersist
//    @PreUpdate
//    private void prePersist() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        this.otherInformation = mapper.writeValueAsString(this.otherInformationNode);
//    }

}
