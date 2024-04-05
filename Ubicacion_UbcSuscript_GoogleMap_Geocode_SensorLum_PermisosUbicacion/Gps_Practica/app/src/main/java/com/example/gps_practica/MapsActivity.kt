package com.example.gps_practica

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gps_practica.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    //private lateinit var binding: ActivityMapsBinding


    //SENSORES
    private lateinit var mSensorManager: SensorManager
    private lateinit var mLightSensor: Sensor
    private lateinit var mLightSensorListener: SensorEventListener

    //Geocoder
    val mGeocoder = Geocoder(baseContext)
    val editText = findViewById<EditText>(R.id.editTextText)
    val addressString = editText.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = ActivityMapsBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (addressString.isNotEmpty()) {
                    try {
                        val addresses = mGeocoder.getFromLocationName(addressString, 2)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val addressResult = addresses[0]
                            val position = LatLng(addressResult.latitude, addressResult.longitude)
                            if (mMap != null) {
                                //Agregar Marcador al mapa
                            } else {
                                Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this, "La dirección esta vacía", Toast.LENGTH_SHORT).show()
                }

            }
            true

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Buscar por nombre
        val mGeocoder = Geocoder(baseContext)

        mMap!!.uiSettings.isZoomGesturesEnabled = true

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val MK1 = LatLng(-42.0, 151.0)
        val MK2 = LatLng(-38.0, 151.0)
        val MK3 = LatLng(-36.0, 151.0)
        val MK4 = LatLng(-34.0, 151.0)
        val MK5 = LatLng(-32.0, 151.0)


        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(10F))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val Marcador1 = mMap!!.addMarker(
            MarkerOptions().position(sydney).title("Marca 1")
                .snippet("algo 1")
                .alpha(0.5f)
        )

        val Marcador2 = mMap!!.addMarker(
            MarkerOptions().position(MK1).title("Marca 2")
                .snippet("algo 2")
                .alpha(1f)
        )

        val Marcador3 = mMap!!.addMarker(
            MarkerOptions().position(MK2).title("Marca 3")
                .snippet("algo 3")
                .alpha(1f)
                .icon(bitmapDescriptorFromVector(this, R.drawable.baseline_accessible_forward_24))
        )

        val Marcador4 = mMap!!.addMarker(
            MarkerOptions().position(MK3).title("Marca 2")
                .snippet("algo 2")
                .alpha(1f)
        )

        val Marcador5 = mMap!!.addMarker(
            MarkerOptions().position(MK4).title("Marca 2")
                .snippet("algo 2")
                .alpha(1f)
        )

        Marcador2!!.remove()

        mMap!!.clear()

//Icono azul BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        Marcador1!!.isVisible = false
        Marcador2!!.isVisible = false
        Marcador3!!.isVisible = true


        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_silver))


        //SENSORES
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!

        mLightSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (mMap != null) {
                    if (event!!.values[0] < 5000) {
                        Log.i("MAPS", "DARK MAP " + event.values[0])
                        mMap!!.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                baseContext,
                                R.raw.map_style_silver
                            )
                        )
                    } else {
                        Log.i("MAPS", "LIGHT MAP " + event.values[0])
                        mMap!!.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                baseContext,
                                R.raw.map_style_default
                            )
                        )
                    }

                }


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                TODO("Not yet implemented")
            }

        }

    }



    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
