package com.breezenationalplasticfsm.features.addshop.presentation

data class ShopListSubmitResponse(var status:String = "",var user_id:String = "",var session_token:String = "",var shop_list:ArrayList<ShopExtraContactResponse> = ArrayList())

data class ShopExtraContactResponse (var shop_id:String = "",
                                     var contact_serial1:String = "1",var contact_name1:String = "", var contact_number1:String = "",var contact_email1:String="",var contact_doa1:String="",var contact_dob1:String="",
                                     var contact_serial2:String = "2",var contact_name2:String = "", var contact_number2:String = "",var contact_email2:String="",var contact_doa2:String="",var contact_dob2:String="",
                                     var contact_serial3:String = "3",var contact_name3:String = "", var contact_number3:String = "",var contact_email3:String="",var contact_doa3:String="",var contact_dob3:String="",
                                     var contact_serial4:String = "4",var contact_name4:String = "", var contact_number4:String = "",var contact_email4:String="",var contact_doa4:String="",var contact_dob4:String="",
                                     var contact_serial5:String = "5",var contact_name5:String = "", var contact_number5:String = "",var contact_email5:String="",var contact_doa5:String="",var contact_dob5:String="",
                                     var contact_serial6:String = "6",var contact_name6:String = "", var contact_number6:String = "",var contact_email6:String="",var contact_doa6:String="",var contact_dob6:String="")


data class multiContactRequestData(var user_id:String = "",var session_token:String = "",var shop_list:ArrayList<ShopExtraContactReq> = ArrayList())

data class ShopExtraContactReq (var shop_id:String = "",
                                     var contact_serial1:String = "1",var contact_name1:String = "", var contact_number1:String = "",var contact_email1:String="",var contact_doa1:String="",var contact_dob1:String="",
                                     var contact_serial2:String = "2",var contact_name2:String = "", var contact_number2:String = "",var contact_email2:String="",var contact_doa2:String="",var contact_dob2:String="",
                                     var contact_serial3:String = "3",var contact_name3:String = "", var contact_number3:String = "",var contact_email3:String="",var contact_doa3:String="",var contact_dob3:String="",
                                     var contact_serial4:String = "4",var contact_name4:String = "", var contact_number4:String = "",var contact_email4:String="",var contact_doa4:String="",var contact_dob4:String="",
                                     var contact_serial5:String = "5",var contact_name5:String = "", var contact_number5:String = "",var contact_email5:String="",var contact_doa5:String="",var contact_dob5:String="",
                                     var contact_serial6:String = "6",var contact_name6:String = "", var contact_number6:String = "",var contact_email6:String="",var contact_doa6:String="",var contact_dob6:String="",)