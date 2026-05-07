package mx.edu.itesca.fridapawls_2026.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

class LocationHelper(private val context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onResult: (Double, Double) -> Unit) {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onResult(it.latitude, it.longitude)
                }
            }
    }
}