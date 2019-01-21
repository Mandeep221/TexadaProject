package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.content.Context;

import com.example.muhbandtekamshuru.demoapp.data.network.GetWeatherInteractor;
import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;
import com.example.muhbandtekamshuru.demoapp.utils.NetworkUtils;

public class HomePresenter implements GetWeatherInteractor.OnFinishedListener {

    //private User user;
    private View view;
    private Context context;
    private GetWeatherInteractor weatherInteractor;
    private boolean favExpanded = false;

    public HomePresenter(Context context, View view, GetWeatherInteractor weatherInteractor) {
        //this.user = new User();
        this.view = view;
        this.context = context;
        this.weatherInteractor = weatherInteractor;
    }

    public void getWeatherData(String city){
        if(city.isEmpty() || city == null){
            view.handleCityNameValidation();
        }else {
            if(NetworkUtils.isNetworkConnected(context)){
                // make api call
                weatherInteractor.getWeatherData(city, this);
                view.handleSceneDuringApiRequest();
            }else {
                view.showNetworkErrorMessage();
            }
        }
    }

    public void onClickFavourites(){
        if(!favExpanded){
            view.showFavoritesView();
            favExpanded = true;
        }
    }

    public void onClickCancelFavourites(){
        if(favExpanded){
            view.hideFavouritesView();
            favExpanded = false;
        }
    }

    public void setFavouriteCity(String city){
        view.setFavouriteCity(city);
    }

    @Override
    public void onSuccess(WeatherResponse response) {
        view.onSuccess(response);
        view.handleSceneAfterApiRequest();
    }

    @Override
    public void onFailure(int error) {
        view.handleSceneAfterApiRequest();
        view.onFailure(error);
    }

    public interface View{
        void handleCityNameValidation();
        void onSuccess(WeatherResponse response);
        void onFailure(int error);
        void handleSceneDuringApiRequest();
        void handleSceneAfterApiRequest();
        void showFavoritesView();
        void hideFavouritesView();
        void showNetworkErrorMessage();



        void setFavouriteCity(String city);
    }
}
