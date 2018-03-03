package com.vendemas.vendemas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vendemas.vendemas.R;
import com.vendemas.vendemas.database.DatosPrueba;
import com.vendemas.vendemas.database.FirebaseManager;
import com.vendemas.vendemas.database.SQLHelper;
import com.vendemas.vendemas.fragments.GetMyTel;
import com.vendemas.vendemas.fragments.Login;
import com.vendemas.vendemas.utilities.Comunes;
import com.vendemas.vendemas.utilities.mensajeria.mFirebaseInstanceIdService;
import com.vendemas.vendemas.utilities.objetos.Cliente;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.view.View.GONE;
import static com.vendemas.vendemas.database.DatosPrueba.yoMismo;
import static com.vendemas.vendemas.utilities.Comunes.SP_EMAIL;
import static com.vendemas.vendemas.utilities.Comunes.SP_NOMBRE;
import static com.vendemas.vendemas.utilities.Comunes.currentToken;

public class MainLogin extends AppCompatActivity
{
    private FragmentManager fm = getSupportFragmentManager();
    private Login fgmtLogin;
    private GetMyTel fgmtGetMyTel;

    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        sp = getSharedPreferences(Comunes.pakageName, MODE_PRIVATE);

        mFirebaseInstanceIdService mfis = new mFirebaseInstanceIdService();
        mfis.onTokenRefresh();

        mAuth = FirebaseAuth.getInstance();

        showHash();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        callbackManager = CallbackManager.Factory.create();

        final Handler handler = new Handler();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        validarLogin();
                    }
                });
            }
        }).start();
    }

    private void showHash()
    {
        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(Comunes.pakageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e("name not found", e.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("Resultado", "["+resultCode+"]");
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void login(FirebaseUser user)
    {
        fUser = user;
        //getCorreoFromFB();
        guardarCorreo("correo@prueba.ups");
        guardarNombre(fUser.getDisplayName());
        validarLogin();
    }

    public void validarLogin()
    {
        boolean loggedin = AccessToken.getCurrentAccessToken() != null;
        if ( loggedin )
        {
            yoMismo = new Cliente(sp.getString(SP_NOMBRE, ""), sp.getString(Comunes.SP_TELEFONO, ""), sp.getString(SP_EMAIL, ""));
            if ( yoMismo.getTelefono().equals( "" ) )
            {
                yoMismo.setTokenChat(currentToken);
                esconderSplash();
                showFgmtGetMyTel();
            }
            else
            {
                esconderSplash();
                startActivity(new Intent(MainLogin.this, MainActivity.class));
                finish();
            }
        }
        else
        {
            fgmtLogin = new Login();
            fgmtLogin.callbackManager = callbackManager;
            fgmtLogin.accessToken = accessToken;
            fm.beginTransaction().replace(R.id.contenedor_form_login, fgmtLogin).setTransition(Fade.IN).commit();
            esconderSplash();
        }
    }

    private void esconderSplash()
    {
        findViewById(R.id.progressB).setVisibility(GONE);
    }

    private void showFgmtGetMyTel()
    {
        fgmtGetMyTel = new GetMyTel();
        fm.beginTransaction().replace(R.id.contenedor_form_login, fgmtGetMyTel).setTransition(Fade.IN).commit();
    }

    public void handleFacebookAccessToken(AccessToken token)
    {
        Log.d("Equiteca", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("equiteca 2", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            login(user);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w("equiteca 3", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getCorreoFromFB()
    {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted (JSONObject object, GraphResponse response )
                    {
                        try
                        {
                            Log.e ("JSONFromFB", object.toString());
                            guardarCorreo(object.getString("email"));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        fUser = mAuth.getCurrentUser();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void guardarNombre(String nombre)
    {
        sp.edit().putString(SP_NOMBRE, nombre).apply();
        if ( yoMismo != null ) yoMismo.setNombre(nombre);
        else
        {
            yoMismo = new Cliente();
            yoMismo.setNombre(nombre);
        }
    }

    public void guardarTelefono(String myTel)
    {
        sp.edit().putString(Comunes.SP_TELEFONO, myTel).apply();
        if ( yoMismo != null ) yoMismo.setTelefono(myTel);
        else
        {
            yoMismo = new Cliente();
            yoMismo.setTelefono(myTel);
        }
        FirebaseManager fbManager = new FirebaseManager();
        if ( fbManager.darmeDeAlta(yoMismo) )
        {
            Toast.makeText(this, "Seregistró correctamente", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Error al guardar en firebase", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarCorreo(String email)
    {
        sp.edit().putString(SP_EMAIL, email).apply();
        if ( yoMismo != null ) yoMismo.setCorreo(email);
        else
        {
            yoMismo = new Cliente();
            yoMismo.setCorreo(email);
        }
    }
}