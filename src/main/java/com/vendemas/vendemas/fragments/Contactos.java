package com.vendemas.vendemas.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.activities.MainActivity;
import com.vendemas.vendemas.database.SQLHelper;
import com.vendemas.vendemas.utilities.adapters.ContactosAdapter;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Cliente;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.vendemas.vendemas.database.SQLHelper.CORREO;
import static com.vendemas.vendemas.database.SQLHelper.DIRECCION;
import static com.vendemas.vendemas.database.SQLHelper.NOMBRE;
import static com.vendemas.vendemas.database.SQLHelper.TELEFONO;
import static com.vendemas.vendemas.database.SQLHelper.USUARIO;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Contactos extends Fragment {

    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    private static final boolean CLIENTES = false;
    private static final boolean PROVEEDORES = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_clientes:
                    mostrarClientes();
                    return true;
                case R.id.nav_proveedores:
                    mostrarProveedores();
                    return true;
                case R.id.nav_contactos:
                    checkPermisos();
                    return true;
            }
            return false;
        }
    };

    public BottomNavigationView navigation;
    private boolean tipoContactos = CLIENTES;
    private ListView listaContactos;
    private Cliente[] contactosDeTelefono;
    private Cliente[] clientes;

    public Contactos() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fgmt_contactos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigation = getActivity().findViewById(R.id.navegador);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listaContactos = getActivity().findViewById(R.id.listaContactos);

        mostrarClientes();
    }

    private void mostrarProveedores() {
        tipoContactos = PROVEEDORES;
    }

    public void mostrarClientes() {
        tipoContactos = CLIENTES;
        cargarClientes();
    }

    private void cargarClientes()
    {
        cargarClientesDB();
        if ( clientes != null ) listaContactos.setAdapter(new ContactosAdapter(clientes, getActivity()));
        else
            Toast.makeText(getActivity(), "No hay clientes registrados aún", Toast.LENGTH_SHORT).show();
    }

    private void cargarClientesDB()
    {
        SQLHelper dbHelper = new SQLHelper(getContext(), SQLHelper.DB_NAME, null, SQLHelper.VERSION);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] campos =
                {
                        NOMBRE,
                        TELEFONO,
                        CORREO,
                        DIRECCION
                };

        Cursor cursor = null;
        try
        {
            //cursor = db.rawQuery("SELECT * FROM "+USUARIO, null );
            cursor = db.query( USUARIO, campos, null, null, null, null, NOMBRE);
            if ( cursor.moveToFirst() )
            {
                clientes = new Cliente[cursor.getCount()];
                int i = 0;
                do
                {
                    clientes[i] = new Cliente(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    i++;
                } while ( cursor.moveToNext() );
            }

        }
        catch (SQLiteConstraintException e)
        {
            Toast.makeText(getActivity(), "Error al cargar clientes", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if ( cursor != null ) cursor.close();
        db.close();
    }

    private void cargarContactosFromPhone()
    {
        if ( contactosDeTelefono != null )
        {
            listaContactos.setAdapter(new ContactosAdapter(contactosDeTelefono, getActivity()));
            if ( getActivity() != null ) ((MainActivity) getActivity()).disposeDialogo();
            return;
        }
        final Handler h = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                if (cursor.moveToFirst()) {
                    contactosDeTelefono = new Cliente[cursor.getCount()];
                    int i = 0;
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                contactosDeTelefono[i] = new Cliente(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                                i++;
                                break;
                            }
                            pCur.close();
                        }

                    } while (cursor.moveToNext());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            listaContactos.setAdapter(new ContactosAdapter(contactosDeTelefono, getActivity()));
                        }
                    });
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).disposeDialogo();
                    }
                });
            }
        }).start();
    }

    public void checkPermisos()
    {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);

            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            }
        }
        else
        {
            if (getActivity() != null) ((MainActivity) getActivity()).mostrarDialogoNormal();
            cargarContactosFromPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if ( requestCode == 2 )
        {
            if ( grantResults[0] == PERMISSION_GRANTED )
            {
                checkPermisos();
            }
            else
            {
                mostrarClientes();
                navigation.setSelectedItemId(R.id.nav_clientes);
            }
        }
    }
}
