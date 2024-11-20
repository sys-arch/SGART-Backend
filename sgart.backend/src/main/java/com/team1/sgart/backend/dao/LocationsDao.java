package com.team1.sgart.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team1.sgart.backend.model.Locations;

@Repository
public interface LocationsDao extends JpaRepository<Locations, String> {
    @Query(value = "SELECT location_name FROM SGART_LocationsTable WHERE location_id = :locationId", nativeQuery = true)
    String findLocationNameById(@Param("locationId") String locationId);

}