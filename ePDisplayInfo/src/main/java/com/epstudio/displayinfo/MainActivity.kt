package com.epstudio.displayinfo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        setContent {
            DisplayInfoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    EPDisplayInfoApp(appContainer, this)
                }
            }
        }
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

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text="Source Code"
                                )
                            },
                            onClick = {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(EPDisplayInfoApplication.sourceCode))
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