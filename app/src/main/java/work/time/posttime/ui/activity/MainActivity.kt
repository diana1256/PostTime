package work.time.adsert.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import work.time.worktim.R
import work.time.worktim.databinding.ActivityMainBinding
import work.time.worktim.ui.activity.NotificationPermissionHandler
import work.time.worktim.ui.util.AlarmScheduler
import work.time.worktim.ui.util.Pref


    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private val notificationPermissionHandler = NotificationPermissionHandler(this)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            if (notificationPermissionHandler.checkNotificationPermission()) {
                AlarmScheduler().scheduleAlarms(this)
            } else {
                notificationPermissionHandler.requestNotificationPermission(this)
            }

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    ?: NavHostFragment.create(R.navigation.nav_graf)

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()

            val navController = navHostFragment.navController

            if (!Pref(applicationContext).isBoardingShowed()) {
               navController.navigate(R.id.authFragment)
            } else {
                navController.navigate(R.id.homeFragment)
            }
        }


        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (notificationPermissionHandler.handlePermissionResult(
                    requestCode,
                    permissions,
                    grantResults
                )
            ) {
                AlarmScheduler().scheduleAlarms(this)
            } else {
                Toast.makeText(
                    this,
                    "Дайте разрешение уведомления в настройках!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onBackPressed() {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController

            when (navController.currentDestination?.id) {
                R.id.scanerFragment -> {
                    navController.navigate(R.id.action_scaner_to_home)
                }

                R.id.homeFragment -> {
                    finish()
                }

                R.id.authFragment -> {
                    finish()
                }

                else -> {
                    super.onBackPressed()
                }
            }
        }

    }
