package com.blackbirdcompany.jose.josearmando_albarado;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jose on 02/03/2017.
 */

public class Adaptador extends BaseAdapter {
    private Activity actividad;
    private ArrayList<Situacion> situaciones;

    public Adaptador(Activity activity, ArrayList situaciones) {
        this.actividad= activity;
        this.situaciones = situaciones;
    }

    @Override
    public int getCount() {
        return situaciones.size();
    }

    @Override
    public Object getItem(int i) {
        return situaciones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View v = inflater.inflate(R.layout.lista_view, null, true);
        TextView tvNombre = (TextView) v.findViewById(R.id.tvNombre);
        TextView tvFecha = (TextView) v.findViewById((R.id.tvFecha));
        TextView tvPosX = (TextView) v.findViewById((R.id.tvPosX));
        TextView tvPosY = (TextView) v.findViewById((R.id.tvPosY));

        tvNombre.setText(situaciones.get(i).getNombre());
        tvFecha.setText(situaciones.get(i).getFecha().toString()+" / "+situaciones.get(i).getHora());
        tvPosX.setText(""+situaciones.get(i).getX());
        tvPosY.setText(""+situaciones.get(i).getY());
        return v;
    }
}
