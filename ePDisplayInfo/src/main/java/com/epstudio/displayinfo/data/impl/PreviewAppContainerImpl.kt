package com.epstudio.displayinfo.data.impl

import android.content.Context
import com.epstudio.displayinfo.data.AppContainer
import com.epstudio.displayinfo.data.DisplayInfoRepository

class PreviewAppContainerImpl: AppContainer {
    override val displayInfoRepository : DisplayInfoRepository by lazy{
        PreviewDisplayInfoRepositoryImpl()
    }
}