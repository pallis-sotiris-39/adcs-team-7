package org.capy.sensors;

import org.capy.network.apis.NetworkApi;
import org.capy.network.models.ReadingRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.capy.Util.*;

public abstract class Sensor {
    private static final int MAX_NUM_OF_READINGS = 4 * 24 * 365;
    private static final long READING_GENERATION_PERIOD = 10L; // Generate and send a reading every {READING_GENERATION_PERIOD}
    protected static final long READ_EVENT_INTERVAL_SECS = 15L * 60L; // 15 minutes * 60 secs is the reading interval
    protected static final int LATE_EVENT_INTERVAL = 30; // Every 30 readings we send a late event
    protected static final long JANUARY_FIRST_TIMESTAMP = 1609459200L; // 1/1/2021 0:00:00 Timestamp

    protected Sensor(NetworkApi api, int id, SensorType sensorType, String label) {
        this.api = api;
        this.id = id;
        this.sensorType = sensorType;
        this.label = label;
        this.random = new Random();
        this.timer = new Timer(label);
        this.timerTask = initializeTask();
        setLateEvents();
    }

    private final NetworkApi api;
    private final int id;
    private final SensorType sensorType;
    private final String label;

    private final Timer timer;
    private final TimerTask timerTask;
    protected final Random random;

    protected Queue<Reading> lateReadings;
    protected int readingsCounter = 0;
    protected long nextTimestamp = JANUARY_FIRST_TIMESTAMP;
    protected int nextLateEventToDiscard;
    protected int nextLateEventToSend;

    public final void start() {
        timer.schedule(timerTask, 0, READING_GENERATION_PERIOD);
    }

    private TimerTask initializeTask() {
        return new TimerTask() {
            @Override
            public void run() {
                checkForDiscard();
                checkForLateSend();
                checkIfFinished();
                checkToSetLateEvents();
                Reading tempReading = getNextReading();
                sendData(tempReading);
            }
        };
    }

    private void checkForDiscard() {
        if (readingsCounter % LATE_EVENT_INTERVAL == nextLateEventToDiscard) {
            Reading tempLateReading = getNextReading();
            lateReadings.add(tempLateReading);
        }
    }

    private void checkToSetLateEvents() {
        if (readingsCounter % LATE_EVENT_INTERVAL == 0) {
            setLateEvents();
        }
    }

    private void checkForLateSend() {
        if (readingsCounter % LATE_EVENT_INTERVAL == nextLateEventToSend) {
            Reading tempReading = lateReadings.remove();
            sendData(tempReading);
            checkForDiscard();
            checkForLateSend();
            checkIfFinished();
            checkToSetLateEvents();
        }
    }

    private void sendData(Reading reading) {
        readingsCounter++;
        ReadingRequest readingRequest = new ReadingRequest(id, sensorType, label, reading);
        Call<Void> sendReadingCall = api.sendReading(readingRequest);
        int currCounter = readingsCounter;
        sendReadingCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    String error = " Something went wrong in sensor: " +
                            label +
                            " This is the response code: " +
                            response.code() +
                            " This is the message: " +
                            response.message();
                    System.err.println(error);
                } else if (currCounter % 1000 == 0) {
                    System.out.println("✅ Successfully sent " + currCounter + " messages from sensor: " + label);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String error = " Call failed fo sensor: " +
                        label +
                        " This is the reason: " +
                        t.getLocalizedMessage();
                System.err.println(error);
            }
        });
    }

    private void checkIfFinished() {
        if (readingsCounter >= MAX_NUM_OF_READINGS) {
            System.out.println(ANSI_GREEN + "ALL READINGS HAVE FINISHED" + ANSI_RESET);
            timerTask.cancel();
            timer.cancel();
            timer.purge();
        }
    }

    protected void setLateEvents() {
        nextLateEventToDiscard = random.nextInt(LATE_EVENT_INTERVAL);
        nextLateEventToSend = random.nextInt(LATE_EVENT_INTERVAL);
    }

    protected abstract Reading getNextReading();

    protected abstract Queue<Reading> initializeLateReadings();
}