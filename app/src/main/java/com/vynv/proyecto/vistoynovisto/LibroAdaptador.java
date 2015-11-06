package com.vynv.proyecto.vistoynovisto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kinstorm on 05/11/2015.
 */
    //adaptador del listview
    public  class LibroAdaptador extends ArrayAdapter<Libro> {

        Activity context;

        private ArrayList<Libro> listalibros = new  ArrayList<Libro>();
    private ArrayList<Libro> listalibrosoriginal = new  ArrayList<Libro>();

        private FiltroLibros filter;

        public LibroAdaptador(Context context, ArrayList<Libro> datoslib) {
            super(context, R.layout.listitem_libro, datoslib);
            //this.listalibrosoriginal = new ArrayList<Libro>();
            // this.listalibrosoriginal.addAll(datoslib);
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
            LblTitulo.setText(listalibros.get(position).getTitulo());

            ImageView ImgLibro = (ImageView)item.findViewById(R.id.ImgListaLibro);

            Bitmap image = BitmapFactory.decodeFile(listalibros.get(position).getRutaimagen());

            ImgLibro.setImageBitmap(image);

            TextView LblEditorial = (TextView) item.findViewById(R.id.LblListaEditorial);
            LblEditorial.setText(listalibros.get(position).getEditorial());

            TextView LblAutor = (TextView) item.findViewById(R.id.LblListaAutor);
            LblAutor.setText(listalibros.get(position).getAutor());

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
    }

