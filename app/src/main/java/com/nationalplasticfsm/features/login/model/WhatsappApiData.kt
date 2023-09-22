package com.nationalplasticfsm.features.login.model

import com.nationalplasticfsm.app.domain.VisitRevisitWhatsappStatus

data class WhatsappApiData(val user_id:String,var shop_whatsapp_api_list:ArrayList<VisitRevisitWhatsappStatus>)

data class WhatsappApiFetchData(val status:String,var message:String,var user_id:String,var shop_whatsapp_api_list:ArrayList<VisitRevisitWhatsappStatus>)