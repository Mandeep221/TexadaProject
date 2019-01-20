package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhbandtekamshuru.demoapp.R;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiClient;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiInterface;
import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;
import com.example.muhbandtekamshuru.demoapp.ui.custom.ResizeAnimation;
import com.example.muhbandtekamshuru.demoapp.utils.AppConstants;
import com.example.muhbandtekamshuru.demoapp.utils.CommonUtils;
import com.example.muhbandtekamshuru.demoapp.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private HomePresenter presenter;

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "099d5b5b2b974898e9b5db5f7bf6eaa4";

    @BindView(R.id.ah_iv_background)
    ImageView imgWeatherTheme;

    @BindView(R.id.ah_et_city)
    AppCompatEditText etCity;

    @BindView(R.id.ah_ib_get_weather)
    ImageButton btnGetWeather;

    @BindView(R.id.ah_tv_temperature)
    TextView temperature;

    @BindView(R.id.cs_rl_celsius)
    RelativeLayout rl_celsius;

    @BindView(R.id.cs_rl_fahrenheit)
    RelativeLayout rl_fahrenheit;

    @BindView(R.id.cs_tv_celsius)
    TextView tv_label_celsius;

    @BindView(R.id.cs_tv_fahrenheit)
    TextView tv_label_fahrenheit;

    @BindView(R.id.ah_tv_tagline)
    TextView tagLine;

    @BindView(R.id.ah_tv_subtagline)
    TextView subTagLine;

    @BindView(R.id.ah_rl_details)
    RelativeLayout rlDetails;

    @BindView(R.id.ah_rl_favourite)
    RelativeLayout rlFavourite;

    @BindView(R.id.ah_ll_fav_container)
    LinearLayout favCitiesContainer;
    boolean favExpanded = false;

    int tempCelsius, tempFahrenheit;

    List<String> list = new ArrayList<>();
    AlphaAnimation animation1;
    TextView sunsetLabel, sunsetValue;
    TextView sunriseLabel, sunriseValue;
    TextView humidityLabel, humidityValue;
    TextView pressureLabel, pressureValue;
    TextView visibilityLabel, visibilityValue;
    TextView windLabel, windValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
        animation1.setRepeatCount(10000);
        animation1.setFillAfter(true);

        sunriseLabel = ((LinearLayout)findViewById(R.id.ah_layout_sunset_sunrise)).findViewById(R.id.iir_tv_left_key);
        sunriseValue = ((LinearLayout)findViewById(R.id.ah_layout_sunset_sunrise)).findViewById(R.id.iir_tv_left_value);
        sunsetLabel = ((LinearLayout)findViewById(R.id.ah_layout_sunset_sunrise)).findViewById(R.id.iir_tv_right_key);
        sunsetValue = ((LinearLayout)findViewById(R.id.ah_layout_sunset_sunrise)).findViewById(R.id.iir_tv_right_value);

        pressureLabel = ((LinearLayout)findViewById(R.id.ah_layout_humidity_pressure)).findViewById(R.id.iir_tv_left_key);
        pressureValue = ((LinearLayout)findViewById(R.id.ah_layout_humidity_pressure)).findViewById(R.id.iir_tv_left_value);
        humidityLabel = ((LinearLayout)findViewById(R.id.ah_layout_humidity_pressure)).findViewById(R.id.iir_tv_right_key);
        humidityValue = ((LinearLayout)findViewById(R.id.ah_layout_humidity_pressure)).findViewById(R.id.iir_tv_right_value);

        visibilityLabel = ((LinearLayout)findViewById(R.id.ah_layout_visibility_wind)).findViewById(R.id.iir_tv_left_key);
        visibilityValue = ((LinearLayout)findViewById(R.id.ah_layout_visibility_wind)).findViewById(R.id.iir_tv_left_value);
        windLabel = ((LinearLayout)findViewById(R.id.ah_layout_visibility_wind)).findViewById(R.id.iir_tv_right_key);
        windValue = ((LinearLayout)findViewById(R.id.ah_layout_visibility_wind)).findViewById(R.id.iir_tv_right_value);


        presenter = new HomePresenter(this);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        String res = CommonUtils.getDateCurrentTimeZone(1547915239);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View inflatedLayout= inflater.inflate(R.layout.layout_favourite_cities, favCitiesContainer , false);
        favCitiesContainer.addView(inflatedLayout);

        // click events
        (inflatedLayout.findViewById(R.id.lfc_tv_fav_one)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData(AppConstants.FAVOURITE_CITY_ONE);
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_fav_two)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData(AppConstants.FAVOURITE_CITY_TWO);
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_fav_three)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData(AppConstants.FAVOURITE_CITY_THREE);
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator slideAnimator = ValueAnimator
                        .ofInt(rlDetails.getHeight(), CommonUtils.dpToPx(48))
                        .setDuration(300);


// we want to manually handle how each tick is handled so add a
// listener
                slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // get the value the interpolator is at
                        Integer value = (Integer) animation.getAnimatedValue();
                        // I'm going to set the layout's height 1:1 to the tick
                        rlFavourite.getLayoutParams().height = value.intValue();
                        // force all layouts to see which ones are affected by
                        // this layouts height change
                        rlFavourite.requestLayout();
                    }
                });

                // create a new animationset
                AnimatorSet set = new AnimatorSet();
// since this is the only animation we are going to run we just use
// play
                set.play(slideAnimator);
// this is how you set the parabola which controls acceleration
                set.setInterpolator(new AccelerateDecelerateInterpolator());
// start the animation
                set.start();
//                ResizeAnimation resizeAnimation = new ResizeAnimation(
//                        rlFavourite,
//                        100,
//                        500
//
//                );
//                resizeAnimation.setDuration(200);
//                rlFavourite.startAnimation(resizeAnimation);
//                alphaAnimation(etCity, 0, 1);
                favExpanded = false;
            }
        });
    }

    @OnClick({R.id.cs_rl_celsius, R.id.cs_rl_fahrenheit, R.id.ah_view_tint})
    protected void onClickCelsius(View view){
        if (view.getId() == R.id.cs_rl_celsius){
            rl_celsius.setBackgroundResource(R.drawable.shape_rectangle_left_filled_rounded);
            rl_fahrenheit.setBackgroundResource(R.drawable.shape_rectangle_right_rounded);
            tv_label_celsius.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tv_label_fahrenheit.setTextColor(getResources().getColor(R.color.white));
            temperature.setText(tempCelsius + "°C");
        }else if(view.getId() == R.id.cs_rl_fahrenheit) {
            rl_fahrenheit.setBackgroundResource(R.drawable.shape_rectangle_right_filled_rounded);
            rl_celsius.setBackgroundResource(R.drawable.shape_rectangle_left_rounded);
            tv_label_celsius.setTextColor(getResources().getColor(R.color.white));
            tv_label_fahrenheit.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            temperature.setText(tempFahrenheit  + "°F" );
        }else {
//            if(favExpanded){
//                ResizeAnimation resizeAnimation = new ResizeAnimation(
//                        rlFavourite,
//                        CommonUtils.dpToPx(48),
//                        rlDetails.getHeight()
//                );
//                resizeAnimation.setDuration(200);
//                rlFavourite.startAnimation(resizeAnimation);
//                alphaAnimation(etCity, 0, 1);
//                favExpanded = false;
//            }
        }

    }

    private void mapData(WeatherResponse response){
        tempCelsius = CommonUtils.kelvinToCelsius(response.getMain().getTemp());
        tempFahrenheit = CommonUtils.kelvinToFahrenheit(response.getMain().getTemp());
        temperature.setText(tempCelsius + "°");

        int newWeatherCode = response.getWeather().get(0).getId() / 100 * 100;
        CommonUtils.changeImageBackground(imgWeatherTheme, newWeatherCode);
        tagLine.setText(response.getWeatherTagLine(newWeatherCode));
    }

    @OnTextChanged(R.id.ah_et_city)
    protected void onTextChanged(CharSequence text) {
        String featureName = text.toString();
        //Log.d("gringe", featureName);
        btnGetWeather.setVisibility(featureName.length() > 0? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.ah_tv_favourite_label)
    protected void expand()
    {


        if (!favExpanded){

            ValueAnimator slideAnimator = ValueAnimator
                    .ofInt(rlFavourite.getHeight(), rlDetails.getHeight())
                    .setDuration(300);


// we want to manually handle how each tick is handled so add a
// listener
            slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // get the value the interpolator is at
                    Integer value = (Integer) animation.getAnimatedValue();
                    // I'm going to set the layout's height 1:1 to the tick
                    rlFavourite.getLayoutParams().height = value.intValue();
                    // force all layouts to see which ones are affected by
                    // this layouts height change
                    rlFavourite.requestLayout();
                }
            });

            // create a new animationset
            AnimatorSet set = new AnimatorSet();
// since this is the only animation we are going to run we just use
// play
            set.play(slideAnimator);
// this is how you set the parabola which controls acceleration
            set.setInterpolator(new AccelerateDecelerateInterpolator());
// start the animation
            set.start();
            // rlFavourite.animate().scaleY(-100f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);

//            ResizeAnimation resizeAnimation = new ResizeAnimation(
//                    rlFavourite,
//                    500,
//                    100
//            );
//            resizeAnimation.setDuration(200);
//            rlFavourite.startAnimation(resizeAnimation);
//            alphaAnimation(etCity, 1, 0);
            favExpanded = true;


        }else {
            alphaAnimation(etCity, 1, 0);
        }
    }


    @OnClick(R.id.ah_ib_get_weather)
    protected void getWeather(){
        rlDetails.setVisibility(View.INVISIBLE);

        btnGetWeather.startAnimation(animation1);

        KeyboardUtils.hideSoftInput(this);

        fetchWeatherData(etCity.getText().toString());
    }

    private void fetchWeatherData(final String city){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherResponse> call = apiInterface.getWeatherDetails(city, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (response.isSuccessful()){
                    tempCelsius = CommonUtils.kelvinToCelsius(response.body().getMain().getTemp());
                    tempFahrenheit = CommonUtils.kelvinToFahrenheit(response.body().getMain().getTemp());
                    temperature.setText(tempCelsius + "°");

                    int newWeatherCode = response.body().getWeather().get(0).getId() / 100 * 100;
                    CommonUtils.changeImageBackground(imgWeatherTheme, newWeatherCode);
                    tagLine.setText(response.body().getWeatherTagLine(newWeatherCode));

                    sunriseLabel.setText("SUNRISE");
                    sunriseValue.setText(response.body().getSys().getSunrise());
                    sunsetLabel.setText("SUNSET");
                    sunsetValue.setText(response.body().getSys().getSunset());

                    pressureLabel.setText("PRESSURE");
                    pressureValue.setText(response.body().getMain().getPressure());
                    humidityLabel.setText("HUMIDITY");
                    humidityValue.setText(response.body().getMain().getHumidity());

                    visibilityLabel.setText("VISIBILITY");
                    visibilityValue.setText(response.body().getVisibility());
                    windLabel.setText("WIND");
                    windValue.setText(response.body().getWind().getSpeed());
                    btnGetWeather.clearAnimation();
                    rlDetails.setVisibility(View.VISIBLE);
                    Toast.makeText(HomeActivity.this, ""+rlDetails.getHeight() , Toast.LENGTH_SHORT).show();
                    etCity.setText(city);
                }else {
                    Toast.makeText(HomeActivity.this, "Not success", Toast.LENGTH_SHORT).show();
                }
                //mapData();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                btnGetWeather.clearAnimation();
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                // Log error here since request failed
                Log.e("Homeerror", t.toString());
            }

        });
    }

    @Override
    public void getWeather(String info) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    public void alphaAnimation(View view, float start, float end){
        AlphaAnimation animation1 = new AlphaAnimation(start, end);
        animation1.setDuration(200);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }
}
