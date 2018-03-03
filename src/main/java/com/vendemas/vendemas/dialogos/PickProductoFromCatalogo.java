package com.vendemas.vendemas.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.fragments.Catalogo;
import com.vendemas.vendemas.utilities.adapters.CatalogoAdapter;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Producto;

/**
 * Created by RicK' on 21/02/2018.
 */

public class PickProductoFromCatalogo extends Dialog
{
    private Producto[] productos;
    private Cliente cliente;
    private boolean misProductos, selectItems;
    private TextView tvNombre;
    private ImageView fabAdOk;
    private ListView listaProductos;
    private View.OnClickListener listener;

    public PickProductoFromCatalogo(@NonNull Context context, Producto[] productos, Cliente cliente, boolean selectItems, @Nullable View.OnClickListener listener)
    {
        super(context);
        this.productos = productos;
        this.cliente = cliente;
        this.listener = listener;
        this.selectItems = selectItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fgmt_catalogo);
        fabAdOk = findViewById(R.id.fabAgregarOk);
        tvNombre = findViewById(R.id.titulo_lista);
        listaProductos = findViewById(R.id.listaProductos);

        tvNombre.setText("Cataogo de: "+cliente.getNombre().split(" ")[0]);

        View.OnClickListener listener;

        if (this.listener != null)
        {
            listener = this.listener;
        }
        else if ( !misProductos )
        {
            listener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(getContext(), "Seguir con pedido", Toast.LENGTH_SHORT).show();
                }
            };
        }
        else
        {
            listener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(getContext(), "Agregar Nuevo producto", Toast.LENGTH_SHORT).show();
                }
            };
        }
        fabAdOk.setImageResource(android.R.drawable.ic_menu_send);
        fabAdOk.setOnClickListener(listener);
        CatalogoAdapter cAdapter = new CatalogoAdapter(getContext(), cliente, productos);
        cAdapter.selectItems = selectItems;
        listaProductos.setAdapter(cAdapter);
    }

    public Producto[] getProductos()
    {
        return  productos;
    }

    public int getProductosSeleccionados()
    {
        int productosSeleccionados = 0;
        for (int i = 0; i < productos.length; i++)
        {
            if ( productos[i].getChkAddToCart().isChecked() )
                productosSeleccionados++;
        }
        return productosSeleccionados;
    }
}
