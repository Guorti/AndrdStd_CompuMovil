package com.example.taller_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class Explorar_Destinos_Activity : AppCompatActivity() {

    var arreglo : MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorar_destinos)

        val boundle = intent.extras?.getString("Categoria")
        //Log.i("FLAG2", boundle.toString())
        val listView = findViewById<ListView>(R.id.listaDestinos)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, arreglo)
        val bundle = Bundle()
        val intentDetDest = Intent(this, Detalles_Destino_Activity::class.java)

        listView.adapter = adapter



        llenarArreglo(arreglo, boundle.toString())

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                llenarBundle(bundle, parent?.getItemAtPosition(position).toString())
                intentDetDest.putExtras(bundle)
                startActivity(intentDetDest)
            }
        })
    }

    fun loadJSONFromAsset(): String?{
        var json: String? = null
        try {
            val istream: InputStream = assets.open("destinos.json")
            val size: Int = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun llenarArreglo(arreglo : MutableList<String>, boundle : String){
        val json = JSONObject(loadJSONFromAsset())
        val destinosJson = json.getJSONArray("destinos")

        for(i in 0 until destinosJson.length()){
            val jsonObject =  destinosJson.getJSONObject(i)
            if(boundle == "Todos"){
                arreglo.add(jsonObject.getString("nombre"))
            }
            else if(jsonObject.getString("categoria") == boundle){
                arreglo.add(jsonObject.getString("nombre"))
            }
        }
    }

    fun llenarBundle(bundle : Bundle, itemSelecc : String){
        val json = JSONObject(loadJSONFromAsset())
        val destinosJson = json.getJSONArray("destinos")

        bundle.putString("nombreSelec", itemSelecc)
        for(i in 0 until destinosJson.length()){
            val jsonObject =  destinosJson.getJSONObject(i)
            if(jsonObject.getString("nombre") == itemSelecc){
                bundle.putString("paisSelec", jsonObject.getString("pais"))
                bundle.putString("catSelec", jsonObject.getString("categoria"))
                bundle.putString("planSelec", jsonObject.getString("plan"))
                bundle.putString("precioSelec", jsonObject.getString("precio"))
            }
        }
    }



}