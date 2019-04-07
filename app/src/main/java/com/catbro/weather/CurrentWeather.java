package com.catbro.weather;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String mIcon;
    private long mTime;
    private String mTimeZone;
    private  double mTemprature;
    private  double mHumidity;
    private double mPrecipChance;
    private String mSummary;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
       // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconId = R.drawable.clear_day;

        if(mIcon.equals("clear-day"))
            iconId = R.drawable.clear_day;
        else if (mIcon.equals("clear-night"))
            iconId = R.drawable.clear_night;
        else if (mIcon.equals("rain"))
            iconId = R.drawable.rain;
        else if (mIcon.equals("snow"))
            iconId = R.drawable.snow;
        else if (mIcon.equals("sleet"))
            iconId = R.drawable.sleet;
        else if (mIcon.equals("wind"))
            iconId = R.drawable.wind;
        else if (mIcon.equals("fog"))
            iconId = R.drawable.fog;
        else if (mIcon.equals("cloudy"))
            iconId = R.drawable.cloudy;
        else if (mIcon.equals("partly-cloudy-day"))
            iconId = R.drawable.partly_cloudy;
        else if (mIcon.equals("partly-cloudy-night"))
            iconId = R.drawable.cloudy_night;

        return iconId;
    }

    public long getTime() {
        return mTime;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public  String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date date = new Date(getTime() * 1000);
        String timeString = formatter.format(date);
        return  timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double toCelsius()
    {
        return ((mTemprature-32)*5)/9;
    }
    public int getTemprature() {
        return (int)Math.round(toCelsius());
    }

    public void setTemprature(double temprature) {
        mTemprature = temprature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        return (int)Math.round(mPrecipChance * 100);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
