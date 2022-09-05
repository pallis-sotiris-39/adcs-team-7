package com.capy.streaming.objects;

public class DailyModel {
    public DailyModel (){
        this.sensorId = -1;
        this.sensorType = null;
        this.label = "";
        this.min = Float.MAX_VALUE;
        this.max = Float.MIN_VALUE;
        this.avg = 0f;
        this.sum = 0f;
        this.total = 0;
    }
    public DailyModel (
            int sensorId,
            SensorType sensorType,
            String label,
            float min,
            float max,
            float avg,
            float sum,
            int total
    ){
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.label = label;
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.sum = sum;
        this.total = total;
    }

    public int sensorId;
    public SensorType sensorType;
    public String label;
    public float min;
    public float max;
    public float avg;
    public float sum;
    public int total;
}
