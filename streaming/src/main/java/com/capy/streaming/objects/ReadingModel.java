package com.capy.streaming.objects;

@SuppressWarnings("unused, FieldCanBeLocal")
public record ReadingModel (int sensorId, SensorType sensorType, String label, float value, boolean isLate) { }
