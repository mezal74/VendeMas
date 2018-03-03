package com.vendemas.vendemas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RicK' on 23/02/2018.
 */

public class SQLHelper extends SQLiteOpenHelper
{
    /*  Campos  */
    public static final String USUARIO = "Usuario";
    public static final String NOMBRE = "nombre";
    public static final String TELEFONO = "telefono";
    public static final String CORREO = "correo";
    public static final String DIRECCION = "direccion";


    /*  Tablas  */
    public static final String DELETE_USUARIOS = "DROP TABLE IF EXIST " + USUARIO;
    public static final String DB_NAME = "dbVendeMas";


    /*  Otros  */
    public static final int VERSION = 0x1;


    /*  Acciones  */
    private final String CREAR_TABLA_USUARIOS =
        "CREATE TABLE "+USUARIO+" ( " +
            NOMBRE + " Text, " +
            TELEFONO + " carchar(50), " +
            CORREO + " Text, " +
            DIRECCION + " Text, " +
            "PRIMARY KEY (telefono)" +
        ")";

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREAR_TABLA_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int vAntigua, int vNueva)
    {
        db.execSQL(DELETE_USUARIOS);
        onCreate(db);
    }
}
