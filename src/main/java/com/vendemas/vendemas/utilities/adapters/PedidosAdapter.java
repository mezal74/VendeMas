package com.vendemas.vendemas.utilities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.utilities.objetos.Pedido;

import static com.vendemas.vendemas.fragments.Pedidos.COMPRAS;
import static com.vendemas.vendemas.fragments.Pedidos.VENTAS;

/**
 * Created by RicK' on 21/02/2018.
 */

public class PedidosAdapter extends BaseAdapter
{
    private Pedido[] pedidos, showProductos;
    private Context ctx;
    private short estdoShow;
    private boolean isCompras;
    private int cantidad = 0;

    public PedidosAdapter(Pedido[] pedidos, Context ctx, short estdoShow, boolean isCompras)
    {
        this.pedidos = pedidos;
        this.ctx = ctx;
        this.estdoShow = estdoShow;
        this.isCompras = isCompras;
        saberQueProductosMostrar();
    }

    private void saberQueProductosMostrar()
    {
        for (int i = 0; i < pedidos.length; i++)
        {
            if ( pedidos[i].getIsCompra() == COMPRAS && isCompras == COMPRAS && pedidos[i].getEstado() == estdoShow )
            {
                cantidad++;
            }
            else if ( pedidos[i].getIsCompra() == VENTAS && isCompras == VENTAS && pedidos[i].getEstado() == estdoShow )
            {
                cantidad++;
            }
        }

        showProductos = new Pedido[cantidad];
        int j = 0;
        for (int i = 0; i < pedidos.length; i++)
        {
            if ( pedidos[i].getIsCompra() == COMPRAS && isCompras  == COMPRAS && pedidos[i].getEstado() == estdoShow )
            {
                showProductos[j] = pedidos[i];
                j++;
            }
            else if ( pedidos[i].getIsCompra() == VENTAS && isCompras == VENTAS && pedidos[i].getEstado() == estdoShow )
            {
                showProductos[j] = pedidos[i];
                j++;
            }
        }
    }


    @Override
    public int getCount()
    {
        return cantidad;
    }

    @Override
    public Object getItem(int i)
    {
        if ( showProductos != null ) return showProductos[i];
        else return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rootView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_pedido, viewGroup, false);

        showProductos[i].setTvNombre((TextView) rootView.findViewById(R.id.nombreContacto));
        showProductos[i].setTvTelefono((TextView) rootView.findViewById(R.id.telefono));
        showProductos[i].setTvCorreo((TextView) rootView.findViewById(R.id.correo));

        showProductos[i].setTvFecha((TextView) rootView.findViewById(R.id.fecha));
        showProductos[i].setTvTotalPagar((TextView) rootView.findViewById(R.id.totalPagar));
        showProductos[i].setTvAbonado((TextView) rootView.findViewById(R.id.abonado));
        showProductos[i].setTvRestante((TextView) rootView.findViewById(R.id.restante));
        showProductos[i].setTvEstado((TextView) rootView.findViewById(R.id.lblEstatus));

        showProductos[i].setTvListadoProductos((TextView) rootView.findViewById(R.id.listadoProductos));

        return rootView;
    }
}
