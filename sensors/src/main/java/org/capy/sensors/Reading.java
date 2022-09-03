package org.capy.sensors;

@SuppressWarnings("unused")
public class Reading {

    public Reading(long timestamp, float value) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Reading() {
        this.value = 0f;
        this.timestamp = 0L;
    }

    private final float value;
    private final long timestamp;

    public float getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
