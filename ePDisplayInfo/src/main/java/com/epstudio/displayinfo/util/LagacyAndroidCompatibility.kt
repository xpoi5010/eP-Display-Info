package com.epstudio.displayinfo.util

import android.R.attr.colorMode
import android.R.attr.name
import android.R.attr.rotation
import android.R.attr.type
import android.os.Build
import android.os.Parcel
import android.view.Display
import android.view.DisplayCutout


open class DisplayInfo(){
    var layerStack: Int = 0
    var flags: Int = 0

    fun readFromParcel(source: Parcel){

    }
}