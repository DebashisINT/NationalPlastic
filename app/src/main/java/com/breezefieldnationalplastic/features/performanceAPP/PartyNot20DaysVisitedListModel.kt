package com.breezefieldnationalplastic.features.performanceAPP

data class PartyNot20DaysVisitedListModel(var list_partynotVLast20:ArrayList<List_Party_Last20>)
data class List_Party_Last20(var shop_name:String="",var shop_type:String="",var last_order_date:String="",var last_visit_date:String="")
data class PartyWiseSales(var list_party_wise_sales:ArrayList<listparty_wise_sales>)

data class listparty_wise_sales(var shop_id:String="",var shop_name:String="",var shop_type:String="",var total_sales_value:String="")


data class PartyWiseDataModel(var shop_name:String="",var shop_type_name:String="",var total_sales_value:String="")