package com.breezenationalplasticfsm.app.domain

import androidx.room.*
import com.breezenationalplasticfsm.app.AppConstant
import com.breezenationalplasticfsm.features.contacts.ProductDtls
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListDataModel
import com.breezenationalplasticfsm.features.viewAllOrder.orderOptimized.CommonProductCatagory
import com.breezenationalplasticfsm.features.viewAllOrder.orderOptimized.CustomProductRate

/**
 * Created by Saikat on 08-11-2018.
 */
@Dao
interface ProductListDao {

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE)
    fun getAll(): List<ProductListEntity>
    
    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE +" where id IN (SELECT id FROM product_list group by brand)")
    fun getUniqueBrandList(): List<ProductListEntity>

    @Query("SELECT product_name FROM " + AppConstant.PRODUCT_LIST_TABLE)
    fun getNameAll(): List<String>

    ////nw code new
    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate1 as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id ")
    fun getCustomizeProductListAll(): List<CustomProductRate>


    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate1 IS NULL then '0.00' else PR.rate1 END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.00' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id ")
    fun getCustomizeProductListAllV1(): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,PR.Qty_per_Unit,PR.Scheme_Qty,PR.Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id ")
    fun getCustomizeProductListAllFromOnline(): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate IS NULL then '0.0' else PR.rate END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "case when PR.Qty_per_Unit IS NULL then '0.0' else PR.Qty_per_Unit END as Qty_per_Unit,\n" +
            "case when PR.Scheme_Qty IS NULL then '0.0' else PR.Scheme_Qty END as Scheme_Qty,\n" +
            "case when PR.Effective_Rate IS NULL then '0.0' else PR.Effective_Rate END as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id ")
    fun getCustomizeProductListAllFromOnlineV1(): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate1 as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id")
    fun getCustomizeProductListByBeandID(brand_id:String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate1 IS NULL then '0.0' else PR.rate1 END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "0.0 as Qty_per_Unit,0.0 as Scheme_Qty,\n" +
            "0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id where brand_id=:brand_id")
    fun getCustomizeProductListByBeandIDV1(brand_id:String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,PR.Qty_per_Unit,PR.Scheme_Qty,PR.Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id")
    fun getCustomizeProductListByBeandIDFromOnlineRate(brand_id:String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate IS NULL then '0.0' else PR.rate END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "case when PR.Qty_per_Unit IS NULL then '0.0' else PR.Qty_per_Unit END as Qty_per_Unit,\n" +
            "case when PR.Scheme_Qty IS NULL then '0.0' else PR.Scheme_Qty END as Scheme_Qty,\n" +
            "case when PR.Effective_Rate IS NULL then '0.0' else PR.Effective_Rate END as Effective_Rate\n" +
            "from\n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id where brand_id=:brand_id")
    fun getCustomizeProductListByBeandIDFromOnlineRateV1(brand_id:String): List<CustomProductRate>


    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate1 as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id and category_id=:category_id")
    fun getCustomizeProductListByBeandIDCategoryID(brand_id:String, category_id: String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate1 IS NULL then '0.0' else PR.rate1 END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id where (brand_id=:brand_id and category_id=:category_id)")
    fun getCustomizeProductListByBeandIDCategoryIDV1(brand_id:String, category_id: String): List<CustomProductRate>


    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,PR.Qty_per_Unit,PR.Scheme_Qty,PR.Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id and category_id=:category_id")
    fun getCustomizeProductListByBeandIDCategoryIDFromOnlineRate(brand_id:String, category_id: String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate IS NULL then '0.0' else PR.rate END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "case when PR.Qty_per_Unit IS NULL then '0.0' else PR.Qty_per_Unit END as Qty_per_Unit,\n" +
            "case when PR.Scheme_Qty IS NULL then '0.0' else PR.Scheme_Qty END as Scheme_Qty,\n" +
            "case when PR.Effective_Rate IS NULL then '0.0' else PR.Effective_Rate END as Effective_Rate\n" +
            "from\n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id where (brand_id=:brand_id and category_id =:category_id)")
    fun getCustomizeProductListByBeandIDCategoryIDFromOnlineRateV1(brand_id:String, category_id: String): List<CustomProductRate>


    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate1 as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id and category_id=:category_id and watt_id=:watt_id")
    fun getCustomizeProductListByBeandIDCategoryIDWattID(brand_id:String, category_id: String,watt_id: String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate1 IS NULL then '0.0' else PR.rate1 END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "0.0 as Qty_per_Unit,0.0 as Scheme_Qty,0.0 as Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_rate) as PR\n" +
            "on PL.id = PR.product_id where (brand_id=:brand_id and category_id=:category_id and watt_id=:watt_id)")
    fun getCustomizeProductListByBeandIDCategoryIDWattIDV1(brand_id:String, category_id: String,watt_id: String): List<CustomProductRate>


    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show," +
            "PL.product_discount_show,PR.rate as rate,PR.stock_amount,PR.stock_unit,PR.isStockShow,PR.isRateShow,PR.Qty_per_Unit,PR.Scheme_Qty,PR.Effective_Rate from \n" +
            "(select * from product_list) as PL\n" +
            "inner JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id and brand_id=:brand_id and category_id=:category_id and watt_id=:watt_id")
    fun getCustomizeProductListByBeandIDCategoryIDWattIDFromOnlineRate(brand_id:String, category_id: String,watt_id: String): List<CustomProductRate>

    @Query("select PL.id as product_id,PL.product_name,PL.brand_id,PL.brand,PL.category_id,PL.category,PL.watt_id,PL.watt,PL.product_mrp_show,\n" +
            "PL.product_discount_show,\n" +
            "case when PR.rate IS NULL then '0.0' else PR.rate END as rate,\n" +
            "case when PR.stock_amount IS NULL then '0.0' else PR.stock_amount END as stock_amount,\n" +
            "case when PR.stock_unit IS NULL then 'Units' else PR.stock_unit END as stock_unit,\n" +
            "case when PR.isStockShow IS NULL then '0' else PR.isStockShow END as isStockShow,\n" +
            "case when PR.isRateShow IS NULL then '1' else PR.isRateShow END as isRateShow,\n" +
            "case when PR.Qty_per_Unit IS NULL then '0.0' else PR.Qty_per_Unit END as Qty_per_Unit,\n" +
            "case when PR.Scheme_Qty IS NULL then '0.0' else PR.Scheme_Qty END as Scheme_Qty,\n" +
            "case when PR.Effective_Rate IS NULL then '0.0' else PR.Effective_Rate END as Effective_Rate\n" +
            "from\n" +
            "(select * from product_list) as PL\n" +
            "left JOIN\n" +
            "(select * from product_online_rate_temp_table) as PR\n" +
            "on PL.id = PR.product_id where (brand_id=:brand_id and category_id = :category_id and watt_id = :watt_id)")
    fun getCustomizeProductListByBeandIDCategoryIDWattIDFromOnlineRateV1(brand_id:String, category_id: String,watt_id: String): List<CustomProductRate>

    @Query("select DISTINCT  brand_id as id_sel,brand as name_sel from product_list")
    fun getDistinctBrandList(): List<CommonProductCatagory>

    @Query("select DISTINCT category_id as id_sel,category as name_sel from product_list where brand_id =:brand_id ")
    fun getDistinctCategoryList(brand_id:String): List<CommonProductCatagory>

    @Query("SELECT DISTINCT watt_id as id_sel,watt as name_sel FROM product_list where brand_id=:brand_id and category_id=:category_id ")
    fun getDistinctWattList(brand_id:String,category_id:String): List<CommonProductCatagory>

    @Query("SELECT DISTINCT watt_id as id_sel,watt as name_sel FROM product_list where brand_id=:brand_id ")
    fun getDistinctWattList1(brand_id:String): List<CommonProductCatagory>
    ////nw code end




    @Query("SELECT Distinct(brand) FROM " + AppConstant.PRODUCT_LIST_TABLE + " order by brand")
    fun getBrandList(): List<String>

    @Query("SELECT Distinct(brand) FROM " + AppConstant.PRODUCT_LIST_TABLE + " where category=:category COLLATE NOCASE")
    fun getBrandListAccordingToCategory(category: String): List<String>

    @Query("SELECT Distinct(category) FROM " + AppConstant.PRODUCT_LIST_TABLE)
    fun getCategoryList(): List<String>

    @Query("SELECT Distinct(category) FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand COLLATE NOCASE order by category")
    fun getCategoryListAccordingToBrand(brand: String): List<String>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id and id IN (SELECT id FROM product_list group by category) COLLATE NOCASE order by category")
    fun getCategoryListAccordingToBrandId(brand_id: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand and category=:category COLLATE NOCASE")
    fun getAllValueAccordingToCategoryBrand(brand: String, category: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id and category_id=:category_id COLLATE NOCASE")
    fun getAllValueAccordingToCategoryBrandId(brand_id: String, category_id: String): List<ProductListEntity>

    /*@Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand and category=:category order by cast(watt as int)")
    fun getAllValueAccordingToCategoryBrandFilteredByWatt(brand: String, category: String): List<ProductListEntity>*/

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand and category=:category and watt=:watt COLLATE NOCASE")
    fun getAllValueAccordingToCategoryBrandFilteredByWatt(brand: String, category: String, watt: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id and category_id=:category_id and watt_id=:watt_id COLLATE NOCASE")
    fun getAllValueAccordingToCategoryBrandFilteredByWattId(brand_id: String, category_id: String, watt_id: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand COLLATE NOCASE")
    fun getAllValueAccordingToBrand(brand: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id COLLATE NOCASE")
    fun getAllValueAccordingToBrandId(brand_id: String): List<ProductListEntity>

    /*@Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand order by cast(watt as int)")
    fun getAllValueAccordingToBrandWattWise(brand: String): List<ProductListEntity>*/

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand and watt=:watt COLLATE NOCASE")
    fun getAllValueAccordingToBrandWattWise(brand: String, watt: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id and watt_id=:watt_id  COLLATE NOCASE")
    fun getAllValueAccordingToBrandWattIdWise(brand_id: String, watt_id: String): List<ProductListEntity>

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where category=:category")
    fun getAllValueAccordingToCategory(category: String): List<ProductListEntity>

    @Query("SELECT Distinct(watt) FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand COLLATE NOCASE")
    fun getWattListBrandWise(brand: String): List<String>

    /*@Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id and id IN (SELECT id FROM product_list group by watt) COLLATE NOCASE")
    fun getWattListBrandIdWise(brand_id: String): List<ProductListEntity>*/

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id COLLATE NOCASE group by watt")
    fun getWattListBrandIdWise(brand_id: String): List<ProductListEntity>

    @Query("SELECT Distinct(watt) FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand=:brand and category=:category COLLATE NOCASE")
    fun getWattListBrandCategoryWise(brand: String, category: String): List<String>

    /*@Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id  and category_id=:category_id and " +
            "id IN (SELECT id FROM product_list group by watt) COLLATE NOCASE")
    fun getWattListBrandCategoryIdWise(brand_id: String, category_id: String): List<ProductListEntity>*/

    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE + " where brand_id=:brand_id  and category_id=:category_id COLLATE NOCASE group by watt")
    fun getWattListBrandCategoryIdWise(brand_id: String, category_id: String): List<ProductListEntity>

    @Query("DELETE FROM " + AppConstant.PRODUCT_LIST_TABLE)
    fun deleteAllProduct()

    @Insert
    fun insert(vararg leaveType: ProductListEntity)

/*    @Insert
    fun insertAll(vararg product_list: ArrayList<ProductListEntity>)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<ProductListEntity>)


    @Query("SELECT * FROM " + AppConstant.PRODUCT_LIST_TABLE +" where id=:id")
    fun getSingleProduct(id: Int): ProductListEntity

    @Query("select id as product_id,product_name,0 as isTick from product_list")
    fun getProducts(): List<ProductDtls>

    @Query("select id as product_id,product_name," +
            "case when id in (:id_list) then 1 else 0 end as isTick " +
            "from product_list")
    fun getSelectedProductIdList(id_list: List<String>): List<ProductDtls>

}