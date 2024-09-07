package com.example.permission

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun permissionDialog(
    permissionTextProvider:PermissionTextProvider,
    isPermanentlyDeclined:Boolean,
    onOkClick:()->Unit,
    onGoToAppSettings:()->Unit,
    onDismiss:()->Unit,
    modifier: Modifier =Modifier
){
  AlertDialog(onDismissRequest = onDismiss,
      confirmButton =
      {
          Column(modifier=Modifier.fillMaxWidth())
          {
              Divider()
              Text(text = if (isPermanentlyDeclined){"Grant Permission"} else "OK",
                  modifier= Modifier.fillMaxWidth().clickable {
                          if (isPermanentlyDeclined) { onGoToAppSettings() } else onOkClick()
                      }.padding(16.dp)
              )
          }
      },
      title = { Text(text = "Permission Required")},
      text  = { Text(text = permissionTextProvider.getDescription(isPermanentlyDeclined))},
      modifier = modifier
  )
}

interface PermissionTextProvider{
    fun getDescription(isPermanentlyDeclined:Boolean):String
}

class CameraPermissionTextProvider:PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined:Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined camera permission. "+
                    "You can go to the app settings to grant it."
        } else {
            "This app needs  your camera so that your friends "+
                    "can see you in a call."
        }
    }
}
class recordAudioPermissionTextProvider:PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined:Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined microphone permission. "+
                    "You can go to the app settings to grant it."
        } else {
            "This app needs to your microphone so that your friends "+
                    "can hear you in a call."
        }
    }
}
class phoneCallPermissionTextProvider:PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined:Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined phone calling permission. "+
                    "You can go to the app settings to grant it."
        } else {
            "This app needs to your phone calling so that you can talk "+
                    "to your friends"
        }
    }
}
















