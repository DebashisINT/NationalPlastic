package com.breezefieldnationalplastic.features.photoReg.adapter

import com.breezefieldnationalplastic.features.photoReg.model.UserListResponseModel

interface PhotoAttendanceListner {
    fun getUserInfoOnLick(obj: UserListResponseModel)
    fun getUserInfoAttendReportOnLick(obj: UserListResponseModel)
}