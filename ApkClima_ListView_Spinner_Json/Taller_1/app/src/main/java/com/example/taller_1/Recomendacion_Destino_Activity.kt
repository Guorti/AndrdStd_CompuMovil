package com.example.taller_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class Recomendacion_Destino_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendacion_destino)

        val boundle = intent.extras!!.getInt("Indice")
        val txtViewNombre = findViewById<TextView>(R.id.textViewNombreRec)
        val txtViewPais = findViewById<TextView>(R.id.textViewPaisRec)
        val txtViewCat = findViewById<TextView>(R.id.textViewCategoriaRec)
        val txtViewPlan = findViewById<TextView>(R.id.textViewPlanRec)
        val txtViewCosto = findViewById<TextView>(R.id.textViewCostoRec)

        if(boundle == -1){ txtViewNombre.setText("NA")
            txtViewPais.visibility = View.GONE
            txtViewCat.visibility = View.GONE
            txtViewPlan.visibility = View.GONE
            txtViewCosto.visibility = View.GONE
        } else { txtViewNombre.setText(MainActivity.favoritosList[boundle].getNombreDestino())
            txtViewPais.setText(MainActivity.favoritosList[boundle].getPaisDestino())
            txtViewCat.setText(MainActivity.favoritosList[boundle].getCategoriaDestino())
            txtViewPlan.setText(MainActivity.favoritosList[boundle].getPlanDestino())
            txtViewCosto.setText("USD " + MainActivity.favoritosList[boundle].getPrecioDestino())
        }
    }
}