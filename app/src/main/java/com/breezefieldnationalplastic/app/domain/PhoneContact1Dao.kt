package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant

@Dao
interface PhoneContact1Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<PhoneContact1Entity>)

    @Insert
    fun insert(vararg obj: PhoneContact1Entity)

    @Query("select * from "+ AppConstant.PHONE_CONTACT1)
    fun getAll(): List<PhoneContact1Entity>

    @Query("delete from "+ AppConstant.PHONE_CONTACT1)
    fun deleteAll()
}