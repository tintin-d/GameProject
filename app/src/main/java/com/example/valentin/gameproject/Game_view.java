package com.example.valentin.gameproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by valentin on 12/10/17.
 */

public class Game_view extends View {

    Paint paint;
    private Drawable mCustomImage;
    float x, y;
    boolean first=true;
    public Game_view(Context context) {
        super(context);
    }

    public Game_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCustomImage = context.getResources().getDrawable(R.drawable.cannon);
    }

    public Game_view(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon);
    }



    @Override
    public  void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(first){
            //première apparition du cannon
            x=canvas.getWidth()/2;
            y=canvas.getHeight()-100;
            first=false;
        }
        int maxW=canvas.getWidth();
        int maxH=canvas.getHeight();
        //Bornes de l'écran
        if(x>=maxW-70){x=maxW-70;}
        if(x<=20){x=0;}
        if(y>=maxH-20){y=maxH-20;}
        if(y<=20){y=20;}


        paint= new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        mCustomImage.setBounds((int)x,(int)y,(int)x+70,(int)y+80);
        mCustomImage.draw(canvas);
    }


    public float getMyX() {
        return x;
    }

    public float getMyY() {
        return y;
    }

    public void setMyX(float x) {
        this.x = x;
    }

    public void setMyY(float y) {
        this.y = y;
    }
}
