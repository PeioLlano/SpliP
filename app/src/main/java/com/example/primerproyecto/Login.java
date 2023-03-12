package com.example.primerproyecto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.primerproyecto.BBDD.BBDD;
import com.example.primerproyecto.Dialogs.EstiloDialog;
import com.example.primerproyecto.Dialogs.IdiomaDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Login extends AppCompatActivity implements IdiomaDialog.ListenerdelDialogoIdioma, EstiloDialog.ListenerdelDialogoEstilo{

    SQLiteDatabase bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cargarPreferencias();
        cargarLogeado();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BBDD gestorBBDD = new BBDD(this, "SpliP", null, 1);
        bbdd = gestorBBDD.getWritableDatabase();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)!=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
        }

        EditText eUsername = (EditText) findViewById(R.id.eUsername);
        EditText ePassword = (EditText) findViewById(R.id.ePassword);

        Button bSingIn = (Button) findViewById(R.id.bSignIn);

        int tiempoToast= Toast.LENGTH_SHORT;
        Toast avisoInicioIncorrecto = Toast.makeText(this, "Credenciales incorrectas, intentelo de nuevo.", tiempoToast);

        bSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor c = bbdd.rawQuery("SELECT Usuario, Contraseña FROM Usuarios WHERE Usuario = ? AND Contraseña = ?", new String[]{eUsername.getText().toString(), ePassword.getText().toString()});

                if (c.getCount() != 0) {
                    Intent intent = new Intent(Login.this, ListGrupos.class);

                    c.moveToFirst();
                    intent.putExtra("usuario", c.getString(0));

                    guardarPreferenciaLogin(eUsername.getText().toString());

                    startActivity(intent);
                    Login.this.finish();
                }
                else {
                    avisoInicioIncorrecto.show();
                }
            }
        });

        FloatingActionButton bLanguage = (FloatingActionButton) findViewById(R.id.bLanguage);
        bLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdiomaDialog dialog = new IdiomaDialog();
                dialog.show(getSupportFragmentManager(), "DialogoIdioma");
            }
        });

        FloatingActionButton bStyle = (FloatingActionButton) findViewById(R.id.bStyle);
        bStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EstiloDialog dialog = new EstiloDialog();
                dialog.show(getSupportFragmentManager(), "DialogoEstilo");
            }
        });

        TextView tPosibleLanguages = (TextView) findViewById(R.id.tPosibleLanguages);
        TextView tChangeLanguages = (TextView) findViewById(R.id.tChangeLanguages);

        TextView tPosibleStyles = (TextView) findViewById(R.id.tPosibleStyles);
        TextView tChangeStyle = (TextView) findViewById(R.id.tChangeStyle);

        final Boolean[] clicadoAjustes = {false};

        FloatingActionButton bSettings = (FloatingActionButton) findViewById(R.id.bMail);
        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicadoAjustes[0]) {
                    bLanguage.setVisibility(View.VISIBLE);
                    bLanguage.setClickable(true);

                    bStyle.setVisibility(View.VISIBLE);
                    bStyle.setClickable(true);

                    tChangeLanguages.setVisibility(View.VISIBLE);
                    tPosibleLanguages.setVisibility(View.VISIBLE);

                    tChangeStyle.setVisibility(View.VISIBLE);
                    tPosibleStyles.setVisibility(View.VISIBLE);
                }
                else {
                    bLanguage.setVisibility(View.INVISIBLE);
                    bLanguage.setClickable(false);

                    bStyle.setVisibility(View.INVISIBLE);
                    bStyle.setClickable(false);

                    tChangeLanguages.setVisibility(View.INVISIBLE);
                    tPosibleLanguages.setVisibility(View.INVISIBLE);

                    tChangeStyle.setVisibility(View.INVISIBLE);
                    tPosibleStyles.setVisibility(View.INVISIBLE);
                }
                clicadoAjustes[0] = !clicadoAjustes[0];
            }
        });

        Button bSignUp = (Button) findViewById(R.id.bSignUp);
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_registro = new Intent(Login.this, SignUp.class);
                startActivity(intent_registro);
            }
        });
    }

    @Override
    public void alElegirIdioma(int i) {
        int tiempoToast= Toast.LENGTH_SHORT;
        CharSequence[] opciones = {"Inglés", "Español", "Euskera"};
        Toast avisoIdiomaCambiado = Toast.makeText(this, "Idioma cambiado a: " + opciones[i], tiempoToast);

        guardarPreferenciaIdioma((String) opciones[i]);

        switch (i){
            case 0:
                Locale nuevaloc = new Locale("en");
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
                break;
            case 1:
                Locale nuevalocEs = new Locale("es");
                Locale.setDefault(nuevalocEs);
                Configuration configurationEs = getBaseContext().getResources().getConfiguration();
                configurationEs.setLocale(nuevalocEs);
                configurationEs.setLayoutDirection(nuevalocEs);

                Context contextEs = getBaseContext().createConfigurationContext(configurationEs);
                getBaseContext().getResources().updateConfiguration(configurationEs, contextEs.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
                break;
            case 2:
                Locale nuevalocEu = new Locale("eu");
                Locale.setDefault(nuevalocEu);
                Configuration configurationEu = getBaseContext().getResources().getConfiguration();
                configurationEu.setLocale(nuevalocEu);
                configurationEu.setLayoutDirection(nuevalocEu);

                Context contextEu = getBaseContext().createConfigurationContext(configurationEu);
                getBaseContext().getResources().updateConfiguration(configurationEu, contextEu.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
                break;
        }
        avisoIdiomaCambiado.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.really_exit)
                .setMessage(R.string.really_exit_long)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Login.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public void alElegirEstilo(int i) {
        int tiempoToast= Toast.LENGTH_SHORT;
        CharSequence[] opciones = {"Dark", "Normal"};
        Toast avisoEstiloCambiado = Toast.makeText(this, "Estilo cambiado a: " + opciones[i], tiempoToast);
        avisoEstiloCambiado.show();

        guardarPreferenciaEstilo((String) opciones[i]);

        switch (i) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    public void cargarLogeado() {
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String loged_user = preferences.getString("loged_user", "");

        if(loged_user != "") {
            Intent intent = new Intent(Login.this, ListGrupos.class);

            intent.putExtra("usuario", loged_user);

            startActivity(intent);
            this.finish();
        }
    }

    public void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String estilo = preferences.getString("tema", "");

        switch (estilo) {
            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Normal":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

        String idioma = preferences.getString("idioma", "");

        switch (idioma){
            case "Inglés":
                Locale nuevaloc = new Locale("en");
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
                break;
            case "Español":
                Locale nuevalocEs = new Locale("es");
                Locale.setDefault(nuevalocEs);
                Configuration configurationEs = getBaseContext().getResources().getConfiguration();
                configurationEs.setLocale(nuevalocEs);
                configurationEs.setLayoutDirection(nuevalocEs);

                Context contextEs = getBaseContext().createConfigurationContext(configurationEs);
                getBaseContext().getResources().updateConfiguration(configurationEs, contextEs.getResources().getDisplayMetrics());
                break;
            case "Euskera":
                Locale nuevalocEu = new Locale("eu");
                Locale.setDefault(nuevalocEu);
                Configuration configurationEu = getBaseContext().getResources().getConfiguration();
                configurationEu.setLocale(nuevalocEu);
                configurationEu.setLayoutDirection(nuevalocEu);

                Context contextEu = getBaseContext().createConfigurationContext(configurationEu);
                getBaseContext().getResources().updateConfiguration(configurationEu, contextEu.getResources().getDisplayMetrics());
                break;
        }

    }

    public void guardarPreferenciaEstilo(String tema){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tema", tema);
        editor.commit();
    }

    public void guardarPreferenciaLogin(String user){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loged_user", user);
        editor.commit();
    }

    public void guardarPreferenciaIdioma(String idioma){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idioma", idioma);
        editor.commit();
    }
}