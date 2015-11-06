package com.vynv.proyecto.vistoynovisto;

/**
 * Created by Msi on 15/04/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Editar extends Activity implements View.OnClickListener {

    LibroAdaptador adaptador;

    ArrayList<Libro> datoslibros;
    private static final int SELECT_PICTURE = 1;
    private boolean hayimagen=false;
    private String selectedImagePath;
    private String rutaoriginal="";
    private ImageView img;

    String imagen="";
    //inicializamos la interfaz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inicializamos interfaz
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);

        Button botonActualiza;

        //registrar boton
        botonActualiza = (Button)findViewById(R.id.cmdActualizar);


        //registramos clics en boton actualizar
        botonActualiza.setOnClickListener(this);

        //recogemos los textview de la interfaz
        EditText txtTitulo = (EditText)findViewById(R.id.txtTitulo);
        EditText txtEditorial = (EditText)findViewById(R.id.txtEditorial);
        EditText txtAutor = (EditText)findViewById(R.id.txtAutor);
        img = (ImageView)findViewById(R.id.ImageView01);

        Intent intent = getIntent();

        String titulo =  intent.getStringExtra("titulo");
        String imagen =  intent.getStringExtra("imagen");
        rutaoriginal =  intent.getStringExtra("imagen");
        String editorial =  intent.getStringExtra("editorial");
        String autor =  intent.getStringExtra("autor");

        txtTitulo.setText(titulo);
        Bitmap image = BitmapFactory.decodeFile(imagen);
        img.setImageBitmap(image);
        txtEditorial.setText(editorial);
        txtAutor.setText(autor);


        // img = (ImageView)findViewById(R.id.ImageView01);

        ((Button) findViewById(R.id.Button01))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        hayimagen=true;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture2"), SELECT_PICTURE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                /*
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;*/
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);

                //        System.out.println("Image Path : " + selectedImagePath);

                img.setImageURI(selectedImageUri);
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onClick(View v) {

        //registramos los edittext
        EditText txtTitulo = (EditText)findViewById(R.id.txtTitulo);
        ImageView ImageView01 = (ImageView)findViewById(R.id.ImageView01);
        EditText txtEditorial = (EditText)findViewById(R.id.txtEditorial);
        EditText txtAutor = (EditText)findViewById(R.id.txtAutor);

        boolean hayError = false;

        String titulo2 = txtTitulo.getText().toString(); //recogemos txttitulo
        String rutaimagen="";
        if(hayimagen==true) { //comprobamos si hay una imagen seleccionada
            rutaimagen = selectedImagePath;
        }else{
            rutaimagen = rutaoriginal;
        }
        String editorial2 = txtEditorial.getText().toString(); //recogemos txteditorial
        String autor2 = txtAutor.getText().toString(); //recogemos txtautor


        //si el nombre es superior a los 20 caracteres imprimimos error
        if ((titulo2.length() < 1 || titulo2.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del titulo no es válido.");
            hayError = true;
        }
        //si el nombre del grupo no es valido
        if ((editorial2.length() < 1 || editorial2.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del editorial no es válido.");
            hayError = true;
        }
        //si el nombre del grupo no es valido
        if ((autor2.length() < 1 || autor2.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del autor no es válido.");
            hayError = true;
        }

        //si no hay ningun error ingresamos los nuevos datos de disco
        if (hayError == false) {

            Intent i=new Intent(this,MainActivity.class);
            i.putExtra("titulo",titulo2);
            i.putExtra("imagen", rutaimagen);
            i.putExtra("editorial",editorial2);
            i.putExtra("autor",autor2);
            setResult(RESULT_OK, i);

        }

        finish();

    }

    private void mostrarMensaje(String mensaje) {

        Toast.makeText(this.getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

    }

}
