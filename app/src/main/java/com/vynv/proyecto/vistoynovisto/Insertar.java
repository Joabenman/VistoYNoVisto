package com.vynv.proyecto.vistoynovisto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;


public class Insertar extends Activity implements View.OnClickListener {

    LibroAdaptador adaptador;

    ArrayList<Libro> datoslibros;
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;

    String titulo="";
    String imagen="";
    String editorial="";
    String autor="";

    //inicializamos la interfaz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inicializamos interfaz
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar);

        Button botonGuardar;

        //registrar boton
        botonGuardar = (Button)findViewById(R.id.cmdGuardar);


        //registramos clics en boton guardar
        botonGuardar.setOnClickListener(this);


        img = (ImageView)findViewById(R.id.ImageView01);

        ((Button) findViewById(R.id.Button01))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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
                System.out.println("Image Path : " + selectedImagePath);
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

        String titulo = txtTitulo.getText().toString(); //recogemos txttitulo
        String imagen = selectedImagePath;
        String editorial = txtEditorial.getText().toString(); //recogemos txteditorial
        String autor = txtAutor.getText().toString(); //recogemos txtautor


        //si el nombre es superior a los 20 caracteres imprimimos error
        if ((titulo.length() < 1 || titulo.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del disco no es válido.");
            hayError = true;
        }
        //si el nombre del grupo no es valido
        if ((editorial.length() < 1 || editorial.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del grupo no es válido.");
            hayError = true;
        }
        //si el nombre del grupo no es valido
        if ((autor.length() < 1 || autor.length() > 50) && hayError == false) {
            mostrarMensaje("El nombre del grupo no es válido.");
            hayError = true;
        }

        //si no hay ningun error ingresamos los nuevos datos de disco
        if (hayError == false) {

            Intent i=new Intent(this,MainActivity.class);
            i.putExtra("titulo",titulo);
            i.putExtra("imagen",imagen);
            i.putExtra("editorial",editorial);
            i.putExtra("autor",autor);
            setResult(RESULT_OK, i);

        }

        finish();

    }

    private void mostrarMensaje(String mensaje) {

        // Toast.makeText(this.getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

    }

}
