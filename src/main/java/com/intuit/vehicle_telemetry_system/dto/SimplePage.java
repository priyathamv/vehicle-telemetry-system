package com.intuit.vehicle_telemetry_system.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SimplePage<T> {
  private int currentPage;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private List<T> content;
}