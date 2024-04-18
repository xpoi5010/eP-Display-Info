package com.epstudio.displayinfo

import android.app.Application
import com.epstudio.displayinfo.data.AppContainer
import com.epstudio.displayinfo.data.impl.AppContainerImpl

class EPDisplayInfoApplication : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        val classee = Class.forName("android.os.ServiceManager")
        val methodd = classee.getMethod("listServices")
        val services = methodd.invoke(null)
        container = AppContainerImpl(this)
    }

    companion object{
        const val website : String = "https://www.epstudio.cc"
        const val privacyPolicy : String = "https://www.epstudio.cc/post/12"
    }
}