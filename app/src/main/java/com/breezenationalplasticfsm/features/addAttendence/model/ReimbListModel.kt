package com.breezenationalplasticfsm.features.addAttendence.model

data class ReimbListModel (var id:String,var visit_location:String,var isSelected:Boolean = false)

data class VisitLocationListResponse(var status:String,var message:String,var visit_location_list:ArrayList<VisitLocationList>)
data class VisitLocationList(var id:Int , var visit_location:String)

data class AreaListResponse(var status:String,var message:String, var area_list_by_city:ArrayList<AreaList>)
data class AreaList(var area_location_id:String,var area_location_name:String,var area_lat:String,var area_long:String,var isSelected:Boolean = false)