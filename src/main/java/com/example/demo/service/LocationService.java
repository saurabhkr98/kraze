package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface LocationService {
    ResponseEntity<Object> getGeoCodingResponse(String filePath, String apiKey) throws IOException;
}
