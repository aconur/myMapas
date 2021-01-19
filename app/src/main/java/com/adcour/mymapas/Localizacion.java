package com.adcour.mymapas;

public class Localizacion {

    private String titulo;
    private String fragmento;
    private String etiqueta;
    private double latitud;
    private double longitud;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFragmento() {
        return fragmento;
    }

    public void setFragmento(String fragmento) {
        this.fragmento = fragmento;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
