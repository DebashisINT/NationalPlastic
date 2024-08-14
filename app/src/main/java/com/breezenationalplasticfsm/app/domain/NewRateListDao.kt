package com.breezedsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezenationalplasticfsm.app.AppConstant


@Dao
interface NewRateListDao {
    @Query("SELECT * FROM " + AppConstant.NEW_RATE_LIST)
    fun getAll(): List<NewRateListEntity>

    @Insert
    fun insert(vararg model: NewRateListEntity)

    @Query("delete FROM " + AppConstant.NEW_RATE_LIST)
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(model: List<NewRateListEntity>)

}