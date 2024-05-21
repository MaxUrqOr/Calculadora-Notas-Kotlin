package com.continental.calculadoranotas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Spalsh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spalsh)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Creamos un nuevo Handler
        val handler = Handler()

        // Tiempo de espera en milisegundos (ejemplo: 3 segundos)
        val tiempoEspera = 2000L

        // Creamos un Runnable que se ejecutará después del tiempo de espera
        val runnable = Runnable {
            // Este código se ejecutará después del tiempo de espera
            // Por ejemplo, aquí podemos iniciar una nueva actividad
            val intent = Intent(this, Lista::class.java)
            startActivity(intent)
            finish()
        }

        // Programamos la ejecución del Runnable después del tiempo de espera
        handler.postDelayed(runnable, tiempoEspera)
    }
}