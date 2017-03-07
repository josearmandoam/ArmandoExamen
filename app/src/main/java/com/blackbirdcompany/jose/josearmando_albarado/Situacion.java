package com.blackbirdcompany.jose.josearmando_albarado;

import java.sql.Date;

/**
 * Created by jose on 02/03/2017.
 */

public class Situacion {
    private String nombre;
    private Date fecha;
    private int x,y;
    private String hora;

    public Situacion(String nombre, Date fecha, int x, int y, String hora) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.x = x;
        this.y = y;
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
