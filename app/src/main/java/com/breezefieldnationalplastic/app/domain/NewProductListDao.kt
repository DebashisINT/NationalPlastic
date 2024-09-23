package com.breezedsm.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldnationalplastic.app.AppConstant
import com.breezefieldnationalplastic.features.orderITC.CommonProductCatagory
import com.breezefieldnationalplastic.features.orderITC.ProductRateList

@Dao
interface NewProductListDao {

    @Query("SELECT * FROM " + AppConstant.NEW_PRODUCT_LIST)
    fun getAll(): List<NewProductListEntity>

    @Insert
    fun insert(vararg model: NewProductListEntity)

    @Query("delete FROM " + AppConstant.NEW_PRODUCT_LIST)
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAllLarge(model: List<NewProductListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<NewProductListEntity>)

    @Query("select PL.product_id,PL.product_name,PL.brand_id,PL.brand_name,PL.UOM,\n" +
            "       PL.category_id,PL.category_name,PL.watt_id,PL.watt_name,\n" +
            "\t   case when PR.mrp IS NULL then '0.00' else PR.mrp END as mrp,\n" +
            "\t   case when PR.item_price IS NULL then '0.00' else PR.item_price END as item_price,\n" +
            "\t   case when PR.specialRate IS NULL then '0.00' else PR.specialRate END as specialRate\n" +
            "\t  from (select * from new_product_list) as PL\n" +
            "left join (select * from new_rate_list) as PR \n" +
            "on PL.product_id = PR.product_id")
    fun getProductRateL(): List<ProductRateList>

    @Query("select PL.product_id,PL.product_name,PL.brand_id,PL.brand_name,PL.UOM,\n" +
            "       PL.category_id,PL.category_name,PL.watt_id,PL.watt_name,\n" +
            "\t   case when PR.mrp IS NULL then '0.00' else PR.mrp END as mrp,\n" +
            "\t   case when PR.item_price IS NULL then '0.00' else PR.item_price END as item_price,\n" +
            "\t   case when PR.specialRate IS NULL then '0.00' else PR.specialRate END as specialRate\n" +
            "\t from (select * from new_product_list) as PL\n" +
            "left join (select * from new_rate_list) as PR \n" +
            "on PL.product_id = PR.product_id where (PL.product_name LIKE '%' || :search_param || '%' or PL.brand_name LIKE '%' || :search_param || '%' \n" +
            "or PL.category_name LIKE '%' || :search_param || '%' or PL.watt_name LIKE '%' || :search_param || '%') ")
    fun getProductRateFilteredL(search_param:String): List<ProductRateList>

    @Query("select PL.product_id,PL.product_name,PL.brand_id,PL.brand_name,PL.UOM,\n" +
            "       PL.category_id,PL.category_name,PL.watt_id,PL.watt_name,\n" +
            "\t   case when PR.mrp IS NULL then '0.00' else PR.mrp END as mrp,\n" +
            "\t   case when PR.item_price IS NULL then '0.00' else PR.item_price END as item_price,\n" +
            "\t   case when PR.specialRate IS NULL then '0.00' else PR.specialRate END as specialRate\n" +
            "\t from (select * from new_product_list) as PL\n" +
            "left join (select * from new_rate_list) as PR \n" +
            "on PL.product_id = PR.product_id where (PL.brand_id=:brandID) ")
    fun getProductRateFilteredLByBrand(brandID:String): List<ProductRateList>

    @Query("select PL.product_id,PL.product_name,PL.brand_id,PL.brand_name,PL.UOM,\n" +
            "       PL.category_id,PL.category_name,PL.watt_id,PL.watt_name,\n" +
            "\t   case when PR.mrp IS NULL then '0.00' else PR.mrp END as mrp,\n" +
            "\t   case when PR.item_price IS NULL then '0.00' else PR.item_price END as item_price,\n" +
            "\t   case when PR.specialRate IS NULL then '0.00' else PR.specialRate END as specialRate\n" +
            "\t from (select * from new_product_list) as PL\n" +
            "left join (select * from new_rate_list) as PR \n" +
            "on PL.product_id = PR.product_id where (PL.brand_id=:brandID and PL.category_id=:categoryID) ")
    fun getProductRateFilteredLByBrandCategory(brandID:String,categoryID:String): List<ProductRateList>

    @Query("select PL.product_id,PL.product_name,PL.brand_id,PL.brand_name,PL.UOM,\n" +
            "       PL.category_id,PL.category_name,PL.watt_id,PL.watt_name,\n" +
            "\t   case when PR.mrp IS NULL then '0.00' else PR.mrp END as mrp,\n" +
            "\t   case when PR.item_price IS NULL then '0.00' else PR.item_price END as item_price,\n" +
            "\t   case when PR.specialRate IS NULL then '0.00' else PR.specialRate END as specialRate\n" +
            "\t from (select * from new_product_list) as PL\n" +
            "left join (select * from new_rate_list) as PR \n" +
            "on PL.product_id = PR.product_id where (PL.brand_id=:brandID and PL.category_id=:categoryID and PL.watt_id=:wattID) ")
    fun getProductRateFilteredLByBrandCategoryWatt(brandID:String,categoryID:String,wattID:String): List<ProductRateList>

    @Query("select DISTINCT  brand_id as id_sel,brand_name as name_sel from new_product_list")
    fun getDistinctBrandList(): List<CommonProductCatagory>

    @Query("select DISTINCT  category_id as id_sel,category_name as name_sel from new_product_list where brand_id=:selGrIDStr")
    fun getDistinctCategoryList(selGrIDStr:String): List<CommonProductCatagory>

    @Query("select DISTINCT  watt_id as id_sel,watt_name as name_sel from new_product_list where brand_id=:selGrIDStr and category_id=:selCategoryIDStr")
    fun getDistinctMeasureList(selGrIDStr:String,selCategoryIDStr:String): List<CommonProductCatagory>

    @Query("select * from new_product_list where product_id=:product_id")
    fun getProductDtls(product_id:String): NewProductListEntity
}