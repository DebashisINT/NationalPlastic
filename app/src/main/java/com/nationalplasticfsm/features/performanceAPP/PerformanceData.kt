package com.nationalplasticfsm.features.performanceAPP

import com.nationalplasticfsm.features.orderList.model.NewProductListDataModel


data class NoOrderTakenShop(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
,var type:String="")

data class NoOrderTakenShop2(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
                            ,var type:String="",var orderDate:String="")

data class NoProductSoldShop(var product_name:String="")

data class OrderProductListForTeam(var order_id:String="",var product_name:String="")


data class NoOrderTakenList(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
                            ,var type:String="",var age_since_party_creation_count:String="")

data class ShopDtlsCustom(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
                          ,var type:String="",var age_since_party_creation_count:String="",var dateAdded:String = "",var lastVisitedDate:String="")



data class NoVisitedActivityInshop(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
                             ,var type:String="",var visitedDate:String="")

data class NoCollActivityInshop(var shop_id:String="",var shop_name:String="",var owner_contact_number:String="",var address:String="",var owner_name:String=""
                                   ,var type:String="",var collectionDate:String="")



data class AllPerformanceData(var empId:String="",var empName:String="",var attendanceP:String="",var attendanceA:String="",var lastthreemonthOrderV:String="",var visitInactivity_partyCount:String=""
                                ,var lastvisitD:String="",var orderInactivity_partyCount:String="",var lastOrderD:String="")

