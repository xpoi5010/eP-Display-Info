package com.epstudio.displayinfo.data

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.epstudio.displayinfo.ui.model.DisplayInfoModel

interface DisplayInfoRepository{
    fun getDisplays(): SnapshotStateMap<Int, DisplayInfoModel>
}