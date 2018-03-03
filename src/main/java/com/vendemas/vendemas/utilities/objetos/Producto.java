package com.vendemas.vendemas.utilities.objetos;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Producto implements Parcelable
{
    private int icon, cantidad;
    private String nombre, descripcion, precio;
    private TextView tvNombre, tvDesc, tvPrecio, tvCantidad;
    private EditText etCantidad;
    private ImageView ivImagen;
    private CheckBox chkAddToCart;
    private View contenedorCantidad;
    private boolean selectItems, seleccionado;
    private TextView tvTotal;
    private double total;

    public Producto(int icon, int cantidad, String nombre, String descripcion, String precio, TextView tvNombre, TextView tvDesc, TextView tvPrecio, TextView tvCantidad, ImageView ivImagen, CheckBox chkAddToCart)
    {
        this.icon = icon;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tvNombre = tvNombre;
        this.tvDesc = tvDesc;
        this.tvPrecio = tvPrecio;
        this.tvCantidad = tvCantidad;
        this.ivImagen = ivImagen;
        this.chkAddToCart = chkAddToCart;
    }

    public Producto(int icon, int cantidad, String nombre, String descripcion, String precio)
    {
        this.icon = icon;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    protected Producto(Parcel in) {
        icon = in.readInt();
        cantidad = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readString();
        selectItems = in.readByte() != 0;
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    @Override
    public String toString()
    {
        return nombre;
    }

    public void setProductoMio(boolean b)
    {
        if ( !b )
        {
            esteProductoNoEsMio();
        }
        else
        {
            esteProductoMePertenece();
        }
    }

    private void esteProductoMePertenece()
    {
        if ( !selectItems ) chkAddToCart.setVisibility(GONE);
        else chkAddToCart.setVisibility(VISIBLE);
        etCantidad.setVisibility(GONE);
    }

    private void esteProductoNoEsMio()
    {
        contenedorCantidad.setVisibility(GONE);
        chkAddToCart.setVisibility(VISIBLE);
        chkAddToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                seleccionado = b;
            }
        });
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public TextView getTvNombre() {
        return tvNombre;
    }

    public void setTvNombre(TextView tvNombre) {
        this.tvNombre = tvNombre;
        this.tvNombre.setText(nombre);
    }

    public TextView getTvDesc()
    {
        return tvDesc;
    }

    public void setTvDesc(TextView tvDesc) {
        this.tvDesc = tvDesc;
        this.tvDesc.setText(descripcion);
    }

    public TextView getTvPrecio() {
        return tvPrecio;
    }

    public void setTvPrecio(TextView tvPrecio) {
        this.tvPrecio = tvPrecio;
        this.tvPrecio.setText(precio);
    }

    public TextView getTvCantidad()
    {
        return tvCantidad;
    }

    public void setTvCantidad(TextView tvCantidad)
    {
        this.tvCantidad = tvCantidad;
        this.tvCantidad.setText("Existencias: "+cantidad);
    }

    public ImageView getIvImagen() {
        return ivImagen;
    }

    public void setIvImagen(ImageView ivImagen) {
        this.ivImagen = ivImagen;
        this.ivImagen.setImageResource(icon);
    }

    public CheckBox getChkAddToCart() {
        return chkAddToCart;
    }

    public void setChkAddToCart(CheckBox chkAddToCart)
    {
        this.chkAddToCart = chkAddToCart;
    }

    public View getContenedorCantidad() {
        return contenedorCantidad;
    }

    public void setContenedorCantidad(View contenedorCantidad) {
        this.contenedorCantidad = contenedorCantidad;
    }

    public EditText getEtCantidad() {
        return etCantidad;
    }

    public void setEtCantidad(EditText etCantidad) {
        this.etCantidad = etCantidad;
    }

    public void setSelectItems(boolean selectItems) {
        this.selectItems = selectItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeInt(cantidad);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(precio);
        parcel.writeByte((byte) (selectItems ? 1 : 0));
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setTvTotal(TextView tvTotal) {
        this.tvTotal = tvTotal;
        this.tvTotal.setText(getPrecio());
    }

    public TextView getTvTotal() {
        return tvTotal;
    }

    public double getTotal()
    {
        return cantidad*Double.parseDouble(getPrecio().replace("$", "").replace("MXN",""));
    }
}
