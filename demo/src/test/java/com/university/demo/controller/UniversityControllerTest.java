//package com.university.demo.controller;
//
//
//import com.university.demo.entity.University;
//import com.university.demo.service.UniversityService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class UniversityControllerTest {
//
//    @Autowired
//    private UniversityService universityService;
//    @Autowired
//    private UniversityController universityController;
//
//    @BeforeEach
//    void setUp() {
//        universityService = mock(UniversityService.class);
//        universityController = new UniversityController(universityService);
//    }
//
//    @Test
//    void testCreateUniversity() throws Exception {
//        University university = new University();
//        university.setId(1L);
//        university.setName("Test University");
//
//        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "image content".getBytes());
//
//        when(universityService.addUniversity(any(University.class))).thenReturn(university);
//
//        ResponseEntity<University> response = universityController.createUniversity(
//                "Test University", "123 Main St", "PUBLIC", 4.5,
//                "A test university", LocalDate.now(), LocalDateTime.now(),
//                image, "Some info");
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(university, response.getBody());
//        verify(universityService, times(1)).addUniversity(any(University.class));
//    }
//
//    @Test
//    void testGetUniversity() {
//        University university = new University();
//        university.setId(1L);
//        university.setName("Test University");
//
//        when(universityService.getUniversity(1L)).thenReturn(university);
//
//        ResponseEntity<University> response = universityController.getUniversity(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(university, response.getBody());
//    }
//
//    @Test
//    void testGetAllUniversities() {
//        University university = new University();
//        university.setName("Test University");
//
//        when(universityService.getAllUniversities()).thenReturn(List.of(university));
//
//        ResponseEntity<List<University>> response = universityController.getAllUniversities();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1, response.getBody().size());
//    }
//}
//
