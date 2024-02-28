package work.time.worktim.ui.fragment

import androidx.navigation.fragment.findNavController
import work.time.worktim.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION", "SameParameterValue")
class MapKitFragment : Fragment(), LocationListener {

    private lateinit var mapView: MapView
    private lateinit var progressBar: ProgressBar
    private lateinit var tv: TextView
    private var isFragmentTransactionExecuted = false
    private lateinit var locationManager: LocationManager
    private lateinit var googleMap: GoogleMap
    private lateinit var gpsUtils: GpsUtils

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map_kit, container, false)
        mapView = rootView.findViewById(R.id.mapKit)
        progressBar = rootView.findViewById(R.id.progressBar)
        tv = rootView.findViewById(R.id.tvyyy)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        mapView.getMapAsync { map ->
            googleMap = map
            // You can customize the map settings here
        }

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsUtils = GpsUtils(requireContext())

        if (gpsUtils.isGpsEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L, // например, 5000 миллисекунд (5 секунд)
                    0.0f,
                    this
                )

            }
        } else {
            gpsUtils.buildGpsDialog()
        }

        return rootView
    }

    override fun onLocationChanged(location: Location) {
        val userLocation = LatLng(location.latitude, location.longitude)
        val targetLocation = LatLng(42.876815, 74.592473455)
//42.876815, 74.592473455
        if (!isFragmentTransactionExecuted && isWithinRadius(userLocation, targetLocation, 450.0)) {
            val navController = try {
                findNavController()
            } catch (e: IllegalStateException) {
                null
            }

            if (navController != null) {
                navController.navigate(R.id.scanerFragment)
                isFragmentTransactionExecuted = true
            }
        } else {
            if (!isFragmentTransactionExecuted) {
                Toast.makeText(
                    requireContext(),
                    "Вы находитесь не в указанном месте",
                    Toast.LENGTH_SHORT
                ).show()
                isFragmentTransactionExecuted = true
            }
        }
    }

    private fun isWithinRadius(userLocation: LatLng, targetLocation: LatLng, radius: Double): Boolean {
        val results = FloatArray(1)
        Location.distanceBetween(
            userLocation.latitude, userLocation.longitude,
            targetLocation.latitude, targetLocation.longitude,
            results
        )
        return results[0] <= radius
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start location updates
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L,
                        0.0f,
                        this
                    )
                } else {
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Dummy implementation, if needed
    }

    override fun onProviderEnabled(provider: String) {
        // Dummy implementation, if needed
    }

    override fun onProviderDisabled(provider: String) {
        // Dummy implementation, if needed
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}



/*

Да, определение местоположения иногда может быть медленным, особенно в помещении или в условиях плохой сети. В вашем коде можно внести некоторые улучшения для оптимизации и ускорения определения местоположения. Вот несколько предложений:

1. **Использование LocationManager.NETWORK_PROVIDER**: Вместо использования только `LocationManager.NETWORK_PROVIDER`, вы можете добавить и `LocationManager.GPS_PROVIDER`. Это может улучшить точность определения местоположения.

2. **Использование последнего известного местоположения**: Вместо ожидания обновлений местоположения с помощью `requestLocationUpdates`, вы можете сначала проверить последнее известное местоположение с помощью `getLastKnownLocation` и использовать его, если оно доступно.

3. **Уменьшение частоты запросов на обновление местоположения**: Установите больший интервал времени между запросами на обновление местоположения, чтобы уменьшить нагрузку на батарею и сеть.

4. **Отслеживание изменения только при необходимости**: Если ваше приложение не нуждается в постоянном отслеживании местоположения, вы можете останавливать запросы на обновление после того, как местоположение пользователя было определено.

5. **Оптимизация алгоритма проверки радиуса**: Вы можете оптимизировать ваш метод `isWithinRadius`, чтобы он работал более эффективно.

6. **Показ статуса ожидания**: Показывайте пользователю какой-либо индикатор ожидания, когда приложение пытается получить местоположение, чтобы пользователь понимал, что происходит.

Ниже пример, как можно внести изменения в ваш код:

```kotlin
class MapKitFragment : Fragment(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var lastKnownLocation: Location? = null
    private var isLocationPermissionGranted = false
    private var isWaitingForLocationUpdate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация интерфейса
        // Проверка разрешений и запрос обновлений местоположения
        return rootView
    }

    private fun requestLocationUpdates() {
        if (isLocationPermissionGranted) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L, // Интервал времени в миллисекундах
                0.0f,
                this
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000L,
                0.0f,
                this
            )
            isWaitingForLocationUpdate = true
        }
    }

    override fun onLocationChanged(location: Location) {
        lastKnownLocation = location
        if (isWaitingForLocationUpdate) {
            // Обработка нового местоположения
            isWaitingForLocationUpdate = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Обработка запроса разрешений
        // Запрос обновлений местоположения после получения разрешений
    }

    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted) {
            requestLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    // Остальные методы класса
}
```

Помните также обработать ситуации, когда местоположение не доступно или пользователь отказался предоставить разрешения.


private lateinit var mapView: MapView
private lateinit var progressBar: ProgressBar
private lateinit var tv : TextView
private var isFragmentTransactionExecuted = false
private lateinit var mapObjectCollection: MapObjectCollection
private lateinit var locationManager: LocationManager
private lateinit var gpsUtils: GpsUtils

@SuppressLint("MissingInflatedId")
override fun onCreateView(
 inflater: LayoutInflater, container: ViewGroup?,
 savedInstanceState: Bundle?
): View? {
 val rootView = inflater.inflate(R.layout.fragment_map_kit, container, false)
 mapView = rootView.findViewById(R.id.mapKit)
 progressBar = rootView.findViewById(R.id.progressBar)
 tv = rootView.findViewById(R.id.tvyyy)
 mapObjectCollection = mapView.map.mapObjects.addCollection()
 locationManager = MapKitFactory.getInstance().createLocationManager()
 gpsUtils = GpsUtils(requireContext())

 if (gpsUtils.isGpsEnabled()) {
     if (ActivityCompat.checkSelfPermission(
             requireContext(),
             Manifest.permission.ACCESS_FINE_LOCATION
         ) != PackageManager.PERMISSION_GRANTED
     ) {
         ActivityCompat.requestPermissions(
             requireActivity(),
             arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
             LOCATION_PERMISSION_REQUEST_CODE
         )
     } else {
         locationManager.subscribeForLocationUpdates(
             1000.0,
             0,
             0.0,
             false,
             FilteringMode.OFF,
             this
         )
     }
 } else {
     gpsUtils.buildGpsDialog()
 }

 return rootView
}

override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
}

override fun onLocationUpdated(location: Location) {
 val userLocation = Point(location.position.latitude, location.position.longitude)
 val targetLocation = Point(42.876815, 74.592473455)
 //42.876815, 74.592473455
 if (!isFragmentTransactionExecuted && isWithinRadius(userLocation, targetLocation, 100.0)) {
     // Check if the fragment is associated with an activity before navigating
     val navController = try {
         findNavController()
     } catch (e: IllegalStateException) {
         null
     }

     if (navController != null) {
         navController.navigate(R.id.scanerFragment)
         isFragmentTransactionExecuted = true
     }
 } else {
     if (!isFragmentTransactionExecuted) {
         Toast.makeText(requireContext(), "Вы находитесь не в указанном месте", Toast.LENGTH_SHORT).show()
         isFragmentTransactionExecuted = true
     }
 }
}


private fun isWithinRadius(userLocation: Point, targetLocation: Point, radius: Double): Boolean {
 val distance = SphericalUtil.computeDistanceBetween(
     LatLng(userLocation.latitude, userLocation.longitude),
     LatLng(targetLocation.latitude, targetLocation.longitude)
 )
 return distance <= radius
}


companion object {
 const val LOCATION_PERMISSION_REQUEST_CODE = 100
}

override fun onStart() {
 mapView.onStart()
 MapKitFactory.getInstance().onStart()
 super.onStart()
}

override fun onStop() {
 mapView.onStop()
 MapKitFactory.getInstance().onStop()
 super.onStop()
}
}

*/