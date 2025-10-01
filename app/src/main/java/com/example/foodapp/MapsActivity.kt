package com.example.foodapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.api.MapsApi
import com.example.foodapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1
        private const val LOCATION_UPDATE_INTERVAL = 5000L
    }

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    private var currentLatLng: LatLng? = null
    private var searchMarker: Marker? = null
    private var routePolyline: Polyline? = null

    private val mapsApi by lazy { MapsApi(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermissionIfNeeded()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initially hide buttons
        binding.btnStart.visibility = View.GONE
        binding.btnStop.visibility = View.GONE
        binding.btnCross.visibility = View.GONE

        // Button listeners
        binding.btnStart.setOnClickListener {
            startLocationUpdates()
            binding.btnStart.visibility = View.GONE
            binding.btnStop.visibility = View.VISIBLE
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnCross.setOnClickListener {
            clearSearch()
        }

        binding.btnStop.setOnClickListener {
            stopLocationUpdates()
            binding.btnStop.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }
    }

    private fun clearSearch() {
        searchMarker?.remove()
        searchMarker = null

        routePolyline?.remove()
        routePolyline = null

        stopLocationUpdates()

        binding.btnCross.visibility = View.GONE
        binding.btnStart.visibility = View.GONE
        binding.btnStop.visibility = View.GONE

        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
    }


    private fun requestLocationPermissionIfNeeded() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), LOCATION_PERMISSION_REQUEST)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (hasLocationPermission()) {
            mMap.isMyLocationEnabled = true // show blue dot
            showCurrentLocationOnce()
        }
        initSearchView()
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) searchLocation(query)
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        binding.searchView.isIconified = false
    }

    private fun searchLocation(locationName: String) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(locationName, 1)
            if (!addresses.isNullOrEmpty()) {
                val destLatLng = LatLng(addresses[0].latitude, addresses[0].longitude)

                // Place search marker
                searchMarker?.remove()
                searchMarker = mMap.addMarker(MarkerOptions().position(destLatLng).title(locationName))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15f))

                // Enable Start button after a successful search
                binding.btnStart.visibility = View.VISIBLE
                binding.btnCross.visibility = View.VISIBLE

                val origin = currentLatLng
                if (origin != null) fetchAndDrawRoute(origin, destLatLng)
                else {
                    try {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener { loc: Location? ->
                            if (loc != null) {
                                val originLatLng = LatLng(loc.latitude, loc.longitude)
                                currentLatLng = originLatLng
                                fetchAndDrawRoute(originLatLng, destLatLng)
                            } else Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show()
                        }
                    } catch (se: SecurityException) {
                        se.printStackTrace()
                        Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAndDrawRoute(origin: LatLng, dest: LatLng) {
        lifecycleScope.launch {
            val route = mapsApi.fetchRoute(origin, dest)
            withContext(Dispatchers.Main) {
                if (route.isNotEmpty()) drawRouteOnMap(route)
                else Toast.makeText(this@MapsActivity, "Route not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun drawRouteOnMap(route: List<LatLng>) {
        routePolyline?.remove()
        val options = PolylineOptions()
            .addAll(route)
            .width(12f)
            .color(android.graphics.Color.BLUE)
            .geodesic(true)
        routePolyline = mMap.addPolyline(options)
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    private fun showCurrentLocationOnce() {
        try {
            if (!hasLocationPermission()) return
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    currentLatLng = latLng
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun startLocationUpdates() {
        try {
            if (!hasLocationPermission()) return
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL
            ).build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.lastOrNull()?.let { updateMapLocation(it) }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback!!,
                mainLooper
            )
            Toast.makeText(this, "Location updates started", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
            locationCallback = null
            Toast.makeText(this, "Location updates stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        currentLatLng = latLng
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (requestCode == LOCATION_PERMISSION_REQUEST && results.any { it == PackageManager.PERMISSION_GRANTED }) {
            mMap.isMyLocationEnabled = true
            showCurrentLocationOnce()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates() // auto stop on pause
    }
}
