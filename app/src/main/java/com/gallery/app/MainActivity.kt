package com.gallery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gallery.app.core.datastore.DataStoreManager
import com.gallery.app.core.permissions.PermissionManager
import com.gallery.app.core.theme.GalleryTheme
import com.gallery.app.core.widgets.PermissionRationaleDialog
import com.gallery.app.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var thumbnailManager: com.gallery.app.core.editor.thumbnail.FrameThumbnailManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userPrefs by dataStoreManager.userPreferences.collectAsState(
                initial = com.gallery.app.core.datastore.UserPreferences()
            )

            var hasPermission by remember { mutableStateOf(permissionManager.hasMediaPermission()) }
            var showRationale by remember { mutableStateOf(false) }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { results ->
                val granted = results.values.any { it }
                hasPermission = granted
                if (!granted) {
                    showRationale = true
                }
            }

            LaunchedEffect(Unit) {
                if (!hasPermission) {
                    permissionLauncher.launch(permissionManager.getRequiredPermissions().toTypedArray())
                }
            }

            GalleryTheme(
                appTheme = userPrefs.theme,
                dynamicColor = userPrefs.dynamicColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasPermission) {
                        AppNavGraph(thumbnailManager = thumbnailManager)
                    } else if (showRationale) {
                        PermissionRationaleDialog(
                            onGrantPermission = {
                                showRationale = false
                                permissionLauncher.launch(permissionManager.getRequiredPermissions().toTypedArray())
                            },
                            onDismiss = { showRationale = false }
                        )
                    }
                }
            }
        }
    }
}
