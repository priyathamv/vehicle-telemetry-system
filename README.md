## System Design Assumptions

1. Data frequency: Vehicles send data every 10 seconds.
2. Fleet size: Initially designed for 10,000 vehicles, scalable to millions.
3. Data retention: Raw data kept for last 6 months
4. Latency requirements: Near real-time processing for rule-based actions (within 10 seconds).
5. Report generation: Daily and monthly reports are pre-calculated and stored.
6. Speed limit: Default global speed limit of 70 km/h, configurable per region.
7. Time zones: All timestamps are in UTC, conversion to local time happens in the application layer.

## Storage estimates
- Assuming 1 million vehicles sending events every 10 seconds<br>
- Total no of events per day per vehicle = 8,640 events<br>
- Total no of events per day for 1 million vehicles = 8,640 * 1 million<br>
- Each event size = ~50 bytes<br>
- Total size of all events per day = 8,640 events * 1 million * 50 bytes = 4,320 GB = 4.3 TB<br>
- Total size of all events for last 6 months = 180 days * 4.3 TB = 774 TB<br>

## Server estimates
- Total no of requests per day = 8,640 * 1 million (from above)
- Total no of requests per second (distributed equally) = 2,00,000 requests
- Assuming each server can handle 6,400 requests per second
- Minimum no of servers needed = 2,00,000 / 6,400 = ~32 servers
- Maximum no of servers needed (under peak load) = 1 million / 6,400 = ~156 servers

## API design
### 1. Capturing an event
```
POST /api/v1/telemetry/event
{
  "vehicleId": 1,
  "latitude": 37.8044,
  "longitude": -122.2711,
  "fuelPercentage": 36.75,
  "speed": 69.85,
  "time": "2024-08-16 06:15:15.267472+00"
}
```
### 2. Get raw events (for developers)
```
GET /api/v1/telemetry/event
Query params: 
    vehicleId
    page
    size
``` 
### 2. Generate reports
```
GET /api/v1/telemetry/generate-report
Query params: 
    frequency
    startDate
    endDate
    timezone
``` 


## Database models
### Tables
```
vehicle_telemetry
    vehicle_id BIGINT NOT NULL,
    time TIMESTAMPTZ NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    fuel_percentage REAL NOT NULL,
    speed REAL NOT NULL,
    PRIMARY KEY (vehicle_id, time)
```

### Materialized Views
```
over_speeding_view
    vehicle_id BIGINT NOT NULL,
    day DATE NOT NULL,
    over_speed_count INT PRECISION NOT NULL,
    max_speed DOUBLE PRECISION NOT NULL)
```

```
vehicle_distance_view
    vehicle_id BIGINT NOT NULL,
    time_bucket TIMESTAMPTZ NOT NULL,
    total_distance DOUBLE PRECISION NOT NULL)
```

## High Level design
![alt text](https://imgtr.ee/images/2024/08/18/9bf300abd8fa068117ed4a6d98f621a8.png)