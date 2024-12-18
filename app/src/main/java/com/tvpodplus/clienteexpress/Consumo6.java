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

public class Consumo6 extends AppCompatActivity {

    private EditText inputA, inputB;
    private Spinner spinnerTipo;
    private Button buttonCalcular;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo6);

        inputA = findViewById(R.id.input_a);
        inputB = findViewById(R.id.input_b);
        spinnerTipo = findViewById(R.id.spinner_tipo);
        buttonCalcular = findViewById(R.id.button_calcular);
        textResultado = findViewById(R.id.text_resultado);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operaciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorA = inputA.getText().toString();
                String valorB = inputB.getText().toString();
                String tipo = spinnerTipo.getSelectedItem().toString().toLowerCase();

                if (valorA.isEmpty() || valorB.isEmpty()) {
                    Toast.makeText(Consumo6.this, "Por favor, ingresa todos los valores.", Toast.LENGTH_SHORT).show();
                    return;
                }

                calcularTrinomio(valorA, valorB, tipo);
            }
        });
    }

    private void calcularTrinomio(String a, String b, String tipo) {
        String urlStr = "http://192.168.56.1:3000/trinomio/" + a + "/" + b + "/" + tipo;

        new Thread(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                runOnUiThread(() -> textResultado.setText("Resultado: " + response.toString()));

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(Consumo6.this, "Error al conectar con el servicio.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
