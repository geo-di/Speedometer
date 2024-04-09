package com.example.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    TextView textView;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        database = openOrCreateDatabase("speeddb.db",MODE_PRIVATE,null);
        //database.execSQL("Delete from  Happened");
        database.execSQL("Create table if not exists Happened(" +
                "latitude Text," +
                "longitude Text," +
                "speed Text," +
                "timestamp Text Primary Key)");

    }


    public void showdata(View view) {

        openDataActivity();
    }

    private void openDataActivity() {

        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);

    }

    public void showsettings(View view) {

        openSettingsActivity();
    }

    private void openSettingsActivity() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showspeed(View view) {

        openSpeedActivity();

    }

    public void openSpeedActivity() {

        Intent intent = new Intent(this, SpeedActivity.class);
        startActivity(intent);
    }


}