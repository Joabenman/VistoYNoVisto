package com.vynv.proyecto.vistoynovisto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class MainActivity extends Activity {

    Socket miCliente;

    String direccion = "192.168.56.1";//oJo suele cambiar
    int puerto = 6000;



    ArrayList<Libro> datoslibros = new ArrayList();
    LibroAdaptador adaptador;
    EditText editsearch;

    String titulo="";
    String imagen="";
    String editorial="";
    String autor="";
    String nfichero="recibelibros.xml";

    private ListView listlibros;
    private int selectedItem;
    private TextView lblEtiqueta;
    private ListView lstOpciones;

    String menusocket;

    //cuando se lanza la aplicacion
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);



        lblEtiqueta = (TextView)findViewById(R.id.LblEtiqueta);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);

        //enables filtering for the contents of the given ListView
        lstOpciones.setTextFilterEnabled(true);

        //Log.d("tag",datoslibros.get(0).getTitulo());
        adaptador = new LibroAdaptador(this, datoslibros);

        lstOpciones.setAdapter(adaptador);

        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //Alternativa 1:
                String opcionSeleccionada =
                        ((Libro) a.getAdapter().getItem(position)).getTitulo();

                lblEtiqueta.setText("Libro seleccionado: " + opcionSeleccionada);
            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }

        });
        listlibros = (ListView) findViewById(R.id.LstOpciones);

        //filtro
        EditText myFilter = (EditText) findViewById(R.id.search);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s.toString());
            }
        });

    }



    //cargamos el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onResume() {

        super.onResume();
        if(datoslibros.size()==0){
            cargarLibros();
        }
        registerForContextMenu(listlibros);
    }

    @Override
    public void onStop() {
        super.onStop();
        guardarLibrosSincronizado();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("titulo")) {
                // Toast.makeText(this, data.getExtras().getString("titulo"), Toast.LENGTH_SHORT).show();
                titulo = data.getExtras().getString("titulo");
            }
            if (data.hasExtra("imagen")) {
                // Toast.makeText(this, data.getExtras().getString("imagen"),Toast.LENGTH_SHORT).show();
                imagen = data.getExtras().getString("imagen");
            }
            if (data.hasExtra("editorial")) {
                // Toast.makeText(this, data.getExtras().getString("editorial"),  Toast.LENGTH_SHORT).show();
                editorial = data.getExtras().getString("editorial");
            }
            if (data.hasExtra("autor")) {
                // Toast.makeText(this, data.getExtras().getString("autor"),Toast.LENGTH_SHORT).show();
                autor = data.getExtras().getString("autor");
            }

            Log.d("tag", titulo);
            datoslibros.add(new Libro(titulo, imagen , editorial, autor));
            adaptador.notifyDataSetChanged();

        }
        if (resultCode == RESULT_OK && requestCode == 3) {
            if (data.hasExtra("titulo")) {
                //  Toast.makeText(this, data.getExtras().getString("titulo"), Toast.LENGTH_SHORT).show();
                titulo = data.getExtras().getString("titulo");
            }
            if (data.hasExtra("imagen")) {
                // Toast.makeText(this, data.getExtras().getString("imagen"), Toast.LENGTH_SHORT).show();
                imagen = data.getExtras().getString("imagen");
            }
            if (data.hasExtra("editorial")) {
                //   Toast.makeText(this, data.getExtras().getString("editorial"), Toast.LENGTH_SHORT).show();
                editorial = data.getExtras().getString("editorial");
            }
            if (data.hasExtra("autor")) {
                // Toast.makeText(this, data.getExtras().getString("autor"), Toast.LENGTH_SHORT).show();
                autor = data.getExtras().getString("autor");
            }

            Log.d("titulo", "en el main" + titulo);
            Log.d("titulo", "en el main" + selectedItem);
            datoslibros.set(selectedItem, new Libro(titulo, imagen , editorial, autor));
            adaptador.notifyDataSetChanged();
        }
    }

    //opciones del menu de la barra de herramientas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.insertar:
                Intent intent = new Intent(this, Insertar.class);

                startActivityForResult(intent, 1);

                break;

            case R.id.guardar:
                guardarLibrosSincronizado();
                break;

            case R.id.cargar:
                cargarLibros();
                break;

            case R.id.importar:
                //CargarXmlTask cargarxml = new CargarXmlTask();
                //cargarxml.execute("http://192.168.42.235/libros.xml");
                /*String direccion = "192.168.42.235";//oJo
                int puerto = 6000;

                relacionservidor cargamos = new relacionservidor(direccion, puerto);
                cargamos.execute();*/

                menusocket="1";
               // relacionservidor cargamos = new relacionservidor(direccion, puerto, menusocket);
               // cargamos.execute();


                break;

            case R.id.exportar:
                //CargarXmlTask cargarxml = new CargarXmlTask();
                //cargarxml.execute("http://192.168.42.235/libros.xml");
                /*String direccion = "192.168.42.235";//oJo
                int puerto = 6000;

                relacionservidor cargamos = new relacionservidor(direccion, puerto);
                cargamos.execute();*/
                menusocket="0";
               // relacionservidor cargamos2 = new relacionservidor(direccion, puerto, menusocket);
               // cargamos2.execute();
                break;




            default:

                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        selectedItem = info.position;
        menu.setHeaderTitle(datoslibros.get(info.position).getTitulo());
        inflater.inflate(R.menu.popup_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case R.id.mnuVisualizar:

                Intent intentVisualiza = new Intent(this, Visualizar.class);
                titulo =(datoslibros.get(info.position).getTitulo());
                imagen =(datoslibros.get(info.position).getRutaimagen());
                editorial =(datoslibros.get(info.position).getEditorial());
                autor =(datoslibros.get(info.position).getAutor());

                intentVisualiza.putExtra("titulo",titulo);
                intentVisualiza.putExtra("imagen",imagen);
                intentVisualiza.putExtra("editorial", editorial);
                intentVisualiza.putExtra("autor", autor);
                startActivity(intentVisualiza);

            break;

            case R.id.mnuEditar:

                Intent intent = new Intent(this, Editar.class);
                titulo =(datoslibros.get(info.position).getTitulo());
                imagen =(datoslibros.get(info.position).getRutaimagen());
                editorial =(datoslibros.get(info.position).getEditorial());
                autor =(datoslibros.get(info.position).getAutor());

                Log.d("titulo", titulo);
                intent.putExtra("titulo",titulo);
                intent.putExtra("imagen",imagen);
                intent.putExtra("editorial",editorial);
                intent.putExtra("autor", autor);
                startActivityForResult(intent, 3);
                break;


            case R.id.mnuBorrar:
                //lo borramos de la base de datos
                borrar(info);

                //lo borramos del listview y lo actualizamos
                datoslibros.remove(selectedItem);
                adaptador.notifyDataSetChanged();

                break;

        }

        return true;

    }

    private void guardarLibrosSincronizado() {
        //guardamos los libros en la base de datos
        LibrosSQLiteHelper Librosdbh = new LibrosSQLiteHelper(this, "DBLibros", null, 1);
        SQLiteDatabase db = Librosdbh.getWritableDatabase();
        ArrayList<Libro> enviadatos = new ArrayList();
        enviadatos = datoslibros;
        datoslibros=Librosdbh.saveSincronizado(enviadatos);
        db.close();

        adaptador = new LibroAdaptador(this, datoslibros); //actualizamos el listview
        lstOpciones.setAdapter(adaptador);
        //adaptador.notifyDataSetChanged();
        Toast.makeText(this, "datos guardados de forma sincronizada ", Toast.LENGTH_SHORT).show();

    }



    private void cargarLibros() {
        //cargamos los libros desde la base de datos
        Log.d("socket", "cargamos datos");
        LibrosSQLiteHelper Librosdbh = new LibrosSQLiteHelper(this, "DBLibros", null, 1);
        SQLiteDatabase db = Librosdbh.getReadableDatabase();

        datoslibros= Librosdbh.load();
        db.close();

        adaptador = new LibroAdaptador(this, datoslibros); //actualizamos el listview
        lstOpciones.setAdapter(adaptador);
        // adaptador.notifyDataSetChanged();
        Toast.makeText(this, "datos cargados ", Toast.LENGTH_SHORT).show();
    }

    private void borrar(AdapterView.AdapterContextMenuInfo info){
        //lo borramos de la base de datos
        LibrosSQLiteHelper Librosdbh = new LibrosSQLiteHelper(this, "DBLibros", null, 1);
        titulo =(datoslibros.get(info.position).getTitulo()); //pillamos el titulo del elemento seleccionado
        Librosdbh.delete(titulo);

    }







/*


    //adaptador del listview
    class LibroAdaptador extends ArrayAdapter<Libro> {

        Activity context;
        private ArrayList<Libro> listalibrosoriginal;
        private ArrayList<Libro> listalibros;
        private FiltroLibros filter;

        public LibroAdaptador(Context context, ArrayList<Libro> datoslib) {
            super(context, R.layout.listitem_libro, datoslib);
            this.listalibrosoriginal = new ArrayList<Libro>();
            this.listalibrosoriginal.addAll(datoslib);
            this.listalibros= new ArrayList<Libro>();
            this.listalibros.addAll(datoslib);
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new FiltroLibros();
            }
            return filter;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_libro, null);

            TextView LblTitulo = (TextView)item.findViewById(R.id.LblListaTitulo);
            LblTitulo.setText(datoslibros.get(position).getTitulo());

            ImageView ImgLibro = (ImageView)item.findViewById(R.id.ImgListaLibro);

            Bitmap image = BitmapFactory.decodeFile(datoslibros.get(position).getRutaimagen());

            ImgLibro.setImageBitmap(image);

            TextView LblEditorial = (TextView) item.findViewById(R.id.LblListaEditorial);
            LblEditorial.setText(datoslibros.get(position).getEditorial());

            TextView LblAutor = (TextView) item.findViewById(R.id.LblListaAutor);
            LblAutor.setText(datoslibros.get(position).getAutor());

            return(item);
        }

        private class FiltroLibros extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0)
                {
                    ArrayList<Libro> filteredItems = new ArrayList<Libro>();

                    for(int i = 0, l = listalibrosoriginal.size(); i < l; i++)
                    {
                        Log.d("filtro", listalibrosoriginal.size() + "  " + listalibrosoriginal.get(i) + "  " + constraint);
                        Libro libro = listalibrosoriginal.get(i);

                        if(libro.getTitulo().toLowerCase().contains(constraint))
                            filteredItems.add(libro);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                }
                else
                {
                    synchronized(this)
                    {
                        result.values = listalibrosoriginal;
                        result.count = listalibrosoriginal.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                listalibros = (ArrayList<Libro>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = listalibros.size(); i < l; i++)
                    add(listalibros.get(i));
                notifyDataSetInvalidated();
            }
        }
    }*/



}
