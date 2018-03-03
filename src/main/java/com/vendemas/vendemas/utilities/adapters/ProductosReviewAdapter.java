package com.vendemas.vendemas.utilities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.activities.VerificarPedido;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Producto;

/**
 * Created by RicK' on 21/02/2018.
 */

public class ProductosReviewAdapter extends RecyclerView.Adapter
{
    private Context ctx;
    private Cliente cliente;
    private Producto[] productos;
    private boolean misProductos;
    private TextView tvTotal;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public ImageView img;
        public TextView nombre, precio, descripcion, subTotal;
        public EditText cantidad;
        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            nombre = layout.findViewById(R.id.nombre);
            precio = layout.findViewById(R.id.precio);
            descripcion = layout.findViewById(R.id.descripcion);
            img = layout.findViewById(R.id.img);
            cantidad = layout.findViewById(R.id.cantidad);
            subTotal = layout.findViewById(R.id.subTotal);

        }
    }

    public ProductosReviewAdapter( Context ctx, Cliente cliente, Producto[] productos )
    {
        this.ctx = ctx;
        this.cliente = cliente;
        this.productos = productos;
        misProductos = cliente.getTelefono().equals(DatosPrueba.yoMismo.getTelefono());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.row_review_producto, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i)
    {
        final ViewHolder root = (ViewHolder) holder;

        productos[i].setTvNombre( root.nombre );
        productos[i].setTvPrecio( root.precio );
        productos[i].setTvDesc( root.descripcion );
        productos[i].setIvImagen( root.img );
        productos[i].setEtCantidad(root.cantidad);
        productos[i].setTvTotal(root.subTotal);

        productos[i].getEtCantidad().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        productos[i].getEtCantidad().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                int cant;
                if (editable.length() > 0) cant = Integer.parseInt(editable.toString());
                else cant = 0;
                if ( cant < 0 )
                    productos[root.getAdapterPosition()].getEtCantidad().setText("0");
                productos[root.getAdapterPosition()].setCantidad(cant);
                productos[root.getAdapterPosition()].getTvTotal().setText("$" + String.valueOf(cant*Double.parseDouble(productos[root.getAdapterPosition()].getPrecio().replace("$", "").replace("MXN",""))));
                ((VerificarPedido) ctx).updateTotales();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return productos.length;
    }
}