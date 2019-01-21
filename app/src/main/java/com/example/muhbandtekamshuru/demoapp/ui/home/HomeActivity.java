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

import com.example.muhbandtekamshuru.demoapp.MainContract;
import com.example.muhbandtekamshuru.demoapp.R;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiClient;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiInterface;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiService;
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

    @BindView(R.id.ah_ll_input_container)
    LinearLayout inputContainer;

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


        presenter = new HomePresenter(this, new ApiService());


         LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        final View inflatedLayout= inflater.inflate(R.layout.layout_favourite_cities, favCitiesContainer , false);
        favCitiesContainer.addView(inflatedLayout);

        // click events
        (inflatedLayout.findViewById(R.id.lfc_tv_fav_one)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getWeatherData(AppConstants.FAVOURITE_CITY_ONE);
                (inflatedLayout.findViewById(R.id.lfc_tv_cancel)).performClick();
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_fav_two)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getWeatherData(AppConstants.FAVOURITE_CITY_TWO);
                (inflatedLayout.findViewById(R.id.lfc_tv_cancel)).performClick();
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_fav_three)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getWeatherData(AppConstants.FAVOURITE_CITY_THREE);
                (inflatedLayout.findViewById(R.id.lfc_tv_cancel)).performClick();
            }
        });

        (inflatedLayout.findViewById(R.id.lfc_tv_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFavoritesAnimation(rlDetails.getHeight(), CommonUtils.dpToPx(48));
                favExpanded = false;
                alphaAnimation(inputContainer, 0, 1);
            }
        });
    }

    private void handleFavoritesAnimation(int startValue, int endValue){
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(startValue, endValue)
                .setDuration(300);
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

        if (startValue > endValue){
            favExpanded = true;
            alphaAnimation(inputContainer, 1, 0);
        }else
        {
            favExpanded = false;
            alphaAnimation(inputContainer, 0, 1);
        }
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

        }

    }

    @OnTextChanged(R.id.ah_et_city)
    protected void onTextChanged(CharSequence text) {
        String featureName = text.toString();
        btnGetWeather.setVisibility(featureName.length() > 0? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.ah_tv_favourite_label)
    protected void expand()
    {
        if (!favExpanded){
                handleFavoritesAnimation(rlFavourite.getHeight(), rlDetails.getHeight());
            favExpanded = true;
            alphaAnimation(inputContainer, 1, 0);
        }
    }


    @OnClick(R.id.ah_ib_get_weather)
    protected void getWeather(){
        presenter.getWeatherData(etCity.getText().toString());
    }


    @Override
    public void handleCityNameValidation() {
        Toast.makeText(HomeActivity.this, "Please enter city name..",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(WeatherResponse response) {
        rl_celsius.setBackgroundResource(R.drawable.shape_rectangle_left_filled_rounded);
        rl_fahrenheit.setBackgroundResource(R.drawable.shape_rectangle_right_rounded);
        tv_label_celsius.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_label_fahrenheit.setTextColor(getResources().getColor(R.color.white));

        tempCelsius = CommonUtils.kelvinToCelsius(response.getMain().getTemp());
        tempFahrenheit = CommonUtils.kelvinToFahrenheit(response.getMain().getTemp());
        temperature.setText(tempCelsius + "°C");

        int newWeatherCode;
        if (response.getWeather().get(0).getId() == 801){
            newWeatherCode = 900;
        }else {
            newWeatherCode = response.getWeather().get(0).getId() / 100 * 100;
        }
        CommonUtils.changeImageBackground(imgWeatherTheme, newWeatherCode);
        String line = response.getWeatherTagLine(newWeatherCode);
        String[] parts = line.split(":");
        tagLine.setText(parts[0]);
        subTagLine.setText(parts[1]);

        sunriseLabel.setText("SUNRISE");
        sunriseValue.setText(response.getSys().getSunrise());
        sunsetLabel.setText("SUNSET");
        sunsetValue.setText(response.getSys().getSunset());

        pressureLabel.setText("PRESSURE");
        pressureValue.setText(response.getMain().getPressure());
        humidityLabel.setText("HUMIDITY");
        humidityValue.setText(response.getMain().getHumidity());

        visibilityLabel.setText("VISIBILITY");
        visibilityValue.setText(response.getVisibility());
        windLabel.setText("WIND");
        windValue.setText(response.getWind().getSpeed());
    }

    @Override
    public void onFailure(String error) {
        Log.e("onFailure", error);
    }

    @Override
    public void handleSceneDuringApiRequest() {
        rlDetails.setVisibility(View.INVISIBLE);
        btnGetWeather.startAnimation(animation1);
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public void handleSceneAfterApiRequest() {
        btnGetWeather.clearAnimation();
        rlDetails.setVisibility(View.VISIBLE);
    }

    public void alphaAnimation(View view, float start, float end){
        AlphaAnimation animation1 = new AlphaAnimation(start, end);
        animation1.setDuration(200);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }
}
