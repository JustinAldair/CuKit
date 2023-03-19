package com.example.cukit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class inicio extends AppCompatActivity {

  ViewGroup view_populares;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inicio);
    view_populares = (ViewGroup) findViewById(R.id.view_populares);
    cargarFavoritos();
    cargarFavoritos();
    cargarFavoritos();
    cargarFavoritos();
  }

  public void cargarFavoritos(){
    LayoutInflater inflater = LayoutInflater.from(this);
    int id = R.layout.receta_popular;
    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

    TextView tv_nombre = (TextView) relativeLayout.findViewById(R.id.tv_nombre);
    TextView tv_categoria = (TextView) relativeLayout.findViewById(R.id.tv_categoria);
    ImageView image_comida = (ImageView) relativeLayout.findViewById(R.id.image_comida);
    tv_nombre.setText("NOMBRE PLATILLO");
    tv_categoria.setText("NOMBRE CATEGORIA");
    String imageUrl = "https://img.freepik.com/foto-gratis/camarones-plancha-ensalada-verduras-frescas-comida-sana-endecha-plana-ensalada-caprese-italiana-tomates-albahaca-mozzarella-ingredientes-ensalada-caprese-tradicional-italiana-ensalada-griega-mediterranea_1150-44795.jpg";
    Glide.with(this)
            .load(imageUrl)
            .override(200, 200)
            .into(image_comida);

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    relativeLayout.setPadding(5, 0, 5, 10);
    relativeLayout.setLayoutParams(params);
    view_populares.addView(relativeLayout);

  }

}