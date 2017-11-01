package com.example.valentin.gameproject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Score_activity extends ListActivity {

    private Button test;
    private ListView listScores;
    private ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public boolean mRequestingLocationUpdates;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private LocationCallback mLocationCallback;
    private final String REQUESTING_LOCATION_UPDATES_KEY="myKey";
    private final String FILENAME = "scores_file";
    private ArrayList<Score> mesScores=new ArrayList<Score>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_activity);

        listScores=(ListView)findViewById(android.R.id.list);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        readScores();
        //saveScores();
       displaySort();

    }



    public void addCell(Score sc){
        mesScores.add(sc);
        Map<String, String> maplist= new HashMap<String, String>();
        maplist.put("ligne1", sc.getPseudo());
        maplist.put("ligne2", ""+sc.getScore());
        list.add(maplist);
    }


    public void displaySort(){
        ArrayList<Map<String,String>> sortedList = new ArrayList<Map<String,String>>();

        while(!(list.isEmpty())){
            Map<String, String> curMap=new HashMap<String,String>();
            curMap=(list.get(0));
            int iter=0;
            for(Map map: list){
                if(Integer.parseInt((String)map.get("ligne2")) > Integer.parseInt((String)curMap.get("ligne2"))){
                    curMap=map;
                }
            }
            //n'affichera que les 20 premiers meilleurs scores
            //Les autres faisant tt de même partie du fichier sauvegardé
            //modifier??
            int nbElem=1;
            if(nbElem<=20){
                sortedList.add(curMap);
                nbElem++;
            }

            list.remove(curMap);
        }
        String[] from = { "ligne1", "ligne2" };
        int[] to = { android.R.id.text1, android.R.id.text2 };
        SimpleAdapter adapter = new SimpleAdapter(this, sortedList, android.R.layout.simple_list_item_2, from, to);
        setListAdapter(adapter);
    }


    public void saveScores(){
        String sep="\n";

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for(Score sc: mesScores){

                fos.write(sc.getPseudo().getBytes());
                fos.write(sep.getBytes());
                fos.write((""+sc.getScore()).getBytes());
                fos.write(sep.getBytes());
                fos.write((""+sc.getLatitude()).getBytes());
                fos.write(sep.getBytes());
                fos.write((""+sc.getLongitude()).getBytes());
                fos.write(sep.getBytes());

            }
            fos.close();
        } catch (IOException e) {
            Log.d("File: ", "impossible de modifier le fichier");
        }


    }

    public void readScores(){
        try{
            FileInputStream fis = openFileInput(FILENAME);

            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null ) {
                //total.append(line).append('\n');
                String pseudo=line;
                int score=Integer.parseInt(r.readLine());
                float lat=Float.parseFloat(r.readLine());
                float lon=Float.parseFloat(r.readLine());
                addCell(new Score(pseudo,score,lat,lon));
            }

            fis.close();
        }catch (IOException e){
            Log.d("File: ", "impossible de lire le fichier");
        }
    }


    public void map(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
