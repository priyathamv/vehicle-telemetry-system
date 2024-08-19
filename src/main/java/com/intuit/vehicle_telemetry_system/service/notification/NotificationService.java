package com.intuit.vehicle_telemetry_system.service.notification;

import com.intuit.vehicle_telemetry_system.model.VehicleTelemetry;

public interface NotificationService {
  void sendNotification(String message);
}
