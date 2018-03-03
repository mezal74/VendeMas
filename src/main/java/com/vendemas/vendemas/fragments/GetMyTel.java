package com.vendemas.vendemas.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.dialogos.EnterVerificationCode;


/**
 * Created by RicK' on 28/02/2018.
 */

public class GetMyTel extends Fragment
{
    public static String myTel;
    private EditText etMyTel;
    private TextView tvTerminos;
    private Button btnVerificar;

    public GetMyTel() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fgmt_get_my_tel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        cargarControles();
        setUpListeners();
    }

    private void setUpListeners()
    {
        btnVerificar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if ( etMyTel.getText().toString().length() < 5 )
                {
                    Toast.makeText(getActivity(), "El teléfono no es válido", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    enviarSMS();
                }
            }
        });

        tvTerminos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Estas son los terminos y condiciones de uso :D")
                        .setTitle("Importante")
                        .setNeutralButton("Cerrar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void enviarSMS()
    {
        myTel = etMyTel.getText().toString();
        //Toast.makeText(getActivity(), "Enviando mensaje de confirmacion", Toast.LENGTH_SHORT).show();
        new EnterVerificationCode(getActivity()).show();
    }

    private void cargarControles()
    {
        if ( getActivity() != null )
        {
            tvTerminos = getActivity().findViewById(R.id.tvTerminosCondiciones);
            etMyTel = getActivity().findViewById(R.id.etTelefono);
            btnVerificar = getActivity().findViewById(R.id.btnAceptar);
        }
        else
        {
            Log.e("Activity", "Es null D: en GetMyTel.cargarControles()");
        }
    }
}
