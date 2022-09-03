package org.capy.network.models;

import org.capy.sensors.Reading;
import org.capy.sensors.SensorType;

@SuppressWarnings("unused, FieldCanBeLocal")
public class ReadingRequest {
    public ReadingRequest(int sensorId, SensorType sensorType, String label, Reading reading) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.label = label;
        this.timestamp = reading.getTimestamp();
        this.value = reading.getValue();
    }

    private final int sensorId;
    private final SensorType sensorType;
    private final String label;
    private final long timestamp;
    private final float value;
}
