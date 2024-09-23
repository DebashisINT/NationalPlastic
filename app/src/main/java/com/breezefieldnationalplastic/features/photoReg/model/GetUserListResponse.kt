package com.breezefieldnationalplastic.features.photoReg.model

import com.breezefieldnationalplastic.features.stockAddCurrentStock.model.CurrentStockGetDataDtls

class GetUserListResponse {
    var status:String ? = null
    var message:String ? = null
    var user_list :ArrayList<UserListResponseModel>? = null
}