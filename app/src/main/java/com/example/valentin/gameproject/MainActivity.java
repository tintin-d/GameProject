package com.example.valentin.gameproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;



public class MainActivity extends AppCompatActivity {


    public Button play;
    public Button score;
    private boolean inPlay=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=(Button)findViewById(R.id.btnPlay);
        score=(Button)findViewById(R.id.btnScore);
    }
    //on ajoute un menu option pour les scores
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.score:
                sendMessage(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //appel à score activity
    public void sendMessage(View view) {
        Intent intent = new Intent(this, Score_activity.class);
        startActivity(intent);
    }

    //appel à game activity
    public void playPush(View v){
        Intent intent = new Intent(this, Game_activity.class);
        startActivity(intent);
    }
}
