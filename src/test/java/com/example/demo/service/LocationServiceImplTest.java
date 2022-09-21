package com.example.demo.service;

import com.example.demo.client.GoogleClient;
import com.example.demo.models.GeoCodingResponse;
import com.example.demo.models.Geometry;
import com.example.demo.models.Location;
import com.example.demo.models.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@Slf4j
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest
@AutoConfigureMockMvc
class LocationServiceImplTest {

    @Autowired
    LocationServiceImpl locationService;

    @MockBean
    GoogleClient googleClient;

    @Test
    void getGeoCodingResponse_file_not_found() {
        Assertions.assertThrows(FileNotFoundException.class, () -> locationService.getGeoCodingResponse("wrong_file_path.txt", "apiKey"));
    }

    @Test
    void getGeoCodingResponse_empty_file_given() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.getGeoCodingResponse("src/test/resources/empty_input.txt", "apiKey"));
    }

    @Test
    void getGeoCodingResponse_wrong_api_key() {
        GeoCodingResponse geoCodingResponse = GeoCodingResponse.builder().status("ACCESS_DENIED").build();
        when(googleClient.getGeoCodingResponse(anyList(), anyString())).thenReturn(geoCodingResponse);
        Assertions.assertThrows(AccessException.class, () -> locationService.getGeoCodingResponse("src/test/resources/input.txt", "apiKey"));
    }

    @Test
    void getGeoCodingResponse_happy_flow() throws IOException {
        Result result1 = Result.builder().geometry(Geometry.builder().location(Location.builder().lat(12.34).lng(16.78).build()).build()).build();
        Result result2 = Result.builder().geometry(Geometry.builder().location(Location.builder().lat(15.34).lng(10.78).build()).build()).build();
        Result result3 = Result.builder().geometry(Geometry.builder().location(Location.builder().lat(8.34).lng(24.78).build()).build()).build();
        GeoCodingResponse geoCodingResponse = GeoCodingResponse.builder().results(List.of(result1, result2, result3)).status("OK").build();
        when(googleClient.getGeoCodingResponse(anyList(), anyString())).thenReturn(geoCodingResponse);
        ResponseEntity<Object> responseEntity = locationService.getGeoCodingResponse("src/test/resources/input.txt", "apiKey");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully wrote to the file", responseEntity.getBody().toString().substring(0, 30));
    }
}