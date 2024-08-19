package com.intuit.vehicle_telemetry_system.repository;

import com.intuit.vehicle_telemetry_system.dto.MonitoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRuleRepository extends JpaRepository<MonitoringRule, Long> {
  MonitoringRule findByRuleType(String ruleType);
}
