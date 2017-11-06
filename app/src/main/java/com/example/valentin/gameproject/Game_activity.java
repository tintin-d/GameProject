package com.example.valentin.gameproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.GenericArrayType;
import java.nio.Buffer;
import java.util.Locale;
import android.Manifest;

/**
 * Created by valentin on 12/10/17.
 */

public class Game_activity extends Activity implements SensorEventListener{

    //gestion des capteurs
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean accelSopported;
    //notre vue personnalisée
    private Game_view gameView;
    //accélération en x et en y sur les axes des capteurs
    private float speedX;
    private float speedY;
    //notre layout de score et ses composants
    private RelativeLayout scoreview;
    private EditText pseudoEdit;
    private TextView scoreText;
    private TextView curScore;
    private TextView latText;
    private TextView lonText;
    private final String FILENAME = "scores_file";//fichier de sauvegarde
    // Request code to use when requesting location permission
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        //Relative layout des scores
        scoreview=findViewById(R.id.saveScore);
        //entrée du pseudo
        pseudoEdit=findViewById(R.id.pseudoLabel);
        //Score final de la partie
        scoreText=findViewById(R.id.monScore);
        //latitude
        latText=findViewById(R.id.mlatitude);
        //longitude
        lonText=findViewById(R.id.mlongitude);
        //score en bas à gauche de l'écran
        curScore=findViewById(R.id.curScore);
        //vue dans laquelle se déroule la partie
        gameView=findViewById(R.id.game_view);
        //Debut des services de localisation
        beginLocationupsate(savedInstanceState);

        //implémentation des capteurs: on choisis l'accéléromètre avec le delai "fastest" pour
        //que les mouvements paraissent plus fluides.
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelSopported=mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        speedX=speedY=1;
        Log.d("create","gameAct");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //les calculs suivants permettent d'utiliser le capteur correctement.
                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];
                double xAngle = Math.atan( ax / (Math.sqrt((ay*ay) + (az*az))));
                xAngle *= 180.00;
                xAngle /= 3.141592;
                speedX=-(float)(xAngle/10);
                move();
                break;
            default:
                break;
    }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onPause(){
        //on met en pause tous les procesus qui écoutent les capteurs du téléphone
        mSensorManager.unregisterListener(this, mAccelerometer);
        gameView.onPauseMySurfaceView();
        super.onPause();
        stopLocationUpdates();
    }

    public void resume(){
        gameView.onResumeMySurfaceView();
    }

    @Override
    public void onResume(){
        //on réactive les services necessaires.
        super.onResume();
        resume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // We request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else
            startLocationUpdates();


    }

    //methode appelée à chaque rafraichissements du capteur
    public void move(){
        //si game over est a vrai on lance la methode qui va permettre d'avoir l'écran de auvegarde.
        if(gameView.getGameOver()) {
            over(null);
        }
        else {
            //si game over est a faux on rafraichit les coordonées
            float curX = gameView.getMyX();
            gameView.setMyX((curX + speedX));
            curScore.setText("Score: "+gameView.getMonScore());
            gameView.invalidate();
            Log.d("target",""+gameView.getTargetHashMap().isEmpty());
            //quand le nombre de cibles est a zero on en remet
            if(gameView.getTargetHashMap().isEmpty()){
                gameView.insertTarget(1,6);
            }
        }
    }

    //si jeu terminé game over a vrai.
    public void over(View v){
        gameView.setGameOver(true);
        scoreText.setText(""+gameView.getMonScore());
        //on affiche 'écran de sauvegarde
        scoreview.setVisibility(View.VISIBLE);
        //on appele la bonne methode sur notre vue
        //(nécessaire à cause des deux threads différentes.)
        gameView.gameOver(true);
    }


    //si sauvegardé on reprends le jeu
    public void saveScore(View v) {
        //Un petit toast affiché si  on a oublié de mettre un pseudo
        //(déclanche des erreurs sinon)
        if(pseudoEdit.getText().toString().matches("")){
            Toast t=Toast.makeText(getApplicationContext(),"Veuillez entrer un pseudo",Toast.LENGTH_LONG);
            t.show();
        }else {
            //on relance le jeu après avoir enregistré les données
            //on remet aussi les donées de jeu à zero
            gameView.gameOver(false);
            scoreview.setVisibility(View.INVISIBLE);
            writeScore();
            gameView.setMonScore(0);
        }
    }



//Ecriture du score courant dans la base de sdonnéees.

    public void writeScore(){
        String sep="\n";
        StringBuilder total = new StringBuilder();
        //séparé en 2 blocs car l'un gère l'écriture et l'autre la lecture. Si un seul block:
        // l'échec de l'un entraine la non réalisation de l'autre.
        //block lecture
            try {
                FileInputStream fis = openFileInput(FILENAME);
                BufferedReader r = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                fis.close();
            }catch (IOException e){Log.d("File","Impossible de lire ou de trouver le ficgier");}
        //block écriture
        //Il faut bien respecter cet enchainement pour que la lecture du fichier se fasse sans problem
            try{
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(("" + total).getBytes());
                fos.write(pseudoEdit.getText().toString().replace(" ","").getBytes());
                fos.write(sep.getBytes());
                fos.write(scoreText.getText().toString().getBytes());
                fos.write(sep.getBytes());
                fos.write(latText.getText().toString().getBytes());
                fos.write(sep.getBytes());
                fos.write(lonText.getText().toString().getBytes());
                fos.write(sep.getBytes());
                fos.close();
                //un petit toast pour rassurer le joueur
                Toast t=Toast.makeText(getApplicationContext(),"Score sauvegardé",Toast.LENGTH_LONG);
                t.show();
            } catch (IOException e) {
                Log.d("File: ", "impossible de modifier le fichier");
            }
    }


      //-----------------------------//
     //  Gestion de la loalisation  //
    //-----------------------------//
    public void beginLocationupsate(Bundle savedInstanceState){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();
        createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // Update UI with the most recent location.
                updateUI(locationResult.getLastLocation());
            }
        };
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // We request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else
            this.updateUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();

                } else {
                    Toast.makeText(this, ("Permission refusée"), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void updateUI() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latText.setText(String.format(Locale.ENGLISH, "%.4f",location.getLatitude()));
                                lonText.setText(String.format(Locale.ENGLISH, "%.4f",location.getLongitude()));
                            }
                        }
                    });
        } catch (SecurityException e) {

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateUI(Location location) {
        if (location != null) {
            latText.setText(String.format(Locale.ENGLISH, "%.4f",location.getLatitude()));
            lonText.setText(String.format(Locale.ENGLISH, "%.4f",location.getLongitude()));
        }
    }


    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        try {
            // Initialize UI with the last known location.
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            updateUI(location);
                        }
                    });

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        } catch (SecurityException e) {
            Toast.makeText(this, "Erreur de sécurité", Toast.LENGTH_LONG).show();
        }
    }


}
