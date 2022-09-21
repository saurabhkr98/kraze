package com.example.demo.controller;

import com.example.demo.service.LocationService;
import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.rmi.AccessException;

@RestController
public class GeoCodingController {

    @Autowired
    LocationService locationService;

    @GetMapping("/get/location/file")
    public ResponseEntity<Object> getLocationFile(@RequestParam("filePath") String filePath, String apiKey) {
        try {
            ResponseEntity<Object> responseEntityObject = locationService.getGeoCodingResponse(filePath, apiKey);
            return ResponseEntity.status(200).body(responseEntityObject.getBody());
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(400).body("Wrong filepath provided");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Empty file provided");
        } catch (AccessException e) {
            return ResponseEntity.status(400).body("Wrong api key provided");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while generating output file");
        } catch (InternalException e) {
            return ResponseEntity.status(500).body("Error occurred while calling google map api");
        }
    }
}
