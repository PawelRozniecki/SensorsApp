package com.github.pawelrozniecki.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SensorsActivity extends AppCompatActivity  implements  SensorEventListener{


    private SensorManager sensorManager;
    private Sensor accelerometer,ambientTemperatureSensor, gyroSensor, pressureSensor;
    private List<Sensor> deviceSensors;
    private  RecyclerViewerAdapter adapter;
    private RecyclerView recyclerView;
    private Dialog dialog;

    private Button acc_btn;
    private Button dismiss;
    private TextView z,y,x, ambientTemperature, gyroX, gyroY, gyroZ, pressureValue;
    private float lastX, lastY, lastZ;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        //Sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerometer!=null){
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("debug", "Registered accelerometer listener");

        }
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(ambientTemperatureSensor!=null){
            sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("debug", "Registred temperature listener");

        }

        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroSensor!=null){
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("debug", "Registred gyro listener");

        }

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor!=null){
            sensorManager.registerListener(this,pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        //Layout items
        dialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_accelometer);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewer);
        acc_btn = (Button) findViewById(R.id.btn_accelometer);
        dismiss = (Button) dialog.findViewById(R.id.dismiss);

        //Values

        x = dialog.findViewById(R.id.xVal);
        y = dialog.findViewById(R.id.yVal);
        z = dialog.findViewById(R.id.zVal);
        gyroX  = dialog.findViewById(R.id.gyroX);
        gyroY = dialog.findViewById(R.id.gyroY);
        gyroZ =  dialog.findViewById(R.id.gyroZ);
        ambientTemperature = dialog.findViewById(R.id.ambient_temp);
        pressureValue = dialog.findViewById(R.id.pressure);

        //Setting up Recycler viewer adapter
        adapter = new RecyclerViewerAdapter(deviceSensors);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //click listeners

        acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();
        sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    protected  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){


    }

    @Override
     public void onSensorChanged(SensorEvent event){

        Sensor sensor = event.sensor;
        if(sensor.getType()== Sensor.TYPE_ACCELEROMETER){

            displayStartValues();
            displayCurrentValues();

            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);
        }

         else if(sensor.getType() == Sensor.TYPE_LIGHT){

            float ambientTemp = event.values[0];
            ambientTemperature.setText("Illuminance: " + Float.toString(ambientTemp)  +" lx");

        }
         else if(sensor.getType()==Sensor.TYPE_GYROSCOPE){

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            gyroX.setText("X: " + Float.toString(x)+ " rad/s");
            gyroY.setText("Y: "  + Float.toString(y) + " rad/s" );
            gyroZ.setText("X: " + Float.toString(z) + " rad/s");

        }
         else if(sensor.getType()==Sensor.TYPE_PRESSURE){
             float pressure = event.values[0];
             pressureValue.setText(Float.toString(pressure) + " hPa");

        }

    }


    public void displayStartValues() {
        x.setText("X: 0.0");
        y.setText("Y: 0.0");
        z.setText("Z: 0.0");
    }
    public void displayCurrentValues() {

        x.setText("X: " + Float.toString(deltaX));
        y.setText("Y: " +  Float.toString(deltaY));
        z.setText("Z: "+ Float.toString(deltaZ));
    }






}
