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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 Created by valentin on 12/10/17.
 */

public class Game_view extends SurfaceView implements Runnable{

    private List<Integer>imagesTarget= new ArrayList<Integer>();
    private Thread thread = null;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    float x=100;float y=100;
    private Drawable mCustomImage;
    private Drawable mCustomImageB;
    boolean first=true;
    private Balle b;
    private HashMap<Integer,Target> targetHashMap;



    private Boolean gameOver=false;




    public Game_view(Context context) {
        super(context);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        b=new Balle(this.getContext());
        insertTarget(4);
    }

    public Game_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        b=new Balle(this.getContext());
        insertTarget(4);
    }

    public Game_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        b=new Balle(this.getContext());
        insertTarget(4);
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
                Canvas canvas = surfaceHolder.lockCanvas();
                int maxW=canvas.getWidth();
                int maxH=canvas.getHeight();
                paint.setColor(Color.argb(0,0,0,0));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(Color.RED);
                if(first){
                    //première apparition du cannon
                    x=maxW/2-((maxW/7)/2);
                    y=canvas.getHeight()-(maxH/9);
                    first=false;
                }
                //Bornes de l'écran
                if(x>=maxW-70){x=maxW-70;}
                if(x<=0){x=0;}
                if(y>=maxH-20){y=maxH-20;}
                if(y<=20){y=20;}



                //redessinement des éléments y compris le fond
                mCustomImageB.setBounds(0,0,maxW,maxH);
                mCustomImageB.draw(canvas);
                mCustomImage.setBounds((int)x,(int)y,(int)x+(maxW/7),(int)y+(maxH/9));
                mCustomImage.draw(canvas);

                //implémentation de la balle
                //on lui donnera des coordonnées dès son initialisation
                if(b.getFirstUse()){
                    b.setFirstUse(false);
                    b.setY(y-maxH/60);
                    b.setX(x+((maxW/7)/2)-(maxW/20)/2);
                }
                float bx=b.getX(); float by=b.getY();

                Drawable maBalle=b.getBalle(1);
                maBalle.setBounds((int)bx,(int)by, (int)bx+(maxW/20),(int)by+(maxW/20));
                b.setSize(maxW/20);
                b.incrementY(maxH/200);
                if(b.getY()<0){
                    b.setFirstUse(true);
                }
                maBalle.draw(canvas);



                //insertion des cibles
                int i=0;
                for(Integer key: targetHashMap.keySet()){
                    Target cible=targetHashMap.get(key);
                    Drawable d= cible.getTarget().get(key);

                    int w=maxW / 80;
                    int h=maxH / 12;
                    cible.setSize(maxW/w);


                    int fx =w  + (i % (maxW/w)) * h;
                    int fy =w;
                        int sx =h  + (i % (maxW/w)) * h;
                        int sy =h;

                        i++;
                    cible.setX(fx);
                    cible.setY(fy);
                    d.setBounds(fx,fy,sx,sy);
                    d.draw(canvas);
                    //Log.d("coord",""+key+", "+cible.getX()+", "+cible.getY()+", "+ cible.getSize());
                }



                isTouched();
                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        }
    }


    public void insertTarget(int nb){
        for(int i=1;i<=nb;i++) {
            targetHashMap.put(i, new Target(this.getContext()));
            imagesTarget.add(targetHashMap.get(i).Randomize());
        }
    }

    public float getMyX() {
        return x;
    }


    public void setMyX(float x) {
        this.x = x;
    }


    public boolean isTouched(){
        Boolean touched=false;
        float bx=b.getX();
        float by=b.getY();
        float bxS=bx+b.getSize();
        float byS=by+b.getSize();
        for (Integer k : targetHashMap.keySet()){
            Target cible=targetHashMap.get(k);
            float tx=cible.getX();
            float ty=cible.getY();
            float txS=tx+cible.getSize();
            float tyS=ty+cible.getSize();
           // Log.d("coord",""+bx+" "+bxS);
           // Log.d("coord",""+tx+" "+txS);

            touched=rectangle_collision(bx,by,bxS,byS,tx,ty,txS,tyS);
            ///Log.d("test", "" + touched+ " "+ k);


        }

        return touched;
    }
    boolean rectangle_collision(float x_1, float y_1, float width_1, float height_1, float x_2, float y_2, float width_2, float height_2)
    {
        return !(x_1 > x_2+width_2 || x_1+width_1 < x_2 || y_1 > y_2+height_2 || y_1+height_1 < y_2);
    }




    public void gameOver(Boolean bool){
        setGameOver(bool);
        running=!bool;
        if(!bool){
            first=true;
            b.setFirstUse(true);
            targetHashMap=new HashMap<Integer,Target>();
            ////A modifier
            insertTarget(5);
            thread = new Thread(this);
            thread.start();
        }




    }
    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

}
