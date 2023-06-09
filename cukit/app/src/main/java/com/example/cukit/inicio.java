package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class inicio extends AppCompatActivity implements View.OnClickListener{

  ViewGroup view_populares, view_recetas;
  TableRow tr_recetas;
  Button btn_comedia_mexicana, btn_desayuno, btn_saludable, btn_comida_rapida, btn_postres;
  NavigationView nav_view;
  ImageView btn_menu;
  DrawerLayout drawer_layout;

  private int count = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inicio);
    view_populares = (ViewGroup) findViewById(R.id.view_populares);
    view_recetas = (ViewGroup) findViewById(R.id.view_recetas);
    tr_recetas = (TableRow) findViewById(R.id.tr_recetas);

    btn_comedia_mexicana = (Button) findViewById(R.id.btn_comedia_mexicana);
    btn_desayuno = (Button) findViewById(R.id.btn_desayuno);
    btn_saludable = (Button) findViewById(R.id.btn_saludable);
    btn_comida_rapida = (Button) findViewById(R.id.btn_comida_rapida);
    btn_postres = (Button) findViewById(R.id.btn_postres);
    ViewGroup ly_card = (ViewGroup) findViewById(R.id.ly_card);
    btn_menu = (ImageView) findViewById(R.id.btn_menu);
    ly_card.setVisibility(View.GONE);

    //Asigno los IDs de las categorias registradas en la BD a cada boton
    btn_comedia_mexicana.getTag(2);
    btn_desayuno.getTag(3);
    btn_saludable.getTag(4);
    btn_comida_rapida.getTag(5);
    btn_postres.getTag(6);

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

    drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
    btn_menu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawer_layout.openDrawer(GravityCompat.START);
      }
    });

    SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
    String token = localStorage.getString("token", null);


    if(token == null){
      Intent intent = new Intent(getApplicationContext(), inicioSesion.class);
      startActivity(intent);
      finish();
    }

    obtenerFavoritos(token);
    obtenerRecetas(token);
  }

  //=============================================
  //Peticiones al Servidor
  public void obtenerFavoritos(String token){
    RequestQueue queue = Volley.newRequestQueue(this);

    SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
    String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

    String url = "http://"+ip_servidor+":3000/recetas/populares";

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                  JSONArray res = jsonObject.getJSONArray("res");//Obtener valores del JSON

                  for (int i = 0; i < res.length(); i++) {
                    JSONObject objeto = res.getJSONObject(i);

                    String idReceta = objeto.getString("idreceta");
                    String nombre = objeto.getString("nombre");
                    String categoria = objeto.getString("categoria");
                    String url_fotos = objeto.getString("url_fotos");
                    String foto_perfil = objeto.getString("fotoPerfil");

                    String[] fotoComida = url_fotos.split(",");

                    cargarFavoritos(nombre, categoria, fotoComida[0], foto_perfil, idReceta);
                  }

                } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                  e.printStackTrace();
                  Snackbar.make(getWindow().getDecorView(), "Error al Cargar las Recetas Populares", Snackbar.LENGTH_LONG)
                          .setAction("Action", null).show();
                }
              }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          Snackbar.make(getWindow().getDecorView(), "Error Intentelo Más Tarde", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show();
        }
      }){
      //Establecer encabezados
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer "+token); // Agregar un header estándar
        return headers;
      }
    };
    queue.add(stringRequest);
  }

  public void obtenerRecetas(String token){
    RequestQueue queue = Volley.newRequestQueue(this);

    SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
    String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

    String url = "http://"+ip_servidor+":3000/recetas";

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                  JSONArray res = jsonObject.getJSONArray("res");//Obtener valores del JSON

                  ArrayList<String> ids = new ArrayList<String>();
                  ArrayList<String> puntuacion = new ArrayList<String>();

                  ArrayList<String> platillos = new ArrayList<String>();
                  ArrayList<String> fotos = new ArrayList<String>();

                  for (int i = 0; i < res.length(); i++) {//3

                    JSONObject objeto = res.getJSONObject(i);

                    String idreceta = objeto.getString("idreceta");
                    String platillo = objeto.getString("platillo");
                    String url_fotos = objeto.getString("url_fotos");
                    String promedio_puntuaciones = objeto.getString("promedio_puntuaciones");

                    String[] fotoComida = url_fotos.split(",");

                    ids.add(idreceta);
                    puntuacion.add(promedio_puntuaciones);
                    platillos.add(platillo);
                    fotos.add(fotoComida[0]);

                    if(ids.size()%2 == 0){
                      cargarRecetas(platillos, fotos, puntuacion, ids);
                      ids.clear();
                      platillos.clear();
                      fotos.clear();
                      puntuacion.clear();
                      puntuacion.clear();
                    }else if(i+1 >= res.length()){
                      cargarRecetas(platillos, fotos, puntuacion, ids);
                    }

                  }

                } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                  e.printStackTrace();
                  Snackbar.make(getWindow().getDecorView(), "Error al Obtener las Recetas", Snackbar.LENGTH_LONG)
                          .setAction("Action", null).show();
                }
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Snackbar.make(getWindow().getDecorView(), "Intentelo Más Tarde", Snackbar.LENGTH_LONG)
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
  //=============================================

  //Cargar en Pantalla Dinamicamente
  public void cargarFavoritos(String nombre, String categoria, String fotoComida, String foto_perfil, String idReceta){
    SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
    String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

    LayoutInflater inflater = LayoutInflater.from(this);
    int id = R.layout.receta_popular;
    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

    CardView cardView = (CardView) relativeLayout.findViewById(R.id.cardView);
    cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), VerReceta.class);
            intent.putExtra("idReceta", idReceta);
            startActivity(intent);
        }
    });


    TextView tv_nombre = (TextView) relativeLayout.findViewById(R.id.tv_nombre);
    TextView tv_categoria = (TextView) relativeLayout.findViewById(R.id.tv_categoria);
    ImageView image_comida = (ImageView) relativeLayout.findViewById(R.id.image_comida);
    tv_nombre.setText(nombre);
    tv_categoria.setText(categoria);
    String imageUrl = "http://"+ip_servidor+":3000/imagen/"+fotoComida;
    Glide.with(this)
            .load(imageUrl)
            .override(200, 200)
            .into(image_comida);

    imageUrl = "http://"+ip_servidor+":3000/imagen/"+foto_perfil;
    ImageView image_usuario = (ImageView) relativeLayout.findViewById(R.id.profile_image);
    Glide.with(this)
            .load(imageUrl)
            .override(200, 200)
            .into(image_usuario);

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    relativeLayout.setPadding(5, 0, 5, 10);
    relativeLayout.setLayoutParams(params);
    view_populares.addView(relativeLayout);

  }

  //Cada ArrayList unicamente tendrá como máximo 2 datos ya que se generaran 2 CardView como máximo por cada TableRow
  public void cargarRecetas(ArrayList nombres, ArrayList fotosComida, ArrayList puntuaciones, ArrayList ids) {
    SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
    String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

    LayoutInflater inflater = LayoutInflater.from(this);

    //Componente que tinene un TableRow la cual tendra como máximo 2 CardView
    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.card_comida, null);
    TableRow tableRow = (TableRow) relativeLayout.findViewById(R.id.tr_recetas);

    //Creo un LinearLayout la cual tendra las CardView creadas y después se cargaran al TableRow
    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    //Obtener las medidas de la pantalla para poder mostrar 2 CARDVIEW de manera horizontal
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int width = displayMetrics.widthPixels;

    int cardWidth = width / 2;//Calcular el Width que tendra el CardView

    //GENERO LAS CARDVIEW
    for (int i = 0; i < nombres.size(); i++) {
      RelativeLayout relativeLayout2 = (RelativeLayout) inflater.inflate(R.layout.card_comida_solo, null);
      ImageView image_comida = (ImageView) relativeLayout2.findViewById(R.id.image_comida_card_1);


      String imageUrl =  "http://"+ip_servidor+":3000/imagen/"+fotosComida.get(i).toString();
      Glide.with(this)
              .load(imageUrl)
              .override(200, 200)
              .into(image_comida);
      TextView tv_nombre = (TextView) relativeLayout2.findViewById(R.id.tv_nombre_receta_1);
      tv_nombre.setText(nombres.get(i).toString());

      //Carga ImageView que son estrellas que representan la puntuación del la receta donde 5 es la maxima calificación.

      if(puntuaciones.get(i).toString() != "null"){
        float puntuacion_float = Float.parseFloat(puntuaciones.get(i).toString());
        int puntuacion_int = (int) Math.floor(puntuacion_float);

        for(int j=0; j<puntuacion_int; j++){
          ViewGroup viewGroup = (ViewGroup)  relativeLayout2.findViewById(R.id.view_stars);
          RelativeLayout relativeLayout3 = (RelativeLayout) inflater.inflate(R.layout.star, null);
          viewGroup.addView(relativeLayout3);
        }
      }


      String idReceta = ids.get(i).toString();
      CardView cardView = (CardView) relativeLayout2.findViewById(R.id.view_card_comida);
      cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getApplicationContext(), VerReceta.class);
          intent.putExtra("idReceta", idReceta);
          startActivity(intent);
        }
      });

      //VOY AGREGANDO CADA CardView A un linear Layout
      linearLayout.addView(relativeLayout2, new LinearLayout.LayoutParams(cardWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    //Agrego el linearLayout a la tableRow la cual es un componente.xml que contiene como máximo 2 CardView
    tableRow.addView(linearLayout);

    //Finalamente cargo la tableRow al View el cual se vizualizara al usuario
    view_recetas.addView(relativeLayout);

  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onBackPressed() {
  }

}