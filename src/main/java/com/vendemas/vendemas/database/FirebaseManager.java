package com.vendemas.vendemas.database;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vendemas.vendemas.activities.Chat;
import com.vendemas.vendemas.utilities.Comunes;
import com.vendemas.vendemas.utilities.objetos.Cliente;

import static com.vendemas.vendemas.database.DatosPrueba.yoMismo;
import static com.vendemas.vendemas.utilities.Comunes.DB_USERS_TAG;
import static com.vendemas.vendemas.utilities.Comunes.FIELD_PROVEEDORES;
import static com.vendemas.vendemas.utilities.Comunes.TAG_agregandoafb;

/**
 * Created by RicK' on 28/02/2018.
 */

public class FirebaseManager
{
    /**
     * Estructura de relación clientes
     *
     *
     *  (Teléfono)
     *      -Nombre Show
     *      -TokenChat
     *      -Lista<Vendedores>
     *      -Lista<Productos>
     *
     * */
    private static final String TAG = "FireBManager";
    private DatabaseReference mDatabase;
    private ValueEventListener postListener;
    private Query mPostReference;
    private Query getClienTokenRef;

    private ValueEventListener sendInvToCatalogoListener, getClienTokenListener;


    public FirebaseManager()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public ValueEventListener getPostListener() {
        return postListener;
    }

    public void setPostListener(ValueEventListener postListener)
    {
        this.postListener = postListener;
        mPostReference.addValueEventListener(postListener);
    }

    public Query getmPostReference() {
        return mPostReference;
    }

    public void setmPostReference(Query mPostReference) {
        this.mPostReference = mPostReference;
    }

    public boolean darmeDeAlta( Cliente yop )
    {
        Log.d(TAG, "Guardando: [(myTel:"+yop.getTelefono()+"), (nombreShow:"+yop.getNombre()+"), (tokenChat:"+yop.getTokenChat()+")]");
        try
        {
            mDatabase.child(DB_USERS_TAG).child(yop.getTelefono()).setValue(yop);
            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error al guardar");
            e.printStackTrace();
        }
        Log.e(TAG,"Guardado con éxito");
        return false;
    }

    public void addProveedorACliente( final Cliente me, final Cliente cliente )
    {
        mPostReference = mDatabase.child(DB_USERS_TAG).child(cliente.getTelefono());

        Log.e(Comunes.TAG_agregandoafb, "addProveedorACliente("+me.getTelefono()+")");

        Log.e(TAG_agregandoafb, mPostReference.toString());

        sendInvToCatalogoListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.e(Comunes.TAG_agregandoafb, "onDataChange()");

                Cliente clienteLeido = dataSnapshot.getValue(Cliente.class);

                if ( clienteLeido == null )
                {
                    Log.e("Error", "Cliente es nullo");
                    return;
                }

                String proveedores = clienteLeido.getProveedores();

                Log.d("SnapShot", dataSnapshot.child(FIELD_PROVEEDORES).getValue(String.class)+".");

                if ( proveedores != null && proveedores.equals("") )
                {
                    proveedores = proveedores+","+cliente.getTelefono();
                    Log.d("Proveedores leidos", proveedores);
                }
                else
                {
                    proveedores = cliente.getTelefono();
                    Log.d("Proveedores creado", proveedores);
                }
                clienteLeido.setProveedores(clienteLeido.getProveedores()+proveedores);
                mPostReference.getRef().setValue(clienteLeido);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        };

        mPostReference.addListenerForSingleValueEvent(sendInvToCatalogoListener);
    }

    public void actualizarToken(String refreshedToken)
    {
        if ( yoMismo != null ) mDatabase.child(DB_USERS_TAG).child(yoMismo.getTelefono()).child(Comunes.FIELD_TOKEN_CHAT).setValue(refreshedToken);
    }

    public void getTokenCliente(final Cliente cliente)
    {

        if ( getClienTokenRef != null ) getClienTokenRef.removeEventListener(getClienTokenListener);

        getClienTokenRef = mDatabase.child(DB_USERS_TAG).child(cliente.getTelefono());

        Log.e("Referencia Token", getClienTokenRef.toString());

        getClienTokenListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.e("Token leido", "onDataChange("+cliente.getTelefono()+")");

                Cliente clienteLeido = dataSnapshot.getValue(Cliente.class);

                Chat.tokenDestino = clienteLeido.getTokenChat();

                Log.e("Token leido", cliente.getTokenChat());

            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e("Token leido", "Cargando token de cliente");
            }
        };

        getClienTokenRef.addValueEventListener(getClienTokenListener);
    }
}
