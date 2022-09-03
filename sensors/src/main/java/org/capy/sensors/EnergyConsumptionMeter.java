package org.capy.sensors;

import org.capy.network.apis.NetworkApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class EnergyConsumptionMeter extends Sensor {
    private final static int LOWEST_ENERGY_HOUR = 0;
    private final static int HIGHEST_ENERGY_HOUR = 12;
    private final static int ENERGY_READINGS_BETWEEN_PEAKS = 48;

    private final float[] minMonthEnergyConsumed;
    private final float[] maxMonthEnergyConsumed;
    private int currentLow = 0;
    private int currentHigh = 0;
    private int currentIndex = 0;
    private int currentMonth = 0;
    private final float[] currentEnergies = new float[ENERGY_READINGS_BETWEEN_PEAKS];

    public EnergyConsumptionMeter(NetworkApi api, int id, String label, float[] minMonthEnergy, float[] maxMonthEnergy) {
        super(api, id, SensorType.ENERGY_CONSUMPTION_METER, label);

        this.lateReadings = initializeLateReadings();
        this.minMonthEnergyConsumed = minMonthEnergy;
        this.maxMonthEnergyConsumed = maxMonthEnergy;
        setLowToHighEnergies();
        setHighToLowEnergies();
    }

    @Override
    protected Reading getNextReading() {
        long currentTimestamp = nextTimestamp;
        LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentTimestamp), ZoneId.of("UTC"));
        if (currentMonth < (currentLocalDateTime.getMonthValue() - 1)) {
            currentMonth = (currentLocalDateTime.getMonthValue() - 1);
        }

        if (currentLocalDateTime.getHour() == HIGHEST_ENERGY_HOUR && currentLocalDateTime.getMinute() == 0) {
            setHighToLowEnergies();
            currentIndex = 0;
        }
        if (currentLocalDateTime.getHour() == LOWEST_ENERGY_HOUR && currentLocalDateTime.getMinute() == 0) {
            setLowToHighEnergies();
            currentIndex = 0;
        }

        float quarterlyOffset = random.nextFloat(-0.005f, 0.005f);

        float energyConsumed = currentEnergies[currentIndex++] + quarterlyOffset;
        nextTimestamp += READ_EVENT_INTERVAL_SECS;
        return new Reading(currentTimestamp, energyConsumed);
    }

    private void setLowToHighEnergies() {
        getHighestEnergy();
        int[] lowToHighEnergies = random.ints(ENERGY_READINGS_BETWEEN_PEAKS, currentLow, currentHigh).toArray();
        Arrays.sort(lowToHighEnergies);
        for (int i = 0; i < ENERGY_READINGS_BETWEEN_PEAKS; i++) {
            currentEnergies[i] = ((float) lowToHighEnergies[i]) / 1000;
        }
    }

    private void setHighToLowEnergies() {
        getLowestEnergy();
        int[] highToLowEnergies = random.ints(ENERGY_READINGS_BETWEEN_PEAKS, currentLow, currentHigh).toArray();
        Arrays.sort(highToLowEnergies);
        for (int i = 0; i < ENERGY_READINGS_BETWEEN_PEAKS; i++) {
            currentEnergies[i] = ((float) highToLowEnergies[ENERGY_READINGS_BETWEEN_PEAKS - i - 1]) / 1000;
        }
    }

    private void getLowestEnergy() {
        float minEnergy = minMonthEnergyConsumed[currentMonth] + random.nextFloat(-0.02f, 0.02f);
        currentLow = (int) (minEnergy * 1000);
        if (currentLow > currentHigh) getHighestEnergy();
    }

    private void getHighestEnergy() {
        float maxEnergy = maxMonthEnergyConsumed[currentMonth] + random.nextFloat(-0.02f, 0.02f);
        currentHigh = (int) (maxEnergy * 1000);
        if (currentLow > currentHigh) getLowestEnergy();
    }

    @Override
    protected Queue<Reading> initializeLateReadings() {
        ArrayDeque<Reading> initialLateReadings = new ArrayDeque<>();
        initialLateReadings.add(new Reading(1609308000L, 0.305f)); //6:00
        initialLateReadings.add(new Reading(1609335900L, 0.267f)); //13:45
        initialLateReadings.add(new Reading(1609348500L, 0.284f)); //17:15
        initialLateReadings.add(new Reading(1609381800L, 0.321f)); //2:30
        initialLateReadings.add(new Reading(1609399800L, 0.319f)); //7:30
        initialLateReadings.add(new Reading(1609431300L, 0.277f)); //16:15
        initialLateReadings.add(new Reading(1609452000L, 0.348f)); //22:00
        return initialLateReadings;
    }
}
