package desarrollomovil.fabianpineda.xyz.poke;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final String URL_POKEMON = "http://pokeapi.co/api/v2/pokemon/%d/";

    private static final Random RNG = new Random();

    private TextView pokemon1Nombre;
    private TextView pokemon1HP;
    private ImageView pokemon1Imagen;

    private TextView pokemon2Nombre;
    private TextView pokemon2HP;
    private ImageView pokemon2Imagen;

    private String nombre1;
    private String nombre2;
    private String imagen1;
    private String imagen2;

    private int vida1;
    private int vida2;

    private TextView informacion;

    private Button botonAccion;

    private boolean enProgreso;
    private boolean descargando;

    private class Request extends AsyncTask<String, Void, String[]> {
        private IOException error;

        @Override
        protected String[] doInBackground(String... strings) {
            URL u;
            HttpURLConnection conexion;
            InputStream in;

            StringBuilder resultado1 = new StringBuilder();
            StringBuilder resultado2 = new StringBuilder();

            int leidos = 0;
            byte[] buffer = new byte[1024];

            try {
                u = new URL(strings[0]);
                conexion = (HttpURLConnection) u.openConnection();
                in = new BufferedInputStream(conexion.getInputStream());

                while ((leidos = in.read(buffer)) != -1) {
                    resultado1.append(new String(buffer, 0, leidos));
                }

                in.close();
                conexion.disconnect();

                u = new URL(strings[1]);
                conexion = (HttpURLConnection) u.openConnection();
                in = new BufferedInputStream(conexion.getInputStream());

                leidos = 0;
                buffer = new byte[1024];

                while ((leidos = in.read(buffer)) != -1) {
                    resultado2.append(new String(buffer, 0, leidos));
                }

                in.close();
                conexion.disconnect();
            } catch(IOException e) {
                error = e;
                descargando = false;
                return null;
            }

            return new String[] {resultado1.toString(), resultado2.toString()};
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            descargando = false;

            if (error != null) {
                actualizarInformacion("Error: " + error.getMessage());
                return;
            }

            procesarJSON(s);
        }
    }

    private void luchar() {
        int turno = RNG.nextInt(2);
        int dano = 1 + RNG.nextInt(20);

        String salida = "";

        if (turno == 0) {
            salida = salida + "Turno de " + nombre1 + "!\n";
            vida2 -= dano;

            salida = salida + nombre1 + " ataca a " + nombre2 + " por " + dano + " daño!\n";
        } else {
            salida = salida + "Turno de " + nombre2 + "!\n";
            vida1 -= dano;

            salida = salida + nombre2 + " ataca a " + nombre1 + " por " + dano + " daño!\n";
        }

        if (vida1 < 1) {
            vida1 = 0;
            salida = salida + nombre1 + " ha sido derrotado. " + nombre2 + " gana la batalla!\nPresione el botón para iniciar una nueva batalla.";
            actualizarInformacion(salida);
            pokemon1HP.setText(String.valueOf(vida1));
            enProgreso = false;
            return;
        } else if (vida2 < 1) {
            vida2 = 0;
            salida = salida + nombre2 + " ha sido derrotado. " + nombre1 + " gana la batalla!\nPresione el botón para iniciar una nueva batalla.";
            actualizarInformacion(salida);
            pokemon2HP.setText(String.valueOf(vida2));
            enProgreso = false;
            return;
        }

        dano = 1 + RNG.nextInt(20);

        if (turno == 0) {
            salida = salida + "Turno de " + nombre2 + "!\n";
            vida1 -= dano;

            salida = salida + nombre2 + " ataca a " + nombre1 + " por " + dano + " daño!\n";
        } else {
            salida = salida + "Turno de " + nombre1 + "!\n";
            vida2 -= dano;

            salida = salida + nombre1 + " ataca a " + nombre2 + " por " + dano + " daño!\n";
        }

        if (vida1 < 1) {
            vida1 = 0;
            salida = salida + nombre1 + " ha sido derrotado. " + nombre2 + " gana la batalla!\nPresione el botón para iniciar una nueva batalla.";
            enProgreso = false;
        } else if (vida2 < 1) {
            vida2 = 0;
            salida = salida + nombre2 + " ha sido derrotado. " + nombre1 + " gana la batalla!\nPresione el botón para iniciar una nueva batalla.";
            enProgreso = false;
        }

        actualizarInformacion(salida);
        pokemon1HP.setText(String.valueOf(vida1));
        pokemon2HP.setText(String.valueOf(vida2));
    }

    private void comenzarBatalla() {
        enProgreso = true;

        pokemon1Nombre.setText(nombre1);
        pokemon2Nombre.setText(nombre2);

        vida1 = 100;
        vida2 = 100;

        pokemon1HP.setText(String.valueOf(vida1));
        pokemon2HP.setText(String.valueOf(vida2));

        Glide.with(this).load(imagen1).into(pokemon1Imagen);
        Glide.with(this).load(imagen2).into(pokemon2Imagen);
    }

    private void procesarJSON(String[] datos) {
        JSONObject pokemon1 = null;
        JSONObject pokemon2 = null;

        try {
            pokemon1 = new JSONObject(datos[0]);
            pokemon2 = new JSONObject(datos[1]);
            nombre1 = pokemon1.getString("name");
            nombre2 = pokemon2.getString("name");
            imagen1 = pokemon1.getJSONObject("sprites").getString("front_default");
            imagen2 = pokemon2.getJSONObject("sprites").getString("front_default");
        } catch (JSONException e) {
            actualizarInformacion("Error procesando respuesta.");
            return;
        }

        comenzarBatalla();
    }

    private void descargar(int pokemon1, int pokemon2) {
        String url1 = String.format(URL_POKEMON, pokemon1);
        String url2 = String.format(URL_POKEMON, pokemon2);

        new Request().execute(new String[] {url1, url2});
    }

    private void actualizarInformacion(String texto) {
        informacion.setText(texto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enProgreso = false;
        descargando = false;

        pokemon1Nombre = (TextView) findViewById(R.id.poke1nombre);
        pokemon1HP = (TextView) findViewById(R.id.poke1hp);
        pokemon1Imagen = (ImageView) findViewById(R.id.poke1img);

        pokemon2Nombre = (TextView) findViewById(R.id.poke2nombre);
        pokemon2HP = (TextView) findViewById(R.id.poke2hp);
        pokemon2Imagen = (ImageView) findViewById(R.id.poke2img);

        informacion = (TextView) findViewById(R.id.informacion);

        botonAccion = (Button) findViewById(R.id.botonAccion);
        botonAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enProgreso && !descargando) {
                    descargando = true;
                    descargar(1 + RNG.nextInt(721), 1 + RNG.nextInt(721));
                } else if (enProgreso) {
                    luchar();
                }
            }
        });
    }
}
