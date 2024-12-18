package com.tvpodplus.clienteexpress;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consumo1 extends AppCompatActivity {

    private TextView textViewResultado;
    private Button btnPrimerNombre, btnSegundoNombre, btnMostrarTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo1);

        textViewResultado = findViewById(R.id.txtViewResultado);
        btnPrimerNombre = findViewById(R.id.btnPrimerNombre);
        btnSegundoNombre = findViewById(R.id.btnSegundoNombre);
        btnMostrarTodo = findViewById(R.id.btnMostrarTodo);

        btnPrimerNombre.setOnClickListener(v -> obtenerNombres(1)); // Solicita el primer nombre
        btnSegundoNombre.setOnClickListener(v -> obtenerNombres(2)); // Solicita el segundo nombre
        btnMostrarTodo.setOnClickListener(v -> obtenerNombres(0)); // Solicita todos los nombres
    }

    private void obtenerNombres(int id) {
        consumirServicio(id);
    }

    private void consumirServicio(int id) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://10.10.16.78:3000/nombre");
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

                    runOnUiThread(() -> procesarRespuesta(respuesta.toString(), id));

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

    private void procesarRespuesta(String jsonResponse, int id) {
        try {
            JSONArray nombresArray = new JSONArray(jsonResponse);

            if (id == 0) {
                StringBuilder allNames = new StringBuilder("Todos los nombres:\n");
                for (int i = 0; i < nombresArray.length(); i++) {
                    JSONObject persona = nombresArray.getJSONObject(i);
                    String nombre = persona.getString("nombre");
                    allNames.append(nombre).append("\n");
                }
                textViewResultado.setText(allNames.toString());
            } else {
                JSONObject persona = nombresArray.getJSONObject(id - 1);
                String nombre = persona.getString("nombre");
                textViewResultado.setText("Nombre: " + nombre);
            }

        } catch (Exception e) {
            e.printStackTrace();
            textViewResultado.setText("Error al procesar la respuesta");
        }
    }
}
