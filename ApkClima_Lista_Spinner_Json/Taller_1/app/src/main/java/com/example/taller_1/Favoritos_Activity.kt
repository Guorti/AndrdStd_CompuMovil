package com.example.taller_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class Favoritos_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        var arreglo : MutableList<String> = ArrayList()
        val listView = findViewById<ListView>(R.id.listaFavoritos)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, arreglo)
        listView.adapter = adapter


        for(i in 0 until MainActivity.favoritosList.size){
            arreglo.add(MainActivity.favoritosList.get(i).getNombreDestino().toString())
        }

    }


}