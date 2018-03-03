package com.vendemas.vendemas.dialogos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.vendemas.vendemas.R;
import com.vendemas.vendemas.activities.MainLogin;
import com.vendemas.vendemas.fragments.GetMyTel;

/**
 * Created by RicK' on 28/02/2018.
 */

public class EnterVerificationCode extends Dialog
{
    private EditText etCodigo;
    private ProgressDialog pd;
    private final Handler handler = new Handler();
    private static final int MAX_LENGTH = 6;
    private Context ctx;

    public EnterVerificationCode(@NonNull Context context)
    {
        super(context);
        ctx = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_codigo_verificacion);

        cargarControles ((EditText) findViewById(R.id.etCodigo));

        setUpListeners();
    }

    private void setUpListeners()
    {
        etCodigo.addTextChangedListener(new TextWatcher()
        {
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
                if ( editable.length() == MAX_LENGTH )
                {
                    validarCodigo();
                }
            }
        });
    }

    private void validarCodigo()
    {
        pd = new ProgressDialog(ctx);
        pd.setMessage("Validando...");
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2*1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( pd.isShowing() ) pd.dismiss();
                        //Toast.makeText(ctx, "Código es válido", Toast.LENGTH_SHORT).show();
                        ((MainLogin)ctx).guardarTelefono(GetMyTel.myTel);
                        ((MainLogin)ctx).validarLogin();
                    }
                });
            }
        }).start();
    }

    private void cargarControles(EditText codigo)
    {
        etCodigo = codigo;
    }
}
