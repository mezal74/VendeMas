package com.vendemas.vendemas.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.fragments.Catalogo;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Pedido;
import com.vendemas.vendemas.utilities.objetos.Producto;

public class PerfilContacto extends AppCompatActivity
{
    private ImageView ivChatContacto, ivEditContacto;
    private TextView tvNombre, tvTelefono, tvCorreo;
    private Cliente cliente;
    private Producto[] productos;
    private FragmentManager fm = getSupportFragmentManager();
    private Catalogo fgmtCatalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_contacto);

        cargarControles();
        setUpListeners();
        productos = DatosPrueba.productos;

        cargarCatalogo();
    }

    private void cargarCatalogo()
    {
        fgmtCatalogo = new Catalogo();
        fgmtCatalogo.setDatos(productos, cliente);
        fgmtCatalogo.selectItems = true;
        fgmtCatalogo.setListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                productos= fgmtCatalogo.juntarProductosSeleccionados();
                if ( productos != null )
                    continuarConPedido();
                else
                    Toast.makeText(PerfilContacto.this, "Para continuar, selecciona algún producto", Toast.LENGTH_SHORT).show();
            }
        });
        fm.beginTransaction().replace(R.id.contenedor_catalogo, fgmtCatalogo).commit();
    }

    private void continuarConPedido()
    {
        Intent in = new Intent(PerfilContacto.this, VerificarPedido.class);
        Pedido pedido = new Pedido(productos, DatosPrueba.yoMismo, cliente, 0);
        in.putExtra("pedido", pedido);
        in.putExtra("productos", productos);
        startActivity(in);
        finish();
    }

    private void setUpListeners()
    {
        ivChatContacto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(PerfilContacto.this, Chat.class);
                i.putExtra("usuario", cliente);
                startActivity(i);
                finish();
            }
        });

        ivEditContacto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PerfilContacto.this, "Editar a: "+cliente.getNombre().split(" ")[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarControles()
    {
        ivChatContacto = findViewById(R.id.chatTo);
        ivEditContacto = findViewById(R.id.editar);
        tvNombre = findViewById(R.id.nombre);
        tvCorreo = findViewById(R.id.correo);
        tvTelefono = findViewById(R.id.telefono);
        if ( getIntent().getExtras() != null ) cliente = getIntent().getExtras().getParcelable("usuario");
        else cliente = DatosPrueba.clientes[0];

        tvNombre.setText(cliente.getNombre());
        tvTelefono.setText(cliente.getTelefono());
        tvCorreo.setText(cliente.getCorreo());
    }
}
