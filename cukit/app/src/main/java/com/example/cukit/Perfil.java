package com.example.cukit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    EditText et_nombre_perfil, et_correo_perfil, et_usuario_perfil;
    ImageView iv_perfil;
    TextView tv_statusImg_perfil;
    NavigationView nav_view;
    Button btn_cargar_img_perfil, btn_guardar_perfil;
    private String base64Image_perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

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

        et_nombre_perfil = (EditText) findViewById(R.id.et_nombre_perfil);
        et_correo_perfil = (EditText) findViewById(R.id.et_correo_perfil);
        et_usuario_perfil = (EditText) findViewById(R.id.et_usuario_perfil);
        iv_perfil = (ImageView) findViewById(R.id.iv_perfil);
        tv_statusImg_perfil = (TextView) findViewById(R.id.status_img_perfil);
        btn_cargar_img_perfil = (Button) findViewById(R.id.btn_cargar_img_perfil);

        et_nombre_perfil.setEnabled(false);
        et_correo_perfil.setEnabled(false);
        tv_statusImg_perfil.setVisibility(View.GONE);

        btn_guardar_perfil = (Button) findViewById(R.id.btn_guardar_perfil);

        btn_cargar_img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galleryIntent();

            }
        });

        btn_guardar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarDatos(v);
            }
        });


        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView btn_menu = (ImageView) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });


        obtenerInfo();
    }

    public void ActualizarDatos(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");
        String token = localStorage.getString("token", null);
        String idPefil = localStorage.getString("idPefil", null);

        String url = "http://"+ip_servidor+":3000/perfil/actualizar/"+idPefil;

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(view, "Error al Actualizar Sus Datos Verifique sus Campos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(view, "Datos Actualizados", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                headers.put("authorization", "Barear "+token); // Agregar un header estándar
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("usuario", et_usuario_perfil.getText().toString());
                    jsonBody.put("url_foto","data:image/jpg;base64,"+base64Image_perfil);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
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

                                    et_usuario_perfil.setText(objeto.getString("usuario"));
                                    et_correo_perfil.setText(objeto.getString("correo"));
                                    et_nombre_perfil.setText(objeto.getString("nombre"));

                                    String url_foto = objeto.getString("url_foto");

                                    String imageUrl =  "http://"+ip_servidor+":3000/imagen/"+url_foto;
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(iv_perfil);


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

    private void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64Image_perfil = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    byte[] decodedBytes = Base64.decode(base64Image_perfil, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    iv_perfil.setImageBitmap(decodedBitmap);

                    btn_cargar_img_perfil.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), inicio.class);
        startActivity(intent);
        finish();
    }

}