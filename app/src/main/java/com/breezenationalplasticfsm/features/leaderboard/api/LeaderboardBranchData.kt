package com.breezenationalplasticfsm.features.leaderboard.api

import com.breezenationalplasticfsm.base.BaseResponse

data class LeaderboardBranchData(var branch_list:ArrayList<BranchData> = ArrayList()):BaseResponse()
data class BranchData(var branch_head:String ,var branch_head_id:Int,var sub_branch:ArrayList<SubBranchData> = ArrayList())
data class SubBranchData(var id:Int=0, var value: String="",var isTick:Boolean = false)
data class LeaderboardOwnData(var user_id:Int, var user_name: String,var user_phone: String, var attendance: Int,
                              var new_visit: Int, var revisit: Int,var order: Int,var activities: Int,
                              var position: Int,var totalscore: Int,var profile_pictures_url: String
):BaseResponse()
data class LeaderboardOverAllData(var user_list:ArrayList<OverallUserListData> = ArrayList()):BaseResponse()
data class OverallUserListData(var user_id:Int ,var user_name:String,var user_phone:String,var attendance:Int,
                               var new_visit:Int,var revisit:Int,var order:Int,var activities:Int,
                               var position:Int,var totalscore:Int,var profile_pictures_url:String)
