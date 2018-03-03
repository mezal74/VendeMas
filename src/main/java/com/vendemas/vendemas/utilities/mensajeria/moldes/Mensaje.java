package com.vendemas.vendemas.utilities.mensajeria.moldes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by RicK' on 02/03/2018.
 */

public class Mensaje
{
    private String mensaje;
    private int from = 0
            /* 0 = mensajeEnviado;
               1 = mensajeRecibido
            */;
    private TextView tvMensaje;

    public Mensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public Mensaje(@NonNull String mensaje, int from)
    {
        this.mensaje = mensaje;
        this.from = from;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public int getFrom()
    {
        return from;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public TextView getTvMensaje()
    {
        return tvMensaje;
    }

    public void setTvMensaje(TextView tvMensaje)
    {
        this.tvMensaje = tvMensaje;
        this.tvMensaje.setText(mensaje);
    }
}
