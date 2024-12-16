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

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private TextView txtViewResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumber = findViewById(R.id.editTextNumber);
        txtViewResultado = findViewById(R.id.txtViewsResultado);
        Button btnSumar = findViewById(R.id.btnSumar);

        btnSumar.setOnClickListener(v -> {
            String n1 = editTextNumber.getText().toString();
            if (n1.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un número", Toast.LENGTH_SHORT).show();
            } else {
                consumirAPI(n1);
            }
        });
    }

    private void consumirAPI(String n1) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.10.16.78:3000/suma/" + n1);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String resultado = in.readLine();
                    in.close();
                    runOnUiThread(() -> txtViewResultado.setText("Resultado: " + resultado));
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}