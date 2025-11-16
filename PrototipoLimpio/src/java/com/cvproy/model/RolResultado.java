package com.cvproy.model;

public class RolResultado {

    private String nombreRol;
    private int afinidad;
    private int fortalezas;
    private int brechas;
    private int oportunidades;

    public RolResultado(String nombreRol, int afinidad, int fortalezas, int brechas, int oportunidades) {
        this.nombreRol = nombreRol;
        this.afinidad = afinidad;
        this.fortalezas = fortalezas;
        this.brechas = brechas;
        this.oportunidades = oportunidades;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public int getAfinidad() {
        return afinidad;
    }

    public int getFortalezas() {
        return fortalezas;
    }

    public int getBrechas() {
        return brechas;
    }

    public int getOportunidades() {
        return oportunidades;
    }
}