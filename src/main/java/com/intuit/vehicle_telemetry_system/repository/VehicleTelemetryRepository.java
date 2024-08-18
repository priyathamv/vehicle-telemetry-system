package com.intuit.vehicle_telemetry_system.repository;

import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VehicleTelemetryRepository extends JpaRepository<VehicleTelemetry, VehicleTelemetryId>, JpaSpecificationExecutor<VehicleTelemetry>, QueryByExampleExecutor<VehicleTelemetry> {
}
