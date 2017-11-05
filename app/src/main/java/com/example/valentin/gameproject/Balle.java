package com.example.valentin.gameproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;

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
    private float cx,cy;
    private int radius;

    public Balle(Context context){
        balle.put(1,context.getResources().getDrawable(R.drawable.pb1));
        balle.put(2,context.getResources().getDrawable(R.drawable.pb2));
        balle.put(3,context.getResources().getDrawable(R.drawable.pb3));
        size=0;
        speed=1;
        Log.d("cons","Balle");
    }

    public void setX(float x) {
        this.x = x;
        this.cx=x+(size/2);
    }

    public void setY(float y) {
        this.y = y;
        //offcet rajouté pour centrer la zone de collision avec l'image
        this.cy=y+(size/2)+(size/10);
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
        setRadius(size);
    }

    //adaptation du rayon en fonction de l'image à l'écran
    public void setRadius(int size){this.radius=size/2-(size/7);}
    public int getRadius(){return this.radius;}

    public float getCx(){return cx;}
    public float getCy(){return cy;}


}
