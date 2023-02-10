package com.nationalplasticfsm.features.nearbyshops.presentation

import com.nationalplasticfsm.features.nearbyshops.model.ShopData

data class ShopModifiedListResponse (var status:String,var modified_shop_list:ArrayList<ShopData> = ArrayList())

data class ShopModifiedUpdateList (var user_id:String="",var shop_modified_list:ArrayList<ShopIdModified> = ArrayList())

data class ShopIdModified(var shop_id:String)
