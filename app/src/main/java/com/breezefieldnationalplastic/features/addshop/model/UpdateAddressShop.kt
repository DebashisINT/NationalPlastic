package com.breezefieldnationalplastic.features.addshop.model

import com.breezefieldnationalplastic.app.domain.AddShopDBModelEntity
import com.breezefieldnationalplastic.app.domain.CallHisEntity
import com.breezefieldnationalplastic.base.BaseResponse

data class UpdateAddrReq  (var user_id:String="",var shop_list:ArrayList<UpdateAddressShop> = ArrayList())

data class UpdateAddressShop(var shop_id:String="",var shop_updated_lat:String="",var shop_updated_long:String="",var shop_updated_address:String="")