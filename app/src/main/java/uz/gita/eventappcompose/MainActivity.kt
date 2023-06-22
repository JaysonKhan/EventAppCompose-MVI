package uz.gita.eventappcompose

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
import cafe.adriel.voyager.navigator.Navigator
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.DexterBuilder.MultiPermissionListener
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.eventappcompose.domain.usecase.UseCase
import uz.gita.eventappcompose.presentation.screen.home.HomeScreen
import uz.gita.eventappcompose.ui.theme.EventAppComposeTheme
import uz.gita.eventappcompose.utils.service.EventService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1

    @Inject
    lateinit var useCase: UseCase
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        scope.launch {
            useCase.getAllEvents().onEach { list ->
                val arrayList = ArrayList<String>()

                for (i in list.indices) {
                    arrayList.add(list[i].events)
                }
                val intent = Intent(this@MainActivity, EventService::class.java)
                intent.putStringArrayListExtra("enabledActions", arrayList)

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Starting service
        val serviceIntent = Intent(this, EventService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkNotificationPermission()
                }
                    this.startForegroundService(serviceIntent)

        } else {
            this.startService(serviceIntent)
        }

        setContent {
            EventAppComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(HomeScreen())
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.areAllPermissionsGranted()) {
                            // Ruxsat berildi
                            // Notificationlarni chiqaring
                        }
                        if (it.isAnyPermissionPermanentlyDenied) {
                            // Foydalanuvchi ruxsat berishni rad qildi
                            // Xodimiz qo'shimcha ma'lumot yoki qo'llanma ko'rsating
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener { error ->
                // Xatolik sodir bo'ldi
            }
            .onSameThread()
            .check()
    }


}