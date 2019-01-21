package com.example.muhbandtekamshuru.demoapp;

import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;

public interface MainContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface presenter{

        void onDestroy();

        void onRefreshButtonClick();

        void requestDataFromServer();

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView {

        void hideProgress();
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetWeatherInteractor {

        interface OnFinishedListener {
            void onSuccess(WeatherResponse response);
            void onFailure(String error);
        }

        void getWeatherData(String city, OnFinishedListener onFinishedListener);
    }
}

