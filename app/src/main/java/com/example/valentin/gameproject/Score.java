package com.example.valentin.gameproject;

/**
 * Created by valentin on 01/11/17.
 */

public class Score {


    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    private String pseudo;
    private int score;
    private float latitude;
    private float longitude;

    public Score(String p, int s, float lt, float lg){
        pseudo=p; score=s; latitude=lt; longitude=lg;
    }


//accesseurs des attributs de score
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

}
