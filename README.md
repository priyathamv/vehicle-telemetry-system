## System Design Assumptions

1. Data frequency: Vehicles send data every 5 seconds.
2. Fleet size: Initially designed for 10,000 vehicles, scalable to millions.
3. Data retention: Raw data kept for 1 year
4. Latency requirements: Near real-time processing for rule-based actions (within 10 seconds).
5. Report generation: Daily and monthly reports are pre-calculated and stored.
6. Speed limit: Default global speed limit of 70 km/h, configurable per region.
7. Time zones: All timestamps are in UTC, conversion to local time happens in the application layer.

## Storage estimates
- Assuming 1 million vehicles sending events every 5 seconds<br>
- Total no of events per day per vehicle = 17,280 events<br>
- Total no of events per day for 1 million vehicles = 17,280 * 1 million<br>
- Each event size = ~50 bytes<br>
- Total size of all events per day = 17,280 events * 1 million * 50 bytes = 8,640 GB = 8.6 TB<br>
- Total size of all events per year = 365 days * 8.6 TB = 3,139 TB<br>

## Server estimates
- Total no of requests per day = 17,280 * 1 million (from above)
- Total no of requests per second (distributed equally) = 2,00,000 requests
- Assuming each server can handle 6,400 requests per second
- Minimum no of servers needed = 2,00,000 / 6,400 = ~32 servers
- Maximum no of servers needed (under peak load) = 1 million / 6,400 = ~156 servers

## API design
### 1. Capturing an event
```
POST /api/v1/telemetry/event
{
  "time": null,
  "vehicleId": 1,
  "latitude": 37.8044,
  "longitude": -122.2711,
  "fuelPercentage": 36.75,
  "speed": 69.85
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
![vehicle_telemetry.drawio (2).png](..%2F..%2F..%2F..%2FDownloads%2Fvehicle_telemetry.drawio%20%282%29.png)
