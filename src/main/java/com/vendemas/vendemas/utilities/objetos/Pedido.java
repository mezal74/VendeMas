package com.vendemas.vendemas.utilities.objetos;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import com.vendemas.vendemas.database.DatosPrueba;

import java.util.Calendar;

import static com.vendemas.vendemas.fragments.Pedidos.COMPRAS;
import static com.vendemas.vendemas.fragments.Pedidos.VENTAS;
import static java.util.Calendar.YEAR;

/**
 * Created by RicK' on 20/02/2018.
 */

public class Pedido implements Parcelable
{
    public static final short POR_VERIFICAR = 0x1, POR_PAGAR = 0x2, POR_ENTREGAR = 0x3, CERRADO = 0x4;
    private Producto[] productos;
    private Cliente comprador, vendedor;
    private boolean isCompra;
    private Calendar fecha = Calendar.getInstance();
    private short estado = POR_VERIFICAR;
    private double total = 0.0, abonado;

    private TextView tvNombre, tvCorreo, tvTelefono;
    private TextView tvFecha, tvEstado, tvTotalPagar, tvAbonado, tvRestante;
    private TextView tvListadoProductos;

    // Igualar pedido a uno existente para mostrar como historial, pendiente o dar seguimiento.
    public Pedido(Producto[] productos, Cliente comprador, Cliente vendedor, Calendar fecha, short estado, double abonado) {
        this.productos = productos;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.fecha = fecha;
        this.estado = estado;
        if (  comprador.getTelefono().equals(DatosPrueba.yoMismo.getTelefono()) )
            isCompra = COMPRAS;
        else
            isCompra = VENTAS;
        for (int i = 0; i < productos.length; i++)
        {
            total += Double.parseDouble(productos[i].getPrecio().replace("$", "").replace("MXN","")) * productos[i].getCantidad();
        }
        this.abonado = abonado;
    }

    // Crear un pedido nuevo para su proceso.
    public Pedido(Producto[] productos, Cliente comprador, Cliente vendedor, double abonado)
    {
        this.productos = productos;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.abonado = abonado;
        if (  comprador.getTelefono().equals(DatosPrueba.yoMismo.getTelefono()) )
            isCompra = COMPRAS;
        else
            isCompra = VENTAS;
        for (int i = 0; i < this.productos.length; i++)
        {
            total += Double.parseDouble(this.productos[i].getPrecio().replace("$", "").replace("MXN",""));
        }
    }

//------------------GETTERS & SETTERS---------------------------------------------------------------

    public Producto[] getProductos() {
        return productos;
    }

    public void setProductos(Producto[] productos) {
        this.productos = productos;
    }

    public String getProductosString()
    {
        String salida = "";
        for (int i = 0; i < productos.length; i++)
        {
            salida = salida+"[ "+productos[i].getCantidad()+" ] : {"+productos[i].getPrecio()+"} -> "+productos[i].getNombre()+"\n";
        }
        return salida;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public Cliente getVendedor() {
        return vendedor;
    }

    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAbonado() {
        return abonado;
    }

    public void setAbonado(double abonado) {
        this.abonado = abonado;
    }

//----------------------PARCELABLE-------------------------------------------------------------------

    protected Pedido(Parcel in) {
        comprador = in.readParcelable(Cliente.class.getClassLoader());
        vendedor = in.readParcelable(Cliente.class.getClassLoader());
        estado = (short) in.readInt();
        total = in.readDouble();
        abonado = in.readDouble();
    }

    public static final Creator<Pedido> CREATOR = new Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel in) {
            return new Pedido(in);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(comprador, i);
        parcel.writeParcelable(vendedor, i);
        parcel.writeInt((int) estado);
        parcel.writeDouble(total);
        parcel.writeDouble(abonado);
    }

    public TextView getTvNombre() {
        return tvNombre;
    }

    public void setTvNombre(TextView tvNombre)
    {
        this.tvNombre = tvNombre;
        if ( isCompra == COMPRAS ) this.tvNombre.setText(vendedor.getNombre());
        else this.tvNombre.setText(comprador.getNombre());
    }

    public TextView getTvCorreo() {
        return tvCorreo;
    }

    public void setTvCorreo(TextView tvCorreo) {
        this.tvCorreo = tvCorreo;
        if ( isCompra == COMPRAS) this.tvCorreo.setText(vendedor.getCorreo());
        else this.tvCorreo.setText(comprador.getCorreo());
    }

    public TextView getTvTelefono() {
        return tvTelefono;
    }

    public void setTvTelefono(TextView tvTelefono) {
        this.tvTelefono = tvTelefono;
        if ( isCompra == COMPRAS ) this.tvTelefono.setText(vendedor.getTelefono());
        else this.tvTelefono.setText(comprador.getTelefono());
    }

    public TextView getTvFecha() {
        return tvFecha;
    }

    public void setTvFecha(TextView tvFecha) {
        this.tvFecha = tvFecha;
        this.tvFecha.setText("["+fecha.get(Calendar.DAY_OF_MONTH)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(YEAR)+"]");
    }

    public TextView getTvEstado() {
        return tvEstado;
    }

    public void setTvEstado(TextView tvEstado) {
        this.tvEstado = tvEstado;
        switch ( estado )
        {
            case POR_VERIFICAR:
                tvEstado.setText("POR VERIFICAR");
                break;
            case POR_PAGAR:
                tvEstado.setText("POR PAGAR");
                break;
            case POR_ENTREGAR:
                tvEstado.setText("POR ENTREGAR");
                break;
            case CERRADO:
                tvEstado.setText("ENTREGADO");
                break;
        }
    }

    public double calcularTotal()
    {
        double total = 0;
        double abonado = 0;
        for (int i = 0; i < productos.length; i++)
        {
            total += productos[i].getTotal();
        }
        return total-abonado;
    }

    public TextView getTvTotalPagar() {
        return tvTotalPagar;
    }

    public void setTvTotalPagar(TextView tvTotalPagar)
    {
        this.tvTotalPagar = tvTotalPagar;
        this.tvTotalPagar.setText("$"+total);
    }

    public TextView getTvAbonado() {
        return tvAbonado;
    }

    public void setTvAbonado(TextView tvAbonado) {
        this.tvAbonado = tvAbonado;
        this.tvAbonado.setText("$"+abonado);
    }

    public TextView getTvRestante() {
        return tvRestante;
    }

    public void setTvRestante(TextView tvRestante) {
        this.tvRestante = tvRestante;
        this.tvRestante.setText("$"+String.valueOf(total-abonado));
    }

    public TextView getTvListadoProductos() {
        return tvListadoProductos;
    }

    public void setTvListadoProductos(TextView tvListadoProductos)
    {
        this.tvListadoProductos = tvListadoProductos;
        this.tvListadoProductos.setText(getProductosString());
    }

    public boolean getIsCompra()
    {
        return isCompra;
    }
}
