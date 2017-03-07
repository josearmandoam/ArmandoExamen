package com.blackbirdcompany.jose.josearmando_albarado;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PantallaJuego extends AppCompatActivity {
    SensorManager sm;
    GameView gv;
    private String jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Bundle b=getIntent().getExtras();
        jugador=b.getString("player");

        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        gv=new GameView(this,sm,jugador);
        setContentView(gv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(gv,gv.acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(gv);
    }
}
