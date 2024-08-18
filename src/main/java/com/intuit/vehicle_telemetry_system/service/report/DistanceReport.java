package com.intuit.vehicle_telemetry_system.service.report;

import com.intuit.vehicle_telemetry_system.repository.VehicleDistanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DistanceReport implements AbstractReport {

  private VehicleDistanceRepository vehicleDistanceRepository;

  @Autowired
  public DistanceReport(VehicleDistanceRepository vehicleDistanceRepository) {
    this.vehicleDistanceRepository = vehicleDistanceRepository;
  }

  @Override
  public void processData(Pair<LocalDateTime, LocalDateTime> dateRange) {
    List<Object[]> totalDistanceByVehicleIdAndTimeRange = vehicleDistanceRepository.findTotalDistanceByVehicleIdAndTimeRange(dateRange.getFirst(), dateRange.getSecond());

    log.info("totalDistanceByVehicleIdAndTimeRange: {}", totalDistanceByVehicleIdAndTimeRange);
  }
}
