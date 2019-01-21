package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.view.View;

import com.example.muhbandtekamshuru.demoapp.MainContract;
import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;

public class HomePresenter implements MainContract.GetWeatherInteractor.OnFinishedListener {

    //private User user;
    private View view;
    private MainContract.GetWeatherInteractor weatherInteractor;

    public HomePresenter(View view, MainContract.GetWeatherInteractor weatherInteractor) {
        //this.user = new User();
        this.view = view;
        this.weatherInteractor = weatherInteractor;
    }

    public void getWeatherData(String city){
        if(city.isEmpty() || city == null){
            view.handleCityNameValidation();
        }else {
            // make api call
            weatherInteractor.getWeatherData(city, this);
            view.handleSceneDuringApiRequest();
        }
    }

    @Override
    public void onSuccess(WeatherResponse response) {
        view.onSuccess(response);
        view.handleSceneAfterApiRequest();
    }

    @Override
    public void onFailure(String error) {
        view.onFailure(error);
        view.handleSceneAfterApiRequest();
    }

    public interface View{
        void handleCityNameValidation();
        void onSuccess(WeatherResponse response);
        void onFailure(String error);
        void handleSceneDuringApiRequest();
        void handleSceneAfterApiRequest();
    }
}
