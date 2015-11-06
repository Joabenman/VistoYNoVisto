package com.vynv.proyecto.vistoynovisto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Msi on 07/06/2015.
 */
public class Visualizar extends Activity {

    private ImageView img;
    private String defecto="/mnt/sdcard/img/book.jpg";

    protected void onCreate(Bundle savedInstanceState) {
        //inicializamos

        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar);

        //recogemos el intent juego
        Libro libro = (Libro)getIntent().getSerializableExtra("juego");
        Intent intent = getIntent();

        //recogemos los textview de la interfaz
        TextView lblTitulo = (TextView)findViewById(R.id.lblTitulo);
        TextView lblEditorial = (TextView)findViewById(R.id.lblEditorial);
        TextView lblAutor = (TextView)findViewById(R.id.lblAutor);
        img = (ImageView)findViewById(R.id.imagen);


        String titulo =  intent.getStringExtra("titulo");
        String imagen =  intent.getStringExtra("imagen");
        Log.d("visualizar", imagen);
        if(imagen==null) {
            imagen = defecto;
        }

        String editorial = intent.getStringExtra("editorial");
        String autor =  intent.getStringExtra("autor");

        //mostramos los datos recogidos
        lblTitulo.setText(titulo);
        Bitmap image = BitmapFactory.decodeFile(imagen);
        img.setImageBitmap(image);
        lblEditorial.setText(editorial);
        lblAutor.setText(autor);



    }

}
