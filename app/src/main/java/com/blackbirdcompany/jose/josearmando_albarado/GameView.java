package com.blackbirdcompany.jose.josearmando_albarado;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

/**
 * Created by jose on 08/02/2017.
 */
public class GameView extends SurfaceView implements SensorEventListener {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    SensorManager sm;
    Sensor acelerometro;
    float cxMax,cyMax;
    float cx,cy,tam;
    private String player;
   // float posX,posY;//posicion del obstaculo
    private String TAG="AVISO";

    public GameView(final Context context, final SensorManager sm,String player) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        this.sm=sm;
        this.player=player;
        holder = getHolder();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cx==v.getX() && cy==v.getY()){
                    Log.e(TAG,"has tocado la bola");
                }
            }
        });
        cx=50;
        cy=100;
        tam=30;
        cxMax=getWidth();
        cyMax=getHeight();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                cxMax=getWidth();
                cyMax=getHeight();
                List<Sensor> lista=sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
                if(!lista.isEmpty()){
                    acelerometro=lista.get(0);
                    sm.registerListener(GameView.this,acelerometro, SensorManager.SENSOR_DELAY_UI);
                }
                //h.run();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
       // bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onDraw(Canvas canvas) {

        //en primer lugar limpio el canvas antes de empezar a pintar
        Paint pincel=new Paint();
        pincel.setColor(Color.BLACK);
        //bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.cone);
       // posX=getWidth()-bmp.getWidth();
      //  posY=getHeight()-bmp.getHeight();
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), pincel);


        //ahora dibujo el circulo con las coordenadas
        pincel.setStrokeWidth(5);
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.BLUE);
        canvas.drawCircle(cx, cy, tam, pincel);

    }
    protected void actualizaBola(float x,float y){
        if((cx - x)<cxMax-tam && (cx - x)>tam) {
            cx = cx - x;
        }
        if((cy + y)<cyMax-tam && (cy + y)>tam) {
            cy = cy + y;
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    float x= event.values[0]*2;
                    float y= event.values[1]*2;
                    actualizaBola(x,y);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}