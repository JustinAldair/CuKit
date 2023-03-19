package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registrate extends AppCompatActivity {
    private TextView tv_iniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);

        //Volver al inicio de sesion
        tv_iniciarSesion = findViewById(R.id.r_tv_iniciarSesion);
        tv_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), inicioSesion.class);
                startActivity(intent);
            }
        });
    }
}