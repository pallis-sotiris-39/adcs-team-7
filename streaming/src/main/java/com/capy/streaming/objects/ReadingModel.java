package com.capy.streaming.objects;

@SuppressWarnings("unused, FieldCanBeLocal")
public record ReadingModel (SensorType sensorType, String label, long timestamp, float value) { }
