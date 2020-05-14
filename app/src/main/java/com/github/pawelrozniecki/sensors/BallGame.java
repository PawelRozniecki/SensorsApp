package com.github.pawelrozniecki.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class BallGame extends AppCompatActivity implements SensorEventListener {


    private ShapeDrawable ball = new ShapeDrawable();
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private AnimatedView animatedView = null;
    public static int xAcceleration, yAcceleration;
    public static int counter = 0;
    public static float xVelocity, yVelocity = 0.0f;
    public static float xPosition,yPosition = 0.0f;

    boolean wallTouched = false;

    public int displayWidth, displayHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter = 0;
        animatedView = new AnimatedView(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels-100;
        displayHeight =  displayMetrics.heightPixels-100;


        xAcceleration = displayWidth/2;
        yAcceleration = displayHeight/2;

        Log.v("Y Size:" , Integer.toString(displayHeight));
        Log.v("X Size:" , Integer.toString(displayWidth));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        }

        setContentView(animatedView);


    }
    @Override
    protected  void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);

    }
    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            xAcceleration-= (int) event.values[0];
            yAcceleration += (int) event.values[1];

            if (xAcceleration > displayWidth) {
                xAcceleration = displayWidth;
                gameOver();


            } else if (xAcceleration < 0) {
                xAcceleration = 0;
                gameOver();

            }

            if (yAcceleration > displayHeight-100) {

                yAcceleration = displayHeight-100;
                gameOver();
            } else if (yAcceleration < 0) {
                yAcceleration = 0;
                gameOver();
            }

        }

    }

        @Override
        public void onAccuracyChanged (Sensor sensor,int accuracy){

        }

        public void gameOver(){

            wallTouched = true;
            counter++;
            resetBallPosition();

            if (counter>0){
                onPause();

                Toast.makeText(getApplicationContext(), "Game over. Going back to menu", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(BallGame.this, MainActivity.class);
                    startActivity(intent);
                }, 1000);
            }

        }

        public void resetBallPosition(){

        yAcceleration = displayHeight/2;
        xAcceleration = displayWidth/2;

        }
        public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView{

            static final int width = 100;
            static final int height = 100;

            public AnimatedView(Context context) {

                super(context);
                ball = new ShapeDrawable(new OvalShape());
                ball.getPaint().setColor(Color.parseColor("#4b7bec"));

            }
            Paint p = new Paint();


            @Override
            protected void onDraw(Canvas canvas) {


                p.setColor(Color.BLACK);
                p.setTextSize(80);


                ball.setBounds(xAcceleration, yAcceleration, xAcceleration+ width, yAcceleration+ height);
                canvas.drawColor(Color.parseColor("#d1d8e0"));
                canvas.drawText("Touched wall: " + Integer.toString(counter), 100,100, p);
                ball.draw(canvas);
                invalidate();

            }

        }
    }
