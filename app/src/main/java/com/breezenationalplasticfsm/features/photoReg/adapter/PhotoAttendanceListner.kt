package com.breezenationalplasticfsm.features.photoReg.adapter

import com.breezenationalplasticfsm.features.photoReg.model.UserListResponseModel

interface PhotoAttendanceListner {
    fun getUserInfoOnLick(obj: UserListResponseModel)
    fun getUserInfoAttendReportOnLick(obj: UserListResponseModel)
}