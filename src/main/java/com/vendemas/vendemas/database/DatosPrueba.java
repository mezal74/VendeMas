package com.vendemas.vendemas.database;

import com.vendemas.vendemas.utilities.objetos.Cliente;
import com.vendemas.vendemas.utilities.objetos.Pedido;
import com.vendemas.vendemas.utilities.objetos.Producto;

import java.util.Calendar;

/**
 * Created by RicK' on 20/02/2018.
 */

public class DatosPrueba
{

    public static Cliente[] clientes = new Cliente[]
            {
                    new Cliente("Juancho de la rosa", "3322116655", "juancho@dominio.com"),
                    new Cliente("Jaime Pérez Zánches", "1122334455", "jaime@dominio.com"),
                    new Cliente("Miranda Alvarado Robles", "123456789", "miranda@dominio.com"),
                    new Cliente("Paul Robinson", "987654321", "paul@dominio.com")
            };

    public static Cliente yoMismo;

    public static final Producto[] productos = new Producto[]
            {
                    new Producto(android.R.drawable.ic_btn_speak_now, 1, "Microfono", "Microfono para grabar voces", "$100.00 MXN"),
                    new Producto(android.R.drawable.ic_lock_idle_alarm, 50, "Reloj despertador", "Programa hasta 5 alarmas separadas", "$30.00 MXN"),
                    new Producto(android.R.drawable.ic_input_get, 20, "Libro", "Lo más interesante est´escrito en estas páginas", "$70.83 MXN"),
                    new Producto(android.R.drawable.ic_lock_idle_lock, 20, "Candado", "Candado para asegurar cosas", "$20.00 MXN")
            };

    /*public static final Pedido[] pedidos = new Pedido[]
            {
                    // VENTAS
                    new Pedido(
                            new Producto[] { productos[0], productos[2], productos[3] }
                            , clientes[0] , yoMismo , Calendar.getInstance()
                            , Pedido.POR_VERIFICAR , 0
                    ),
                    new Pedido(
                            new Producto[] { productos[0] }
                            , clientes[1] , yoMismo , Calendar.getInstance()
                            , Pedido.POR_PAGAR , 40
                    ),
                    new Pedido(
                            new Producto[] { productos[2], productos[3] }
                            , clientes[2] , yoMismo , Calendar.getInstance()
                            , Pedido.POR_ENTREGAR , 0
                    ),
                    // COMPRAS
                    new Pedido(
                            new Producto[] { productos[0], productos[2], productos[3] }
                            , yoMismo , clientes[1] , Calendar.getInstance()
                            , Pedido.POR_VERIFICAR , 0
                    ),
                    new Pedido(
                            new Producto[] { productos[0] }
                            , yoMismo , clientes[2] , Calendar.getInstance()
                            , Pedido.POR_PAGAR , 40
                    ),
                    new Pedido(
                            new Producto[] { productos[2], productos[3] }
                            , yoMismo , clientes[3] , Calendar.getInstance()
                            , Pedido.POR_ENTREGAR , 0
                    ),
            };*/

}
