package com.team1.sgart.backend.services;

import com.team1.sgart.backend.dao.LocationsDao;
import com.team1.sgart.backend.model.Locations;
import com.team1.sgart.backend.model.LocationsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private static final Logger logger = LoggerFactory.getLogger(LocationsService.class);
    private final LocationsDao locationsDao;

    @Autowired
    public LocationsService(LocationsDao locationsDao) {
        this.locationsDao = locationsDao;
        logger.info("[!] LocationsService created");
    }

    public List<LocationsDTO> getAllLocations() {
        List<Locations> locations = locationsDao.findAll();

        return locations.stream().map(location ->{
            LocationsDTO locationsDTO = new LocationsDTO();
            locationsDTO.setLocationId(location.getLocationId());
            locationsDTO.setLocationName(location.getLocationName());
            return locationsDTO;
        }).collect(Collectors.toList());
    }

    public String getLocationById(UUID locationId) {
        if (locationId == null) {
            logger.warn("El ID de ubicación proporcionado es null.");
            return "Ubicación no especificada";
        }
        logger.info("Buscando ubicación con ID: {}", locationId);
        String locationName = locationsDao.findLocationNameById(locationId.toString());
        if (locationName == null) {
            logger.warn("Ubicación no encontrada para el ID: {}", locationId);
            return "Ubicación no especificada";
        }
        logger.info("Ubicación encontrada: {}", locationName);
        return locationName;
    }
    
}
