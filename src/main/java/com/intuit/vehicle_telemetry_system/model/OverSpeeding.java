package com.intuit.vehicle_telemetry_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "over_speeding_view")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverSpeeding {
  @EmbeddedId
  private VehicleDateId id;

  @Column(name = "day", insertable = false, updatable = false)
  private LocalDate day;

  @Column(name = "vehicle_id", insertable = false, updatable = false)
  private int vehicleId;

  @Column(name = "over_speed_count")
  private long overSpeedCount;

  @Column(name = "max_speed")
  private double maxSpeed;

  public OverSpeeding(Long vehicleId, LocalDateTime timeBucket) {
    this.id = new VehicleDateId(vehicleId, timeBucket);
  }
}
