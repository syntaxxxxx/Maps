package com.fiqri.aboutmapapp;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fiqri on 11/01/18.
 */

interface ApiService {

    @GET("directions/json")
    Call<ResponseRoute> ini_request_route(@Query("origin") String awal,
                                          @Query("destination") String tujuan,
                                          @Query("mode") String mode);
}
