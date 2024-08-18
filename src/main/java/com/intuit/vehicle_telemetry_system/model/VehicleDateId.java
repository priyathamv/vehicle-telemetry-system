package com.intuit.vehicle_telemetry_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleDateId implements Serializable {
  @Column(name = "vehicle_id")
  private Long vehicleId;

  private LocalDateTime day;
}
