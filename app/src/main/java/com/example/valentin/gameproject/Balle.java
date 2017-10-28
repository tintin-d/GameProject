package com.example.valentin.gameproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;

import static java.security.AccessController.getContext;

/**
 * Created by valentin on 27/10/17.
 */

public class Balle {
    private float x,y;
    private int tempsRafale;
    private int speed;
    private Drawable balle;
    private Boolean firstUse=true;

    public Balle(Context context){
        balle = context.getResources().getDrawable(R.drawable.pb1);
        speed=1;
        tempsRafale=1;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTempsRafale(int tempsRafale) {
        this.tempsRafale = tempsRafale;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBalle(Drawable balle) {
        this.balle = balle;
    }

    public Boolean getFirstUse() {
        return firstUse;
    }

    public void setFirstUse(Boolean firstUse) {

        this.firstUse = firstUse;
    }

    public float getX() {

        return x;
    }

    public float getY() {
        return y;
    }

    public int getTempsRafale() {
        return tempsRafale;
    }

    public int getSpeed() {
        return speed;
    }

    public Drawable getBalle() {
        return balle;
    }


}
