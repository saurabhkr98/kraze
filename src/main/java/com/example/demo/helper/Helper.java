package com.example.demo.helper;

import com.example.demo.cache.PlaceData;
import com.example.demo.models.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class Helper {

    public void getPlaceListFromFilePath(String filePath, List<String> placeList) throws FileNotFoundException {
        Scanner inFile;
        try {
            inFile = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            String message = String.format("File not found at given location or path incorrect %s", filePath);
            log.error(message);
            e.printStackTrace();
            throw new FileNotFoundException(message);
        }
        while (inFile.hasNext()) {
            placeList.add(inFile.next());
        }
        if (placeList.size() == 0) {
            String message = "File is empty";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public void populateContainers(List<String> placeList, List<String> nonCachedList, Map<Integer, Location> integerLocationMap) {
        for (int i = 0; i < placeList.size(); i++) {
            if (Objects.nonNull(PlaceData.placeLocationMap.get(placeList.get(i)))) {
                log.info("Data fetched from cache for place {}", placeList.get(i));
                integerLocationMap.put(i, PlaceData.placeLocationMap.get(placeList.get(i)));
            } else {
                nonCachedList.add(placeList.get(i));
            }
        }
    }

    public String writeOutputFile(List<String> placeList, Map<Integer, Location> integerLocationMap) throws IOException {
        try {
            String fileName = LocalDateTime.now() + File.separator + "output.txt";
            File f = new File(fileName);
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileWriter myWriter = new FileWriter(f);
            for (int i = 0; i < placeList.size(); i++) {
                myWriter.write(integerLocationMap.get(i).getLat() + "," + integerLocationMap.get(i).getLng() + String.format("%n"));
            }
            myWriter.close();
            String message = String.format("Successfully wrote to the file %s", fileName);
            log.info(message);
            return message;
        } catch (IOException e) {
            String message  = "An error occurred while generating output file";
            log.error(message);
            e.printStackTrace();
            throw new IOException(message);
        }
    }
}
