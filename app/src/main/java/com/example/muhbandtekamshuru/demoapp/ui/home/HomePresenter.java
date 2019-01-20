package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.view.View;

public class HomePresenter {

    //private User user;
    private View view;

    public HomePresenter(View view) {
        //this.user = new User();
        this.view = view;
    }

    public interface View{

        void getWeather(String info);
        void showProgressBar();
        void hideProgressBar();

    }
}
