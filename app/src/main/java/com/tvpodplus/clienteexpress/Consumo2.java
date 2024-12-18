package com.tvpodplus.clienteexpress;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consumo2 extends AppCompatActivity {

    private TextView textViewResultado;
    private Button btnObtenerRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo2);

        textViewResultado = findViewById(R.id.txtViewResultado);
        btnObtenerRespuesta = findViewById(R.id.btnObtenerRespuesta);

        btnObtenerRespuesta.setOnClickListener(v -> obtenerRespuesta());
    }

    private void obtenerRespuesta() {
        consumirServicio();
    }

    private void consumirServicio() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://192.168.56.1:3000/Cristian");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder respuesta = new StringBuilder();
                    String linea;

                    while ((linea = in.readLine()) != null) {
                        respuesta.append(linea);
                    }
                    in.close();

                    runOnUiThread(() -> textViewResultado.setText(respuesta.toString()));

                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
