package com.example.muhbandtekamshuru.demoapp.data.network;

import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;

public interface GetWeatherInteractor {
    interface OnFinishedListener {
        void onSuccess(WeatherResponse response);
        void onFailure(int errorCode);
    }

    void getWeatherData(String city, OnFinishedListener onFinishedListener);
}
