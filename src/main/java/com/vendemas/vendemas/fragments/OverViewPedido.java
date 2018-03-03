package com.vendemas.vendemas.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.utilities.adapters.ProductosReviewAdapter;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Producto;

import static android.view.View.GONE;

/**
 * Created by RicK' on 21/02/2018.
 */

public class OverViewPedido extends Fragment
{
    public Producto[] productos;
    private RecyclerView listaProductos;
    public Cliente cliente;
    private Button btnAceptar;

    public OverViewPedido () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fgmt_review_pedido, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        listaProductos = getActivity().findViewById(R.id.listaReviewProductos);
        btnAceptar = getActivity().findViewById(R.id.btnAceptar);

        listaProductos.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaProductos.setLayoutManager(layoutManager);

        listaProductos.setAdapter(new ProductosReviewAdapter(getActivity(), cliente, productos));

        getActivity().findViewById(R.id.listadoProductos).setVisibility(GONE);

        btnAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(), "Pedido registrado con éxito.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }
}
