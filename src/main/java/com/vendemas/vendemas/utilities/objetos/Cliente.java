package com.vendemas.vendemas.utilities.objetos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Cliente implements Parcelable
{
    private String nombre, correo, direccion, telefono, tokenChat = "", proveedores = "";

    public Cliente(String nombre, String telefono, String correo)
    {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    protected Cliente(Parcel in) {
        nombre = in.readString();
        telefono = in.readString();
        correo = in.readString();
        direccion = in.readString();
        tokenChat = in.readString();
        proveedores = in.readString();
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>()
    {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

    public Cliente(String nombre, String telefono)
    {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Cliente(String nombre, String telefono, String correo, String direccion)
    {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Cliente() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(nombre);
        parcel.writeString(telefono);
        parcel.writeString(correo);
        parcel.writeString(direccion);
        parcel.writeString(tokenChat);
        parcel.writeString(proveedores);
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTokenChat() {
        return tokenChat;
    }

    public void setTokenChat(String tokenChat) {
        this.tokenChat = tokenChat;
    }

    public String getProveedores() {
        return proveedores;
    }

    public void setProveedores(String proveedores) {
        this.proveedores = proveedores;
    }
}
