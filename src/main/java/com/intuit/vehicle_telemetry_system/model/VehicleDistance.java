package com.intuit.vehicle_telemetry_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_distance_view")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDistance {
  @EmbeddedId
  private VehicleDistanceId id;

  @Column(name = "time_bucket", insertable = false, updatable = false)
  private LocalDateTime timeBucket;

  @Column(name = "vehicle_id", insertable = false, updatable = false)
  private int vehicleId;


  @Column(name = "total_distance")
  private double totalDistance;

  public VehicleDistance(Long vehicleId, LocalDateTime time) {
    this.id = new VehicleDistanceId(vehicleId, time);
  }
}
