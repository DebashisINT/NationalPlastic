package com.nationalplasticfsm.features.location.model

import com.nationalplasticfsm.base.BaseResponse
import java.io.Serializable

/**
 * Created by Saikat on 17-Aug-20.
 */
data class AppInfoInputModel(var session_token: String = "",
                             var user_id: String = "",
                             var app_info_list: ArrayList<AppInfoDataModel>?= null): Serializable

data class AppInfoDataModel(var id: String = "",
                            var date_time: String = "",
                            var battery_status: String = "",
                            var battery_percentage: String = "",
                            var network_type: String = "",
                            var mobile_network_type: String = "",
                            var device_model: String = "",
                            var android_version: String = ""): Serializable

data class AppInfoResponseModel(var app_info_list: ArrayList<AppInfoDataModel>?= null): BaseResponse(), Serializable

data class VisitRemarksResponseModel(var remarks_list: ArrayList<VisitRemarksDataModel>?= null) : BaseResponse(), Serializable

data class VisitRemarksDataModel(var id: String = "",
                                 var name: String = "") : Serializable