package com.example.speedometer;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Switch;
import android.widget.TextView;
import java.lang.String;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.os.Bundle;

public class DataActivity extends AppCompatActivity {

    SQLiteDatabase database;

    Switch switchView;

    View view;

    String query;
    TextView datatext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        database = openOrCreateDatabase("speeddb.db",MODE_PRIVATE,null);

        switchView = findViewById(R.id.switch1);
        datatext = findViewById(R.id.textView7);

        checkswitch(view);

    }

    public String filterData(String query ) {

        Cursor cursor = database.rawQuery(query,null);
        StringBuilder data = new StringBuilder();
        while (cursor.moveToNext()){
            data.append("Latitude:"+cursor.getString(0)+"\n");
            data.append("Longitude:"+cursor.getString(1)+"\n");
            data.append("Speed:"+cursor.getString(2)+" m/s\n");


            Date date = new Date(Long.parseLong(cursor.getString(3)));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);

            data.append("Time:"+formattedDate+"\n");
            data.append("-----------------\n");


        }
        cursor.close();
        return data.toString();

    }

    public void checkswitch(View view){

        if(switchView.isChecked()) {
            switchView.setText("Show Recent");
            long weekago = System.currentTimeMillis() / 1000 - 7 * 24 * 3600;
            query = "Select * from Happened where cast(timestamp as integer) >= " + weekago;
        } else{
            switchView.setText("Show All");
            query = "Select * from Happened";
        }

        datatext.setText(filterData(query));

    }
}