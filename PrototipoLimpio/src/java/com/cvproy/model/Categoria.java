package com.cvproy.model;

public class Categoria {
    private int id;
    private String nombre;
    private boolean completada; 

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.completada = false; 
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isCompletada() { return completada; }
    
    // Setter
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}