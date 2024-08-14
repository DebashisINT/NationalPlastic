package com.breezenationalplasticfsm.features.login.api.productlistapi

import com.breezenationalplasticfsm.app.NetworkConstant
import com.breezenationalplasticfsm.app.domain.ProductListEntity
import com.breezenationalplasticfsm.base.BaseResponse
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListOfflineResponseModel
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListOfflineResponseModelNew
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductListResponseModel
import com.breezenationalplasticfsm.features.login.model.productlistmodel.ProductRateListResponseModel
import com.breezenationalplasticfsm.features.orderITC.GetOrderHistory
import com.breezenationalplasticfsm.features.orderITC.GetProductRateReq
import com.breezenationalplasticfsm.features.orderITC.GetProductReq
import com.breezenationalplasticfsm.features.orderITC.SyncOrd
import com.breezenationalplasticfsm.features.viewAllOrder.orderOptimized.ProductRateOnlineListResponseModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Saikat on 20-11-2018.
 */
interface ProductListApi {
    @FormUrlEncoded
    @POST("ProductList/List")
    fun getProductList(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("last_update_date") last_update_date: String): Observable<ProductListResponseModel>


    @FormUrlEncoded
    @POST("ProductList/ProductRate")
    fun getProductRateList(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("shop_id") shop_id: String): Observable<ProductRateListResponseModel>

    @FormUrlEncoded
    @POST("ProductList/ProductRate")
    fun getProductRateOnlineList(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("shop_id") shop_id: String): Observable<ProductRateOnlineListResponseModel>


    @FormUrlEncoded
    @POST("ProductList/OfflineProductRate")
    fun getOfflineProductRateList(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<ProductListOfflineResponseModel>

    @FormUrlEncoded
    @POST("ProductList/OfflineProductRate")
    fun getOfflineProductRateListNew(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<ProductListOfflineResponseModelNew>

    @POST("ITCOrderWithProductDetail/ITCOrderWithProductDetailSave")
    fun syncProductListITC(@Body addOrder: SyncOrd): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("ProductList/ITCProdMastList")
    fun getProductListITC(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<GetProductReq>

    @FormUrlEncoded
    @POST("ProductList/ITCProdRateList")
    fun getProductRateListITC(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<GetProductRateReq>

    @FormUrlEncoded
    @POST("ITCOrderWithProductDetail/ITCListForOrderedProduct")
    fun getOrderHistoryApi( @Field("user_id") user_id: String): Observable<GetOrderHistory>


    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): ProductListApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(ProductListApi::class.java)
        }
    }
}