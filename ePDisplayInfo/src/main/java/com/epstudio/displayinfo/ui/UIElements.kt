package com.epstudio.displayinfo.ui

import android.os.Build
import android.view.Display
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.epstudio.displayinfo.DisplayIdentifyActivity
import com.epstudio.displayinfo.data.impl.display1
import com.epstudio.displayinfo.data.impl.display2
import com.epstudio.displayinfo.data.impl.supportedMode1
import com.epstudio.displayinfo.ui.model.DisplayInfoModel
import com.epstudio.displayinfo.ui.model.HDRTypes
import com.epstudio.displayinfo.ui.model.ModeInfoModel
import com.epstudio.displayinfo.ui.theme.DisplayInfoTheme
import com.epstudio.displayinfo.util.gcd
import org.jetbrains.annotations.Nullable
import kotlin.math.floor
import kotlin.text.Typography.times

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DisplayCard(
    display: DisplayInfoModel,
    isCurrentDisplay: Boolean = false,
){
    var isCollapsed by remember {
        mutableStateOf(!isCurrentDisplay)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isCollapsed = !isCollapsed
            },

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = display.productInfo?.name ?: display.name,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1.0f),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
            )
            IconToggleButton(
                checked = isCollapsed,
                onCheckedChange = {
                    isCollapsed = !isCollapsed
                },
            ) {
                Icon(
                    imageVector = if(isCollapsed) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "Collapse",
                )
            }
        }
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn(animationSpec = tween(400, 0, EaseOutExpo))
                    + expandVertically(animationSpec = tween(400, 0, EaseOutExpo), expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(400, 0, EaseOutExpo))
                    + shrinkVertically(animationSpec = tween(400, 0, EaseOutExpo), shrinkTowards = Alignment.Top)
        ) {
            Column (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ){
                KeyValueContent("Name", String.format("%s", display.name))
                KeyValueContent("Display ID", String.format("%d", display.id))
                KeyValueContent(key = "Size", value = String.format("%.1f inch", display.displaySize))
                KeyValueContent(key = "Density", value = String.format("%.1f dpi", (display.densityX + display.densityY) / 2, display.densityX, display.densityY))
                if(Build.VERSION.SDK_INT < 34){
                    if(display.supportHdrTypes.isEmpty()){
                        KeyValueContent("HDR Modes", "Not support")
                    }
                    else{
                        KeyValueContent("HDR Modes", null){
                            HDRModesContent(hdrTypes = display.supportHdrTypes)
                        }
                    }
                }
                if(display.productInfo != null){
                    if(display.productInfo?.name != null){
                        KeyValueContent("Product Name", display.productInfo?.name)
                    }
                    if(display.productInfo?.modelYear != null){
                        KeyValueContent("Model Year", display.productInfo?.modelYear.toString())
                    }
                    if(display.productInfo?.manufactureYear != null){
                        KeyValueContent("Manufacture Year", display.productInfo?.manufactureYear.toString())
                    }
                    if(display.productInfo?.manufactureWeek != null){
                        KeyValueContent("Manufacture Week", display.productInfo?.manufactureWeek.toString())
                    }
                    if(display.productInfo?.productId != null){
                        KeyValueContent("Product ID", display.productInfo?.productId)
                    }
                }
                KeyValueContent(
                    key = "Modes",
                    value = null
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for(mode in display.supportModes){
                            DisplayModeCard(mode, isCurrentMode = mode.id == display.activeModeId)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(0.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .width(180.dp)
                        .height(180.dp),
                    contentAlignment = Alignment.TopEnd
                ){
                    ScreenRadioPreviewBox(display = display)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayModeCard(
    displayMode: ModeInfoModel,
    isCurrentMode: Boolean = false
){
    val modeId = displayMode.id ?: -1
    val resWidth = displayMode.physicalWidth
    val resHeight = displayMode.physicalHeight
    val resFreshRate = displayMode.refreshRate
    var isCollapsed by remember {
        mutableStateOf(!isCurrentMode)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(isCurrentMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isCollapsed = !isCollapsed
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
        ){
            Text(
                text = String.format("%dx%d@%.2fHz", resWidth, resHeight, resFreshRate),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1.0f),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Black,
            )
            if(isCurrentMode){
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = "Current",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = if(isCollapsed) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                contentDescription = "Collapse",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(22.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    )
            )
        }
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn(animationSpec = tween(400, 0, EaseOutExpo))
                    + expandVertically(animationSpec = tween(400, 0, EaseOutExpo), expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(400, 0, EaseOutExpo))
                    + shrinkVertically(animationSpec = tween(400, 0, EaseOutExpo), shrinkTowards = Alignment.Top)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                KeyValueContent(key = "Mode ID", value = String.format("%d", displayMode.id))
                KeyValueContent(key = "Width", value = String.format("%d px", displayMode.physicalWidth))
                KeyValueContent(key = "Height", value = String.format("%d px", displayMode.physicalHeight))
                if(Build.VERSION.SDK_INT >= 34){
                    if(displayMode.supportHdrTypes.isEmpty()){
                        KeyValueContent("HDR Modes", "Not support")
                    }
                    else{
                        KeyValueContent("HDR Modes", null){
                            HDRModesContent(hdrTypes = displayMode.supportHdrTypes, color = MaterialTheme.colorScheme.surface)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HDRModesContent(
    hdrTypes: Array<HDRTypes>,
    color: Color = MaterialTheme.colorScheme.primary
){
    for(hdrType in hdrTypes){
        val hdrTypeStr = when(hdrType){
            HDRTypes.DolbyVision -> "Dolby Vision"
            HDRTypes.Hdr10 -> "HDR10"
            HDRTypes.Hdr10Plus -> "HDR10+"
            HDRTypes.Hlg -> "HLG"
            else -> "UNKNOWN"
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color,
            ),
        ){
            Text(
                text=hdrTypeStr,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 3.dp, top = 3.dp, end = 8.dp)
            )
        }
    }
}


@Composable
fun ScreenRadioPreviewBox(display: DisplayInfoModel){
    val currentMode = display.supportModes.find{
        it -> it.id == display.activeModeId
    }
    if(currentMode != null){
        val aspectRatio = currentMode.physicalWidth.toFloat() / currentMode.physicalHeight
        val widthRatio = if(aspectRatio >= 1) 1.0f else aspectRatio
        val heightRatio = if(aspectRatio >= 1) 1 / aspectRatio else 1.0f
        val aspectRatioMultipler = gcd(currentMode.physicalWidth, currentMode.physicalHeight)
        val aspectRatioWidth = currentMode.physicalWidth / aspectRatioMultipler
        val aspectRatioHeight = currentMode.physicalHeight / aspectRatioMultipler
        Box(
            modifier = Modifier
                .fillMaxWidth(widthRatio)
                .fillMaxHeight(heightRatio),
            contentAlignment = Alignment.Center
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "$aspectRatioWidth:$aspectRatioHeight",
                        fontSize = 3.em,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayIdentifyBox(display: DisplayInfoModel){
    val activeMode = display.supportModes.find{
        x -> x.id == display.activeModeId
    }
    if(activeMode != null){
        Box(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ){
            Text("1290 x 1080")
        }
    }
}

@Composable
fun KeyValueContent(key: String, value: String?, content: @Composable (RowScope.()->Unit)? = null){
    Column(

    ) {
        Text(
            text = key,
            fontWeight = FontWeight.Bold
        )
        if(value != null){
            Text(
                text = value,
            )
        }
        if(content != null){
            Row(
                content = content,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayCardPreview(){
    DisplayInfoTheme {
        val displayModel = display1
        DisplayCard(
            display = displayModel,
            isCurrentDisplay = true)
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun DisplayCardPreviewSDK33(){
    DisplayCardPreview()
}

@Preview(showBackground = true, apiLevel = 30)
@Composable
fun DisplayCardPreviewSDK30(){
    DisplayCardPreview()
}


@Preview(showBackground = true)
@Composable
fun DisplayModelCardPreview(){
    DisplayInfoTheme{
        val modeModel = supportedMode1
        DisplayModeCard(modeModel, isCurrentMode = true)
    }
}

@Preview(

)
@Composable
fun DisplayIdentifyBoxPreview(){
//    DisplayIdentifyBox(display = display1)
}

@Preview
@Composable
fun ScreenRadioPreviewBoxPreview(){
    DisplayInfoTheme {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            contentAlignment = Alignment.Center
        ){
            ScreenRadioPreviewBox(display = display1)
        }
    }
}