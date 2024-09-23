package com.breezefieldnationalplastic.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant
import com.breezefieldnationalplastic.features.contacts.ContactDtls

@Dao
interface PhoneContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<PhoneContactEntity>)

    @Insert
    fun insert(vararg obj: PhoneContactEntity)

    @Query("select * from "+ AppConstant.PHONE_CONTACT)
    fun getAll(): List<PhoneContactEntity>

    @Query("delete from "+ AppConstant.PHONE_CONTACT)
    fun deleteAll()

    @Query("select PC.sl_no,PC.contact_id,PC.contact_name,\n" +
            "case when PC1.contact_phone IS NULL then '' else PC1.contact_phone END as contact_phone from phone_contact as PC\n" +
            "left join phone_contact1 as PC1 on PC.contact_id = PC1.contact_id\n" +
            "WHERE PC1.contact_phone <> '' and PC1.contact_phone not like '%*%'  ")
    fun getCUstomData() : List<PhoneContactEntity>

/*    @Query("select distinct :grName as gr_name ,contact_id,'' as addr, contact_name as name,contact_phone as number, 0 as isTick from(\n" +
            "\n" +
            "select PC.sl_no,PC.contact_id,PC.contact_name,\n" +
            "case when PC1.contact_phone IS NULL then '' else PC1.contact_phone END as contact_phone from phone_contact as PC\n" +
            "left join phone_contact1 as PC1 on PC.contact_id = PC1.contact_id\n" +
            "WHERE PC1.contact_phone <> '' and PC1.contact_phone not like '%*%'  \n" +
            ") order by name")
    fun getCUstomData1(grName:String) : List<ContactDtls>*/

    @Query("select * from (\n" +
            "select distinct :grName as gr_name ,contact_id,'' as addr, contact_name as name,contact_phone as number, 0 as isTick from(\n" +
            "            select PC.sl_no,PC.contact_id,PC.contact_name,\n" +
            "            case when PC1.contact_phone IS NULL then '' else PC1.contact_phone END as contact_phone from phone_contact as PC\n" +
            "            left join phone_contact1 as PC1 on PC.contact_id = PC1.contact_id\n" +
            "            WHERE PC1.contact_phone <> '' and PC1.contact_phone not like '%*%'  \n" +
            "            ) where number not in (select owner_contact_number from shop_detail) order by name) group by number")
    fun getCUstomData1(grName:String) : List<ContactDtls>

}