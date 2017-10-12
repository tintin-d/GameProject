package com.example.valentin.gameproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void playPush(View v){
        inPlay=true;
        play.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
    }
}
