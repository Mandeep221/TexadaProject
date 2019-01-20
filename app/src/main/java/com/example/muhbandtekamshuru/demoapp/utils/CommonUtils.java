package com.example.muhbandtekamshuru.demoapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.muhbandtekamshuru.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    public static int kelvinToCelsius(double temp){
        double celsius = temp - 273.15;

        // round off
        if( celsius > (int)celsius + 0.5){
            return (int)celsius + 1;
        }else {
            return (int)celsius;
        }
    }

    public static int kelvinToFahrenheit(double temp) {
        double celsius = temp - 273.15;
        double fah = celsius*9/5+32;

        // round off
        if( fah > (int)fah + 0.5){
            return (int)fah + 1;
        }else {
            return (int)fah;
        }
    }

    public static void changeImageBackground(ImageView imageView, int weatherCode){
        switch (weatherCode){
            case 200 : imageView.setImageResource(R.drawable.clouds_one);
                break;
            case 300: imageView.setImageResource(R.drawable.umbrella_four);
                break;
            case 500 : imageView.setImageResource(R.drawable.umbrella_three);
                break;
            case 600: imageView.setImageResource(R.drawable.umbrella);
                break;
            case 700 : imageView.setImageResource(R.drawable.umbrella_two);
                break;
            case 800: imageView.setImageResource(R.drawable.sun_one);
                break;
            case 801: imageView.setImageResource(R.drawable.umbrella);
                break;
                default: imageView.setImageResource(R.drawable.umbrella);

        }
    }

//    public static double FahrenheitToCelsius(double temp){
//        Double b=temp-32;
//        return b*5/9;
//    }
//
//    public static int celsiusToFahrenheit(double temp){
//
//    }
}
