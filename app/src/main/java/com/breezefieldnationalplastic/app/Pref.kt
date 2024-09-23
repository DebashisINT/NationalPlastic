package com.breezefieldnationalplastic.app

import com.marcinmoskala.kotlinpreferences.PreferenceHolder

/**
 * Created by Pratishruti on 08-11-2017.
 */
// Revision History
// 1.0 Pref  AppV 4.0.6 Saheli    16/01/2023 Update beat feature
// 2.0 Pref  AppV 4.0.6 Saheli    25/01/2023 mantis 25623
// 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650
// 4.0 Pref  AppV 4.0.7 Suman    23/03/2023 ShowApproxDistanceInNearbyShopList Show approx distance in nearby + shopmaster  mantis 0025742
// 5.0 Pref  AppV 4.0.8 Saheli   06/04/2023 mantis 0025780 IsAssignedDDAvailableForAllUser Useds LoginActivity If this feature 'On' then Assigned DD [Assigned DD Table] shall be available in 'Shop Master'
// Rev 6.0 Pref AppV 4.0.8 Suman    26/04/2023 mail repetation fix 25923
// Rev 7.0 Pref AppV 4.0.8 Suman    02/05/2023 mantis id 25979
//Rev 8.0 Pref AppV 4.0.8 Saheli    05/05/2023  mantis 0026023
// Rev 9.0 Pref AppV 4.0.8 Saheli    12/05/2023  mantis 0026101
// Rev 10.0 Pref AppV 4.0.8 Suman    16/05/2023  mantis 26119
// Rev 11.0 Pref AppV 4.0.8 Suman    19/05/2023  mantis 26163
// 12.0 Pref v 4.1.6 Tufan 11/07/2023 mantis 26546 revisit sync time
// 13.0 Pref v 4.1.6 Suman 13/07/2023 mantis 26555 Usersettings
// 14.0 Pref v 4.1.6 Tufan 11/08/2023 mantis 26655 Order Past Days
// 15.0 Pref v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
// 16.0 Pref v 4.1.6 Tufan 07/09/2023 mantis 26785 Multi visit Interval in Minutes Against the Same Shop
//Begin 16.0 Pref v 4.1.6 Tufan 21/09/2023 mantis 26812 AND 26813  FSSAI Lic No and GSTINPANMandatoryforSHOPTYPE4 In add shop page edit
//Rev 17.0 Pref v 4.2.8 Suman 25/06/2024 mantis 27575

object Pref : PreferenceHolder() {
    var text: String? by bindToPreferenceFieldNullable()
    var num: Int by bindToPreferenceField(0, "SomeIntKey")
    var login_time: String? by bindToPreferenceFieldNullable()
    var add_attendence_time: String? by bindToPreferenceFieldNullable()
    var logout_time: String? by bindToPreferenceFieldNullable()
    var appLaunchDate: String? by bindToPreferenceFieldNullable()
    var latitude: String? by bindToPreferenceFieldNullable()
    var longitude: String? by bindToPreferenceFieldNullable()
    var merediam: String? by bindToPreferenceFieldNullable()
    var prevTimeStamp: Long by bindToPreferenceField(0, "prevTimeStamp")
    var user_id: String? by bindToPreferenceFieldNullable()
    var isLocFuzedBroadPlaying: Boolean by bindToPreferenceField(false, "isLocFuzedBroadPlaying")
    var temp_user_id: String? by bindToPreferenceFieldNullable()
    var session_token: String? by bindToPreferenceFieldNullable()
    var login_date_time: String? by bindToPreferenceFieldNullable()
    var user_name: String? by bindToPreferenceFieldNullable()
    var isLogoutInitiated: Boolean by bindToPreferenceField(false, "SomeIntKey")
    var imei: String by bindToPreferenceField("", "imei")
    var totalTimeSpenAtShop: String by bindToPreferenceField("0", "totalTimeSpenAtShop")
    var user_profile_img: String by bindToPreferenceField("", "userProfileImg")
    var totalShopVisited: String by bindToPreferenceField("0", "totalShopVisited")
    var totalAttendance: String by bindToPreferenceField("0", "totalAttendance")
    var isAutoLogout: Boolean by bindToPreferenceField(false, "isAutoLogout")
    var login_date: String? by bindToPreferenceFieldNullable()
    var temp_login_date: String? by bindToPreferenceFieldNullable()
    var isLocationActivitySynced: Boolean by bindToPreferenceField(true, "isLocationActivitySynced")
    var prevShopActivityTimeStamp: Long by bindToPreferenceField(0, "prevTimeStamp")
    var prevContactSyncTimeStamp: Long by bindToPreferenceField(0, "prevContactSyncTimeStamp")
    var prevLocationActivityTimeStamp: Long by bindToPreferenceField(0, "prevLocTimeStamp")
    var shouldCallHisSync: Long by bindToPreferenceField(0, "shouldCallHisSync")
    var prevIdealLocationActivityTimeStamp: Long by bindToPreferenceField(
        0,
        "prevIdealLocTimeStamp"
    )
    var prevShopActivityTimeStampMonitorService: Long by bindToPreferenceField(
        0,
        "prevTimeStampMonitor"
    )
    var prevShopDurationTimeStamp: Long by bindToPreferenceField(0, "prevShopDurationTimeStamp")
    var prevMeetingDurationTimeStamp: Long by bindToPreferenceField(
        0,
        "prevMeetingDurationTimeStamp"
    )
    var prevLocNotiDurationTimeStamp: Long by bindToPreferenceField(
        0,
        "prevLocNotiDurationTimeStamp"
    )
    var prevHomeLocReasonTimeStamp: Long by bindToPreferenceField(0, "prevHomeLocReasonTimeStamp")
    var prevBatNetSaveTimeStamp: Long by bindToPreferenceField(0, "prevBatNetSaveTimeStamp")
    var prevBatNetSyncTimeStamp: Long by bindToPreferenceField(0, "prevBatNetSyncTimeStamp")
    var isLoginInitiated: Boolean by bindToPreferenceField(false, "isLoginInitiated")
    var profile_img: String by bindToPreferenceField("", "profile_img")
    var profile_state: String by bindToPreferenceField("", "profile_state")
    var profile_city: String by bindToPreferenceField("", "profile_city")
    var profile_pincode: String by bindToPreferenceField("", "profile_pincode")
    var profile_address: String by bindToPreferenceField("", "profile_address")
    var profile_country: String by bindToPreferenceField("", "profile_country")
    var isProfileUpdated: Boolean by bindToPreferenceField(false, "isProfileUpdated")
    var isGeoFenceAdded: Boolean by bindToPreferenceField(false, "isGeoFenceAdded")
    var isMarketingImgSynched: Boolean by bindToPreferenceField(false, "isMarketingImgSynched")
    var current_latitude: String by bindToPreferenceField("", "current_latitude")
    var current_longitude: String by bindToPreferenceField("", "current_longitude")
    var current_address: String by bindToPreferenceField("", "current_address")
    var current_pincode: String by bindToPreferenceField("", "current_pincode")
    var isAddAttendence: Boolean by bindToPreferenceField(false, "isAddAttendence")
    var gpsAccuracy: String by bindToPreferenceField("100", "gpsAccuracy")
    var source_latitude: String by bindToPreferenceField("", "source_latitude")
    var source_longitude: String by bindToPreferenceField("", "source_longitude")
    var isOnLeave: String by bindToPreferenceField("", "isOnLeave")
    var logout_latitude: String by bindToPreferenceField("0.0", "logout_latitude")
    var logout_longitude: String by bindToPreferenceField("0.0", "logout_longitude")
    var totalS2SDistance: String by bindToPreferenceField("0.0", "totalS2SDistance")
    var tempDistance: String by bindToPreferenceField("0.0", "tempDistance")
    var willAlarmTrigger: Boolean by bindToPreferenceField(false, "willAlarmTrigger")
    var isShopVisited: Boolean by bindToPreferenceField(false, "isShopVisited")
    var deviceToken: String by bindToPreferenceField("", "deviceToken")
    var isSeenTermsConditions: Boolean by bindToPreferenceField(false, "isSeenTermsConditions")
    var termsConditionsText: String by bindToPreferenceField("", "termsConditionsText")
    var prevOrderCollectionCheckTimeStamp: Long by bindToPreferenceField(
        0,
        "prevOrderCollectionCheckTimeStamp"
    )
    var isHomeLocAvailable: Boolean by bindToPreferenceField(false, "isHomeLocAvailable")
    var approvedInTime: String by bindToPreferenceField("", "approvedInTime")
    var approvedOutTime: String by bindToPreferenceField("", "approvedOutTime")
    var home_latitude: String by bindToPreferenceField("", "home_latitude")
    var home_longitude: String by bindToPreferenceField("", "home_longitude")
    var isFieldWorkVisible: String by bindToPreferenceField("", "isFieldWorkVisible")
    var willStockShow: Boolean by bindToPreferenceField(false, "willStockShow")
    var maxFileSize: String by bindToPreferenceField("400", "maxFileSize")
    var willKnowYourStateShow: Boolean by bindToPreferenceField(false, "willKnowYourStateShow")
    var willAttachmentCompulsory: Boolean by bindToPreferenceField(true, "willAttachmentCompulsory")
    var canAddBillingFromBillingList: Boolean by bindToPreferenceField(
        true,
        "canAddBillingFromBillingList"
    )
    var willShowUpdateDayPlan: Boolean by bindToPreferenceField(false, "willShowUpdateDayPlan")
    var updateDayPlanText: String by bindToPreferenceField("", "updateDayPlanText")
    var dailyPlanListHeaderText: String by bindToPreferenceField("", "dailyPlanListHeaderText")
    var allPlanListHeaderText: String by bindToPreferenceField("", "allPlanListHeaderText")
    var willSetYourTodaysTargetVisible: Boolean by bindToPreferenceField(
        false,
        "willSetYourTodaysTargetVisible"
    )
    var attendenceAlertHeading: String by bindToPreferenceField(
        "Attendance Confirmation",
        "attendenceAlertHeading"
    )
    var attendenceAlertText: String by bindToPreferenceField(
        "Do you want submit attendance?",
        "attendenceAlertText"
    )
    var isRateNotEditable: Boolean by bindToPreferenceField(false, "isRateNotEditable")
    var isMeetingAvailable: Boolean by bindToPreferenceField(false, "isMeetingAvailable")
    var meetingText: String by bindToPreferenceField("Meeting", "meetingText")
    var meetingDistance: String by bindToPreferenceField("30", "meetingDistance")
    var willLeaveApprovalEnable: Boolean by bindToPreferenceField(false, "willLeaveApprovalEnable")
    var willReportShow: Boolean by bindToPreferenceField(false, "willReportShow")
    var isFingerPrintMandatoryForAttendance: Boolean by bindToPreferenceField(
        false,
        "isFingerPrintMandatoryForAttendance"
    )
    var isFingerPrintMandatoryForVisit: Boolean by bindToPreferenceField(
        false,
        "isFingerPrintMandatoryForVisit"
    )
    var isSelfieMandatoryForAttendance: Boolean by bindToPreferenceField(
        false,
        "isSelfieMandatoryForAttendance"
    )
    var willAttendanceReportShow: Boolean by bindToPreferenceField(
        false,
        "willAttendanceReportShow"
    )
    var willPerformanceReportShow: Boolean by bindToPreferenceField(
        false,
        "willPerformanceReportShow"
    )
    var willVisitReportShow: Boolean by bindToPreferenceField(false, "willVisitReportShow")
    var attendance_text: String by bindToPreferenceField("10.30 AM", "attendance_text")
    var updateBillingText: String by bindToPreferenceField(
        "Update Billing Details Within 3 Days",
        "updateBillingText"
    )
    var willTimesheetShow: Boolean by bindToPreferenceField(false, "willTimesheetShow")
    var isAttendanceFeatureOnly: Boolean by bindToPreferenceField(false, "isAttendanceFeatureOnly")
    var isCollectioninMenuShow: Boolean by bindToPreferenceField(false, "iscollectioninMenuShow")
    var isVisitShow: Boolean by bindToPreferenceField(false, "isVisitShow")
    var isOrderShow: Boolean by bindToPreferenceField(false, "isOrderShow")
    var isShopAddEditAvailable: Boolean by bindToPreferenceField(false, "isShopAddEditAvailable")
    var isEntityCodeVisible: Boolean by bindToPreferenceField(false, "isEntityCodeVisible")
    var isRateOnline: Boolean by bindToPreferenceField(false, "isRateOnline")
    var isAreaMandatoryInPartyCreation: Boolean by bindToPreferenceField(
        false,
        "isAreaMandatoryInPartyCreation"
    )
    var isShowPartyInAreaWiseTeam: Boolean by bindToPreferenceField(
        false,
        "isShowPartyInAreaWiseTeam"
    )
    var isChangePasswordAllowed: Boolean by bindToPreferenceField(false, "isChangePasswordAllowed")
    var isHomeRestrictAttendance: String by bindToPreferenceField("0", "isHomeRestrictAttendance")
    var ppText: String by bindToPreferenceField("PP", "ppText")
    var ddText: String by bindToPreferenceField("DD", "ddText")
    var isReplaceShopText: Boolean by bindToPreferenceField(false, "isReplaceShopText")
    var isQuotationShow: Boolean by bindToPreferenceField(false, "isQuotationShow")
    var shopText: String by bindToPreferenceField("Shop", "shopText")
    var isCustomerFeatureEnable: Boolean by bindToPreferenceField(false, "isCustomerFeatureEnable")
    var isAreaVisible: Boolean by bindToPreferenceField(false, "isAreaVisible")
    var cgstPercentage: String by bindToPreferenceField("14", "cgstPercentage")
    var sgstPercentage: String by bindToPreferenceField("14", "sgstPercentage")
    var tcsPercentage: String by bindToPreferenceField("0.75", "tcsPercentage")
    var isQuotationPopupShow: Boolean by bindToPreferenceField(false, "isQuotationPopupShow")
    var homeLocDistance: String by bindToPreferenceField("50", "homeLocDistance")
    var shopLocAccuracy: String by bindToPreferenceField("500", "shopLocAccuracy")
    var isMultipleAttendanceSelection: Boolean by bindToPreferenceField(
        false,
        "isMultipleAttendanceSelection"
    )
    var isOrderReplacedWithTeam: Boolean by bindToPreferenceField(false, "isOrderReplacedWithTeam")
    var isSefieAlarmed: Boolean by bindToPreferenceField(false, "isSefieAlarmed")
    var isDDShowForMeeting: Boolean by bindToPreferenceField(false, "isDDShowForMeeting")
    var isDDMandatoryForMeeting: Boolean by bindToPreferenceField(false, "isDDMandatoryForMeeting")
    var isOfflineTeam: Boolean by bindToPreferenceField(false, "isOfflineTeam")
    var supervisor_name: String by bindToPreferenceField("", "supervisor_name")
    var client_text: String by bindToPreferenceField("", "client_text")
    var product_text: String by bindToPreferenceField("", "product_text")
    var activity_text: String by bindToPreferenceField("", "activity_text")
    var project_text: String by bindToPreferenceField("", "project_text")
    var time_text: String by bindToPreferenceField("", "time_text")
    var comment_text: String by bindToPreferenceField("", "comment_text")
    var submit_text: String by bindToPreferenceField("", "submit_text")
    var timesheet_past_days: String by bindToPreferenceField("30", "timesheet_past_days")
    var isAllTeamAvailable: Boolean by bindToPreferenceField(false, "isAllTeamAvailable")
    var reportId: String by bindToPreferenceField("", "reportId")
    var isNextVisitDateMandatory: Boolean by bindToPreferenceField(
        false,
        "isNextVisitDateMandatory"
    )
    var isRecordAudioEnable: Boolean by bindToPreferenceField(false, "isRecordAudioEnable")
    var isShowCurrentLocNotifiaction: Boolean by bindToPreferenceField(
        false,
        "isShowCurrentLocNotifiaction"
    )
    var isUpdateWorkTypeEnable: Boolean by bindToPreferenceField(false, "isUpdateWorkTypeEnable")
    var isAchievementEnable: Boolean by bindToPreferenceField(false, "isAchievementEnable")
    var isTarVsAchvEnable: Boolean by bindToPreferenceField(false, "isTarVsAchvEnable")
    var isLeaveEnable: Boolean by bindToPreferenceField(false, "isLeaveEnable")
    var isOrderMailVisible: Boolean by bindToPreferenceField(false, "isOrderMailVisible")
    var isShopEditEnable: Boolean by bindToPreferenceField(false, "isShopEditEnable")
    var isClearData: Boolean by bindToPreferenceField(false, "isClearData")
    var isTaskEnable: Boolean by bindToPreferenceField(false, "isTaskEnable")
    var isAppInfoEnable: Boolean by bindToPreferenceField(false, "isAppInfoEnable")
    var appInfoMins: String by bindToPreferenceField("1", "appInfoMins")
    var autoRevisitDistance: String by bindToPreferenceField("120", "autoRevisitDistance")
    var autoRevisitTime: String by bindToPreferenceField("5", "autoRevisitTime")
    var willAutoRevisitEnable: Boolean by bindToPreferenceField(false, "willAutoRevisitEnable")
    var dynamicFormName: String by bindToPreferenceField("", "dynamicFormName")
    var willDynamicShow: Boolean by bindToPreferenceField(false, "willDynamicShow")
    var isOfflineShopSaved: Boolean by bindToPreferenceField(false, "isOfflineShopSaved")
    var willActivityShow: Boolean by bindToPreferenceField(false, "willActivityShow")
    var willMoreVisitUpdateCompulsory: Boolean by bindToPreferenceField(
        false,
        "willMoreVisitUpdateCompulsory"
    )
    var willMoreVisitUpdateOptional: Boolean by bindToPreferenceField(
        true,
        "willMoreVisitUpdateOptional"
    )
    var isRememberMe: Boolean by bindToPreferenceField(false, "isRememberMe")
    var PhnNo: String by bindToPreferenceField("", "PhnNo")
    var pwd: String by bindToPreferenceField("", "pwd")
    var docAttachmentNo: String by bindToPreferenceField("0", "docAttachmentNo")
    var isDocumentRepoShow: Boolean by bindToPreferenceField(false, "isDocumentRepoShow")
    var isChatBotShow: Boolean by bindToPreferenceField(false, "isChatBotShow")
    var isAttendanceBotShow: Boolean by bindToPreferenceField(false, "isAttendanceBotShow")
    var isVisitBotShow: Boolean by bindToPreferenceField(false, "isVisitBotShow")
    var chatBotMsg: String by bindToPreferenceField("", "chatBotMsg")
    var contactMail: String by bindToPreferenceField("", "contactMail")
    var isShowOrderRemarks: Boolean by bindToPreferenceField(false, "isShowOrderRemarks")
    var isShowOrderSignature: Boolean by bindToPreferenceField(false, "isShowOrderSignature")
    var isVoiceEnabledForAttendanceSubmit: Boolean by bindToPreferenceField(
        false,
        "isVoiceEnabledForAttendanceSubmit"
    )
    var isVoiceEnabledForOrderSaved: Boolean by bindToPreferenceField(
        false,
        "isVoiceEnabledForOrderSaved"
    )
    var isVoiceEnabledForInvoiceSaved: Boolean by bindToPreferenceField(
        false,
        "isVoiceEnabledForInvoiceSaved"
    )
    var isVoiceEnabledForCollectionSaved: Boolean by bindToPreferenceField(
        false,
        "isVoiceEnabledForCollectionSaved"
    )
    var isVoiceEnabledForHelpAndTipsInBot: Boolean by bindToPreferenceField(
        false,
        "isVoiceEnabledForHelpAndTipsInBot"
    )
    var isShowSmsForParty: Boolean by bindToPreferenceField(false, "isShowSmsForParty")
    var isShowTimeline: Boolean by bindToPreferenceField(false, "isShowTimeline")
    var willScanVisitingCard: Boolean by bindToPreferenceField(false, "willScanVisitingCard")
    var isCreateQrCode: Boolean by bindToPreferenceField(false, "isCreateQrCode")
    var isScanQrForRevisit: Boolean by bindToPreferenceField(false, "isScanQrForRevisit")
    var willShowHomeLocReason: Boolean by bindToPreferenceField(false, "willShowHomeLocReason")
    var isShowLogoutReason: Boolean by bindToPreferenceField(false, "isShowLogoutReason")
    var isShowHomeLocReason: Boolean by bindToPreferenceField(false, "isShowHomeLocReason")
    var willShowShopVisitReason: Boolean by bindToPreferenceField(false, "willShowShopVisitReason")
    var minVisitDurationSpentTime: String by bindToPreferenceField("", "minVisitDurationSpentTime")
    var isShowShopVisitReason: Boolean by bindToPreferenceField(false, "isShowShopVisitReason")
    var durationCompletedShopId: String by bindToPreferenceField("", "durationCompletedShopId")
    var durationCompletedStartTimeStamp: String by bindToPreferenceField(
        "",
        "durationCompletedStartTimeStamp"
    )
    var willShowPartyStatus: Boolean by bindToPreferenceField(false, "willShowPartyStatus")
    var willShowEntityTypeforShop: Boolean by bindToPreferenceField(
        false,
        "willShowEntityTypeforShop"
    )
    var isShowRetailerEntity: Boolean by bindToPreferenceField(false, "isShowRetailerEntity")
    var isShowDealerForDD: Boolean by bindToPreferenceField(false, "isShowDealerForDD")
    var isShowBeatGroup: Boolean by bindToPreferenceField(false, "isShowBeatGroup")
    var isShowShopBeatWise: Boolean by bindToPreferenceField(false, "isShowShopBeatWise")
    var isShowBankDetailsForShop: Boolean by bindToPreferenceField(
        false,
        "isShowBankDetailsForShop"
    )
    var isShowOTPVerificationPopup: Boolean by bindToPreferenceField(
        false,
        "isShowOTPVerificationPopup"
    )
    var locationTrackInterval: String by bindToPreferenceField("60", "locationTrackInterval")
    var isShowMicroLearning: Boolean by bindToPreferenceField(false, "isShowMicroLearning")
    var isLocationPermissionGranted: Boolean by bindToPreferenceField(
        false,
        "isLocationPermissionGranted"
    )
    var homeLocReasonCheckMins: String by bindToPreferenceField("1", "homeLocReasonCheckMins")
    var homeLocStartTimeStamp: String by bindToPreferenceField("", "homeLocStartTimeStamp")
    var homeLocEndTimeStamp: String by bindToPreferenceField("", "homeLocEndTimeStamp")
    var currentLocationNotificationMins: String by bindToPreferenceField(
        "1",
        "currentLocationNotificationMins"
    )
    var isMultipleVisitEnable: Boolean by bindToPreferenceField(false, "isMultipleVisitEnable")
    var isShowVisitRemarks: Boolean by bindToPreferenceField(false, "isShowVisitRemarks")
    var isShowNearbyCustomer: Boolean by bindToPreferenceField(false, "isShowNearbyCustomer")
    var isServiceFeatureEnable: Boolean by bindToPreferenceField(false, "isServiceFeatureEnable")
    var isPatientDetailsShowInOrder: Boolean by bindToPreferenceField(
        false,
        "isPatientDetailsShowInOrder"
    )
    var isPatientDetailsShowInCollection: Boolean by bindToPreferenceField(
        false,
        "isPatientDetailsShowInCollection"
    )
    var isShopImageMandatory: Boolean by bindToPreferenceField(false, "isShopImageMandatory")

    //From Hahnemann
    var isRevisitCaptureImage: Boolean by bindToPreferenceField(false, "isRevisitCaptureImage")
    var isShowAllProduct: Boolean by bindToPreferenceField(false, "isShowAllProduct")
    var isPrimaryTargetMandatory: Boolean by bindToPreferenceField(
        false,
        "isPrimaryTargetMandatory"
    )
    var isStockAvailableForAll: Boolean by bindToPreferenceField(false, "isStockAvailableForAll")
    var isStockAvailableForPopup: Boolean by bindToPreferenceField(true, "isStockAvailableForPopup")
    var isOrderAvailableForPopup: Boolean by bindToPreferenceField(true, "isOrderAvailableForPopup")
    var isCollectionAvailableForPopup: Boolean by bindToPreferenceField(
        true,
        "isCollectionAvailableForPopup"
    )
    var isDDFieldEnabled: Boolean by bindToPreferenceField(false, "isDDFieldEnabled")
    var isActivatePJPFeature: Boolean by bindToPreferenceField(false, "isActivatePJPFeature")
    var willShowTeamDetails: Boolean by bindToPreferenceField(false, "willShowTeamDetails")
    var isAllowPJPUpdateForTeam: Boolean by bindToPreferenceField(false, "isAllowPJPUpdateForTeam")
    var willReimbursementShow: Boolean by bindToPreferenceField(false, "willReimbursementShow")
    var isVisitPlanMandatory: Boolean by bindToPreferenceField(false, "isVisitPlanMandatory")
    var isVisitPlanShow: Boolean by bindToPreferenceField(false, "isVisitPlanShow")
    var isAttendanceDistanceShow: Boolean by bindToPreferenceField(
        false,
        "isAttendanceDistanceShow"
    )
    var visitDistance: String by bindToPreferenceField("", "visitDistance")
    var willTimelineWithFixedLocationShow: Boolean by bindToPreferenceField(
        false,
        "willTimelineWithFixedLocationShow"
    )
    var distributorName: String by bindToPreferenceField("", "distributorName")
    var marketWorked: String by bindToPreferenceField("", "marketWorked")


    var isCompetitorImgEnable: Boolean by bindToPreferenceField(false, "IsCompetitorenable")
    var isOrderStatusRequired: Boolean by bindToPreferenceField(false, "IsOrderStatusRequired")
    var isCurrentStockEnable: Boolean by bindToPreferenceField(false, "IsCurrentStockEnable")
    var IsCurrentStockApplicableforAll: Boolean by bindToPreferenceField(
        false,
        "IsCurrentStockApplicableforAll"
    )
    var IscompetitorStockRequired: Boolean by bindToPreferenceField(
        false,
        "IscompetitorStockRequired"
    )
    var IsCompetitorStockforParty: Boolean by bindToPreferenceField(
        false,
        "IsCompetitorStockforParty"
    )
    var IsFaceDetectionOn: Boolean by bindToPreferenceField(false, "IsFaceDetectionOn")
    var IsFaceDetection: Boolean by bindToPreferenceField(false, "IsFaceDetection")
    var IsFaceDetectionWithCaptcha: Boolean by bindToPreferenceField(
        false,
        "IsFaceDetectionWithCaptcha"
    )
    //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7
    // var IsScreenRecorderEnable: Boolean by bindToPreferenceField(false, "IsScreenRecorderEnable")
    //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

    //    var IsFromPortal: Boolean by bindToPreferenceField(false, "IsFromPortal")
    var IsFromPortal: Boolean by bindToPreferenceField(
        false,
        " IsDocRepoFromPortal"
    )//30-08-21 changes this
    var IsDocRepShareDownloadAllowed: Boolean by bindToPreferenceField(
        false,
        "IsDocRepShareDownloadAllowed"
    )


    var IsShowMenuAddAttendance: Boolean by bindToPreferenceField(false, "IsShowMenuAddAttendance")
    var IsShowMenuAttendance: Boolean by bindToPreferenceField(false, "IsShowMenuAttendance")
    var IsShowMenuMIS_Report: Boolean by bindToPreferenceField(false, "IsShowMenuMIS_Report")
    var IsShowMenuAnyDesk: Boolean by bindToPreferenceField(false, "IsShowMenuAnyDesk")
    var IsShowMenuPermission_Info: Boolean by bindToPreferenceField(
        false,
        "IsShowMenuPermission_Info"
    )
    var IsShowMenuScan_QR_Code: Boolean by bindToPreferenceField(false, "IsShowMenuScan_QR_Code")
    var IsShowMenuChat: Boolean by bindToPreferenceField(false, "IsShowMenuChat")
    var IsShowMenuWeather_Details: Boolean by bindToPreferenceField(
        false,
        "IsShowMenuWeather_Details"
    )
    var IsShowMenuHome_Location: Boolean by bindToPreferenceField(false, "IsShowMenuHome_Location")
    var IsShowMenuShare_Location: Boolean by bindToPreferenceField(
        false,
        "IsShowMenuShare_Location"
    )
    var IsShowMenuMap_View: Boolean by bindToPreferenceField(false, "IsShowMenuMap_View")
    var IsShowMenuReimbursement: Boolean by bindToPreferenceField(false, "IsShowMenuReimbursement")
    var IsShowMenuOutstanding_Details_PP_DD: Boolean by bindToPreferenceField(
        false,
        "IsShowMenuOutstanding_Details_PP_DD"
    )
    var IsShowMenuStock_Details_PP_DD: Boolean by bindToPreferenceField(
        false,
        "IsShowMenuStock_Details_PP_DD"
    )


    var IsLeavePressed: Boolean by bindToPreferenceField(false, "IsLeavePressed")// local
    var IsLeaveGPSTrack: Boolean by bindToPreferenceField(false, "IsLeaveGPSTrack")
    var IsShowActivitiesInTeam: Boolean by bindToPreferenceField(false, "IsShowActivitiesInTeam")
    var IsMyJobFromTeam: Boolean by bindToPreferenceField(false, "IsMyJobFromTeam")// local

    var IsShowPartyOnAppDashboard: Boolean by bindToPreferenceField(
        false,
        "IsShowPartyOnAppDashboard"
    )
    var IsShowAttendanceOnAppDashboard: Boolean by bindToPreferenceField(
        false,
        "IsShowAttendanceOnAppDashboard"
    )
    var IsShowTotalVisitsOnAppDashboard: Boolean by bindToPreferenceField(
        false,
        "IsShowTotalVisitsOnAppDashboard"
    )
    var IsShowVisitDurationOnAppDashboard: Boolean by bindToPreferenceField(
        false,
        "IsShowVisitDurationOnAppDashboard"
    )


    var IsShowDayStart: Boolean by bindToPreferenceField(false, "IsShowDayStart")
    var IsshowDayStartSelfie: Boolean by bindToPreferenceField(false, "IsshowDayStartSelfie")
    var IsShowDayEnd: Boolean by bindToPreferenceField(false, "IsShowDayEnd")
    var IsshowDayEndSelfie: Boolean by bindToPreferenceField(false, "IsshowDayEndSelfie")

    var IsShowLeaveInAttendance: Boolean by bindToPreferenceField(false, "IsShowLeaveInAttendance")


    var DayStartMarked: Boolean by bindToPreferenceField(false, "DayStartMarked")
    var DayEndMarked: Boolean by bindToPreferenceField(false, "DayEndMarked")
    var DayStartShopType: String by bindToPreferenceField("", "DayStartShopType")
    var DayStartShopID: String by bindToPreferenceField("", "DayStartShopID")


    //19-08-21
    var IsShowMarkDistVisitOnDshbrd: Boolean by bindToPreferenceField(
        false,
        "IsShowMarkDistVisitOnDshbrd"
    )//30-08-21 changes this
    var IsDDvistedOnceByDay: Boolean by bindToPreferenceField(false, "IsMyDDVisit")
    var visit_distributor_id: String by bindToPreferenceField("", "visit_distributor_id")
    var visit_distributor_name: String by bindToPreferenceField("", "visit_distributor_name")
    var visit_distributor_date_time: String by bindToPreferenceField(
        "",
        "visit_distributor_date_time"
    )

    var IsActivateNewOrderScreenwithSize: Boolean by bindToPreferenceField(
        false,
        "IsActivateNewOrderScreenwithSize"
    )

    var IsPhotoDeleteShow: Boolean by bindToPreferenceField(false, "IsPhotoDeleteShow")


    /*28-09-2021 For Gupta Power*/
    var RevisitRemarksMandatory: Boolean by bindToPreferenceField(false, "RevisitRemarksMandatory")

    var new_ord_gender_male: String by bindToPreferenceField("", "new_ord_gender_male")
    var new_ord_gender_female: String by bindToPreferenceField("", "new_ord_gender_female")


    var GPSAlertGlobal: Boolean by bindToPreferenceField(false, "GPSAlertGlobal")
    var GPSAlert: Boolean by bindToPreferenceField(false, "GPSAlert")
    var GPSAlertwithSound: Boolean by bindToPreferenceField(false, "GPSAlertwithSound")

    var IsTeamAttendance: Boolean by bindToPreferenceField(
        false,
        "IsTeamAttendance"
    )/*29-10-2021 Team Attendance*/

    var IsDuplicateShopContactnoAllowedOnline: Boolean by bindToPreferenceField(
        false,
        "IsDuplicateShopContactnoAllowedOnline"
    )/*02-11-2021*/


    /*24-11-2021 ITC face And Distributoraccu*/
    var FaceDetectionAccuracyUpper: String by bindToPreferenceField(
        "0.93",
        "FaceDetectionAccuracyUpper"
    )
    var FaceDetectionAccuracyLower: String by bindToPreferenceField(
        "0.73",
        "FaceDetectionAccuracyLower"
    )
    var DistributorGPSAccuracy: String by bindToPreferenceField("500", "DistributorGPSAccuracy")

    /*26-11-2021*/
    var BatterySettingGlobal: Boolean by bindToPreferenceField(false, "BatterySettingGlobal")
    var PowerSaverSettingGlobal: Boolean by bindToPreferenceField(false, "PowerSaverSettingGlobal")

    var BatterySetting: Boolean by bindToPreferenceField(false, "BatterySetting")
    var PowerSaverSetting: Boolean by bindToPreferenceField(false, "PowerSaverSetting")

    /*1-12-2021*/
    var IsnewleadtypeforRuby: Boolean by bindToPreferenceField(false, "IsnewleadtypeforRuby")

    /*16-12-2021 return features*/
    var IsReturnEnableforParty: Boolean by bindToPreferenceField(false, "IsReturnEnableforParty")

    var IsReturnActivatedforPP: Boolean by bindToPreferenceField(false, "IsReturnActivatedforPP")
    var IsReturnActivatedforDD: Boolean by bindToPreferenceField(false, "IsReturnActivatedforDD")
    var IsReturnActivatedforSHOP: Boolean by bindToPreferenceField(
        false,
        "IsReturnActivatedforSHOP"
    )

    /*06-01-2022*/
    var MRPInOrderGlobal: Boolean by bindToPreferenceField(false, "MRPInOrderGlobal")
    var FaceRegistrationOpenFrontCamera: Boolean by bindToPreferenceField(
        false,
        "FaceRegistrationOpenFrontCamera"
    )

    var MRPInOrder: Boolean by bindToPreferenceField(false, "MRPInOrder")
    var FaceRegistrationFrontCamera: Boolean by bindToPreferenceField(
        false,
        "FaceRegistrationFrontCamera"
    )

    /*18-01-2022*/
    var IslandlineforCustomer: Boolean by bindToPreferenceField(false, "IslandlineforCustomer")
    var IsprojectforCustomer: Boolean by bindToPreferenceField(false, "IsprojectforCustomer")


    var Leaveapprovalfromsupervisorinteam: Boolean by bindToPreferenceField(
        false,
        "Leaveapprovalfromsupervisorinteam"
    )
    var Leaveapprovalfromsupervisor: Boolean by bindToPreferenceField(
        false,
        "Leaveapprovalfromsupervisor"
    )

    var IsRestrictNearbyGeofence: Boolean by bindToPreferenceField(
        false,
        "IsRestrictNearbyGeofence"
    )

    /*07-02-2022*/
    var IsNewQuotationfeatureOn: Boolean by bindToPreferenceField(false, "IsNewQuotationfeatureOn")
    var SqMtrRateCalculationforQuotEuro: String by bindToPreferenceField(
        "",
        "SqMtrRateCalculationforQuotEuro"
    )

    /*10-02-2022*/
    var IsAlternateNoForCustomer: Boolean by bindToPreferenceField(
        false,
        "IsAlternateNoForCustomer"
    )
    var IsWhatsappNoForCustomer: Boolean by bindToPreferenceField(false, "IsWhatsappNoForCustomer")


    var IsOnLeaveForTodayApproved: Boolean by bindToPreferenceField(
        false,
        "IsOnLeaveForTodayApproved"
    )
    var OnLeaveForTodayStatus: String by bindToPreferenceField("", "OnLeaveForTodayStatus")


    var IsNewQuotationNumberManual: Boolean by bindToPreferenceField(
        false,
        "IsNewQuotationNumberManual"
    )
    var ShowQuantityNewQuotation: Boolean by bindToPreferenceField(
        false,
        "ShowQuantityNewQuotation"
    )
    var ShowAmountNewQuotation: Boolean by bindToPreferenceField(false, "ShowAmountNewQuotation")

    var NewQuotationRateCaption: String by bindToPreferenceField("", "NewQuotationRateCaption")
    var NewQuotationShowTermsAndCondition: Boolean by bindToPreferenceField(
        false,
        "NewQuotationShowTermsAndCondition"
    )

    var ShowUserwiseLeadMenu: Boolean by bindToPreferenceField(false, "ShowUserwiseLeadMenu")
    var GeofencingRelaxationinMeter: Int by bindToPreferenceField(
        100,
        "GeofencingRelaxationinMeter"
    )


    var user_login_ID: String by bindToPreferenceField("", "user_login_ID")

    var IsFeedbackHistoryActivated: Boolean by bindToPreferenceField(
        false,
        "IsFeedbackHistoryActivated"
    )
    var IsAutoLeadActivityDateTime: Boolean by bindToPreferenceField(
        false,
        "IsAutoLeadActivityDateTime"
    )
    var LogoutWithLogFile: Boolean by bindToPreferenceField(false, "LogoutWithLogFile")

    var IsCollectionOrderWise: Boolean by bindToPreferenceField(false, "IsCollectionOrderWise")
    var ShowCollectionOnlywithInvoiceDetails: Boolean by bindToPreferenceField(
        false,
        "ShowCollectionOnlywithInvoiceDetails"
    )


    var ShowCollectionAlert: Boolean by bindToPreferenceField(false, "ShowCollectionAlert")
    var ShowZeroCollectioninAlert: Boolean by bindToPreferenceField(
        false,
        "ShowZeroCollectioninAlert"
    )

    var IsPendingCollectionRequiredUnderTeam: Boolean by bindToPreferenceField(
        false,
        "IsPendingCollectionRequiredUnderTeam"
    )

    var IsPendingColl: Boolean by bindToPreferenceField(false, "IsPendingColl")
    var IsZeroOrder: Boolean by bindToPreferenceField(false, "IsZeroOrder")

    var IsCollectionEntryConsiderOrderOrInvoice: Boolean by bindToPreferenceField(
        false,
        "IsCollectionEntryConsiderOrderOrInvoice"
    )  // if 0 then order else invoice for collection

    var IsShowRepeatOrderinNotification: Boolean by bindToPreferenceField(
        false,
        "IsShowRepeatOrderinNotification"
    )
    var IsShowRepeatOrdersNotificationinTeam: Boolean by bindToPreferenceField(
        false,
        "IsShowRepeatOrdersNotificationinTeam"
    )

    var ZeroOrderInterval: String by bindToPreferenceField("0", "ZeroOrderInterval")

    var AutoDDSelect: Boolean by bindToPreferenceField(true, "AutoDDSelect")
    var ShowPurposeInShopVisit: Boolean by bindToPreferenceField(false, "ShowPurposeInShopVisit")
    var contactNameText: String by bindToPreferenceField("Contact", "contactNameText")
    var contactNumberText: String by bindToPreferenceField("Contact", "contactNumberText")
    var emailText: String by bindToPreferenceField("Email", "emailText")
    var dobText: String by bindToPreferenceField("Date of Birth", "dobText")
    var dateOfAnniversaryText: String by bindToPreferenceField(
        "Date of Anniversary",
        "dateOfAnniversaryText"
    )

    var IsTodayDOBDOA: Boolean by bindToPreferenceField(false, "IsTodayDOBDOA")

    var GPSAlertwithVibration: Boolean by bindToPreferenceField(false, "GPSAlertwithVibration")
    var WillRoomDBShareinLogin: Boolean by bindToPreferenceField(false, "WillRoomDBShareinLogin")

    var ShopScreenAftVisitRevisit: Boolean by bindToPreferenceField(
        true,
        "ShopScreenAftVisitRevisit"
    )
    var ShopScreenAftVisitRevisitGlobal: Boolean by bindToPreferenceField(
        true,
        "ShopScreenAftVisitRevisitGlobal"
    )

    var NotiCountFlag: Boolean by bindToPreferenceField(false, "NotiCountFlag")

    var IsShowNearByTeam: Boolean by bindToPreferenceField(false, "IsShowNearByTeam")

    var IsFeedbackAvailableInShop: Boolean by bindToPreferenceField(
        true,
        "IsFeedbackAvailableInShop"
    )

    var IsAllowBreakageTracking: Boolean by bindToPreferenceField(false, "IsAllowBreakageTracking")
    var IsAllowBreakageTrackingunderTeam: Boolean by bindToPreferenceField(
        false,
        "IsAllowBreakageTrackingunderTeam"
    )

    var IsRateEnabledforNewOrderScreenwithSize: Boolean by bindToPreferenceField(
        false,
        "IsRateEnabledforNewOrderScreenwithSize"
    )

    var IgnoreNumberCheckwhileShopCreation: Boolean by bindToPreferenceField(
        false,
        "IgnoreNumberCheckwhileShopCreation"
    )
    var Showdistributorwisepartyorderreport: Boolean by bindToPreferenceField(
        false,
        "Showdistributorwisepartyorderreport"
    )

    var IsMenuSurveyEnabled: Boolean by bindToPreferenceField(false, "IsMenuSurveyEnabled")

    var IsSurveyRequiredforNewParty: Boolean by bindToPreferenceField(
        false,
        "IsSurveyRequiredforNewParty"
    )
    var IsSurveyRequiredforDealer: Boolean by bindToPreferenceField(
        false,
        "IsSurveyRequiredforDealer"
    )

    var IsShowHomeLocationMap: Boolean by bindToPreferenceField(true, "IsShowHomeLocationMap")
    var IsShowHomeLocationMapGlobal: Boolean by bindToPreferenceField(true, "IsShowHomeLocationMap")

    var IsBeatRouteAvailableinAttendance: Boolean by bindToPreferenceField(
        false,
        "IsBeatRouteAvailableinAttendance"
    )
    var SelectedBeatIDFromAttend: String by bindToPreferenceField("0", "SelectedBeatIDFromAttend")
    var IsAllBeatAvailableforParty: Boolean by bindToPreferenceField(
        false,
        "IsAllBeatAvailableforParty"
    )
    var ShowAttednaceClearmenu: Boolean by bindToPreferenceField(false, "ShowAttednaceClearmenu")
    var IsBeatRouteReportAvailableinTeam: Boolean by bindToPreferenceField(
        false,
        "IsBeatRouteReportAvailableinTeam"
    )
    var beatText: String by bindToPreferenceField("Beat", "beatText")
    var TodaysTaskText: String by bindToPreferenceField("Today's Task", "TodaysTaskText")

    var isLocationHintPermissionGranted: Boolean by bindToPreferenceField(
        false,
        "isLocationHintPermissionGranted"
    )
    var AutostartPermissionStatus: Boolean by bindToPreferenceField(
        false,
        "AutostartPermissionStatus"
    )
    var IsDistributorSelectionRequiredinAttendance: Boolean by bindToPreferenceField(
        false,
        "IsDistributorSelectionRequiredinAttendance"
    )
    var SelectedDDIDFromAttend: String by bindToPreferenceField("0", "SelectedDDIDFromAttend")

    var PowerSaverStatus: String by bindToPreferenceField("Off", "PowerSaverStatus")
    var GPSNetworkIntervalMins: String by bindToPreferenceField("0", "GPSNetworkIntervalMins")
    var prevGpsNetSyncTimeStamp: Long by bindToPreferenceField(0, "prevGpsNetSyncTimeStamp")
    var prevGpsNetSyncTimeStampService: Long by bindToPreferenceField(
        0,
        "prevGpsNetSyncTimeStampService"
    )

    var IsAllowNearbyshopWithBeat: Boolean by bindToPreferenceField(
        false,
        "IsAllowNearbyshopWithBeat"
    )
    var IsGSTINPANEnableInShop: Boolean by bindToPreferenceField(false, "IsGSTINPANEnableInShop")

    var IsMultipleImagesRequired: Boolean by bindToPreferenceField(
        false,
        "IsMultipleImagesRequired"
    )

    var IsALLDDRequiredforAttendance: Boolean by bindToPreferenceField(
        false,
        "IsALLDDRequiredforAttendance"
    )

    var IsFeedbackMandatoryforNewShop: Boolean by bindToPreferenceField(
        false,
        "IsFeedbackMandatoryforNewShop"
    )

    var IsShowNewOrderCart: Boolean by bindToPreferenceField(false, "IsShowNewOrderCart")
    var IsLoginSelfieRequired: Boolean by bindToPreferenceField(false, "IsLoginSelfieRequired")

    var IsmanualInOutTimeRequired: Boolean by bindToPreferenceField(false, "IsmanualInOutTimeRequired")
    var surveytext: String by bindToPreferenceField("survey", "surveytext")

    var IsDiscountInOrder: Boolean by bindToPreferenceField(false, "IsDiscountInOrder")
    var IsViewMRPInOrder: Boolean by bindToPreferenceField(false, "IsViewMRPInOrder")


    var IsJointVisitEnable:Boolean by bindToPreferenceField(false, "IsJointVisitEnable")
    var IsShowAllEmployeeforJointVisit:Boolean by bindToPreferenceField(false, "IsShowAllEmployeeforJointVisit")
    var JointVisitSelectedUserName:String by bindToPreferenceField("", "JointVisitSelectedUserName")

    var IsShowStateInTeam: Boolean by bindToPreferenceField(false, "IsShowStateInTeam")
    var IsShowBranchInTeam: Boolean by bindToPreferenceField(false, "IsShowBranchInTeam")
    var IsShowDesignationInTeam: Boolean by bindToPreferenceField(false, "IsShowDesignationInTeam")

    var IsAllowClickForVisit: Boolean by bindToPreferenceField(false, "IsAllowClickForVisit")
    var IsShowInPortalManualPhotoRegn: Boolean by bindToPreferenceField(false, "IsShowInPortalManualPhotoRegn")
    var IsShowTypeInRegistration: Boolean by bindToPreferenceField(false, "IsShowTypeInRegistration")
    var UpdateUserName: Boolean by bindToPreferenceField(false, "UpdateUserName")
    var IsAllowClickForPhotoRegister: Boolean by bindToPreferenceField(false, "IsAllowClickForPhotoRegister")
    var ShowAutoRevisitInAppMenu: Boolean by bindToPreferenceField(false, "ShowAutoRevisitInAppMenu")// 1.0  AppV 4.0.6
    var OfflineShopAccuracy: String by bindToPreferenceField("700", "OfflineShopAccuracy")// 1.0  AppV 4.0.6

    var IsFaceRecognitionOnEyeblink: Boolean by bindToPreferenceField(false, "IsFaceRecognitionOnEyeblink")
    var PartyUpdateAddrMandatory: Boolean by bindToPreferenceField(false, "PartyUpdateAddrMandatory") // 2.0 AppV 4.0.6

    var IsAttendVisitShowInDashboardGlobal: Boolean by bindToPreferenceField(false, "IsAttendVisitShowInDashboardGlobal") // 2.0 DashboardFragment  AppV 4.0.6
    var IsAttendVisitShowInDashboard: Boolean by bindToPreferenceField(false, "IsAttendVisitShowInDashboard") // 2.0 DashboardFragment  AppV 4.0.6
    var CommonAINotification: Boolean by bindToPreferenceField(false, "CommonAINotification")// 1.0  AppV 4.0.6 LocationFuzedService
    var IsIMEICheck: Boolean by bindToPreferenceField(false, "IsIMEICheck")//1.0 LoginActivity  AppV 4.0.6

    var Show_App_Logout_Notification_Global: Boolean by bindToPreferenceField(false, "Show_App_Logout_Notification_Global")//2.0 LocationFuzedService  AppV 4.0.6
    var Show_App_Logout_Notification: Boolean by bindToPreferenceField(false, "Show_App_Logout_Notification")//2.0 LocationFuzedService  AppV 4.0.6
    var AllowProfileUpdate: Boolean by bindToPreferenceField(false, "AllowProfileUpdate")// 1.0 MyProfileFragment  AppV 4.0.6
    var ShowAutoRevisitInDashboard: Boolean by bindToPreferenceField(false, "ShowAutoRevisitInDashboard")
    var ShowTotalVisitAppMenu: Boolean by bindToPreferenceField(false, "ShowTotalVisitAppMenu")// 3.0  AppV 4.0.6  DashboardActivity

    var IsMultipleContactEnableforShop: Boolean by bindToPreferenceField(false, "IsMultipleContactEnableforShop")
    var IsContactPersonSelectionRequiredinRevisit: Boolean by bindToPreferenceField(false, "IsContactPersonSelectionRequiredinRevisit")
    var IsContactPersonRequiredinQuotation: Boolean by bindToPreferenceField(false, "IsContactPersonRequiredinQuotation")// 4.0  AppV 4.0.6  DashboardFragment Addquot work
    var IsAllowShopStatusUpdate: Boolean by bindToPreferenceField(false, "IsAllowShopStatusUpdate")//2.0 NearByShopsListAdapter  AppV 4.0.6 IsAllowShopStatusUpdate
    var IsShowBeatInMenu:Boolean by bindToPreferenceField(false, "IsShowBeatInMenu")//userwise 1.0 Pref  AppV 4.0.6 Update beat feature
    var IsBeatAvailable :Boolean by bindToPreferenceField(false, "IsBeatAvailable")// global 1.0 Pref  AppV 4.0.6 Update beat feature

    var isExpenseFeatureAvailable :Boolean by bindToPreferenceField(false, "isExpenseFeatureAvailable")//mantis 25607

    var IsDiscountEditableInOrder: Boolean by bindToPreferenceField(false, "IsDiscountEditableInOrder")//mantis 25623

    var IsRouteStartFromAttendance: Boolean by bindToPreferenceField(false, "IsRouteStartFromAttendance")//mantis 25637

    var IsShowQuotationFooterforEurobond: Boolean by bindToPreferenceField(false, "IsShowQuotationFooterforEurobond")// 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650
    var IsShowOtherInfoinShopMaster: Boolean by bindToPreferenceField(false, "IsShowOtherInfoinShopMaster") // 3.0 Pref  AppV 4.0.7 Suman    10/03/2023 Pdf generation settings wise  mantis 25650

    var IsVoiceEnable: Boolean by bindToPreferenceField(true, "IsVoiceEnable")

    var IsAllowZeroRateOrder: Boolean by bindToPreferenceField(false, "IsAllowZeroRateOrder")

    // 4.0 Pref  AppV 4.0.7 Suman    23/03/2023 ShowApproxDistanceInNearbyShopList Show approx distance in nearby + shopmaster  mantis 0025742
    var ShowApproxDistanceInNearbyShopList: Boolean by bindToPreferenceField(false, "ShowApproxDistanceInNearbyShopList")

    // 5.0 Pref  AppV 4.0.8 Saheli    06/04/2023 mantis 0025780 IsAssignedDDAvailableForAllUser Useds LoginActivity If this feature 'On' then Assigned DD [Assigned DD Table] shall be available in 'Shop Master'
    var IsAssignedDDAvailableForAllUserGlobal: Boolean by bindToPreferenceField(false, "IsAssignedDDAvailableForAllUser ")
    var IsAssignedDDAvailableForAllUser : Boolean by bindToPreferenceField(false, "IsAssignedDDAvailableForAllUser ")

    //Begin Rev 17 DashboardActivity AppV 4.0.8 Suman    24/04/2023 distanct+station calculation 25806
    var profile_latitude: String by bindToPreferenceField("", "profile_latitude")
    var profile_longitude: String by bindToPreferenceField("", "profile_longitude")
    //End of Rev 17 DashboardActivity AppV 4.0.8 Suman    24/04/2023 distanct+station calculation 25806

    // 6.0 Pref  AppV 4.0.8 Saheli    20/04/2023 mantis 25860
    var IsShowEmployeePerformanceGlobal: Boolean by bindToPreferenceField(false, "IsShowEmployeePerformance")
    var IsShowEmployeePerformance : Boolean by bindToPreferenceField(false, "IsShowEmployeePerformance")

    //Begin Rev 6.0 Pref AppV 4.0.8 Suman    26/04/2023 mail repetation fix 25923
    var prevQutoNoForMail : String by bindToPreferenceField("", "prevQutoNoForMail")
    //End Rev 6.0 Pref AppV 4.0.8 Suman    26/04/2023 mail repetation fix 25923

    //Begin Rev 7.0 Pref AppV 4.0.8 Suman    02/05/2023 mantis id 25979
    var IsAttendanceCheckedforExpense : Boolean by bindToPreferenceField(false, "IsAttendanceCheckedforExpense")
    var IsShowLocalinExpense : Boolean by bindToPreferenceField(false, "IsShowLocalinExpense")
    var IsShowOutStationinExpense : Boolean by bindToPreferenceField(false, "IsShowOutStationinExpense")
    var IsSingleDayTAApplyRestriction : Boolean by bindToPreferenceField(false, "IsSingleDayTAApplyRestriction")
    var IsTAAttachment1Mandatory : Boolean by bindToPreferenceField(false, "IsTAAttachment1Mandatory")
    var IsTAAttachment2Mandatory : Boolean by bindToPreferenceField(false, "IsTAAttachment2Mandatory")

    var NameforConveyanceAttachment1 : String by bindToPreferenceField("", "NameforConveyanceAttachment1")
    var NameforConveyanceAttachment2 : String by bindToPreferenceField("", "NameforConveyanceAttachment2")

    //End of Rev 7.0 Pref AppV 4.0.8 Suman    02/05/2023 mantis id 25979

    //Begin Rev 8.0 Pref AppV 4.0.8 Saheli    05/05/2023  mantis 0026023
    var IsTaskManagementAvailable: Boolean by bindToPreferenceField(false, "IsTaskManagementAvailable")
    //end Rev 8.0 Pref AppV 4.0.8 Saheli    05/05/2023  mantis 0026023

    var IsShowPrivacyPolicyInMenu: Boolean by bindToPreferenceField(false, "IsShowPrivacyPolicyInMenu")

    //Begin Rev 9.0 Pref AppV 4.0.8 Saheli    12/05/2023  mantis 0026101
    var IsAttachmentAvailableForCurrentStock : Boolean by bindToPreferenceField(false, "IsAttachmentAvailableForCurrentStock")
    //end Rev 9.0 Pref AppV 4.0.8 Saheli    12/05/2023  mantis 0026101

    //Begin Rev 10.0 Pref AppV 4.0.8 Suman    16/05/2023  mantis 26119
    var IsShowReimbursementTypeInAttendance : Boolean by bindToPreferenceField(false, "IsShowReimbursementTypeInAttendance")
    var selectedVisitStationID : String by bindToPreferenceField("", "selectedVisitStationID")
    var selectedVisitStationName : String by bindToPreferenceField("", "selectedVisitStationName")
    //End of Rev 10.0 Pref AppV 4.0.8 Suman    16/05/2023  mantis 26119

    //Begin Rev 11.0 Pref AppV 4.0.8 Suman    19/05/2023  mantis 26163
    var IsBeatPlanAvailable : Boolean by bindToPreferenceField(false, "IsBeatPlanAvailable")
    //End of Rev 11.0 Pref AppV 4.0.8 Suman    19/05/2023  mantis 26163

    var IsMenuShowAIMarketAssistant : Boolean by bindToPreferenceField(false, "IsMenuShowAIMarketAssistant")

    var IsUpdateVisitDataInTodayTable : Boolean by bindToPreferenceField(false, "IsUpdateVisitDataInTodayTable")

    //Begin Rev 12.0 Pref AppV 4.1.6 Saheli    20/06/2023  mantis 0026391
    var isNewOptimizedStock : Boolean by bindToPreferenceField(true, "isNewOptimizedStock")
    var savefromOrderOrStock : Boolean by bindToPreferenceField(true, "savefromOrderOrStock")
    //end Rev 12.0 Pref AppV 4.1.6 Saheli    20/06/2023  mantis 0026391

    //Begin 12.0 Pref v 4.1.6 Tufan 11/07/2023 mantis 26546 revisit sync time
    var ShopSyncIntervalInMinutes: String by bindToPreferenceField("10", "ShopSyncIntervalInMinutes")
    //End 12.0 Pref v 4.1.6 Tufan 11/07/2023 mantis 26546 revisit sync time

    //Begin 13.0 Pref v 4.1.6 Suman 13/07/2023 mantis 26555 Usersettings
    var IsUsbDebuggingRestricted : Boolean by bindToPreferenceField(false, "IsUsbDebuggingRestricted")
    //End 13.0 Pref v 4.1.6 Suman 13/07/2023 mantis 26555 Usersettings

    var IsShowWhatsAppIconforVisit : Boolean by bindToPreferenceField(false, "IsShowWhatsAppIconforVisit")
    var IsAutomatedWhatsAppSendforRevisit : Boolean by bindToPreferenceField(false, "IsAutomatedWhatsAppSendforRevisit")

    var UserLoginContactID : String by bindToPreferenceField("", "UserLoginContactID")

    //Begin 14.0 Pref v 4.1.6 Tufan 11/08/2023 mantis 26655 Order Past Days
    var Order_Past_Days: String by bindToPreferenceField("10", "Order_Past_Days")
    var IsAllowBackdatedOrderEntry : Boolean by bindToPreferenceField(false, "IsAllowBackdatedOrderEntry")
  //End 14.0 Pref v 4.1.6 Tufan 11/08/2023 mantis 26655 Order Past Days

    //Begin 15.0 Pref v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product
  var Show_distributor_scheme_with_Product : Boolean by bindToPreferenceField(false, "Show_distributor_scheme_with_Product")
    //end 15.0 Pref v 4.1.6 Tufan 22/08/2023 mantis 26649 Show distributor scheme with Product

   //Begin 16.0 Pref v 4.1.6 Tufan 07/09/2023 mantis 26785 Multi visit Interval in Minutes Against the Same Shop
   var MultiVisitIntervalInMinutes : String by bindToPreferenceField("1", "MultiVisitIntervalInMinutes")
    //End 16.0 Pref v 4.1.6 Tufan 07/09/2023 mantis 26785 Multi visit Interval in Minutes Against the Same Shop


    //Begin 16.0 Pref v 4.1.6 Tufan 21/09/2023 mantis 26812 AND 26813  FSSAI Lic No and GSTINPANMandatoryforSHOPTYPE4 In add shop page edit
    var GSTINPANMandatoryforSHOPTYPE4: Boolean by bindToPreferenceField(
        false,
        "GSTINPANMandatoryforSHOPTYPE4"
    )
    var FSSAILicNoEnableInShop: Boolean by bindToPreferenceField(
        false,
        "FSSAILicNoEnableInShop"
    )
    var FSSAILicNoMandatoryInShop4: Boolean by bindToPreferenceField(
        false,
        "FSSAILicNoMandatoryInShop4"
    )
    //end 16.0 Pref v 4.1.6 Tufan 21/09/2023 mantis 26812 AND 26813  FSSAI Lic No and GSTINPANMandatoryforSHOPTYPE4 In add shop page edit

    var IsDisabledUpdateAddress: Boolean by bindToPreferenceField(false, "IsDisabledUpdateAddress")

    var IsAutoLogoutFromBatteryCheck: Boolean by bindToPreferenceField(false, "IsAutoLogoutFromBatteryCheck")
    var IsLoggedIn: Boolean by bindToPreferenceField(false, "IsLoggedIn")
    var IsAnyPageVisitFromDshboard: Boolean by bindToPreferenceField(false, "IsAnyPageVisitFromDshboard")

    //Begin Puja 16.11.23 mantis-0026997 //

    var isLeadContactNumber: Boolean by bindToPreferenceField(false, "isLeadContactNumber")
    var isModelEnable: Boolean by bindToPreferenceField(false, "isModelEnable")
    var isPrimaryApplicationEnable: Boolean by bindToPreferenceField(false, "isPrimaryApplicationEnable")
    var isSecondaryApplicationEnable: Boolean by bindToPreferenceField(false, "isSecondaryApplicationEnable")
    var isBookingAmount: Boolean by bindToPreferenceField(false, "isBookingAmount")
    var isLeadTypeEnable: Boolean by bindToPreferenceField(false, "isLeadTypeEnable")
    var isStageEnable: Boolean by bindToPreferenceField(false, "isStageEnable")
    var isFunnelStageEnable: Boolean by bindToPreferenceField(false, "isFunnelStageEnable")

    //End puja 16.11.23 mantis-0026997 //

    var IsShowMenuCRMContacts: Boolean by bindToPreferenceField(false, "IsShowMenuCRMContacts")

    //mantis id 27063 Suman 04-12-2023
    var IsCallLogHistoryActivated: Boolean by bindToPreferenceField(false, "IsCallLogHistoryActivated")

    var IsGPSRouteSync: Boolean by bindToPreferenceField(true, "IsGPSRouteSync")

    var IsSyncBellNotificationInApp: Boolean by bindToPreferenceField(true, "IsSyncBellNotificationInApp")
    var IsShowCustomerLocationShare: Boolean by bindToPreferenceField(true, "IsShowCustomerLocationShare")
    var scheduler_template: String? by bindToPreferenceFieldNullable()
    var scheduler_file: String? by bindToPreferenceFieldNullable()
    var storeGmailId: String? by bindToPreferenceFieldNullable()
    var storeGmailPassword: String? by bindToPreferenceFieldNullable()
    var minAccuracy: String by bindToPreferenceField("200", "minAccuracy")
    var isAutoMailProceeding: Boolean by bindToPreferenceField(false, "isAutoMailProceeding")
    var isTemplateDialogAlertShow: Boolean by bindToPreferenceField(true, "isTemplateDialogAlertShow")
    var logId: String by bindToPreferenceField("", "logId")
    var loginPassword: String by bindToPreferenceField("", "loginPassword")
    var AdditionalInfoRequiredForTimelines: Boolean by bindToPreferenceField(true, "AdditionalInfoRequiredForTimelines")
    var ShowPartyWithGeoFence: Boolean by bindToPreferenceField(false, "ShowPartyWithGeoFence")
    var ShowPartyWithCreateOrder: Boolean by bindToPreferenceField(false, "ShowPartyWithCreateOrder")
    var Allow_past_days_for_apply_reimbursement: String by bindToPreferenceField("", "Allow_past_days_for_apply_reimbursement")
    var IsShowLeaderBoard: Boolean by bindToPreferenceField(false, "IsShowLeaderBoard")

    var automail_sending_email: String by bindToPreferenceField("", "automail_sending_email")
    var automail_sending_pass: String by bindToPreferenceField("", "automail_sending_pass")
    var recipient_email_ids: String by bindToPreferenceField("", "recipient_email_ids")
    //mantis id 0027389 Puja 23-04-2024
    var AdditionalinfoRequiredforContactListing: Boolean by bindToPreferenceField(true, "AdditionalinfoRequiredforContactListing")
    var AdditionalinfoRequiredforContactAdd: Boolean by bindToPreferenceField(true, "AdditionalinfoRequiredforContactAdd")
    var ContactAddresswithGeofence: Boolean by bindToPreferenceField(true, "ContactAddresswithGeofence")
    var IsShowOtherInfoinActivity: Boolean by bindToPreferenceField(true, "IsShowOtherInfoinActivity")

    var user_ShopStatus: Boolean by bindToPreferenceField(false, "user_ShopStatus")

    var ShowUserwisePartyWithGeoFence : Boolean by bindToPreferenceField(false, "ShowUserwisePartyWithGeoFence")
    var ShowUserwisePartyWithCreateOrder : Boolean by bindToPreferenceField(false, "ShowUserwisePartyWithCreateOrder")
    //begin mantis id 0027432 loc_k & firebase_k functionality Puja 08-05-2024 v4.2.7
    var loc_k : String by bindToPreferenceField("", "loc_k")
    var firebase_k : String by bindToPreferenceField("", "firebase_k")
    //end mantis id 0027432 loc_k & firebase_k functionality Puja 08-05-2024 v4.2.7
    var IsRouteUpdateForShopUser : Boolean by bindToPreferenceField(true, "IsRouteUpdateForShopUser")
    var isCallLogHintPermissionGranted: Boolean by bindToPreferenceField(false, "isCallLogHintPermissionGranted")

    var IsShowCRMOpportunity: Boolean by bindToPreferenceField(false, "IsShowCRMOpportunity")
    var IsEditEnableforOpportunity: Boolean by bindToPreferenceField(false, " IsEditEnableforOpportunity")
    var IsDeleteEnableforOpportunity: Boolean by bindToPreferenceField(false, "IsDeleteEnableforOpportunity")


    var IsCRMPhonebookSyncEnable: Boolean by bindToPreferenceField(true, "IsCRMPhonebookSyncEnable")
    var IsCRMSchedulerEnable: Boolean by bindToPreferenceField(true, "IsCRMSchedulerEnable")
    var IsCRMAddEnable: Boolean by bindToPreferenceField(true, "IsCRMAddEnable")
    var IsCRMEditEnable: Boolean by bindToPreferenceField(true, "IsCRMEditEnable")

    //Rev 17.0 Pref v 4.2.8 Suman 25/06/2024 mantis 27575
    var IsUserWiseLMSEnable: Boolean by bindToPreferenceField(false, "IsUserWiseLMSEnable")
    var IsUserWiseLMSFeatureOnly: Boolean by bindToPreferenceField(false, "IsUserWiseLMSFeatureOnly")

    var IsAlarmServiceRestartCalled: Boolean by bindToPreferenceField(false, "IsAlarmServiceRestartCalled")

    //Suman 27-07-2024 mantis id 27647
    var IsUserWiseRecordAudioEnableForVisitRevisit: Boolean by bindToPreferenceField(false, "IsUserWiseRecordAudioEnableForVisitRevisit")

    var prevShopAudioSyncTimeStamp: Long by bindToPreferenceField(0, "prevShopAudioSyncTimeStamp")

    var QuestionAfterNoOfContentForLMS: String by bindToPreferenceField("1", "QuestionAfterNoOfContentForLMS")

    var videoCompleteCount: String by bindToPreferenceField("0", "videoCompleteCount")

    var like_count: Int by bindToPreferenceField(0, "like_count")

    var share_count: Int by bindToPreferenceField(0, "share_count")

    var comment_count: Int by bindToPreferenceField(0, "comment_count")

    var correct_answer_count: Int by bindToPreferenceField(0, "correct_answer_count")

    var wrong_answer_count: Int by bindToPreferenceField(0, "wrong_answer_count")

    var content_watch_count: Int by bindToPreferenceField(0, "content_watch_count")

    var FirstLogiForTheDayTag: Boolean by bindToPreferenceField(true, "FirstLogiForTheDayTag")





    var LastVideoPlay_TopicID : String by bindToPreferenceField("", "LastVideoPlay_TopicID")
    var LastVideoPlay_TopicName : String by bindToPreferenceField("", "LastVideoPlay_TopicName")
    var LastVideoPlay_ContentID : String by bindToPreferenceField("", "LastVideoPlay_ContentID")
    var LastVideoPlay_ContentName : String by bindToPreferenceField("", "LastVideoPlay_ContentName")
    var LastVideoPlay_VidPosition : String by bindToPreferenceField("", "LastVideoPlay_VidPosition")
    var LastVideoPlay_BitmapURL : String by bindToPreferenceField("", "LastVideoPlay_BitmapURL")
    var LastVideoPlay_ContentDesc : String by bindToPreferenceField("", "LastVideoPlay_ContentDesc")
   // var LastVideoPlay_ContentParcentBar : String by bindToPreferenceField("", "LastVideoPlay_ContentParcentBar")
    var LastVideoPlay_ContentParcent : String by bindToPreferenceField("", "LastVideoPlay_ContentParcent")
   // var LastVideoPlay_ContentParcentStatus : String by bindToPreferenceField("", "LastVideoPlay_ContentParcentStatus")

    var IsAllowGPSTrackingInBackgroundForLMS: Boolean by bindToPreferenceField(true, "IsAllowGPSTrackingInBackgroundForLMS")

    var CurrentBookmarkCount: Int by bindToPreferenceField(0, "CurrentBookmarkCount")

    //Suman 17-09-2024 mantis 27700
    var IsRetailOrderStatusRequired: Boolean by bindToPreferenceField(false, "IsRetailOrderStatusRequired")
}


