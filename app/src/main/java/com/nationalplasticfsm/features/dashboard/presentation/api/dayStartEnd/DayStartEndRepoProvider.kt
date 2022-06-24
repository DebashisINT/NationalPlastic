package com.nationalplasticfsm.features.dashboard.presentation.api.dayStartEnd

import com.nationalplasticfsm.features.stockCompetetorStock.api.AddCompStockApi
import com.nationalplasticfsm.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}