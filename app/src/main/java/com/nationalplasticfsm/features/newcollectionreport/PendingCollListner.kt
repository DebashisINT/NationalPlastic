package com.nationalplasticfsm.features.newcollectionreport

import com.nationalplasticfsm.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}