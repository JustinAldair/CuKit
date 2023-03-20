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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registrate extends AppCompatActivity {
    private TextView tv_iniciarSesion;
    private EditText r_edt_nombre, r_ed_correo, r_ed_contraseña;
    private Button r_btn_Registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);

        r_edt_nombre = (EditText) findViewById(R.id.r_edt_nombre);
        r_ed_correo = (EditText) findViewById(R.id.r_ed_correo);
        r_ed_contraseña = (EditText) findViewById(R.id.r_ed_contraseña);

        r_btn_Registrarse = (Button) findViewById(R.id.r_btn_Registrarse);
        r_btn_Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse(view);
            }
        });

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

    public void registrarse(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://"+ip_servidor+":3000/usuario/registrarse";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(view, "El correo ingresado ya se encuentra registrado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                //Abrir la pantalla Inicio Sesion
                                Intent intent = new Intent(Registrate.this, inicioSesion.class);
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
                        System.out.println(error);
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
                    jsonBody.put("nombre", r_edt_nombre.getText().toString());
                    jsonBody.put("correo", r_ed_correo.getText().toString());
                    jsonBody.put("pass", r_ed_contraseña.getText().toString());
                    jsonBody.put("usuario", r_edt_nombre.getText().toString());
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