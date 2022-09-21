package com.example.demo.client;

import com.example.demo.models.GeoCodingResponse;
import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class GoogleClient {

    @Autowired
    RestTemplate restTemplate;

    public GeoCodingResponse getGeoCodingResponse(List<String> placeList, String apiKey) {
        if (Objects.isNull(placeList) || placeList.size() == 0) {
            return null;
        }
        final String[] url = {"https://maps.googleapis.com/maps/api/geocode/json?address="};
        placeList.forEach(place -> {
            url[0] = url[0] + place + ",";
        });
        String path = url[0];
        int length = path.length();
        path = path.substring(0, length - 1);
        path = path + "&key=" + apiKey;
        try {
            ResponseEntity<GeoCodingResponse> geoCodingResponseResponseEntity = restTemplate.getForEntity(path, GeoCodingResponse.class);
            return geoCodingResponseResponseEntity.getBody();
        } catch (Exception e) {
            String message = "Error occurred while calling google map api";
            log.error(message);
            e.printStackTrace();
            throw new InternalException(message);
        }
    }
}
