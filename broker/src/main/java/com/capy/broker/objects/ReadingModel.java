package com.capy.broker.objects;

@SuppressWarnings("unused, FieldCanBeLocal")
public record ReadingModel (int sensorId, SensorType sensorType, String label, float value) { }
