package com.example.pruebaandroid.fragmentos;

public class ItemPqrs {
    private String titulo, descrip;

    public ItemPqrs(String titulo, String descrip) {
        this.titulo = titulo;
        this.descrip = descrip;
    }

    public ItemPqrs() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
