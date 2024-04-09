package com.example.speedometer;

import java.util.Locale;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.util.Log;
import android.content.res.TypedArray;
import android.speech.tts.TextToSpeech;
import android.database.SQLException;


import android.os.Bundle;
import android.widget.TextView;

public class SpeedActivity extends AppCompatActivity implements LocationListener  {

    LocationManager locationManager;
    TextView km,m,slow;
    SQLiteDatabase database;

    TextToSpeech tts;

    int lim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        database = openOrCreateDatabase("speeddb.db",MODE_PRIVATE,null);


        SharedPreferences sp = getApplication().getSharedPreferences("speedlimit", Context.MODE_PRIVATE);
        lim = sp.getInt("speedlimit" , -1);

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        km = findViewById(R.id.textView2);
        m = findViewById(R.id.textView3);
        slow = findViewById(R.id.textView4);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        km.setText(getString(R.string.speedmsg,location.getSpeed()*3.6,"km/h"));
        m.setText(getString(R.string.speedmsg, location.getSpeed(),"m/s"));
        if (location.getSpeed() > lim) {

            if(km.getCurrentTextColor() != getColor(R.color.red)){

                try {
                    database.execSQL("Insert into Happened (latitude, longitude, speed, timestamp) values ('" +
                            location.getLatitude() + "','" +
                            location.getLongitude() + "','" +
                            location.getSpeed() + "','" +
                            location.getTime() + "');");
                } catch (SQLException e) {
                    Log.e("DatabaseError", e.getMessage());
                }
            }


            km.setTextColor(getColor(R.color.red));
            m.setTextColor(getColor(R.color.red));
            slow.setText(getString(R.string.slow));
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.blue));
            tts.speak(getString(R.string.slow),TextToSpeech.QUEUE_FLUSH, null);




        } else {
            km.setTextColor(getColor(R.color.green));
            m.setTextColor(getColor(R.color.green));
            slow.setText("");

            TypedArray ta = obtainStyledAttributes(new int[]{android.R.attr.colorBackground});
            int backgroundColor = ta.getColor(0, 0);
            ta.recycle();


            getWindow().getDecorView().setBackgroundColor(backgroundColor);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}