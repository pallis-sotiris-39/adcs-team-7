package com.capy.dbconnector.serializer;

import com.capy.dbconnector.objects.ReadingModel;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class ReadingModelSerdes extends Serdes.WrapperSerde<ReadingModel> {
    public ReadingModelSerdes() {
        super(new ReadingSerializer(), new ReadingDeserializer());
    }

    public static Serde<ReadingModel> serdes() {
        ReadingSerializer serializer = new ReadingSerializer();
        ReadingDeserializer deSerializer = new ReadingDeserializer();
        return Serdes.serdeFrom(serializer, deSerializer);
    }
}
