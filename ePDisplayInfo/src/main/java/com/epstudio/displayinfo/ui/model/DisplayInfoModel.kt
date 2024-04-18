package com.epstudio.displayinfo.ui.model

import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION
import android.view.Display.HdrCapabilities.HDR_TYPE_HDR10
import android.view.Display.HdrCapabilities.HDR_TYPE_HDR10_PLUS
import android.view.Display.HdrCapabilities.HDR_TYPE_HLG
import android.view.Display.HdrCapabilities.HDR_TYPE_INVALID
import kotlin.math.sqrt

data class DisplayInfoModel(
    var id: Int,
    var name: String,
    var supportModes: Array<ModeInfoModel>,
    var supportHdrTypes: Array<HDRTypes>,
    var activeModeId: Int,
    var displaySize: Float,
    var densityX: Float,
    var densityY: Float,
    var productInfo: DisplayProductInfoModel?
) {
    fun updateFromRawDisplay(display: Display){
        val modeInfoModels = ArrayList<ModeInfoModel>()
        for (supportMode in display.supportedModes) {
            val modeInfoModel = ModeInfoModel.createFromRawSupportedMode(supportMode)
            modeInfoModels.add(modeInfoModel)
        }
        val hdrTypes = ArrayList<HDRTypes>()

        if(Build.VERSION.SDK_INT < 34){
            for(hdrCapability in display.hdrCapabilities.supportedHdrTypes){
                when(hdrCapability){
                    HDR_TYPE_DOLBY_VISION -> hdrTypes.add(HDRTypes.DolbyVision)
                    HDR_TYPE_HDR10 -> hdrTypes.add(HDRTypes.Hdr10)
                    HDR_TYPE_HDR10_PLUS -> hdrTypes.add(HDRTypes.Hdr10Plus)
                    HDR_TYPE_HLG -> hdrTypes.add(HDRTypes.Hlg)
                }
            }
        }
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)
        val densityWidth = displayMetrics.xdpi
        val densityHeight = displayMetrics.ydpi
        val sizeX = displayMetrics.widthPixels / densityWidth
        val sizeY = displayMetrics.heightPixels / densityHeight
        val size = sqrt(sizeX * sizeX + sizeY * sizeY)
        id = display.displayId
        name = display.name
        supportModes = modeInfoModels.toTypedArray<ModeInfoModel>()
        supportHdrTypes = hdrTypes.toTypedArray()
        activeModeId = display.mode.modeId
        densityX = densityWidth
        densityY = densityHeight
        displaySize = size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DisplayInfoModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (!supportModes.contentEquals(other.supportModes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + supportModes.contentHashCode()
        return result
    }

    companion object{
        fun createFromRawDisplay(display: Display): DisplayInfoModel {
            val modeInfoModels = ArrayList<ModeInfoModel>()
            for (supportMode in display.supportedModes) {
                val modeInfoModel = ModeInfoModel.createFromRawSupportedMode(supportMode)
                modeInfoModels.add(modeInfoModel)
            }
            val hdrTypes = ArrayList<HDRTypes>()

            if (Build.VERSION.SDK_INT < 34 && display.hdrCapabilities != null){
                for(hdrCapability in display.hdrCapabilities.supportedHdrTypes){
                    when(hdrCapability){
                        HDR_TYPE_DOLBY_VISION -> hdrTypes.add(HDRTypes.DolbyVision)
                        HDR_TYPE_HDR10 -> hdrTypes.add(HDRTypes.Hdr10)
                        HDR_TYPE_HDR10_PLUS -> hdrTypes.add(HDRTypes.Hdr10Plus)
                        HDR_TYPE_HLG -> hdrTypes.add(HDRTypes.Hlg)
                    }
                }
            }
            val displayMetrics = DisplayMetrics()
            display.getRealMetrics(displayMetrics)
            val densityWidth = displayMetrics.xdpi
            val densityHeight = displayMetrics.ydpi
            val sizeX = displayMetrics.widthPixels / densityWidth
            val sizeY = displayMetrics.heightPixels / densityHeight
            val size = sqrt(sizeX * sizeX + sizeY * sizeY)
            val productInfo = when{
                Build.VERSION.SDK_INT < 31 -> DisplayProductInfoModel.createFromRawProductInfoUnsafe(display)
                display.deviceProductInfo == null -> null
                Build.VERSION.SDK_INT > 31 -> DisplayProductInfoModel.createFromRawProductInfo(display.deviceProductInfo ?: throw Exception())
                else -> {
                    null
                }
            }
            return DisplayInfoModel(
                id = display.displayId,
                name = display.name,
                supportModes = modeInfoModels.toTypedArray<ModeInfoModel>(),
                supportHdrTypes = hdrTypes.toTypedArray(),
                activeModeId = display.mode.modeId,
                displaySize = size,
                densityX = densityWidth,
                densityY = densityHeight,
                productInfo = productInfo
            )
        }
    }
}