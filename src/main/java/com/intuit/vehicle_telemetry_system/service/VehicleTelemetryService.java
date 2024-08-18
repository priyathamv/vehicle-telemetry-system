package com.intuit.vehicle_telemetry_system.service;

import com.intuit.vehicle_telemetry_system.dto.SimplePage;
import com.intuit.vehicle_telemetry_system.dto.VehicleTelemetryDTO;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import org.springframework.data.domain.Page;

public interface VehicleTelemetryService {
  boolean saveTelemetry(VehicleTelemetryDTO vehicleTelemetryDTO);

  SimplePage<VehicleTelemetry> getFilteredVehicleTelemetry(Long vehicleId, Float fuelPercentage, Float speed, int page, int size);
}
