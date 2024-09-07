package com.example.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachReversed
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permission.ui.theme.PermissionTheme

class MainActivity : ComponentActivity() {

    private val permissionToRequest= arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    val viewModel=viewModel<MainViewModel>()
                    val dialogQueue=viewModel.visiblePermissionDialogQueue

                    val cameraPermissionLauncher= rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { viewModel.onPermissionResult(permission=Manifest.permission.CAMERA, isGranted = it) }
                    )
                    val multiplePermissionResultLauncher= rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = {
                            permissionToRequest.forEach { permissions->
                                viewModel.onPermissionResult(permission = permissions, isGranted = it[permissions]==true)
                            }
                        }
                    )

                    Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Button(onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) })
                        {
                            Text(text = "One Permission")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {multiplePermissionResultLauncher.launch(permissionToRequest)})
                        {
                            Text(text = "Multiple Permission")
                        }

                    }
                    dialogQueue.reversed().forEach{ permissions->
                        permissionDialog(
                            permissionTextProvider = when (permissions)
                            {
                                Manifest.permission.CAMERA->{CameraPermissionTextProvider()}
                                Manifest.permission.RECORD_AUDIO->{recordAudioPermissionTextProvider()}
                                Manifest.permission.CALL_PHONE->{phoneCallPermissionTextProvider()}
                                else->return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permissions),
                            onOkClick = { viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(arrayOf(permissions))
                            },
                            onGoToAppSettings = ::openAppSettings,
                            onDismiss = viewModel::dismissDialog
                        )
                    }
                }
            }
        }
    }

}
fun Activity.openAppSettings()
{
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",packageName,null)).also(::startActivity)
}

