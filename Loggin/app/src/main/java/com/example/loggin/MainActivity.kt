package com.example.loggin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loggin.fragments.LogginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar el fragmento inicial de login si es la primera vez que se crea la actividad
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LogginFragment())
                .commit()
        }
    }
}