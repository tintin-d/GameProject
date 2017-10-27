package com.example.valentin.gameproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.nio.Buffer;

/**
 * Created by valentin on 12/10/17.
 */

public class Game_activity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean accelSopported;
    private Game_view gameView;
    private boolean started=false;
    private float speedX;
    private float speedY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        gameView=findViewById(R.id.game_view);
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelSopported=mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        speedX=speedY=1;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];
                double xAngle = Math.atan( ax / (Math.sqrt((ay*ay) + (az*az))));
               // double yAngle = Math.atan( ay / (Math.sqrt((ax*ax) + (az*az))));

                xAngle *= 180.00;   //yAngle *= 180.00;
                xAngle /= 3.141592; //yAngle /= 3.141592;
                speedX=-(float)(xAngle/10);
                //speedY=(float)(yAngle/10);
                move();
                break;
            default:
                break;
    }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause(){
        mSensorManager.unregisterListener(this, mAccelerometer);
        gameView.onPauseMySurfaceView();
        super.onPause();
    }

    public void resume(){
        gameView.onResumeMySurfaceView();
        Log.d("test","test");
    }

    @Override
    public void onResume(){
        super.onResume();
        resume();
    }


    public void move(){
        float curX=gameView.getMyX();
        //float curY=gameView.getMyY();
        gameView.setMyX((curX+speedX));
       // gameView.setMyY((curY+speedY*1));
        gameView.invalidate();
    }

}
