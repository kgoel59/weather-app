package com.catbro.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    private  CurrentWeather mCurrentWeather;

    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTempratureLabel;
    @BindView(R.id.humidityLabel) TextView mHumidityLabel;
    @BindView(R.id.precipLabel) TextView mPrecipLabel;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImage) ImageView mIconImage;
    @BindView(R.id.refresh) ImageView mRefresh;
    @BindView(R.id.progressBar) ProgressBar mPbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this); //Bind butterKnife

        mPbar.setVisibility(View.INVISIBLE);

        final double latitude  = 30.8481642;
        final double longitude = 76.8273472;

        mRefresh.setOnClickListener(v -> getForecast(latitude,longitude));

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        String apiHost = "https://api.darksky.net/forecast";
        String apiKey = "ebca1ec0a4c5f3bdace33723d64c67cc";

        String apiURL = apiHost + "/" + apiKey + "/" + latitude + "," + longitude;

        if(isNetworkAvailable()) {
            runOnUiThread(() -> toggleRefresh());

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(apiURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> toggleRefresh());
                    Log.e(TAG, "Exception Caught", e);
                }

                @Override
                public void onResponse(Call call, Response response)  {
                    try {
                        runOnUiThread(() -> toggleRefresh());
                        String jsonData =  response.body().string();
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(() -> updateDisplay());
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                }
            });
        } else {
            Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mPbar.getVisibility() == View.INVISIBLE) {
            mPbar.setVisibility(View.VISIBLE);
            mRefresh.setVisibility(View.INVISIBLE);
        }
        else {
            mPbar.setVisibility(View.INVISIBLE);
            mRefresh.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTempratureLabel.setText(Integer.toString(mCurrentWeather.getTemprature()));
        mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        mHumidityLabel.setText(Double.toString(mCurrentWeather.getHumidity()));
        mPrecipLabel.setText(mCurrentWeather.getPrecipChance() + " %");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImage.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws org.json.JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemprature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return  isAvailable;
    }

    private  void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getSupportFragmentManager(),"error_dialog");
    }
}
