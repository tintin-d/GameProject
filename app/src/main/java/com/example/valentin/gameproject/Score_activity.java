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
import android.widget.AdapterView;
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
import android.view.View.OnClickListener;

public class Score_activity extends ListActivity {

    private Button test;
    private ListView listScores;
    private ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private final String FILENAME = "scores_file";
    private ArrayList<Score> mesScores=new ArrayList<Score>();
    private ArrayList<Map<String,String>> sortedList;
    public static final String EXTRA_MESSAGE = "com.example.valentin.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_activity);

        listScores=(ListView)findViewById(android.R.id.list);

        //initialisation affichage score
        sortedList = new ArrayList<Map<String,String>>();
        readScores();
        displaySort();

        //Listener permettant d'afficher la carte.
        listScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tokens[]=adapterView.getAdapter().getItem(i).toString().split(" ");
                String ps=null;
                String sc=null;
                if(tokens[0].contains("1")) {
                     ps = tokens[0].substring(tokens[0].lastIndexOf("=") + 1, tokens[0].lastIndexOf(","));
                     sc = tokens[1].substring(tokens[1].lastIndexOf("=") + 1, tokens[1].lastIndexOf("}"));
                }
                else{
                     sc = tokens[0].substring(tokens[0].lastIndexOf("=") + 1, tokens[0].lastIndexOf(","));
                     ps = tokens[1].substring(tokens[1].lastIndexOf("=") + 1, tokens[1].lastIndexOf("}"));
                }

                for(Score key: mesScores) {
                    if(key.getPseudo().equals(ps) && key.getScore()==Integer.parseInt(sc))
                    map(key.getPseudo()+"/"+key.getScore()+"/"+key.getLatitude()+"/"+key.getLongitude());
                }
            }
        });
    }


    //ajout d'un score dans la liste des scores
    public void addCell(Score sc){
        mesScores.add(sc);
        Map<String, String> maplist= new HashMap<String, String>();
        maplist.put("ligne1", sc.getPseudo());
        maplist.put("ligne2", ""+sc.getScore());
        list.add(maplist);
    }


    public void displaySort(){
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
            String line;
            while ((line = r.readLine()) != null ) {
                //En temps normal l'écriture et la lscture des données dans notre fichier se fait de manière
                //symétrique. Aucun control n'a donc été réalisé vu qu'aucun problème ne devrait se poser.
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

    public void map(String message) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
