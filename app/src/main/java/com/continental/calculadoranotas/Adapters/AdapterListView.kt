package com.continental.calculadoranotas.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.continental.calculadoranotas.Entities.Nota
import com.continental.calculadoranotas.Lista
import com.continental.calculadoranotas.MainActivity
import com.continental.calculadoranotas.R

class AdapterListView (private val context: Context, private val dataList: List<Nota>) : BaseAdapter() {
    // animacion
    val jumpAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.animation1)
    // Variable para controlar si se está procesando un clic en algún elemento
    private var isClickProcessing = false

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listaitem, parent, false)
        }

        val item = getItem(position) as Nota
        val textViewTitle = convertView!!.findViewById<TextView>(R.id.textViewTitle)
        val crdItem = convertView!!.findViewById<CardView>(R.id.crdItem)
        val textNota = convertView!!.findViewById<TextView>(R.id.txtnota)

        textViewTitle.text = item.nombre
        textNota.text = item.notaFinal

        // Agregar un OnClickListener al convertView para manejar el clic
        convertView.setOnClickListener {
            // Verificar si ya se está procesando un clic en otro elemento
            if (!isClickProcessing) {
                // Deshabilitar los clics en otros elementos convertView
                isClickProcessing = true

                // Crear un Intent para iniciar la actividad DetallesActividad
                crdItem.startAnimation(jumpAnimation)

                // Creamos un nuevo Handler
                val handler = Handler()

                // Tiempo de espera en milisegundos (ejemplo: 3 segundos)
                val tiempoEspera = 450L

                // Creamos un Runnable que se ejecutará después del tiempo de espera
                val runnable = Runnable {
                    // Este código se ejecutará después del tiempo de espera
                    // Por ejemplo, aquí podemos iniciar una nueva actividad
                    val intent = Intent(context, MainActivity::class.java)

                    // Agregar todos los datos relevantes al Intent
                    intent.putExtra("id", item.id)
                    intent.putExtra("nombre", item.nombre)
                    intent.putExtra("por1", item.por1)
                    intent.putExtra("por2", item.por2)
                    intent.putExtra("por3", item.por3)
                    intent.putExtra("por4", item.por4)
                    intent.putExtra("nota1", item.nota1)
                    intent.putExtra("nota2", item.nota2)
                    intent.putExtra("nota3", item.nota3)
                    intent.putExtra("nota4", item.nota4)
                    intent.putExtra("notaFinal", item.notaFinal)

                    context.startActivity(intent)
                    // Establecer las animaciones de transición
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.left_in, R.anim.left_out)
                    }
                    // Volver a habilitar el clic del convertView después de iniciar la actividad
                    isClickProcessing = false
                }

                // Programamos la ejecución del Runnable después del tiempo de espera
                handler.postDelayed(runnable, tiempoEspera)
            }
        }

        return convertView
    }

}