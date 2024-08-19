package com.intuit.vehicle_telemetry_system.service.rule;

import com.intuit.vehicle_telemetry_system.dto.MonitoringRule;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;

public interface Rule {
  void execute(VehicleTelemetry telemetry, MonitoringRule rule);
}
