package com.intuit.vehicle_telemetry_system.service.rule;

import com.intuit.vehicle_telemetry_system.dto.MonitoringRule;
import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import com.intuit.vehicle_telemetry_system.service.notification.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class OverSpeedingRule implements Rule {

  private NotificationService notificationService;

  public OverSpeedingRule(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @Override
  public void execute(VehicleTelemetry telemetry, MonitoringRule rule) {
    if (telemetry.getSpeed() >= Float.parseFloat(rule.getParameter())) {
      String notificationMessage = String.format("Alert for vehicle %s: Speed: %s km/h", telemetry.getVehicleId(), telemetry.getSpeed());
      notificationService.sendNotification(notificationMessage);
    }
  }

}
