package com.continental.calculadoranotas

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.continental.calculadoranotas.Adapters.AdapterListView
import com.continental.calculadoranotas.Entities.Nota

class Lista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val listView = findViewById<ListView>(R.id.lstNotas)

        val btnNuevo = findViewById<AppCompatButton>(R.id.btnNuevo)

        val databaseHelper = MainActivity.DatabaseHelper(this) // Crear una instancia de DatabaseHelper

        // Obtener la lista de notas desde la base de datos
        val notaList = databaseHelper.obtenerNotas()

        // Crear un adaptador para el ListView y configurarlo con la lista de notas
        val adapter = AdapterListView(this, notaList)
        listView.adapter = adapter

        btnNuevo.setOnClickListener{
            // Crear un Intent para iniciar ActividadB
            val intent = Intent(this, MainActivity::class.java)

            // Iniciar ActividadB
            startActivity(intent)

            //Ejecutamos animacion
            overridePendingTransition(R.anim.left_in, R.anim.left_out)
        }
    }
}