package com.vendemas.vendemas.utilities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.activities.Chat;
import com.vendemas.vendemas.activities.PerfilContacto;
import com.vendemas.vendemas.dialogos.AddContacto;
import com.vendemas.vendemas.utilities.objetos.Cliente;

/**
 * Created by RicK' on 20/02/2018.
 */

public class ContactosAdapter extends BaseAdapter
{
    private Cliente[] clientes;
    private Context ctx;

    public ContactosAdapter(Cliente[] clientes, Context ctx)
    {
        this.clientes = clientes;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return clientes.length;
    }

    @Override
    public Object getItem(int i) {
        return clientes[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        View rootView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_cliente, viewGroup, false);

        if ( clientes[i] == null || clientes[i].getNombre() == null )
        {
            rootView.setVisibility(View.GONE);
            return rootView;
        }

        ((TextView)rootView.findViewById(R.id.nombre)).setText(clientes[i].getNombre());
        ((TextView)rootView.findViewById(R.id.telefono)).setText(clientes[i].getTelefono());
        if ( clientes[i].getCorreo() != null ) {
            ((TextView) rootView.findViewById(R.id.correo)).setText(clientes[i].getCorreo());

            rootView.findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ctx, PerfilContacto.class);
                    in.putExtra("usuario", clientes[i]);
                    ctx.startActivity(in);
                }
            });

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ctx, Chat.class);
                    in.putExtra("usuario", clientes[i]);
                    ctx.startActivity(in);
                }
            });
        }
        else
        {
            rootView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    new AddContacto(ctx, clientes[i]).show();
                }
            });
        }

        return rootView;
    }
}
