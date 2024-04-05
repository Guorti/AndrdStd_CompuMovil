package com.example.taller_1

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat


class Detalles_Destino_Activity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_destino)


        val boundle = intent.extras
        val txtViewNombre = findViewById<TextView>(R.id.textViewNombreDes)
        val txtViewPais = findViewById<TextView>(R.id.textViewNombrePa)
        val txtViewCat = findViewById<TextView>(R.id.textViewNombreCat)
        val txtViewPlan = findViewById<TextView>(R.id.textViewNombrePlan)
        val txtViewCosto = findViewById<TextView>(R.id.textViewCosto)
        val btnAddFav = findViewById<Button>(R.id.buttonAddFav)

        fetchWeatherData(boundle?.getString("nombreSelec").toString())

        txtViewNombre.setText(boundle?.getString("nombreSelec"))
        txtViewPais.setText(boundle?.getString("paisSelec"))
        txtViewCat.setText(boundle?.getString("catSelec"))
        txtViewPlan.setText(boundle?.getString("planSelec"))
        txtViewCosto.setText("USD " + boundle?.getString("precioSelec"))

        for(i in 0 until MainActivity.favoritosList.size){ if(txtViewNombre.text.toString() == MainActivity.favoritosList.get(i).getNombreDestino()){
                btnAddFav.visibility = View.GONE
            }
        }

        btnAddFav.setOnClickListener(){ MainActivity.favoritosList.add(DestinoTuristico(txtViewNombre.text.toString(), txtViewPais.text.toString(), txtViewCat.text.toString(), txtViewPlan.text.toString(), txtViewCosto.text.toString()))
            Toast.makeText(baseContext, "Añadido a Favoritos", Toast.LENGTH_LONG).show()
            btnAddFav.visibility = View.GONE
        }

    }




    fun fetchWeatherData(nombreQuery : String){
        val txtViewCiudadC = findViewById<TextView>(R.id.textViewNombreCiudad)
        val txtViewRegionC = findViewById<TextView>(R.id.textViewNombreRegion)
        val txtViewPaisC = findViewById<TextView>(R.id.textViewValorNombrePaisClima)
        val txtViewHoraC = findViewById<TextView>(R.id.textViewHoraLocal)
        val txtViewTemperaturaC = findViewById<TextView>(R.id.textViewTemperatura)
        val txtViewSensacionC = findViewById<TextView>(R.id.textViewSensacion)

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weatherapi.com/v1/").build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(nombreQuery, "f5857345c87c41f486b01214242502")
        response.enqueue(object : Callback<WeatherApp> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){

                    txtViewCiudadC.setText(responseBody.location.name)
                    txtViewRegionC.setText(responseBody.location.region)
                    txtViewPaisC.setText(responseBody.location.country)
                    txtViewHoraC.setText(responseBody.location.localtime)
                    txtViewTemperaturaC.setText(responseBody.current.temp_c.toString() + "ªC")
                    txtViewSensacionC.setText("Se siente: " + responseBody.current.feelslike_c.toString() + "ªC")


                    val temperature = responseBody.current.temp_c
                    Log.i("TEMP", temperature.toString())
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.i("FAIL", "Ha fallado")
                Log.e("API_RESPONSE", "Error fetching weather data", t)
            }

        })
    }

}