package com.university.demo;

import com.university.demo.dao.UniversityDao;
import com.university.demo.entity.University;
import com.university.demo.service.UniversityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UniversityServiceTest {

    @InjectMocks
    private UniversityService universityService;

    @Mock
    private UniversityDao universityDao;


    private static final String dir = "/home/universities/";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUniversity() throws IOException {
        University university = new University();
        university.setName("Test University");

        when(universityDao.save(any(University.class))).thenReturn(university);

        University result = universityService.saveUniversity(university);

        assertEquals("Test University", result.getName());
        verify(universityDao, times(1)).save(university);
    }

    @Test
    public void testUpdateUniversity() throws IOException {
        University existingUniversity = new University();
        existingUniversity.setId(1L);
        existingUniversity.setName("Old University");

        University updatedUniversity = new University();
        updatedUniversity.setName("Updated University");

        when(universityDao.findById(1L)).thenReturn(Optional.of(existingUniversity));
        when(universityDao.save(any(University.class))).thenReturn(updatedUniversity);

        University result = universityService.updateUniversity(1L, updatedUniversity);

        assertEquals("Updated University", result.getName());
        verify(universityDao, times(1)).save(existingUniversity);
    }
}
