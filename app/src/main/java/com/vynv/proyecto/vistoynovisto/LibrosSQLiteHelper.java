package com.vynv.proyecto.vistoynovisto;

/**
 * Created by Msi on 27/04/2015.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class LibrosSQLiteHelper extends SQLiteOpenHelper {

    LibroAdaptador adaptador;

    ArrayList<Libro> datoslibros;

    //Sentencia SQL para crear la tabla de BDLibros
    String sqlCreate = "CREATE TABLE BDLibros (codigo INT PRIMARY KEY, titulo varchar2(50), imagen varchar2(250), editorial  varchar2 (50), autor varchar2 (50));";
    String sqlDrop = "DROP TABLE IF EXISTS BDLibros";
    // String sqlCreatetemporal = "CREATE TABLE BDLibrostemporal (codigo INT PRIMARY KEY, titulo varchar2(50), imagen varchar2(250), editorial  varchar2 (50), autor varchar2 (50));";
    // String sqlDroptemporal = "DROP TABLE IF EXISTS BDLibrostemporal";
    public LibrosSQLiteHelper(Context contexto, String nombre,
                              CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }


    public ArrayList<Libro> saveSincronizado(ArrayList<Libro> al) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Libro> cargalibrossqlite = new ArrayList();
        if (db != null) {

            //db.execSQL(sqlDrop);
            //db.execSQL(sqlCreate);
            int existe=0;

            for (int i = 0; i < al.size(); i++) { //recorremos todos los datos para guardarlos
                //cogemos los datos que esten en esa posicion del listview
                Libro lib = al.get(i);

                int iddb = i;
                String Titulodb = lib.getTitulo();
                String Imagendb = lib.getRutaimagen();
                String Editorialdb = lib.getEditorial();
                String Autordb = lib.getAutor();

                //comprobamos si el libro ya existe

                Cursor c = db.rawQuery("SELECT COUNT(*) FROM BDLibros WHERE titulo='"+Titulodb+"';", null);

                if (c.moveToFirst()) {//Recorremos el cursor hasta que no haya más registros
                    do {
                        existe= c.getInt(0);
                    } while(c.moveToNext());
                }

                if(existe>0){ //si ya existe el libro introducido modificamos el antiguo valor
                    //actualizamos el campo
                    Log.d("guardado", "guardando, existe " + existe + Titulodb);
                    db.execSQL("UPDATE BDLibros set  imagen ='"+Imagendb+"', editorial='"+Editorialdb+"', autor='"+Autordb+"' WHERE titulo='"+Titulodb+"';");
                }
                else{ //si no existe el libro lo introducimos
                    //insertamos
                    Log.d("guardado", "guardando, no existe" + existe + Titulodb);
                    db.execSQL("INSERT INTO BDLibros (codigo, titulo , imagen, editorial, autor ) values (" + iddb + ", '" + Titulodb + "', '" + Imagendb + "', '" + Editorialdb + "', '" + Autordb + "');");
                }
            }
            //cargamos los datos
            Cursor d = db.rawQuery("SELECT * FROM BDLibros", null);

            if (d.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros para cargarlos y devolverlos de vuelta
                do {
                    cargalibrossqlite.add(new Libro(/*c.getString(0),*/d.getString(1), d.getString(2), d.getString(3), d.getString(4)));
                } while(d.moveToNext());
            }
        }
        db.close();
        return cargalibrossqlite;
    }

    public void delete(String titulodb){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            // borramos el elemento de la base de datos
            db.execSQL("DELETE from BDLibros where titulo='"+titulodb+"';");
        }

        db.close();


    }

    public ArrayList<Libro> load() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Libro> cargalibrossqlite = new ArrayList();

        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM BDLibros", null);

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    cargalibrossqlite.add(new Libro(/*c.getString(0),*/c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                } while(c.moveToNext());
            }
        }
        db.close();
        return cargalibrossqlite;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS BDLibros");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}