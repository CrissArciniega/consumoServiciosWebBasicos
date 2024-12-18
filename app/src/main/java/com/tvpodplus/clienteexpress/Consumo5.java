package com.tvpodplus.clienteexpress;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consumo5 extends AppCompatActivity {

    private Spinner spinnerFiguras;
    private EditText editParam1, editParam2;
    private Button btnCalcular;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo5);

        spinnerFiguras = findViewById(R.id.spinnerFiguras);
        editParam1 = findViewById(R.id.editParam1);
        editParam2 = findViewById(R.id.editParam2);
        btnCalcular = findViewById(R.id.btnCalcular);
        txtResultado = findViewById(R.id.txtResultado);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.figuras_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiguras.setAdapter(adapter);

        btnCalcular.setOnClickListener(v -> calcularFigura());
    }

    private void calcularFigura() {
        String figura = spinnerFiguras.getSelectedItem().toString().toLowerCase();
        String param1 = editParam1.getText().toString();
        String param2 = editParam2.getText().toString();

        if (param1.isEmpty() || param2.isEmpty()) {
            Toast.makeText(this, "Ingrese ambos parámetros", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlServicio = "http://192.168.56.1:3000/figura/" + figura + "/" + param1 + "/" + param2;

        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlServicio);
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

                    runOnUiThread(() -> txtResultado.setText("Resultado: " + respuesta.toString()));
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
