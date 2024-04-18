package com.epstudio.displayinfo.ui.model

import android.os.Build
import android.view.Display
import android.view.Display.Mode

data class ModeInfoModel(
    val id: Int,
    val physicalWidth: Int,
    val physicalHeight: Int,
    val refreshRate: Float,
    val supportHdrTypes: Array<HDRTypes>
){
    companion object{
        fun createFromRawSupportedMode(supportMode: Mode): ModeInfoModel {
            val hdrTypes = ArrayList<HDRTypes>()
            if (Build.VERSION.SDK_INT >= 34){
                for(hdrCapability in supportMode.supportedHdrTypes){
                    when(hdrCapability){
                        Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION -> hdrTypes.add(HDRTypes.DolbyVision)
                        Display.HdrCapabilities.HDR_TYPE_HDR10 -> hdrTypes.add(HDRTypes.Hdr10)
                        Display.HdrCapabilities.HDR_TYPE_HDR10_PLUS -> hdrTypes.add(HDRTypes.Hdr10Plus)
                        Display.HdrCapabilities.HDR_TYPE_HLG -> hdrTypes.add(HDRTypes.Hlg)
                    }
                }
            }
            return ModeInfoModel(
                id = supportMode.modeId,
                physicalWidth = supportMode.physicalWidth,
                physicalHeight = supportMode.physicalHeight,
                refreshRate = supportMode.refreshRate,
                supportHdrTypes = hdrTypes.toTypedArray()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModeInfoModel

        if (id != other.id) return false
        if (physicalWidth != other.physicalWidth) return false
        if (physicalHeight != other.physicalHeight) return false
        if (refreshRate != other.refreshRate) return false
        if (!supportHdrTypes.contentEquals(other.supportHdrTypes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + physicalWidth
        result = 31 * result + physicalHeight
        result = 31 * result + refreshRate.hashCode()
        result = 31 * result + supportHdrTypes.contentHashCode()
        return result
    }
}