package work.time.worktim.ui.fragment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

class GpsUtils(private val context: Context) {

    fun isGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun buildGpsDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("GPS не включен!!")
            .setMessage("Пожалуйста, включите службы определения местоположения в настройках, чтобы приложение работало правильно.")
        builder.setPositiveButton("Ок") { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        builder.show()
    }
}
