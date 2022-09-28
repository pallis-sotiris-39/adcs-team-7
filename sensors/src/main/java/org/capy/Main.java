package org.capy;

import org.capy.network.apis.NetworkApi;
import org.capy.sensors.EnergyConsumptionMeter;
import org.capy.sensors.Sensor;
import org.capy.sensors.Thermometer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Main {
    public static final String BASE_URL = "http://localhost:8080/api/v1/";
    private static final float[] minMonthTemperatureAthens = {7.5f, 7.0f, 7.5f, 8.9f, 12.8f, 17.0f, 22.0f, 23.0f, 21.0f, 17.5f, 13.0f, 10.0f};
    private static final float[] maxMonthTemperatureAthens = {12.5f, 12.0f, 13.4f, 17.3f, 22.0f, 27.0f, 31.0f, 33.0f, 29.0f, 24.5f, 19.0f, 15.0f};
    private static final float[] minMonthTemperatureThessaloniki = {0.7f, 2.1f, 5.3f, 9.6f, 14.4f, 19.2f, 21.8f, 21.9f, 17.2f, 12.2f, 7.5f, 2.3f};
    private static final float[] maxMonthTemperatureThessaloniki = {8.3f, 10.5f, 14.3f, 18.9f, 23.7f, 28.5f, 31.3f, 31.2f, 25.9f, 20.1f, 14.8f, 9.5f};
    private static final float[] minMonthEnergyAthens = {0.318f, 0.316f, 0.319f, 0.212f, 0.215f, 0.314f, 0.311f, 0.315f, 0.218f, 0.216f, 0.214f, 0.312f};
    private static final float[] maxMonthEnergyAthens = {0.418f, 0.416f, 0.419f, 0.312f, 0.315f, 0.414f, 0.411f, 0.415f, 0.318f, 0.316f, 0.314f, 0.412f};

    protected static int globalSensorId = 0;

    public static void main(String[] args) {
        System.out.println("App is running! \uD83D\uDD0C");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkApi api = retrofit.create(NetworkApi.class);

        Sensor thermometerAthens = new Thermometer(api, globalSensorId++, "Athens thermometer", minMonthTemperatureAthens, maxMonthTemperatureAthens);
        Sensor thermometerThessaloniki = new Thermometer(api, globalSensorId++, "Thessaloniki thermometer", minMonthTemperatureThessaloniki, maxMonthTemperatureThessaloniki);
        Sensor energyConsumptionMeter = new EnergyConsumptionMeter(api, globalSensorId++, "Athens energy consumption meter", minMonthEnergyAthens, maxMonthEnergyAthens);

        thermometerAthens.start();
        /*thermometerThessaloniki.start();
        energyConsumptionMeter.start();*/
    }
}