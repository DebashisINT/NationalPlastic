package com.breezenationalplasticfsm.features.addshop.model

import com.breezenationalplasticfsm.base.BaseResponse



// Revision History
// 1.0 rev imageListResponse mantis 26103 saheli v 4.0.8 15-05-2023
data class imageListResponse(  var user_id: String? = null,
                               var shop_id: String? = null,
                               var image_list: ArrayList<image_list>? = null) : BaseResponse()



data class image_list(
    val attachment_image1: String,
    val attachment_image2: String,
    val attachment_image3: String,
    val attachment_image4: String
)

// 1.0 start rev imageListResponse mantis 26103 saheli v 4.0.8 15-05-2023

data class ImagestockwiseListResponse(  var user_id: String? = null,
                               var stock_id: String? = null,
                               var stockwise_image_list: ArrayList<Stockwise_image_list>? = null) : BaseResponse()

data class Stockwise_image_list(
    val attachment_stock_image1: String,
    val attachment_stock_image2: String
)

// 1.0 end rev imageListResponse mantis 26103 saheli v 4.0.8 15-05-2023

