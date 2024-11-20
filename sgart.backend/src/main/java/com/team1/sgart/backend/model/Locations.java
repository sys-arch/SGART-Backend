
package com.team1.sgart.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SGART_LocationsTable")
public class Locations {

    @Id
    @Column(name = "location_id", nullable = false, unique = true)
    private String locationId;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    public Locations() {}

    public Locations(String locationId, String locationName) {
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