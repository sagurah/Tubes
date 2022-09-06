package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

public class MainMenu extends AppCompatActivity() {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}