package com.breezenationalplasticfsm.features.dailyPlan.model

import com.breezenationalplasticfsm.base.BaseResponse
import java.io.Serializable

/**
 * Created by Saikat on 24-12-2019.
 */
class GetPlanDetailsResponseModel : BaseResponse(), Serializable {
    var plan_data_details: ArrayList<GetPlanDetailsDataModel>? = null
}