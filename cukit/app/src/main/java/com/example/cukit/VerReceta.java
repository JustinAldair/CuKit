package com.example.cukit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VerReceta extends AppCompatActivity {


    ImageView ivReceta, profile_image;
    TextView tvNombreReceta, tvUsuarioReceta, tvTiempoReceta, tv_comentar, tv_calificar;
    ListView lvIngredientesReceta, lvPasosReceta;
    EditText et_comentario;

    ViewGroup ly_comentarios, ly_comentarios_container;

    NavigationView nav_view;
    private String idReceta = null;
    private String usuario = null;

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
                        intent2 = new Intent(getApplicationContext(), Perfil.class);
                        startActivity(intent2);
                        finish();
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

                    case R.id.op4:
                        intent = new Intent(getApplicationContext(), AgregarReceta.class);
                        startActivity(intent);
                        finish();
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
        ly_comentarios= (ViewGroup) findViewById(R.id.ly_comentarios);
        ly_comentarios_container = (ViewGroup) findViewById(R.id.ly_comentarios_container);
        tv_comentar = (TextView) findViewById(R.id.tv_comentar);
        et_comentario = (EditText) findViewById(R.id.et_comentario);
        tv_calificar = (TextView) findViewById(R.id.tv_calificar);

        tv_calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificarReceta();
            }
        });

        et_comentario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if(et_comentario.getText().length() == 0){
                        Snackbar.make(getWindow().getDecorView(), "Escriba su comentario", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        comentar();
                    }
                    return true;
                }
                return false;
            }
        });

        ly_comentarios_container.setVisibility(View.GONE);
        tv_comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ly_comentarios_container.getVisibility() == View.VISIBLE) {
                    ly_comentarios_container.setVisibility(View.GONE);
                } else {
                    ly_comentarios_container.setVisibility(View.VISIBLE);
                }
            }
        });


        obtenerInfo();
        obtenerReceta();
        obtenerComentarios();
    }

    public void obtenerComentarios(){
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor
        String token = localStorage.getString("token", "");//Obtengo el Token

        String url = "http://"+ip_servidor+":3000/recetas/comentarios/"+this.idReceta;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {
                                JSONArray res = jsonObject.getJSONArray("res");

                                for (int i = 0; i < res.length(); i++) {//3

                                    JSONObject objeto = res.getJSONObject(i);

                                    String comentario = objeto.getString("comentario");
                                    String usuario = objeto.getString("usuario");
                                    String fecha = objeto.getString("fecha").split("T")[0];
                                    String url_foto = objeto.getString("url_foto");

                                    cargarComentarios(comentario, usuario, fecha, url_foto);

                                }
                            }else{
                                Snackbar.make(getWindow().getDecorView(), "Error al Obtener los Comentarios", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView(), "Se produjo al Cargar los Comentarios", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    public void obtenerInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");
        String token = localStorage.getString("token", "");
        String idPerfil = localStorage.getString("idPefil", "");

        String url = "http://"+ip_servidor+":3000/usuarios/info/"+idPerfil;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {
                                JSONArray res = jsonObject.getJSONArray("res");

                                for (int i = 0; i < res.length(); i++) {//3

                                    JSONObject objeto = res.getJSONObject(i);
                                    usuario = objeto.getString("usuario");

                                }
                            }else{
                                Snackbar.make(getWindow().getDecorView(), "Error al Obtener Tus Datos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView(), "Se produjo tus Datos", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

                                    String imageUrl = "http://"+ip_servidor+":3000/imagen/"+fotoComida[0];
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(ivReceta);

                                    imageUrl = "http://"+ip_servidor+":3000/imagen/"+fotoPerfil;
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

    public void calificar(int rating){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor
        String token = localStorage.getString("token", "");//Obtengo el Token de autenticación

        String url = "http://"+ip_servidor+":3000/puntuacion/agregar";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){
                                Snackbar.make(getWindow().getDecorView(), "Error al registrar la calificación", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else {
                                ly_comentarios.removeAllViews();

                                obtenerComentarios();
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

                        Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Agregar un header estándar
                headers.put("authorization", "Barear "+token); // Agregar un header estándar
                return headers;
            }

            //Mandar parametros a la petición en formato JSON
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Enviar un cuerpo JSON en la solicitud POST
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("idReceta", idReceta);
                    jsonBody.put("usuario", tvUsuarioReceta.getText().toString());
                    jsonBody.put("puntuacion", Float.parseFloat(Integer.toString(rating)) * 0.5);

                    Date date = new Date();
                    // Now obtain the week ot year when the task was finished
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateFormatString = dateFormat.format(date);

                    jsonBody.put("fecha", dateFormatString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        //Agregamos la petición para ser procesada y ejecutada
        queue.add(request);
    }

    public void comentar(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor
        String token = localStorage.getString("token", "");//Obtengo el Token de autenticación
        String idPefil = localStorage.getString("idPefil", "");//Obtengo el id del perfil de usuario

        String url = "http://"+ip_servidor+":3000/comentarios/agregar";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){
                                Snackbar.make(getWindow().getDecorView(), "Error al Relizar el Comentario", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else {
                                ly_comentarios.removeAllViews();

                                obtenerComentarios();
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

                        Snackbar.make(getWindow().getDecorView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Agregar un header estándar
                headers.put("authorization", "Barear "+token); // Agregar un header estándar
                return headers;
            }

            //Mandar parametros a la petición en formato JSON
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Enviar un cuerpo JSON en la solicitud POST
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("idReceta", idReceta);
                    jsonBody.put("usuario", usuario);
                    jsonBody.put("comentario", et_comentario.getText().toString());
                    et_comentario.setText("");

                    jsonBody.put("idPerfil", idPefil);
                    Date date = new Date();
                    // Now obtain the week ot year when the task was finished
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateFormatString = dateFormat.format(date);

                    jsonBody.put("fecha", dateFormatString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        //Agregamos la petición para ser procesada y ejecutada
        queue.add(request);
    }


    public void calificarReceta(){
        // Crear un RatingBar y agregarlo a un LinearLayout
        RatingBar ratingBar = new RatingBar(this);
        LinearLayout layout = new LinearLayout(this);
        layout.addView(ratingBar);


        // Crear una alerta y agregar el LinearLayout al contenido
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calificar Receta");
        builder.setView(layout);

        // Agregar un botón "Aceptar"
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al hacer clic en "Aceptar"
                int rating = ratingBar.getProgress(); // Obtener la calificación actual
                calificar(rating);
            }
        });

        // Agregar un botón "Cancelar"
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al hacer clic en "Cancelar"
                dialog.cancel();
            }
        });

        // Mostrar la alerta
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void cargarComentarios(String comentario, String usuario, String fecha, String foto){
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        int id = R.layout.comentario;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageView perfil = (ImageView) relativeLayout.findViewById(R.id.profile_image);
        String imageUrl = "http://"+ip_servidor+":3000/imagen/"+foto;
        ImageView image_usuario = (ImageView) relativeLayout.findViewById(R.id.profile_image);
        Glide.with(this)
                .load(imageUrl)
                .override(200, 200)
                .into(perfil);

        TextView tv_fecha = (TextView) relativeLayout.findViewById(R.id.tv_fecha);
        tv_fecha.setText(fecha);

        TextView tv_comentario = (TextView) relativeLayout.findViewById(R.id.tv_comentario);
        tv_comentario.setText(comentario);

        TextView tv_usuario = (TextView) relativeLayout.findViewById(R.id.tv_usuario);
        tv_usuario.setText(usuario);
        ly_comentarios.addView(relativeLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), inicio.class);
        startActivity(intent);
        finish();
    }
}