package com.breezenationalplasticfsm.features.marketAssist

data class ShopDtls(var shop_id:String="",var shop_name:String="",var address:String="",var owner_name:String="",var owner_contact_number:String="",var shopLat:String="",var shopLong:String="",
                    var shopType:String="",var beatName:String="",var retailer_id:String="",var party_status_id:String="")

data class SuggestiveProduct(var product_id:String="",var product_name:String="",var qty:String="",var rate:String="",var total_price:String="")
data class OrderDtlsLast30Days(var product_id:String="",var product_name:String="",var qty:String="",var rate:String="",var total_price:String="",var date:String="",var order_id:String="")

data class ProductOccur(var product_id:String="",var occur:Int=0,var totalQty:String="")

data class SuggestiveProductFinal(var product_id:String="",var product_name:String="",var suggestiveOrdRate:String="",var suggestiveOrdQty:String="")

data class PurchasedProductTotal(var product_id:String="",var product_name:String="",var totalValue:String="",var totalQty:String="")

open class OrderAge(var shop_id:String="",var maxD:String="",var dateAge:String="",var countShop:String="",var isUp:Boolean=false,var totalAmt:String="")
open class ShopLVisit(var shop_id:String="",var shop_name:String="",var lastVisitedDate:String="",var lastVisitAge :Int=0,var isUp:Boolean=false)
open class ShopLOrd(var shop_id:String="",var ordV:String="",var isUp:Boolean=false)

data class ChurnShopL(var shop_id:String="",var shop_name:String="",var address:String="",var owner_name:String="",var owner_contact_number:String="",var shopLat:String="",var shopLong:String="",
                      var shopType:String="",var beatName:String="",var retailer_id:String="",var party_status_id:String="",var lastVisitedDate:String="",
                      var tag1:Boolean = false,var tag2:Boolean = false,var tag3:Boolean = false,var tag4:Boolean = false,var tag5:Boolean = false,var tag6:Boolean = false,
                        var lastPurchaseAge:String="",var lastVisitAge:String="",var avgShopOrdAmt:String="",var avgTimeSinceFirstOrd:String="",var shopVisitAvg:String="",var orderBehav:String="")

data class ShopActivityCnt(var shopid:String="",var cnt:String="")

data class ShopLastVisit(var shop_id:String="",var lastVisitedDate:String="",var lastVIsitAge:String="",var totalVisitCount:String="")