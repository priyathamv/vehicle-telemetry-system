-- Create the Vehicle Telemetry table
CREATE TABLE IF NOT EXISTS vehicle_telemetry (
    vehicle_id BIGINT NOT NULL,
    time TIMESTAMPTZ NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    fuel_percentage REAL NOT NULL,
    speed REAL NOT NULL,
    PRIMARY KEY (vehicle_id, time)
);

-- Create the hypertable
SELECT create_hypertable('vehicle_telemetry', 'time', if_not_exists => TRUE);

-- Continuous aggregate that calculates the distance between consecutive points for each vehicle
CREATE MATERIALIZED VIEW vehicle_distance_view AS
WITH distances AS (
    SELECT
        time,
        vehicle_id,
        6371 * acos(
            cos(radians(LAG(latitude) OVER (PARTITION BY vehicle_id ORDER BY time))) *
            cos(radians(latitude)) *
            cos(radians(longitude) - radians(LAG(longitude) OVER (PARTITION BY vehicle_id ORDER BY time))) +
            sin(radians(LAG(latitude) OVER (PARTITION BY vehicle_id ORDER BY time))) *
            sin(radians(latitude))
        ) AS distance
    FROM
        vehicle_telemetry
)
SELECT
    vehicle_id,
    time_bucket('1 minute', time) AT TIME ZONE 'UTC' AS time_bucket,
    SUM(distance) AS total_distance
FROM
    distances
GROUP BY
    vehicle_id, time_bucket;


CREATE MATERIALIZED VIEW over_speeding_view AS
SELECT
    time_bucket('1 day', time) AS day,
    vehicle_id,
    COUNT(*) AS over_speed_count,
    MAX(speed) AS max_speed
FROM
    vehicle_telemetry
WHERE
    speed > 80.0
GROUP BY
    time_bucket('1 day', time), vehicle_id;

-- Monitoring Rules
CREATE TABLE monitoring_rule (
    id SERIAL PRIMARY KEY,
    rule_type VARCHAR(50) NOT NULL,
    parameter VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO monitoring_rule (rule_type, parameter)
VALUES ('OVERSPEEDING', '80.0');