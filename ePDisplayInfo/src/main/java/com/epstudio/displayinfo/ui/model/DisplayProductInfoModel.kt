package com.epstudio.displayinfo.ui.model

import android.annotation.SuppressLint
import android.hardware.display.DeviceProductInfo
import android.os.Build
import android.os.Parcel
import android.view.Display
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.painterResource
import java.lang.Exception

data class DisplayProductInfoModel(
    val name: String?,
    val modelYear: Int?,
    val manufactureYear: Int?,
    val manufactureWeek: Int?,
    val manufacturePip: String?,
    val productId: String?
) {
    companion object{
        @RequiresApi(Build.VERSION_CODES.S)
        fun createFromRawProductInfo(productInfo: DeviceProductInfo): DisplayProductInfoModel{
            return DisplayProductInfoModel(
                name = productInfo.name,
                modelYear = productInfo.modelYear,
                manufactureYear = productInfo.manufactureYear,
                manufactureWeek = productInfo.manufactureWeek,
                manufacturePip = productInfo.manufacturerPnpId,
                productId = productInfo.productId
            )
        }

        @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
        fun createFromRawProductInfoUnsafe(rawDisplay: Display): DisplayProductInfoModel?{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                throw Exception("This device is running on a higher android version. It isn't require use this method.")
            }
            else{
                try{
                    val rawDisplayInfoField = rawDisplay.javaClass.getDeclaredField("mDisplayInfo")
                    rawDisplayInfoField.isAccessible = true
                    val rawDisplayInfo = rawDisplayInfoField.get(rawDisplay)
                    val rawDisplayInfoWriteParcel = rawDisplayInfo.javaClass.getMethod("writeToParcel", Parcel::class.java, Int::class.java)
                    val parcel = Parcel.obtain()
                    rawDisplayInfoWriteParcel.isAccessible = true
                    rawDisplayInfoWriteParcel.invoke(rawDisplayInfo, parcel, 0)
                    parcel.setDataPosition(0)

                    val layerStack = parcel.readInt()
                    val flags = parcel.readInt()
                    val type = parcel.readInt()
                    val displayId = parcel.readInt()
                    val addressStr = parcel.readString()
                    if(addressStr?.compareTo("android.view.DisplayAddress\$Physical") == 0){
                        val physicalDisplayId = parcel.readLong()
                        println("physicalDisplayId: $physicalDisplayId")
                    }
                    else if(addressStr?.compareTo("android.view.DisplayAddress\$Network") == 0){
                        val macAddress = parcel.readString()
                        println("macAddress: $macAddress")
                    }
                    val deviceProductInfoStr = parcel.readString()
                    if(deviceProductInfoStr?.compareTo("android.hardware.display.DeviceProductInfo") == 0){
                        val name = parcel.readString()
                        val manufacturePip = parcel.readString()
                        val productId = parcel.readValue(null) as String
                        val modelYear = parcel.readValue(null) as Int?
                        val manufactureData = parcel.readValue(null)
                        return DisplayProductInfoModel(
                            name = name,
                            manufacturePip = manufacturePip,
                            modelYear = modelYear,
                            manufactureWeek = null,
                            manufactureYear = null,
                            productId = productId
                        )
                    }
                    return null
                }
                catch(ex: Exception) {
                    return null
                }
            }
        }
    }
}