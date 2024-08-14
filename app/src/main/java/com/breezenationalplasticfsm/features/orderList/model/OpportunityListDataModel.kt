package com.breezenationalplasticfsm.features.orderList.model

/**
 * Created by Puja on 01.06.2024
 */
class OpportunityListDataModel {
    var shop_id: String? = null
    var shop_name: String? = null
    var shop_type: String? = null
    var opportunity_id: String? = null
    var opportunity_description: String? = null
    var opportunity_amount: String? = null
    var opportunity_status_id: String? = null
    var opportunity_status_name: String? = null
    var opportunity_created_date: String? = null
    var opportunity_created_time: String?= null
    var opportunity_created_date_time: String?= null
    var opportunity_edited_date_time: String?= null
    var opportunity_product_list: ArrayList<OpptProductListDataModel>? = null
}