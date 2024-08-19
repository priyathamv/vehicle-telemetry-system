package com.intuit.vehicle_telemetry_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleTelemetryId implements Serializable {
  @Column(name = "vehicle_id")
  private Long vehicleId;

  private Instant time;
}
