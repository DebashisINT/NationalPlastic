package com.nationalplasticfsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nationalplasticfsm.app.AppConstant

/**
 * Created by Saikat on 10-Jul-20.
 */
@Dao
interface ClientListDao {

    @Query("SELECT * FROM " + AppConstant.CLIENT_LIST)
    fun getAll(): List<ClientListEntity>

    @Insert
    fun insertAll(vararg client: ClientListEntity)

    @Query("DELETE FROM " + AppConstant.CLIENT_LIST)
    fun deleteAll()
}