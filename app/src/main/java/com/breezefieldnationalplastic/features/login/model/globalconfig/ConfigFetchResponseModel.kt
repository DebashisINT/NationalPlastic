package com.breezefieldnationalplastic.features.login.model.globalconfig

import com.breezefieldnationalplastic.base.BaseResponse

/**
 * Created by Saikat on 14-01-2019.
 */
//Revision History
// 1.0 ConfigFetchResponseModel AppV 4.0.6  Saheli    25/01/2023  mantis 25623
// 2.0 ConfigFetchResponseModel AppV 4.0.8  Saheli    06/04/2023  mantis 0025780
// 3.0 ConfigFetchResponseModel AppV 4.0.8  Saheli    20/04/2023  mantis 25860
// 4.0 ConfigFetchResponseModel AppV 4.0.8  Saheli    20/04/2023  mantis 26023
// 5.0 ConfigFetchResponseModel AppV 4.0.8  Saheli    12/05/2023  mantis 0026101
class ConfigFetchResponseModel : BaseResponse() {
    var min_accuracy: String? = "200"
    var max_accuracy: String? = "1500"
    var min_distance: String? = null
    var max_distance: String? = "1000"
    var idle_time: String? = null
    var willStockShow: Boolean? = null
    var maxFileSize: String? = "400"
    var willKnowYourStateShow: Boolean? = null
    var willAttachmentCompulsory: Boolean? = null
    var canAddBillingFromBillingList: Boolean? = null
    var willShowUpdateDayPlan: Boolean? = null
    var updateDayPlanText: String? = "Update Day Plan"
    var dailyPlanListHeaderText: String? = "List of Party"
    var allPlanListHeaderText: String? = "Plan/Achievement Details"
    var willSetYourTodaysTargetVisible: Boolean? = null
    var attendenceAlertHeading: String? = ""
    var attendenceAlertText: String? = ""
    var meetingText: String? = ""
    var meetingDistance: String? = ""
    var updateBillingText: String? = ""
    var isRateOnline: Boolean? = null
    var ppText: String = ""
    var ddText: String = ""
    /*var isReplaceShopText: Boolean? = null
    var isQuotationShow: Boolean? = null*/
    var shopText: String = ""
    var isCustomerFeatureEnable: Boolean? = null
    var isAreaVisible: Boolean? = null
    var cgstPercentage: String = ""
    var sgstPercentage: String = ""
    var tcsPercentage: String = ""
    var docAttachmentNo: String = ""
    var chatBotMsg: String = ""
    var contactMail: String = ""
    var isVoiceEnabledForAttendanceSubmit: Boolean? = null
    var isVoiceEnabledForOrderSaved: Boolean? = null
    var isVoiceEnabledForInvoiceSaved: Boolean? = null
    var isVoiceEnabledForCollectionSaved: Boolean? = null
    var isVoiceEnabledForHelpAndTipsInBot: Boolean? = null


    //From Hahnemann
    var isRevisitCaptureImage: Boolean? = null
    var isShowAllProduct: Boolean? = null
    var isPrimaryTargetMandatory: Boolean? = null
    var isStockAvailableForAll: Boolean? = null
    var isStockAvailableForPopup: Boolean? = null
    var isOrderAvailableForPopup: Boolean? = null
    var isCollectionAvailableForPopup: Boolean? = null
    var isDDFieldEnabled: Boolean? = null
    var isActivatePJPFeature: Boolean? = null
    var willReimbursementShow: Boolean? = null

    var GPSAlert: Boolean? = null

    //02-11-2021
    var IsDuplicateShopContactnoAllowedOnline: Boolean? = null

    //26-11-2021
    var BatterySetting: Boolean? = null
    var PowerSaverSetting: Boolean? = null
    /*1-12-2021*/
    var IsnewleadtypeforRuby: Boolean? = null

    /*16-12-2021 return features*/
    var IsReturnActivatedforPP: Boolean? = null
    var IsReturnActivatedforDD: Boolean? = null
    var IsReturnActivatedforSHOP: Boolean? = null


    var FaceRegistrationFrontCamera: Boolean? = null
    var MRPInOrder: Boolean? = null
    var SqMtrRateCalculationforQuotEuro: Double? = null

    var NewQuotationRateCaption: String = ""
    var NewQuotationShowTermsAndCondition: Boolean? = null
    var IsCollectionEntryConsiderOrderOrInvoice: Boolean? = null
    var contactNameText: String = ""
    var contactNumberText: String = ""
    var emailText: String = ""
    var dobText: String = ""
    var dateOfAnniversaryText: String = ""
    var ShopScreenAftVisitRevisit:Boolean? = null
    var IsSurveyRequiredforNewParty:Boolean? = null
    var IsSurveyRequiredforDealer:Boolean? = null
    var IsShowHomeLocationMap:Boolean? = null

    var IsBeatRouteAvailableinAttendance:Boolean? = null
    var IsAllBeatAvailable:Boolean? = null
    var BeatText: String = ""
    var TodaysTaskText:String = ""
    var IsDistributorSelectionRequiredinAttendance:Boolean? = null
    var IsAllowNearbyshopWithBeat:Boolean? = null
    var IsGSTINPANEnableInShop:Boolean? = null
    var IsMultipleImagesRequired:Boolean? = null
    var IsALLDDRequiredforAttendance:Boolean? = null

    var IsShowNewOrderCart:Boolean? = null
    var IsmanualInOutTimeRequired:Boolean? = null
    var surveytext: String = ""
    var IsDiscountInOrder:Boolean? = null
    var IsViewMRPInOrder:Boolean? = null
    var IsShowStateInTeam:Boolean? = null
    var IsShowBranchInTeam:Boolean? = null
    var IsShowDesignationInTeam:Boolean? = null

    var IsShowInPortalManualPhotoRegn:Boolean? = null

    var IsAttendVisitShowInDashboard:Boolean? = null
    var Show_App_Logout_Notification: Boolean? = null//2.0 LocationFuzedService  AppV 4.0.6

    var IsBeatAvailable: Boolean? = null
    var IsDiscountEditableInOrder:Boolean? = null//mantis 25623
    var isExpenseFeatureAvailable:Boolean?=null//mantis 25607
    var IsRouteStartFromAttendance:Boolean?=null

    var IsShowQuotationFooterforEurobond:Boolean?=null
    var IsShowOtherInfoinShopMaster:Boolean?=null

    var IsAllowZeroRateOrder: Boolean? = null

    var ShowApproxDistanceInNearbyShopList: Boolean? = null
    var IsAssignedDDAvailableForAllUser: Boolean? = null// mantis 0025780
    var IsShowEmployeePerformance:Boolean?=null// mantis 25860

    var IsTaskManagementAvailable:Boolean?=null// mantis 26023

    var IsShowPrivacyPolicyInMenu:Boolean?=null// mantis 25912

    var IsAttendanceCheckedforExpense:Boolean?=null
    var IsShowLocalinExpense:Boolean?=null
    var IsShowOutStationinExpense:Boolean?=null
    var IsSingleDayTAApplyRestriction:Boolean?=null
    var IsTAAttachment1Mandatory:Boolean?=null
    var IsTAAttachment2Mandatory:Boolean?=null
    var NameforConveyanceAttachment1:String = ""
    var NameforConveyanceAttachment2:String = ""

    var IsAttachmentAvailableForCurrentStock:Boolean?=null// mantis 0026101

    var IsShowReimbursementTypeInAttendance:Boolean?=null// mantis 26119

    var IsBeatPlanAvailable:Boolean?=null// mantis 26163

    var IsUpdateVisitDataInTodayTable:Boolean?=null


    var IsShowWhatsAppIconforVisit:Boolean?=null
    var IsAutomatedWhatsAppSendforRevisit:Boolean?=null

    var ShopSyncIntervalInMinutes:String? = ""

    var IsAllowBackdatedOrderEntry:Boolean?=null
    var Order_Past_Days:Int?=null

    var Show_distributor_scheme_with_Product:Boolean?=null

    var MultiVisitIntervalInMinutes:String? = ""

    var GSTINPANMandatoryforSHOPTYPE4:Boolean?=null
    var FSSAILicNoEnableInShop:Boolean?=null
    var FSSAILicNoMandatoryInShop4:Boolean?=null


    // Begin puja 16.11.23 mantis-0026997 //

    var isLeadContactNumber:Boolean?=false
    var isModelEnable:Boolean?=false
    var isPrimaryApplicationEnable:Boolean?=false
    var isSecondaryApplicationEnable:Boolean?=false
    var isBookingAmount:Boolean?=false
    var isLeadTypeEnable:Boolean?=false
    var isStageEnable:Boolean?=false
    var isFunnelStageEnable:Boolean?=false
    var IsGPSRouteSync:Boolean?=null
    var IsSyncBellNotificationInApp:Boolean?=null
    var IsShowCustomerLocationShare:Boolean?=true

    //End puja 16.11.23 mantis-0026997 //

    //begin mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 21-02-2024
    var AdditionalInfoRequiredForTimelines:Boolean? = true
    //end mantis id 0027255 AdditionalInfoRequiredForTimelines functionality Puja 21-02-2024

    //begin mantis id 0027279 ShowPartyWithGeoFence functionality Puja 01-03-2024
    var ShowPartyWithGeoFence:Boolean? = false
    //end mantis id 0027279 ShowPartyWithGeoFence functionality Puja 01-03-2024

    //begin mantis id 0027285 ShowPartyWithCreateOrder functionality Puja 01-03-2024
    var ShowPartyWithCreateOrder:Boolean? = false
    //end mantis id 0027285 ShowPartyWithCreateOrder functionality Puja 01-03-2024

    //begin mantis id 0027282 Allow_past_days_for_apply_reimbursement functionality Puja 01-03-2024 v4.2.6
    var Allow_past_days_for_apply_reimbursement:String = ""
    //end mantis id 0027282 Allow_past_days_for_apply_reimbursement functionality Puja 01-03-2024  v4.2.6

    //begin mantis id 0027298 IsShowLeaderBoard functionality Puja 12-03-2024  v4.2.6
    var IsShowLeaderBoard:Boolean? = false
    //end mantis id 0027298 IsShowLeaderBoard functionality Puja 12-03-2024  v4.2.6

    //begin mantis id 0027298 loc_k functionality Puja 08-05-2024  v4.2.7
    var loc_k:String? = ""
    //end mantis id 0027298 loc_k functionality Puja 08-05-2024  v4.2.7

    //begin mantis id 0027298 firebase_k functionality Puja 08-05-2024  v4.2.7
    var firebase_k:String? = ""
    //end mantis id 0027298 firebase_k functionality Puja 08-05-2024  v4.2.7

    //begin mantis id 0027683 QuestionAfterNoOfContentForLMS functionality Puja 05-08-2024  v4.2.9
    var QuestionAfterNoOfContentForLMS:String? = ""
    //end mantis id 0027683 QuestionAfterNoOfContentForLMS functionality Puja 05-08-2024  v4.2.9

    var IsAllowGPSTrackingInBackgroundForLMS:Boolean? = false
    var IsRetailOrderStatusRequired:Boolean? = false

}