package com.vendemas.vendemas.utilities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Producto;

/**
 * Created by RicK' on 21/02/2018.
 */

public class CatalogoAdapter extends BaseAdapter
{
    private Context ctx;
    private Cliente cliente;
    private Producto[] productos;
    private boolean misProductos;
    public boolean selectItems = false;

    public CatalogoAdapter( Context ctx, Cliente cliente, Producto[] productos )
    {
        this.ctx = ctx;
        this.cliente = cliente;
        this.productos = productos;
        misProductos = cliente.getTelefono().equals(DatosPrueba.yoMismo.getTelefono());
    }


    @Override
    public int getCount()
    {
        return productos.length;
    }

    @Override
    public Object getItem(int i)
    {
        return productos[i];
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    public Producto[] getProductos()
    {
        return productos;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        View rootView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_producto, viewGroup, false);

        productos[i].setSelectItems(selectItems);

        productos[i].setTvNombre((TextView) rootView.findViewById(R.id.nombre));
        productos[i].setTvPrecio((TextView) rootView.findViewById(R.id.precio));
        productos[i].setTvDesc((TextView) rootView.findViewById(R.id.descripcion));
        productos[i].setIvImagen((ImageView) rootView.findViewById(R.id.img));
        productos[i].setEtCantidad(((EditText)rootView.findViewById(R.id.cantidad)));

        productos[i].setTvCantidad((TextView) rootView.findViewById(R.id.lblCantidad));
        productos[i].setChkAddToCart((CheckBox) rootView.findViewById(R.id.addToCart));
        productos[i].setContenedorCantidad(rootView.findViewById(R.id.contenedorCantidad));

        productos[i].setProductoMio(areMisProductos());

        if ( areMisProductos() )
        {
            rootView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(ctx, "Mostrar diálogo para editar ["+productos[i].getNombre()+"]", Toast.LENGTH_SHORT).show();
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
                    productos[i].getChkAddToCart().setSelected(true);
                }
            });
        }

        return rootView;
    }

    private boolean areMisProductos()
    {
        return misProductos;
    }

}
