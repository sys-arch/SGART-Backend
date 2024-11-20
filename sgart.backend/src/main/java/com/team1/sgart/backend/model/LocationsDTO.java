package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationsDTO {

    @JsonProperty("locationId")
    private String locationId;

    @JsonProperty("locationName")
    private String locationName;

    public LocationsDTO() {}

    public LocationsDTO(String locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}