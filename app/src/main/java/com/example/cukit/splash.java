package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    private static final int dura = 1000; // Duración del splash screen en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
