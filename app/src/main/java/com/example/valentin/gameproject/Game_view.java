package com.example.valentin.gameproject;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 Created by valentin on 12/10/17.
 */

public class Game_view extends SurfaceView implements Runnable{


    private List<Integer>imagesTarget= new ArrayList<Integer>();//liste de nombres aléatoires pour choisir les images
    private Thread thread = null;//la thread pricipale du jeu
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//notre objet contenant les options de dessin
    private SurfaceHolder surfaceHolder;
    volatile boolean running = false;//Booleen servant à savoir si on dois executer les instructions de la methode run ou non
    private float x=100; private float y=100;
    private Drawable mCustomImage;//mon canon
    private Drawable mCustomImageB;//mon arrière plan
    boolean first=true;//pour savoir si c'est la première fois que la vue est appelée
    private Balle b;//ma basse
    private int monScore;
    private int decompte;//servant a déclaancher la descente des cibles
    private HashMap<Integer,Target> targetHashMap;//liste de cibles
    private List<Integer> deleteList;//liste des cibles touchées à supprimer
    private Boolean gameOver=false;//pour savoir depuis l'extérieur de la clsse si on est en game-over ou pas
    private MediaPlayer mediaPlayer;//notre lecteur audio
    private boolean played;//permet de savoir si le lecteur est en mode lecture ou non


//3 constructeurs identiques
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


    //modification et acquisition du score
    public void setMonScore(int monScore){this.monScore=monScore;}
    public int getMonScore(){return this.monScore;}

    //reprise de l'activité de notre thread
    public void onResumeMySurfaceView(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    //mise en pause de notre thread
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
        //tant que running est vrai on exécute ce qui suit
        while(running) {
            if (surfaceHolder.getSurface().isValid()) {
                //on empêche d'autre methodes d'accéder au canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                //on récupère ses dimentions
                int maxW=canvas.getWidth();
                int maxH=canvas.getHeight();
                paint.setColor(Color.argb(0,0,0,0));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(Color.GREEN);
                //on dessine notre canon
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
                //dépllace les cibles vers le bas
                if(decompte>60 && !gameOver){
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
                    //insertTarget(targetHashMap.size(),6); code non utilisé car ne fonctionne pas
                }
                insertTarget(maxW,maxH,canvas);
                decompte++;
                canvas.drawLine(0,y-maxH/100,maxW,y-maxH/100,paint);//ligne de gameOver
                //on relache le canvas
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    //ajout des cibles en mémoire
    public void insertTarget(int begining, int nb){
        for(int i=begining;i<begining+nb;i++) {
            targetHashMap.put(i, new Target(this.getContext()));
            imagesTarget.add(targetHashMap.get(i).Randomize());
        }
    }

    //Getters et setters
    public HashMap<Integer, Target> getTargetHashMap() {
        return targetHashMap;
    }

    public void setTargetHashMap(HashMap<Integer, Target> targetHashMap) {
        this.targetHashMap = targetHashMap;
    }

    public float getMyX() {
        return x;
    }

    public void setMyX(float x) {
        this.x = x;
    }


    //vérification de collision des cercles
    public void isTouched(){
        //coordonées de la alle
        float bx=b.getCx();
        float by=b.getCy();
        float br=b.getRadius();
        for(Integer key: targetHashMap.keySet()){
            if(!(targetHashMap.get(key).hasBeenTouched())) {
                //coordonnées des cibles
                float tx = targetHashMap.get(key).getCx();
                float ty = targetHashMap.get(key).getCy();
                float tr = targetHashMap.get(key).getRadius();
                //calcul permettant de detecter la collision
                if (Math.sqrt((tx - bx) * (tx - bx) + (ty - by) * (ty - by)) < (br + tr)) {
                    targetHashMap.get(key).setTouched(true);
                    deleteList.add(key);
                    //un petit son est emis
                    play("touch");
                }
            }
        }
        //on compte ensuite combien d'objets ont été touchés
        //Si on le fait dans la même boucle on passe plusieurs fois dans la boucle if et
        // on incrémente trop le score
        for(Integer key: targetHashMap.keySet()) {
            if ((targetHashMap.get(key).hasBeenTouched())) {
                monScore+=1;
            }
        }
    }

    //on plae ensuite la balle selon l'emplacement du canon pui on la laisse aller en ligne droite
    //on a une seule instant de balle à la fois à l'écran.
    public void insertBall(int maxW,int maxH,Canvas canvas){
        //implémentation de la balle
        //on lui donnera des coordonnées dès son initialisation
        if(b.getFirstUse()){
            //setSize doit être fait avant tt les autres
            //pour ne pas fausser le rayon
            b.setSize(maxW/20);
            b.setFirstUse(false);
            b.setY(y-maxH/60);
            b.setX(x+((maxW/7)/2)-(maxW/20)/2);
        }
        float bx=b.getX(); float by=b.getY();
        Drawable maBalle=b.getBalle(1);
        maBalle.setBounds((int)bx,(int)by, (int)bx+(maxW/20),(int)by+(maxW/20));
        //vitesse de déplacement de la balle
        b.incrementY(maxH/80);
        if(b.getY()<0){
            //si on sort de l'écran on réinitialise les coordonnées de la balle
            b.setFirstUse(true);
        }
        maBalle.draw(canvas);
    }


    public void insertTarget(int maxW, int maxH, Canvas canvas){
        //insertion des cibles
        int i=0;
        for(Integer key: targetHashMap.keySet()){
            Target cible=targetHashMap.get(key);
                Drawable d = cible.getTarget().get(key);
                //on calcule les coordonnées des cibles à l'interrieur de la hashmap
                int w = maxW / 80;
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
        }
        //on vérifie les collisions à chaque redessinement des cibles
        isTouched();
        //si on à des cibles à supprimer car elle ont été touchées on le fait ici
        if(!deleteList.isEmpty()) {
            for (Integer iter : deleteList) {
                targetHashMap.remove(iter);
                b.setFirstUse(true);
            }
            //on vide aussi cette liste encsuite
            deleteList.clear();
        }
    }

    //cette methode permet d'entrer et de sortir du mode game-over selon le boolean qu'on lui
    // passe en paramère
    public void gameOver(Boolean bool){
        setGameOver(bool);
        running=!bool;//permet de mettre en pause ou non les redessinements
        Log.d("run",""+running);
        if(!bool){
            first=true;
            b.setFirstUse(true);
            //on réinitialise les variables des balles et des cibles
            targetHashMap=new HashMap<Integer,Target>();
            for(Integer key: targetHashMap.keySet()) {
                targetHashMap.get(key).setFirstUse(true);
            }
            //on remet le décompte à zero
            decompte=0;
            //on vide la table de cible et on en recrée une nouvelle
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

    //Gestion du son
    public void stop(View v){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer=null;
            played=false;
        }
    }

    public void play(String str){
        stop(null);
        played=true;
        if(str.equals("touch")) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.touch);
        }

        else{
            Log.d("Sons","Pas de son à jouer pour cette action");
        }
            mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stop(null);
                    }
                }
        );
        mediaPlayer.start();
    }

}
