package com.tvpodplus.clienteexpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FrameLayout contenedorFragmento;
    private LayoutInflater inflador;

    private Button btnConsumo1, btnConsumo2, btnConsumo3, btnConsumo4, btnConsumo5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contenedorFragmento = findViewById(R.id.fragment_container);
        inflador = LayoutInflater.from(this);

        btnConsumo1 = findViewById(R.id.consumo1);
        btnConsumo2 = findViewById(R.id.consumo2);
        btnConsumo3 = findViewById(R.id.consumo3);
        btnConsumo4 = findViewById(R.id.consumo4);
        btnConsumo5 = findViewById(R.id.consumo5);

        configurarBotones();
    }

    private void configurarBotones() {
        btnConsumo1.setOnClickListener(v -> {
            //Consumo1 Activity
            startActivity(new Intent(MainActivity.this, Consumo1.class));
        });

        btnConsumo2.setOnClickListener(v -> {
            //Consumo2 Activity
            startActivity(new Intent(MainActivity.this, Consumo2.class));
        });

        btnConsumo3.setOnClickListener(v -> {
            //Consumo3 Activity
            startActivity(new Intent(MainActivity.this, Consumo3.class));
        });

        btnConsumo4.setOnClickListener(v -> {
            //Consumo4 Activity
            startActivity(new Intent(MainActivity.this, Consumo4.class));
        });

        btnConsumo5.setOnClickListener(v -> {
            //Consumo5 Activity
            startActivity(new Intent(MainActivity.this, Consumo5.class));
        });
    }
}
