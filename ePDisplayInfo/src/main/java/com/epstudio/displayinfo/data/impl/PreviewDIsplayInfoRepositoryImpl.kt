package com.epstudio.displayinfo.data.impl

import android.content.Context
import android.hardware.display.DisplayManager
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.content.getSystemService
import com.epstudio.displayinfo.data.DisplayInfoRepository
import com.epstudio.displayinfo.ui.model.DisplayInfoModel

class PreviewDisplayInfoRepositoryImpl: DisplayInfoRepository {
    private val displayInfoModels: SnapshotStateMap<Int, DisplayInfoModel>;

    init{
        val models = ArrayList<Pair<Int, DisplayInfoModel>>()
        val displays = arrayOf(
            display1, display2, display3, display4
        )
        for(display in displays){
            models.add(Pair(display.id, display))
        }
        displayInfoModels = mutableStateMapOf(*models.toTypedArray())
    }

    override fun getDisplays(): SnapshotStateMap<Int, DisplayInfoModel> = displayInfoModels

}