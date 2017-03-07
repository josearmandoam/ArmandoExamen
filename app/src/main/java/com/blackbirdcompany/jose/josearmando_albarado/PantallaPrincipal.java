package com.blackbirdcompany.jose.josearmando_albarado;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PantallaPrincipal extends AppCompatActivity {
    private EditText edNombre;
    private Button bJugar;
    private Button bSituaciones;
    private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);

        edNombre=(EditText)findViewById(R.id.editText);
        bJugar=(Button)findViewById(R.id.bJugar);
        bSituaciones=(Button)findViewById(R.id.bSituaciones);

        edNombre.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.toString().length() == 0) {
                    bJugar.setEnabled(false);
                    bSituaciones.setEnabled(false);
                }else{
                    bJugar.setEnabled(true);
                    bSituaciones.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

        bJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edNombre.getText().toString().isEmpty()){
                    //abro la activdad de la lista
                    Intent i=new Intent(PantallaPrincipal.this,PantallaJuego.class);
                    i.putExtra("player",edNombre.getText().toString());
                    startActivity(i);
                }else{
                    mensajeError();
                }
            }
        });
        bSituaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edNombre.getText().toString().isEmpty()){
                    //abro la activdad del juego
                    Intent i=new Intent(PantallaPrincipal.this,Situaciones.class);
                    i.putExtra("player",edNombre.getText().toString());
                    startActivity(i);
                }else{
                    mensajeError();
                }
            }
        });
    }
    private void mensajeError(){
        AlertDialog.Builder dSalida=new AlertDialog.Builder(this);
        dSalida.setTitle("Error");
        dSalida.setMessage("No has rellenado el campo del Nombre del jugador");
        dSalida.setIcon(R.mipmap.err);

        dSalida.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edNombre.requestFocus();
            }
        });
        alerta=dSalida.create();
        alerta.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pref:
                Intent i=new Intent(PantallaPrincipal.this,Preferencias.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
