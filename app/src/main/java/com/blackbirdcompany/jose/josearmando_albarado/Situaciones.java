package com.blackbirdcompany.jose.josearmando_albarado;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;

public class Situaciones extends AppCompatActivity {
    private String url_consulta;
    private AlertDialog alerta;
    private Adaptador adp;
    private ListView lista;
    private ArrayList situaciones;
    private String jugador;
    private TextView textView;
    private SharedPreferences sp;
    private String IP_Server;
    private String Usuario;
    private boolean error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.situaciones);
        lista=(ListView)findViewById(R.id.listaSituaciones);
        Bundle b=getIntent().getExtras();
        jugador=b.getString("player");
        error=false;

        sp= PreferenceManager.getDefaultSharedPreferences(this);

        IP_Server=sp.getString("servidor","iesayala.ddns.net");
        Usuario=sp.getString("usuario","joseA");
        String archivo=sp.getString("archivo","Situaciones.php");
        url_consulta="http://"+IP_Server+"/"+Usuario+"/"+archivo;

        textView=(TextView)findViewById(R.id.textView2);
        getSupportActionBar().setTitle(jugador);
        //url_consulta="http://iesayala.ddns.net/joseA/Situaciones.php";


        situaciones= new ArrayList();
        new Listas().execute();
    }
    class Listas extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        private String TAG;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Situaciones.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... args) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr;
            try {
                jsonStr = sh.makeServiceCall(url_consulta + "?ins_sql=select%20nombre,fecha,hora,posX,posY%20from%20Situaciones%20where%20nombre=%27"+jugador+"%27");
            }catch(Exception a){
                jsonStr=null;
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                mensajeError();
                adp=null;
                lista.setAdapter(adp);
            }

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                if(alerta!=null) {
                    alerta.dismiss();
                }
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "0");
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "1");

                    // Getting JSON Array node
                    // JSONArray contacts = jsonObj.getJSONArray("contacts");
                    JSONArray catts = jsonObj.getJSONArray("Situaciones");

                    Log.e(TAG, "2");

                    // looping through All Contacts
                    for (int i = 0; i < catts.length(); i++) {
                        JSONObject c = catts.getJSONObject(i);

                        String nombre=c.getString("nombre");
                        Date fecha = Date.valueOf(c.getString("fecha"));
                        String hora=c.getString("hora");
                        int x=c.getInt("posX");
                        int y=c.getInt("posY");

                        Situacion situacion=new Situacion(nombre,fecha,x,y,hora);

                        situaciones.add(situacion);

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mensajeError();
                        }
                    });

                }
            } else {

                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                        mensajeError();
                    }
                });

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if(situaciones.size()>0) {
                adp = new Adaptador(Situaciones.this, situaciones);
                lista.setAdapter(adp);
                textView.setText("");
            }else{
                if (!error) {
                    textView.setText(jugador + " no tiene ninguna partida guardada");
                }else{
                    textView.setText("Se ha producido un error de conexión,"+"\n"+ "actualiza una vez resuelto el problema");
                }
            }
        }
    }
    private void mensajeError(){
        error=true;
        AlertDialog.Builder dSalida=new AlertDialog.Builder(this);
        dSalida.setTitle("Error");
        dSalida.setMessage("Se ha producido un error de conexión");
        dSalida.setIcon(R.mipmap.err);
        dSalida.setNegativeButton("Configurar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(Situaciones.this,Preferencias.class);
                startActivity(i);
            }
        });
        alerta=dSalida.create();
        alerta.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_situaciones,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.act:
                actualizarLista();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizarLista() {
        IP_Server=sp.getString("servidor","");
        Usuario=sp.getString("usuario","");
        String archivo=sp.getString("archivo","");

        url_consulta="http://"+IP_Server+"/"+Usuario+"/"+archivo;
        situaciones.clear();
        new Listas().execute();
    }
}
