package com.example.valentin.gameproject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by valentin on 29/10/17.
 */

public class Target {
    private HashMap<Integer,Drawable> target=new HashMap<Integer,Drawable>();
    private float x,y;
    private int speed;
    private Boolean firstUse=true;
    private  boolean touched;



    private int size;

    public Target(Context context){
        target.put(1,context.getResources().getDrawable(R.drawable.pk1));
        target.put(2,context.getResources().getDrawable(R.drawable.pk2));
        target.put(3,context.getResources().getDrawable(R.drawable.pk3));
        target.put(4,context.getResources().getDrawable(R.drawable.pk4));
        target.put(5,context.getResources().getDrawable(R.drawable.pk5));
        target.put(6,context.getResources().getDrawable(R.drawable.pk6));
        target.put(7,context.getResources().getDrawable(R.drawable.pk7));
        target.put(8,context.getResources().getDrawable(R.drawable.pk8));
        target.put(9,context.getResources().getDrawable(R.drawable.pk9));
        target.put(10,context.getResources().getDrawable(R.drawable.pk10));
        target.put(11,context.getResources().getDrawable(R.drawable.pk11));
        touched=false;

    }

    public int Randomize() {
        int r=(int)(Math.random()*(getTarget().size()+1));
        return r;
    }

    public HashMap<Integer, Drawable> getTarget() {
        return target;
    }

    public void setTarget(HashMap<Integer, Drawable> target) {
        this.target = target;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public boolean hasBeenTouched() {
        return touched;
    }

    public void setTouched(boolean hasBeenTouched) {
        this.touched = hasBeenTouched;
    }

}
