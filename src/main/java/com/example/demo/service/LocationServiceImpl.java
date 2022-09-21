package com.example.demo.service;

import com.example.demo.cache.PlaceData;
import com.example.demo.client.GoogleClient;
import com.example.demo.helper.Helper;
import com.example.demo.models.GeoCodingResponse;
import com.example.demo.models.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.AccessException;
import java.util.*;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    GoogleClient googleClient;

    @Autowired
    Helper helper;

    @Override
    public ResponseEntity<Object> getGeoCodingResponse(String filePath, String apiKey) throws IOException {
        List<String> placeList = new ArrayList<>();
        List<String> nonCachedList = new ArrayList<>();
        Map<Integer, Location> integerLocationMap = new HashMap<>();
        helper.getPlaceListFromFilePath(filePath, placeList);
        helper.populateContainers(placeList, nonCachedList, integerLocationMap);
        GeoCodingResponse geoCodingResponse = googleClient.getGeoCodingResponse(nonCachedList, apiKey);
        if (!Objects.equals(geoCodingResponse.getStatus(), "OK")) {
            String message = "api key provided is not correct or does not have necessary permissions";
            log.error(message);
            throw new AccessException(message);
        }
        int j = 0;
        for (int i = 0; i < placeList.size(); i++) {
            if (!integerLocationMap.containsKey(i)) {
                integerLocationMap.put(i, geoCodingResponse.getResults().get(j).getGeometry().getLocation());
                PlaceData.placeLocationMap.put(placeList.get(i), geoCodingResponse.getResults().get(j).getGeometry().getLocation());
                j++;
            }
        }
        String message = helper.writeOutputFile(placeList, integerLocationMap);
        return ResponseEntity.status(200).body(message);
    }
}
