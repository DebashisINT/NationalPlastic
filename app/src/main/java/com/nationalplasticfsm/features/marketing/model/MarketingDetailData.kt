package com.nationalplasticfsm.features.marketing.model

import android.os.Parcel
import android.os.Parcelable
import timber.log.Timber

/**
 * Created by Pratishruti on 23-02-2018.
 */
class MarketingDetailData() :Parcelable{
    var material_name:String?=null
    var material_id:Int?=null
    var date:String?=null
    var isChecked:Boolean=false
    var typeid:String?=null
    var shop_id:String?=null

    override fun writeToParcel(dest: Parcel, p1: Int) {
        try{
            dest.writeValue(this.material_name)
            dest.writeValue(this.date)
            dest.writeValue(this.typeid)
            dest.writeValue(this.material_id)
            dest.writeValue(this.shop_id)
            dest.writeByte((if (isChecked) 1 else 0).toByte())
        }catch (ex:Exception){
            Timber.d("parcel err")
            ex.printStackTrace()
        }
    }

    override fun describeContents(): Int {
        return 0
    }
    constructor(parcel: Parcel) : this() {
        try{
            material_name = parcel.readString()
            date = parcel.readString()
            shop_id = parcel.readString()
            material_id=parcel.readInt()
            typeid=parcel.readString()
            isChecked = parcel.readInt() != 0
        }catch (ex:Exception){
            Timber.d("parcel errr")
        }
    }

    companion object CREATOR : Parcelable.Creator<MarketingDetailData> {
        override fun createFromParcel(parcel: Parcel): MarketingDetailData {
            return MarketingDetailData(parcel)
        }

        override fun newArray(size: Int): Array<MarketingDetailData?> {
            return arrayOfNulls(size)
        }
    }

}