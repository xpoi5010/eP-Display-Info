package com.epstudio.displayinfo.data.impl

import com.epstudio.displayinfo.ui.model.DisplayInfoModel
import com.epstudio.displayinfo.ui.model.HDRTypes

val display1 = DisplayInfoModel(
    id = 1,
    name = "Bulit-in Screen",
    supportModes = arrayOf(supportedMode4, supportedMode5),
    supportHdrTypes = arrayOf(HDRTypes.DolbyVision, HDRTypes.Hdr10),
    activeModeId = 4,
    densityX = 620f,
    densityY = 620f,
    displaySize = 12f,
    productInfo = productData1
)

val display2 = DisplayInfoModel(
    id = 2,
    name = "Test Display",
    supportModes = arrayOf(supportedMode1, supportedMode3),
    supportHdrTypes = arrayOf(HDRTypes.Hlg),
    activeModeId = 1,
    densityX = 620f,
    densityY = 620f,
    displaySize = 12f,
    productInfo = productData1,
)

val display3 = DisplayInfoModel(
    id = 3,
    name = "Test Display",
    supportModes = arrayOf(supportedMode2, supportedMode3),
    supportHdrTypes = arrayOf(HDRTypes.DolbyVision),
    activeModeId = 3,
    densityX = 620f,
    densityY = 620f,
    displaySize = 12f,
    productInfo = productData1
)

val display4 = DisplayInfoModel(
    id = 4,
    name = "Test Display",
    supportModes = arrayOf(supportedMode2, supportedMode3),
    activeModeId = 2,
    supportHdrTypes = arrayOf(),
    densityX = 620f,
    densityY = 620f,
    displaySize = 12f,
    productInfo = null
)