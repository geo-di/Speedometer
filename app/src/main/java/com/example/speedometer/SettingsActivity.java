package com.example.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText numtext;

    TextView current;

    int limit;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        numtext = findViewById(R.id.editTextNumber);

        current = findViewById(R.id.textView6);

        sp = getSharedPreferences("speedlimit" , Context.MODE_PRIVATE);

        current.setText(getString(R.string.currentlimit,sp.getInt("speedlimit",-1)));
    }



    public void savelimit(View view){

        limit = Integer.parseInt(numtext.getText().toString());

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("speedlimit",limit);
        editor.apply();
        current.setText(getString(R.string.currentlimit,sp.getInt("speedlimit",-1)));

        Toast.makeText(this , "Settings Applied!" , Toast.LENGTH_LONG).show();
    }
}