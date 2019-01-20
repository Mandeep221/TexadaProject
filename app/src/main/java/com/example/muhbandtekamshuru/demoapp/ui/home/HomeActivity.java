package com.example.muhbandtekamshuru.demoapp.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhbandtekamshuru.demoapp.R;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiClient;
import com.example.muhbandtekamshuru.demoapp.data.network.ApiInterface;
import com.example.muhbandtekamshuru.demoapp.data.network.model.Movie;
import com.example.muhbandtekamshuru.demoapp.data.network.model.MovieResponse;
import com.example.muhbandtekamshuru.demoapp.data.network.model.WeatherResponse;
import com.example.muhbandtekamshuru.demoapp.utils.CommonUtils;
import com.example.muhbandtekamshuru.demoapp.utils.KeyboardUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    int tempCelsius, tempFahrenheit;

    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        String res = CommonUtils.getDateCurrentTimeZone(1547915239);
    }

    @OnClick(R.id.cs_rl_celsius)
    protected void onClickCelsius(){
        rl_celsius.setBackgroundResource(R.drawable.shape_rectangle_left_filled_rounded);
        rl_fahrenheit.setBackgroundResource(R.drawable.shape_rectangle_right_rounded);
        tv_label_celsius.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_label_fahrenheit.setTextColor(getResources().getColor(R.color.white));
        temperature.setText(tempCelsius + "°C");
    }

    @OnClick(R.id.cs_rl_fahrenheit)
    protected void onClickFahrenheit(){
        rl_fahrenheit.setBackgroundResource(R.drawable.shape_rectangle_right_filled_rounded);
        rl_celsius.setBackgroundResource(R.drawable.shape_rectangle_left_rounded);
        tv_label_celsius.setTextColor(getResources().getColor(R.color.white));
        tv_label_fahrenheit.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        temperature.setText(tempFahrenheit  + "°F" );
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

    @OnClick(R.id.ah_ib_get_weather)
    protected void getWeather(){

        KeyboardUtils.hideSoftInput(this);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherResponse> call = apiInterface.getWeatherDetails(etCity.getText().toString(), API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                ///List<Movie> movies = response.body().getResults();
                //Log.d(TAG, "Number of movies received: " + movies.size());
                // String main  = response.body().getWeather().get(0).getMain();
                //Toast.makeText(HomeActivity.this, main, Toast.LENGTH_LONG).show();

                tempCelsius = CommonUtils.kelvinToCelsius(response.body().getMain().getTemp());
                tempFahrenheit = CommonUtils.kelvinToFahrenheit(response.body().getMain().getTemp());
                temperature.setText(tempCelsius + "°");

                int newWeatherCode = response.body().getWeather().get(0).getId() / 100 * 100;
                CommonUtils.changeImageBackground(imgWeatherTheme, newWeatherCode);
                tagLine.setText(response.body().getWeatherTagLine(newWeatherCode));
                //mapData();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Log error here since request failed
                // Log.e(TAG, t.toString());
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
}
