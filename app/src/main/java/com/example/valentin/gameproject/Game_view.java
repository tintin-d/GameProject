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
    private float x=100; private float y=100;
    private Drawable mCustomImage;
    private Drawable mCustomImageB;
    boolean first=true;
    private Balle b;
    private int monScore;
    private int decompte;
    private HashMap<Integer,Target> targetHashMap;
    private List<Integer> deleteList;
    private Boolean gameOver=false;




    public Game_view(Context context) {
        super(context);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        deleteList=new ArrayList<Integer>();
        b=new Balle(this.getContext());
        monScore=0;
        insertTarget(1,6);
        Log.d("cons","gameView");

    }

    public Game_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        b=new Balle(this.getContext());
        deleteList=new ArrayList<Integer>();
        monScore=0;
        insertTarget(1,6);
        Log.d("cons","gameView");

    }

    public Game_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder=getHolder();
        mCustomImage = context.getResources().getDrawable(R.drawable.cannon1);
        mCustomImageB = context.getResources().getDrawable(R.drawable.cave2);
        targetHashMap=new HashMap<Integer,Target>();
        b=new Balle(this.getContext());
        deleteList=new ArrayList<Integer>();
        monScore=0;
        insertTarget(1,6);
        Log.d("cons","gameView");
    }



    public void setMonScore(int monScore){this.monScore=monScore;}
    public int getMonScore(){return this.monScore;}

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
                paint.setColor(Color.GREEN);
                if(first){
                    //première apparition du cannon
                    x=maxW/2-((maxW/7)/2);
                    y=canvas.getHeight()-(maxH/9);
                    first=false;
                }
                //Bornes de l'écran
                if(x>=maxW-(maxW/7)){x=maxW-(maxW/7);}
                if(x<=0){x=0;}
                if(y>=maxH-(maxH/9)){y=maxH-(maxH/9);}
                if(y<=maxH/9){y=maxH/9;}

                //redessinement des éléments y compris le fond
                mCustomImageB.setBounds(0,0,maxW,maxH);
                mCustomImageB.draw(canvas);
                //placemennt du cannon sur l'écran
                mCustomImage.setBounds((int)x,(int)y,(int)x+(maxW/7),(int)y+(maxH/9));
                mCustomImage.draw(canvas);

                insertBall(maxW,maxH,canvas);

                if(decompte>100 && !gameOver){
                    decompte=0;
                    for(Integer key:targetHashMap.keySet()){
                        if(!targetHashMap.get(key).getFirstUse()){
                            int h = maxH / 12;
                            targetHashMap.get(key).setY(targetHashMap.get(key).getY()+h);
                            if(targetHashMap.get(key).getY()>y-targetHashMap.get(key).getSize()){
                                gameOver(true);
                            }
                        }
                    }
                    //insertTarget(targetHashMap.size(),6);
                }
                insertTarget(maxW,maxH,canvas);
                decompte++;
                canvas.drawLine(0,y-maxH/100,maxW,y-maxH/100,paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public HashMap<Integer, Target> getTargetHashMap() {
        return targetHashMap;
    }

    public void setTargetHashMap(HashMap<Integer, Target> targetHashMap) {
        this.targetHashMap = targetHashMap;
    }

    public void insertTarget(int begining, int nb){
        for(int i=begining;i<begining+nb;i++) {
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


    public void isTouched(Canvas canvas){
        float bx=b.getCx();
        float by=b.getCy();
        float br=b.getRadius();
        for(Integer key: targetHashMap.keySet()){
            if(!(targetHashMap.get(key).hasBeenTouched())) {
                float tx = targetHashMap.get(key).getCx();
                float ty = targetHashMap.get(key).getCy();
                float tr = targetHashMap.get(key).getRadius();
                if (Math.sqrt((tx - bx) * (tx - bx) + (ty - by) * (ty - by)) < (br + tr)) {
                    targetHashMap.get(key).setTouched(true);
                    deleteList.add(key);
                }
            }
        }
        for(Integer key: targetHashMap.keySet()) {
            if ((targetHashMap.get(key).hasBeenTouched())) {
                monScore+=1;
            }
        }
    }


    public void insertBall(int maxW,int maxH,Canvas canvas){
        //implémentation de la balle
        //on lui donnera des coordonnées dès son initialisation
        if(b.getFirstUse()){
            //setSize doit être fait avant tt les autres
            b.setSize(maxW/20);
            b.setFirstUse(false);
            b.setY(y-maxH/60);
            b.setX(x+((maxW/7)/2)-(maxW/20)/2);
        }
        float bx=b.getX(); float by=b.getY();
        Drawable maBalle=b.getBalle(1);
        maBalle.setBounds((int)bx,(int)by, (int)bx+(maxW/20),(int)by+(maxW/20));

        b.incrementY(maxH/200);
        if(b.getY()<0){
            b.setFirstUse(true);
        }
        maBalle.draw(canvas);
        //test pour débugg
        //canvas.drawCircle(b.getCx(),b.getCy(),b.getRadius(),paint);
    }

    public void insertTarget(int maxW, int maxH, Canvas canvas){
        //insertion des cibles
        int i=0;
        for(Integer key: targetHashMap.keySet()){
            Target cible=targetHashMap.get(key);
                Drawable d = cible.getTarget().get(key);
                int w = maxW / 100;
                int h = maxH / 12;
                cible.setSize(maxW / w);
                int fx = w + (i % (maxW / w)) * h + i * 10;
                int fy = w;
                i++;
                //on donne de nouvelles coordonnées à la cible  si elle na pas déja  été utilisée
                if(cible.getFirstUse()) {
                    cible.setX(fx);
                    cible.setY(fy);

                    cible.setFirstUse(false);
                }
                d.setBounds((int)cible.getX(), (int)cible.getY(), (int)(cible.getSize()+cible.getX()), (int)(cible.getSize()+cible.getY()));
                d.draw(canvas);
                //test pour débugg
                canvas.drawCircle(cible.getCx(), cible.getCy(), cible.getRadius(), paint);
        }
        isTouched(canvas);
        if(!deleteList.isEmpty()) {
            for (Integer iter : deleteList) {
                targetHashMap.remove(iter);
                b.setFirstUse(true);
            }
            deleteList.clear();
        }

    }


    public void gameOver(Boolean bool){
        setGameOver(bool);
        running=!bool;
        Log.d("run",""+running);
        if(!bool){
            first=true;
            b.setFirstUse(true);
            targetHashMap=new HashMap<Integer,Target>();
            ////A modifier
            for(Integer key: targetHashMap.keySet()) {
                targetHashMap.get(key).setFirstUse(true);
            }
            decompte=0;
            targetHashMap.clear();
            insertTarget(1,6);
            thread = new Thread(this);
            thread.start();
            Log.d("start",""+targetHashMap.size());
        }
    }
    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

}
