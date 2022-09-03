package com.capy.streaming.objects;

public class DailyModel {
    public DailyModel (){}
    public DailyModel (
            SensorType sensorType,
            String label,
            long dayTimestamp,
            float min,
            float max,
            float avg,
            float sum
    ){
        this.sensorType = sensorType;
        this.label = label;
        this.dayTimestamp = dayTimestamp;
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.sum = sum;
    }

    private SensorType sensorType;
    private String label;
    private long dayTimestamp;
    private float min;
    private float max;
    private float avg;
    private float sum;

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(long dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
