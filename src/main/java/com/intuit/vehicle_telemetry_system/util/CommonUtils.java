package com.intuit.vehicle_telemetry_system.util;

import java.time.ZoneId;
import java.util.Optional;

import static com.intuit.vehicle_telemetry_system.constant.AppConstants.UTC_TIMEZONE;

public class CommonUtils {
  private CommonUtils() {}

  public static ZoneId getTimezone(String timezoneStr) {
    try {
      return Optional.ofNullable(timezoneStr)
          .map(ZoneId::of)
          .orElse(ZoneId.of(UTC_TIMEZONE));
    } catch (Exception e) {
      return ZoneId.of(UTC_TIMEZONE);
    }
  }
}
