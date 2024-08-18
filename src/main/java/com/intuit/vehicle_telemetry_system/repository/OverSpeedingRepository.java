package com.intuit.vehicle_telemetry_system.repository;

import com.intuit.vehicle_telemetry_system.model.OverSpeeding;
import com.intuit.vehicle_telemetry_system.model.VehicleDateId;
import com.intuit.vehicle_telemetry_system.model.VehicleDistance;
import com.intuit.vehicle_telemetry_system.model.VehicleDistanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OverSpeedingRepository extends JpaRepository<OverSpeeding, VehicleDateId> {

  @Query(value = "SELECT v.vehicle_id, v.over_speed_count, v.max_speed " +
      "FROM over_speeding_view v " +
      "WHERE v.day BETWEEN :startDate AND :endDate ", nativeQuery = true)
  List<Object[]> findOverSpeedVehicles(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
