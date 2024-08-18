package com.intuit.vehicle_telemetry_system.service.report;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public interface AbstractReport {
  void processData(Pair<LocalDateTime, LocalDateTime> dateRange);
}
