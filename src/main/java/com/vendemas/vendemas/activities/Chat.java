package com.vendemas.vendemas.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vendemas.vendemas.R;
import com.vendemas.vendemas.database.FirebaseManager;
import com.vendemas.vendemas.dialogos.PickProductoFromCatalogo;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.Comunes;
import com.vendemas.vendemas.utilities.mensajeria.adapters.MensajesAdapter;
import com.vendemas.vendemas.utilities.mensajeria.moldes.Mensaje;
import com.vendemas.vendemas.utilities.mensajeria.moldes.Notification;
import com.vendemas.vendemas.utilities.mensajeria.moldes.Sender;
import com.vendemas.vendemas.utilities.mensajeria.moldes.mResponse;
import com.vendemas.vendemas.utilities.mensajeria.remoto.APIService;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Pedido;
import com.vendemas.vendemas.utilities.objetos.Producto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vendemas.vendemas.database.DatosPrueba.yoMismo;

public class Chat extends AppCompatActivity
{
    public static Handler h;
    public static String tokenDestino;
    public static RecyclerView listaMensajes;
    public static MensajesAdapter mensajesAdapter;

    public static final int ENVIADO = 0, RECIBIDO = 1;

    private Cliente cliente;
    private ImageView ivShowCatalogo, ivImagenCliente, ivSendMensaje;
    private TextView tvNombreCliente;
    public static EditText etMensaje;
    private Toolbar toolbar;
    private Producto[] showProductos;
    private PickProductoFromCatalogo pickProductosDialog;
    private APIService mService;
    public static boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mService = Comunes.getFCMClient();
        h = new Handler();
        cargarControles();
        setUpListeners();
    }

    private void setUpListeners()
    {
        toolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Chat.this, PerfilContacto.class));
                finish();
            }
        });

        ivShowCatalogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(Chat.this)
                        .setPositiveButton("Mi catálogo", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                pickProductosDialog = new PickProductoFromCatalogo(Chat.this, DatosPrueba.productos, yoMismo, true, new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        int cantidad = pickProductosDialog.getProductosSeleccionados(), j = 0;
                                        if ( cantidad > 0 ) showProductos = new Producto[cantidad];
                                        for (int k = 0; k < pickProductosDialog.getProductos().length; k++)
                                        {
                                            if ( pickProductosDialog.getProductos()[k].getChkAddToCart().isChecked() )
                                            {
                                                showProductos[j] = pickProductosDialog.getProductos()[k];
                                                j++;
                                            }
                                        }
                                        pickProductosDialog.dismiss();
                                    }
                                });
                                pickProductosDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        if (showProductos != null ) showProductosSeleccionados();
                                        else Toast.makeText(Chat.this, "No se han seleccionado productos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                pickProductosDialog.show();
                            }
                        })
                        .setNegativeButton("Catalogo de: " + cliente.getNombre().split(" ")[0], new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                pickProductosDialog = new PickProductoFromCatalogo(Chat.this, DatosPrueba.productos, cliente, true, new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        int cantidad = pickProductosDialog.getProductosSeleccionados(), j = 0;
                                        if ( cantidad > 0 ) showProductos = new Producto[cantidad];

                                        for (int k = 0; k < pickProductosDialog.getProductos().length; k++)
                                        {
                                            if ( pickProductosDialog.getProductos()[k].getChkAddToCart().isChecked() )
                                            {
                                                showProductos[j] = pickProductosDialog.getProductos()[k];
                                                Log.e("ShowProductos", "Productos: "+showProductos[j].toString());
                                                j++;
                                            }
                                        }
                                        pickProductosDialog.dismiss();
                                    }
                                });
                                pickProductosDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        if (showProductos != null ) showProductosSeleccionados();
                                        else Toast.makeText(Chat.this, "No se han seleccionado productos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                pickProductosDialog.show();
                            }
                        })
                        .setTitle("Mostrar catálogo")
                        .setMessage("¿Qué catalogo quieres ver?")
                        .show();
            }
        });

        ivSendMensaje.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String mensaje = etMensaje.getText().toString();
                etMensaje.setText("");
                enviarMensaje(mensaje);
            }
        });
        etMensaje.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    private void enviarMensaje(final String mensaje)
    {
        Notification notification = new Notification(mensaje, yoMismo.getNombre());
        Sender sender = new Sender(notification, tokenDestino);
        if ( tokenDestino != null ) mService.sendNotification(sender)
            .enqueue(new Callback<mResponse>()
            {
                @Override
                public void onResponse(Call<mResponse> call, Response<mResponse> response)
                {
                    if ( response.body().success == 1 )
                    {
                        cargarMensaje(mensaje, ENVIADO, Chat.this);
                    }
                    else if ( cliente.getTokenChat().equals("") )
                    {
                        Toast.makeText(Chat.this, "Este cliente no tiene la aplicación instalada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<mResponse> call, Throwable t)
                {
                    t.printStackTrace();
                }
            });
        else
        {
            Toast.makeText(Chat.this, "Este cliente no tiene la aplicación instalada", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProductosSeleccionados()
    {
        Pedido p = new Pedido(showProductos, yoMismo, cliente, 0);
        Intent i = new Intent(Chat.this, VerificarPedido.class);
        i.putExtra("productos", showProductos);
        i.putExtra("pedido", p);
        startActivity(i);
    }


    private void cargarControles()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ( getIntent().getExtras() != null ) cliente = getIntent().getExtras().getParcelable("usuario");
        else cliente = new Cliente("Juancho de florencia", "123456789", "correo@dominiesito.com.mx");

        FirebaseManager firebaseManager = new FirebaseManager();

        firebaseManager.getTokenCliente(cliente);

        listaMensajes = findViewById(R.id.listaMensajes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Chat.this);

        listaMensajes.setLayoutManager(layoutManager);

        ivShowCatalogo = findViewById(R.id.showCatalogo);
        etMensaje = findViewById(R.id.etMensaje);
        etMensaje.clearFocus();
        ivSendMensaje = findViewById(R.id.sendMensaje);

        ivImagenCliente = findViewById(R.id.img);
        tvNombreCliente = findViewById(R.id.nombre);

        tvNombreCliente.setText(cliente.getNombre());
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        etMensaje = null;
        h = null;
    }

    public static void cargarMensaje(String m, int f, Context ctx)
    {
        Log.d("cargarMensaje()", "Entró");
        if ( mensajesAdapter == null )
        {
            Log.d("cargarMensaje()", "Primer mensaje");
            Mensaje[] nuevoMensaje = {new Mensaje( m, f )};
            mensajesAdapter = new MensajesAdapter( ctx, nuevoMensaje);
        }
        else
        {
            Mensaje[] chat = new Mensaje[mensajesAdapter.getItemCount()+1];
            Log.d("cargarMensaje()", "Agregando ["+chat.length+"] mensaje");
            for (int i = 0; i < chat.length; i++)
            {
                if ( i < chat.length-1 )
                {
                    chat[i] = mensajesAdapter.getMensajes()[i];
                }
                else if ( i == chat.length-1 )
                {
                    chat[i] = new Mensaje(m, f);
                }
            }
            mensajesAdapter = new MensajesAdapter(ctx, chat);
        }
        listaMensajes.setAdapter(mensajesAdapter);
    }

    public void estoyAqui()
    {
        Log.e("Estoy", "Aqui!!");
    }
}
