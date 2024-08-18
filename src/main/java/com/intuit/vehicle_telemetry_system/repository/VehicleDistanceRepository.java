package com.intuit.vehicle_telemetry_system.repository;

import com.intuit.vehicle_telemetry_system.model.VehicleDistance;
import com.intuit.vehicle_telemetry_system.model.VehicleDistanceId;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleDistanceRepository extends JpaRepository<VehicleDistance, VehicleDistanceId> {
  @Query(value = "SELECT v.vehicle_id, SUM(v.total_distance) " +
      "FROM vehicle_distance_view v " +
      "WHERE v.time_bucket BETWEEN :startTime AND :endTime " +
      "GROUP BY v.vehicle_id", nativeQuery = true)
  List<Object[]> findTotalDistanceByVehicleIdAndTimeRange(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);

}
