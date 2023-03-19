package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    private static final int dura = 1000; // Duración del splash screen en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        //Alamaceno la IP del servidor para usarlo en futuras peticiones y cualquier cambio en la ip unicamente
        //modifcar la linea 25 con la nueva ip
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putString("ip", "192.168.1.78");
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
}
