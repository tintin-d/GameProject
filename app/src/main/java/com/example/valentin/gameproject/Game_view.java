package com.example.valentin.gameproject;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/*
 Created by valentin on 12/10/17.
 */

public class Game_view extends SurfaceView implements Runnable{

    Thread thread = null;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    Bitmap mBitmap;
    float x=100;float y=100;
    private Drawable mCustomImage;
    private Drawable mCustomImageB;
    boolean first=true;



    public Game_view(Context context) {
        super(context);
        surfaceHolder=getHolder();
       // this.setBackgroundResource(R.drawable.cave2);
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);

    }

    public Game_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);

        //this.setBackgroundResource(R.drawable.cave2);
    }

    public Game_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder=getHolder();
        //this.setBackgroundResource(R.drawable.cave2);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImage = context.getResources().getDrawable(R.drawable.cave2);


    }



    public void onResumeMySurfaceView(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseMySurfaceView(){
        boolean retry = true;
        running = false;
        while(retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {

        while(running) {
            if (surfaceHolder.getSurface().isValid()) {
                Log.d("running",""+surfaceHolder.getSurface().isValid());
                Canvas canvas = surfaceHolder.lockCanvas();
                int maxW=canvas.getWidth();
                int maxH=canvas.getHeight();
                paint.setColor(Color.argb(0,0,0,0));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(Color.RED);
                if(first){
                    //première apparition du cannon
                    x=canvas.getWidth()/2;
                    y=canvas.getHeight()-100;
                    first=false;
                }
                //Bornes de l'écran
                if(x>=maxW-70){x=maxW-70;}
                if(x<=0){x=0;}
                if(y>=maxH-20){y=maxH-20;}
                if(y<=20){y=20;}
                mCustomImageB.setBounds(0,0,maxW,maxH);
                mCustomImageB.draw(canvas);
                mCustomImage.setBounds((int)x,(int)y,(int)x+70,(int)y+80);
                mCustomImage.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
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
