package com.intuit.vehicle_telemetry_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "vehicle_telemetry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleTelemetry {
  @EmbeddedId
  private VehicleTelemetryId id;

  @Column(name = "vehicle_id", insertable = false, updatable = false)
  private Long vehicleId;

  @Column(name = "time", insertable = false, updatable = false)
  private Instant time;

  private double latitude;

  private double longitude;

  @Column(name = "fuel_percentage")
  private float fuelPercentage;

  private float speed;

  public VehicleTelemetry(Long vehicleId, Instant time) {
    this.id = new VehicleTelemetryId(vehicleId, time);
  }
}

