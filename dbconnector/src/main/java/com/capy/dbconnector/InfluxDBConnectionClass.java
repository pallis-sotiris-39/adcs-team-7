package com.capy.dbconnector;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InfluxDBConnectionClass {
    private final static String url = "http://influxdb:8086";

    private final static String token = "secrettoken";

    private final static String org = "capybara";

    private final static String bucket = "sensorbucket";

    @Bean
    public InfluxDBClient buildConnection(){
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
