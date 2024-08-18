package com.intuit.vehicle_telemetry_system.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTelemetryDTO {
  private Instant time;
  private Long vehicleId;
  private double latitude;
  private double longitude;
  private float fuelPercentage;
  private float speed;
}
