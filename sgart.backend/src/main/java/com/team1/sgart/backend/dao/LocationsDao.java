package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.Locations;

import java.util.UUID;

@Repository
public interface LocationsDao extends JpaRepository<Locations, String> {
    @Query("SELECT l.locationName FROM Locations l WHERE l.locationId = :locationId")
    String findLocationNameById(@Param("locationId") UUID locationId);

}