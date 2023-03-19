package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class inicioSesion extends AppCompatActivity {
    TextView tvRegistrate;
    Button btnInicio;
    EditText et_correo, etContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        tvRegistrate = findViewById(R.id.Registrate);
        btnInicio = findViewById(R.id.btnIniciarSesion);

        et_correo = findViewById(R.id.et_correo);
        etContraseña = findViewById(R.id.etContraseña);

        tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registrate.class);
                startActivity(intent);
            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticarse(view);
            }
        });
    }

    public void autenticarse(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        //Volley es una biblioteca para realizar peticiones HTTP en segundo plano
        RequestQueue queue = Volley.newRequestQueue(this);
        
        //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        //=========================================================
        String url = "http://"+ip_servidor+":3000/usuarios/auth";
        //=========================================================

        //El siguiente código es el cuerpo de la petición en este caso POST
        //Hay métodos para responder en caso de exito y en caso de error
        //Hay métodos para agregar encabezados a la petición
        //Hay métodos para mandar parametros en JSON
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(view, "Correo o Contraseña Incorrectos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{//Usuario Autenticado

                                //Con SharedPreferences almacenó el token que mandó con JWT desde el servidor
                                //este token se usara para autorizar cada petición que se desea ejecutar por el usuario.
                                SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                                SharedPreferences.Editor editor = localStorage.edit();
                                editor.putString("token", jsonObject.getString("auth"));
                                editor.commit();

                                //Abrir la pantalla Inicio
                                Intent intent = new Intent(inicioSesion.this, inicio.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {//En caso de error con el servidor
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Agregar un header estándar
                return headers;
            }

            //Mandar parametros a la petición en formato JSON
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Enviar un cuerpo JSON en la solicitud POST
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("correo", et_correo.getText().toString());
                    jsonBody.put("pass", etContraseña.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        //Agregamos la petición para ser procesada y ejecutada
        queue.add(request);
    }


}

