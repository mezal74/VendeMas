package com.vendemas.vendemas.utilities.mensajeria.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.utilities.mensajeria.moldes.Mensaje;

import static com.vendemas.vendemas.activities.Chat.ENVIADO;

/**
 * Created by RicK' on 02/03/2018.
 */

public class MensajesAdapter extends RecyclerView.Adapter
{
    private static int posicion = 0;
    private Context ctx;
    private Mensaje[] mensajes;
    private int last;

    public Mensaje[] getMensajes()
    {
        return mensajes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mensaje;
        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            mensaje = layout.findViewById(R.id.mensaje);
        }
    }

    public MensajesAdapter(Context c, Mensaje[] m)
    {
        posicion = 0;
        ctx = c;
        mensajes = m;
        last = m[m.length-1].getFrom();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if ( mensajes[posicion].getFrom() == ENVIADO )
        {
            Log.d("cargarMensaje()", "onBindViewHolder() -> Enviado");
            holder = new ViewHolder(inflater.inflate(R.layout.row_mensaje_enviado, parent, false));
        }
        else
        {
            Log.d("cargarMensaje()", "onBindViewHolder() -> Recibido");
            holder = new ViewHolder(inflater.inflate(R.layout.row_mensaje_recibido, parent, false));
        }
        Log.d("cargarMensaje()", "onCreateViewHolder()");

        posicion++;

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        mensajes[position].setTvMensaje(((ViewHolder) holder).mensaje);
    }

    @Override
    public int getItemCount()
    {
        return mensajes.length;
    }
}
