package com.blackbirdcompany.jose.josearmando_albarado;

import android.graphics.Canvas;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by jose on 08/02/2017.
 */
public class GameLoopThread extends Thread {
    static final long FPS = 10;
    private GameView view;
    private boolean running = false;
    private long tiempo;
    private int aux;
    private int x,y;
    private String player;
    String url;
    String hora;
    Date fecha;

    InputStream is=null;
    String result=null;
    String line=null;

    public GameLoopThread(GameView view) {
        this.view = view;
        tiempo=0;
        aux=5;
        url="http://iesayala.ddns.net/joseA/Insertar.php";
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                    x= (int) view.getCx();
                    y= (int) view.getCy();
                    player=view.getPlayer();
                    //System.out.println(Calendar.YEAR+"-"+Calendar.MONTH+""+Calendar.DAY_OF_MONTH);
                   // System.out.println(getDate(System.currentTimeMillis(), "hh:mm:ss"));

                    tiempo++;
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

            if(tiempo/10==aux){//como el juego esta optimizado a 10 FPS me creo una variable que se vaya incrimentando cada vez que se dibuje sobre el canvas
                //cuando ese tiempo dividido sea igual a 5,10,15 etc es decir cada 5 segundos envio los datos a la base de datos
                System.out.println("" + (tiempo/10));

                fecha= Date.valueOf(getDate(System.currentTimeMillis(),"yyyy-MM-dd"));
                hora=getDate(System.currentTimeMillis(), "hh:mm:ss");
                aux+=5;
                enviarDatos();
            }
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }

    private void enviarDatos() {
        insert();
    }

    public void insert() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url+"?&nombre="+player+"&fecha="+fecha+"&hora="+hora+"&posX="+x+"&posY="+y);
        String responseBody;

        try {
            BasicHttpResponse response = null;
            try {
                response = (BasicHttpResponse) httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();

            responseBody = EntityUtils.toString(entity);


        } catch (ClientProtocolException e) {
            Log.w("com.name.pkg", e);

        } catch (IOException e) {
            Log.w("com.name.pkg", e);
        }
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}