package com.vendemas.vendemas.dialogos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vendemas.vendemas.R;
import com.vendemas.vendemas.database.FirebaseManager;
import com.vendemas.vendemas.database.SQLHelper;
import com.vendemas.vendemas.utilities.Comunes;
import com.vendemas.vendemas.utilities.objetos.Cliente;

import static com.vendemas.vendemas.database.DatosPrueba.yoMismo;
import static com.vendemas.vendemas.database.SQLHelper.CORREO;
import static com.vendemas.vendemas.database.SQLHelper.DIRECCION;
import static com.vendemas.vendemas.database.SQLHelper.NOMBRE;
import static com.vendemas.vendemas.database.SQLHelper.TELEFONO;
import static com.vendemas.vendemas.database.SQLHelper.USUARIO;

/**
 * Created by RicK' on 22/02/2018.
 */

public class AddContacto extends Dialog
{
    public Cliente cliente;
    private EditText etNombre, etTelefono, etCorreo, etDirección;
    private Button btnAgregar;

    private DatabaseReference dBRef;

    public AddContacto(@NonNull Context context, Cliente cliente)
    {
        super(context);
        this.cliente = cliente;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_cliente);

        dBRef = FirebaseDatabase.getInstance().getReference("clientes");

        btnAgregar = findViewById(R.id.btnAgregar);
        etNombre = findViewById(R.id.et_nombre);
        etTelefono = findViewById(R.id.et_telefono);
        etCorreo = findViewById(R.id.et_correo);
        etDirección = findViewById(R.id.direccion);

        etNombre.setText(cliente.getNombre());
        etTelefono.setText(cliente.getTelefono());

        btnAgregar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                agregarCliente();
            }
        });

    }

    private void agregarCliente()
    {
        if ( !
                (
                        etNombre.getText().toString().isEmpty() ||
                        etTelefono.getText().toString().isEmpty() ||
                        etCorreo.getText().toString().isEmpty() ||
                        etDirección.getText().toString().isEmpty()
                )

            )
        {
            cliente = new Cliente(etNombre.getText().toString(),
                    etTelefono.getText().toString().replace(" ", "").replace("-", "").replace("(","").replace(")", ""),
                    etCorreo.getText().toString(),
                    etDirección.getText().toString());
            /* Agregar a FireBase TODO Modificar estructura a "Invitaciones" */
            agregarClienteFirebase(cliente);
            agregarClienteDB(cliente);
        }
    }

    private void agregarClienteFirebase(Cliente cliente)
    {
        Log.e(Comunes.TAG_agregandoafb, "agregarClienteFirebase()");
        FirebaseManager fbManager = new FirebaseManager();
        fbManager.addProveedorACliente(yoMismo, cliente);
    }

    private void agregarClienteDB(Cliente cliente)
    {
        SQLHelper dbHelper = new SQLHelper(getContext(), SQLHelper.DB_NAME, null, SQLHelper.VERSION);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "INSERT INTO "+USUARIO+" ("+NOMBRE+", "+TELEFONO+", "+CORREO+", "+DIRECCION+") VALUES ('"+cliente.getNombre()+"', '"+cliente.getTelefono()+"', '"+cliente.getCorreo()+"', '"+cliente.getDireccion()+"')";

        try
        {
            db.execSQL(query);
        }
        catch (SQLiteConstraintException e)
        {
            Toast.makeText(getContext(), "Este teléfono ya está registrado", Toast.LENGTH_SHORT).show();
        }

        db.close();
        dismiss();
    }
}
