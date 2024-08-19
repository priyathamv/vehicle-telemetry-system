package com.intuit.vehicle_telemetry_system.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monitoring_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringRule  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String ruleType;

  @Column(nullable = false)
  private String parameter;
}
