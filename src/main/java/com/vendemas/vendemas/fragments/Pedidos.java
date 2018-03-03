package com.vendemas.vendemas.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.utilities.adapters.PedidosAdapter;
import com.vendemas.vendemas.database.DatosPrueba;

import static com.vendemas.vendemas.utilities.objetos.Pedido.POR_ENTREGAR;
import static com.vendemas.vendemas.utilities.objetos.Pedido.POR_PAGAR;
import static com.vendemas.vendemas.utilities.objetos.Pedido.POR_VERIFICAR;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Pedidos extends Fragment
{
    public static final boolean COMPRAS = false, VENTAS = true;
    private boolean tipoPedidos = VENTAS;
    private short estadoPedidos = POR_ENTREGAR;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.nav_por_aprovar:
                    mostrarPorAprovar();
                    return true;
                case R.id.nav_por_entregar:
                    mostrarPorEntregar();
                    return true;
                case R.id.nav_por_pagar:
                    mostrarPorPagar();
                    return true;
            }
            return false;
        }
    };

    private View switchTipoPedidos, vIndicadorSwitchTipoPedidos;
    private TextView lblVentas, lblCompras;
    private ListView listaPedidos;

    public Pedidos(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fgmt_pedidos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        cargarControles();
        setUpListeners();
    }

    private void cargarControles()
    {
        BottomNavigationView navigation = getActivity().findViewById(R.id.navegador);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchTipoPedidos = getActivity().findViewById(R.id.contenedor_tipo_pedidos);
        vIndicadorSwitchTipoPedidos = switchTipoPedidos.findViewById(R.id.vIndicadorSwitch);
        lblVentas = switchTipoPedidos.findViewById(R.id.lblVentas);
        lblCompras = switchTipoPedidos.findViewById(R.id.lblCompras);
        listaPedidos = getActivity().findViewById(R.id.listaPedidos);
        mostrarPorEntregar();
    }

    private void setUpListeners()
    {
        switchTipoPedidos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if ( tipoPedidos == COMPRAS )
                {
                    tipoPedidos = VENTAS;
                    vIndicadorSwitchTipoPedidos.setX(vIndicadorSwitchTipoPedidos.getX()-vIndicadorSwitchTipoPedidos.getWidth());

                }
                else
                {
                    tipoPedidos = COMPRAS;
                    vIndicadorSwitchTipoPedidos.setX(vIndicadorSwitchTipoPedidos.getX() + vIndicadorSwitchTipoPedidos.getWidth());
                }
                refreshPedidos();
            }
        });
    }

    private void refreshPedidos()
    {
        //listaPedidos.setAdapter(new PedidosAdapter(DatosPrueba.pedidos, getActivity(), estadoPedidos, tipoPedidos));
    }

    private void mostrarPorPagar()
    {
        estadoPedidos = POR_PAGAR;
        refreshPedidos();
    }

    private void mostrarPorEntregar()
    {
        estadoPedidos = POR_ENTREGAR;
        refreshPedidos();
    }

    private void mostrarPorAprovar()
    {
        estadoPedidos = POR_VERIFICAR;
        refreshPedidos();
    }
}
