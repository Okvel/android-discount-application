package by.bsuir.levko.salesby.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import by.bsuir.levko.salesby.R
import by.bsuir.levko.salesby.task.MarkerDataParserAsyncTask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : OnMapReadyCallback, AppCompatActivity() {

    companion object {
        const val LOCATION_UPDATE_MIN_DISTANCE = 10f
        const val LOCATION_UPDATE_MIN_TIME = 5000L
        const val INTERNET_PERMISSION_CODE = 101
        const val GPS_LOCATION_PERMISSION_CODE = 102
        const val INTERNET_LOCATION_PERMISSION_CODE = 103
    }

    private lateinit var locationManager: LocationManager
    private lateinit var map: GoogleMap
    private var locationPermitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val infoWindowClickListener = GoogleMap.OnInfoWindowClickListener {
        val intent = Intent(this, ShopSaleActivity::class.java)
        intent.putExtra("shopName", it.title)
        startActivity(intent)

    }

    override fun onMapReady(map: GoogleMap?) {
        val parser = MarkerDataParserAsyncTask()
        parser.execute()
        val items = parser.get()
        if (items != null && map != null) {
            this.map = map
            map.setOnInfoWindowClickListener(infoWindowClickListener)
            for (item in items) {
                map.addMarker(MarkerOptions().position(item.position).title(item.name).snippet(item.address))
            }

            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), INTERNET_PERMISSION_CODE)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            locationPermitted = false
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GPS_LOCATION_PERMISSION_CODE)
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), INTERNET_LOCATION_PERMISSION_CODE)
        } else {
            setLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            GPS_LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.contains(PERMISSION_GRANTED) && !locationPermitted) {
                    setLocation()
                }
                return
            }
            INTERNET_LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.contains(PERMISSION_GRANTED) && !locationPermitted) {
                    setLocation()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocation() {
        map.isMyLocationEnabled = true
        map.uiSettings?.isMyLocationButtonEnabled = true
        map.uiSettings?.setAllGesturesEnabled(true)
        getCurrentLocation()
        locationPermitted = true
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        var location: Location? = null
        if (!(isGPSEnabled || isNetworkEnabled)) {

        } else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener)
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
        }

        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16f))
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {

        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }
}
