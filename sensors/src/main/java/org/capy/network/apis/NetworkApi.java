package org.capy.network.apis;

import org.capy.network.models.ReadingRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetworkApi {

    @Headers({"Accept: application/json"})
    @POST("readings")
    Call<Void> sendReading(@Body ReadingRequest readingRequest);
}
