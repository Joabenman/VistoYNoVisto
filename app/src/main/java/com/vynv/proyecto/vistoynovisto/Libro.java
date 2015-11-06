package com.vynv.proyecto.vistoynovisto;

import java.io.Serializable;


/**
 * Created by Usuario on 07/04/2015.
 */

public class Libro implements Serializable {
/* por ahora lo hacemos con las tres labels*/

    private String titulo;
    private String rutaimagen;
    private String editorial;
    private String autor;

    public Libro(){
        titulo = "";
        rutaimagen = "";
        editorial = "";
        autor = "";
    }

    public Libro(String tit, String ruta, String edi, String aut){
        titulo = tit;
        rutaimagen = ruta;
        editorial = edi;
        autor = aut;
    }

    public Libro(String tit, String edi, String aut){
        titulo = tit;
        rutaimagen = "";
        editorial = edi;
        autor = aut;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getTitulo(){
        return titulo;
    }

    public void setRutaimagen(String rutaimagen) {
        this.rutaimagen = rutaimagen;
    }
    public String getRutaimagen(){
        return rutaimagen;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public String getEditorial(){
        return editorial;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAutor() { return autor; }






}
