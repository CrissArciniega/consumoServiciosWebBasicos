package com.tvpodplus.clienteexpress;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consumo4 extends AppCompatActivity {

    private EditText editTextNumero;
    private TextView textViewResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo4);

        editTextNumero = findViewById(R.id.editTextNumber);
        textViewResultado = findViewById(R.id.txtViewsResultado);
        Button btnSumar = findViewById(R.id.btnSumar);

        btnSumar.setOnClickListener(v -> {
            String numero = editTextNumero.getText().toString();
            if (numero.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un número", Toast.LENGTH_SHORT).show();
            } else {
                consumirAPI(numero);
            }
        });
    }

    private void consumirAPI(String numero) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://10.10.16.78:3000/suma/" + numero);
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

                    runOnUiThread(() -> textViewResultado.setText("Resultado: " + respuesta.toString()));

                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error del servidor: " + responseCode, Toast.LENGTH_SHORT).show());
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
