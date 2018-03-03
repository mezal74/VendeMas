package com.vendemas.vendemas.activities;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.fragments.OverViewPedido;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Pedido;
import com.vendemas.vendemas.utilities.objetos.Producto;

public class VerificarPedido extends AppCompatActivity
{
    private Pedido pedido;
    private FragmentManager fm = getSupportFragmentManager();
    private OverViewPedido fgmtOverViewProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_pedido);

        if (getIntent().getExtras() != null)
        {
            Log.e("Extras", "Sí hay extras");
            pedido = getIntent().getExtras().getParcelable("pedido");
            Producto[] productos;
            Parcelable[] productosPrcel = getIntent().getExtras().getParcelableArray("productos");
            productos = new Producto[productosPrcel.length];
            for (int i = 0; i < productosPrcel.length; i++)
            {
                productos[i] = (Producto) productosPrcel[i];
            }
            if (productos != null) pedido.setProductos(productos);
            else
            {
                Toast.makeText(this, "Pedidos es nulo -.-", Toast.LENGTH_SHORT).show();
                finish();
            }
            if ( pedido != null )
                Log.e("Pedido", "No es Null");
        }
        else finish();



        pedido.setTvNombre((TextView) findViewById(R.id.nombreContacto));
        pedido.setTvTelefono((TextView) findViewById(R.id.telefono));
        pedido.setTvCorreo((TextView) findViewById(R.id.correo));
        pedido.setTvFecha((TextView) findViewById(R.id.fecha));

        pedido.setTvTotalPagar((TextView) findViewById(R.id.totalPagar));
        pedido.setTvAbonado((TextView) findViewById(R.id.abonado));
        pedido.setTvRestante((TextView) findViewById(R.id.restante));

        pedido.setTvEstado((TextView) findViewById(R.id.lblEstatus));

        //pedido.setTvListadoProductos((TextView) findViewById(R.id.listadoProductos));

        fgmtOverViewProductos = new OverViewPedido();
        fgmtOverViewProductos.productos = pedido.getProductos();
        if ( pedido.getComprador().getTelefono().equals(DatosPrueba.yoMismo.getTelefono()) ) fgmtOverViewProductos.cliente = pedido.getVendedor();
        else fgmtOverViewProductos.cliente = pedido.getComprador();

        fm.beginTransaction().replace(R.id.contenedor_revision_productos, fgmtOverViewProductos).commit();

    }

    public void updateTotales()
    {
        final Handler h = new Handler();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final double total = pedido.calcularTotal();
                h.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pedido.getTvTotalPagar().setText("$"+total);
                    }
                });
            }
        });
    }
}
