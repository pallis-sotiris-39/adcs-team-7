package com.capy.broker.network;

import com.capy.broker.objects.SensorType;

@SuppressWarnings("unused, FieldCanBeLocal")
public record ReadingRequest (int sensorId, SensorType sensorType, String label, long timestamp, float value) { }
