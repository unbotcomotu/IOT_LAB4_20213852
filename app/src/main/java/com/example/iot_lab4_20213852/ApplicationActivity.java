package com.example.iot_lab4_20213852;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.iot_lab4_20213852.databinding.ActivityApplicationBinding;
import com.example.iot_lab4_20213852.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ApplicationActivity extends AppCompatActivity {
    ActivityApplicationBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_application);
        b=ActivityApplicationBinding.inflate(getLayoutInflater());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(b.bottomNavigationView, navController);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.ligasFragment, true)
                .build();
        b.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {


            if (item.getItemId() == R.id.ligasFragment) {
                navController.navigate(R.id.ligasFragment, null, navOptions);
                return true;
            } else if (item.getItemId() == R.id.posicionesFragment) {
                navController.navigate(R.id.posicionesFragment, null, navOptions);
                return true;
            } else if (item.getItemId() == R.id.resultadosFragment) {
                navController.navigate(R.id.resultadosFragment, null, navOptions);
                return true;
            }
            return false;
        });

    }
}