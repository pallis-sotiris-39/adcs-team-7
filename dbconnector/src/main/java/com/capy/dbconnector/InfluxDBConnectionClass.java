package com.capy.dbconnector;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBConnectionClass {
    private static final String url = "http://localhost:8086";
    private static final String token = "mLAssWeFfJxYQiFQbOdpFEds9gzKyykl0THOU3l3M4FOksNwBNCBhedzBw-GnA1yLeWySIX1Z_wllXy_7QOJEw==";
    private static final String org = "capy-bara";
    private static final String bucket = "sensor-test";

    @Bean
    public InfluxDBClient buildConnection(){
        System.out.println("CONNECTED TO INFLUX HOPEFULLY");
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
