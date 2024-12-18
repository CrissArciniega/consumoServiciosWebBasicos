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

public class Consumo3 extends AppCompatActivity {

    private TextView textViewNumeros, textViewResultado;
    private Button btnSuma;

    private static final int NUMERO_1 = 2;
    private static final int NUMERO_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo3);

        textViewNumeros = findViewById(R.id.txtNumerosSumados);
        textViewResultado = findViewById(R.id.txtResultado);
        btnSuma = findViewById(R.id.btnSuma);

        btnSuma.setOnClickListener(v -> obtenerSuma());
    }

    private void obtenerSuma() {
        textViewNumeros.setText("Números: " + NUMERO_1 + " + " + NUMERO_2);
        consumirServicio();
    }

    private void consumirServicio() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://192.168.56.1:3000/suma");
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

                    runOnUiThread(() -> {
                        textViewResultado.setText("Resultado: " + respuesta.toString());
                    });

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
