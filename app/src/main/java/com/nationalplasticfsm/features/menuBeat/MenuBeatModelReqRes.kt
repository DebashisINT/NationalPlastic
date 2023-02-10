package com.nationalplasticfsm.features.menuBeat

data class MenuBeatGetReq(var beat_id:String ="",var user_id:String = "",var session_token:String = "")

data class MenuBeatResponse (var status:String = "", var message:String = "",var area_id:String = "", var area_name:String = "",var route_id:String="",
                             var route_name:String="",var beat_id:String = "",var beat_name:String = "")


data class MenuBeatAreaRouteResponse (var status:String = "",
                                var message:String = "",var area_list:ArrayList<MenuBeatAreaResponse> = ArrayList())

data class MenuBeatAreaResponse (var area_id:String = "", var area_name:String = "",var route_list:ArrayList<MenuBeatRoutesResponse> = ArrayList())

data class MenuBeatRoutesResponse (var route_id:String="",var route_name:String="",var beat_list:ArrayList<MenuBeat> = ArrayList())

data class MenuBeat (var beat_id:String = "",var beat_name:String = "")