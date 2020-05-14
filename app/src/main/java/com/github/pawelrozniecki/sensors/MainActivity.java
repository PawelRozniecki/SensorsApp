package com.github.pawelrozniecki.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CardView sensorBtn, gameBtn, gpsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorBtn = findViewById(R.id.sensor_btn);
        gameBtn = findViewById(R.id.game);
        gpsBtn = findViewById(R.id.gps);


        sensorBtn.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, SensorsActivity.class);
            startActivity(intent);
        });

        gameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BallGame.class);
            startActivity(intent);
        });

        gpsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GpsActivity.class);
            startActivity(intent);
        });



    }


}
