package com.example.gps_practica

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ejercicio_1.Datos
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    //CAMBIOS POSICION
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtViewLatitud = findViewById<TextView>(R.id.textViewLatitud)
        val txtViewLongitud = findViewById<TextView>(R.id.textViewLongitud)
        val txtViewDistanciaDorado = findViewById<TextView>(R.id.textViewDistancia)


        // Initialize the FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        permisoUbicacion()

        //NOTIFICACION CAMBIOS POSICION
        mLocationRequest = createLocationRequest()

        //DISTANCIA A EL DORADO
        val latitudDorado = 4.7010
        val longitudDorado = -74.1461


        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.i("LOCATION", "Location update in the callback: $location")
                if (location != null) {
                    txtViewLatitud.text = location.latitude.toString()
                    txtViewLongitud.text = location.longitude.toString()

                    txtViewDistanciaDorado.text = "Distancia a el Dorado: " + distance(location.latitude, location.longitude, latitudDorado, longitudDorado) + "Km"

                }
            }
        }

        //Mapa google
        val btnMap = findViewById<Button>(R.id.buttonMap)

        btnMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


    }

    //NOTIFICACION CAMBIOS POSICION
    //Despues en cualquier otro lugar
    private fun createLocationRequest(): LocationRequest =
    // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).apply {
            setMinUpdateIntervalMillis(5000)
        }.build()

    //NOTIFICACION CAMBIOS POSICION

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    /*
    //FUNCION DE LOCALIZACION CON INTERVALOS DE TIEMPO
    private fun startLocationUpdates() {
    val locationRequest = LocationRequest.create()
            .setInterval(UPDATE_INTERVAL) // Set desired location update interval in milliseconds
            .setFastestInterval(FASTEST_INTERVAL) // Set minimum interval for updates (optional)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // Set location accuracy preference

        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
    }

     */

    //DETENER SUSCRIPCION LOCALIZACION
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    //RESUMIR SUSCRIPCION LOCALIZACION
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun permisoUbicacion(){

        val txtViewLatitud = findViewById<TextView>(R.id.textViewLatitud)
        val txtViewLongitud = findViewById<TextView>(R.id.textViewLongitud)

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                //performAction(...)
                //Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show()
                mFusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
                    Log.i("LOCATION", "onSuccess location")
                    if (location != null) {
                        Log.i("LOCATION", "Longitud: " + location.longitude)
                        Log.i("LOCATION", "Latitud: " + location.latitude)

                        txtViewLatitud.text = location.latitude.toString()
                        txtViewLongitud.text = location.longitude.toString()

                    }
                }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined.
                //showInContextUI(...)
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Datos.MY_PERMISSION_REQUEST_LOCATION)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Datos.MY_PERMISSION_REQUEST_LOCATION)
            }
        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //val textV = findViewById<TextView>(R.id.textView)
        when (requestCode) {
            Datos.MY_PERMISSION_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    //Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show()



                } else {
                    // Explain to the user that the feature is unavailable
                }
                return
            }

            else -> {
                // Ignore all other requests.

            }
        }
    }


    fun distance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val RADIUS_OF_EARTH_KM = 6371

        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(long1 - long2)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val result = RADIUS_OF_EARTH_KM * c
        return (result * 100.0).roundToInt() / 100.0
    }

}