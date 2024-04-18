package com.epstudio.displayinfo

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.epstudio.displayinfo.data.impl.display1
import com.epstudio.displayinfo.ui.DisplayIdentifyBox
import com.epstudio.displayinfo.ui.model.DisplayInfoModel

open class DisplayIdentifyActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val appContext = (application as EPDisplayInfoApplication).container
        val currentDisplay = DisplayInfoModel.createFromRawDisplay(display ?: throw Exception())

        setContent{
            DisplayIdentifyApp(display = currentDisplay)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DisplayIdentifyApp(
    display: DisplayInfoModel
){
    Scaffold (
        modifier = Modifier
            .padding(vertical = 30.dp, horizontal = 30.dp)
    ) {
        innerPadding -> Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            DisplayIdentifyBox(display = display)
        }
    }
}

@Composable
@Preview(showSystemUi = true, device = TV_1080p)
fun DisplayIdentifyAppPreview(){
    DisplayIdentifyApp(display1)
}