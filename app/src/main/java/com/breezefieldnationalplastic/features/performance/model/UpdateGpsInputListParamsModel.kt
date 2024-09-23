package com.breezefieldnationalplastic.features.performance.model

/**
 * Created by saheli on 20-02-2023.
 */
class UpdateGpsInputListParamsModel {
    var gps_status_list: ArrayList<Gps_status_list> = ArrayList()

}

data class Gps_status_list( var session_token: String = "",
                             var user_id: String = "",
                             var gps_id: String = "",
                             var date: String = "",
                             var gps_off_time: String = "",
                             var gps_on_time: String = "",
                             var duration: String = "")