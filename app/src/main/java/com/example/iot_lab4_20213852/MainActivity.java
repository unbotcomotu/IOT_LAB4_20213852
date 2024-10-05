package com.example.iot_lab4_20213852;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iot_lab4_20213852.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.botonIngresar.setOnClickListener(view -> {
            if(verificarConexionInternet()){
                ingresar();
            }else {
                lanzarDialogNoInternet();
            }
        });
    }

    private Boolean verificarConexionInternet(){
        ConnectivityManager manager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
        return activeNetworkInfo!=null&&activeNetworkInfo.isConnected();
    }


    private void lanzarDialogNoInternet(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("No hay conexión a Internet")
                .setMessage("¿Desea ingresar a su configuración?")
                .setPositiveButton("Configuración", (dialog, which) -> {
                    Intent intent= new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Entendido", (dialog, which) -> {
                })
                .setIcon(R.drawable.iconwarning)
                .show();
    }

    private void ingresar(){
        Intent intent=new Intent(this,ApplicationActivity.class);
        startActivity(intent);
    }

}