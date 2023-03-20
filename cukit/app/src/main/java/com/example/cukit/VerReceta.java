package com.example.cukit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerReceta extends AppCompatActivity {


    ImageView ivReceta, profile_image;
    TextView tvNombreReceta, tvUsuarioReceta, tvTiempoReceta;
    ListView lvIngredientesReceta, lvPasosReceta;

    NavigationView nav_view;
    private String idReceta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_receta);

        //OBTENER EL ID DE LA RECETA A MOSTRAR
        Intent intent = getIntent();
        this.idReceta = intent.getStringExtra("idReceta");

        //MENU LATERAL
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.op1:
                        Intent intent2 = new Intent(getApplicationContext(), inicio.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.op2:
                        return true;
                    case R.id.op3:
                        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                        SharedPreferences.Editor editor = localStorage.edit();
                        editor.remove("token");
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), inicioSesion.class);
                        startActivity(intent);
                        finishAffinity();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //ELEMENTOS DE LA PANTALLA
        ivReceta = (ImageView) findViewById(R.id.ivReceta);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        tvNombreReceta = (TextView) findViewById(R.id.tvNombreReceta);
        tvUsuarioReceta = (TextView) findViewById(R.id.tvUsuarioReceta);
        tvTiempoReceta = (TextView) findViewById(R.id.tvTiempoReceta);
        lvIngredientesReceta=(ListView) findViewById(R.id.lvIngredientesReceta);
        lvPasosReceta=(ListView) findViewById(R.id.lvPasosReceta);

        obtenerReceta();
    }

    public void obtenerReceta(){
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor
        String token = localStorage.getString("token", "");//Obtengo el Token

        String url = "http://"+ip_servidor+":3000/recetas/receta/"+this.idReceta;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Snackbar.make(getWindow().getDecorView(), "Receta Cargada", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {
                                JSONArray res = jsonObject.getJSONArray("res");

                                for (int i = 0; i < res.length(); i++) {//3

                                    JSONObject objeto = res.getJSONObject(i);

                                    String platillo = objeto.getString("nombre");
                                    String usuario = objeto.getString("usuario");
                                    String url_fotos = objeto.getString("url_fotos");
                                    String fotoPerfil = objeto.getString("fotoPerfil");
                                    String ingredientes = objeto.getString("ingredientes");
                                    String instrucciones = objeto.getString("instrucciones");
                                    String tiempo = objeto.getString("tiempo");

                                    String[] fotoComida = url_fotos.split(",");
                                    String[] ingredientes_arr = ingredientes.split(",");
                                    String[] instrucciones_arr = instrucciones.split("\\.");

                                    String imageUrl = fotoComida[0];
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(ivReceta);

                                    imageUrl = fotoPerfil;
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(profile_image);

                                    tvNombreReceta.setText(platillo);
                                    tvUsuarioReceta.setText(usuario);
                                    tvTiempoReceta.setText(tiempo + " Minutos");

                                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, ingredientes_arr);
                                    lvIngredientesReceta.setAdapter(adaptador);

                                    adaptador = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, instrucciones_arr);
                                    lvPasosReceta.setAdapter(adaptador);


                                }
                            }else{
                                Snackbar.make(getWindow().getDecorView(), "Error al Cargar la Receta", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Se produjo un error al Obtener Receta: "+ error);
            }
        }){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "Barear "+token); // Agregar un header estándar
                return headers;
            }
        };
        queue.add(stringRequest);
    }

}