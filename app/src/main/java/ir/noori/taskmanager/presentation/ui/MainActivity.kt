package ir.noori.taskmanager.presentation.ui

import CheckInternetStatus
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import ir.noori.taskmanager.databinding.ActivityMainBinding
import ir.noori.taskmanager.presentation.ui.splash.SplashViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        checkPostNotificationPermission()
        observer()
        checkInternetStatus()
    }

    private fun checkPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun observer() {

//        viewModel.isDarkMode.observe(this) { isDark ->
//            AppCompatDelegate.setDefaultNightMode(
//                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
//                else AppCompatDelegate.MODE_NIGHT_NO
//            )
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    private fun checkInternetStatus() {
        if (!CheckInternetStatus.isInternetAvailable(applicationContext)) {
            Toast.makeText(this, R.string.internet_warning, Toast.LENGTH_SHORT).show()
        }
    }

}