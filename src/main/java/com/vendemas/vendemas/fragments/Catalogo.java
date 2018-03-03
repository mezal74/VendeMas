package com.vendemas.vendemas.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.utilities.adapters.CatalogoAdapter;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Producto;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Catalogo extends Fragment
{
    private Producto[] productos;
    private Cliente cliente;
    private boolean misProductos;
    private TextView tvNombre;
    private ImageView fabAdOk;
    private ListView listaProductos;
    private View.OnClickListener listener;
    public boolean borrarTitulo;
    public boolean selectItems;

    public Catalogo(){}

    public void setDatos(Producto[] ps, Cliente c)
    {
        cliente = c;
        productos = ps;
        misProductos = cliente.getTelefono().equals(DatosPrueba.yoMismo.getTelefono());
    }

    public void setListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fgmt_catalogo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        fabAdOk = getActivity().findViewById(R.id.fabAgregarOk);
        tvNombre = getActivity().findViewById(R.id.titulo_lista);
        listaProductos = getActivity().findViewById(R.id.listaProductos);
        if ( !borrarTitulo ) tvNombre.setText("Cataogo de: "+cliente.getNombre().split(" ")[0]);
        else tvNombre.setVisibility(View.GONE);

        View.OnClickListener listener = null;

        if (this.listener != null)
        {
            listener = this.listener;
        }
        if ( !misProductos )
        {
            fabAdOk.setImageResource(android.R.drawable.ic_menu_send);
        }
        else
        {
            listener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(getActivity(), "Agregar Nuevo producto", Toast.LENGTH_SHORT).show();
                }
            };
        }
        fabAdOk.setOnClickListener(listener);
        CatalogoAdapter cAdapter = new CatalogoAdapter(getActivity(), cliente, productos);
        cAdapter.selectItems = selectItems;
        listaProductos.setAdapter(cAdapter);
    }

    public int getProductosSeleccionados()
    {
        int productosSeleccionados = 0;
        for (int i = 0; i < productos.length; i++)
        {
            if ( productos[i].isSeleccionado() )
                productosSeleccionados++;
        }
        return productosSeleccionados;
    }

    public Producto[] juntarProductosSeleccionados()
    {
        int cantidad = getProductosSeleccionados(), j = 0;
        Producto[] showProductos;
        if ( cantidad > 0 ) showProductos = new Producto[cantidad];
        else return null;
        for (int k = 0; k < productos.length; k++)
        {
            if ( productos[k].isSeleccionado() )
            {
                showProductos[j] = productos[k];
                Log.e("ShowProductos", "Productos: "+showProductos[j].toString());
                j++;
            }
        }
        return showProductos;
    }

}
