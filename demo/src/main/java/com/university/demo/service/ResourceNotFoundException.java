package com.university.demo.service;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
