package com.example.foodapp.api

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.json.JSONObject

class MapsApi(private val context: Context) {

    private val httpClient = HttpClient(CIO)

    private fun getOsrmUrl(origin: LatLng, dest: LatLng): String {
        // OSRM expects lon,lat format
        return "https://router.project-osrm.org/route/v1/driving/" +
                "${origin.longitude},${origin.latitude};${dest.longitude},${dest.latitude}" +
                "?overview=full&geometries=polyline"
    }

    suspend fun fetchRoute(origin: LatLng, dest: LatLng): List<LatLng> {
        val url = getOsrmUrl(origin, dest)
        Log.d("Url", "OSRM request: $url")

        return try {
            val response: String = httpClient.get(url).body()

            Log.d("MapsApi", "OSRM response: $response")

            val json = JSONObject(response)
            val routes = json.optJSONArray("routes")
            if (routes == null || routes.length() == 0) return emptyList()

            val overview = routes.getJSONObject(0).getString("geometry")
            decodePolyline(overview)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        var lat = 0
        var lng = 0
        while (index < encoded.length) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            poly.add(LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5))
        }
        return poly
    }
}
