package com.intuit.vehicle_telemetry_system.service;

import com.intuit.vehicle_telemetry_system.dto.FrequencyEnum;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public interface VehicleReportService {
  boolean getTotalDistanceTraveled(LocalDateTime startDate, LocalDateTime endDate, String timezoneStr, FrequencyEnum frequency);
}
