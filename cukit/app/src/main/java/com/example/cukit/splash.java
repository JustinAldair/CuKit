package com.example.cukit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class splash extends AppCompatActivity {

    private static final int dura = 1000; // Duración del splash screen en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Creamos un nuevo AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// Configuramos el diálogo
        builder.setTitle("Ingresar texto");
        builder.setMessage("Por favor, ingrese el texto que desea:");

        // Creamos el layout personalizado para el diálogo
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.input, null);
        builder.setView(view);

        // Agregamos el botón de confirmación
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtenemos el texto ingresado por el usuario
                EditText editText = view.findViewById(R.id.et_ip);
                String ip = editText.getText().toString();

                //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
                SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                //Alamaceno la IP del servidor para usarlo en futuras peticiones y cualquier cambio en la ip unicamente
                //modifcar la linea 25 con la nueva ip
                SharedPreferences.Editor editor = localStorage.edit();
                editor.putString("ip", ip);
                editor.commit();

                // Creamos un handler para lanzar la actividad principal luego del tiempo de duración del splash screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(splash.this, inicioSesion.class);
                        startActivity(mainIntent);
                        finish(); // Cerramos la actividad del splash screen
                    }
                }, dura);

            }
        });

        // Mostramos el diálogo
        builder.show();

    }
}
