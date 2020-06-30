package com.javieramado.ejemploretrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imgComic;
    TextView txtvTitle;
    Button btnGetComic;
    XkcdService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgComic = findViewById(R.id.imgComic);
        txtvTitle = findViewById(R.id.txtvTitle);
        btnGetComic = findViewById(R.id.btnGetComic);

        //Se crea la variable de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creación del servicio
        service = retrofit.create(XkcdService.class);

        //Funcionalidad del botón
        btnGetComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LLAMAMOS A LA API Y OBTENEMOS LOS DATOS
                //Objeto que nos permite hacer la llamada. Obtiene un cómic aleatorio
                Call<Comic> comicCall = service.getComic(new Random().nextInt(1000)); //Es asíncrono, la ejecución va a parte de la ejecución de la app
                //Encolamos la llamada
                comicCall.enqueue(new Callback<Comic>() {
                    //Qué pasa cuando la respuesta es correcta
                    @Override
                    public void onResponse(Call<Comic> call, Response<Comic> response) {
                        //Con esto ya tenemos nuestro objeto cómic
                        Comic comic = response.body();

                        try {
                            //Ahora metemos el cómic en el ImageView
                            if (comic != null) {
                                txtvTitle.setText(comic.getTitle()); //añade el título de la imágen
                                //Uso de la librería Picasso para el tratamiento de imágenes
                                Picasso.with(MainActivity.this) //Se le pasa el contexto
                                        .load(comic.getImg()) //Se le pasa la imagen de nuestro objeto comic
                                        .into(imgComic); //Se le pasa el ImageView donde se va a mostrar
                            }
                        } catch (Exception e){
                            Log.e("MainActivity", e.toString());
                        }
                    }
                    //Qué pasa cuando la respuesta no es correcta
                    @Override
                    public void onFailure(Call<Comic> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Ocurrió un error con la API", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
