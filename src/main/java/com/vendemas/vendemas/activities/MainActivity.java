package com.vendemas.vendemas.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.vendemas.vendemas.R;
import com.vendemas.vendemas.fragments.Catalogo;
import com.vendemas.vendemas.fragments.Contactos;
import com.vendemas.vendemas.fragments.Pedidos;
import com.vendemas.vendemas.database.DatosPrueba;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fm = getSupportFragmentManager();
    private ImageView ivPedidosChats;
    private TextView tvTitulo;
    private boolean isChatOrPedidos = CHAT;
    private static final boolean CHAT = false, PEDIDOS = true;
    private static final String lbl_chats = "Chats";
    private static final String lbl_pedidos = "Pedidos";
    private Catalogo fgmtCatalogo;
    private Contactos fgmtContactos;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cargarControles();
        setUpListeners();
        cargarContactos();
    }

    private void setUpListeners()
    {
        ivPedidosChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChatOrPedidos == CHAT) {
                    cargarPedidos();
                } else {
                    cargarContactos();
                }
            }
        });
    }

    private void cargarPedidos()
    {
        isChatOrPedidos = PEDIDOS;
        tvTitulo.setText(lbl_pedidos);
        ivPedidosChats.setImageDrawable(getDrawable(R.drawable.ic_chat));
        fm.beginTransaction().replace(R.id.contenedor_main, new Pedidos()).commit();
    }

    private void cargarContactos()
    {
        isChatOrPedidos = CHAT;
        fgmtContactos = new Contactos();
        tvTitulo.setText(lbl_chats);
        ivPedidosChats.setImageDrawable(getDrawable(android.R.drawable.ic_dialog_dialer));
        fm.beginTransaction().replace(R.id.contenedor_main, fgmtContactos).commit();
    }

    private void cargarControles()
    {
        ivPedidosChats = findViewById(R.id.goChat);
        tvTitulo = findViewById(R.id.titulo);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item )
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            cargarContactos();
        } else if (id == R.id.nav_catalogo) {
            cargarCatlogo();
        }
        /*else if (id == R.id.nav_slideshow)
        {

        }*/
        else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainLogin.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void cargarCatlogo()
    {
        ((TextView) findViewById(R.id.titulo)).setText("Mi catálogo");
        fgmtCatalogo = new Catalogo();
        fgmtCatalogo.setDatos(DatosPrueba.productos, DatosPrueba.yoMismo);
        fgmtCatalogo.borrarTitulo = true;
        fm.beginTransaction().replace(R.id.contenedor_main, fgmtCatalogo).commit();
    }

    public void mostrarDialogoNormal()
    {
        pd = new ProgressDialog(this);
        pd.setMessage("Cargando contactos...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public void disposeDialogo()
    {
        pd.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if ( requestCode == 2 )
        {
            if ( grantResults[0] == PERMISSION_GRANTED )
            {
                fgmtContactos.checkPermisos();
            }
            else
            {
                Toast.makeText(this, "Se necesita el permiso de contactos para mostrarlos", Toast.LENGTH_SHORT).show();
                fgmtContactos.mostrarClientes();
                fgmtContactos.navigation.setSelectedItemId(R.id.nav_clientes);
            }
        }
    }
}
