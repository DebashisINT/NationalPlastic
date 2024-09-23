package com.breezefieldnationalplastic.features.dashboard.presentation.api.dayStartEnd

import com.breezefieldnationalplastic.features.stockCompetetorStock.api.AddCompStockApi
import com.breezefieldnationalplastic.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}