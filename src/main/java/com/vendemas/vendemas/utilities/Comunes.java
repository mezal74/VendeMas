package com.vendemas.vendemas.utilities;

import com.vendemas.vendemas.utilities.mensajeria.remoto.APIService;
import com.vendemas.vendemas.utilities.mensajeria.remoto.RetrofitClient;

/**
 * Created by RicK' on 27/02/2018.
 */

public class Comunes {
    public static final String SP_TELEFONO = "telefono";
    public static final String SP_NOMBRE = "nombre";
    public static final String SP_EMAIL = "email";
    public static final String DB_USERS_TAG = "usuarios";
    public static final String FIELD_PROVEEDORES = "proveedores";
    public static final String FIELD_TOKEN_CHAT = "tokenChat";
    public static String currentToken = "";

    private static String baseURL = "https://fcm.googleapis.com/";
    public static String pakageName = "com.vendemas.vendemas";
    public static String TAG_agregandoafb = "Agregando Firebas";

    public static APIService getFCMClient()
    {
        return RetrofitClient.getClient(baseURL).create(APIService.class);
    }
}
