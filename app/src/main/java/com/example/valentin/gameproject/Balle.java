package com.example.valentin.gameproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.util.HashMap;

import static java.security.AccessController.getContext;

/**
 * Created by valentin on 27/10/17.
 */

public class Balle {
    private float x,y;
    private int speed;
    private Boolean firstUse=true;
    private HashMap<Integer,Drawable> balle=new HashMap<Integer,Drawable>();
    private int size;

    public Balle(Context context){
        balle.put(1,context.getResources().getDrawable(R.drawable.pb1));
        balle.put(2,context.getResources().getDrawable(R.drawable.pb2));
        balle.put(3,context.getResources().getDrawable(R.drawable.pb3));
        size=0;
        speed=1;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
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


    public int getSpeed() {
        return speed;
    }

    public Drawable getBalle(int key) {
        return balle.get(key);
    }

    public void incrementY(int mvt){
        setY(getY()-mvt*speed);
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }



}
