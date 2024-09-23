package com.breezefieldnationalplastic.features.newcollectionreport

import com.breezefieldnationalplastic.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}