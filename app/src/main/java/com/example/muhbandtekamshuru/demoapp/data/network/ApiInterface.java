package com.example.muhbandtekamshuru.demoapp.data.network;

import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Call<WeatherResponse> getWeatherDetails(@Query("q") String city, @Query("APPID") String apiKey );

}
