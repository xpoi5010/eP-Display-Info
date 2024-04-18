package com.epstudio.displayinfo.data

import android.content.Context
import com.epstudio.displayinfo.data.impl.DisplayInfoRepositoryImpl

interface AppContainer{
    val displayInfoRepository: DisplayInfoRepository
}