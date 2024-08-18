package com.intuit.vehicle_telemetry_system.service;

import com.intuit.vehicle_telemetry_system.dto.FrequencyEnum;
import com.intuit.vehicle_telemetry_system.repository.OverSpeedingRepository;
import com.intuit.vehicle_telemetry_system.repository.VehicleDistanceRepository;
import com.intuit.vehicle_telemetry_system.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static com.intuit.vehicle_telemetry_system.constant.AppConstants.decimalFormat2;

@Slf4j
@Service
public class VehicleReportServiceImpl implements VehicleReportService {
  private VehicleDistanceRepository vehicleDistanceRepository;
  private OverSpeedingRepository overSpeedingRepository;

  @Autowired
  public VehicleReportServiceImpl(VehicleDistanceRepository vehicleDistanceRepository, OverSpeedingRepository overSpeedingRepository) {
    this.vehicleDistanceRepository = vehicleDistanceRepository;
    this.overSpeedingRepository = overSpeedingRepository;
  }

  @Override
  public boolean getTotalDistanceTraveled(LocalDateTime startDate, LocalDateTime endDate, String timezoneStr, FrequencyEnum frequency) {

    Pair<LocalDateTime, LocalDateTime> startAndEndDate = getStartAndEndDate(startDate, endDate, timezoneStr, frequency);

    List<Object[]> overSpeedVehicles = overSpeedingRepository.findOverSpeedVehicles(
        startAndEndDate.getFirst().toLocalDate(),
        startAndEndDate.getSecond().toLocalDate());
    
    overSpeedVehicles.forEach(curOverSpeedVehicle -> {
      Object vehicleId = curOverSpeedVehicle[0];
      Object overSpeedCount = curOverSpeedVehicle[1];
      Object maxSpeed = curOverSpeedVehicle[2];
      log.info("Over Speed vehicles");
      log.info("Vehicle Id: {}, Over Speed count: {}, Max Speed: {}", vehicleId, overSpeedCount, maxSpeed);
    });

    List<Object[]> vehicleDistances = vehicleDistanceRepository.findTotalDistanceByVehicleIdAndTimeRange(
        startAndEndDate.getFirst(),
        startAndEndDate.getSecond());

    vehicleDistances.forEach(curVehicleDistance -> {
      Object vehicleId = curVehicleDistance[0];
      Double distanceCovered = Double.valueOf(decimalFormat2.format(String.valueOf(curVehicleDistance[1])));

      log.info("\nVehicle distances");
      log.info("Vehicle Id: {}, Distance covered: {}", vehicleId, distanceCovered);
    });

    return true;
  }

  private Pair<LocalDateTime, LocalDateTime> getStartAndEndDate(
      LocalDateTime startDate,
      LocalDateTime endDate,
      String timezoneStr,
      FrequencyEnum frequency) {
    ZoneId timezone = CommonUtils.getTimezone(timezoneStr);

    LocalDateTime now = LocalDateTime.now(timezone);

    if (frequency == null) {
      startDate = Optional.ofNullable(startDate).orElse(now);
      endDate = Optional.ofNullable(endDate).orElse(now);
    } else if (frequency == FrequencyEnum.DAY) {
      if (startDate == null) {
        startDate = now.with(LocalDateTime.MIN);
        endDate = now.with(LocalDateTime.MAX);
      }
    } else if (frequency == FrequencyEnum.MONTH) {
      startDate = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalDateTime.MIN);
      endDate = now;
    }

    // Ensure startDate is not after endDate
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }

    return Pair.of(startDate, endDate);
  }
}
