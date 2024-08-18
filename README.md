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
![alt text](https://viewer.diagrams.net/index.html?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=vehicle_telemetry.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22ydolTpvk3e-coQaXjNj9%22%3E5Vpbe%2BI2EP01fF%2F6kHyyZRt45JK9tLRNS3bT3TdhC1DXICqLAPvrO8ISvsgQkkCc7L4k1uiCfObM6IyggXuz9XtBFtPfeUTjhouidQP3G67rYNSEf8qySS1Nz00NE8EiPSgzDNl3qo1IW5csoklhoOQ8lmxRNIZ8PqehLNiIEHxVHDbmcfFTF2RCLcMwJLFtvWORnKbWlo8y%2BwfKJlPzyQ7SPTNiBmtDMiURX%2BVM%2BLqBe4JzmT7N1j0aK%2FAMLum8d3t6dxsTdC6PmRCNblqdzq9f79D7T%2B96X8NxO%2Fl86WhvJHJj3phGAIBuciGnfMLnJL7OrF3Bl%2FOIqmURtLIxA84XYHTA%2BC%2BVcqO9SZaSg2kqZ7HuhR2LzT9q%2FpVvml%2F0cttGf11obXQr3ava4F4MtCnhSxHSQy%2BuuUTEhMoD44Kdp4DilM8o7AfmCRoTye6L%2ByCaa5PduMwd8KA98hjvpOvek3ipP6kXM%2FWqLhopalNhea%2Fom9WUSTpckC0UK4jQoh%2F08lRIuj6MqI2AnoDbmt46vrEJ3FUWLY4JgWkuUgJ0JtBwvZR2CoTO%2BH1%2BSrtHUhqjao%2B%2BDKcx%2FkkzzrHuadfpHdfKOLc0pnrtIIZ9d0eQdIKJehpScc%2FgnV100SXhNzqPfnl1%2BShAdecjt27CNwuMdx5gPF0zmQsUaH3J9WSTVCM%2F54YKBoDBkXTyyMFHRo77zMjZTu0IQTa5AQvO5jLJrXyjDBnh%2FKBIuKZXkmGl8V7r4Hh4SHeQEW73Ks%2FgYNOKa0FJlFQyc0BGIOILbCIxm8zhOQTfKQ93VYwyUMkd3TFjUZQSlybsOxlt11M00PDB4n634fcPBbmW8HryTs8WKHMgwvamhEt0hRw%2FKKBupNXjuGE589LBlcuaFfh4nABlyynkBB7FlkNveCIngg7%2FGtiZ%2BmLFIPm56BZCNFHFTb9rp2ooUBbqMdzEDJKPwA8n7FGapgajnQHOgck2ef25lLAM1fYkzVOOf5os77X9YhC1PDvL44os3zxXljey92Wz%2FAmTbPBCSfZZKAcW6%2B%2FoCAxKiahiCF18%2Bvj6NIjn161B2hZuf9OIJRWJIiThlNYPod8sybimDWG7AkHvbCrOrsVrS6CCS4g1rs7dNjoN3m7buSqlVC8wlhzmQQXmjuefC%2FSab6ceV8pvW2UZnMlpN6%2BnnYN6%2BoRp%2Fdiqcx8%2FziuePfdx4hkbRjx1fKtdIuU5xLZ9SpFkMw9VJAnIBj%2BK6t6jBPTyILqbblF0n0ZzO4U1DcHPr7hd780no1PmlWMv0PfUZi90neVbofgbGX8jauN8wcIKAfQHV1xjs4W69oK4q7jTghCST43UKj1QJMkpTnO%2FGCQtVFEeORVHOT6bfGpZjviZ6lUflcRVu%2B561XxYziHAfDYG0m6l5f7L3porA%2BfVXfBiz4LyjV2ae6iEaf1f4tmJ%2B23Q0319UNp3vwa%2BMd8Kqwyu4L8lNx2XaQ7swAAAYL2Fx%2FQb1D%2FA%2FqF%2FQO%2B3P%2FvogwyF485FfAx%2FPtMpC2PF9FsIBoNTskkknZkdwBulm0jXe4NHbamCcSvueryqqvlsRy22j1oL1rxwnXN16j1Fm1aB%2BbC4tKHMQWW%2BHHlmjVC%2BgfP9EtqpvtWzDhSQXvOBhVL9ay20vxA%2BviyBZvbroHR49hsrfP0%2F%3C%2Fdiagram%3E%3C%2Fmxfile%3E#%7B%22pageId%22%3A%22ydolTpvk3e-coQaXjNj9%22%7D)