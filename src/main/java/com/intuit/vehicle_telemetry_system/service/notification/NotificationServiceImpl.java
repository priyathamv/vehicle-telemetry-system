package com.intuit.vehicle_telemetry_system.service.notification;

import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  @Override
  public void sendNotification(String message) {
    log.info("Sending notification...");
    log.info(message);
  }
}
