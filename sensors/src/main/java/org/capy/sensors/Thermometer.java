package org.capy.sensors;

import org.capy.network.apis.NetworkApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Thermometer extends Sensor {
    private final static int LOWEST_TEMP_HOUR = 6;
    private final static int HIGHEST_TEMP_HOUR = 15;
    private final static int LOW_TO_HIGH_VALUES_AMOUNT = 36;
    private final static int HIGH_TO_LOW_VALUES_AMOUNT = 60;

    private final float[] minMonthTemperatures;
    private final float[] maxMonthTemperatures;

    private final float[] lowToHighTemperatures = new float[LOW_TO_HIGH_VALUES_AMOUNT];
    private final float[] highToLowTemperatures = new float[HIGH_TO_LOW_VALUES_AMOUNT];
    private float[] currentTemperatures;
    private int currentLow = 0;
    private int currentHigh = 0;
    private int currentIndex = 36;
    private int currentMonth = 0;


    public Thermometer(NetworkApi api, int id, String label, float[] minMonthTemperatures, float[] maxMonthTemperatures) {
        super(api, id, SensorType.THERMOMETER, label);

        this.minMonthTemperatures = minMonthTemperatures;
        this.maxMonthTemperatures = maxMonthTemperatures;
        this.lateReadings = initializeLateReadings();
        setLowToHighTemperatures();
        setHighToLowTemperatures();
        currentTemperatures = highToLowTemperatures;
    }

    @Override
    protected Reading getNextReading() {
        long currentTimestamp = nextTimestamp;
        LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentTimestamp), ZoneId.of("UTC"));

        if (currentMonth < (currentLocalDateTime.getMonthValue() - 1))
            currentMonth = (currentLocalDateTime.getMonthValue() - 1);

        if (currentLocalDateTime.getHour() == HIGHEST_TEMP_HOUR && currentLocalDateTime.getMinute() == 0) {
            setHighToLowTemperatures();
            currentTemperatures = highToLowTemperatures;
            currentIndex = 0;
        }
        if (currentLocalDateTime.getHour() == LOWEST_TEMP_HOUR && currentLocalDateTime.getMinute() == 0) {
            setLowToHighTemperatures();
            currentTemperatures = lowToHighTemperatures;
            currentIndex = 0;
        }

        float quarterlyOffset = random.nextFloat(-0.2f, 0.2f);

        float temperature = currentTemperatures[currentIndex++] + quarterlyOffset;

        nextTimestamp += READ_EVENT_INTERVAL_SECS;
        return new Reading(currentTimestamp, temperature);
    }

    private void setLowToHighTemperatures() {
        getHighestTemp();
        int[] lowToHighTemps = random.ints(LOW_TO_HIGH_VALUES_AMOUNT, currentLow, currentHigh).toArray();
        Arrays.sort(lowToHighTemps);
        for (int i = 0; i < LOW_TO_HIGH_VALUES_AMOUNT; i++) {
            lowToHighTemperatures[i] = ((float) lowToHighTemps[i]) / 10;
        }
    }

    private void setHighToLowTemperatures() {
        getLowestTemp();
        int[] highToLowTemps = random.ints(HIGH_TO_LOW_VALUES_AMOUNT, currentLow, currentHigh).toArray();
        Arrays.sort(highToLowTemps);
        for (int i = 0; i < HIGH_TO_LOW_VALUES_AMOUNT; i++) {
            highToLowTemperatures[i] = ((float) highToLowTemps[HIGH_TO_LOW_VALUES_AMOUNT - i - 1]) / 10;
        }
    }

    private void getLowestTemp() {
        do {
            float minTemp = minMonthTemperatures[currentMonth] + random.nextFloat(-2.0f, 2.0f);
            currentLow = (int) (minTemp * 10);
        } while (currentLow >= currentHigh);
    }

    private void getHighestTemp() {
        do {
            float maxTemp = maxMonthTemperatures[currentMonth] + random.nextFloat(-2.0f, 2.0f);
            currentHigh = (int) (maxTemp * 10);
        } while (currentLow >= currentHigh);
    }

    @Override
    protected Queue<Reading> initializeLateReadings() {
        ArrayDeque<Reading> initialLateReadings = new ArrayDeque<>();
        initialLateReadings.add(new Reading(1609318800L, 9.2f)); //9:00
        initialLateReadings.add(new Reading(1609342200L, 12.3f)); //15:30
        initialLateReadings.add(new Reading(1609359300L, 10.8f)); //20:15
        initialLateReadings.add(new Reading(1609371900L, 10.2f)); //23:45
        initialLateReadings.add(new Reading(1609389900L, 7.9f)); //4:45
        initialLateReadings.add(new Reading(1609439400L, 10.4f)); //18:30
        initialLateReadings.add(new Reading(1609448400L, 9.5f)); //21:00
        return initialLateReadings;
    }
}
