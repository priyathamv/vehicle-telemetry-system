package com.intuit.vehicle_telemetry_system.service;

import com.intuit.vehicle_telemetry_system.dto.SimplePage;
import com.intuit.vehicle_telemetry_system.dto.VehicleTelemetryDTO;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetryId;
import com.intuit.vehicle_telemetry_system.repository.VehicleTelemetryRepository;
import com.intuit.vehicle_telemetry_system.service.rule.RuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class VehicleTelemetryServiceImpl implements VehicleTelemetryService {
  private VehicleTelemetryRepository vehicleTelemetryRepository;
  private RuleEngine ruleEngine;

  @Autowired
  public VehicleTelemetryServiceImpl(VehicleTelemetryRepository vehicleTelemetryRepository, RuleEngine ruleEngine) {
    this.vehicleTelemetryRepository = vehicleTelemetryRepository;
    this.ruleEngine = ruleEngine;
  }

  @Override
  public boolean saveTelemetry(VehicleTelemetryDTO vehicleTelemetryDTO) {
    try {
      VehicleTelemetry vehicleTelemetry = convertToVehicleTelemetry(vehicleTelemetryDTO);

      // Evaluating rules
      ruleEngine.evaluate(vehicleTelemetry);

      // Persisting the record
      vehicleTelemetryRepository.save(vehicleTelemetry);
      return true;
    } catch (Exception ex) {
      log.info("Exception while saving Vehicle telemetry: {}", ex.getLocalizedMessage());
      return false;
    }
  }

  @Override
  public SimplePage<VehicleTelemetry> getFilteredVehicleTelemetry(Long vehicleId, Float fuelPercentage, Float speed, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<VehicleTelemetry> vehicleTelemetryPage = vehicleTelemetryRepository.findAll(
        withFilters(vehicleId, fuelPercentage, speed),
        pageable
    );

    return new SimplePage<>(
        vehicleTelemetryPage.getNumber(),
        vehicleTelemetryPage.getSize(),
        vehicleTelemetryPage.getTotalElements(),
        vehicleTelemetryPage.getTotalPages(),
        vehicleTelemetryPage.getContent()
    );
  }

  private VehicleTelemetry convertToVehicleTelemetry(VehicleTelemetryDTO vehicleTelemetryDTO) {
    VehicleTelemetryId vehicleTelemetryId = new VehicleTelemetryId(
        vehicleTelemetryDTO.getVehicleId(),
        isNull(vehicleTelemetryDTO.getTime()) ? Instant.now() : vehicleTelemetryDTO.getTime()
    );

    return VehicleTelemetry.builder()
        .id(vehicleTelemetryId)
        .vehicleId(vehicleTelemetryId.getVehicleId())
        .time(vehicleTelemetryId.getTime())
        .latitude(vehicleTelemetryDTO.getLatitude())
        .longitude(vehicleTelemetryDTO.getLongitude())
        .fuelPercentage(vehicleTelemetryDTO.getFuelPercentage())
        .speed(vehicleTelemetryDTO.getSpeed())
        .build();
  }

  private Specification<VehicleTelemetry> withFilters(Long vehicleId, Float fuelPercentage, Float speed) {
    return (root, query, criteriaBuilder) -> {
      var predicates = criteriaBuilder.conjunction();

      if (vehicleId != null) {
        predicates.getExpressions().add(criteriaBuilder.equal(root.get("vehicleId"), vehicleId));
      }

      if (fuelPercentage != null) {
        predicates.getExpressions().add(criteriaBuilder.equal(root.get("fuelPercentage"), fuelPercentage));
      }

      if (speed != null) {
        predicates.getExpressions().add(criteriaBuilder.equal(root.get("speed"), speed));
      }

      return predicates;
    };
  }

}
