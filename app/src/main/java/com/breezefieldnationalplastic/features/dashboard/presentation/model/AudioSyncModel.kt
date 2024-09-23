package com.breezefieldnationalplastic.features.dashboard.presentation.model

import com.facebook.stetho.json.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator

data class AudioSyncModel(var user_id:String="",var session_token:String="",var shop_id:String="",var visit_datetime:String="",var revisitORvisit:String="")