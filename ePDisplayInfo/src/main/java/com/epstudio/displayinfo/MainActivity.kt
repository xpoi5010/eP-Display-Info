package com.epstudio.displayinfo

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.DisplayManager.DisplayListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import com.epstudio.displayinfo.data.AppContainer
import com.epstudio.displayinfo.data.impl.PreviewAppContainerImpl
import com.epstudio.displayinfo.ui.theme.DisplayInfoTheme
import com.epstudio.displayinfo.ui.model.*
import com.epstudio.displayinfo.ui.*
import com.epstudio.displayinfo.ui.theme.Rubik

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appContainer = (application as EPDisplayInfoApplication).container
        val attr = window.attributes
        window.attributes = attr
        setContent {
            DisplayInfoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    EPDisplayInfoApp(appContainer, this)
                }
            }
        }
//        for(display in appContainer.displayInfoRepository.getDisplays()){
//            if(display.key != this.display?.displayId){
//                val secondIntent = Intent(this, DisplayIdentifyActivity::class.java)
//                secondIntent.addFlags(
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
//                            or Intent.FLAG_ACTIVITY_NO_ANIMATION
//                            or Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
//                            or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                )
//                val options = ActivityOptions.makeBasic()
//                options.launchDisplayId = display.key
//                startActivity(
//                    secondIntent,
//                    options.toBundle()
//                )
//            }
//
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EPDisplayInfoApp(
    appContainer: AppContainer,
    appContext: Context?
){
    val topbarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topbarState)
    var isDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }
    val displays: SnapshotStateMap<Int, DisplayInfoModel> = remember {
        appContainer.displayInfoRepository.getDisplays()
    }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "eP Display Info",
                        fontFamily = Rubik,
                        fontWeight = FontWeight.Normal)
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(
                        onClick = {
                            isDropdownMenuExpanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownMenuExpanded,
                        onDismissRequest = {
                            isDropdownMenuExpanded = false
                        },
                        modifier = Modifier
                            .width(150.dp)
                    ){
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text="Privacy Policy"
                                )
                            },
                            onClick = {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(EPDisplayInfoApplication.privacyPolicy))
                                appContext?.startActivity(browserIntent)
                                isDropdownMenuExpanded = false
                            })
                    }
                }
            )

        },
    ) {
            innerPadding -> Column (
        modifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ){
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            displays.map{
                item -> key(
                    item.key
                ) {
                    DisplayCard(item.value, isCurrentDisplay = false)
                }
            }
        }
    }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    val previewAppContainer = PreviewAppContainerImpl()
    DisplayInfoTheme {
        EPDisplayInfoApp(previewAppContainer, null)
    }
}