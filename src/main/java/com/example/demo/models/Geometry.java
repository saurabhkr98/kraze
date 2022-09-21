package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    private RoughLocation bounds;
    @JsonProperty("location_type")
    private String locationType;
    private Location location;
    @JsonProperty("viewport")
    private RoughLocation viewPort;
}
