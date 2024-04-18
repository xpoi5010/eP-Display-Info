package com.epstudio.displayinfo.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.hardware.display.DisplayManager.DisplayListener
import android.os.Process
import android.util.DisplayMetrics
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.content.getSystemService
import com.epstudio.displayinfo.ui.model.DisplayInfoModel
import com.epstudio.displayinfo.data.DisplayInfoRepository
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.reflect.typeOf


class DisplayInfoRepositoryImpl(
    private val appContext: Context
)  : DisplayListener, DisplayInfoRepository {
    private val displayManager by lazy{
        appContext.getSystemService<DisplayManager>() ?: throw Exception()
    }

    private val displayInfoModels: SnapshotStateMap<Int, DisplayInfoModel>;

    init{
        val displays = displayManager.displays
        val models = ArrayList<Pair<Int, DisplayInfoModel>>()
        if(displays == null){
            throw Exception()
        }

        for(display in displays){
            println(display.cutout)
            val displayInfoModel = DisplayInfoModel.createFromRawDisplay(display)
            models.add(Pair(displayInfoModel.id, displayInfoModel))
        }
        displayInfoModels = mutableStateMapOf(*models.toTypedArray())
        displayManager.registerDisplayListener(this, null)
    }

    override fun getDisplays(): SnapshotStateMap<Int, DisplayInfoModel> = displayInfoModels

    override fun onDisplayAdded(displayId: Int) {
        val newDisplay = displayManager.getDisplay(displayId)
        val newDisplayModel = DisplayInfoModel.createFromRawDisplay(newDisplay)
        displayInfoModels[displayId] = newDisplayModel
    }

    override fun onDisplayRemoved(displayId: Int) {
        displayInfoModels.remove(displayId)
    }

    override fun onDisplayChanged(displayId: Int) {
        val updatedDisplay = displayManager.getDisplay(displayId)
        val displayModel = displayInfoModels[displayId]
        displayModel?.updateFromRawDisplay(updatedDisplay)
    }
}