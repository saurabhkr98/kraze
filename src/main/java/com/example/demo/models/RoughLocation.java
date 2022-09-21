package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoughLocation {
    @JsonProperty("northeast")
    private Location northEast;
    @JsonProperty("southwest")
    private Location southWest;
}
