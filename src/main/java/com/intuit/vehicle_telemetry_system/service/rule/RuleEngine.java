package com.intuit.vehicle_telemetry_system.service.rule;

import com.intuit.vehicle_telemetry_system.dto.MonitoringRule;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.repository.MonitoringRuleRepository;
import com.intuit.vehicle_telemetry_system.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.intuit.vehicle_telemetry_system.constant.AppConstants.OVERSPEEDING;

@Service
public class RuleEngine {
  private MonitoringRuleRepository ruleRepository;
  private Map<String, Supplier<Rule>> ruleFactories;

  public RuleEngine(MonitoringRuleRepository ruleRepository, NotificationService notificationService) {
    this.ruleRepository = ruleRepository;
    this.ruleFactories = new HashMap<>();

    ruleFactories.put(OVERSPEEDING, () -> new OverSpeedingRule(notificationService));
  }

  public void evaluate(VehicleTelemetry telemetry) {
    List<MonitoringRule> rules = ruleRepository.findAll();
    for (MonitoringRule rule : rules) {
      Rule ruleInstance = ruleFactories.get(rule.getRuleType()).get();
      ruleInstance.execute(telemetry, rule);
    }
  }

  public float getOverSpeedLimit() {
    MonitoringRule overSpeedRule = ruleRepository.findByRuleType(OVERSPEEDING);

    return Float.parseFloat(overSpeedRule.getParameter());
  }
}
