package com.breezefieldnationalplastic.features.viewAllOrder

data class OrdResponse(var status:String="",var message:String="",var order_status_list:ArrayList<OrdStatusL> = ArrayList())
data class OrdStatusL(var Order_Code:String="",var OrderStatus:String="",var Order_date:String="")
