package com.capy.dbconnector;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBConnectionClass {
    private static final String url = "http://localhost:8086";
    private static final String token = "hMI-r7k-sa2DAdRkZu7CLBYW2UtOLr5v9LqL1t97UXqY20rEEHYDtR8JfOgIGb_vGrg8u1mQNDbyGHt2M--ovg==";
    private static final String org = "capy-bara";
    private static final String bucket = "sensor-test";

    @Bean
    public InfluxDBClient buildConnection(){
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
