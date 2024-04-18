package com.epstudio.displayinfo.data.impl

import com.epstudio.displayinfo.ui.model.HDRTypes
import com.epstudio.displayinfo.ui.model.ModeInfoModel

val supportedMode1 = ModeInfoModel(
    id = 1,
    physicalWidth = 7680,
    physicalHeight = 4320,
    refreshRate = 120f,
    supportHdrTypes = arrayOf(HDRTypes.DolbyVision, HDRTypes.Hdr10)
)

val supportedMode2 = ModeInfoModel(
    id = 2,
    physicalWidth = 3840,
    physicalHeight = 2160,
    refreshRate = 200f,
    supportHdrTypes = arrayOf(HDRTypes.Hlg, HDRTypes.Hdr10)
)

val supportedMode3 = ModeInfoModel(
    id = 3,
    physicalWidth = 1920,
    physicalHeight = 1080,
    refreshRate = 170f,
    supportHdrTypes = arrayOf(HDRTypes.Hdr10Plus, HDRTypes.Hdr10)
)

val supportedMode4 = ModeInfoModel(
    id = 4,
    physicalWidth = 1096,
    physicalHeight = 2560,
    refreshRate = 120f,
    supportHdrTypes = arrayOf(HDRTypes.Hdr10)
)

val supportedMode5 = ModeInfoModel(
    id = 5,
    physicalWidth = 1644,
    physicalHeight = 3840,
    refreshRate = 120f,
    supportHdrTypes = arrayOf()
)