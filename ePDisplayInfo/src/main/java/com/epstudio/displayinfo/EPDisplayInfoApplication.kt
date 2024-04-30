package com.epstudio.displayinfo

import android.app.Application
import com.epstudio.displayinfo.data.AppContainer
import com.epstudio.displayinfo.data.impl.AppContainerImpl

class EPDisplayInfoApplication : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }

    companion object{
        const val sourceCode : String = "https://github.com/xpoi5010/eP-Display-Info"
        const val privacyPolicy : String = "https://github.com/xpoi5010/eP-Display-Info/blob/main/PrivacyPolicy.md"
    }
}