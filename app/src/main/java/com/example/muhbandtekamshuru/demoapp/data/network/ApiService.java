package com.example.muhbandtekamshuru.demoapp.data.network;

import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiService implements GetWeatherInteractor {

    private final static String API_KEY = "099d5b5b2b974898e9b5db5f7bf6eaa4";

    @Override
    public void getWeatherData(String city, final OnFinishedListener onFinishedListener) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherResponse> call = apiInterface.getWeatherDetails(city, API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()){
                    onFinishedListener.onSuccess(response.body());
                }else {
                    onFinishedListener.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                onFinishedListener.onFailure(-1);
            }
        });
    }
}
