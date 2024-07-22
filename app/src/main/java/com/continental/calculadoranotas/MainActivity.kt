package com.continental.calculadoranotas

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.continental.calculadoranotas.Entities.Nota
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() {
    val notasList = mutableListOf<Nota>()

    private lateinit var edtpor1: EditText
    private lateinit var edtpor2: EditText
    private lateinit var edtpor3: EditText
    private lateinit var edtpor4: EditText
    private lateinit var edtnota1: EditText
    private lateinit var edtnota2: EditText
    private lateinit var edtnota3: EditText
    private lateinit var edtnota4: EditText
    private lateinit var edtNotaFinal: EditText
    private lateinit var edtNombre: EditText
    private lateinit var textFelicidades: TextView

    private lateinit var btnVerLista: AppCompatButton
    private lateinit var btnGuardar: AppCompatButton
    private lateinit var btnLimpiar: AppCompatButton
    private lateinit var btnEliminar: AppCompatButton

    private val TAG = "MainActivity"

    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Obtener el Intent que inició esta actividad
        val intent = intent

        // Extraer los datos del Intent
        val id = intent.getIntExtra("id", 0) // Aquí 0 es el valor predeterminado en caso de que no se encuentre el extra
        val nombre = intent.getStringExtra("nombre") ?: ""
        val por1 = intent.getStringExtra("por1") ?: ""
        val por2 = intent.getStringExtra("por2") ?: ""
        val por3 = intent.getStringExtra("por3") ?: ""
        val por4 = intent.getStringExtra("por4") ?: ""
        val nota1 = intent.getStringExtra("nota1") ?: ""
        val nota2 = intent.getStringExtra("nota2") ?: ""
        val nota3 = intent.getStringExtra("nota3") ?: ""
        val nota4 = intent.getStringExtra("nota4") ?: ""
        val notaFinal = intent.getStringExtra("notaFinal") ?: ""

        // FindViewById solo después de setContentView
        edtpor1 = findViewById(R.id.edtpor1)
        edtpor2 = findViewById(R.id.edtpor2)
        edtpor3 = findViewById(R.id.edtpor3)
        edtpor4 = findViewById(R.id.edtpor4)
        edtnota1 = findViewById(R.id.edtnota1)
        edtnota2 = findViewById(R.id.edtnota2)
        edtnota3 = findViewById(R.id.edtnota3)
        edtnota4 = findViewById(R.id.edtnota4)
        edtNotaFinal = findViewById(R.id.edtnotaFinal)
        textFelicidades = findViewById(R.id.textFelicidades)

        edtNombre = findViewById(R.id.edtNombre)

        btnVerLista = findViewById(R.id.btnVerLista)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnEliminar = findViewById(R.id.btnEliminar)

        if(id > 0){
            edtNombre.setText(nombre)

            edtpor1.setText(por1)
            edtpor2.setText(por2)
            edtpor3.setText(por3)
            edtpor4.setText(por4)

            edtnota1.setText(nota1)
            edtnota2.setText(nota2)
            edtnota3.setText(nota3)
            edtnota4.setText(nota4)

            edtNotaFinal.setText(notaFinal)
        }else{
            edtpor1.setText("20")
            edtpor2.setText("20")
            edtpor3.setText("20")
            edtpor4.setText("40")
        }

        val textoVisualizador = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // Calcula la nota final y actualiza el EditText edtNotaFinal
                calcularNotaFinal(edtpor1, edtpor2, edtpor3, edtpor4, edtnota1, edtnota2, edtnota3, edtnota4, edtNotaFinal)
            }
        }

        // Asigna el listener a cada EditText
        edtpor1.addTextChangedListener(textoVisualizador)
        edtpor2.addTextChangedListener(textoVisualizador)
        edtpor3.addTextChangedListener(textoVisualizador)
        edtpor4.addTextChangedListener(textoVisualizador)
        edtnota1.addTextChangedListener(textoVisualizador)
        edtnota2.addTextChangedListener(textoVisualizador)
        edtnota3.addTextChangedListener(textoVisualizador)
        edtnota4.addTextChangedListener(textoVisualizador)

        validarNotasEditText(edtnota1)
        validarNotasEditText(edtnota2)
        validarNotasEditText(edtnota3)
        validarNotasEditText(edtnota4)

        validarPorcentajesEditText(edtpor1)
        validarPorcentajesEditText(edtpor2)
        validarPorcentajesEditText(edtpor3)
        validarPorcentajesEditText(edtpor4)

        btnVerLista.setOnClickListener {
            val intent = Intent(this, Lista::class.java)

            // Iniciar ActividadB
            startActivity(intent)

            //Ejecutamos animacion
            overridePendingTransition(R.anim.left_in, R.anim.left_out)

            finish()
        }

        btnLimpiar.setOnClickListener {
            edtNombre.setText("")

            edtpor1.setText("")
            edtpor2.setText("")
            edtpor3.setText("")
            edtpor4.setText("")

            edtnota1.setText("")
            edtnota2.setText("")
            edtnota3.setText("")
            edtnota4.setText("")

            edtNotaFinal.setText("")
        }

        btnGuardar.setOnClickListener{

            if(id == 0){
                val databaseHelper = DatabaseHelper(this) // Aquí usamos el contexto de la actividad

                if(!edtNombre.text.toString().isEmpty()){
                    databaseHelper.agregarNotas(
                        edtNombre.text.toString(),
                        edtnota1.text.toString(),
                        edtnota2.text.toString(),
                        edtnota3.text.toString(),
                        edtnota4.text.toString(),
                        edtpor1.text.toString(),
                        edtpor2.text.toString(),
                        edtpor3.text.toString(),
                        edtpor4.text.toString(),
                        edtNotaFinal.text.toString()
                    )
                    // Muestra un mensaje de Toast corto
                    Toast.makeText(this, "Se guardo el curso", Toast.LENGTH_SHORT).show()
                }else{
                    // Muestra un mensaje de Toast corto
                    Toast.makeText(this, "No puede dejar el nombre en Blanco", Toast.LENGTH_SHORT).show()

                }

                //val notasGuardadas = databaseHelper.obtenerNotas()
            }else{
                val databaseHelper = DatabaseHelper(this) // Aquí usamos el contexto de la actividad
                if(!edtNombre.text.toString().isEmpty()){
                    databaseHelper.actualizarNotas(
                        id,
                        edtNombre.text.toString(),
                        edtnota1.text.toString(),
                        edtnota2.text.toString(),
                        edtnota3.text.toString(),
                        edtnota4.text.toString(),
                        edtpor1.text.toString(),
                        edtpor2.text.toString(),
                        edtpor3.text.toString(),
                        edtpor4.text.toString(),
                        edtNotaFinal.text.toString()
                    )
                    // Muestra un mensaje de Toast corto
                    Toast.makeText(this, "Se guardo el curso", Toast.LENGTH_SHORT).show()
                }else{
                    // Muestra un mensaje de Toast corto
                    Toast.makeText(this, "No puede dejar el nombre en Blanco", Toast.LENGTH_SHORT).show()

                }

            }

        }

        btnEliminar.setOnClickListener {
            val databaseHelper = DatabaseHelper(this) // Aquí usamos el contexto de la actividad
            databaseHelper.eliminarNotaPorId(id)
            // Muestra un mensaje de Toast corto
            Toast.makeText(this, "Se elimino el curso", Toast.LENGTH_SHORT).show()

            // Cambiamos de actividad
            val intent = Intent(this, Lista::class.java)

            // Iniciar ActividadB
            startActivity(intent)

            //Ejecutamos animacion
            overridePendingTransition(R.anim.left_in, R.anim.left_out)

            finish()
        }

    }

    private fun calcularNotaFinal(edtpor1: EditText, edtpor2: EditText, edtpor3: EditText, edtpor4: EditText,
                                  edtnota1: EditText, edtnota2: EditText, edtnota3: EditText, edtnota4: EditText,
                                  edtNotaFinal: EditText) {
        // Convierte el texto a Float solo si es un número válido, de lo contrario, utiliza 0 como valor predeterminado
        val por1 = edtpor1.text.toString().toFloatOrNull() ?: 0f
        val por2 = edtpor2.text.toString().toFloatOrNull() ?: 0f
        val por3 = edtpor3.text.toString().toFloatOrNull() ?: 0f
        val por4 = edtpor4.text.toString().toFloatOrNull() ?: 0f
        val nota1 = edtnota1.text.toString().toFloatOrNull() ?: 0f
        val nota2 = edtnota2.text.toString().toFloatOrNull() ?: 0f
        val nota3 = edtnota3.text.toString().toFloatOrNull() ?: 0f
        val nota4 = edtnota4.text.toString().toFloatOrNull() ?: 0f
        // Calcula la nota final sumando todos los valores obtenidos
        val notaFinal = (por1/100 * nota1) + (por2/100 * nota2) + (por3/100 * nota3) + (por4/100 * nota4)

        // Actualiza el EditText edtNotaFinal con la nota final calculada
        edtNotaFinal.setText(String.format("%.2f", notaFinal))

        //Mensaje
        if(notaFinal>=10.5f){
            showCustomDialog()
            textFelicidades.setTextColor(Color.WHITE)
            textFelicidades.setText("FELICIDADES APROBASTE")
        }else{
            textFelicidades.setTextColor(Color.RED)
            textFelicidades.setText("LO SIENTO JALASTE")
        }

    }

    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "NotasDB"
            private const val TABLE_NOTAS = "notas"
            private const val COLUMN_ID = "id"
            private const val COLUMN_NOMBRE = "nombre"
            private const val COLUMN_NOTA1 = "nota1"
            private const val COLUMN_NOTA2 = "nota2"
            private const val COLUMN_NOTA3 = "nota3"
            private const val COLUMN_NOTA4 = "nota4"
            private const val COLUMN_PROMEDIO1 = "promedio1"
            private const val COLUMN_PROMEDIO2 = "promedio2"
            private const val COLUMN_PROMEDIO3 = "promedio3"
            private const val COLUMN_PROMEDIO4 = "promedio4"
            private const val COLUMN_NOTA_FINAL = "notaFinal"
        }

        override fun onCreate(db: SQLiteDatabase) {
            val CREATE_NOTAS_TABLE = ("CREATE TABLE $TABLE_NOTAS ("
                    + "$COLUMN_ID INTEGER PRIMARY KEY,"
                    + "$COLUMN_NOMBRE VARCHAR(150),"
                    + "$COLUMN_NOTA1 VARCHAR(4),"
                    + "$COLUMN_NOTA2 VARCHAR(4),"
                    + "$COLUMN_NOTA3 VARCHAR(4),"
                    + "$COLUMN_NOTA4 VARCHAR(4),"
                    + "$COLUMN_PROMEDIO1 VARCHAR(4),"
                    + "$COLUMN_PROMEDIO2 VARCHAR(4),"
                    + "$COLUMN_PROMEDIO3 VARCHAR(4),"
                    + "$COLUMN_PROMEDIO4 VARCHAR(4),"
                    + "$COLUMN_NOTA_FINAL VARCHAR(4))")
            db.execSQL(CREATE_NOTAS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTAS")
            onCreate(db)
        }

        fun agregarNotas(nombre: String, nota1: String, nota2: String, nota3: String, nota4: String,
                         promedio1: String, promedio2: String, promedio3: String, promedio4: String,
                         notaFinal: String) {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_NOMBRE, nombre)
            values.put(COLUMN_NOTA1, nota1)
            values.put(COLUMN_NOTA2, nota2)
            values.put(COLUMN_NOTA3, nota3)
            values.put(COLUMN_NOTA4, nota4)
            values.put(COLUMN_PROMEDIO1, promedio1)
            values.put(COLUMN_PROMEDIO2, promedio2)
            values.put(COLUMN_PROMEDIO3, promedio3)
            values.put(COLUMN_PROMEDIO4, promedio4)
            values.put(COLUMN_NOTA_FINAL, notaFinal)
            db.insert(TABLE_NOTAS, null, values)
            db.close()
        }

        fun obtenerNotas(): List<Nota> {
            val notas = mutableListOf<Nota>()
            val selectQuery = "SELECT * FROM $TABLE_NOTAS"
            val db = this.readableDatabase
            val cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, null)
            } catch (e: Exception) {
                e.printStackTrace()
                db.execSQL(selectQuery)
                return emptyList()
            }

            if (cursor != null && cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
                val por1Index = cursor.getColumnIndex(COLUMN_PROMEDIO1)
                val por2Index = cursor.getColumnIndex(COLUMN_PROMEDIO2)
                val por3Index = cursor.getColumnIndex(COLUMN_PROMEDIO3)
                val por4Index = cursor.getColumnIndex(COLUMN_PROMEDIO4)
                val nota1Index = cursor.getColumnIndex(COLUMN_NOTA1)
                val nota2Index = cursor.getColumnIndex(COLUMN_NOTA2)
                val nota3Index = cursor.getColumnIndex(COLUMN_NOTA3)
                val nota4Index = cursor.getColumnIndex(COLUMN_NOTA4)
                val notaFinalIndex = cursor.getColumnIndex(COLUMN_NOTA_FINAL)

                do {
                    val id = cursor.getInt(idIndex)
                    val nombre = cursor.getString(nombreIndex)
                    val por1 = cursor.getString(por1Index)
                    val por2 = cursor.getString(por2Index)
                    val por3 = cursor.getString(por3Index)
                    val por4 = cursor.getString(por4Index)
                    val nota1 = cursor.getString(nota1Index)
                    val nota2 = cursor.getString(nota2Index)
                    val nota3 = cursor.getString(nota3Index)
                    val nota4 = cursor.getString(nota4Index)
                    val notaFinal = cursor.getString(notaFinalIndex)

                    val nota = Nota(id, nombre, por1, por2, por3, por4, nota1, nota2, nota3, nota4, notaFinal)
                    notas.add(nota)
                } while (cursor.moveToNext())
            }

            cursor?.close()
            db.close()

            return notas
        }


        fun obtenerNotasPorId(id: Int): String {
            val notas = StringBuilder()
            val selectQuery = "SELECT * FROM $TABLE_NOTAS WHERE $COLUMN_ID = ?"
            val db = this.readableDatabase
            val cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))
            } catch (e: Exception) {
                e.printStackTrace()
                db.execSQL(selectQuery)
                return "Error al obtener las notas."
            }

            if (cursor != null && cursor.columnCount > 0) {
                val nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
                val nota1Index = cursor.getColumnIndex(COLUMN_NOTA1)
                val nota2Index = cursor.getColumnIndex(COLUMN_NOTA2)
                val nota3Index = cursor.getColumnIndex(COLUMN_NOTA3)
                val nota4Index = cursor.getColumnIndex(COLUMN_NOTA4)
                val promedio1Index = cursor.getColumnIndex(COLUMN_PROMEDIO1)
                val promedio2Index = cursor.getColumnIndex(COLUMN_PROMEDIO2)
                val promedio3Index = cursor.getColumnIndex(COLUMN_PROMEDIO3)
                val promedio4Index = cursor.getColumnIndex(COLUMN_PROMEDIO4)
                val notaFinalIndex = cursor.getColumnIndex(COLUMN_NOTA_FINAL)

                if (nombreIndex != -1 && nota1Index != -1 && nota2Index != -1 && nota3Index != -1 && nota4Index != -1 &&
                    promedio1Index != -1 && promedio2Index != -1 && promedio3Index != -1 && promedio4Index != -1 &&
                    notaFinalIndex != -1) {
                    if (cursor.moveToFirst()) {
                        do {
                            val nombre = cursor.getString(nombreIndex)
                            val nota1 = cursor.getInt(nota1Index)
                            val nota2 = cursor.getInt(nota2Index)
                            val nota3 = cursor.getInt(nota3Index)
                            val nota4 = cursor.getInt(nota4Index)
                            val promedio1 = cursor.getInt(promedio1Index)
                            val promedio2 = cursor.getInt(promedio2Index)
                            val promedio3 = cursor.getInt(promedio3Index)
                            val promedio4 = cursor.getInt(promedio4Index)
                            val notaFinal = cursor.getInt(notaFinalIndex)

                            notas.append("Nombre: $nombre, ID: $id, Nota1: $nota1, Nota2: $nota2, Nota3: $nota3, Nota4: $nota4, Promedio1: $promedio1, Promedio2: $promedio2, Promedio3: $promedio3, Promedio4: $promedio4, Nota Final: $notaFinal\n")
                        } while (cursor.moveToNext())
                    }
                } else {
                    Log.e("Error", "Error al obtener los índices de las columnas.")
                }
            } else {
                Log.e("Error", "Cursor es nulo o no tiene columnas.")
            }

            cursor?.close()
            db.close()

            return notas.toString()
        }

        fun actualizarNotas(id: Int, nombre: String, nota1: String, nota2: String, nota3: String, nota4: String,
                            promedio1: String, promedio2: String, promedio3: String, promedio4: String,
                            notaFinal: String): Int {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_NOMBRE, nombre)
            values.put(COLUMN_NOTA1, nota1)
            values.put(COLUMN_NOTA2, nota2)
            values.put(COLUMN_NOTA3, nota3)
            values.put(COLUMN_NOTA4, nota4)
            values.put(COLUMN_PROMEDIO1, promedio1)
            values.put(COLUMN_PROMEDIO2, promedio2)
            values.put(COLUMN_PROMEDIO3, promedio3)
            values.put(COLUMN_PROMEDIO4, promedio4)
            values.put(COLUMN_NOTA_FINAL, notaFinal)

            // Actualiza los datos en la base de datos
            val resultado = db.update(TABLE_NOTAS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))

            db.close()

            return resultado
        }

        fun eliminarNotaPorId(id: Int): Int {
            val db = this.writableDatabase

            // Elimina la fila con el ID proporcionado
            val resultado = db.delete(TABLE_NOTAS, "$COLUMN_ID = ?", arrayOf(id.toString()))

            db.close()

            return resultado
        }
    }

    private fun validarNotasEditText(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Verifica si el texto es un número válido
                val nota = s.toString().toFloatOrNull() ?: return

                // Valida si la nota está dentro del rango permitido
                if (nota > 20) {
                    edt.error = "La nota no puede ser mayor a 20"
                    // Puedes establecer el valor máximo permitido aquí o dejarlo en 20
                    edt.setText("20")
                } else if (nota < 0) {
                    edt.error = "La nota no puede ser menor a 0"
                    // Puedes establecer el valor mínimo permitido aquí o dejarlo en 0
                    edt.setText("0")
                }
            }
        })
    }

    private fun validarPorcentajesEditText(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Verifica si el texto es un número válido
                val porcentaje = s.toString().toFloatOrNull() ?: return
                val sumaPorcentajes = obtenerSumaPorcentajes()

                // Valida si la suma de los porcentajes es mayor a 100
                if (sumaPorcentajes > 100) {
                    edt.error = "La suma de los porcentajes no puede ser mayor a 100"
                    // Puedes establecer el valor máximo permitido aquí o dejarlo en 0
                    edt.setText("")
                }
            }
        })
    }

    // Función para obtener la suma de los porcentajes de todos los EditText
    private fun obtenerSumaPorcentajes(): Float {
        val por1 = edtpor1.text.toString().toFloatOrNull() ?: 0f
        val por2 = edtpor2.text.toString().toFloatOrNull() ?: 0f
        val por3 = edtpor3.text.toString().toFloatOrNull() ?: 0f
        val por4 = edtpor4.text.toString().toFloatOrNull() ?: 0f
        return por1 + por2 + por3 + por4
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_cherri, null)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)

        val lottieAnimationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Opcional: Configurar la animación manualmente
        // lottieAnimationView.setAnimation(R.raw.your_lottie_file)
        // lottieAnimationView.playAnimation()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Opcional: Configurar la posición del diálogo
        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.x = 0
        params.y = 0
        dialog.window!!.attributes = params

        dialog.show()

        // Cerrar el diálogo automáticamente después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 3000) // 3000 milisegundos = 3 segundos
    }
}