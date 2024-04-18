package com.epstudio.displayinfo.data.impl

import android.content.Context
import com.epstudio.displayinfo.data.AppContainer

class AppContainerImpl(
    private val appContext: Context
): AppContainer {
    override val displayInfoRepository: DisplayInfoRepositoryImpl by lazy{
        DisplayInfoRepositoryImpl(
            appContext = appContext
        )
    }
}