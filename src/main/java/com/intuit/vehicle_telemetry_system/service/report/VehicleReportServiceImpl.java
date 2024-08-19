package com.intuit.vehicle_telemetry_system.service.report;

import com.intuit.vehicle_telemetry_system.dto.FrequencyEnum;
import com.intuit.vehicle_telemetry_system.dto.MonitoringRule;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.repository.MonitoringRuleRepository;
import com.intuit.vehicle_telemetry_system.repository.VehicleDistanceRepository;
import com.intuit.vehicle_telemetry_system.repository.VehicleTelemetryRepository;
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

import static com.intuit.vehicle_telemetry_system.constant.AppConstants.OVERSPEEDING;
import static com.intuit.vehicle_telemetry_system.constant.AppConstants.decimalFormat2;

@Slf4j
@Service
public class VehicleReportServiceImpl implements VehicleReportService {
  private VehicleDistanceRepository vehicleDistanceRepository;
  private VehicleTelemetryRepository vehicleTelemetryRepository;
  private MonitoringRuleRepository monitoringRuleRepository;

  @Autowired
  public VehicleReportServiceImpl(
      VehicleDistanceRepository vehicleDistanceRepository,
      VehicleTelemetryRepository vehicleTelemetryRepository,
      MonitoringRuleRepository monitoringRuleRepository) {
    this.vehicleDistanceRepository = vehicleDistanceRepository;
    this.vehicleTelemetryRepository = vehicleTelemetryRepository;
    this.monitoringRuleRepository = monitoringRuleRepository;
  }

  @Override
  public boolean getTotalDistanceTraveled(LocalDateTime startDate, LocalDateTime endDate, String timezoneStr, FrequencyEnum frequency) {

    Pair<LocalDateTime, LocalDateTime> startAndEndDate = getStartAndEndDate(startDate, endDate, timezoneStr, frequency);

    MonitoringRule overSpeedRule = monitoringRuleRepository.findByRuleType(OVERSPEEDING);
    float overSpeedLimit = Float.parseFloat(overSpeedRule.getParameter());

    List<Object[]> overSpeedVehicles = vehicleTelemetryRepository.findOverSpeedVehicles(
        overSpeedLimit,
        startAndEndDate.getFirst(),
        startAndEndDate.getSecond());
    
    log.info("########## Over Speed vehicles report ##########");
    overSpeedVehicles.forEach(curOverSpeedVehicle -> {
      Object vehicleId = curOverSpeedVehicle[0];
      Object overSpeedCount = curOverSpeedVehicle[1];
      Object maxSpeed = curOverSpeedVehicle[2];
      log.info("Vehicle Id: {}, Over Speed count: {}, Max Speed: {}", vehicleId, overSpeedCount, maxSpeed);
    });

    List<Object[]> vehicleDistances = vehicleDistanceRepository.findTotalDistanceByVehicleIdAndTimeRange(
        startAndEndDate.getFirst(),
        startAndEndDate.getSecond());

    log.info("########## Vehicle distances report ##########");
    vehicleDistances.forEach(curVehicleDistance -> {
      Object vehicleId = curVehicleDistance[0];
      Double distanceCovered = Double.parseDouble(String.format("%.2f", curVehicleDistance[1]));

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
