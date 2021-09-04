package com.programmingmanav.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Initialising values
    ConstraintLayout constraintLayout;
    LinearLayout placeLinearLayout;
    ImageView topCloud,groundCloud,sunMoon,middleCloud,loactionImageView;
    TextView placeName,disc,temp;
    Boolean isDay = true;
    Boolean dofade = true;
    String place = "Noida";
    EditText getLocationEditText;
    Button setLocationButton;
    int temperature;
    int temperature_max;
    int temperature_min;
    String discription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findView By ID;
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        temp = findViewById(R.id.temp);
        disc = findViewById(R.id.discription);
        topCloud = findViewById(R.id.TopImageView1);
        middleCloud = findViewById(R.id.TopImageView2);
        groundCloud = findViewById(R.id.TopImageView3);
        sunMoon = findViewById(R.id.Sun_Moon);
        placeName = findViewById(R.id.placeName);
        placeName.setText(place);
        getLocationEditText = findViewById(R.id.LocationEditText);
        setLocationButton = findViewById(R.id.setButton);
        loactionImageView = findViewById(R.id.locationImageView);
        placeLinearLayout = findViewById(R.id.setLoaction);

        //cloudAnimation.
        cloudAnimate();

        //setting values.
        setValues();

        //getting Wether
        getWeather();



    }

    public void cloudAnimate(){
        Animation animationm = AnimationUtils.loadAnimation(this , R.anim.floation_animation_m);
        Animation animationt = AnimationUtils.loadAnimation(this , R.anim.floating_animation);
        Animation animationg = AnimationUtils.loadAnimation(this , R.anim.floating_animation_g);
        topCloud.startAnimation(animationt);
        middleCloud.startAnimation(animationm);
        groundCloud.startAnimation(animationg);
    }
    public void setValues(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour < 6 || hour > 18){
            isDay = false;
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this , R.color.Night_Blue));
            sunMoon.setImageResource(R.drawable.moon);
            topCloud.setImageResource(R.drawable.final_clouds_night_t);
            middleCloud.setImageResource(R.drawable.final_clouds_night_m);
            groundCloud.setImageResource(R.drawable.final_clouds_night_g);
        } else {
            isDay = true;
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this , R.color.Day_Blue));
            sunMoon.setImageResource(R.drawable.sun);
            topCloud.setImageResource(R.drawable.final_clouds_t);
            middleCloud.setImageResource(R.drawable.final_clouds_m);
            groundCloud.setImageResource(R.drawable.final_clouds_d);
        }
    }
    public void getLocation(View view) {

        if(dofade){
            placeLinearLayout.animate().translationY(930);
            faid(dofade);
            dofade = false;
        }else{
            placeLinearLayout.animate().translationY(-1000);
            faid(dofade);
            dofade = true;
        }


    }
    public class getWeather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(urls[0]);
                httpURLConnection =(HttpURLConnection) url.openConnection();
                InputStream in = (InputStream) httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                return e.toString();
            }
        }
    }
    public void getWeather(){
        //Getting Temperature
        String result;
        getWeather gettemp = new getWeather();
        try {
            result = gettemp.execute("https://api.openweathermap.org/data/2.5/weather?q="+place+",&appid=7af368d8932f3cc7e05eb00719ecef80").get();
            String tempTemperature = result.substring(result.indexOf("\"temp\":") + 7 , result.indexOf(",",result.indexOf("\"temp\":") + 7));
            String tempWeather = result.substring(result.indexOf("\"main\":\"") + 8, result.indexOf("\"",result.indexOf("\"main\":\"") + 8));
            String temp_min = result.substring(result.indexOf("\"temp_min\":") + 11, result.indexOf(",",result.indexOf("\"temp_min\":") + 11));
            String temp_max = result.substring(result.indexOf("\"temp_max\":") + 11, result.indexOf(",",result.indexOf("\"temp_max\":") + 11));
            temperature_min = (int)(Double.parseDouble(temp_min) - 273);
            temperature_max = (int)(Double.parseDouble(temp_max) - 273);
            temperature = (int) (Double.parseDouble(tempTemperature) - 273.00);
            discription = tempWeather+" "+temperature_min+"/"+temperature_max+"°C";
            temp.setText(String.valueOf(temperature)+"°C");
            disc.setText(discription);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("temp",e.toString());
            temp.setText(e.toString()+"°");
        }
    }
    public void faid(Boolean x){
        if(x){
            loactionImageView.setAlpha(0.5F);
            loactionImageView.setEnabled(false);
            temp.setAlpha(0.5F);
            disc.setAlpha(0.5F);
            topCloud.setAlpha(0.2F);
            middleCloud.setAlpha(0.5F);
            groundCloud.setAlpha(0.2F);
            sunMoon.setAlpha(0.2F);
            placeName.setAlpha(0.5F);
        }else{
            place = getLocationEditText.getText().toString();
            getWeather();
            placeName.setText(place);
            loactionImageView.setAlpha(1F);
            loactionImageView.setEnabled(true);
            temp.setAlpha(1);
            disc.setAlpha(1);
            topCloud.setAlpha(1F);
            middleCloud.setAlpha(1F);
            groundCloud.setAlpha(1F);
            sunMoon.setAlpha(1F);
            placeName.setAlpha(1F);
        }
    }
}