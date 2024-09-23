package com.breezefieldnationalplastic.features.photoReg.present

import com.breezefieldnationalplastic.app.domain.ProspectEntity
import com.breezefieldnationalplastic.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}