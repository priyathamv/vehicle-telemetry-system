package com.intuit.vehicle_telemetry_system.controller;

import com.intuit.vehicle_telemetry_system.dto.FrequencyEnum;
import com.intuit.vehicle_telemetry_system.dto.SimplePage;
import com.intuit.vehicle_telemetry_system.dto.VehicleTelemetryDTO;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.service.VehicleTelemetryService;
import com.intuit.vehicle_telemetry_system.service.report.VehicleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/telemetry")
public class VehicleTelemetryController {
  private VehicleTelemetryService vehicleTelemetryService;
  private VehicleReportService vehicleReportService;

  @Autowired
  public VehicleTelemetryController(VehicleTelemetryService vehicleTelemetryService, VehicleReportService vehicleReportService) {
    this.vehicleTelemetryService = vehicleTelemetryService;
    this.vehicleReportService = vehicleReportService;
  }

  @PostMapping("/event")
  public ResponseEntity<Boolean> addTelemetry(@RequestBody VehicleTelemetryDTO telemetry) {
    boolean saveStatus = vehicleTelemetryService.saveTelemetry(telemetry);

    return saveStatus ?
        ResponseEntity.ok(true) :
        ResponseEntity.internalServerError().body(false);
  }

  @GetMapping("/event")
  public ResponseEntity<SimplePage<VehicleTelemetry>> getVehicleTelemetry(
      @RequestParam(value = "vehicleId", required = false) Long vehicleId,
      @RequestParam(value = "fuelPercentage", required = false) Float fuelPercentage,
      @RequestParam(value = "speed", required = false) Float speed,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    SimplePage<VehicleTelemetry> filteredVehicleTelemetry = vehicleTelemetryService.getFilteredVehicleTelemetry(
        vehicleId, fuelPercentage, speed, page, size);

    return ResponseEntity.ok(filteredVehicleTelemetry);
  }

  @GetMapping("/generate-report")
  public ResponseEntity<Boolean> generateDailyReports(
      @RequestParam(value = "frequency", required = false) FrequencyEnum frequency,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
      @RequestParam(value = "timezone", required = false, defaultValue = "UTC") String timezone) {

    boolean reportStatus = this.vehicleReportService.getTotalDistanceTraveled(startDate, endDate, timezone, frequency);

    return reportStatus ?
        ResponseEntity.ok(true) :
        ResponseEntity.internalServerError().body(false);
  }
}
