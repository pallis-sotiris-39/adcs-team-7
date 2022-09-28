package com.capy.dbconnector.serializer;

import com.capy.dbconnector.objects.DailyModel;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class DailyModelSerdes extends Serdes.WrapperSerde<DailyModel> {
    public DailyModelSerdes() {
        super(new DailySerializer(), new DailyDeserializer());
    }

    public static Serde<DailyModel> serdes() {
        DailySerializer serializer = new DailySerializer();
        DailyDeserializer deSerializer = new DailyDeserializer();
        return Serdes.serdeFrom(serializer, deSerializer);
    }
}
