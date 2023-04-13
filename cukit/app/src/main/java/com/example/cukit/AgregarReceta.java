package com.example.cukit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AgregarReceta extends AppCompatActivity {


    NavigationView nav_view;
    Spinner spinner;
    EditText et_platillo, et_descripcion, et_ingredientes, et_instrucciones, et_tiempo;
    ArrayList<String> idCategorias = new ArrayList<>();
    Button btn_guardar, btn_cargar_img, button_capture;
    TextView tv_statusImg;
    ImageView iv_comida_agregar;

    TextureView textureView;

    private String base64Image;

    private static final String BASE = "data:image/png;base64,";
    private String sImage = "";


    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);

        tv_statusImg = (TextView) findViewById(R.id.status_img);
        tv_statusImg.setVisibility(View.GONE);
        iv_comida_agregar = (ImageView) findViewById(R.id.iv_comida_agregar);
        iv_comida_agregar.setVisibility(View.GONE);

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

        spinner = (Spinner) findViewById(R.id.spinner);
        et_platillo = (EditText) findViewById(R.id.et_platillo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_ingredientes = (EditText) findViewById(R.id.et_ingredientes);
        et_instrucciones = (EditText) findViewById(R.id.et_instrucciones);
        et_tiempo = (EditText) findViewById(R.id.et_tiempo);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_cargar_img = (Button) findViewById(R.id.btn_cargar_img);

        btn_cargar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galleryIntent();

            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarReceta(v);
            }
        });

        obtenerCategorias();

        button_capture = (Button) findViewById(R.id.button_capture);
        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });



    }



    private void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void obtenerCategorias(){
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor
        String token = localStorage.getString("token", "");//Obtengo el Token

        String url = "http://"+ip_servidor+":3000/categorias";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "true") {
                                JSONArray res = jsonObject.getJSONArray("res");

                                ArrayList<String> categorias = new ArrayList<>();

                                for (int i = 0; i < res.length(); i++) {//3

                                    JSONObject objeto = res.getJSONObject(i);

                                    String idcategoria = objeto.getString("idcategoria");
                                    String nombre = objeto.getString("nombre");

                                    idCategorias.add(idcategoria);
                                    categorias.add(nombre);

                                }
                                cargarCategorias(categorias);

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

    public void guardarReceta(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");
        String token = localStorage.getString("token", null);
        String idPefil = localStorage.getString("idPefil", null);

        String url = "http://"+ip_servidor+":3000/recetas/agregar";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(view, "Error al Agregar la Receta Verifique sus Campos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(view, "Receta Guardada", Snackbar.LENGTH_LONG)
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
                    jsonBody.put("nombre", et_platillo.getText().toString());
                    jsonBody.put("descripcion", et_descripcion.getText().toString());
                    jsonBody.put("ingredientes", et_ingredientes.getText().toString());
                    jsonBody.put("instrucciones", et_instrucciones.getText().toString());
                    jsonBody.put("tiempo", et_tiempo.getText().toString());
                    jsonBody.put("idPerfil", idPefil);

                    jsonBody.put("url_fotos","data:image/jpg;base64,"+base64Image);

                    String idCategoria = idCategorias.get(spinner.getSelectedItemPosition());

                    jsonBody.put("idCategoria", Integer.parseInt(idCategoria));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }

    public void cargarCategorias(ArrayList categorias){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, categorias);
        spinner.setAdapter(adaptador);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            System.out.println("CORRECT");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_comida_agregar.setImageBitmap(photo);
            iv_comida_agregar.setVisibility(View.VISIBLE);
            tv_statusImg.setVisibility(View.VISIBLE);

        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    iv_comida_agregar.setImageBitmap(decodedBitmap);
                    iv_comida_agregar.setVisibility(View.VISIBLE);


                    tv_statusImg.setVisibility(View.VISIBLE);

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