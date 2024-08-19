package com.intuit.vehicle_telemetry_system.repository;

import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleTelemetryRepository extends JpaRepository<VehicleTelemetry, VehicleTelemetryId>, JpaSpecificationExecutor<VehicleTelemetry>, QueryByExampleExecutor<VehicleTelemetry> {

  @Query(value = "SELECT v.vehicle_id, COUNT(v.speed), MAX(v.speed) " +
      "FROM vehicle_telemetry v " +
      "WHERE v.speed >= :speedLimit AND v.time BETWEEN :startDate AND :endDate " +
      "GROUP BY v.vehicle_id", nativeQuery = true)
  List<Object[]> findOverSpeedVehicles(@Param("speedLimit") float speedLimit,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
}
