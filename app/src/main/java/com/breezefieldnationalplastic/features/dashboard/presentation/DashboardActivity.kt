package com.breezefieldnationalplastic.features.dashboard.presentation

//import com.fieldtrackingsystem.features.logout.presentation.LogOutTimeSelect

import android.Manifest
import android.R.attr.fastScrollAlwaysVisible
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.*
import android.os.*
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.provider.MediaStore
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.breezedsm.app.domain.NewOrderDataEntity
import com.breezefieldnationalplastic.*
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.*
import com.breezefieldnationalplastic.app.NewFileUtils.browseDocuments
import com.breezefieldnationalplastic.app.NewFileUtils.browsePDFDocuments
import com.breezefieldnationalplastic.app.NewFileUtils.getExtension
import com.breezefieldnationalplastic.app.domain.*
import com.breezefieldnationalplastic.app.types.DashboardType
import com.breezefieldnationalplastic.app.types.FragType
import com.breezefieldnationalplastic.app.types.TopBarConfig
import com.breezefieldnationalplastic.app.uiaction.DisplayAlert
import com.breezefieldnationalplastic.app.utils.*
import com.breezefieldnationalplastic.app.utils.AppUtils.Companion.isProfile
import com.breezefieldnationalplastic.base.BaseResponse
import com.breezefieldnationalplastic.base.presentation.BaseActivity
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.NewQuotation.*
import com.breezefieldnationalplastic.features.NewQuotation.api.GetQuotRegProvider
import com.breezefieldnationalplastic.features.NewQuotation.model.ViewDetailsQuotResponse
import com.breezefieldnationalplastic.features.SearchLocation.SearchLocationFragment
import com.breezefieldnationalplastic.features.SearchLocation.locationInfoModel
import com.breezefieldnationalplastic.features.TA.ViewAllTAListFragment
import com.breezefieldnationalplastic.features.achievement.AchievementFragment
import com.breezefieldnationalplastic.features.activities.presentation.*
import com.breezefieldnationalplastic.features.addAttendence.*
import com.breezefieldnationalplastic.features.addAttendence.api.addattendenceapi.AddAttendenceRepoProvider
import com.breezefieldnationalplastic.features.addAttendence.api.leavetytpeapi.LeaveTypeRepoProvider
import com.breezefieldnationalplastic.features.addAttendence.model.GetReportToFCMResponse
import com.breezefieldnationalplastic.features.addAttendence.model.GetReportToResponse
import com.breezefieldnationalplastic.features.addAttendence.model.Leave_list_Response
import com.breezefieldnationalplastic.features.addorder.presentation.AddOrderFragment
import com.breezefieldnationalplastic.features.addshop.model.AddShopRequestData
import com.breezefieldnationalplastic.features.addshop.model.AddShopResponse
import com.breezefieldnationalplastic.features.addshop.presentation.AddShopFragment
import com.breezefieldnationalplastic.features.addshop.presentation.OTPVerificationDialog
import com.breezefieldnationalplastic.features.addshop.presentation.ScanImageFragment
import com.breezefieldnationalplastic.features.addshop.presentation.ShowCardDetailsDialog
import com.breezefieldnationalplastic.features.alarm.model.AlarmData
import com.breezefieldnationalplastic.features.alarm.presetation.AttendanceReportFragment
import com.breezefieldnationalplastic.features.alarm.presetation.PerformanceReportFragment
import com.breezefieldnationalplastic.features.alarm.presetation.VisitReportDetailsFragment
import com.breezefieldnationalplastic.features.alarm.presetation.VisitReportFragment
import com.breezefieldnationalplastic.features.attendance.AttendanceFragment
import com.breezefieldnationalplastic.features.attendance.api.AttendanceRepositoryProvider
import com.breezefieldnationalplastic.features.attendance.model.AttendanceRequest
import com.breezefieldnationalplastic.features.attendance.model.AttendanceResponse
import com.breezefieldnationalplastic.features.averageshop.presentation.AverageShopFragment
import com.breezefieldnationalplastic.features.avgorder.presentation.AverageOrderFragment
import com.breezefieldnationalplastic.features.avgtimespent.presentation.AvgTimespentShopListFragment
import com.breezefieldnationalplastic.features.beatCustom.TeamBeatListFragment
import com.breezefieldnationalplastic.features.billing.presentation.AddBillingFragment
import com.breezefieldnationalplastic.features.billing.presentation.BillingDetailsFragment
import com.breezefieldnationalplastic.features.billing.presentation.BillingListFragment
import com.breezefieldnationalplastic.features.changepassword.presentation.ChangePasswordDialog
import com.breezefieldnationalplastic.features.chat.model.ChatListDataModel
import com.breezefieldnationalplastic.features.chat.model.ChatUserDataModel
import com.breezefieldnationalplastic.features.chat.model.GroupUserDataModel
import com.breezefieldnationalplastic.features.chat.presentation.*
import com.breezefieldnationalplastic.features.chatbot.presentation.ChatBotFragment
import com.breezefieldnationalplastic.features.chatbot.presentation.ChatBotShopListFragment
import com.breezefieldnationalplastic.features.chatbot.presentation.ReportsFragment
import com.breezefieldnationalplastic.features.chatbot.presentation.SelectLanguageDialog
import com.breezefieldnationalplastic.features.commondialog.presentation.CommonDialog
import com.breezefieldnationalplastic.features.commondialog.presentation.CommonDialogClickListener
import com.breezefieldnationalplastic.features.commondialogsinglebtn.AddFeedbackSingleBtnDialog
import com.breezefieldnationalplastic.features.commondialogsinglebtn.CommonDialogSingleBtn
import com.breezefieldnationalplastic.features.commondialogsinglebtn.OnDialogClickListener
import com.breezefieldnationalplastic.features.commondialogsinglebtn.TermsAndConditionsSingleBtnDialog
import com.breezefieldnationalplastic.features.contacts.ActivityDtlsFrag
import com.breezefieldnationalplastic.features.contacts.AddOpptFrag
import com.breezefieldnationalplastic.features.contacts.ContactDtls
import com.breezefieldnationalplastic.features.contacts.ContactGr
import com.breezefieldnationalplastic.features.contacts.ContactsAddFrag
import com.breezefieldnationalplastic.features.contacts.ContactsFrag
import com.breezefieldnationalplastic.features.contacts.SchedulerAddFormFrag
import com.breezefieldnationalplastic.features.contacts.SchedulerViewFrag
import com.breezefieldnationalplastic.features.contacts.TemplateAddFrag
import com.breezefieldnationalplastic.features.contacts.TemplateViewFrag
import com.breezefieldnationalplastic.features.contacts.ViewCrmOpptFrag
import com.breezefieldnationalplastic.features.dailyPlan.prsentation.AllShopListFragment
import com.breezefieldnationalplastic.features.dailyPlan.prsentation.DailyPlanListFragment
import com.breezefieldnationalplastic.features.dailyPlan.prsentation.PlanDetailsFragment
import com.breezefieldnationalplastic.features.damageProduct.ShopDamageProductListFrag
import com.breezefieldnationalplastic.features.damageProduct.ShopDamageProductSubmitFrag
import com.breezefieldnationalplastic.features.dashboard.presentation.api.ShopVisitImageUploadRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.api.dashboardApi.DashboardRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.api.otpsentapi.OtpSentRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.api.otpverifyapi.OtpVerificationRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.api.unreadnotificationapi.UnreadNotificationRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.getcontentlisapi.GetContentListRepoProvider
import com.breezefieldnationalplastic.features.dashboard.presentation.model.ContentListResponseModel
import com.breezefieldnationalplastic.features.dashboard.presentation.model.ShopVisitImageUploadInputModel
import com.breezefieldnationalplastic.features.dashboard.presentation.model.UnreadNotificationResponseModel
import com.breezefieldnationalplastic.features.device_info.presentation.DeviceInfoListFragment
import com.breezefieldnationalplastic.features.distributorwiseorder.DistributorwiseorderlistFragment
import com.breezefieldnationalplastic.features.document.DocumentRepoFeatureNewFragment
import com.breezefieldnationalplastic.features.document.presentation.DocumentListFragment
import com.breezefieldnationalplastic.features.document.presentation.DocumentTypeListFragment
import com.breezefieldnationalplastic.features.document.presentation.OpenFileWebViewFragment
import com.breezefieldnationalplastic.features.dymanicSection.presentation.AddDynamicFragment
import com.breezefieldnationalplastic.features.dymanicSection.presentation.AllDynamicListFragment
import com.breezefieldnationalplastic.features.dymanicSection.presentation.DynamicListFragment
import com.breezefieldnationalplastic.features.dymanicSection.presentation.EditDynamicFragment
import com.breezefieldnationalplastic.features.gpsDisabilityScreen.GpsDisableFragment
import com.breezefieldnationalplastic.features.home.presentation.HomeFragment
import com.breezefieldnationalplastic.features.homelocation.presentation.HomeLocationFragment
import com.breezefieldnationalplastic.features.homelocation.presentation.HomeLocationMapFragment
import com.breezefieldnationalplastic.features.know_your_state.KnowYourStateFragment
import com.breezefieldnationalplastic.features.lead.LeadFrag
import com.breezefieldnationalplastic.features.lead.ViewLeadFrag
import com.breezefieldnationalplastic.features.leaderboard.LeaderBoardFrag
import com.breezefieldnationalplastic.features.leaveapplynew.LeaveHome
import com.breezefieldnationalplastic.features.leaveapplynew.model.clearAttendanceonRejectReqModelRejectReqModel
import com.breezefieldnationalplastic.features.localshops.LocalShopListFragment
import com.breezefieldnationalplastic.features.localshops.LocalShopListMapFragment
import com.breezefieldnationalplastic.features.localshops.NearByShopsMapFragment
import com.breezefieldnationalplastic.features.location.*
import com.breezefieldnationalplastic.features.location.api.LocationRepoProvider
import com.breezefieldnationalplastic.features.location.model.*
import com.breezefieldnationalplastic.features.location.shopRevisitStatus.ShopRevisitStatusRepositoryProvider
import com.breezefieldnationalplastic.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.breezefieldnationalplastic.features.login.ShopFeedbackEntity
import com.breezefieldnationalplastic.features.login.model.alarmconfigmodel.AlarmConfigDataModel
import com.breezefieldnationalplastic.features.login.presentation.LoginActivity
import com.breezefieldnationalplastic.features.logout.presentation.api.LogoutRepositoryProvider
import com.breezefieldnationalplastic.features.logoutsync.presentation.LogoutSyncFragment
import com.breezefieldnationalplastic.features.marketAssist.MarketAssistTabFrag
import com.breezefieldnationalplastic.features.marketAssist.ShopDtlsMarketAssistFrag
import com.breezefieldnationalplastic.features.marketAssist.ShopListMarketAssistFrag
import com.breezefieldnationalplastic.features.marketing.presentation.MarketingPagerFragment
import com.breezefieldnationalplastic.features.meetinglist.prsentation.MeetingListFragment
import com.breezefieldnationalplastic.features.member.MapViewForTeamFrag
import com.breezefieldnationalplastic.features.member.model.TeamLocDataModel
import com.breezefieldnationalplastic.features.member.model.TeamShopListDataModel
import com.breezefieldnationalplastic.features.member.presentation.*
import com.breezefieldnationalplastic.features.menuBeat.MenuBeatFrag
import com.breezefieldnationalplastic.features.micro_learning.presentation.FileOpeningTimeIntentService
import com.breezefieldnationalplastic.features.micro_learning.presentation.MicroLearningListFragment
import com.breezefieldnationalplastic.features.micro_learning.presentation.MicroLearningWebViewFragment
import com.breezefieldnationalplastic.features.myallowancerequest.MyallowanceRequestFragment
import com.breezefieldnationalplastic.features.myjobs.model.CustomerDataModel
import com.breezefieldnationalplastic.features.myjobs.presentation.*
import com.breezefieldnationalplastic.features.mylearning.MyLearningFragment
import com.breezefieldnationalplastic.features.myorder.presentation.MyOrderListFragment
import com.breezefieldnationalplastic.features.myprofile.presentation.MyProfileFragment
import com.breezefieldnationalplastic.features.nearbyshops.api.ShopListRepositoryProvider
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopData
import com.breezefieldnationalplastic.features.nearbyshops.model.ShopListResponse
import com.breezefieldnationalplastic.features.nearbyshops.multipleattachImage.MultipleImageFragment
import com.breezefieldnationalplastic.features.nearbyshops.presentation.BeatListFragment
import com.breezefieldnationalplastic.features.nearbyshops.presentation.NearByShopsListFragment
import com.breezefieldnationalplastic.features.nearbyshops.presentation.NewNearByShopsListFragment
import com.breezefieldnationalplastic.features.nearbyshops.presentation.ShopCallHisFrag
import com.breezefieldnationalplastic.features.nearbyuserlist.presentation.NearbyUserListFragment
import com.breezefieldnationalplastic.features.newcollection.CollectionDetailsStatusFragment
import com.breezefieldnationalplastic.features.newcollection.CollectionShopListFragment
import com.breezefieldnationalplastic.features.newcollection.NewCollectionListFragment
import com.breezefieldnationalplastic.features.newcollectionreport.*
import com.breezefieldnationalplastic.features.notification.NotificationFragment
import com.breezefieldnationalplastic.features.orderITC.CartListFrag
import com.breezefieldnationalplastic.features.orderITC.OrderListFrag
import com.breezefieldnationalplastic.features.orderITC.ProductListFrag
import com.breezefieldnationalplastic.features.orderITC.ViewNewOrdHisAllFrag
import com.breezefieldnationalplastic.features.orderITC.ViewNewOrdHistoryFrag
import com.breezefieldnationalplastic.features.orderITC.ViewOrdDtls
import com.breezefieldnationalplastic.features.orderList.NewDateWiseOrderListFragment
import com.breezefieldnationalplastic.features.orderList.NewOrderListFragment
import com.breezefieldnationalplastic.features.orderList.OrderListFragment
import com.breezefieldnationalplastic.features.orderdetail.presentation.OrderDetailFragment
import com.breezefieldnationalplastic.features.orderhistory.ActivityMapFragment
import com.breezefieldnationalplastic.features.orderhistory.OrderhistoryFragment
import com.breezefieldnationalplastic.features.orderhistory.TimeLineFragment
import com.breezefieldnationalplastic.features.orderhistory.activitiesapi.LocationFetchRepositoryProvider
import com.breezefieldnationalplastic.features.orderhistory.model.FetchLocationRequest
import com.breezefieldnationalplastic.features.orderhistory.model.FetchLocationResponse
import com.breezefieldnationalplastic.features.orderhistory.model.LocationData
import com.breezefieldnationalplastic.features.pendinglocationinout.PendingOutLocationFrag
import com.breezefieldnationalplastic.features.performance.GpsStatusFragment
import com.breezefieldnationalplastic.features.performance.PerformanceFragment
import com.breezefieldnationalplastic.features.performance.api.UpdateGpsStatusRepoProvider
import com.breezefieldnationalplastic.features.performance.model.UpdateGpsInputParamsModel
import com.breezefieldnationalplastic.features.performanceAPP.OwnPerformanceFragment
import com.breezefieldnationalplastic.features.performanceAPP.PerformanceAppFragment
import com.breezefieldnationalplastic.features.performanceAPP.allPerformanceFrag
import com.breezefieldnationalplastic.features.permissionList.ViewPermissionFragment
import com.breezefieldnationalplastic.features.photoReg.*
import com.breezefieldnationalplastic.features.privacypolicy.PrivacypolicyWebviewFrag
import com.breezefieldnationalplastic.features.quotation.presentation.*
import com.breezefieldnationalplastic.features.reimbursement.presentation.*
import com.breezefieldnationalplastic.features.report.presentation.*
import com.breezefieldnationalplastic.features.returnsOrder.*
import com.breezefieldnationalplastic.features.settings.presentation.SettingsFragment
import com.breezefieldnationalplastic.features.shopFeedbackHistory.ShopFeedbackHisFrag
import com.breezefieldnationalplastic.features.shopdetail.presentation.*
import com.breezefieldnationalplastic.features.shopdetail.presentation.api.EditShopRepoProvider
import com.breezefieldnationalplastic.features.stock.StockDetailsFragment
import com.breezefieldnationalplastic.features.stock.StockListFragment
import com.breezefieldnationalplastic.features.stockAddCurrentStock.AddShopStockFragment
import com.breezefieldnationalplastic.features.stockAddCurrentStock.UpdateShopStockFragment
import com.breezefieldnationalplastic.features.stockAddCurrentStock.ViewStockDetailsFragment
import com.breezefieldnationalplastic.features.stockAddCurrentStock.model.MultipleImageFileUploadonStock
import com.breezefieldnationalplastic.features.stockCompetetorStock.AddCompetetorStockFragment
import com.breezefieldnationalplastic.features.stockCompetetorStock.CompetetorStockFragment
import com.breezefieldnationalplastic.features.stockCompetetorStock.ViewComStockProductDetails
import com.breezefieldnationalplastic.features.survey.SurveyFrag
import com.breezefieldnationalplastic.features.survey.SurveyViewDtlsFrag
import com.breezefieldnationalplastic.features.survey.SurveyViewFrag
import com.breezefieldnationalplastic.features.task.presentation.AddTaskFragment
import com.breezefieldnationalplastic.features.task.presentation.CalenderTaskFragment
import com.breezefieldnationalplastic.features.task.presentation.EditTaskFragment
import com.breezefieldnationalplastic.features.task.presentation.TaskListFragment
import com.breezefieldnationalplastic.features.taskManagement.TaskManagementFrag
import com.breezefieldnationalplastic.features.taskManagement.ViewTaskManagementFrag
import com.breezefieldnationalplastic.features.timesheet.presentation.AddTimeSheetFragment
import com.breezefieldnationalplastic.features.timesheet.presentation.EditTimeSheetFragment
import com.breezefieldnationalplastic.features.timesheet.presentation.TimeSheetListFragment
import com.breezefieldnationalplastic.features.viewAllOrder.CartFragment
import com.breezefieldnationalplastic.features.viewAllOrder.OrderTypeListFragment
import com.breezefieldnationalplastic.features.viewAllOrder.ViewAllOrderListFragment
import com.breezefieldnationalplastic.features.viewAllOrder.ViewCartFragment
import com.breezefieldnationalplastic.features.viewAllOrder.orderNew.NewOdrScrListFragment
import com.breezefieldnationalplastic.features.viewAllOrder.orderNew.NewOrderScrActiFragment
import com.breezefieldnationalplastic.features.viewAllOrder.orderNew.NewOrderScrOrderDetailsFragment
import com.breezefieldnationalplastic.features.viewAllOrder.orderNew.NeworderScrCartFragment
import com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized.OrderProductCartFrag
import com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized.OrderProductListFrag
import com.breezefieldnationalplastic.features.viewPPDDStock.ViewOutstandingFragment
import com.breezefieldnationalplastic.features.viewPPDDStock.ViewPPDDListFragment
import com.breezefieldnationalplastic.features.viewPPDDStock.ViewPPDDListOutstandingFragment
import com.breezefieldnationalplastic.features.viewPPDDStock.ViewStockFragment
import com.breezefieldnationalplastic.features.weather.presentation.WeatherFragment
import com.breezefieldnationalplastic.mappackage.MapActivity
import com.breezefieldnationalplastic.mappackage.MapActivityWithoutPath
import com.breezefieldnationalplastic.mappackage.SendBrod
import com.breezefieldnationalplastic.widgets.AppCustomEditText
import com.breezefieldnationalplastic.widgets.AppCustomTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.messaging.FirebaseMessaging
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.themechangeapp.pickimage.PermissionHelper
import com.themechangeapp.pickimage.PermissionHelper.Companion.REQUEST_CODE_DOCUMENT
import com.themechangeapp.pickimage.PermissionHelper.Companion.REQUEST_CODE_DOCUMENT_PDF
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_dashboard_new.*
import kotlinx.android.synthetic.main.menu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.alexandroid.gps.GpsStatusDetector
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.*
import java.sql.Date
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.R.attr.name
import android.content.Context.RECEIVER_EXPORTED
import android.content.pm.ActivityInfo
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.breezefieldnationalplastic.features.addshop.model.AudioFetchDataCLass
import com.breezefieldnationalplastic.features.login.api.opportunity.OpportunityRepoProvider
import com.breezefieldnationalplastic.features.login.model.NewSettingsResponseModel
import com.breezefieldnationalplastic.features.mylearning.AllTopicsWiseContents
import com.breezefieldnationalplastic.features.mylearning.BookmarkFetchResponse
import com.breezefieldnationalplastic.features.mylearning.BookmarkFrag
import com.breezefieldnationalplastic.features.mylearning.BookmarkPlayFrag
import com.breezefieldnationalplastic.features.mylearning.KnowledgeHubAllVideoList
import com.breezefieldnationalplastic.features.mylearning.LeaderboardLmsFrag
import com.breezefieldnationalplastic.features.mylearning.LmsQuestionAnswerSet
import com.breezefieldnationalplastic.features.mylearning.MyLearningAllVideoList
import com.breezefieldnationalplastic.features.mylearning.MyLearningTopicList
import com.breezefieldnationalplastic.features.mylearning.MyLearningVideoPlay
import com.breezefieldnationalplastic.features.mylearning.MyPerformanceFrag
import com.breezefieldnationalplastic.features.mylearning.MyTopicsWiseContents
import com.breezefieldnationalplastic.features.mylearning.NotificationLMSFragment
import com.breezefieldnationalplastic.features.mylearning.SearchLmsFrag
import com.breezefieldnationalplastic.features.mylearning.SearchLmsKnowledgeFrag
import com.breezefieldnationalplastic.features.mylearning.SearchLmsLearningFrag
import com.breezefieldnationalplastic.features.mylearning.VideoPlayLMS
import com.breezefieldnationalplastic.features.mylearning.apiCall.LMSRepoProvider
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.LifecycleCallback.getFragment
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.android.synthetic.main.activity_login_new.login_TV
import kotlinx.android.synthetic.main.toolbar_layout.tv_saved_count
import org.json.JSONException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


/*
 * Created by rp : 26-10-2017:17:59
 */
// Revision History
// 1.0 AppV 4.0.6 Saheli 28/12/2022 revisit handle from menu
// 2.0 AppV 4.0.6 Suman 29/12/2022 PartyUpdateAddrMandatory check before photo registration
// 3.0 AppV 4.0.6 DashboardActivity Saheli 02/01/2023 ShowTotalVisitAppMenu
// 4.0 DashboardActivity AppV 4.0.6 saheli 12-01-2023 multiple contact Data added on Api called
// 5.0 DashboardActivity AppV 4.0.6 Suman 13/01/2023 MenuBeatFrag
// 6.0 DashboardActivity AppV 4.0.6 Saheli 16/01/2023 AutoMail Sended work
// 7.0 DashboardActivity AppV 4.0.6 saheli 20-01-2023 Shop duartion Issue mantis 25597
// 8.0 DashboardActivity AppV 4.0.6 Suman 23-01-2023 Auto mail from notification flow of quotation 25614
// 9.0 DashboardActivity AppV 4.0.6 Suman 24-01-2023 Corss button with multi contact select
// 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList for shop update mantis 25624
// 10.0 DashboardActivity AppV 4.0.7 saheli 10-02-2023 order rate issue mantis 25666
// 11.0 DashboardActivity AppV 4.0.7 saheli 16-02-2023 duartion calculation issue(multiple visit last data calculation) mantis 25675
// 12.0 DashboardActivity AppV 4.0.7 saheli 24-03-2023 room main thread optimizetion location db insertion in dashboardActivity revisitShop(image: String) mantis 0025753
// 13.0 DashboardActivity AppV 4.0.7 Suman 31-03-2023 quotation auto mail app kill work mantis 25766
// 14.0 DashboardActivity AppV 4.0.8 saheli 05-04-2023 mantis 0025783 In-app privacy policy working in menu & Login
// 15.0 DashboardActivity AppV 4.0.8 Suman 19/04/2023 Dashboard Onresume updation for language 0025874
// 16.0 DashboardActivity AppV 4.0.8 saheli 20/04/2023 mantis 25860 performnace module
// Rev 17 DashboardActivity AppV 4.0.8 Suman 24/04/2023 distanct+station calculation 25806
// Rev 18 DashboardActivity AppV 4.0.8 Suman 28/04/2023 worker manager updation 25973
// Rev 19.0 DashboardActivity AppV 4.0.8 saheli 05/05/2023 0026023: A new Menu named as ‘Task Management’ should be implemented in the menu bar.
// REv 20.0 DashboardActivity AppV 4.0.8 saheli 08/05/2023 0026024 :under the 'Assigned Lead' page
// Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
// Rev 22.0 DashboardActivity AppV 4.1.3 Suman 18-05-2023 mantis 26162
// rev 23.0 DashobaordActivity AppV 4.1.6 Saheli 19/06/2023 pdf remark field mantis 26139
// Rev 24.0 DashboardACtivity v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli
// Rev 25.0 DashboardACtivity v 4.2.6 stock optmization mantis 0027421 06-05-2024 Suman
// 26.0 DashboardActivity Fingerprint flow update Suman 14-05-2024 mantis id 0027450 v4.2.8
//Rev 27.0 Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8

class DashboardActivity : BaseActivity(), View.OnClickListener, BaseNavigation,
    OnCompleteListener<Void>, GpsStatusDetector.GpsStatusDetectorCallBack {
    override fun onComplete(task: Task<Void>) {
        mPendingGeofenceTask = PendingGeofenceTask.NONE;

        if (task.isSuccessful) {
            if (Pref.isGeoFenceAdded)
                return
            Pref.isGeoFenceAdded = true
// updateGeofencesAdded(!getGeofencesAdded());
// var messageId = getGeofencesAdded() ? R.string.geofences_added :
// R.string.geofences_removed;
// Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
// if (getGeofencesAdded())
// showSnackMessage("onCompleteSuccess:GeofenceAdded")
// else
// showSnackMessage("onCompleteSuccess:GeofenceRemoved")
        } else {
            // Get the status code for the error and log it using a user-friendly message.
// var errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            showSnackMessage("onCompleteError")
// Log.w(TAG, errorMessage);
        }
    }

    @SuppressLint("MissingPermission")
    override fun loadFragment(mFragType: FragType, addToStack: Boolean, initializeObject: Any) {
        AppUtils.contx = this
        Pref.IsLoggedIn = true

        drawerLayout.closeDrawers()

        if (isFinishing || getCurrentFragType() == mFragType) {
            if (getCurrentFragType() != FragType.MemberListFragment && getCurrentFragType() != FragType.OfflineMemberListFragment)
                return
        }

        if (!isFromAlarm)
            AppUtils.hideSoftKeyboard(this)

        val mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Timber.d("token : " + token.toString())
        })

        //begin Suman 21-09-2023 mantis id 0026837
        try {
            val packageName = "com.google.android.apps.maps"
            val appInfo: ApplicationInfo =
                this.getPackageManager().getApplicationInfo(packageName, 0)
            var appstatus = appInfo.enabled

            if (!appstatus && !mFragType.equals(FragType.DashboardFragment)) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok)

                try {
                    simpleDialog.setCancelable(true)
                    simpleDialog.setCanceledOnTouchOutside(false)
                    val dialogName =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                    val dialogCross =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                    dialogName.text = AppUtils.hiFirstNameText()
                    dialogCross.setOnClickListener {
                        simpleDialog.cancel()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                dialogHeader.text =
                    "Location will be inappropriate as Google map is disabled. Please go to settings of your phone and Enable Google Map. Thank you."
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                })
                simpleDialog.show()
                return
            } else {
                println("load_frag " + " gmap app enable")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        //end Suman 21-09-2023 mantis id 0026837
        if (mFragType.equals(FragType.DashboardFragment) && !mFragType.equals(FragType.LogoutSyncFragment)) {
            Pref.IsAnyPageVisitFromDshboard = false
            println("dasg_tag if")
        } else {
            Pref.IsAnyPageVisitFromDshboard = true
            println("dasg_tag else")
        }

        if (mFragType.equals(FragType.MyLearningFragment) && !mFragType.equals(FragType.LogoutSyncFragment)) {
            Pref.IsAnyPageVisitFromDshboard = false
            println("dasg_tag if")
        } else {
            Pref.IsAnyPageVisitFromDshboard = true
            println("dasg_tag else")
        }

        Pref.MultiVisitIntervalInMinutes = "1"
        Pref.IsShowMenuAnyDesk = false
        //Pref.IsShowCRMOpportunity = true
        //Pref.IsEditEnableforOpportunity = true
        //Pref.IsDeleteEnableforOpportunity = true
        //Pref.IsUsbDebuggingRestricted = false
        //getFCMtoken()
        showToolbar()
        println("load_frag " + mFragType.toString() + " " + Pref.user_id.toString() + " " + Pref.QuestionAfterNoOfContentForLMS)

        //  val notification = NotificationUtils(getString(R.string.app_name), "", "", "")

        //  notification.sendFCMNotificaitonTest(applicationContext,"Hii who are you?")
        //gallaboxApiTest()
        // trackLMSModuleLoad(mFragType)

        batteryCheck(mFragType, addToStack, initializeObject)


        /*if (addToStack) {
 mTransaction.add(R.id.frame_layout_container, getFragInstance(mFragType, initializeObject, true)!!, mFragType.toString())
 mTransaction.addToBackStack(mFragType.toString()).commitAllowingStateLoss()
 } else {
 mTransaction.replace(R.id.frame_layout_container, getFragInstance(mFragType, initializeObject, true)!!, mFragType.toString())
 mTransaction.commitAllowingStateLoss()
 }*/

    }

    fun getFCMtoken() {
        try {
            doAsync {
                var firebaseUrl = "https://www.googleapis.com/auth/firebase.messaging"
                var jsonString = "{\n" +
                        "  \"type\": \"service_account\",\n" +
                        "  \"project_id\": \"demofsm-fee63\",\n" +
                        "  \"private_key_id\": \"fa4e0aa591d3ba0a173a6f3408401efdad118bdb\",\n" +
                        "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCpS0IokL4jjiry\\ngB6fGcIvmZkZds8Rjs2tPrF353Wd4QyeyqycG+Jj7hU4Nd7lgH0GiiF+0QY+7nrv\\nSCQjdSHj+mJUhulL3vA81lypoSzTyvewTjXN5/yj6U3W0MsVlt1rHqckIRvKIdjH\\nfR50X3BB2m4200i3TAhSvSqWBAHp6ySVTRWfYxM70ugVDZdJsr+BW1cZnn5+z93z\\nWMa4RSqbj24ZQOUNEAtdxu8uJ5M1HnlCqa5WeKXpHuO1oUtwlB/EpELqBG6VWM8E\\nXityD+HTJF+Bch4wJhP18Kj40o1bZv4ZDoesXnpZ1NmamQprOv07QBM6U7Ni5XC1\\nVgjMuc9xAgMBAAECggEAHxYEpfI+F8VJOZIxDUHrmFX5+OUKDM1OExvJ9px3ym/C\\no33PyDKOlY7oMpQhw76eNo8yq1iybufXhwyWJjSh7nzRhXfoatgbAPDTvworcxB3\\n/tW9p3uLtoVml6VrRSGYsszEICw8MBea+LaO2wuTT2ROjJ6rYY0Ckj7ODRHbUBpi\\n16wHZzXk0xHvnU+WpiB8O2o0kyecHDASa2tlPDnBWZAjwvrd6YR2D9/3jq11qWK/\\nVlHliNi+O563ih9VueHHXKidlsJXfFl9lzgRKElqov6BX4fkmuGjYrea/LraQQAc\\nK3qGvdbfttuHUvJOIsZaAhu4VM+gnMB0R6yj+pwu6QKBgQDd0FE3ZktMZDCSf8vb\\nB9xn5vC5ga8X6Nu6/L1K5wENfid1ShQE3n4Ni3GXTlXalWvmLi0gpe9EZNUe7avr\\nF5kEXwNVc5Q4P438MfeAAmUU6kzR1T1Y76JX4xRi2idjoJqfm2SPRGNB6wtoS7Gu\\ngCJ3Urd9dH3/hL0D+e01+MHxywKBgQDDYsTVQxBJsZTq75lhmPKVzsgMDozAfXF2\\n6uk+eVg/QFjsyZagWsSLlACwRXuZwoi8f0spGEIX3Jf8f6dvYg3CIDyDc3Yp2rpm\\n6OpMosugWuJEaFFxlwFz6Gu+5q5rCoWh72reqHJm0FGWGbqevD39U1R/zsU0/LnT\\nTtrP3Z9sMwKBgGagZZNOPvR/PoHpovYaMv3XufT6bXqQgGmJWkN3keMeRT9dINoH\\n3yaBJ/MriUly7NM49iQu4f8w7/I5YNuKtX9yPmag7SkBLr5KmAqgEQiWRyimkpW9\\nec1UATCjYqoTurax/NrUd2AeUc7VhsYH/upaWQ8wgMNiNNnMHtZj28f1AoGACNbu\\nGsvm776WAy8F3HGEAB0T1d/OpGLIgF3OYaIxyOLLYyMXqneQztPKWC88kU9IymZj\\n6x8K1nOHeMf5tkNUZgT5V+UgYnJf3ooJF6CB3+ZcuEWT8bSoPyszvLZJC9S1CQeA\\n6UPrsRUZq9XMKKRRlaVwfDvJlkUczx+RLLhVHxsCgYBwJ0d9poRg3nfGb+OJf2mo\\nfjsjo+2n1e9/ppQK79emITvqRCuqsyXLR+oyiTZIhck4Pz9D898bQnzoMWTj3UUo\\ni5mqby/aZih1ZBYAA63SeuKnh7FAdrXAmGoj/m0D1AELtiiWNN7H5d98kn5md2b/\\nE5awhBdjJd0NoUnhodWslA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                        "  \"client_email\": \"firebase-adminsdk-m1emn@demofsm-fee63.iam.gserviceaccount.com\",\n" +
                        "  \"client_id\": \"115535921552187565842\",\n" +
                        "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                        "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                        "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                        "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-m1emn%40demofsm-fee63.iam.gserviceaccount.com\",\n" +
                        "  \"universe_domain\": \"googleapis.com\"\n" +
                        "}\n"

                val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
                val googleCreds =
                    GoogleCredentials.fromStream(stream).createScoped(arrayListOf(firebaseUrl))
                googleCreds.refresh()

                uiThread {
                    sendMessageToFCMV1("e25zTRKXS7WvxnYkdn5rVf:APA91bHj8vgVcGZcPDF5YjPkigSkufM1iZ2p65-wkRM5wcHWBWFzcW1Eq5Ze_2ioMkLIvk21h3sS51_RUfnf_GGWJXF0n9zs3aCrpwOOeQ6NbBn9Ml0TW-cV8RhbWxuRBlBPQ5x85bvb",googleCreds.accessToken.tokenValue)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessageToFCMV1(token: String, accessToken: String) {
        doAsync {
            val fcmUrl = "https://fcm.googleapis.com/v1/projects/demofsm-fee63/messages:send"

            try {
                // Create the message JSON object
                val message = JSONObject()
                val messageContent = JSONObject()
                messageContent.put("token", token)

                val notification = JSONObject()
                notification.put("title", "Hello!")
                notification.put("body", "This is a message from the FCM API V1.")
                messageContent.put("notification", notification)

                //val jsonObject = JSONObject()
                val notificationBody = JSONObject()
                notificationBody.put("body","Leave applied by : "+Pref.user_name!!)
                notificationBody.put("flag", "flag")
                notificationBody.put("applied_user_id",Pref.user_id)
                notificationBody.put("leave_from_date","2024-09-11")
                notificationBody.put("leave_to_date","2024-09-11")
                notificationBody.put("leave_reason","test")
                notificationBody.put("leave_type","1")
                notificationBody.put("leave_type_id","1")
                messageContent.put("data", notificationBody)

                message.put("message", messageContent)

                // Set up the connection
                val url = URL(fcmUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Authorization", "Bearer $accessToken")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                // Write the message to the output stream
                val outputStream: OutputStream = conn.outputStream
                outputStream.write(message.toString().toByteArray())
                outputStream.flush()
                outputStream.close()

                // Read the response
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    println("Message sent successfully")
                } else {
                    println("Error sending message: $responseCode")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            uiThread {

            }
        }



    }

    //test code
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    fun callAlarm() {
        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmServiceRestart::class.java).let { intent ->
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val calendar: Calendar = Calendar.getInstance()
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 1,
            alarmIntent
        )

    }


    fun gallaboxApiTest() {
        val jsonObject = JSONObject()
        val notificationBody = JSONObject()
        notificationBody.put("channelId", "664b0eba596b4d9106362ddb")
        notificationBody.put("channelType", "whatsapp")

        var notificationBody_recipient = JSONObject()
        notificationBody_recipient.put("name", "Suman")
        notificationBody_recipient.put("phone", "918017845376")

        var notificationBody_context = JSONObject()
        notificationBody_context.put("type", "notification")

        var notificationBody_whatsapp = JSONObject()
        notificationBody_whatsapp.put("type", "template")
        var notificationBody_whatsapp_template = JSONObject()
        var notificationBody_whatsapp_template_body = JSONObject()
        notificationBody_whatsapp_template_body.put("Name", "Bajrang")
        notificationBody_whatsapp_template.put("templateName", "independence_day_celeb")
        notificationBody_whatsapp_template.put(
            "bodyValues",
            notificationBody_whatsapp_template_body
        )
        notificationBody_whatsapp.put("template", notificationBody_whatsapp_template)

        notificationBody.put("recipient", notificationBody_recipient)
        notificationBody.put("context", notificationBody_recipient)
        notificationBody.put("recipient", notificationBody_recipient)

        notificationBody.put("recipient", notificationBody_recipient)
        notificationBody.put("context", notificationBody_context)
        notificationBody.put("whatsapp", notificationBody_whatsapp)

        var jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            "https://server.gallabox.com/devapi/messages/whatsapp",
            notificationBody,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {
                    var suc = "as"
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    var e = error.localizedMessage
                    Toaster.msgShort(this@DashboardActivity, e.toString())
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["apiSecret"] = "71400c1d1e384da38ef5cd6852ce07bb"
                params["Content-Type"] = "application/json"
                params["apiKey"] = "664b23b402fc9498c685699d"
                return params
            }
        }
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        MySingleton.getInstance(mContext.applicationContext)!!.addToRequestQueue(jsonObjectRequest)


    }

    var contactDtls: ArrayList<ContactDtls> = ArrayList()
    private fun getPhoneBookGroups(): ArrayList<ContactGr> {
        val groups: ArrayList<ContactGr> = ArrayList()

        val projection = arrayOf(ContactsContract.Groups._ID, ContactsContract.Groups.TITLE)
        val cursor =
            contentResolver.query(ContactsContract.Groups.CONTENT_URI, projection, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val groupName = it.getString(it.getColumnIndex(ContactsContract.Groups.TITLE))
                val groupId = it.getString(it.getColumnIndex(ContactsContract.Groups._ID))
                if (!groups.map { it.gr_name }.contains(groupName)) {
                    groups.add(ContactGr(groupId, groupName))
                    println("tag_contact $groupId $groupName")
                }

            }
        }
        return groups
    }

    /*fun getContactsOfGroup(group: Group): Cursor? {
 // getting ids of contacts that are in this specific group
 val where = ((ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "="
 + group.id) + " AND "
 + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
 + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'")
 val query = context.contentResolver.query(
 ContactsContract.Data.CONTENT_URI, arrayOf(
 ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
 ), where, null, ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
 )
 var ids = ""
 query!!.moveToFirst()
 while (!query.isAfterLast) {
 ids += "," + query.getString(0)
 query.moveToNext()
 }
 if (ids.length > 0) {
 ids = ids.substring(1)
 }

 // getting all of information of contacts. it fetches all of number from every one
 val projection = arrayOf(
 "_id",
 "contact_id",
 "lookup",
 "display_name",
 "data1",
 "photo_id",
 "data2"
 )
 val selection =
 (("mimetype ='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'"
 + " AND account_name='" + group.accountName) + "' AND account_type='" + group.accountType + "'"
 + " AND contact_id in (" + ids + ")")
 return context.contentResolver.query(BASE_URI, projection, selection, null, null)
 }*/

    private fun initPermissionCheckOne() {
        var permissionList = arrayOf<String>(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )
        permissionUtils = PermissionUtils(this, object : PermissionUtils.OnPermissionListener {
            @TargetApi(Build.VERSION_CODES.M)
            override fun onPermissionGranted() {

                //Toaster.msgShort(this@DashboardActivity,"PG")
                //var t1 = obtenerDetallesLlamadas(this@DashboardActivity)
                //var t2 = getNamePhoneDetails()
                var t4 = "456"
            }

            override fun onPermissionNotGranted() {

            }
        }, permissionList)
    }

    @SuppressLint("Range")


    data class Contact(val id: String = "", val name: String = "", val number: String = "")

    public fun getNamePhoneDetails(): ArrayList<Contact>? {
        val names: ArrayList<Contact> = ArrayList()
        val cr = contentResolver
        val cur = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null
        )
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                val name =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val grr =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1))
                names.add(Contact(id, name, number))
            }
        }
        return names
    }

    fun getContactsForGroup(groupID: String, activity: Activity): HashMap<String, String>? {
        val dataCursor = activity.contentResolver.query(
            ContactsContract.Data.CONTENT_URI, arrayOf( // PROJECTION
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME, // contact name
                ContactsContract.Data.DATA1 // group
            ),
            ContactsContract.Data.MIMETYPE + " = ? " + "AND " + // SELECTION
                    ContactsContract.Data.DATA1 + " = ? ", arrayOf( // SELECTION_ARGS
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                groupID
            ),
            null
        )
        dataCursor!!.moveToFirst()
        val map = HashMap<String, String>()
        while (dataCursor.moveToNext()) //
        {
            val s0 = dataCursor.getString(0) //contact_id
            val s1 = dataCursor.getString(1) //contact_name
            val s2 = dataCursor.getString(2) //group_id
            Log.d("tag", "contact_id: $s0 contact: $s1 groupID: $s2")
            map[s0] = s1
        }
        return map
    }

    fun getContactsForGroup1(grId: String, grName: String): ArrayList<ContactDtls> {
        val groupId: String = grId
        val cProjection = arrayOf<String>(
            ContactsContract.Contacts.DISPLAY_NAME,
            CommonDataKinds.GroupMembership.CONTACT_ID
        )

        val groupCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            cProjection,
            CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                    + CommonDataKinds.GroupMembership.MIMETYPE + "='"
                    + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
            arrayOf<String>(groupId.toString()),
            null
        )
        if (groupCursor != null && groupCursor.moveToFirst()) {
            do {
                val nameCoumnIndex = groupCursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME)
                val name = groupCursor.getString(nameCoumnIndex)
                val contactId =
                    groupCursor.getLong(groupCursor.getColumnIndex(CommonDataKinds.GroupMembership.CONTACT_ID))
                val numberCursor = contentResolver.query(
                    CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf<String>(CommonDataKinds.Phone.NUMBER),
                    CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null,
                    null
                )
                if (numberCursor!!.moveToFirst()) {
                    val numberColumnIndex =
                        numberCursor!!.getColumnIndex(CommonDataKinds.Phone.NUMBER)
                    do {
                        val phoneNumber = numberCursor!!.getString(numberColumnIndex)
                        Log.d("your tag", "contact $name:$phoneNumber")
                        println("tag_contact for grId ${groupId} contact $name:$phoneNumber")
                        var ph = phoneNumber.toString().replace(" ", "")
                        if (!contactDtls.map { it.number }.contains(ph)) {
                            contactDtls.add(ContactDtls(grName, name, ph))
                        }
                    } while (numberCursor!!.moveToNext())
                    numberCursor!!.close()
                }
            } while (groupCursor.moveToNext())
            groupCursor.close()
        }
        return contactDtls
    }

    data class PhoneCallDtls(
        var number: String? = "",
        var type: String? = "",
        var callDate: String? = "",
        var callDateTime: String? = "",
        var callDuration: String? = ""
    )

    fun obtenerDetallesLlamadas(context: Context): ArrayList<PhoneCallDtls>? {
        //public static String obtenerDetallesLlamadas(Context context) {
        try {
            val stringBuffer = StringBuffer()
            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC"
            )
            val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)

            val phoneCallRecord = ArrayList<PhoneCallDtls>()

            while (cursor.moveToNext()) {
                val phNumber = cursor.getString(number)
                val callType = cursor.getString(type)
                val callDate = cursor.getString(date)
                val callDayTime = Date(java.lang.Long.valueOf(callDate))
                var callDateTime = AppUtils.getDateTimeFromTimeStamp(callDate.toLong())
                val callDuration = cursor.getString(duration)
                var dir: String? = null
                val dircode = callType.toInt()
                when (dircode) {
                    CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                }
                stringBuffer.append(
                    "\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                            + dir + " \nCall Date:--- " + callDayTime
                            + " \nCall duration in sec :--- " + callDuration
                )
                stringBuffer.append("\n----------------------------------")

                try {
                    val obj = PhoneCallDtls()
                    obj.number = phNumber
                    obj.type = dir
                    obj.callDate = callDate
                    obj.callDateTime = callDateTime
                    obj.callDuration = callDuration
                    phoneCallRecord.add(obj)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
            cursor.close()
            return phoneCallRecord
            //return stringBuffer.toString();
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return null
    }


    fun batteryCheck(mFragType: FragType, addToStack: Boolean, initializeObject: Any) {
        val mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        try {
            val pm = mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
            var sett = pm.isIgnoringBatteryOptimizations(packageName)
            if (!mFragType.equals(FragType.DashboardFragment) && sett == false && !mFragType.equals(
                    FragType.LogoutSyncFragment
                )
            ) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok_logout)
                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                var msgHead = "App battery Saver is turned on. Please follow the below settings."
                if (AppUtils.getDeviceName()
                        .contains("MI", ignoreCase = true) || AppUtils.getDeviceName()
                        .contains("Redmi", ignoreCase = true) ||
                    AppUtils.getDeviceName().contains("Poco", ignoreCase = true)
                ) {
                    dialogHeader.text =
                        msgHead + " " + "Go to Settings -> Apps -> Manage Apps -> FSM App -> Battery Saver -> No restrictions."
                } else if (AppUtils.getDeviceName().contains("Vivo", ignoreCase = true)) {
                    dialogHeader.text =
                        msgHead + " " + "Go to Settings -> Battery -> Background Battery Power Consumption Ranking / High Background Power Consumption -> FSM App -> Don't Restrict."
                } else {
                    dialogHeader.text =
                        msgHead + " " + "Go to Settings -> Apps -> FSM App -> Battery -> Unrestricted."
                }
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    //simpleDialog.cancel()
                    //callLogout()

                    /*val intent = Intent()
 val packageName = packageName
 intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
 val uri = Uri.fromParts("package", packageName, null)
 intent.data = uri
 mContext.startActivity(intent)*/
                })
                simpleDialog.show()
                Handler().postDelayed(Runnable {
                    val timer = object : CountDownTimer(6 * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            dialogYes.text =
                                "Logout in " + (millisUntilFinished / 1000).toString() + " seconds..."
                        }

                        override fun onFinish() {
                            simpleDialog.cancel()
                            callLogout()
                            //Toaster.msgShort(this@DashboardActivity,"Logout call")
                        }
                    }.start()
                }, 1000)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (mFragType == FragType.MyLearningFragment || mFragType == FragType.SearchLmsFrag || mFragType == FragType.VideoPlayLMS || mFragType == FragType.LeaderboardLmsFrag || mFragType == FragType.SearchLmsLearningFrag || mFragType == FragType.SearchLmsKnowledgeFrag || mFragType == FragType.MyLearningAllVideoList || mFragType == FragType.KnowledgeHubAllVideoList || mFragType == FragType.MyPerformanceFrag || mFragType == FragType.NotificationLMSFragment || mFragType == FragType.LmsQuestionAnswerSet || mFragType == FragType.MyLearningVideoPlay || mFragType == FragType.MyLearningTopicList || mFragType == FragType.BookmarkFrag || mFragType == FragType.BookmarkPlayFrag || mFragType == FragType.PerformanceInsightPage || mFragType == FragType.MyTopicsWiseContents || mFragType == FragType.AllTopicsWiseContents) {
                    window.setStatusBarColor(resources.getColor(R.color.toolbar_lms))
                    toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_lms))
                } else {
                    window.setStatusBarColor(resources.getColor(R.color.colorPrimary))
                    toolbar.setBackgroundResource(R.drawable.custom_toolbar_back)
                }
                if (addToStack) {
                    mTransaction.add(
                        R.id.frame_layout_container,
                        getFragInstance(mFragType, initializeObject, true)!!,
                        mFragType.toString()
                    )
                    mTransaction.addToBackStack(mFragType.toString()).commitAllowingStateLoss()
                } else {
                    mTransaction.replace(
                        R.id.frame_layout_container,
                        getFragInstance(mFragType, initializeObject, true)!!,
                        mFragType.toString()
                    )
                    mTransaction.commitAllowingStateLoss()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (addToStack) {
                mTransaction.add(
                    R.id.frame_layout_container,
                    getFragInstance(mFragType, initializeObject, true)!!,
                    mFragType.toString()
                )
                mTransaction.addToBackStack(mFragType.toString()).commitAllowingStateLoss()
            } else {
                mTransaction.replace(
                    R.id.frame_layout_container,
                    getFragInstance(mFragType, initializeObject, true)!!,
                    mFragType.toString()
                )
                mTransaction.commitAllowingStateLoss()
            }
        }
    }

    fun callLogout() {
        Pref.IsAutoLogoutFromBatteryCheck = true
        if (AppUtils.isOnline(this)) {
            Timber.d("Battery optimization in online mode.")
            (mContext as DashboardActivity).loadFragment(FragType.LogoutSyncFragment, true, "")
        } else {
            Timber.d("Battery optimization in offline mode.")
            try {
                var soundUriAlarm =
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.beethoven)
                if (soundUriAlarm == null) {
                    soundUriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                }
                var player: MediaPlayer = MediaPlayer.create(this, soundUriAlarm)
                player.isLooping = true
                player.start()
                var vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val pattern = longArrayOf(0, 5, 10, 20, 40, 80, 120, 100, 600, 700, 500, 500, 500)
                vibrator.vibrate(pattern, 1)


                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok)

                try {
                    simpleDialog.setCancelable(true)
                    simpleDialog.setCanceledOnTouchOutside(false)
                    val dialogName =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                    val dialogCross =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                    dialogName.text = AppUtils.hiFirstNameText()
                    dialogCross.setOnClickListener {
                        simpleDialog.cancel()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                dialogHeader.text =
                    "Device is in offline mode. Internet connection is required for auto logout."
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                    player.stop()
                    vibrator.cancel()
                })
                simpleDialog.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }


        /*val unSyncedList = AppDatabase.getDBInstance()!!.userLocationDataDao().getLocationUpdateForADayNotSyn(AppUtils.convertFromRightToReverseFormat(Pref.login_date!!), false)
 var distance = 0.0
 if (unSyncedList != null && unSyncedList.isNotEmpty()) {
 var totalDistance = 0.0
 for (i in unSyncedList.indices) {
 totalDistance += unSyncedList[i].distance.toDouble()
 }
 distance = Pref.tempDistance.toDouble() + totalDistance
 }
 else{
 distance = Pref.tempDistance.toDouble()
 }
 var location = ""
 if (Pref.logout_latitude != "0.0" && Pref.logout_longitude != "0.0") {
 location = LocationWizard.getAdressFromLatlng(this, Pref.logout_latitude.toDouble(), Pref.logout_longitude.toDouble())
 if (location.contains("http"))
 location = "Unknown"
 }

 val repository = LogoutRepositoryProvider.provideLogoutRepository()
 BaseActivity.compositeDisposable.add(
 repository.logout(Pref.user_id!!, Pref.session_token!!, Pref.logout_latitude, Pref.logout_longitude,AppUtils.getCurrentDateTime(),
 distance.toString(), "1", location)
 .observeOn(AndroidSchedulers.mainThread())
 .subscribeOn(Schedulers.io())
 .subscribe({ result ->
 val logoutResponse = result as BaseResponse
 Timber.d("AUTO_LOGOUT : " + "RESPONSE : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
 if (logoutResponse.status == NetworkConstant.SUCCESS) {
 Pref.tempDistance = "0.0"
 if (unSyncedList != null && unSyncedList.isNotEmpty()) {
 for (i in unSyncedList.indices) {
 AppDatabase.getDBInstance()!!.userLocationDataDao().updateIsUploaded(true, unSyncedList[i].locationId)
 }
 }
 val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
 notificationManager.cancelAll()
 Pref.logout_latitude = "0.0"
 Pref.logout_longitude = "0.0"
 clearData()
 Pref.isAutoLogout = false
 Pref.isAddAttendence = false
 } else
 performLogout()
 BaseActivity.isApiInitiated = false
 takeActionOnGeofence()
 }, { error ->
 Toaster.msgShort(this, getString(R.string.something_went_wrong))
 Timber.d("AUTO_LOGOUT : " + "RESPONSE ERROR: " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
 error.printStackTrace()
 performLogout()
 })
 )*/
    }


    private fun checkByU(token: String) {
        var fbToken =
            "fRL4OYJgTNCLFcMPKOcPhH:APA91bHZHlJf56uD_TqnD-Pq0Rvl0ao9x1ZZhtZvu2MpbAJ5FJD_1TrrTnRhfx0ABzfj2WKaX_ji8mjx1W_eawbZs5KUOD8OM0GpTK2m8IV9863_jIvNaFVknSlnAH1T5I3X4iJBERCF"
        if (fbToken != "") {
            try {
                val jsonObject = JSONObject()
                val notificationBody = JSONObject()
                notificationBody.put("title", "titleeeeee")
                notificationBody.put("body", "bodyyyyy")
                notificationBody.put("flag_status", "flag_status")
                notificationBody.put("applied_user_id", Pref.user_id)
                notificationBody.put("act", "1")
                jsonObject.put("data", notificationBody)
                val jsonArray = JSONArray()
                jsonArray.put(0, fbToken)
                jsonObject.put("registration_ids", jsonArray)
                sendCustomNotification(jsonObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun check(token: String) {
        var fbToken =
            "einJt0q5QnmA2YH09qrhD2:APA91bHV8S0T937dZcLqh0tpDTukvZea6Ue3rVh4hl59jDDCom2xKoCSgbmK6JTjTi5jw3iZykqhpwiX0nQcI8f-ng0xdFZZZ9LIagSWBdTzjqig8U4zZwgnJWYRPZ99xBWsBmECPkH-"
        if (fbToken != "") {
            try {
                val jsonObject = JSONObject()
                val notificationBody = JSONObject()
                notificationBody.put("title", "titleeeeee")
                notificationBody.put("body", "bodyyyyy")
                notificationBody.put("flag", "flag")
                notificationBody.put("applied_user_id", Pref.user_id)
                notificationBody.put("act", "1")
                jsonObject.put("data", notificationBody)
                val jsonArray = JSONArray()
                jsonArray.put(0, fbToken)
                jsonObject.put("registration_ids", jsonArray)
                sendCustomNotification(jsonObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendCustomNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notification,
                object : Response.Listener<JSONObject?> {
                    override fun onResponse(response: JSONObject?) {

                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {

                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        Pref.firebase_k.toString()//getString(R.string.firebase_key)
                    params["Content-Type"] = "application/json"
                    return params
                }
            }

        MySingleton.getInstance(applicationContext)!!.addToRequestQueue(jsonObjectRequest)
    }


    private lateinit var mReceiver: BroadcastReceiver
    private lateinit var mReceiverAddshop: BroadcastReceiver
    private lateinit var mReceiverNearbyshop: BroadcastReceiver
    private lateinit var mReceiverAutoLogout: BroadcastReceiver
    private lateinit var mContext: Context

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var include_toolbar: View
    lateinit var searchView: MaterialSearchView
    private lateinit var bottomBar: View

    private lateinit var home_IV: ImageView
    private lateinit var add_shop_IV: ImageView
    private lateinit var nearby_shops_IV: ImageView
    private lateinit var my_orders_IV: ImageView
    private lateinit var iv_search_icon: ImageView
    lateinit var iv_sync_icon: ImageView

    public lateinit var headerTV: AppCustomTextView
    private lateinit var home_TV: AppCustomTextView
    private lateinit var add_shop_TV: AppCustomTextView
    private lateinit var nearby_shops_TV: AppCustomTextView
    private lateinit var my_orders_TV: AppCustomTextView
    private lateinit var maps_TV: AppCustomTextView
    private lateinit var tickTV: ImageView
    private lateinit var ta_tv: AppCustomTextView
    private lateinit var view_pp_dd_tv: AppCustomTextView

    private lateinit var home_RL: RelativeLayout
    private lateinit var add_shop_RL: RelativeLayout
    private lateinit var nearby_shops_RL: RelativeLayout
    private lateinit var my_orders_RL: RelativeLayout
    private lateinit var alert_snack_bar: CoordinatorLayout

    private var imageArrayList: MutableList<View> = ArrayList()
    private var textArrayList: MutableList<View> = ArrayList()


    private lateinit var addOrderTV: AppCustomTextView
    private lateinit var orderHistoryTV: AppCustomTextView
    private lateinit var addTravelAllowenceTV: AppCustomTextView
    private lateinit var settingsTV: AppCustomTextView
    private lateinit var returnTV: AppCustomTextView
    private lateinit var myAllowRequest: AppCustomTextView
    private lateinit var logoutTV: AppCustomTextView
    lateinit var logo: AppCompatImageView
    lateinit var tv_noti_count: AppCustomTextView
    private lateinit var iv_home_icon: ImageView
    private lateinit var iv_leaderboard: ImageView
    private lateinit var add_scheduler_email_verification: ImageView
    private lateinit var add_bookmark: ImageView
    private lateinit var tv_saved_count: AppCustomTextView
    private lateinit var add_template: ImageView
    private lateinit var nearbyShops: AppCustomTextView
    private lateinit var contacts_TV: AppCustomTextView
    private lateinit var menuBeatTV: AppCustomTextView// 5.0 DashboardActivity AppV 4.0.6 MenuBeatFrag
    private lateinit var marketAssistTV: AppCustomTextView
    private lateinit var tv_pending_out_loc_menu: AppCustomTextView
    private lateinit var assignedLead: AppCustomTextView
    private lateinit var taskManagement: AppCustomTextView // Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023
    private lateinit var surveyMenu: AppCustomTextView
    private lateinit var shareLogs: AppCustomTextView
    private lateinit var reimbursement_tv: AppCustomTextView
    private lateinit var achievement_tv: AppCustomTextView
    private lateinit var iv_shopImage: ImageView

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var menuMis: AppCustomTextView
    private lateinit var login_time_tv: AppCustomTextView
    private lateinit var login_time_am_tv: AppCustomTextView
    private lateinit var profile_name_TV: AppCustomTextView

    private lateinit var progress_wheel: com.pnikosis.materialishprogress.ProgressWheel
    private lateinit var version_name_TV: AppCustomTextView
    private lateinit var add_attendence_tv: AppCustomTextView
    private lateinit var profilePicture: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var profilePicture_adv: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var iv_filter_icon: ImageView
    private lateinit var rl_confirm_btn: RelativeLayout
    private lateinit var tv_pp_dd_outstanding: AppCustomTextView
    private lateinit var tv_location: AppCustomTextView
    private lateinit var collection_TV: AppCustomTextView
    private lateinit var state_report_TV: AppCustomTextView
    private lateinit var target_TV: AppCustomTextView
    private lateinit var iv_list_party: ImageView
    private lateinit var rl_report: RelativeLayout
    private lateinit var iv_drop_down_icon: ImageView
    private lateinit var ll_report_list: LinearLayout
    private lateinit var tv_attendance_report: AppCustomTextView
    private lateinit var tv_performance_report: AppCustomTextView
    private lateinit var tv_visit_report: AppCustomTextView
    private lateinit var meeting_TV: AppCustomTextView
    private lateinit var team_TV: AppCustomTextView
    private lateinit var iv_map: AppCompatImageView
    private lateinit var timesheet_TV: AppCustomTextView
    private lateinit var tv_change_pwd: AppCustomTextView
    private lateinit var all_team_TV: AppCustomTextView
    lateinit var update_worktype_tv: AppCustomTextView
    private lateinit var achv_TV: AppCustomTextView
    private lateinit var targ_achv_TV: AppCustomTextView
    private lateinit var leave_tv: AppCustomTextView
    private lateinit var task_TV: AppCustomTextView
    private lateinit var dynamic_TV: AppCustomTextView
    private lateinit var activity_TV: AppCustomTextView
    private lateinit var rl_collection: RelativeLayout
    private lateinit var iv_collection_drop_down_icon: AppCompatImageView
    private lateinit var ll_collection_list: LinearLayout
    private lateinit var tv_report: AppCustomTextView
    private lateinit var tv_entry: AppCustomTextView
    private lateinit var share_loc_TV: AppCustomTextView
    private lateinit var iv_settings: AppCompatImageView
    private lateinit var weather_TV: AppCustomTextView
    private lateinit var doc_TV: AppCustomTextView
    private lateinit var chat_bot_TV: AppCustomTextView
    private lateinit var distributor_wise_order_list_TV: AppCustomTextView
    private lateinit var ic_calendar: AppCompatImageView
    private lateinit var ic_chat_bot: AppCompatImageView
    private lateinit var iv_cancel_chat: AppCompatImageView
    private lateinit var chat_TV: AppCustomTextView
    private lateinit var iv_people: AppCompatImageView
    private lateinit var tv_confirm_btn: AppCustomTextView
    private lateinit var iv_scan: AppCompatImageView
    private lateinit var iv_view_text: AppCompatImageView
    private lateinit var scan_TV: AppCustomTextView
    private lateinit var nearby_user_TV: AppCustomTextView
    private lateinit var fl_net_status: FrameLayout
    private lateinit var home_loc_TV: AppCustomTextView
    private lateinit var device_info_TV: AppCustomTextView
    private lateinit var permission_info_TV: AppCustomTextView
    private lateinit var anydesk_info_TV: AppCustomTextView

    //private lateinit var screen_record_info_TV: AppCustomTextView
    private lateinit var check_custom_status_TV: AppCustomTextView
    private lateinit var micro_learning_TV: AppCustomTextView
    private lateinit var my_learning_TV: AppCustomTextView

    private lateinit var photo_registration: AppCustomTextView
    private lateinit var photo_team_attendance: AppCustomTextView
    private lateinit var tb_auto_revisit_menu: AppCustomTextView// 1.0 AppV 4.0.6
    private lateinit var tv_clear_attendance: AppCustomTextView
    private lateinit var tb_total_visit_menu: AppCustomTextView// 3.0 AppV 4.0.6 DashboardActivity
    private lateinit var leaderBoard_TV: AppCustomTextView

    private lateinit var privacy_policy_tv_menu: AppCustomTextView// 14.0 DashboardActivity AppV 4.0.8 mantis 0025783 In-app privacy policy working in menu & Login

    private lateinit var alarmCofifDataModel: AlarmConfigDataModel
    private lateinit var quo_TV: AppCustomTextView

    private var mShopId: String = ""
    private var backpressed: Long = 0
    public val SELECT_CAMERA = 1
    private lateinit var tv_performance: AppCustomTextView


    private var mAddShopDBModelEntity: AddShopDBModelEntity? = null
    private var mStoreName: String = ""
    private var mCurrentPhotoPath: String = ""
    private var filePath: String = ""

    var qtyList = ArrayList<String>()
    var rateList = ArrayList<String>()

    /*06-01-2022*/
    var mrpList = ArrayList<String>()

    /*28-12-2021*/
    var schemaqtyList = ArrayList<String>()
    var schemarateList = ArrayList<String>()

    var totalScPrice = ArrayList<Double>()

    var totalPrice = ArrayList<Double>()
    private var filter: IntentFilter? = null
    var isGpsDisabled = false
    private var i = 0
    private lateinit var iv_delete_icon: ImageView
    lateinit var rl_cart: RelativeLayout
    lateinit var tv_cart_count: AppCustomTextView
    private var isAddAttendaceAlert = false

    /*Interface to update Shoplist Frag on search event*/
    private var searchListener: SearchListener? = null

    var shop_type = ""

    private var isAttendanceAlertPresent = false

    var reimbursementSelectPosition = 0

    var shop: AddShopDBModelEntity? = null
    var isConfirmed = false
    var isTodaysPerformance = false

    lateinit var teamHierarchy: ArrayList<String>
    private var idealLocAlertDialog: CommonDialogSingleBtn? = null
    private var attendNotiAlertDialog: CommonDialogSingleBtn? = null
    private var forceLogoutDialog: CommonDialogSingleBtn? = null

    public fun setSearchListener(searchListener: SearchListener) {
        this.searchListener = searchListener
    }


    /*********************Geofence*****************/
    private enum class PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private lateinit var mGeofencingClient: GeofencingClient
    private var mGeofenceList: ArrayList<Geofence> = arrayListOf()
    private var mGeofencePendingIntent: PendingIntent? = null
    private var mPendingGeofenceTask = PendingGeofenceTask.NONE
    private var gpsReceiver: GpsLocationReceiver? = null
    private var mGpsStatusDetector: GpsStatusDetector? = null
    private var geoFenceBroadcast: GeofenceBroadcastReceiver? = null
    private var isFromAlarm: Boolean = false
    private var pushStatus = -1
    private var orderCollectionAlertDialog: CommonDialogSingleBtn? = null
    private var isOrderAdded = false
    private var isCollectionAdded = false
    private var isOrderDialogShow = false
    var isDailyPlanFromAlarm = false
    var isAttendanceFromAlarm = false
    var isPerformanceFromAlarm = false
    var isVisitFromAlarm = false
    private var isOtherUsersShopRevisit = false
    var isChangedPassword = false
    var isChatFromDrawer = false
    var isRefreshChatUserList = false
    var newUserModel: GroupUserDataModel? = null
    var isCodeScaneed = false

    /*********************Geofence*****************/
// private lateinit var geofenceService:Intent


    private var isTermsAndConditionsPopShow = false
    private var termsConditionsDialog: TermsAndConditionsSingleBtnDialog? = null
    var isForceLogout = false
    var activityLocationList: MutableList<UserLocationDataEntity>? = null
    var activityLocationListNew: MutableList<LocationData>? = null
    var isMemberMap = false
    var memberLocationList: MutableList<TeamLocDataModel>? = null
    var isAddedEdited = false
    var isFingerPrintSupported = true
    private var checkFingerPrint: CheckFingerPrint? = null
    var nearbyShopList: MutableList<AddShopDBModelEntity>? = null
    var isTimesheetAddedEdited = false
    var isAllMemberShop = false
    var areaId = ""
    var isBack = false
    private var selfieDialog: SelfieDialog? = null
    var isAllTeam = false
    var leaveType = ""
    var isClearData = false
    var dynamicScreen = ""
    var isDynamicFormUpdated = false
    var isFromMenu = false
    var isFromShop = false
    var isChatBotLocalShop = false
    var isChatBotAttendance = false
    var isWeatherFromDrawer = false
    var isAttendanceReportFromDrawer = false
    var isPerformanceReportFromDrawer = false
    var isVisitReportFromDrawer = false
    var isShopFromChatBot = false
    var isOrderFromChatBot = false
    var isCollectionStatusFromDrawer = false
    var isMapFromDrawer = false
    var isTargAchvFromDrawer = false
    var isAchvFromDrawer = false
    var userName = ""
    var grpId = ""
    var isGrp = false
    var visitDistance = ""
    lateinit var textToSpeech: TextToSpeech
    var visitReportDate = ""
    private var isVisitCardScan = false
    private var feedback = ""
    private var revisit_extraContName = ""
    private var revisit_extraContPh = ""
    private var revisitImage = ""
    private var nextVisitDate = ""
    private var mFilePath = ""
    private var mAudioFilePathNW = ""
    private var feedbackDialog: AddFeedbackSingleBtnDialog? = null
    private var shopName = ""
    private var contactNumber = ""
    private var isCodeScan = false
    private var isForRevisit = false
    private var reasonDialog: ReasonDialog? = null
    private var reason = ""
    private var netStatus = ""
    var isSubmit = false
    var isCalledJobApi = false

    private var feedBackDialogCompetetorImg = false
    lateinit var drawerLL: LinearLayout

    private var prosId = ""
    private var approxValue = ""

    lateinit var simpleDialogProcess: Dialog
    lateinit var dialogHeaderProcess: AppCustomTextView
    lateinit var dialog_yes_no_headerTVProcess: AppCustomTextView
    private var shop_id = ""
    private var lastLat = 0.0
    private var lastLng = 0.0

    private lateinit var tv_performance_teamMenu: AppCustomTextView

    //new menu
    private lateinit var cv_menu_adv_voice: CardView
    private lateinit var tv_menuVersion: TextView
    private lateinit var etSearchMenu: EditText
    private lateinit var ll_menuLogout: LinearLayout
    private lateinit var menuName: TextView
    private lateinit var menu_loginTime: TextView

    data class MenuItems(var name: String = "", var icon: Int = 0)
    data class MenuSubItems(
        var parentMenuName: String = "",
        var subList: ArrayList<MenuItems> = ArrayList()
    )

    private var menuItems: ArrayList<MenuItems> = ArrayList()
    private var menuSubItemsL: ArrayList<MenuSubItems> = ArrayList()
    private lateinit var rv_menu: RecyclerView
    private lateinit var adapterMenuAdv: AdapterMenuAdv


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mContext = this@DashboardActivity
        AppUtils.changeLanguage(this, "en")
        filter = IntentFilter()
        //filter?.addAction(AppUtils.gpsDisabledAction)
        //filter?.addAction(AppUtils.gpsEnabledAction)
        // code start by puja 05.04.2024 mantis id - 27333 v4.2.6
        filter!!.addAction(AppUtils.gpsDisabledAction)
        filter!!.addAction(AppUtils.gpsEnabledAction)
        filter!!.addAction("android.intent.action.PHONE_STATE")
        filter!!.addAction("android.intent.action.NEW_OUTGOING_CALL")
        // code end by puja 05.04.2024 mantis id - 27333 v4.2.6
        gpsReceiver = GpsLocationReceiver()
        teamHierarchy = ArrayList()
        //geoFenceBroadcast = GeofenceBroadcastReceiver()

        //checkToShowHomeLocationAlert()

        //checkToShowAddAttendanceAlert()
        //checkGPSAvailability()

        initView()
        updateUI()

        Timber.d("callAlarm() called")
        if (Pref.IsAlarmServiceRestartCalled == false) {
            Pref.IsAlarmServiceRestartCalled = true
            //callAlarm()
        }

        //Code by wasim
        if (intent != null) {
            var sss = intent.hasExtra("TYPE")
            var s = intent.hasExtra("fromClass")
            var ssss = intent.getStringExtra("TYPE")
            if (intent.getParcelableExtra<AlarmData>("ALARM_DATA") != null) {
                isFromAlarm = true
                val alaramData = intent.getParcelableExtra<AlarmData>("ALARM_DATA")
                alarmCofifDataModel = AlarmConfigDataModel()
                alarmCofifDataModel.requestCode = alaramData!!.requestCode
                alarmCofifDataModel.id = alaramData!!.id
                alarmCofifDataModel.report_id = alaramData!!.report_id
                alarmCofifDataModel.report_title = alaramData!!.report_title
                alarmCofifDataModel.alarm_time_hours = alaramData!!.alarm_time_hours
                alarmCofifDataModel.alarm_time_mins = alaramData!!.alarm_time_mins
            } else if (intent.hasExtra("fromClass")) {
                if (intent.getStringExtra("fromClass").equals("LoginActivity", ignoreCase = true)) {
                    /*CommonDialogSingleBtn.getInstance(getString(R.string.terms_conditions), getString(R.string.dummy_text), getString(R.string.ok), object : OnDialogClickListener {
 override fun onOkClick() {
 }
 }).show(supportFragmentManager, "CommonDialogSingleBtn")*/

                    isTermsAndConditionsPopShow = true

                    val list = AppDatabase.getDBInstance()?.shopActivityDao()
                        ?.getShopActivityNextVisitDateWise(AppUtils.getCurrentDateForShopActi())
                    list?.forEach {
                        val notification =
                            NotificationUtils(getString(R.string.app_name), "", "", "")
                        val shop =
                            AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(it.shopid)
                        val body =
                            "${AppUtils.hiFirstNameText()}!, your visit to " + it.shop_name + " whose contact no. is: " + shop?.ownerContactNumber + " & address is " +
                                    shop?.address + " due today as per next visit date. Thanks."
                        notification.sendRevisitDueNotification(this, body)
                    }

                    val shopList = AppDatabase.getDBInstance()?.addShopEntryDao()?.all
                    shopList?.forEach {
                        if (!TextUtils.isEmpty(it.dateOfBirth)) {
                            println("tag_dob_reg inside")
                            //if (AppUtils.getCurrentDateForShopActi() == AppUtils.changeAttendanceDateFormatToCurrent(it.dateOfBirth)) {
                            //Code start with Mantis 27454 date-14.05.2024 by Puja V-4.2.8
                            //if (AppUtils.getCurrentMonthDayForShopActi() == AppUtils.changeAttendanceDateFormatToMonthDay(it.dateOfBirth)) {
                            if (AppUtils.getCurrentMonthDayForShopActi() == AppUtils.getMonthDayFromDate(
                                    it.dateOfBirth
                                )
                            ) {
                                //Code end with Mantis 27454 date-14.05.2024 by Puja V-4.2.8
                                println("tag_dob_reg inside 1 dob ${it.dateOfBirth}")
                                val notification =
                                    NotificationUtils(getString(R.string.app_name), "", "", "")
                                var body = ""
                                body = if (TextUtils.isEmpty(it.ownerEmailId))
                                    "Please wish Mr. " + it.ownerName + " of " + it.shopName + ", Contact Number: " + it.ownerContactNumber + " for birthday today."
                                else
                                    "Please wish Mr. " + it.ownerName + " of " + it.shopName + ", Contact Number: " + it.ownerContactNumber + ", Email: " + it.ownerEmailId + " for birthday today."
                                tv_noti_count.visibility = View.VISIBLE
                                Pref.NotiCountFlag = true
                                //notification.sendLocNotification(this, body)
                                notification.sendLocNotificationNew(this, body)
                                println("tag_dob_reg inside 1 send noti dob ${it.dateOfBirth}")
                            }
                        }



                        if (!TextUtils.isEmpty(it.dateOfAniversary)) {
                            //if (AppUtils.getCurrentDateForShopActi() == AppUtils.changeAttendanceDateFormatToCurrent(it.dateOfAniversary)) {
                            //Code start with Mantis 27454 date-14.05.2024 by Puja V-4.2.8
                            //if (AppUtils.getCurrentMonthDayForShopActi() == AppUtils.changeAttendanceDateFormatToMonthDay(it.dateOfAniversary)) {

                            Handler().postDelayed(Runnable {
                                if (AppUtils.getCurrentMonthDayForShopActi() == AppUtils.getMonthDayFromDate(
                                        it.dateOfAniversary
                                    )
                                ) {
                                    //Code end with Mantis 27454 date-14.05.2024 by Puja V-4.2.8
                                    val notification =
                                        NotificationUtils(getString(R.string.app_name), "", "", "")
                                    var body = ""
                                    body = if (TextUtils.isEmpty(it.ownerEmailId))
                                        "Please wish Mr. " + it.ownerName + " of " + it.shopName + ", Contact Number: " + it.ownerContactNumber + " for Anniversary today."
                                    else
                                        "Please wish Mr. " + it.ownerName + " of " + it.shopName + ", Contact Number: " + it.ownerContactNumber + ", Email: " + it.ownerEmailId + " for Anniversary today."
                                    tv_noti_count.visibility = View.VISIBLE
                                    Pref.NotiCountFlag = true
                                    //notification.sendLocNotification(this, body)
                                    notification.sendLocNotificationNew(this, body)
                                }
                            }, 1500)
                        }

                    }

                    val leadActivityList = AppDatabase.getDBInstance()!!.leadActivityDao()
                        .getAll(AppUtils.getCurrentDateForShopActi())
                    Timber.d("lead activity ${leadActivityList.size}")
                    leadActivityList?.forEach {
                        val notification =
                            NotificationUtils(getString(R.string.app_name), "", "", "")
                        //val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(it.shopid)
                        val body =
                            "${AppUtils.hiFirstNameText()}!, your activity to " + it.customer_name + " whose contact no. is: " + it?.mobile_no + " due today as per next activity date. Thanks."
                        notification.sendLeadActivityNotification(this, body)
                    }

                    val taskList = AppDatabase.getDBInstance()?.taskManagementDao()
                        ?.getAll(AppUtils.getCurrentDateForShopActi())
                    taskList?.forEach {
                        val notification =
                            NotificationUtils(getString(R.string.app_name), "", "", "")
                        val body = "Your task " + it.task_details + " due today."
                        notification.sendTaskDueNotification(this, body)
                    }

                } else if (intent.getStringExtra("fromClass").equals("push", ignoreCase = true)) {
                    isClearData = true
                }
            }
        }


        if (!Pref.IsUserWiseLMSFeatureOnly) {
            if (isTermsAndConditionsPopShow) {
                callTermsAndConditionsdApi()
            } else {
                if (!Pref.isSeenTermsConditions) {
                    showTermsConditionsPopup()
                }
            }
        }


        //AlarmReceiver.setAlarm(this, 12, 11, 12345)

        checkGPS_Availability()

        /*********************Geofence*****************/
// mGeofenceList = ArrayList()
// mGeofencePendingIntent = null
// // Get the geofences used. Geofence data is hard coded in this sample.
// populateGeofenceList();
// mGeofencingClient = LocationServices.getGeofencingClient(this);
// if (!checkPermissions()) {
// PermissionHelper.checkLocationPermission(this, 0)
// } else {
// if(!Pref.isGeoFenceAdded)
// addGeofences()
// }
        /*********************Geofence*****************/

        /*********************GeofenceService*****************/
// if(!Pref.isGeoFenceAdded) {
// Pref.isGeoFenceAdded=true
// geofenceService = Intent(this, GeofenceService::class.java)
// startService(geofenceService)
// }
        /*********************GeofenceService*****************/
        if (AppDatabase.getDBInstance()!!.shopActivityDao().getAll().isNotEmpty())
            takeActionOnGeofence()
        setProfileImg()
        initBackStackActionSet()
        Log.d("settingsval", "" + Pref.IsUserWiseLMSFeatureOnly.toString())

        loadHomeFragment()


        if (/*!isFromAlarm && intent.hasExtra("TYPE")*/pushStatus == 0 && forceLogoutDialog == null) {
            pushStatus = -1
            //logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
            loadFragment(FragType.NotificationFragment, true, "")
        }

        Pref.appLaunchDate = AppUtils.getCurrentDate()


        if (!TextUtils.isEmpty(Pref.appLaunchDate)) {
            if (!AppUtils.compareWithCurrentDate(Pref.appLaunchDate!!)) {
                Pref.appLaunchDate = AppUtils.getCurrentDate()
                AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsVisitedToFalse(false)
            }
        } else {
            Pref.appLaunchDate = AppUtils.getCurrentDate()
        }

        //Code by wasim
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mAlarmReceiver, IntentFilter("ALARM_RECIEVER_BROADCAST"))

        if (shouldFetchLocationActivity())
            fetchActivityList()
        /*else {
 if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }
 }*/
//


        //TODO stuff
        /*if (intent != null && intent.extras != null && !isAttendanceAlertPresent && !isGpsDisabled)
 callShopVisitConfirmationDialog(intent.extras.get("NAME") as String, intent.extras.get("ID") as String)*/

// searchView.openSearch()


        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fcmClearDataReceiver, IntentFilter("FCM_ACTION_RECEIVER_CLEAR_DATA"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fcmReceiver, IntentFilter("FCM_ACTION_RECEIVER"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fcmReceiver_leave, IntentFilter("FCM_ACTION_RECEIVER_LEAVE"))
        LocalBroadcastManager.getInstance(this).registerReceiver(
            fcmReceiver_leave_status,
            IntentFilter("FCM_ACTION_RECEIVER_LEAVE_STATUS")
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(
            fcmReceiver_quotation_approval,
            IntentFilter("FCM_ACTION_RECEIVER_quotation_approval")
        )
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fcm_ACTION_RECEIVER_LEAD, IntentFilter("FCM_ACTION_RECEIVER_LEAD"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(idealLocReceiver, IntentFilter("IDEAL_LOC_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(attendNotiReceiver, IntentFilter("IDEAL_ATTEND_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(collectionAlertReceiver, IntentFilter("ALERT_RECIEVER_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(forceLogoutReceiver, IntentFilter("FORCE_LOGOUT_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(autoRevisit, IntentFilter("AUTO_REVISIT_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(offlineShopReceiver, IntentFilter("OFFLINE_SHOP_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(chatReceiver, IntentFilter("FCM_CHAT_ACTION_RECEIVER"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localeReceiver, IntentFilter("CHANGE_LOCALE_BROADCAST"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(updateStatusReceiver, IntentFilter("FCM_STATUS_ACTION_RECEIVER"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(homeLocReceiver, IntentFilter("HOME_LOC_ACTION_RECEIVER"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(revisitReceiver, IntentFilter("REVISIT_REASON_BROADCAST"))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(updatePJP, IntentFilter("UPDATE_PJP_LIST"))

        Handler().postDelayed(Runnable {
            if (Pref.isShowHomeLocReason && Pref.willShowHomeLocReason && (reasonDialog == null || !reasonDialog?.isVisible!!))
                showHomeLocReasonDialog()
        }, 500)


        Handler().postDelayed(Runnable {
            if (Pref.isShowShopVisitReason && Pref.willShowShopVisitReason && (reasonDialog == null || !reasonDialog?.isVisible!!))
                showRevisitReasonDialog(0, null, "", "", null, null)
        }, 700)

        if (!AppUtils.isOnline(mContext)) {
            fl_net_status.background = getDrawable(R.drawable.red_round)
            netStatus = "Offline"
        }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (AppUtils.isN) {
            connectivityManager?.let {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        try {
                            fl_net_status.background = getDrawable(R.drawable.green_round)
                            netStatus = "Online"
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onLost(network: Network) {
                        try {
                            fl_net_status.background = getDrawable(R.drawable.red_round)
                            netStatus = "Offline"
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            }
        } else {
            val builder = NetworkRequest.Builder()
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            val networkRequest = builder.build()
            connectivityManager?.registerNetworkCallback(networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        try {
                            fl_net_status.background = getDrawable(R.drawable.green_round)
                            netStatus = "Online"
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        try {
                            fl_net_status.background = getDrawable(R.drawable.red_round)
                            netStatus = "Offline"
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                })
        }


        Handler().postDelayed(Runnable {
            if (!isWorkerRunning("workerTag")) {
                val constraint = Constraints.Builder()
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .setRequiresBatteryNotLow(true)
                    .build()
                val request =
                    PeriodicWorkRequest.Builder(WorkerService::class.java, 15, TimeUnit.MINUTES)
                        .setConstraints(constraint)
                        .addTag("workerTag")
                        .build()
                WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "loc_worker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
            }
        }, 1000)


        // In your Application class or MainActivity
        // FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(application); // For logging app activation events


    }

    //Start of Rev 18 DashboardActivity AppV 4.0.8 Suman 28/04/2023 worker manager updation 25973
    fun isWorkerRunning(tag: String): Boolean {
        val workInstance = WorkManager.getInstance(this)
        val status: ListenableFuture<List<WorkInfo>> =
            WorkManager.getInstance(this).getWorkInfosByTag(tag)
        try {
            var runningStatus: Boolean = false
            val workInfoList: List<WorkInfo> = status.get()
            for (obj: WorkInfo in workInfoList) {
                var state: WorkInfo.State = obj.state
                runningStatus = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            return runningStatus
        } catch (ex: ExecutionException) {
            return false
        } catch (ex: InterruptedException) {
            return false
        }
    }
    //End of Rev 18 DashboardActivity AppV 4.0.8 Suman 28/04/2023 worker manager updation 25973

    fun checkToShowHomeLocationAlert() {
        if (!Pref.isHomeLocAvailable && Pref.IsShowHomeLocationMapGlobal && Pref.IsShowHomeLocationMap) {
            //if(Pref.IsShowHomeLocationMapGlobal && Pref.IsShowHomeLocationMap){
            showHomeLocationAlert()
            //}
        } else {
            if (Pref.IsOnLeaveForTodayApproved == false && !Pref.OnLeaveForTodayStatus.equals("PENDING"))
                checkToShowAddAttendanceAlert()
            else {
                if (Pref.IsOnLeaveForTodayApproved == false && Pref.OnLeaveForTodayStatus.equals("PENDING")) {
                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!
                        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_message)
                    val dialogHeader =
                        simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                    val dialog_yes_no_headerTV =
                        simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                    dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText() + "!"
                    dialogHeader.text =
                        "Your applied leave status has not been updated. Contact your Supervisor."
                    val dialogYes =
                        simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                    dialogYes.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()
                }
            }
        }
    }


    private fun showHomeLocationAlert() {
        if (Pref.IsUserWiseLMSFeatureOnly) {
            return
        }

        CommonDialogSingleBtn.getInstance(
            "Attendance Address",
            "Please pin your attendance address from the map",
            "Open map",
            object : OnDialogClickListener {

                override fun onOkClick() {
                    (mContext as DashboardActivity).loadFragment(
                        FragType.SearchLocationFragment,
                        true,
                        ""
                    )
                }
            }).show(supportFragmentManager, "CommonDialogSingleBtn")
    }

    private fun callTermsAndConditionsdApi() {

        isTermsAndConditionsPopShow = false

        if (!AppUtils.isOnline(mContext)) {
            checkToShowHomeLocationAlert()
            showSnackMessage(getString(R.string.no_internet))
            return
        }

        Timber.e("=========Call terms & conditions api (Dashboard)============")

        val repository = GetContentListRepoProvider.getContentListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.getContentList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as ContentListResponseModel

                    Timber.e("RESPONSE: " + response.status + ", MESSAGE: " + response.message)

                    if (response.status == NetworkConstant.SUCCESS) {
                        if (!Pref.isSefieAlarmed)
                            progress_wheel.stopSpinning()

                        if (response.contentlist != null && response.contentlist!!.size > 0) {


                            for (i in response.contentlist!!.indices) {
                                if (response.contentlist?.get(i)?.TemplateID == "1") {
                                    Pref.termsConditionsText =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                            Html.fromHtml(
                                                response.contentlist?.get(i)?.content!!,
                                                Html.FROM_HTML_MODE_COMPACT
                                            ).toString()
                                        else
                                            Html.fromHtml(response.contentlist?.get(i)?.content!!)
                                                .toString()
                                }
                            }

                            showTermsConditionsPopup()
                        }

                    } else if (response.status == NetworkConstant.SESSION_MISMATCH) {
                        if (!Pref.isSefieAlarmed)
                            progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).clearData()
                        startActivity(
                            Intent(
                                mContext as DashboardActivity,
                                LoginActivity::class.java
                            )
                        )
                        (mContext as DashboardActivity).overridePendingTransition(0, 0)
                        (mContext as DashboardActivity).finish()
                    } else if (response.status == NetworkConstant.NO_DATA) {
                        if (!Pref.isSefieAlarmed)
                            progress_wheel.stopSpinning()
                        checkToShowHomeLocationAlert()

                    } else {
                        if (!Pref.isSefieAlarmed)
                            progress_wheel.stopSpinning()
                        checkToShowHomeLocationAlert()
                    }

                }, { error ->
                    Timber.e("ERROR: " + error.message)
                    error.printStackTrace()
                    if (!Pref.isSefieAlarmed)
                        progress_wheel.stopSpinning()
                    checkToShowHomeLocationAlert()
                })
        )
    }

    private fun showTermsConditionsPopup() {
        if (TextUtils.isEmpty(Pref.termsConditionsText)) {
            checkToShowHomeLocationAlert()
            return
        }

        Timber.e("=========Show terms & conditions popup (Dashboard)============")

        if (termsConditionsDialog != null) {
            termsConditionsDialog?.dismissAllowingStateLoss()
            termsConditionsDialog = null
        }

        termsConditionsDialog = TermsAndConditionsSingleBtnDialog.getInstance(
            getString(R.string.terms_conditions),
            Pref.termsConditionsText,
            "I Agree",
            object : OnDialogClickListener {
                override fun onOkClick() {
                    Pref.isSeenTermsConditions = true
                    checkToShowHomeLocationAlert()
                }
            })//.show(supportFragmentManager, "CommonDialogSingleBtn")
        termsConditionsDialog?.show(supportFragmentManager, "CommonDialogSingleBtn")


    }

    fun checkLocationMode() {
        val locationMode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
        if (locationMode != 3) {
            showSnackMessage("Please set location mode to High Accuracy")
            Handler().postDelayed(Runnable {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }, 1000)
        }
    }

    override fun onStart() {
        // code start by puja 10.04.2024 mantis id - 27333 v4.2.6
        //super.onStart()
        try {
            super.onStart()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // code end by puja 10.04.2024 mantis id - 27333 v4.2.6

// showSnackMessage("onStart")
// if (!checkPermissions()) {
// showSnackMessage("onStart:No permission")
// PermissionHelper.checkLocationPermission(this, 0)
// } else {
// addGeofences()
// showSnackMessage("onStart:GeofenceAdded")
// }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkGPS_Availability() {
        mGpsStatusDetector = GpsStatusDetector(this)
        var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGpsDisabled = true
            //loadFragment(FragType.GpsDisableFragment, true, "")
            mGpsStatusDetector?.checkGpsStatus()
        } else {
            isGpsDisabled = false
            //checkLocationMode()
            if (intent != null && intent.extras != null /*&& Pref.isAddAttendence*/ /*&& !isGpsDisabled*/) {

                //Code by wasim
                if (!isFromAlarm) {
                    if (intent.extras!!.get("NAME") != null) {
                        //if (forceLogoutDialog == null)
                        if (Pref.isAddAttendence)
                            callShopVisitConfirmationDialog(
                                intent.extras!!.get("NAME") as String,
                                intent.extras!!.get("ID") as String
                            )
                        else {
                            Handler().postDelayed(Runnable {
                                checkToShowAddAttendanceAlert()
                            }, 1000)
                        }
                    } else {
                        if (intent.hasExtra("TYPE")) {
                            //Toaster.msgShort(this,intent.getStringExtra("TYPE").toString())
                            if (intent.getStringExtra("TYPE").equals("PUSH", ignoreCase = true))
                                pushStatus = 0
                            else if (intent.getStringExtra("TYPE")
                                    .equals("DUE", ignoreCase = true)
                            ) {
                                if (getFragment() != null && getFragment() !is NearByShopsListFragment)
                                    loadFragment(FragType.NearByShopsListFragment, false, "")
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("ACTIVITYDUE", ignoreCase = true)
                            ) {
                                if (getFragment() != null && getFragment() !is LeadFrag)
                                    loadFragment(FragType.LeadFrag, false, "")
                            }
                            // begin rev 19.0 mantis 0026023
                            else if (intent.getStringExtra("TYPE")
                                    .equals("ACTIVITYDUETASK", ignoreCase = true)
                            ) {
                                if (getFragment() != null && getFragment() !is TaskManagementFrag)
                                    loadFragment(FragType.TaskManagementFrag, false, "")
                            }
                            // end rev 19.0 mantis 0026023
                            else if (intent.getStringExtra("TYPE")
                                    .equals("TASK", ignoreCase = true)
                            ) {
                                if (getFragment() != null && getFragment() !is TaskListFragment)
                                    loadFragment(FragType.TaskListFragment, false, "")
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("Msg", ignoreCase = true)
                            ) {
                                val chatUser =
                                    intent.getSerializableExtra("chatUser") as ChatUserDataModel
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() is ChatListFragment)
                                        onBackPressed()

                                    userName = chatUser.name
                                    loadFragment(FragType.ChatListFragment, true, chatUser)
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("TIMESHEET", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is TimeSheetListFragment)
                                        loadFragment(FragType.TimeSheetListFragment, false, "")
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("REIMBURSEMENT", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is ReimbursementListFragment)
                                        loadFragment(FragType.ReimbursementListFragment, false, "")
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("VIDEO", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is MicroLearningListFragment)
                                        loadFragment(FragType.MicroLearningListFragment, false, "")
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("lead_work", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is LeadFrag)
                                        loadFragment(FragType.LeadFrag, false, "")
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("lms_content_assign", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    loadFragment(FragType.NotificationLMSFragment, false, "")
                                }, 500)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("clearData", ignoreCase = true)
                            ) {
                                isClearData = true

                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is LogoutSyncFragment) {
                                        if (AppUtils.isOnline(this))
                                            loadFragment(FragType.LogoutSyncFragment, false, "")
                                        else
                                            showSnackMessage(getString(R.string.no_internet))
                                    }
                                }, 500)

                            } else if (intent.getStringExtra("TYPE")
                                    .equals("LEAVE_APPLY", ignoreCase = true)
                            ) {
                                var usrID = intent.getStringExtra("USER_ID")!!

                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is LeaveHome)
                                        loadFragment(FragType.LeaveHome, false, usrID)
                                }, 700)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("LEAVE_STATUS", ignoreCase = true)
                            ) {
                                var usrID = intent.getStringExtra("USER_ID")!!
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is LeaveHome)
                                    //loadFragment(FragType.LeaveHome, false, usrID)
                                        loadFragment(FragType.LeaveListFragment, false, "")
                                }, 700)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("quotation_approval", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is ViewAllQuotListFragment)
                                        loadFragment(
                                            FragType.MemberListFragment,
                                            false,
                                            Pref.user_id!!
                                        )
                                }, 700)
                            } else if (intent.getStringExtra("TYPE")
                                    .equals("ZERO_COLL_STATUS", ignoreCase = true)
                            ) {
                                Handler().postDelayed(Runnable {
                                    if (getFragment() != null && getFragment() !is CollectionNotiViewPagerFrag1 && getFragment() !is CollectionNotiViewPagerFrag) {
                                        SendBrod.stopBrodColl(this)
                                        SendBrod.stopBrodZeroOrder(this)
                                        tv_noti_count.visibility = View.GONE

                                        if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification == false) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag1,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag1,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag1,
                                                true,
                                                ""
                                            )
                                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                                            loadFragment(
                                                FragType.CollectionNotiViewPagerFrag2,
                                                true,
                                                ""
                                            )
                                        }
                                    }
                                }, 500)
                            } else {
                                showForceLogoutPopup()
                            }
                        }
                    }
                }
                intent = null
            }

            Handler().postDelayed(Runnable {
                if (getFragment() != null && getFragment() is GpsDisableFragment) {
                    onBackPressed()
                }
            }, 500)

            if (PermissionHelper.checkLocationPermission(this, 0)) {
                if (!FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, this)) {
                    /*Start & Stop Expensive service stuff when logged out*/
                    serviceStatusActionable()
                    /*val serviceLauncher = Intent(this, LocationFuzedService::class.java)
 startService(serviceLauncher)*/
                }
            }
        }
    }

    // GpsStatusDetectorCallBack
    override fun onGpsSettingStatus(enabled: Boolean) {

        if (enabled) {
            (mContext as DashboardActivity).showSnackMessage("GPS enabled")
        } else
            (mContext as DashboardActivity).showSnackMessage("GPS disabled")
    }

    override fun onGpsAlertCanceledByUser() {
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun syncShopListAndLogout() {
        Pref.logout_time = AppUtils.getCurrentTimeWithMeredian()
        Pref.logout_latitude = "0.0"
        Pref.logout_longitude = "0.0"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        finish()
        super.clearData()
    }

    @SuppressLint("NewApi", "WrongConstant")
    override fun onResume() {
        super.onResume()

        //begin Suman 21-09-2023 mantis id 0026837
        try {
            val packageName = "com.google.android.apps.maps"
            val appInfo: ApplicationInfo =
                this.getPackageManager().getApplicationInfo(packageName, 0)
            var appstatus = appInfo.enabled

            if (!appstatus) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok)

                try {
                    simpleDialog.setCancelable(true)
                    simpleDialog.setCanceledOnTouchOutside(false)
                    val dialogName =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                    val dialogCross =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                    dialogName.text = AppUtils.hiFirstNameText()
                    dialogCross.setOnClickListener {
                        simpleDialog.cancel()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                dialogHeader.text =
                    "Location will be inappropriate as Google map is disabled. Please go to settings of your phone and Enable Google Map. Thank you."
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                })
                simpleDialog.show()
                return
            } else {
                println("load_frag " + " gmap app enable")
            }

            //val pInfo = this.packageManager.getPackageInfo("com.google.android.apps.maps", PackageManager.GET_PERMISSIONS)
            //val version = pInfo.versionName
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        //end Suman 21-09-2023 mantis id 0026837

        println("tag_lifecycle onresume")
        //if(Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) == 1 && Pref.IsUsbDebuggingRestricted) {
        if (Settings.Secure.getInt(
                this.getContentResolver(),
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) == 1 && Pref.IsUsbDebuggingRestricted
        ) {
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_debugger)
            val okBtn = simpleDialog.findViewById(R.id.tv_dialog_ok) as AppCustomTextView
            val tvHeader =
                simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
            tvHeader.text = getString(R.string.app_name)
            okBtn.setOnClickListener({ view ->
                simpleDialog.cancel()
                startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
            })
            simpleDialog.show()
        }


        /* try{
 var launchIntent: Intent? = packageManager.getLaunchIntentForPackage("com.anydesk.anydeskandroid")
 if(launchIntent!=null){
 anydesk_info_TV.text="Open Anydesk"
 }else{
 anydesk_info_TV.text="Install Anydesk"
 }
 }catch (ex:Exception){
 ex.printStackTrace()
 }*/


        /* if(DashboardFragment.hbRecorder ==null){
 screen_record_info_TV.text="Start Screen Recorder"
 }else{
 if(DashboardFragment.hbRecorder!!.isBusyRecording){
 screen_record_info_TV.text="Stop Recording"
 }else{
 screen_record_info_TV.text="Start Screen Recorder"
 }
 }*/

        //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

        /* if(DashboardFragment.isRecordRootVisible){
 screen_record_info_TV.text="Stop Recording"
 }else{
 screen_record_info_TV.text="Screen Recorder"
 }*/
        //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7


        if (!isGpsDisabled)
            checkLocationMode()
        val networkIntentFilter = IntentFilter()
        networkIntentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        Timber.d("DashActi registerReceiver gpsReceiver")
        //registerReceiver(gpsReceiver, networkIntentFilter);
        registerReceiver(gpsReceiver, networkIntentFilter);

        //registerReceiver(broadcastReceiver, filter)
        mContext.registerReceiver(broadcastReceiver, filter, RECEIVER_EXPORTED)
        //registerReceiver(geoFenceBroadcast, IntentFilter())
        //checkToShowAddAttendanceAlert()

        callUnreadNotificationApi()

        // 26.0 DashboardActivity Fingerprint flow update Suman 14-05-2024 mantis id 0027450 v4.2.8
        //checkForFingerPrint()


        //battery check onresume
        if (Pref.IsAnyPageVisitFromDshboard) {
            val pm = mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
            var sett = pm.isIgnoringBatteryOptimizations(packageName)
            if (sett == false) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok_logout)
                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                if (AppUtils.getDeviceName()
                        .contains("MI", ignoreCase = true) || AppUtils.getDeviceName()
                        .contains("Redmi", ignoreCase = true) ||
                    AppUtils.getDeviceName().contains("Poco", ignoreCase = true)
                ) {
                    dialogHeader.text =
                        "Go to Settings -> Apps -> Manage Apps -> FSM App -> Battery Saver -> No restrictions."
                } else if (AppUtils.getDeviceName().contains("Vivo", ignoreCase = true)) {
                    dialogHeader.text =
                        "Go to Settings -> Battery -> Background Battery Power Consumption Ranking / High Background Power Consumption -> FSM App -> Don't Restrict."
                } else {
                    dialogHeader.text =
                        "Go to Settings -> Apps -> FSM App -> Battery -> Unrestricted."
                }
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                    callLogout()
                })
                simpleDialog.show()
                Handler().postDelayed(Runnable {
                    val timer = object : CountDownTimer(6 * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            dialogYes.text =
                                "Logout in " + (millisUntilFinished / 1000).toString() + " seconds..."
                        }

                        override fun onFinish() {
                            simpleDialog.cancel()
                            callLogout()
                        }
                    }.start()
                }, 1000)
            }
        }

    }

    fun checkForFingerPrint() {
        try {
            if (!Pref.isFingerPrintMandatoryForAttendance) {
                AppUtils.changeLanguage(this@DashboardActivity, "en")
                if (AppUtils.isRevisit!!) {
                    if (fingerprintDialog != null && fingerprintDialog?.isVisible!!) {
                        fingerprintDialog?.dismiss()
                        revisitShop(revisitImage)
                    }
                } else {
                    if (getFragment() != null) {
                        if (getFragment() is AddAttendanceFragment) {
                            (getFragment() as AddAttendanceFragment).continueAddAttendance()
                        } else if (getFragment() is DailyPlanListFragment) {
                            (getFragment() as DailyPlanListFragment).continueAddAttendance()
                        } else if (getFragment() is AddShopFragment) {
                            (getFragment() as AddShopFragment).addShop()
                        }
                    }
                }
                if (getFragment() != null && getFragment() is ChatBotFragment)
                    AppUtils.changeLanguage(
                        this@DashboardActivity,
                        (getFragment() as ChatBotFragment).language
                    )
            } else {

                if (checkFingerPrint != null)
                    checkFingerPrint = null

                AppUtils.changeLanguage(this, "en")

                checkFingerPrint = CheckFingerPrint()
                checkFingerPrint?.checkFingerPrint(
                    this,
                    object : CheckFingerPrint.FingerPrintListener {
                        override fun isFingerPrintSupported(status: Boolean) {
                            if (status) {
                                Log.e(
                                    "DashboardActivity",
                                    "========Device support fingerprint==========="
                                )
                            } else {
                                Log.e(
                                    "DashboardActivity",
                                    "==========Device does not support fingerprint==========="
                                )
                                isFingerPrintSupported = false
                            }
                        }

                        override fun onSuccess(signal: CancellationSignal?) {

                            /*if (signal?.isCanceled!!) {
 signal.cancel()
 }*/

                            // 15.0 DashboardActivity AppV 4.0.8 Suman 19/04/2023 Dashboard Onresume updation for language 0025874
                            AppUtils.changeLanguage(this@DashboardActivity, "en")
                            Log.e(
                                "DashboardActivity",
                                "============Fingerprint accepted============="
                            )
                            // End of Rev 15.0

                            if (AppUtils.isRevisit!!) {
                                if (fingerprintDialog != null && fingerprintDialog?.isVisible!!) {
                                    fingerprintDialog?.dismiss()
                                    revisitShop(revisitImage)
                                }
                            } else {
                                if (getFragment() != null) {
                                    if (getFragment() is AddAttendanceFragment) {
                                        (getFragment() as AddAttendanceFragment).continueAddAttendance()
                                    } else if (getFragment() is DailyPlanListFragment) {
                                        (getFragment() as DailyPlanListFragment).continueAddAttendance()
                                    } else if (getFragment() is AddShopFragment) {
                                        (getFragment() as AddShopFragment).addShop()
                                    }
                                }
                            }

                            if (getFragment() != null && getFragment() is ChatBotFragment)
                                AppUtils.changeLanguage(
                                    this@DashboardActivity,
                                    (getFragment() as ChatBotFragment).language
                                )
                        }

                        override fun onError(msg: String) {
                            Log.e("DashboardActivity", "Fingerprint error=====> $msg")

                            // Rev 15.0
                            if (!Locale.getDefault().language.equals("en", ignoreCase = true))
                                return
                            // End of Rev 15.0
                            when {
                                msg.equals(
                                    "Fingerprint operation cancelled.",
                                    ignoreCase = true
                                ) -> {
                                }

                                msg.equals(
                                    "Fingerprint operation cancelled",
                                    ignoreCase = true
                                ) -> {
                                }

                                msg.equals("Fingerprint operation canceled", ignoreCase = true) -> {
                                }

                                msg.equals(
                                    "Fingerprint operation canceled.",
                                    ignoreCase = true
                                ) -> {
                                }

                                else -> Toaster.msgLong(mContext, msg)
                            }

                            if (getFragment() != null && getFragment() is ChatBotFragment)
                                AppUtils.changeLanguage(
                                    this@DashboardActivity,
                                    (getFragment() as ChatBotFragment).language
                                )
                        }

                    })

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CheckFingerPrint().FingerprintHandler().doAuth()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun callUnreadNotificationApi() {

        if (!AppUtils.isOnline(this))
            return

        val repository = UnreadNotificationRepoProvider.unreadNotificationRepoProvider()
        //progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.unreadNotification()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as UnreadNotificationResponseModel
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        if (response.isUnreadNotificationPresent.equals("true", ignoreCase = true))
                            logo.startAnimation(
                                AnimationUtils.loadAnimation(
                                    mContext,
                                    R.anim.shake
                                )
                            )
                        else {
                            logo.clearAnimation()
                            logo.animate().cancel()
                        }
                    }
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    fun checkToShowAddAttendanceAlert() {
        var inTime = ""
        var outTime = ""

        /*if (AppUtils.getCurrentTimeWithMeredian().contains("AM") || AppUtils.getCurrentTimeWithMeredian().contains("PM")) {
 inTime = "8:00 AM"
 outTime = "11:59 PM"
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("a.m.") || AppUtils.getCurrentTimeWithMeredian().contains("p.m")) {
 inTime = "8:00 a.m."
 outTime = "11:59 p.m."
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("am") || AppUtils.getCurrentTimeWithMeredian().contains("pm")) {
 inTime = "8:00 am"
 outTime = "11:59 pm"
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("A.M.") || AppUtils.getCurrentTimeWithMeredian().contains("P.M.")) {
 inTime = "8:00 A.M."
 outTime = "11:59 P.M."
 }*/

        //Pref.isAddAttendence = false
        /*if (AppUtils.convertDateTimeWithMeredianToLong(AppUtils.getCurrentTimeWithMeredian()) >= AppUtils.convertDateTimeWithMeredianToLong(inTime)
 && AppUtils.convertDateTimeWithMeredianToLong(AppUtils.getCurrentTimeWithMeredian()) <= AppUtils.convertDateTimeWithMeredianToLong(outTime)) {*/

        Handler().postDelayed(Runnable {
            if (!Pref.isAddAttendence && !Pref.isAutoLogout && forceLogoutDialog == null) {
                isAttendanceAlertPresent = true
                showAddAttendanceAlert()
            } else {
                isAttendanceAlertPresent = false

                if (isOrderDialogShow)
                    showOrderCollectionAlert(isOrderAdded, isCollectionAdded)
            }
        }, 250)


        /*} else {
 isAttendanceAlertPresent = false
 }*/
    }

    var appliedLeaveList: ArrayList<Leave_list_Response> = ArrayList()

    private var dialog: CommonDialog? = null
    private fun showAddAttendanceAlert() {

        if (Pref.IsUserWiseLMSFeatureOnly) {
            return
        }

        /*CommonDialogSingleBtn.getInstance(getString(R.string.attendance_alert), getString(R.string.attendance_msg_bdy), getString(R.string.ok), object : OnDialogClickListener {
 override fun onOkClick() {
 if (!isGpsDisabled) {
 isAddAttendaceAlert = true
 loadFragment(FragType.AddAttendanceFragment, true, "")
 }
 //else
 // showAddAttendanceAlert()
 }
 }).show(supportFragmentManager, "CommonDialogSingleBtn")*/


        if (dialog == null || !dialog?.isVisible!!) {
            dialog = CommonDialog.getInstance(AppUtils.hiFirstNameText() + "!",
                getString(R.string.attendance_msg_bdy), /*getString(R.string.cancel),
 getString(R.string.ok),*/
                true,
                object : CommonDialogClickListener {
                    override fun onLeftClick() {
                    }

                    override fun onRightClick(editableData: String) {
                        if (!isGpsDisabled) {

                            /*if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
 if (Pref.isHomeLocAvailable) {

 if (!TextUtils.isEmpty(Pref.home_latitude) && !TextUtils.isEmpty(Pref.home_longitude)) {
 val distance = LocationWizard.getDistance(Pref.home_latitude.toDouble(), Pref.home_longitude.toDouble(), Pref.current_latitude.toDouble(),
 Pref.current_longitude.toDouble())

 Timber.e("Distance from home====> $distance")

 if (distance * 1000 > 50) {
 isAddAttendaceAlert = true
 loadFragment(FragType.AddAttendanceFragment, true, "")
 } else
 (mContext as DashboardActivity).showSnackMessage("Attendance can not be added from home")
 } else {
 Timber.e("========Home location is not available========")
 isAddAttendaceAlert = true
 loadFragment(FragType.AddAttendanceFragment, true, "")
 }

 } else {
 Timber.e("========isHomeLocAvailable is false========")
 isAddAttendaceAlert = true
 loadFragment(FragType.AddAttendanceFragment, true, "")
 }
 } else {
 Timber.e("========Current location is not available========")*/
                            isAddAttendaceAlert = true

                            val attendanceReq = AttendanceRequest()
                            attendanceReq.user_id = Pref.user_id!!
                            attendanceReq.session_token = Pref.session_token
                            attendanceReq.start_date = AppUtils.getCurrentDateForCons()
                            attendanceReq.end_date = AppUtils.getCurrentDateForCons()

                            val repository =
                                AttendanceRepositoryProvider.provideAttendanceRepository()
                            progress_wheel.spin()
                            BaseActivity.compositeDisposable.add(
                                repository.getAttendanceList(attendanceReq)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({ result ->
                                        val attendanceList = result as AttendanceResponse
                                        if (attendanceList.status == "205") {
                                            progress_wheel.stopSpinning()
                                            loadFragment(FragType.AddAttendanceFragment, true, "")
                                        } else if (attendanceList.status == NetworkConstant.SUCCESS) {
                                            progress_wheel.stopSpinning()
                                            Pref.isAddAttendence = true
                                            (mContext as DashboardActivity).showSnackMessage("${AppUtils.hiFirstNameText()}. Attendance already marked for the day.")
                                        }

                                    }, { error ->
                                        progress_wheel.stopSpinning()
                                        error.printStackTrace()
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                                    })
                            )


                            //loadFragment(FragType.AddAttendanceFragment, true, "")
                            //}
                        }
                    }

                })//.show(supportFragmentManager, "")
            // code start by puja 23.03.2024 mantis id - 27333 v4.2.6
            //dialog?.show(supportFragmentManager, "")
            dialog!!.show(supportFragmentManager, "")
            // code end by puja 05.04.2024 mantis id - 27333 v4.2.6
        }
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Timber.d("DashActi broadcastReceiver gps status GpsDisableFragment ${AppUtils.gpsDisabledAction}")
            if (intent.action == AppUtils.gpsDisabledAction) {
                isGpsDisabled = true
                loadFragment(FragType.GpsDisableFragment, true, "")

                /*val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
 if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
 buildAlertMessageNoGps()
 }*/

            } else {
                isGpsDisabled = false
                if (getFragment() != null && getFragment() is GpsDisableFragment) {
                    onBackPressed()
                }
            }
        }
    }

    override fun onPause() {
        println("tag_lifecycle DashboardActivity onPause")
        callmoduleEnd()
        if (checkFingerPrint?.signal != null)
            checkFingerPrint?.signal?.cancel()
        else {
            checkFingerPrint?.signal = CancellationSignal()
            checkFingerPrint?.signal?.cancel()
        }

        super.onPause()

// try {
// /*LocalBroadcastManager.getInstance(this).*/unregisterReceiver(idealLocReceiver)
// LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver)
// } catch (e: Exception) {
// e.printStackTrace()
// }
    }


    override fun onDestroy() {
        //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7
        // DashboardFragment.isRecordRootVisible=false
        //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

        textToSpeech?.let {
            it.stop()
            it.shutdown()
        }
        // code start by puja 23.03.2024 mantis id - 27333
        // unregisterReceiver(broadcastReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        // code end by puja 23.03.2024 mantis id - 27333
        Timber.d("DashActi onDestroy")
        if (gpsReceiver != null)
        // code start by puja 23.03.2024 mantis id - 27333
        // unregisterReceiver(gpsReceiver)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver!!)
        // code end by puja 23.03.2024 mantis id - 27333
        //Code by wasim
        if (mAlarmReceiver != null) {
            try {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mAlarmReceiver)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        if (idealLocReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(idealLocReceiver)
        }

        if (attendNotiReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(attendNotiReceiver)
        }

        if (fcmReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver)


        if (fcmReceiver_leave != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver_leave)

        // 13.0 DashboardActivity AppV 4.0.7 Suman 31-03-2023 quotation auto mail app kill work mantis 25766
        Timber.d("quto_mail ondestroy receiver...")
        /* if (fcmReceiver_quotation_approval != null)
 LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver_quotation_approval)
 */

        if (collectionAlertReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(collectionAlertReceiver)


        if (forceLogoutReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(forceLogoutReceiver)

        if (autoRevisit != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(autoRevisit)

        if (chatReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver)

        if (updateStatusReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(updateStatusReceiver)

        if (localeReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(localeReceiver)

        if (homeLocReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(homeLocReceiver)

        if (revisitReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(revisitReceiver)

        if (updatePJP != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(updatePJP)

        super.onDestroy()
        /*if (geoFenceBroadcast != null)
 unregisterReceiver(geoFenceBroadcast)*/
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        onFireAlarm(intent!!)

        if (intent == null || intent.extras == null)
            return
        var shopName = ""
        var shopId = ""
        if (intent.extras!!.get("NAME") != null)
            shopName = intent.extras!!.get("NAME") as String
        if (intent.extras!!.get("ID") != null)
            shopId = intent.extras!!.get("ID") as String
        if (intent.extras!!.getString("ACTION").equals("CANCEL", true))
            cancelNotification(shopId)
        else if (shopName.isNotBlank() && shopId.isNotBlank() && !isGpsDisabled) {
            //if (forceLogoutDialog == null) {
            if (Pref.isAddAttendence)
                callShopVisitConfirmationDialog(
                    intent.extras!!.get("NAME") as String,
                    intent.extras!!.get("ID") as String
                )
            else {
                Handler().postDelayed(Runnable {
                    checkToShowAddAttendanceAlert()
                }, 1000)
            }
            //}
        } else if (intent.hasExtra("TYPE")) {
            var tt = intent.getStringExtra("TYPE")
            //logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
            if (intent.getStringExtra("TYPE").equals("PUSH", ignoreCase = true)) {
                if (forceLogoutDialog == null)
                    loadFragment(FragType.NotificationFragment, true, "")
            } else if (intent.getStringExtra("TYPE").equals("DUE", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is NearByShopsListFragment)
                    loadFragment(FragType.NearByShopsListFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("ACTIVITYDUE", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is LeadFrag)
                    loadFragment(FragType.LeadFrag, false, "")
            } else if (intent.getStringExtra("TYPE").equals("TASK", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is TaskListFragment)
                    loadFragment(FragType.TaskListFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("Msg", ignoreCase = true)) {
                if (getFragment() != null && getFragment() is ChatListFragment)
                    onBackPressed()
                val chatUser = intent.getSerializableExtra("chatUser") as ChatUserDataModel
                userName = chatUser.name
                loadFragment(FragType.ChatListFragment, true, chatUser)
            } else if (intent.getStringExtra("TYPE").equals("TIMESHEET", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is TimeSheetListFragment)
                    loadFragment(FragType.TimeSheetListFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("REIMBURSEMENT", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is ReimbursementListFragment)
                    loadFragment(FragType.ReimbursementListFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("VIDEO", ignoreCase = true)) {
                if (getFragment() != null && getFragment() !is MicroLearningListFragment)
                    loadFragment(FragType.MicroLearningListFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("lead_work", ignoreCase = true)) {
                loadFragment(FragType.LeadFrag, false, "")
            } else if (intent.getStringExtra("TYPE")
                    .equals("lms_content_assign", ignoreCase = true)
            ) {
                loadFragment(FragType.NotificationLMSFragment, false, "")
            } else if (intent.getStringExtra("TYPE").equals("clearData", ignoreCase = true)) {
                isClearData = true
                Handler().postDelayed(Runnable {
                    if (getFragment() != null && getFragment() !is LogoutSyncFragment) {
                        if (AppUtils.isOnline(this))
                            loadFragment(FragType.LogoutSyncFragment, false, "")
                        else
                            showSnackMessage(getString(R.string.no_internet))
                    }
                }, 500)
            } else if (intent.getStringExtra("TYPE").equals("LEAVE_APPLY", ignoreCase = true)) {
                var usrID = intent.getStringExtra("USER_ID")!!
                Handler().postDelayed(Runnable {
                    loadFragment(FragType.LeaveHome, false, usrID)
                }, 300)
                //loadFragment(FragType.LeaveHome, false, intent.getStringExtra("USER_ID")!!)
            } else if (intent.getStringExtra("TYPE").equals("LEAVE_STATUS", ignoreCase = true)) {
                var usrID = intent.getStringExtra("USER_ID")!!
                Handler().postDelayed(Runnable {
                    //loadFragment(FragType.LeaveHome, false, usrID)
                    loadFragment(FragType.LeaveListFragment, false, "")
                }, 300)
                //loadFragment(FragType.LeaveHome, false, intent.getStringExtra("USER_ID")!!)
            } else if (intent.getStringExtra("TYPE")
                    .equals("quotation_approval", ignoreCase = true)
            ) {
                Handler().postDelayed(Runnable {
                    if (getFragment() != null && getFragment() !is ViewAllQuotListFragment)
                        loadFragment(FragType.MemberListFragment, false, Pref.user_id!!)
                }, 700)
            } else if (intent.getStringExtra("TYPE")
                    .equals("ZERO_COLL_STATUS", ignoreCase = true)
            ) {
                Handler().postDelayed(Runnable {
                    if (getFragment() != null && getFragment() !is CollectionNotiViewPagerFrag1 && getFragment() !is CollectionNotiViewPagerFrag) {
                        SendBrod.stopBrodColl(this)
                        SendBrod.stopBrodZeroOrder(this)
                        tv_noti_count.visibility = View.GONE

                        if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification == false) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                        } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                        } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                            loadFragment(FragType.CollectionNotiViewPagerFrag2, true, "")
                        }

                    }
                }, 500)
            } else {
                showForceLogoutPopup()
            }
        }
    }

    private fun loadHomeFragment() {
        //Code by wasim
        //Pref.IsUserWiseLMSEnable = false
        //Pref.IsUserWiseLMSFeatureOnly = true
        println("tag_home_check loadHomeFragment")
        if (isFromAlarm) {
            println("tag_home_check loadHomeFragment 1")
            navigateFragmentByReportId(alarmCofifDataModel, false)
        } else if (isClearData) {
            println("tag_home_check loadHomeFragment 2")
            if (AppUtils.isOnline(this))
                loadFragment(FragType.LogoutSyncFragment, false, "")
            else
                showSnackMessage(getString(R.string.no_internet))
        } else if (Pref.IsUserWiseLMSFeatureOnly) {
            println("tag_home_check loadHomeFragment 3")
            loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)


        } else {
            println("tag_home_check loadHomeFragment 4")
            loadFragment(FragType.DashboardFragment, false, DashboardType.Home)

            if (Pref.isSefieAlarmed)
                showSelfieDialog()
        }

    }

    private fun initView() {
        ta_tv = findViewById(R.id.ta_tv)
        view_pp_dd_tv = findViewById(R.id.view_pp_dd_tv)
        iv_shopImage = findViewById(R.id.iv_shopImage)
        progress_wheel = findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        profile_name_TV = findViewById(R.id.profile_name_TV)
        profile_name_TV.text = Pref.user_name
        addOrderTV = findViewById(R.id.add_order_TV) // Home menu
        orderHistoryTV = findViewById(R.id.order_history_TV) // Timeline menu
        addTravelAllowenceTV = findViewById(R.id.add_travel_allowence_TV) // Shop menu
        settingsTV = findViewById(R.id.settings_TV) // Order menu
        myAllowRequest = findViewById(R.id.my_allowence_request_TV) // Attendance menu
        add_attendence_tv = findViewById(R.id.add_attendence_tv)
        tv_performance = findViewById(R.id.tv_performance)
        iv_delete_icon = findViewById(R.id.iv_delete_icon)
        rl_cart = findViewById(R.id.rl_cart)
        tv_cart_count = findViewById(R.id.tv_cart_count)
        iv_filter_icon = findViewById(R.id.iv_filter_icon)
        rl_confirm_btn = findViewById(R.id.rl_confirm_btn)
        tv_pp_dd_outstanding = findViewById(R.id.tv_pp_dd_outstanding)


        returnTV = findViewById(R.id.return_TV)


        /*if (AppUtils.getCurrentTimeWithMeredian() >= "8:00 AM" && AppUtils.getCurrentTimeWithMeredian() <= "10:30 AM")
 add_attendence_tv.visibility = View.VISIBLE
 else
 add_attendence_tv.visibility = View.GONE*/

        logoutTV = findViewById(R.id.logout_TV)
        bottomBar = findViewById(R.id.include_bottom_tab)
        menuMis = findViewById(R.id.mis_TV)
        login_time_tv = findViewById(R.id.login_time_tv)
        login_time_tv.text = Pref.login_time
        login_time_am_tv = findViewById(R.id.login_time_am_tv)
        profilePicture = findViewById(R.id.iv_profile_picture)
        profilePicture_adv = findViewById(R.id.iv_profile_picture_menu_adv)
        shareLogs = findViewById(R.id.share_log_TV)
        reimbursement_tv = findViewById(R.id.reimbursement_TV)
        achievement_tv = findViewById(R.id.achievement_TV)
        collection_TV = findViewById(R.id.collection_TV)
        state_report_TV = findViewById(R.id.state_report_TV) // performance menu
        iv_list_party = findViewById(R.id.iv_list_party)
        quo_TV = findViewById(R.id.quo_TV)

        tv_performance_teamMenu = findViewById(R.id.tv_performance_teamMenu)

        if ((profilePicture != null || profilePicture_adv != null) && Pref.profile_img != null && Pref.profile_img.trim()
                .isNotEmpty()
        ) {
            // Picasso.with(this).load(Pref.user_profile_img).into(profilePicture)
            /*Picasso.get()
 .load(Pref.profile_img)
 .resize(100, 100)
 .placeholder(R.drawable.ic_menu_profile_image)
 .error(R.drawable.ic_menu_profile_image)
 .into(profilePicture, object : Callback {
 override fun onSuccess() {
 }

 override fun onError(e: java.lang.Exception?) {
 e?.printStackTrace()
 //profilePicture.setImageResource(R.drawable.ic_menu_profile_image)
 }
 })*/


            try {
                Glide.with(mContext)
                    .load(Pref.profile_img)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_menu_profile_image)
                            .error(R.drawable.ic_menu_profile_image)
                    )
                    .into(profilePicture)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            try {
                Glide.with(mContext)
                    .load(Pref.profile_img)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_menu_profile_image)
                            .error(R.drawable.ic_menu_profile_image)
                    )
                    .into(profilePicture_adv)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        } else {
            profilePicture.setImageResource(R.drawable.ic_menu_profile_image)
        }

        login_time_am_tv.text = Pref.merediam
        version_name_TV = findViewById(R.id.version_name_TV)
        version_name_TV.text = AppUtils.getVersionName(this@DashboardActivity)
        iv_sync_icon = findViewById(R.id.iv_sync_icon)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerlayout)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        include_toolbar = findViewById<View>(R.id.include_toolbar)
        searchView = findViewById<MaterialSearchView>(R.id.search_view)
        iv_search_icon = findViewById<ImageView>(R.id.iv_search_icon)
        home_IV = findViewById<ImageView>(R.id.home_IV)
        add_shop_IV = findViewById<ImageView>(R.id.add_shop_IV)
        nearby_shops_IV = findViewById<ImageView>(R.id.nearby_shops_IV)
        my_orders_IV = findViewById<ImageView>(R.id.my_orders_IV)
        headerTV = findViewById<AppCustomTextView>(R.id.tv_header)
        iv_leaderboard = findViewById<ImageView>(R.id.iv_leaderboard)
        tickTV = findViewById<ImageView>(R.id.iv_tick_icon)
        logo = findViewById(R.id.logo)
        tv_noti_count = findViewById(R.id.tv_noti_count)
        maps_TV = findViewById(R.id.maps_TV)
        target_TV = findViewById(R.id.target_TV) // List of party menu
        rl_report = findViewById(R.id.rl_report)
        iv_drop_down_icon = findViewById(R.id.iv_drop_down_icon)
        ll_report_list = findViewById(R.id.ll_report_list)
        tv_attendance_report = findViewById(R.id.tv_attendance_report)
        tv_performance_report = findViewById(R.id.tv_performance_report)
        tv_visit_report = findViewById(R.id.tv_visit_report)
        meeting_TV = findViewById(R.id.meeting_TV)
        team_TV = findViewById(R.id.team_TV)
        iv_map = findViewById(R.id.iv_map)
        timesheet_TV = findViewById(R.id.timesheet_TV)
        //logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
        update_worktype_tv = findViewById(R.id.update_worktype_tv)
        dynamic_TV = findViewById(R.id.dynamic_TV)

        imageArrayList.add(home_IV)
        imageArrayList.add(add_shop_IV)
        imageArrayList.add(nearby_shops_IV)
        imageArrayList.add(my_orders_IV)

        home_TV = findViewById<AppCustomTextView>(R.id.home_TV)
        add_shop_TV = findViewById<AppCustomTextView>(R.id.add_shop_TV)
        nearby_shops_TV = findViewById<AppCustomTextView>(R.id.nearby_shops_TV)
        my_orders_TV = findViewById<AppCustomTextView>(R.id.my_orders_TV)
        nearbyShops = findViewById<AppCustomTextView>(R.id.nearby_shop_TV)
        contacts_TV = findViewById<AppCustomTextView>(R.id.contacts_TV)
        menuBeatTV =
            findViewById<AppCustomTextView>(R.id.menu_beat_TV)// 5.0 DashboardActivity AppV 4.0.6 MenuBeatFrag
        marketAssistTV = findViewById<AppCustomTextView>(R.id.menu_market_assist_TV)
        tv_pending_out_loc_menu = findViewById<AppCustomTextView>(R.id.tv_pending_out_loc_menu)
        assignedLead = findViewById<AppCustomTextView>(R.id.assigned_lead_TV)
        taskManagement =
            findViewById<AppCustomTextView>(R.id.task_management_TV)// Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023

        surveyMenu = findViewById<AppCustomTextView>(R.id.assigned_survey_TV)

        textArrayList.add(home_TV)
        textArrayList.add(add_shop_TV)
        textArrayList.add(nearby_shops_TV)
        textArrayList.add(my_orders_TV)

        tv_location = findViewById(R.id.tv_location)
        home_RL = findViewById<RelativeLayout>(R.id.home_RL)
        add_shop_RL = findViewById<RelativeLayout>(R.id.add_shop_RL)
        nearby_shops_RL = findViewById<RelativeLayout>(R.id.nearby_shops_RL)
        my_orders_RL = findViewById<RelativeLayout>(R.id.my_orders_RL)
        iv_home_icon = findViewById(R.id.iv_home_icon)
        add_scheduler_email_verification = findViewById(R.id.add_scheduler_email_verification)
        add_bookmark = findViewById(R.id.add_bookmark)
        tv_saved_count = findViewById(R.id.tv_saved_count)
        add_template = findViewById(R.id.add_template)
        iv_home_icon.setOnClickListener(this)
        add_scheduler_email_verification.setOnClickListener(this)
        add_bookmark.setOnClickListener(this)
        add_template.setOnClickListener(this)
        tv_change_pwd = findViewById(R.id.tv_change_pwd)
        all_team_TV = findViewById(R.id.all_team_TV)
        achv_TV = findViewById(R.id.achv_TV)
        targ_achv_TV = findViewById(R.id.targ_achv_TV)
        leave_tv = findViewById(R.id.leave_tv) //Apply Leave menu
        task_TV = findViewById(R.id.task_TV)
        activity_TV = findViewById(R.id.activity_TV)
        rl_collection = findViewById(R.id.rl_collection)
        iv_collection_drop_down_icon = findViewById(R.id.iv_collection_drop_down_icon)
        ll_collection_list = findViewById(R.id.ll_collection_list)
        tv_report = findViewById(R.id.tv_report)
        tv_entry = findViewById(R.id.tv_entry)
        share_loc_TV = findViewById(R.id.share_loc_TV)
        iv_settings = findViewById(R.id.iv_settings)
        weather_TV = findViewById(R.id.weather_TV)
        doc_TV = findViewById(R.id.doc_TV)
        chat_bot_TV = findViewById(R.id.chat_bot_TV)
        distributor_wise_order_list_TV = findViewById(R.id.distributor_wise_order_list_TV)
        alert_snack_bar = findViewById(R.id.alert_snack_bar)
        ic_calendar = findViewById(R.id.ic_calendar)
        ic_chat_bot = findViewById(R.id.ic_chat_bot)
        iv_cancel_chat = findViewById(R.id.iv_cancel_chat)
        chat_TV = findViewById(R.id.chat_TV)
        iv_people = findViewById(R.id.iv_people)
        tv_confirm_btn = findViewById(R.id.tv_confirm_btn)
        iv_scan = findViewById(R.id.iv_scan)
        iv_view_text = findViewById(R.id.iv_view_text)
        scan_TV = findViewById(R.id.scan_TV)
        nearby_user_TV = findViewById(R.id.nearby_user_TV) // Near by team member menu
        fl_net_status = findViewById(R.id.fl_net_status)
        home_loc_TV = findViewById(R.id.home_loc_TV)
        device_info_TV = findViewById(R.id.device_info_TV)
        permission_info_TV = findViewById(R.id.permission_info_TV)
        // anydesk_info_TV = findViewById(R.id.anydesk_info_TV)
        // screen_record_info_TV = findViewById(R.id.screen_record_info_TV)
        check_custom_status_TV = findViewById(R.id.check_custom_status_TV)
        micro_learning_TV = findViewById(R.id.micro_learning_TV)
        my_learning_TV = findViewById(R.id.my_learning_TV)

        photo_registration = findViewById(R.id.photo_registration)
        photo_team_attendance = findViewById(R.id.photo_team_attendance)

        tv_clear_attendance = findViewById(R.id.tv_clear_attendance)
        tb_auto_revisit_menu = findViewById(R.id.tb_auto_revisit_menu)// 1.0 AppV 4.0.6
        tb_auto_revisit_menu.setOnClickListener(this)// 1.0 AppV 4.0.6
        tb_total_visit_menu =
            findViewById(R.id.tb_total_visit_menu)// 3.0 AppV 4.0.6 DashboardActivity
        leaderBoard_TV = findViewById(R.id.leaderBoard_TV)
        tb_total_visit_menu.setOnClickListener(this)// 3.0 AppV 4.0.6 DashboardActivity

        surveyMenu.text = Pref.surveytext

        privacy_policy_tv_menu =
            findViewById(R.id.privacy_policy_tv_menu)// 14.0 DashboardActivity AppV 4.0.8 mantis 0025783 In-app privacy policy working in menu & Login
        privacy_policy_tv_menu.setOnClickListener(this)// 14.0 DashboardActivity AppV 4.0.8 mantis 0025783 In-app privacy policy working in menu & Login
        privacy_policy_tv_menu.setOnClickListener(this)// 14.0 DashboardActivity AppV 4.0.8 mantis 0025783 In-app privacy policy working in menu & Login

        home_RL.setOnClickListener(this)
        add_shop_RL.setOnClickListener(this)
        nearby_shops_RL.setOnClickListener(this)
        my_orders_RL.setOnClickListener(this)
        addOrderTV.setOnClickListener(this)
        orderHistoryTV.setOnClickListener(this)
        addTravelAllowenceTV.setOnClickListener(this)
        settingsTV.setOnClickListener(this)
        myAllowRequest.setOnClickListener(this)
        logoutTV.setOnClickListener(this)
        menuMis.setOnClickListener(this)
        tickTV.setOnClickListener(this)
        logo.setOnClickListener(this)
        nearbyShops.setOnClickListener(this)
        contacts_TV.setOnClickListener(this)
        marketAssistTV.setOnClickListener(this)
        menuBeatTV.setOnClickListener(this)// 5.0 DashboardActivity AppV 4.0.6 MenuBeatFrag
        tv_pending_out_loc_menu.setOnClickListener(this)
        assignedLead.setOnClickListener(this)
        taskManagement.setOnClickListener(this)// Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023
        surveyMenu.setOnClickListener(this)
        shareLogs.setOnClickListener(this)
        reimbursement_tv.setOnClickListener(this)
        achievement_tv.setOnClickListener(this)
        iv_search_icon.setOnClickListener(this)
        profilePicture.setOnClickListener(this)
        profilePicture_adv.setOnClickListener(this)
        maps_TV.setOnClickListener(this)
        iv_sync_icon.setOnClickListener(this)
        add_attendence_tv.setOnClickListener(this)
        ta_tv.setOnClickListener(this)
        view_pp_dd_tv.setOnClickListener(this)
        tv_performance.setOnClickListener(this)
        iv_delete_icon.setOnClickListener(this)
        iv_filter_icon.setOnClickListener(this)
        rl_confirm_btn.setOnClickListener(this)
        tv_pp_dd_outstanding.setOnClickListener(this)
        tv_location.setOnClickListener(this)
        //collection_TV.setOnClickListener(this)
        state_report_TV.setOnClickListener(this)
        target_TV.setOnClickListener(this)
        iv_list_party.setOnClickListener(this)
        rl_report.setOnClickListener(this)
        tv_visit_report.setOnClickListener(this)
        tv_performance_report.setOnClickListener(this)
        tv_attendance_report.setOnClickListener(this)
        meeting_TV.setOnClickListener(this)
        team_TV.setOnClickListener(this)
        iv_map.setOnClickListener(this)
        timesheet_TV.setOnClickListener(this)
        tv_change_pwd.setOnClickListener(this)
        quo_TV.setOnClickListener(this)
        all_team_TV.setOnClickListener(this)
        update_worktype_tv.setOnClickListener(this)
        achv_TV.setOnClickListener(this)
        targ_achv_TV.setOnClickListener(this)
        leave_tv.setOnClickListener(this)
        task_TV.setOnClickListener(this)
        dynamic_TV.setOnClickListener(this)
        activity_TV.setOnClickListener(this)
        rl_collection.setOnClickListener(this)
        tv_report.setOnClickListener(this)
        tv_entry.setOnClickListener(this)
        share_loc_TV.setOnClickListener(this)
        iv_settings.setOnClickListener(this)
        weather_TV.setOnClickListener(this)
        doc_TV.setOnClickListener(this)
        chat_bot_TV.setOnClickListener(this)
        distributor_wise_order_list_TV.setOnClickListener(this)
        ic_calendar.setOnClickListener(this)
        ic_chat_bot.setOnClickListener(this)
        iv_cancel_chat.setOnClickListener(this)
        chat_TV.setOnClickListener(this)
        iv_people.setOnClickListener(this)
        iv_scan.setOnClickListener(this)
        iv_view_text.setOnClickListener(this)
        scan_TV.setOnClickListener(this)
        nearby_user_TV.setOnClickListener(this)
        fl_net_status.setOnClickListener(this)
        home_loc_TV.setOnClickListener(this)
        device_info_TV.setOnClickListener(this)
        permission_info_TV.setOnClickListener(this)
        // anydesk_info_TV.setOnClickListener(this)
        // screen_record_info_TV.setOnClickListener(this)
        check_custom_status_TV.setOnClickListener(this)
        micro_learning_TV.setOnClickListener(this)
        my_learning_TV.setOnClickListener(this)

        photo_registration.setOnClickListener(this)
        photo_team_attendance.setOnClickListener(this)
        assignedLead.setOnClickListener(this)
        surveyMenu.setOnClickListener(this)
        tv_clear_attendance.setOnClickListener(this)

        tv_performance_teamMenu.setOnClickListener(this)

        drawerLL = findViewById(R.id.activity_dashboard_lnr_lyt_slide_view)
        drawerLL.setOnClickListener(this)

        rl_cart.setOnClickListener(this) //06-09-2021

        returnTV.setOnClickListener(this)
        leaderBoard_TV.setOnClickListener(this)

        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setPadding(0, toolbar.paddingTop, 0, toolbar.paddingBottom)
        toolbar.setTitle(R.string.blank)
        toolbar.setSubtitle(R.string.blank)

        simpleDialogProcess = Dialog(mContext)
        simpleDialogProcess.setCancelable(false)
        simpleDialogProcess.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialogProcess.setContentView(R.layout.dialog_message)
        dialogHeaderProcess =
            simpleDialogProcess.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        dialog_yes_no_headerTVProcess =
            simpleDialogProcess.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView


        mDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.blank, R.string.blank
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                /*if (slideOffset == 0.toFloat()
 && getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
 // drawer closed
 getActionBar()
 .setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 invalidateOptionsMenu();
 } else if (slideOffset != 0.toFloat()
 && getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
 // started opening
 AppUtils.hideSoftKeyboard(this@DashboardActivity)
 getActionBar()
 .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
 invalidateOptionsMenu();
 }*/
                AppUtils.hideSoftKeyboard(this@DashboardActivity)

                if (getFragment() != null && getFragment() is ReimbursementListFragment) {
                    if ((getFragment() as ReimbursementListFragment).mPopupWindow != null && (getFragment() as ReimbursementListFragment).mPopupWindow!!.isShowing)
                        (getFragment() as ReimbursementListFragment).mPopupWindow?.dismiss()

                    if ((getFragment() as ReimbursementListFragment).conveyancePopupWindow != null &&
                        (getFragment() as ReimbursementListFragment).conveyancePopupWindow!!.isShowing
                    )
                        (getFragment() as ReimbursementListFragment).conveyancePopupWindow?.dismiss()
                }

                iv_drop_down_icon.isSelected = false
                ll_report_list.visibility = View.GONE

                iv_collection_drop_down_icon.isSelected = false
                ll_collection_list.visibility = View.GONE

                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.isDrawerIndicatorEnabled = true
        mDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            Log.d("tag_drawerclick", "")
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        mDrawerToggle.syncState()
        mDrawerToggle.isDrawerSlideAnimationEnabled = true
        toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
// if (searchListener==null)
// return false
// searchListener?.onSearchQueryListener(query)
// if(getCurrentFragType()== FragType.NearByShopsListFragment){
// (getFragment() as NearByShopsListFragment).setSearchListener(object : SearchListener {
// override fun onSearchQueryListener(query: String) {
// Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show()
// }
//
// })
// }
// Toast.makeText(this@DashboardActivity, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
// if(newText.isEmpty()){
//// searchView.clearSuggestions()
// return false
// }
// Toast.makeText(this@DashboardActivity, newText, Toast.LENGTH_SHORT).show()
                if (searchListener == null)
                    return false
                searchListener?.onSearchQueryListener(newText.trim())


// val arr = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopBySearchData(newText)
// searchView.addSuggestions(arr)
// Toast.makeText(this@DashboardActivity, newText, Toast.LENGTH_SHORT).show()
                return false
            }
        })

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech.setLanguage(Locale.US)

                if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED)
                    Log.e("Dashboard Activity", "TTS Language is not supported!")
                else
                    Log.e("Dashboard Activity", "TTS Language Supported.")

                Log.e("Dashboard Activity", "TTS Initialization success.")
            } else
                Log.e("Dashboard Activity", "TTS Initialization failed!")
        })


        tv_menuVersion = findViewById(R.id.tv_menu_adv_version)
        etSearchMenu = findViewById(R.id.et_search_menu)
        ll_menuLogout = findViewById(R.id.ll_menu_adv_logout)
        rv_menu = findViewById(R.id.rv_menu)
        menuName = findViewById(R.id.tv_menu_adv_name)
        menu_loginTime = findViewById(R.id.tv_menu_adv_login_time)
        cv_menu_adv_voice = findViewById(R.id.cv_menu_adv_voice)

        ll_menuLogout.setOnClickListener {
            logout_TV.performClick()
        }
        cv_menu_adv_voice.setOnClickListener {
            val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            Handler().postDelayed(Runnable {
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
            }, 1000)
            try {
                startActivityForResult(intent, 7009)
                Handler().postDelayed(Runnable {
                }, 3000)

            } catch (a: ActivityNotFoundException) {
                a.printStackTrace()
            }
        }

        etSearchMenu.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapterMenuAdv!!.getFilter().filter(etSearchMenu.text.toString().trim())
            }
        })

        setNewMenu()
    }

    fun setNewMenu() {
        //new menu work
        menuName.text = Pref.user_name
        menu_loginTime.text = "Last login time : " + Pref.login_time
        tv_menuVersion.text = AppUtils.getVersionName(this@DashboardActivity)

        menuItems.clear()

        menuItems.add(MenuItems("Home", R.drawable.ic_home_adv))
        if (AppUtils.getSharedPreferencesIsFaceDetectionOn(mContext)) {
            menuItems.add(MenuItems("Photo Registration", R.drawable.ic_photo_registration_adv))
        }
        if (Pref.IsTeamAttendance) {
            menuItems.add(MenuItems("Team Attendance", R.drawable.ic_team_attendance_adv))
        }
        if (Pref.ShowAutoRevisitInAppMenu) {
            menuItems.add(MenuItems("Revisit", R.drawable.ic_revisit_adv))
        }
        if (Pref.ShowTotalVisitAppMenu) {
            menuItems.add(MenuItems("Total Visit", R.drawable.ic_total_visit_adv))
        }
        if (Pref.ShowAttednaceClearmenu) {
            menuItems.add(MenuItems("Attendance Clear", R.drawable.ic_attendance_clear_adv))
        }
        if (Pref.isChatBotShow) {
            menuItems.add(MenuItems("BreezeBot", R.drawable.ic_breezebot_adv))
        }
        if (Pref.Showdistributorwisepartyorderreport) {
            menuItems.add(MenuItems("Distributor Wise Order List", R.drawable.ic_distributor_adv))
        }
        if (Pref.isChangePasswordAllowed) {
            menuItems.add(MenuItems("Change Password", R.drawable.ic_change_password_adv))
        }
        if (Pref.IsShowMenuAddAttendance) {
            menuItems.add(MenuItems("Add Attendance", R.drawable.ic_add_attendance_adv))
        }
        if (Pref.IsShowMenuAttendance) {
            menuItems.add(MenuItems("View Attendance", R.drawable.ic_view_attendance_adv))
        }
        var isShowUpdateWOrkType = if (!Pref.isAddAttendence)
            false
        else {
            if (Pref.isOnLeave.equals("true", ignoreCase = true))
                false
            else {
                if (Pref.isUpdateWorkTypeEnable)
                    true
                else
                    false
            }
        }
        if (isShowUpdateWOrkType) {
            menuItems.add(MenuItems("Update Your Work Type", R.drawable.ic_breezebot_adv))
        }
        if (Pref.isLeaveEnable) {
            menuItems.add(MenuItems("Apply Leave", R.drawable.ic_apply_leave_adv))
        }
        if (!Pref.isServiceFeatureEnable) {
            menuItems.add(MenuItems(Pref.shopText + "(s)", R.drawable.ic_shops_adv))
        }
        if (Pref.isShowNearbyCustomer) {
            menuItems.add(
                MenuItems(
                    "Nearby " + Pref.shopText + "(s)",
                    R.drawable.ic_nearby_shops_adv
                )
            )
        }
        if (Pref.IsShowMenuCRMContacts) {
            menuItems.add(MenuItems("CRM", R.drawable.ic_crm_adv))
        }
        if (Pref.IsShowBeatInMenu) {
            menuItems.add(MenuItems("Beat", R.drawable.ic_beat_adv))
        }
        if (Pref.IsMenuShowAIMarketAssistant) {
            menuItems.add(MenuItems("Market Assistant", R.drawable.ic_market_assistant_adv))
        }
        if (Pref.IsmanualInOutTimeRequired) {
            menuItems.add(MenuItems("Pending Out Location", R.drawable.ic_pending_loc_adv))
        }
        if (Pref.IsMenuSurveyEnabled) {
            menuItems.add(MenuItems(Pref.surveytext, R.drawable.ic_survey_adv))
        }
        if (Pref.ShowUserwiseLeadMenu) {
            menuItems.add(MenuItems("Assigned Lead", R.drawable.ic_assigned_lead_adv))
        }
        if (Pref.IsTaskManagementAvailable) {
            menuItems.add(MenuItems("Task Management", R.drawable.ic_task_manag_adv))
        }
        if (Pref.IsShowNearByTeam) {
            menuItems.add(MenuItems("Nearby Team Members", R.drawable.ic_nearby_team_adv))
        }
        if (Pref.IsShowLeaderBoard) {
            menuItems.add(MenuItems("Leaderboard", R.drawable.ic_leaderboard_adv))
        }
        if (Pref.willActivityShow) {
            menuItems.add(MenuItems("Activities", R.drawable.ic_activities_adv))
        }
        if (Pref.isShowTimeline) {
            menuItems.add(MenuItems("Timelines", R.drawable.ic_timeline_adv))
        }
        if (Pref.IsShowEmployeePerformanceGlobal && Pref.IsShowEmployeePerformance) {
            menuItems.add(MenuItems("Performance Insights", R.drawable.ic_performance_insights_adv))
        }
        if (Pref.isQuotationShow) {
            menuItems.add(MenuItems("Quotation", R.drawable.ic_quotation_adv))
        }
        if (Pref.isOrderShow || (Pref.ShowPartyWithCreateOrder && Pref.ShowUserwisePartyWithCreateOrder)) {
            menuItems.add(MenuItems("Orders", R.drawable.ic_orders_adv))
        }
        if (Pref.isCollectioninMenuShow) {
            menuItems.add(MenuItems("Collection", R.drawable.ic_collection_adv))
        }
        if (Pref.willKnowYourStateShow) {
            menuItems.add(MenuItems("Performance Status", R.drawable.ic_performance_adv))
        }
        if (Pref.IsShowMenuOutstanding_Details_PP_DD) {
            menuItems.add(MenuItems("Outstanding Details - PP/DD", R.drawable.ic_rank_adv))
        }
        if (Pref.IsShowMenuMIS_Report) {
            menuItems.add(MenuItems("MIS Reports", R.drawable.ic_report_adv))
        }
        if (Pref.willShowUpdateDayPlan) {
            menuItems.add(MenuItems(Pref.dailyPlanListHeaderText, R.drawable.ic_calendar_adv))
        }
        if (Pref.willShowTeamDetails) {
            menuItems.add(MenuItems("Team Details", R.drawable.ic_team_adv))
        }
        if (Pref.isAllTeamAvailable) {
            menuItems.add(MenuItems("All Team - Online", R.drawable.ic_team_online))
        }
        if (Pref.willTimesheetShow) {
            menuItems.add(MenuItems("Timesheet", R.drawable.ic_timesheet_adv))
        }
        if (Pref.isTaskEnable) {
            menuItems.add(MenuItems("Task", R.drawable.ic_task_adv))
        }
        if (Pref.willReportShow) {
            menuItems.add(MenuItems("Reports", R.drawable.ic_reports_adv))
        }
        if (Pref.willReimbursementShow) {
            menuItems.add(MenuItems("Reimbursement", R.drawable.ic_reimb_adv))
        }
        if (Pref.willDynamicShow) {
            menuItems.add(MenuItems("Dynamic", R.drawable.ic_dynamics_adv))
        }
        if (Pref.IsShowMenuMap_View) {
            menuItems.add(MenuItems("Map View", R.drawable.ic_map_view_adv))
        }
        if (AppUtils.getSharedPreferenceslogShareinLogin(mContext)) {
            menuItems.add(MenuItems("Share Log", R.drawable.ic_share_log_adv))
        }
        if (Pref.IsShowMenuShare_Location) {
            menuItems.add(MenuItems("Share My Location", R.drawable.ic_share_loc_adv))
        }
        if (Pref.IsShowMenuHome_Location) {
            menuItems.add(MenuItems("View Home Location", R.drawable.ic_home_addr_adv))
        }
        if (Pref.isDocumentRepoShow) {
            menuItems.add(MenuItems("Document Repository", R.drawable.ic_doc_repo_adv))
        }
        if (Pref.IsShowMenuChat) {
            menuItems.add(MenuItems("Chat", R.drawable.ic_chat_adv))
        }
        if (Pref.IsShowMenuScan_QR_Code) {
            menuItems.add(MenuItems("Scan QR Code", R.drawable.ic_scan_qr_adv))
        }
        if (Pref.isAppInfoEnable) {
            menuItems.add(MenuItems("Device Info", R.drawable.ic_device_info_adv))
        }
        if (Pref.IsShowMenuPermission_Info) {
            menuItems.add(MenuItems("Permission Info", R.drawable.ic_permission_adv))
        }
        if (Pref.isShowMicroLearning) {
            menuItems.add(MenuItems("Micro Learning", R.drawable.ic_micro_learning_adv))
        }
        if (Pref.IsUserWiseLMSEnable) {
            menuItems.add(MenuItems("LMS", R.drawable.ic_learning_adv))
        }
        if (Pref.IsUserWiseLMSFeatureOnly) {
            menuItems.clear()
            menuItems.add(MenuItems("Home", R.drawable.ic_home_adv))
            menuItems.add(MenuItems("LMS", R.drawable.ic_learning_adv))
        }
        if (Pref.IsShowPrivacyPolicyInMenu) {
            menuItems.add(MenuItems("Privacy Policy", R.drawable.ic_privacy_policy))
        }


        //sub items
        var menuSubObjReports = MenuSubItems()
        menuSubObjReports.parentMenuName = "Reports"
        if (Pref.willAttendanceReportShow) {
            menuSubObjReports.subList.add(MenuItems("Attendance Report", 0))
        }
        if (Pref.willPerformanceReportShow) {
            menuSubObjReports.subList.add(MenuItems("Performance Report", 0))
        }
        if (Pref.willVisitReportShow) {
            menuSubObjReports.subList.add(MenuItems("View Visit Report", 0))
        }
        if (Pref.isMeetingAvailable) {
            menuSubObjReports.subList.add(MenuItems("Meeting Details", 0))
        }
        if (Pref.isAchievementEnable) {
            menuSubObjReports.subList.add(MenuItems("Achv. Report", 0))
        }
        if (Pref.isTarVsAchvEnable) {
            menuSubObjReports.subList.add(MenuItems("Targ. vs Achv. Report", 0))
        }

        menuSubItemsL.add(menuSubObjReports)

        var menuSubObjCollection = MenuSubItems()
        menuSubObjCollection.parentMenuName = "Collection"
        menuSubObjCollection.subList.add(MenuItems("Collection Report", 0))
        menuSubObjCollection.subList.add(MenuItems("Entry", 0))

        menuSubItemsL.add(menuSubObjCollection)

        var menuSubObjLms = MenuSubItems()
        menuSubObjLms.parentMenuName = "LMS"
        menuSubObjLms.subList.add(MenuItems("LMS Dashboard", 0))
        menuSubObjLms.subList.add(MenuItems("My Learning", 0))
        menuSubObjLms.subList.add(MenuItems("All Topics", 0))
        menuSubObjLms.subList.add(MenuItems("My Topics", 0))
        menuSubObjLms.subList.add(MenuItems("My Performance", 0))
        menuSubObjLms.subList.add(MenuItems("Leaderboard", 0))

        menuSubItemsL.add(menuSubObjLms)

        adapterMenuAdv =
            AdapterMenuAdv(this, menuItems, menuSubItemsL, object : AdapterMenuAdv.OnClick {
                override fun onClick(obj: MenuItems) {
                    progress_wheel.spin()
                    if (obj.name.equals("Home", ignoreCase = true)) {
                        add_order_TV.performClick()
                    }
                    if (obj.name.equals("Photo Registration", ignoreCase = true)) {
                        photo_registration.performClick()
                    }
                    if (obj.name.equals("Team Attendance", ignoreCase = true)) {
                        photo_team_attendance.performClick()
                    }
                    if (obj.name.equals("Revisit", ignoreCase = true)) {
                        tb_auto_revisit_menu.performClick()
                    }
                    if (obj.name.equals("Total Visit", ignoreCase = true)) {
                        tb_total_visit_menu.performClick()
                    }
                    if (obj.name.equals("Attendance Clear", ignoreCase = true)) {
                        tv_clear_attendance.performClick()
                    }
                    if (obj.name.equals("BreezeBot", ignoreCase = true)) {
                        chat_bot_TV.performClick()
                    }
                    if (obj.name.equals("Distributor Wise Order List", ignoreCase = true)) {
                        distributor_wise_order_list_TV.performClick()
                    }
                    if (obj.name.equals("Change Password", ignoreCase = true)) {
                        tv_change_pwd.performClick()
                    }
                    if (obj.name.equals("Add Attendance", ignoreCase = true)) {
                        add_attendence_tv.performClick()
                    }
                    if (obj.name.equals("View Attendance", ignoreCase = true)) {
                        my_allowence_request_TV.performClick()
                    }
                    if (obj.name.equals("Update Your Work Type", ignoreCase = true)) {
                        update_worktype_tv.performClick()
                    }
                    if (obj.name.equals("Apply Leave", ignoreCase = true)) {
                        leave_tv.performClick()
                    }
                    if (obj.name.equals(Pref.shopText + "(s)", ignoreCase = true)) {
                        add_travel_allowence_TV.performClick()
                    }
                    if (obj.name.equals("Nearby " + Pref.shopText + "(s)", ignoreCase = true)) {
                        nearby_shop_TV.performClick()
                    }
                    if (obj.name.equals("CRM", ignoreCase = true)) {
                        contacts_TV.performClick()
                    }
                    if (obj.name.equals("Beat", ignoreCase = true)) {
                        menu_beat_TV.performClick()
                    }
                    if (obj.name.equals("Market Assistant", ignoreCase = true)) {
                        menu_market_assist_TV.performClick()
                    }
                    if (obj.name.equals("Pending Out Location", ignoreCase = true)) {
                        tv_pending_out_loc_menu.performClick()
                    }
                    if (obj.name.equals(Pref.surveytext, ignoreCase = true)) {
                        assigned_survey_TV.performClick()
                    }
                    if (obj.name.equals("Assigned Lead", ignoreCase = true)) {
                        assigned_lead_TV.performClick()
                    }
                    if (obj.name.equals("Task Management", ignoreCase = true)) {
                        task_management_TV.performClick()
                    }
                    if (obj.name.equals("Nearby Team Members", ignoreCase = true)) {
                        nearby_user_TV.performClick()
                    }
                    if (obj.name.equals("Leaderboard", ignoreCase = true) && obj.icon != 0) {
                        leaderBoard_TV.performClick()
                    }
                    if (obj.name.equals("Activities", ignoreCase = true)) {
                        activity_TV.performClick()
                    }
                    if (obj.name.equals("Timelines", ignoreCase = true)) {
                        order_history_TV.performClick()
                    }
                    if (obj.name.equals("Performance Insights", ignoreCase = true)) {
                        tv_performance_teamMenu.performClick()
                    }
                    if (obj.name.equals("Quotation", ignoreCase = true)) {
                        quo_TV.performClick()
                    }
                    if (obj.name.equals("Orders", ignoreCase = true)) {
                        settings_TV.performClick()
                    }
                    if (obj.name.equals("Collection", ignoreCase = true)) {
                        rl_collection.performClick()
                    }
                    if (obj.name.equals("Performance Status", ignoreCase = true)) {
                        state_report_TV.performClick()
                    }
                    if (obj.name.equals("Outstanding Details - PP/DD", ignoreCase = true)) {
                        state_report_TV.performClick()
                    }
                    if (obj.name.equals("MIS Reports", ignoreCase = true)) {
                        mis_TV.performClick()
                    }
                    if (obj.name.equals(Pref.dailyPlanListHeaderText, ignoreCase = true)) {
                        tv_pp_dd_outstanding.performClick()
                    }
                    if (obj.name.equals("Team Details", ignoreCase = true)) {
                        team_TV.performClick()
                    }
                    if (obj.name.equals("All Team - Online", ignoreCase = true)) {
                        all_team_TV.performClick()
                    }
                    if (obj.name.equals("Timesheet", ignoreCase = true)) {
                        timesheet_TV.performClick()
                    }
                    if (obj.name.equals("Task", ignoreCase = true)) {
                        task_TV.performClick()
                    }
                    if (obj.name.equals("Reports", ignoreCase = true)) {
                        rl_report.performClick()
                    }
                    //sub menu report
                    if (obj.name.equals("Attendance Report", ignoreCase = true)) {
                        tv_attendance_report.performClick()
                    }
                    if (obj.name.equals("Performance Report", ignoreCase = true)) {
                        tv_performance_report.performClick()
                    }
                    if (obj.name.equals("View Visit Report", ignoreCase = true)) {
                        tv_visit_report.performClick()
                    }
                    if (obj.name.equals("Meeting Details", ignoreCase = true)) {
                        meeting_TV.performClick()
                    }
                    if (obj.name.equals("Achv. Report", ignoreCase = true)) {
                        achv_TV.performClick()
                    }
                    if (obj.name.equals("Targ. vs Achv. Report", ignoreCase = true)) {
                        targ_achv_TV.performClick()
                    }
                    if (obj.name.equals("Collection Report", ignoreCase = true)) {
                        tv_report.performClick()
                    }
                    if (obj.name.equals("Entry", ignoreCase = true)) {
                        tv_entry.performClick()
                    }


                    if (obj.name.equals("Reimbursement", ignoreCase = true)) {
                        reimbursement_TV.performClick()
                    }
                    if (obj.name.equals("Dynamic", ignoreCase = true)) {
                        dynamic_TV.performClick()
                    }
                    if (obj.name.equals("Map View", ignoreCase = true)) {
                        maps_TV.performClick()
                    }
                    if (obj.name.equals("Share Log", ignoreCase = true)) {
                        share_log_TV.performClick()
                    }
                    if (obj.name.equals("Share My Location", ignoreCase = true)) {
                        share_loc_TV.performClick()
                    }
                    if (obj.name.equals("View Home Location", ignoreCase = true)) {
                        home_loc_TV.performClick()
                    }
                    if (obj.name.equals("Document Repository", ignoreCase = true)) {
                        doc_TV.performClick()
                    }
                    if (obj.name.equals("Chat", ignoreCase = true)) {
                        chat_TV.performClick()
                    }
                    if (obj.name.equals("Scan QR Code", ignoreCase = true)) {
                        scan_TV.performClick()
                    }
                    if (obj.name.equals("Device Info", ignoreCase = true)) {
                        device_info_TV.performClick()
                    }
                    if (obj.name.equals("Permission Info", ignoreCase = true)) {
                        permission_info_TV.performClick()
                    }
                    if (obj.name.equals("Micro Learning", ignoreCase = true)) {
                        micro_learning_TV.performClick()
                    }
                    /* if(obj.name.equals("LMS",ignoreCase = true)){
 my_learning_TV.performClick()
 }*/
                    if (obj.name.equals("My Topics", ignoreCase = true)) {
                        loadFragment(FragType.SearchLmsFrag, false, "")
                    }
                    if (obj.name.equals("LMS Dashboard", ignoreCase = true)) {
                        loadFragment(FragType.MyLearningFragment, false, "")
                    }
                    if (obj.name.equals("My Learning", ignoreCase = true)) {
                        loadFragment(FragType.MyLearningTopicList, false, "")
                    }
                    /*if (obj.name.equals("My Learning", ignoreCase = true)) {
                        loadFragment(FragType.MyLearningTopicList, false, "")
                    }*/
                    if (obj.name.equals("All Topics", ignoreCase = true)) {
                        loadFragment(FragType.SearchLmsKnowledgeFrag, false, "")
                    }
                    if (obj.name.equals("Leaderboard", ignoreCase = true) && obj.icon == 0) {
                        loadFragment(FragType.LeaderboardLmsFrag, false, "")
                    }
                    if (obj.name.equals("My Performance", ignoreCase = true) && obj.icon == 0) {
                        loadFragment(FragType.MyPerformanceFrag, false, "")
                    }
                    if (obj.name.equals("Privacy Policy", ignoreCase = true)) {
                        privacy_policy_tv_menu.performClick()
                    }

                    Handler().postDelayed(Runnable {
                        progress_wheel.stopSpinning()
                    }, 200)
                }
            }, {
                it
            })
        rv_menu.adapter = adapterMenuAdv

    }

    fun updateUI() {
        rl_report.apply {
            visibility = if (Pref.willReportShow)
                View.VISIBLE
            else
                View.GONE
        }


        addTravelAllowenceTV.text = Pref.shopText + "(s)"
        nearbyShops.text = "Nearby " + Pref.shopText + "(s)"

        /*if (Pref.isReplaceShopText) {
 addTravelAllowenceTV.text = getString(R.string.customers)
 nearbyShops.text = getString(R.string.nearby_customer)
 } else {
 addTravelAllowenceTV.text = getString(R.string.shops)
 nearbyShops.text = getString(R.string.nearby_shops)
 }*/


        if (Pref.isMeetingAvailable)
            meeting_TV.visibility = View.VISIBLE
        else
            meeting_TV.visibility = View.GONE

        if (Pref.willShowTeamDetails)
            team_TV.visibility = View.VISIBLE
        else
            team_TV.visibility = View.GONE

        if (Pref.willReimbursementShow)
            reimbursement_tv.visibility = View.VISIBLE
        else
            reimbursement_tv.visibility = View.GONE

        if (Pref.willShowUpdateDayPlan) {
            target_TV.visibility = View.VISIBLE
            target_TV.text = Pref.dailyPlanListHeaderText
        } else
            target_TV.visibility = View.GONE

        if (Pref.isCollectioninMenuShow)
            rl_collection.visibility = View.VISIBLE
        else
            rl_collection.visibility = View.GONE

        if (Pref.willKnowYourStateShow)
            state_report_TV.visibility = View.VISIBLE
        else
            state_report_TV.visibility = View.GONE

        if (Pref.willAttendanceReportShow)
            tv_attendance_report.visibility = View.VISIBLE
        else
            tv_attendance_report.visibility = View.GONE

        if (Pref.willPerformanceReportShow)
            tv_performance_report.visibility = View.VISIBLE
        else
            tv_performance_report.visibility = View.GONE

        if (Pref.willVisitReportShow)
            tv_visit_report.visibility = View.VISIBLE
        else
            tv_visit_report.visibility = View.GONE

        if (Pref.willTimesheetShow)
            timesheet_TV.visibility = View.VISIBLE
        else
            timesheet_TV.visibility = View.GONE

        if (Pref.isOrderShow || (Pref.ShowPartyWithCreateOrder && Pref.ShowUserwisePartyWithCreateOrder))
            settingsTV.visibility = View.VISIBLE
        else
            settingsTV.visibility = View.GONE

        if (Pref.IsShowBeatInMenu) {
            menuBeatTV.visibility = View.VISIBLE
        } else {
            menuBeatTV.visibility = View.GONE
        }
        if (Pref.IsMenuShowAIMarketAssistant) {
            marketAssistTV.visibility = View.VISIBLE
        } else {
            marketAssistTV.visibility = View.GONE
        }

        if (Pref.isVisitShow) {
            if (!Pref.isServiceFeatureEnable)
                nearbyShops.visibility = View.VISIBLE
            else
                nearbyShops.visibility = View.GONE

            addTravelAllowenceTV.visibility = View.VISIBLE
        } else {
            addTravelAllowenceTV.visibility = View.GONE
            nearbyShops.visibility = View.GONE
        }

        if (Pref.isAttendanceFeatureOnly) {
            //addTravelAllowenceTV.visibility = View.GONE
            //nearbyShops.visibility = View.GONE
            logoutTV.text = getString(R.string.logout)
            //settingsTV.visibility = View.GONE
        } else {
            //addTravelAllowenceTV.visibility = View.VISIBLE
            //nearbyShops.visibility = View.VISIBLE
            logoutTV.text = getString(R.string.sync_logout)
            //settingsTV.visibility = View.VISIBLE
        }

        if (Pref.isChangePasswordAllowed)
            tv_change_pwd.visibility = View.VISIBLE
        else
            tv_change_pwd.visibility = View.GONE


        if (Pref.isQuotationShow)
            quo_TV.visibility = View.VISIBLE
        else
            quo_TV.visibility = View.GONE

        if (Pref.isAllTeamAvailable)
            all_team_TV.visibility = View.VISIBLE
        else
            all_team_TV.visibility = View.GONE

        update_worktype_tv.apply {
            visibility = if (!Pref.isAddAttendence)
                View.GONE
            else {
                if (Pref.isOnLeave.equals("true", ignoreCase = true))
                    View.GONE
                else {
                    if (Pref.isUpdateWorkTypeEnable)
                        View.VISIBLE
                    else
                        View.GONE
                }
            }
        }

        if (Pref.isLeaveEnable)
            leave_tv.visibility = View.VISIBLE
        else
            leave_tv.visibility = View.GONE

        if (Pref.isAchievementEnable)
            achv_TV.visibility = View.VISIBLE
        else
            achv_TV.visibility = View.GONE

        if (Pref.isTarVsAchvEnable)
            targ_achv_TV.visibility = View.VISIBLE
        else
            targ_achv_TV.visibility = View.GONE

        if (Pref.isTaskEnable)
            task_TV.visibility = View.VISIBLE
        else
            task_TV.visibility = View.GONE

        if (Pref.willDynamicShow)
            dynamic_TV.visibility = View.VISIBLE
        else
            dynamic_TV.visibility = View.GONE

        if (Pref.willActivityShow)
            activity_TV.visibility = View.VISIBLE
        else
            activity_TV.visibility = View.GONE

        if (Pref.isDocumentRepoShow)
            doc_TV.visibility = View.VISIBLE
        else
            doc_TV.visibility = View.GONE


        if (Pref.isChatBotShow)
            chat_bot_TV.visibility = View.VISIBLE
        else
            chat_bot_TV.visibility = View.GONE

        if (Pref.Showdistributorwisepartyorderreport)
            distributor_wise_order_list_TV.visibility = View.VISIBLE
        else
            distributor_wise_order_list_TV.visibility = View.GONE



        if (Pref.isShowTimeline)
            orderHistoryTV.visibility = View.VISIBLE
        else
            orderHistoryTV.visibility = View.GONE

        if (Pref.isAppInfoEnable)
            device_info_TV.visibility = View.VISIBLE
        else
            device_info_TV.visibility = View.GONE

        if (Pref.isShowMicroLearning)
            micro_learning_TV.visibility = View.VISIBLE
        else
            micro_learning_TV.visibility = View.GONE

        if (Pref.IsUserWiseLMSEnable)
            my_learning_TV.visibility = View.VISIBLE
        else
            my_learning_TV.visibility = View.GONE

        if (Pref.IsShowNearByTeam)
            nearby_user_TV.visibility = View.VISIBLE
        else
            nearby_user_TV.visibility = View.GONE

        if (Pref.isShowNearbyCustomer)
            nearby_shop_TV.visibility = View.VISIBLE
        else
            nearby_shop_TV.visibility = View.GONE

        if (Pref.IsShowMenuCRMContacts)
            contacts_TV.visibility = View.VISIBLE
        else
            contacts_TV.visibility = View.GONE

        if (Pref.IsmanualInOutTimeRequired)
            tv_pending_out_loc_menu.visibility = View.VISIBLE
        else
            tv_pending_out_loc_menu.visibility = View.GONE


        /* var launchIntent: Intent? = packageManager.getLaunchIntentForPackage("com.anydesk.anydeskandroid")
 if(launchIntent!=null){
 anydesk_info_TV.text="Open Anydesk"
 }else{
 anydesk_info_TV.text="Install Anydesk"
 }*/

        if (AppUtils.getSharedPreferenceslogShareinLogin(mContext)) {
            shareLogs.visibility = View.VISIBLE
        } else {
            shareLogs.visibility = View.GONE
        }
        /*29-10-2021 Team Attendance*/
        if (Pref.IsTeamAttendance) {
            //if(AppUtils.getSharedPreferencesIsFaceDetectionOn(mContext)){
            photo_team_attendance.visibility = View.VISIBLE
        } else {
            photo_team_attendance.visibility = View.GONE
        }

        /*Clear Attendance*/
        if (Pref.ShowAttednaceClearmenu) {
            tv_clear_attendance.visibility = View.VISIBLE
        } else {
            tv_clear_attendance.visibility = View.GONE
        }
        // 1.0 AppV 4.0.6
        if (Pref.ShowAutoRevisitInAppMenu) {
            tb_auto_revisit_menu.visibility = View.VISIBLE

        } else {
            tb_auto_revisit_menu.visibility = View.GONE
        }
        // 3.0 AppV 4.0.6 DashboardActivity
        if (Pref.ShowTotalVisitAppMenu) {
            tb_total_visit_menu.visibility = View.VISIBLE

        } else {
            tb_total_visit_menu.visibility = View.GONE
        }

        if (Pref.IsShowLeaderBoard) {
            leaderBoard_TV.visibility = View.VISIBLE

        } else {
            leaderBoard_TV.visibility = View.GONE
        }


        if (AppUtils.getSharedPreferencesIsFaceDetectionOn(mContext)) {
            photo_registration.visibility = View.VISIBLE
// photo_team_attendance.visibility=View.VISIBLE
        } else {
            photo_registration.visibility = View.GONE
// photo_team_attendance.visibility=View.GONE
        }
        //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7
        /* if(AppUtils.getSharedPreferencesIsScreenRecorderEnable(mContext)){
 screen_record_info_TV.visibility=View.VISIBLE
 }else{
 screen_record_info_TV.visibility=View.GONE
 }*/
        //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7


        if (Pref.IsShowMenuAddAttendance) {
            add_attendence_tv.visibility = View.VISIBLE
        } else {
            add_attendence_tv.visibility = View.GONE
        }
        if (Pref.IsShowMenuAttendance) {
            myAllowRequest.visibility = View.VISIBLE
        } else {
            myAllowRequest.visibility = View.GONE
        }
        if (Pref.IsShowMenuMIS_Report) {
            mis_TV.visibility = View.VISIBLE
        } else {
            mis_TV.visibility = View.GONE
        }
        /* if(Pref.IsShowMenuAnyDesk){
 anydesk_info_TV.visibility=View.VISIBLE
 }else{
 anydesk_info_TV.visibility=View.GONE
 }*/
        if (Pref.IsShowPrivacyPolicyInMenu) {
            privacy_policy_tv_menu.visibility = View.VISIBLE
        } else {
            privacy_policy_tv_menu.visibility = View.GONE
        }
        if (Pref.IsShowMenuPermission_Info) {
            permission_info_TV.visibility = View.VISIBLE
        } else {
            permission_info_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuScan_QR_Code) {
            scan_TV.visibility = View.VISIBLE
        } else {
            scan_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuChat) {
            chat_TV.visibility = View.VISIBLE
        } else {
            chat_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuWeather_Details) {
            weather_TV.visibility = View.VISIBLE
        } else {
            weather_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuHome_Location) {
            home_loc_TV.visibility = View.VISIBLE
        } else {
            home_loc_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuShare_Location) {
            share_loc_TV.visibility = View.VISIBLE
        } else {
            share_loc_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuMap_View) {
            maps_TV.visibility = View.VISIBLE
        } else {
            maps_TV.visibility = View.GONE
        }
        if (Pref.IsShowMenuReimbursement) {
            reimbursement_tv.visibility = View.VISIBLE
        } else {
            reimbursement_tv.visibility = View.GONE
        }
        if (Pref.IsShowMenuOutstanding_Details_PP_DD) {
            tv_pp_dd_outstanding.visibility = View.VISIBLE
        } else {
            tv_pp_dd_outstanding.visibility = View.GONE
        }
        if (Pref.IsShowMenuStock_Details_PP_DD) {
            view_pp_dd_tv.visibility = View.VISIBLE
        } else {
            view_pp_dd_tv.visibility = View.GONE
        }

        /*21-12-2021*/
        if (Pref.isOrderShow) {
            if (Pref.IsReturnEnableforParty) {
                returnTV.visibility = View.VISIBLE
            } else {
                returnTV.visibility = View.GONE
            }
        } else {
            returnTV.visibility = View.GONE
        }
        /*02-03-2022*/
        if (Pref.ShowUserwiseLeadMenu) {
            assignedLead.visibility = View.VISIBLE
        } else {
            assignedLead.visibility = View.GONE
        }

        // Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023
        if (Pref.IsTaskManagementAvailable) {
            taskManagement.visibility = View.VISIBLE
        } else {
            taskManagement.visibility = View.GONE
        }
        // end rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023

        if (Pref.IsMenuSurveyEnabled) {
            surveyMenu.visibility = View.VISIBLE
        } else {
            surveyMenu.visibility = View.GONE
        }
        // 16.0 DashboardActivity AppV 4.0.8 saheli mantis 25860 performnace module
        if (Pref.IsShowEmployeePerformanceGlobal) {
            if (Pref.IsShowEmployeePerformance) {
                tv_performance_teamMenu.visibility = View.VISIBLE
            } else {
                tv_performance_teamMenu.visibility = View.GONE
            }
        } else {
            tv_performance_teamMenu.visibility = View.GONE
        }
        // end rev 16.0 mantis 25860
        // 1.0 AppV 4.0.6
        if (Pref.ShowAutoRevisitInAppMenu)
            tb_auto_revisit_menu.visibility = View.VISIBLE
        else
            tb_auto_revisit_menu.visibility = View.GONE
        //val frag: DashboardFragment? = supportFragmentManager.findFragmentByTag("DashboardFragment") as DashboardFragment?


        Handler().postDelayed(Runnable {
            if (getFragment() != null && getFragment() is DashboardFragment) {
                if (Pref.isScanQrForRevisit)
                    iv_scan.visibility = View.VISIBLE
                else
                    iv_scan.visibility = View.GONE

                if (!Pref.isAttendanceFeatureOnly)
                    logo.visibility = View.VISIBLE
                else
                    logo.visibility = View.GONE
            }
        }, 500)

        setNewMenu()
    }

    private fun showOrderCollectionAlert(isOrderAdded: Boolean, isCollectionAdded: Boolean) {

        try {

            if (isForceLogout)
                return

            var header = AppUtils.hiFirstNameText() + "!"
            var body = ""

            if (!isOrderAdded && !isCollectionAdded) {
                //header = AppUtils.hiFirstNameText()
                body = "No order or collection synced till now. Thanks."
            } else if (!isOrderAdded) {
                //header = AppUtils.hiFirstNameText()
                body = "No order synced till now. Thanks."
            } else if (!isCollectionAdded) {
                //header = AppUtils.hiFirstNameText()
                body = "No collection synced till now. Thanks."
            }

            if (orderCollectionAlertDialog != null) {
                orderCollectionAlertDialog?.dismissAllowingStateLoss()
                orderCollectionAlertDialog = null
            }

            orderCollectionAlertDialog = CommonDialogSingleBtn.getInstance(
                header,
                body,
                getString(R.string.ok),
                object : OnDialogClickListener {
                    override fun onOkClick() {
                        isOrderDialogShow = false
                    }
                })//.show(supportFragmentManager, "CommonDialogSingleBtn")


            Timber.e("Order Alert Dialog show time====> " + AppUtils.getCurrentTime())

            orderCollectionAlertDialog?.show(supportFragmentManager, "CommonDialogSingleBtn")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    public fun setProfileImg() {
        try {
            if ((profilePicture != null || profilePicture_adv != null) && Pref.profile_img != null && Pref.profile_img.trim()
                    .isNotEmpty()
            ) {
                //Picasso.with(this).load(Pref.profile_img).into(profilePicture)
                /*Picasso.get()
 .load(Pref.profile_img)
 .resize(100, 100)
 .into(profilePicture)*/

                try {
                    Glide.with(mContext)
                        .load(Pref.profile_img)
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_menu_profile_image)
                                .error(R.drawable.ic_menu_profile_image)
                        )
                        .into(profilePicture)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    Glide.with(mContext)
                        .load(Pref.profile_img)
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_menu_profile_image)
                                .error(R.drawable.ic_menu_profile_image)
                        )
                        .into(profilePicture_adv)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (profile_name_TV != null && Pref.user_name != null && Pref.user_name!!.trim()
                    .isNotEmpty()
            ) {
                profile_name_TV.text = Pref.user_name
            }
            if (menuName != null && Pref.user_name != null && Pref.user_name!!.trim()
                    .isNotEmpty()
            ) {
                menuName.text = Pref.user_name
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.activity_dashboard_lnr_lyt_slide_view -> {
                //Toast.makeText(this, "asdasf", Toast.LENGTH_LONG).show()
            }

            R.id.home_RL -> {
                deSelectAll()
                home_IV.isSelected = true
                home_TV.isSelected = true
                loadFragment(FragType.HomeFragment, false, "")
            }

            R.id.add_shop_RL -> {
                deSelectAll()
                add_shop_IV.isSelected = true
                add_shop_TV.isSelected = true
                loadFragment(FragType.AddShopFragment, true, "")
            }

            R.id.nearby_shops_RL -> {
                deSelectAll()
                nearby_shops_IV.isSelected = true
                nearby_shops_TV.isSelected = true
                isShopFromChatBot = false
                loadFragment(FragType.NearByShopsListFragment, false, "")
            }

            R.id.my_orders_RL -> {
                deSelectAll()
                my_orders_IV.isSelected = true
                my_orders_TV.isSelected = true
                loadFragment(FragType.MyOrderListFragment, false, "")
            }

            R.id.add_order_TV -> {
// check("")
                if (Pref.IsUserWiseLMSFeatureOnly) {
                    loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)
                } else {
                    Log.d("login_test_calling2", "")
                    loadFragment(FragType.DashboardFragment, false, DashboardType.Home)
                }
            }

            R.id.order_history_TV -> {
                isMemberMap = false
                if (!Pref.willTimelineWithFixedLocationShow)
                    loadFragment(FragType.OrderhistoryFragment, false, "")
                else
                    loadFragment(FragType.TimeLineFragment, false, "")
            }

            R.id.add_travel_allowence_TV -> {
                deSelectAll()

                if (!Pref.isShowShopBeatWise) {
                    isShopFromChatBot = false
                    if (!Pref.isServiceFeatureEnable)
                        loadFragment(FragType.NearByShopsListFragment, false, "")
                    else
                        loadFragment(FragType.CustomerListFragment, false, "")
                } else
                    loadFragment(FragType.BeatListFragment, false, "")
            }

            R.id.my_allowence_request_TV -> {
                deSelectAll()
                isChatBotAttendance = false
                loadFragment(FragType.AttendanceFragment, false, "")
            }

            R.id.add_attendence_tv -> {

                var inTime = ""
                var outTime = ""

                /*if (AppUtils.getCurrentTimeWithMeredian().contains("AM") || AppUtils.getCurrentTimeWithMeredian().contains("PM")) {
 inTime = "8:00 AM"
 outTime = "11:59 PM"
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("a.m.") || AppUtils.getCurrentTimeWithMeredian().contains("p.m.")) {
 inTime = "8:00 a.m."
 outTime = "11:59 p.m."
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("am") || AppUtils.getCurrentTimeWithMeredian().contains("pm")) {
 inTime = "8:00 am"
 outTime = "11:59 pm"
 } else if (AppUtils.getCurrentTimeWithMeredian().contains("A.M.") || AppUtils.getCurrentTimeWithMeredian().contains("P.M.")) {
 inTime = "8:00 A.M."
 outTime = "11:59 P.M."
 }

 if (AppUtils.convertDateTimeWithMeredianToLong(AppUtils.getCurrentTimeWithMeredian()) >= AppUtils.convertDateTimeWithMeredianToLong(inTime)
 && AppUtils.convertDateTimeWithMeredianToLong(AppUtils.getCurrentTimeWithMeredian()) <= AppUtils.convertDateTimeWithMeredianToLong(outTime)) {*/
                if (Pref.isAddAttendence)
                    (mContext as DashboardActivity).showSnackMessage("${AppUtils.hiFirstNameText()}!. Attendance already marked for the day.")
                else {
                    /*if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
 if (Pref.isHomeLocAvailable) {

 if (!TextUtils.isEmpty(Pref.home_latitude) && !TextUtils.isEmpty(Pref.home_longitude)) {
 val distance = LocationWizard.getDistance(Pref.home_latitude.toDouble(), Pref.home_longitude.toDouble(), Pref.current_latitude.toDouble(),
 Pref.current_longitude.toDouble())

 Timber.e("Distance from home====> $distance")

 if (distance * 1000 > 50)
 loadFragment(FragType.AddAttendanceFragment, false, "")
 else
 (mContext as DashboardActivity).showSnackMessage("Attendance can not be added from home")
 } else {
 Timber.e("========Home location is not available========")
 loadFragment(FragType.AddAttendanceFragment, false, "")
 }

 } else {
 Timber.e("========isHomeLocAvailable is false========")
 loadFragment(FragType.AddAttendanceFragment, false, "")
 }
 } else {
 Timber.e("========Current location is not available========")*/


                    val attendanceReq = AttendanceRequest()
                    attendanceReq.user_id = Pref.user_id!!
                    attendanceReq.session_token = Pref.session_token
                    attendanceReq.start_date = AppUtils.getCurrentDateForCons()
                    attendanceReq.end_date = AppUtils.getCurrentDateForCons()

                    val repository = AttendanceRepositoryProvider.provideAttendanceRepository()
                    progress_wheel.spin()
                    BaseActivity.compositeDisposable.add(
                        repository.getAttendanceList(attendanceReq)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val attendanceList = result as AttendanceResponse
                                if (attendanceList.status == "205") {
                                    progress_wheel.stopSpinning()
                                    loadFragment(FragType.AddAttendanceFragment, true, "")
                                } else if (attendanceList.status == NetworkConstant.SUCCESS) {
                                    progress_wheel.stopSpinning()
                                    Pref.isAddAttendence = true
                                    (mContext as DashboardActivity).showSnackMessage("${AppUtils.hiFirstNameText()}. Attendance already marked for the day.")
                                }

                            }, { error ->
                                progress_wheel.stopSpinning()
                                error.printStackTrace()
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            })
                    )


                    //loadFragment(FragType.AddAttendanceFragment, false, "")
                    //}
                }
                /*} else
 showSnackMessage("Attendance can be added only between 8:00 AM and 11:59 PM")*/

                //loadFragment(FragType.AddAttendanceFragment, false, "")
            }

            R.id.tv_performance -> {
                loadFragment(FragType.PerformanceFragment, true, "")
            }

            R.id.ta_tv -> {
                //loadFragment(FragType.ViewAllTAListFragment, false, "")
                showSnackMessage(getString(R.string.functionality_disabled))
            }

            R.id.view_pp_dd_tv -> {
                //loadFragment(FragType.ViewPPDDListFragment, false, false)
                showSnackMessage(getString(R.string.functionality_disabled))
            }

            R.id.tv_pp_dd_outstanding -> {
                loadFragment(FragType.ViewPPDDListOutstandingFragment, false, true)
            }

            R.id.settings_TV -> {
                //showSnackMessage(getString(R.string.functionality_disabled))
                //09-09-2021
                isOrderFromChatBot = false
                if (Pref.IsActivateNewOrderScreenwithSize) {
                    CustomStatic.IsOrderFromTotalOrder = false
                    loadFragment(FragType.NewOdrScrListFragment, false, "")
                } else {
                    //loadFragment(FragType.NewOrderListFragment, false, "")
                    // 19.0 DashboardFragment v 4.2.6 Suman 03/05/2024 mantis 27424 Order show update
                    if (Pref.isOrderShow && Pref.ShowUserwisePartyWithCreateOrder) {
                        (mContext as DashboardActivity).loadFragment(
                            FragType.NewOrderListFragment,
                            false,
                            ""
                        )
                    } else if (Pref.isOrderShow && !Pref.ShowUserwisePartyWithCreateOrder) {
                        (mContext as DashboardActivity).loadFragment(
                            FragType.NewOrderListFragment,
                            false,
                            ""
                        )
                    } else if (!Pref.isOrderShow && Pref.ShowUserwisePartyWithCreateOrder) {
                        (mContext as DashboardActivity).loadFragment(
                            FragType.ViewNewOrdHisAllFrag,
                            false,
                            ""
                        )
                    }
                }

            }

            R.id.return_TV -> {
                loadFragment(FragType.NewReturnListFragment, false, "")
            }

            R.id.state_report_TV -> {
                loadFragment(FragType.KnowYourStateFragment, false, "")
            }

            R.id.logout_TV -> {
                //performLogout()

                if (Pref.IsUserWiseLMSFeatureOnly) {
                    CommonDialog.getInstance(
                        AppUtils.hiFirstNameText() + "!",
                        getString(R.string.confirm_logout),
                        getString(R.string.cancel),
                        getString(R.string.ok),
                        object : CommonDialogClickListener {
                            override fun onLeftClick() {

                            }

                            override fun onRightClick(editableData: String) {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).clearData()
                                startActivity(
                                    Intent(
                                        mContext as DashboardActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            }

                        }).show((mContext as DashboardActivity).supportFragmentManager, "")

                    return
                }

                Pref.IsAutoLogoutFromBatteryCheck = false
                if (Pref.DayEndMarked == false && Pref.IsShowDayEnd == true && Pref.DayStartMarked) {
                    showSnackMessage("Please mark Day End before logout. Thanks.")
                } else {
                    if (Pref.isAttendanceFeatureOnly)
                        performLogout()
                    else {
                        isClearData = false
                        if (AppUtils.isOnline(this@DashboardActivity)) {
                            Handler().postDelayed(Runnable {
                                //callShopDurationApi()
                                //rectifyUnknownLoc()
                                syncGpsNetData()
                            }, 350)
                            //loadFragment(FragType.LogoutSyncFragment, true, "")
                        } else
                            showSnackMessage("Good internet must required to sync all data, please switch on the internet and proceed.")
                    }
                }

            }

            //14.0 DashboardActivity AppV 4.0.8 mantis 0025783 In-app privacy policy working in menu & Login
            R.id.privacy_policy_tv_menu -> {
                loadFragment(FragType.PrivacypolicyWebviewFrag, false, "")
            }

            /*28-12-2022*/
            // 1.0 AppV 4.0.6
            // Revisit feature from menu to handle
            R.id.tb_auto_revisit_menu -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                if (!Pref.isAddAttendence && false) {
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                    return
                } else {
                    if (Pref.IsShowDayStart && !Pref.DayStartMarked) {
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_message)
                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                        val dialog_yes_no_headerTV =
                            simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()
                        dialogHeader.text = "Please start your day..."
                        val dialogYes =
                            simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()
                    } else {
                        progress_wheel.spin()
                        revisit_ll.isEnabled = false
                        //checkAutoRevisit()

                        var loc: Location = Location("")
                        loc.latitude = Pref.current_latitude.toDouble()
                        loc.longitude = Pref.current_longitude.toDouble()

                        //manual single revisit
                        //checkAutoRevisitManual(loc)

                        Handler().postDelayed(Runnable {
                            checkAutoRevisitAll()
                        }, 250)
                    }
                }

            }
            // 3.0 AppV 4.0.6 DashboardActivity
            R.id.tb_total_visit_menu -> {
                (mContext as DashboardActivity).loadFragment(FragType.AverageShopFragment, true, "")
            }

            R.id.mis_TV -> {
                loadFragment(FragType.ReportFragment, false, "")
            }

            R.id.tv_location -> {
                //loadFragment(FragType.LocationListFragment, false, "")
            }

            R.id.iv_tick_icon -> {
                if (getCurrentFragType() == FragType.AddShopFragment) {
                    (getFragment() as AddShopFragment).validateAndSaveData()
                }
            }

            R.id.logo -> {
                /*if (getFragment() != null && getFragment() is AddAttendanceFragment && !isAddAttendaceAlert)
 loadFragment(FragType.DashboardFragment, false, "")
 else*/ /*if (getFragment() != null && getFragment() !is GpsDisableFragment && !isAddAttendaceAlert &&
 getFragment() !is OrderTypeListFragment && getFragment() !is CartFragment && getFragment() !is VisitReportFragment && getFragment() !is AttendanceReportFragment
 && getFragment() !is PerformanceReportFragment && getFragment() !is VisitReportDetailsFragment)
 loadFragment(FragType.DashboardFragment, false, "")*/

                Log.d("notification", "")
                println(
                    "load fragg ${Pref.IsCollectionOrderWise} ${Pref.ShowCollectionAlert} ${Pref.ShowZeroCollectioninAlert} ${Pref.ShowCollectionOnlywithInvoiceDetails} ${Pref.IsPendingCollectionRequiredUnderTeam}" +
                            " ${Pref.IsCollectionEntryConsiderOrderOrInvoice}"
                );


                Pref.IsPendingColl = false
                Pref.IsZeroOrder = false
                SendBrod.stopBrodColl(this)
                SendBrod.stopBrodZeroOrder(this)
                SendBrod.stopBrodDOBDOA(this)
                tv_noti_count.visibility = View.GONE

                Pref.NotiCountFlag = false

                //Pref.IsCollectionOrderWise = true
                //Pref.ShowCollectionAlert = true
                //Pref.ShowZeroCollectioninAlert = false
                //Pref.ShowCollectionOnlywithInvoiceDetails = false
                //Pref.IsPendingCollectionRequiredUnderTeam = false
                //Pref.IsCollectionEntryConsiderOrderOrInvoice = false
                //Pref.IsShowRepeatOrderinNotification = true

                if (Pref.IsUserWiseLMSFeatureOnly) {
                    //Toast.makeText(this, "hiiii", Toast.LENGTH_SHORT).show()
                    loadFragment(FragType.NotificationLMSFragment, true, "")
                    return
                }

                if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification == false) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag, true, "")
                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag1, true, "")
                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                    loadFragment(FragType.CollectionNotiViewPagerFrag2, true, "")
                } else {
                    loadFragment(FragType.NotificationFragment, true, "")
                }

// loadFragment(FragType.NotificationFragment, true, "")
                //showSnackMessage("Under Development")
            }

            R.id.iv_home_icon -> {
                CustomStatic.IsHomeClick = true
                // CustomStatic.IsLMSLeaderboardClick = true
                if (getFragment() != null && (getFragment() is ViewAllOrderListFragment || getFragment() is NotificationFragment) && (ShopDetailFragment.isOrderEntryPressed || AddShopFragment.isOrderEntryPressed)
                    && AppUtils.getSharedPreferenceslogOrderStatusRequired(this)
                ) {


                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!
                        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialog_yes_no)
                    val dialogYes =
                        simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                    val dialogNo =
                        simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                    dialogYes.setOnClickListener({ view ->
                        simpleDialog.cancel()
                        val dialog = Dialog(mContext)
                        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setContentView(R.layout.dialog_cancel_order_status)

                        val user_name =
                            dialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                        val order_status =
                            dialog.findViewById(R.id.tv_cancel_order_status) as AppCustomTextView
                        val cancel_remarks =
                            dialog.findViewById(R.id.et_cancel_order_remarks) as AppCustomEditText
                        val submitRemarks =
                            dialog.findViewById(R.id.tv_cancel_order_submit_remarks) as AppCustomTextView

                        order_status.text = "Failure"
                        user_name.text = "Hi" + Pref.user_name + "!"

                        submitRemarks.setOnClickListener(View.OnClickListener { view ->
                            if (!TextUtils.isEmpty(cancel_remarks.text.toString().trim())) {
                                //Toast.makeText(mContext,cancel_remarks.text.toString(),Toast.LENGTH_SHORT).show()
                                val obj = OrderStatusRemarksModelEntity()
                                //obj.shop_id= mShopId
                                obj.shop_id = ViewAllOrderListFragment.mSShopID_Str.toString()
                                obj.user_id = Pref.user_id
                                obj.order_status = order_status.text.toString()
                                obj.order_remarks = cancel_remarks!!.text!!.toString()
                                obj.visited_date_time = AppUtils.getCurrentDateTime()
                                obj.visited_date = AppUtils.getCurrentDateForShopActi()
                                obj.isUploaded = false

                                var shopAll = AppDatabase.getDBInstance()!!.shopActivityDao()
                                    .getShopActivityAll()
                                if (shopAll.size == 1) {
                                    obj.shop_revisit_uniqKey = shopAll.get(0).shop_revisit_uniqKey
                                } else if (shopAll.size != 0) {
                                    obj.shop_revisit_uniqKey =
                                        shopAll.get(shopAll.size - 1).shop_revisit_uniqKey
                                }
                                if (shopAll.size != 0)
                                    AppDatabase.getDBInstance()?.shopVisitOrderStatusRemarksDao()!!
                                        .insert(obj)
                                dialog.dismiss()

                                if (ShopDetailFragment.isOrderEntryPressed) {
                                    ShopDetailFragment.isOrderEntryPressed = false
                                }
                                if (AddShopFragment.isOrderEntryPressed) {
                                    AddShopFragment.isOrderEntryPressed = false
                                }

                                Log.d("login_test_calling3", "")

                                loadFragment(FragType.DashboardFragment, false, "")

                                Handler().postDelayed(Runnable {
                                    (getFragment() as DashboardFragment).updateItem()
                                }, 500)

                            } else {
                                submitRemarks.setError("Enter Remarks")
                                submitRemarks.requestFocus()
                            }

                        })
                        dialog.show()
                    })
                    dialogNo.setOnClickListener({ view ->
                        simpleDialog.cancel()
                    })
                    simpleDialog.show()


                }
                /*else if (Pref.IsUserWiseLMSFeatureOnly){
 loadFragment(FragType.MyLearningFragment, false, "")
 }*/
                else {
                    Log.d("login_test_calling4", "")
                    if (Pref.IsUserWiseLMSFeatureOnly) {
                        try {
                            //(getFragment() as VideoPlayLMS).callDestroy()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        Handler().postDelayed(Runnable {
                            loadFragment(FragType.MyLearningFragment, false, "")
                        }, 500)

                    } else {
                        loadFragment(FragType.DashboardFragment, false, "")

                        Handler().postDelayed(Runnable {
                            (getFragment() as DashboardFragment).updateItem()
                        }, 500)
                    }
                }

            }

            R.id.add_bookmark -> {
                loadFragment(FragType.BookmarkFrag, true, "")
            }

            //code begin by puja 22-01-24
            R.id.add_scheduler_email_verification -> {

                (getFragment() as SchedulerAddFormFrag).showDialogGmailAuthPic()
                return


                var instructionDialog = Dialog(mContext)
                instructionDialog!!.setCancelable(false)
                instructionDialog!!.getWindow()!!
                    .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction)
                val tvHeader =
                    instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
                val tv_instruction =
                    instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
                val tv_save_instruction =
                    instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
                val et_user_gmail_id =
                    instructionDialog!!.findViewById(R.id.et_user_gmail_id) as EditText
                val et_user_password =
                    instructionDialog!!.findViewById(R.id.et_user_password) as EditText
                val tv_headerOfSetVerification =
                    instructionDialog!!.findViewById(R.id.tv_headerOfSetVerification) as TextView
                val rvContactGrName =
                    instructionDialog!!.findViewById(R.id.rv_dialog_cont_gr) as RecyclerView
                val iv_close =
                    instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView
                val iv_email_info =
                    instructionDialog!!.findViewById(R.id.iv_email_info) as ImageView
                val iv_dialog_instruction_copy =
                    instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
                val lin_credentials_gmail =
                    instructionDialog!!.findViewById(R.id.lin_credentials_gmail) as LinearLayout
                iv_dialog_instruction_copy.visibility = View.GONE
                iv_email_info.visibility = View.VISIBLE

                iv_dialog_instruction_copy.setOnClickListener {
                    val clipboard: ClipboardManager? =
                        mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
                    clipboard!!.setPrimaryClip(clip)
                    Toaster.msgLong(mContext, "Copied to Clipboard")
                }

                iv_email_info.setOnClickListener {

                    showInstructionOfTwoStepVerification()

                }

                tv_save_instruction.setOnClickListener {
                    if (et_user_gmail_id.text.toString()
                            .equals("") && et_user_password.text.toString().trim().equals("")
                    ) {
                        Toast.makeText(
                            mContext,
                            "Put your credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Pref.storeGmailId = et_user_gmail_id.text.toString().trim()
                        Pref.storeGmailPassword = et_user_password.text.toString().trim()
                        // After save 2 step verification
                        // (mContext as DashboardActivity).loadFragment(FragType.SchedulerViewFrag, true, "")
                        instructionDialog!!.dismiss()

                    }
                }
                iv_close.setOnClickListener {
                    instructionDialog!!.dismiss()
                }
                rvContactGrName.visibility = View.GONE
                lin_credentials_gmail.visibility = View.VISIBLE
                tv_save_instruction.visibility = View.VISIBLE
                tv_headerOfSetVerification.visibility = View.VISIBLE
                tv_instruction.visibility = View.GONE
                tvHeader.text = "E-mail configuration"
                instructionDialog!!.show()
            }
            //code end by puja 22-01-24

            R.id.add_template -> {
                (mContext as DashboardActivity).loadFragment(FragType.TemplateViewFrag, true, "")
            }

            R.id.leaderBoard_TV -> {
                (mContext as DashboardActivity).loadFragment(
                    FragType.LeaderBoardFrag,
                    false,
                    ""
                )
            }


            //19-08-21 nearBy shop visit stop untill daystart
            R.id.nearby_shop_TV -> {
                isChatBotLocalShop = false
                if (Pref.IsShowDayStart) {
                    if (!Pref.isAddAttendence) {
                        (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                        return
                    } else {
                        if (!Pref.DayStartMarked) {
                            val simpleDialog = Dialog(mContext)
                            simpleDialog.setCancelable(false)
                            simpleDialog.getWindow()!!
                                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            simpleDialog.setContentView(R.layout.dialog_message)
                            val dialogHeader =
                                simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                            val dialog_yes_no_headerTV =
                                simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                            dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText() + "!"
                            dialogHeader.text = "Please start your day..."
                            val dialogYes =
                                simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                            dialogYes.setOnClickListener({ view ->
                                simpleDialog.cancel()
                            })
                            simpleDialog.show()
// (mContext as DashboardActivity).showSnackMessage("Please start your day")
                        } else {
                            loadFragment(FragType.LocalShopListFragment, false, "")
                        }
                    }
                } else {
                    loadFragment(FragType.LocalShopListFragment, false, "")
                }
            }
            // 5.0 DashboardActivity AppV 4.0.6 MenuBeatFrag

            R.id.contacts_TV -> {
                //if (!Pref.isAddAttendence)
                //(mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                //else
                loadFragment(FragType.ContactsFrag, false, "")
            }

            R.id.menu_beat_TV -> {
                if (!Pref.isAddAttendence) {
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                    return
                } else {
                    loadFragment(FragType.MenuBeatFrag, false, "")
                }
            }

            R.id.menu_market_assist_TV -> {
                //loadFragment(FragType.ShopListMarketAssistFrag, true, "")
                loadFragment(FragType.MarketAssistTabFrag, false, "")
            }

            R.id.tv_pending_out_loc_menu -> {
                if (!Pref.isAddAttendence) {
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                    return
                } else {
                    loadFragment(FragType.PendingOutLocationFrag, false, "")
                }
            }

            R.id.assigned_lead_TV -> {

                if (!Pref.isAddAttendence)
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                else
                    loadFragment(FragType.LeadFrag, false, "")
            }
            //Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023
            R.id.task_management_TV -> {
                if (!Pref.isAddAttendence)
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                else
                    loadFragment(FragType.TaskManagementFrag, false, "")
            }
            // end Rev 19.0 DashboardActivity AppV 4.0.8 saheli mantis 0026023
            R.id.assigned_survey_TV -> {

                if (!Pref.isAddAttendence)
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                else
                    loadFragment(FragType.SurveyFrag, false, "")
            }
// R.id.nearby_shop_TV -> {
// isChatBotLocalShop = false
// loadFragment(FragType.LocalShopListFragment, false, "")
// }
            R.id.share_log_TV -> {
                /*if(Build.VERSION.SDK_INT>=30){
 if (!Environment.isExternalStorageManager()){
 fileManagePermi()
 }else{
 openShareIntents()
 }
 }else{
 openShareIntents()
 }*/
                openShareIntents()
            }

            R.id.iv_search_icon -> {
                searchView.openSearch()
            }

            R.id.iv_profile_picture, R.id.iv_profile_picture_menu_adv -> {
                loadFragment(FragType.MyProfileFragment, false, "")
            }

            R.id.maps_TV -> {
                isMapFromDrawer = true
                loadFragment(FragType.NearByShopsMapFragment, false, "")
            }

            R.id.iv_sync_icon -> {
                when {
                    getCurrentFragType() == FragType.NearByShopsMapFragment -> (getFragment() as NearByShopsMapFragment).fetchCurrentLocation()
                    // 5.0 NearByShopsListFragment AppV 4.0.6 Suman 03-02-2023 updateModifiedShop + sendModifiedShopList for shop update mantis 25624
                    //getCurrentFragType() == FragType.NearByShopsListFragment -> (getFragment() as NearByShopsListFragment).refreshShopList()
                    getCurrentFragType() == FragType.NearByShopsListFragment -> (getFragment() as NearByShopsListFragment).checkModifiedShop()
                    // End of rev 5.0
                    getCurrentFragType() == FragType.OrderTypeListFragment -> (getFragment() as OrderTypeListFragment).refreshProductList()
                    getCurrentFragType() == FragType.ReturnTypeListFragment -> (getFragment() as ReturnTypeListFragment).refreshProductList()
                    getCurrentFragType() == FragType.NewOrderListFragment -> (getFragment() as NewOrderListFragment).refreshOrderList()
                    getCurrentFragType() == FragType.AddShopFragment -> (getFragment() as AddShopFragment).refreshList()
                    getCurrentFragType() == FragType.ShopDetailFragment -> (getFragment() as ShopDetailFragment).refreshList()
                    getCurrentFragType() == FragType.OfflineMemberListFragment -> (getFragment() as OfflineMemberListFragment).refreshList()
                    getCurrentFragType() == FragType.OfflineAllShopListFragment -> (getFragment() as OfflineAllShopListFragment).refreshList()
                    getCurrentFragType() == FragType.OfflineShopListFragment -> (getFragment() as OfflineShopListFragment).refreshList()
                    getCurrentFragType() == FragType.TimeSheetListFragment -> (getFragment() as TimeSheetListFragment).refreshList()
                    getCurrentFragType() == FragType.DashboardFragment -> (getFragment() as DashboardFragment).refresh()

                    getCurrentFragType() == FragType.NewReturnListFragment -> (getFragment() as NewReturnListFragment).refreshOrderList()
                    getCurrentFragType() == FragType.MapViewForTeamFrag -> (getFragment() as MapViewForTeamFrag).refreshMap()
                    getCurrentFragType() == FragType.ContactsFrag -> (getFragment() as ContactsFrag).checkModifiedShop()
                    getCurrentFragType() == FragType.AddOpptFrag -> (getFragment() as AddOpptFrag).checkModifiedstatusProduct()
                }
            }

            R.id.iv_delete_icon -> {
                if (getCurrentFragType() == FragType.OrderTypeListFragment) {
                    (getFragment() as OrderTypeListFragment).goToNextScreen()
                }
                if (getCurrentFragType() == FragType.NewOrderScrActiFragment) {
                    (getFragment() as NewOrderScrActiFragment).clickToCart()
                }
                if (getCurrentFragType() == FragType.ReturnTypeListFragment) {
                    (getFragment() as ReturnTypeListFragment).goToNextScreen()
                }
            }

            R.id.iv_filter_icon -> {
                if (getCurrentFragType() == FragType.OrderTypeListFragment) {
                    (getFragment() as OrderTypeListFragment).setData()
                }
            }

            R.id.rl_confirm_btn -> {
                if (getCurrentFragType() == FragType.CartFragment) {
                    (getFragment() as CartFragment).onConfirmClick()
                }
                if (getCurrentFragType() == FragType.CartReturnFragment) {
                    (getFragment() as CartReturnFragment).onConfirmClick()
                } else if (getCurrentFragType() == FragType.AddBillingFragment) {
                    (getFragment() as AddBillingFragment).onConfirmClick()
                } else if (getCurrentFragType() == FragType.NeworderScrCartFragment) {
                    (getFragment() as NeworderScrCartFragment).showCheckAlert()
                }
                /*else if (getCurrentFragType() == FragType.DocumentListFragment) {
 (getFragment() as DocumentListFragment).onConfirmClick()
 }*/
            }

            R.id.reimbursement_TV -> {
                loadFragment(FragType.ReimbursementListFragment, false, "")
                //showSnackMessage(getString(R.string.under_development))
            }

            R.id.achievement_TV -> {
                //Toast.makeText(mContext, "Configuration required. Please contact admin", Toast.LENGTH_SHORT).show()
                //showSnackMessage("Configuration required. Please contact admin")
                loadFragment(FragType.AchievementFragment, false, "")
            }
            /*R.id.collection_TV -> {
 loadFragment(FragType.NewCollectionListFragment, false, "")
 }*/
            R.id.target_TV -> {
                /*if (!Pref.isAddAttendence)
 checkToShowAddAttendanceAlert()
 else {*/
                isDailyPlanFromAlarm = false
                loadFragment(FragType.DailyPlanListFragment, false, "")
                //}
            }

            R.id.iv_list_party -> {
                loadFragment(FragType.AllShopListFragment, true, "")
            }

            R.id.rl_report -> {
                if (!iv_drop_down_icon.isSelected) {
                    iv_drop_down_icon.isSelected = true
                    ll_report_list.visibility = View.VISIBLE
                } else {
                    iv_drop_down_icon.isSelected = false
                    ll_report_list.visibility = View.GONE
                }
            }

            R.id.tv_attendance_report -> {
                isAttendanceFromAlarm = false
                isAttendanceReportFromDrawer = true
                loadFragment(FragType.AttendanceReportFragment, false, "")
            }

            R.id.tv_performance_report -> {
                isPerformanceFromAlarm = false
                isPerformanceReportFromDrawer = true
                loadFragment(FragType.PerformanceReportFragment, false, "")
            }

            R.id.tv_visit_report -> {
                isVisitFromAlarm = false
                isVisitReportFromDrawer = true
                loadFragment(FragType.VisitReportFragment, false, "")
            }

            R.id.meeting_TV -> {
                loadFragment(FragType.MeetingListFragment, false, "")
            }

            R.id.team_TV -> {
                if (Pref.isOfflineTeam)
                    loadFragment(FragType.OfflineMemberListFragment, true, Pref.user_id!!)
                else {
                    isAllTeam = false
                    loadFragment(FragType.MemberListFragment, true, Pref.user_id!!)
                }
            }

            R.id.iv_map -> {
                if (getCurrentFragType() == FragType.WeatherFragment) {
                    loadFragment(FragType.SearchLocationFragment, true, "")
                } else if (getCurrentFragType() == FragType.LocalShopListFragment) {
                    if (nearbyShopList != null && nearbyShopList!!.size > 0)
                        loadFragment(FragType.LocalShopListMapFragment, true, nearbyShopList!!)
                    else
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                } else {
                    if (!isMemberMap) {
                        if (Pref.willTimelineWithFixedLocationShow) {
                            activityLocationListNew?.takeIf { it.size > 0 }?.let {
                                loadFragment(FragType.ActivityMapFragment, true, it)
                            } ?: let {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                            }
                        } else {
                            activityLocationList?.takeIf { it.size > 0 }?.let {
                                loadFragment(FragType.ActivityMapFragment, true, it)
                            } ?: let {
                                (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                            }
                        }
                    } else {
                        memberLocationList?.takeIf { it.size > 0 }?.let {
                            loadFragment(FragType.ActivityMapFragment, true, it)
                        } ?: let {
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_available))
                        }
                    }
                }
            }

            R.id.timesheet_TV -> {
                loadFragment(FragType.TimeSheetListFragment, false, "")
            }

            R.id.tv_change_pwd -> {
                ChangePasswordDialog.getInstance { newPassword: String, oldPassword: String ->
                    changePasswordApi(newPassword, oldPassword)
                }.show(supportFragmentManager, "")
            }

            R.id.quo_TV -> {
                isBack = false
                loadFragment(FragType.QuotationListFragment, false, "")
            }

            R.id.all_team_TV -> {
                isAllTeam = true
                loadFragment(FragType.MemberListFragment, true, Pref.user_id!!)
            }

            R.id.update_worktype_tv -> {
                loadFragment(FragType.UpdateWorkTypeFragment, false, "")
            }

            R.id.achv_TV -> {
                isAchvFromDrawer = true
                loadFragment(FragType.AchievementReportFragment, false, "")
            }

            R.id.targ_achv_TV -> {
                isTargAchvFromDrawer = true
                loadFragment(FragType.TargetVsAchvFragment, false, "")
            }

            R.id.leave_tv -> {
                loadFragment(FragType.LeaveListFragment, false, "")
            }

            R.id.task_TV -> {
// checkByU("")
                loadFragment(FragType.TaskListFragment, false, "")
            }

            R.id.dynamic_TV -> {
                loadFragment(FragType.AllDynamicListFragment, false, "")
            }

            R.id.activity_TV -> {
                isFromMenu = true
                loadFragment(FragType.AddActivityFragment, false, "")
            }

            R.id.rl_collection -> {
                if (!iv_collection_drop_down_icon.isSelected) {
                    iv_collection_drop_down_icon.isSelected = true
                    ll_collection_list.visibility = View.VISIBLE
                } else {
                    iv_collection_drop_down_icon.isSelected = false
                    ll_collection_list.visibility = View.GONE
                }
            }

            R.id.tv_report -> {
                //loadFragment(FragType.NewCollectionListFragment, false, "")
                isCollectionStatusFromDrawer = true
                loadFragment(FragType.CollectionDetailsStatusFragment, false, "")
            }

            R.id.tv_entry -> {
                isShopFromChatBot = false
                if (Pref.IsCollectionOrderWise) {
                    loadFragment(FragType.NewOrderListFragment, false, "")
                } else {
                    loadFragment(FragType.NearByShopsListFragment, false, "")
                }
            }

            R.id.share_loc_TV -> {
                val uri =
                    "https://www.google.com/maps/?q=" + Pref.current_latitude + "," + Pref.current_longitude
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.let {
                    it.type = "text/plain"
                    it.putExtra(Intent.EXTRA_TEXT, uri)
                    startActivity(Intent.createChooser(it, "Share via"))
                }
            }

            R.id.iv_settings -> {
                loadFragment(FragType.SettingsFragment, true, "")
            }

            R.id.weather_TV -> {
                isWeatherFromDrawer = true
                loadFragment(FragType.WeatherFragment, false, "")
            }

// R.id.doc_TV -> {
// loadFragment(FragType.DocumentTypeListFragment, false, "")
// }

            R.id.doc_TV -> {
                if (Pref.IsFromPortal) {
                    loadFragment(FragType.DocumentRepoFeatureNewFragment, false, "")
                } else {
                    CustomStatic.IsDocZero = false
                    loadFragment(FragType.DocumentTypeListFragment, false, "")
                }
            }

            R.id.chat_bot_TV -> {
                showLanguageAlert(true)
            }

            R.id.distributor_wise_order_list_TV -> {
                loadFragment(FragType.DistributorwiseorderlistFragment, false, "")
            }

            R.id.photo_registration -> {
                // 2.0 AppV 4.0.6 begin old block commented and new block introduced
                /*if (AppUtils.isOnline(mContext)) {
 loadFragment(FragType.ProtoRegistrationFragment, false, "")
 } else {
 (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
 }*/
                if (AppUtils.isOnline(mContext)) {
                    if (Pref.PartyUpdateAddrMandatory) {
                        var isDDLatLongNull = true
                        var assignDD = AppDatabase.getDBInstance()!!.ddListDao().getAll()
                        try {
                            for (i in assignDD.indices) {
                                if (!assignDD[i].dd_latitude.toString()
                                        .equals("0") && !assignDD[i].dd_longitude.toString()
                                        .equals("0")
                                ) {
                                    isDDLatLongNull = false
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                        if (isDDLatLongNull && assignDD.size > 0) {
                            val simpleDialog = Dialog(mContext)
                            simpleDialog.setCancelable(true)
                            simpleDialog.getWindow()!!
                                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            simpleDialog.setContentView(R.layout.dialog_message_broad)
                            val dialogHeader =
                                simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                            val dialog_yes_no_headerTV =
                                simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                            dialog_yes_no_headerTV.text = "Hi " + Pref.user_name!! + "!"
                            dialogHeader.text =
                                "You must update WD Point address from Dashboard > Customer > Update Address."

                            val dialogYes =
                                simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                            dialogYes.setOnClickListener({ view ->
                                Handler().postDelayed(Runnable {
                                    (mContext as DashboardActivity).loadFragment(
                                        FragType.NearByShopsListFragment,
                                        false,
                                        ""
                                    )
                                }, 100)
                                simpleDialog.cancel()
                            })
                            simpleDialog.show()
                        } else {
                            loadFragment(FragType.ProtoRegistrationFragment, false, "")
                        }
                    } else {
                        loadFragment(FragType.ProtoRegistrationFragment, false, "")
                    }

                } else {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }
                // 2.0 AppV 4.0.6 end

            }

            R.id.photo_team_attendance -> {
                if (AppUtils.isOnline(mContext)) {
                    loadFragment(FragType.PhotoAttendanceFragment, false, "")
                } else {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }

            }

            R.id.tv_clear_attendance -> {
                if (AppUtils.isOnline(mContext)) {

                    //Suman 12-06-2024 mantis id 27541 begin
                    if (Pref.isAddAttendence == false) {
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_message)
                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                        val body =
                            simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                        dialogHeader.text = AppUtils.hiFirstNameText()
                        body.text = "Attendance/Leave not found or already cleared."
                        val dialogYes =
                            simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()
                        return
                    }
                    //Suman 12-06-2024 mantis id 27541 end

                    //var shopCreated = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopCreatedToday(AppUtils.getCurrentDate())
                    var shopRevit = AppDatabase.getDBInstance()!!.shopActivityDao()
                        .getTotalShopVisitedForADay(AppUtils.getCurrentDateyymmdd())
                    var ordListToday = AppDatabase.getDBInstance()!!.orderDetailsListDao()
                        .getListAccordingDate(AppUtils.getCurrentDate())
                    var collistToday = AppDatabase.getDBInstance()!!.collectionDetailsDao()
                        .getDateWiseCollection(AppUtils.getCurrentDate())
                    var newOrdListToday = AppDatabase.getDBInstance()?.newOrderScrOrderDao()
                        ?.getRateListByDate(AppUtils.getCurrentDateyymmdd())
                    //code start Mantis- 27446 by puja ITC order check for today for clear attendance functionality 017.05.2024 v4.2.8
                    var newOrdListForITCToday = AppDatabase.getDBInstance()!!.newOrderDataDao()
                        .getTodayOrder(AppUtils.getCurrentDateForShopActi()) as ArrayList<NewOrderDataEntity>
                    //code end Mantis- 27446 by puja ITC order check for today for clear attendance functionality 017.05.2024 v4.2.8

                    //if(shopCreated!!.size>0 || shopRevit!!.size>0 || ordListToday.size>0 || collistToday.size>0){
                    if (shopRevit!!.size > 0 || ordListToday.size > 0 || collistToday.size > 0 || newOrdListToday!!.size > 0 || newOrdListForITCToday!!.size > 0) {
                        //if(shopRevit!!.size>0 || collistToday.size>0){
                        val simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_message)
                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
                        val dialog_yes_no_headerTV =
                            simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
                        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText() + "!"
                        //code start Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
                        //dialogHeader.text = "Attendance can not be clear for today."
                        dialogHeader.text =
                            "You have already created an entry for the day.Attendance can't be cleared from the App.\nPlease contact your administrator."
                        //code end Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
                        val dialogYes =
                            simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
                        dialogYes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()
                    } else {
                        var simpleDialog = Dialog(mContext)
                        simpleDialog.setCancelable(false)
                        simpleDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        simpleDialog.setContentView(R.layout.dialog_yes_no)
                        val dialogHeader =
                            simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                        val dialogBody =
                            simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                        val btn_no =
                            simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
                        val btn_yes =
                            simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView

                        dialogHeader.text = AppUtils.hiFirstNameText() + "!"
                        //dialogBody.text = "Do you want to clear the Attendance or Leave for ${AppUtils.getCurrentDateChanged().replace("-"," ")}"
                        dialogBody.text =
                            "Do you want to clear the Attendance or Leave for ${AppUtils.getCurrentDate_DD_MM_YYYY()} ?"

                        btn_yes.setOnClickListener({ view ->
                            simpleDialog.cancel()
                            apiCallOnClearAttenReject()
                        })
                        btn_no.setOnClickListener({ view ->
                            simpleDialog.cancel()
                        })
                        simpleDialog.show()
                    }

                } else {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                }

            }

            R.id.ic_calendar -> {
                if (getCurrentFragType() == FragType.TaskListFragment)
                    (getFragment() as TaskListFragment).showCalender()
            }

            R.id.ic_chat_bot -> {
                showLanguageAlert(false)
            }

            R.id.iv_cancel_chat -> {
                onBackPressed()
            }

            R.id.chat_TV -> {
                loadFragment(FragType.ChatUserListFragment, false, "")
            }

            R.id.iv_people -> {
                loadFragment(FragType.ShowPeopleFragment, true, grpId)
            }

            R.id.iv_scan -> {
                //isVisitCardScan = true
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
 initPermissionCheck()
 else
 captureImage()*/

                //(getFragment() as AddShopFragment).processImage(null)

                if (getFragment() != null) {
                    if (getFragment() is AddShopFragment)
                        loadFragment(FragType.ScanImageFragment, true, "")
                    else if (getFragment() is DashboardFragment) {
                        isForRevisit = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            isCodeScan = true
                            initPermissionCheck()
                        } else
                            loadFragment(FragType.CodeScannerFragment, true, "")
                    }
                }
            }

            R.id.iv_view_text -> {
                val textList = AppUtils.loadSharedPreferencesTextList(this)
                if (textList == null || textList.isEmpty()) {
                    showSnackMessage(getString(R.string.no_data_available))
                    return
                }

                ShowCardDetailsDialog.newInstance(textList).show(supportFragmentManager, "")
            }

            R.id.scan_TV -> {
                /*val integrator = IntentIntegrator(this)
 integrator.apply {
 setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
 setPrompt("Scan a qrcode")
 setCameraId(0) // Use a specific camera of the device
 setBeepEnabled(true)
 setOrientationLocked(false)
 initiateScan()
 }*/

                isForRevisit = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isCodeScan = true
                    initPermissionCheck()
                } else
                    loadFragment(FragType.CodeScannerFragment, true, "")
            }

            R.id.nearby_user_TV -> {
                loadFragment(FragType.NearbyUserListFragment, false, "")
            }

            R.id.fl_net_status -> {
                InternetStatusDialog.getInstance(netStatus).show(supportFragmentManager, "")
            }

            R.id.home_loc_TV -> {
                if (TextUtils.isEmpty(Pref.home_latitude) || TextUtils.isEmpty(Pref.home_longitude))
                    showSnackMessage("No Home Location Configured. Talk to Admin.")
                else if (Pref.home_latitude == "0.0" || Pref.home_longitude == "0.0")
                    showSnackMessage("No Home Location Configured. Talk to Admin.")
                else
                    loadFragment(FragType.HomeLocationFragment, false, "")
            }

            R.id.device_info_TV -> {
                loadFragment(FragType.DeviceInfoListFragment, false, "")
            }

            R.id.permission_info_TV -> {
                loadFragment(FragType.ViewPermissionFragment, false, "")
            }

            /*
 R.id.anydesk_info_TV -> {
 var launchIntent: Intent? = packageManager.getLaunchIntentForPackage("com.anydesk.anydeskandroid")
 if (launchIntent != null) {
 drawerLayout.closeDrawers()
 startActivity(launchIntent)
 } else {
 drawerLayout.closeDrawers()
 val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.anydesk.anydeskandroid"))
 startActivity(intent)
 }
 }
 */
            //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7
            /* R.id.screen_record_info_TV -> {

 initScreenRecorderPermissionCheck()
 *//*
 permissionUtils = PermissionUtils(this, object : PermissionUtils.OnPermissionListener {
 override fun onPermissionGranted() {

 if (DashboardFragment.hbRecorder != null) {

 if (DashboardFragment.hbRecorder!!.isBusyRecording && DashboardFragment.hbRecorder != null) {
 Toast.makeText(this@DashboardActivity, "Please Stop Recording", Toast.LENGTH_SHORT).show()
 } else {
 if (DashboardFragment.isRecordRootVisible) {
 screen_record_info_TV.text = "Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility = View.GONE
 DashboardFragment.isRecordRootVisible = false
 drawerLayout.closeDrawers()
 } else {
 screen_record_info_TV.text = "Stop Recording"
 DashboardFragment.ll_recorder_root.visibility = View.VISIBLE
 DashboardFragment.isRecordRootVisible = true
 drawerLayout.closeDrawers()
 }
 }
 } else {
 if (DashboardFragment.isRecordRootVisible) {
 screen_record_info_TV.text = "Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility = View.GONE
 DashboardFragment.isRecordRootVisible = false
 drawerLayout.closeDrawers()
 } else {
 screen_record_info_TV.text = "Stop Recording"
 DashboardFragment.ll_recorder_root.visibility = View.VISIBLE
 DashboardFragment.isRecordRootVisible = true
 drawerLayout.closeDrawers()
 }
 }

 }

 override fun onPermissionNotGranted() {
 (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission_storage))
 }

 }, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
*//*


 *//* if(DashboardFragment.hbRecorder ==null){
 screen_record_info_TV.text="Start Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility=View.VISIBLE
 }else{
 if(DashboardFragment.hbRecorder!!.isBusyRecording){
 screen_record_info_TV.text="Stop Recording"
 DashboardFragment.ll_recorder_root.visibility=View.GONE
 }else{
 screen_record_info_TV.text="Start Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility=View.VISIBLE
 }
 }*//*
 }*/
            //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

            R.id.check_custom_status_TV -> {
                Toaster.msgShort(this, isWorkerRunning("workerTag").toString())
            }

            R.id.micro_learning_TV -> {
                loadFragment(FragType.MicroLearningListFragment, false, "")
            }

            R.id.my_learning_TV -> {
                loadFragment(FragType.MyLearningFragment, false, "")
            }

            R.id.tv_performance_teamMenu -> {
                if (AppUtils.isOnline(mContext)) {
                    loadFragment(FragType.PerformanceAppFragment, false, "")
                } else {
                    loadFragment(FragType.OwnPerformanceFragment, false, "")
                }
            }

        }
    }

    private fun initScreenRecorderPermissionCheck() {

        //begin mantis id 0027255 Storage permission updation Puja 19-02-2024
        var permissionList = arrayOf<String>(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        } else {
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
        //end mantis id 0027255 Storage permission updation Puja 19-02-2024

        permissionUtils = PermissionUtils(
            mContext as Activity,
            object : PermissionUtils.OnPermissionListener {
                override fun onPermissionGranted() {
                    //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

                    /* if (DashboardFragment.hbRecorder != null) {

 if (DashboardFragment.hbRecorder!!.isBusyRecording && DashboardFragment.hbRecorder != null) {
 Toast.makeText(this@DashboardActivity, "Please Stop Recording", Toast.LENGTH_SHORT).show()
 } else {
 if (DashboardFragment.isRecordRootVisible) {
 screen_record_info_TV.text = "Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility = View.GONE
 DashboardFragment.isRecordRootVisible = false
 drawerLayout.closeDrawers()
 } else {
 screen_record_info_TV.text = "Stop Recording"
 DashboardFragment.ll_recorder_root.visibility = View.VISIBLE
 DashboardFragment.isRecordRootVisible = true
 drawerLayout.closeDrawers()
 }
 }
 } else {
 if (DashboardFragment.isRecordRootVisible) {
 screen_record_info_TV.text = "Screen Recorder"
 DashboardFragment.ll_recorder_root.visibility = View.GONE
 DashboardFragment.isRecordRootVisible = false
 drawerLayout.closeDrawers()
 } else {
 screen_record_info_TV.text = "Stop Recording"
 DashboardFragment.ll_recorder_root.visibility = View.VISIBLE
 DashboardFragment.isRecordRootVisible = true
 drawerLayout.closeDrawers()
 }
 }*/
                    //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

                }

                override fun onPermissionNotGranted() {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
                }
                // mantis id 0027255 Storage permission updation Puja 19-02-2024
            },
            permissionList
        )// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun fileManagePermi() {
        /* val intent = Intent()
 intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
 val uri = Uri.fromParts("package", this.packageName, null)
 intent.data = uri
 startActivity(intent)*/

    }


    fun showLanguageAlert(mIsChatFromDrawer: Boolean) {
        SelectLanguageDialog.newInstance(object : SelectLanguageDialog.OnItemSelectedListener {
            override fun onItemSelect(language: String) {
                isChatFromDrawer = mIsChatFromDrawer
                if (isChatFromDrawer)
                    loadFragment(FragType.ChatBotFragment, false, language)
                else
                    loadFragment(FragType.ChatBotFragment, true, language)
            }
        }).show(supportFragmentManager, "")
    }

    private fun changePasswordApi(newPassword: String, oldPassword: String) {
        val repository = GetContentListRepoProvider.getContentListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.changePassword(oldPassword, newPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse

                    Timber.e("RESPONSE: " + response.status + ", MESSAGE: " + response.message)

                    progress_wheel.stopSpinning()
                    showSnackMessage(response.message!!)

                    if (response.status == NetworkConstant.SUCCESS) {
                        isChangedPassword = true
                        isClearData = false
                        Handler().postDelayed(Runnable {
                            loadFragment(FragType.LogoutSyncFragment, true, "")
                        }, 500)
                    }


                }, { error ->
                    Timber.e("ERROR: " + error.message)
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    showSnackMessage(getString(R.string.something_went_wrong))
                })
        )
    }


    fun openShareIntents() {
        //openShare()
        //return
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
// val phototUri = Uri.parse(localAbsoluteFilePath)
            //val fileUrl = Uri.parse(File(Environment.getExternalStorageDirectory(), "xbreezefieldnationalplasticlogsample/log").path);
            //27-09-2021
// val fileUrl = Uri.parse(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "xbreezefieldnationalplasticlogsample/log").path);
// val fileUrl = Uri.parse(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
// BuildConfig.APPLICATION_ID+"/Log/Fsmlog.html").path);
            var currentDBPath = "/data/user/0/com.breezefieldnationalplastic/files/Fsmlog.html"
            val fileUrl = Uri.parse(File(currentDBPath, "").path);

            val file = File(fileUrl.path)
            if (!file.exists()) {
                return
            }

            //val uri = Uri.fromFile(file)
            //27-09-2021
            val uri: Uri = FileProvider.getUriForFile(
                mContext,
                mContext!!.applicationContext.packageName.toString() + ".provider",
                file
            )
// shareIntent.data = fileUrl
            shareIntent.type = "image/png"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "Share log using"));
        } catch (e: Exception) {
            e.printStackTrace()
        }


// Uri uri = Uri.fromFile(file);
// emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
// startActivity(Intent.createChooser(emailIntent,""))
    }

    fun openShare() {
        try {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND_MULTIPLE
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
            intent.type = "image/*"

            val fileUrl1 = Uri.parse(
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "xbreezefieldnationalplasticlogsample/log"
                ).path
            );
            val fileUrl2 = Uri.parse(
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "xbreezefieldnationalplasticlogsample/log.bak.1"
                ).path
            );
            val files = ArrayList<Uri>()
            if (!File(fileUrl1.path).exists()) {
                return
            }
            files.add(fileUrl1)

            if (File(fileUrl2.path).exists()) {
                files.add(fileUrl2)
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
            startActivity(Intent.createChooser(intent, "Share log using"))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun deSelectAll() {
        try {
            for (i in 0 until imageArrayList.size) {
                imageArrayList[i].isSelected = false
                textArrayList[i].isSelected = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.frame_layout_container)
    }

    fun showSnackMessage(message: String) {
        DisplayAlert.showSnackMessage(this@DashboardActivity, alert_snack_bar, message)
    }

    fun showSnackMessage(message: String, duration: Int) {
        DisplayAlert.showSnackMessage(this@DashboardActivity, alert_snack_bar, message, duration)
    }

    public fun getCurrentFragType(): FragType {
        val f = supportFragmentManager.findFragmentById(R.id.frame_layout_container)
            ?: return FragType.DEFAULT
        val name = f::class.java.simpleName
        return FragType.valueOf(name)
    }


    private fun getFragInstance(
        mFragType: FragType,
        initializeObject: Any,
        enableFragGeneration: Boolean
    ): Fragment? {

        var mFragment: Fragment? = null

        when (mFragType) {
            FragType.BaseFragment -> {
                if (enableFragGeneration) {
                    mFragment = BaseFragment()
                }
                setTopBarTitle(getString(R.string.blank))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.AddShopFragment -> {

                if (enableFragGeneration) {
                    mFragment = AddShopFragment.getInstance(initializeObject)
                }

                setTopBarTitle("Add " + Pref.shopText)

                /* if (Pref.isReplaceShopText)
 setTopBarTitle(getString(R.string.add_customer))
 else
 setTopBarTitle(getString(R.string.add_shop))*/

                setTopBarVisibility(TopBarConfig.ADDSHOP)
            }

            FragType.NearByShopsListFragment -> {

                if (enableFragGeneration) {
                    mFragment = NearByShopsListFragment.getInstance(initializeObject)
                }

                setTopBarTitle(Pref.shopText + "(s)")

                /*if (Pref.isReplaceShopText)
 setTopBarTitle(getString(R.string.customers))
 else
 setTopBarTitle(getString(R.string.shops))*/

                setTopBarVisibility(TopBarConfig.SHOPLIST)
                try {
                    if (getFragment() != null && getFragment() is NearByShopsListFragment)
                        (getFragment() as NearByShopsListFragment).updateUI("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            FragType.NewNearByShopsListFragment -> {

                if (enableFragGeneration) {
                    mFragment = NewNearByShopsListFragment()
                }
                setTopBarTitle(getString(R.string.shops))
                setTopBarVisibility(TopBarConfig.SHOPLISTV1)
                try {
                    if (getFragment() != null && getFragment() is NewNearByShopsListFragment)
                        (getFragment() as NewNearByShopsListFragment).updateUI("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            FragType.ShopDetailFragment -> {

                if (enableFragGeneration) {
                    mFragment = ShopDetailFragment.getInstance(initializeObject)
                }

// setTopBarTitle(Pref.shopText + " Details")
                setTopBarTitle("Details")

                /* if (Pref.isReplaceShopText)
 setTopBarTitle(getString(R.string.customer_details))
 else
 setTopBarTitle(getString(R.string.shop_detail))*/

                setTopBarVisibility(TopBarConfig.SHOPDETAILS)
            }

            FragType.ShopDetailFragmentV1 -> {

                if (enableFragGeneration) {
                    mFragment = ShopDetailFragmentV1.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.shop_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OrderDetailFragment -> {
                if (enableFragGeneration) {
                    mFragment = OrderDetailFragment()
                }
                setTopBarTitle(getString(R.string.order_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.HomeFragment -> {
                if (enableFragGeneration) {
                    mFragment = HomeFragment()
                }
// setTopBarTitle(getString(R.string.order_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MyOrderListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MyOrderListFragment()
                }
                setTopBarTitle(getString(R.string.my_orders))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MyallowanceRequestFragment -> {
                if (enableFragGeneration) {
                    mFragment = MyallowanceRequestFragment()
                }
                setTopBarTitle(getString(R.string.my_allowence_request))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OrderhistoryFragment -> {

                if (enableFragGeneration) {
                    mFragment = OrderhistoryFragment()
                }
                //isMemberMap = false
                setTopBarTitle(getString(R.string.traveling_history))
                setTopBarVisibility(TopBarConfig.ACTIVITYMAP)
            }

            FragType.PerformanceAppFragment -> {
                if (enableFragGeneration) {
                    mFragment = PerformanceAppFragment()
                }
                setTopBarTitle("Performance Insights")
                setTopBarVisibility(TopBarConfig.GENERAL_HOME_MENU)
            }

            FragType.OwnPerformanceFragment -> {
                if (enableFragGeneration) {
                    mFragment = OwnPerformanceFragment()
                }
                setTopBarTitle("Performance Insights")
                setTopBarVisibility(TopBarConfig.GENERAL_HOME_MENU)
            }

            FragType.allPerformanceFrag -> {
                if (enableFragGeneration) {
                    mFragment = allPerformanceFrag()
                }
                setTopBarTitle("All")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.SearchLocationFragment -> {

                if (enableFragGeneration) {
                    mFragment = SearchLocationFragment()
                }
                setTopBarTitle(/*getString(R.string.history)*/"Map")
                if (getFragment() != null) {
                    if (getFragment() is DashboardFragment)
                        setTopBarVisibility(TopBarConfig.SEARCHLOCATION)
                    else if (getFragment() is AddShopFragment || getFragment() is WeatherFragment)
                        setTopBarVisibility(TopBarConfig.SEARCHLOCATIONFROMADDSHOP)
                }
            }

            FragType.StockListFragment -> {
                if (enableFragGeneration) {
                    mFragment = StockListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.UpdateShopStockFragment -> {
                if (enableFragGeneration) {
                    mFragment = UpdateShopStockFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.current_stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CompetetorStockFragment -> {
                if (enableFragGeneration) {
                    mFragment = CompetetorStockFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.competetor_stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OrderListFrag -> {
                if (enableFragGeneration) {
                    mFragment = OrderListFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.order_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ActivityDtlsFrag -> {
                if (enableFragGeneration) {
                    mFragment = ActivityDtlsFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activity_detail))
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.ViewOrdDtls -> {
                if (enableFragGeneration) {
                    mFragment = ViewOrdDtls.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.order_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewNewOrdHistoryFrag -> {
                if (enableFragGeneration) {
                    mFragment = ViewNewOrdHistoryFrag()
                }
                setTopBarTitle(getString(R.string.orders))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewNewOrdHisAllFrag -> {
                if (enableFragGeneration) {
                    mFragment = ViewNewOrdHisAllFrag()
                }
                setTopBarTitle(getString(R.string.orders))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ProductListFrag -> {
                if (enableFragGeneration) {
                    mFragment = ProductListFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.select_products))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CartListFrag -> {
                if (enableFragGeneration) {
                    mFragment = CartListFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.view_cart))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewStockDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewStockDetailsFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.current_stock_product_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddShopStockFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddShopStockFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.outlet_stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddCompetetorStockFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddCompetetorStockFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.competetor_stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewComStockProductDetails -> {
                if (enableFragGeneration) {
                    mFragment = ViewComStockProductDetails.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.competetor_stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.StockDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = StockDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.stock_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddOrderFragment -> {

                if (enableFragGeneration) {
                    mFragment = AddOrderFragment()
                }
                setTopBarTitle(getString(R.string.add_order))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.SettingsFragment -> {
                if (enableFragGeneration) {
                    mFragment = SettingsFragment()
                }
                setTopBarTitle(getString(R.string.settings))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DashboardFragment -> {
                if (enableFragGeneration) {
                    mFragment = DashboardFragment()
                }

                if (!Pref.isAttendanceFeatureOnly)
                    setTopBarTitle(getString(R.string.dashboard))
                else
                    setTopBarTitle(getString(R.string.attendance_report))

                setTopBarVisibility(TopBarConfig.DASHBOARD)
                try {
                    if (getFragment() != null && getFragment() is DashboardFragment)
                        (getFragment() as DashboardFragment).updateUI("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            FragType.YesterdayRouteFragment -> {
                if (enableFragGeneration) {
                    mFragment = YesterdayRouteFragment().getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.view_yesterdays_route))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AttendanceFragment -> {

                if (enableFragGeneration) {
                    mFragment = AttendanceFragment()
                }
                setTopBarTitle(getString(R.string.view_attendance))

                if (isChatBotAttendance) {
                    //isChatBotAttendance = false
                    setTopBarVisibility(TopBarConfig.ATTENDENCEBACKLIST)
                } else
                    setTopBarVisibility(TopBarConfig.ATTENDENCELIST)
            }

            FragType.ViewAllOrderListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewAllOrderListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.order_detail))
                //setTopBarVisibility(TopBarConfig.BACK)
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.ViewAllQuotListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewAllQuotListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.quotation_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewDetailsQuotFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewDetailsQuotFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.quotation_detail2))
                setTopBarVisibility(TopBarConfig.BACK)
            }


            FragType.AddQuotFormFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddQuotFormFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.quotation_add))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewAllTAListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewAllTAListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.ta_list))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.PerformanceFragment -> {
                if (enableFragGeneration) {
                    mFragment = PerformanceFragment()
                }
                setTopBarTitle(getString(R.string.performance))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewOutstandingFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewOutstandingFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.outstanding_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewPPDDListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewPPDDListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.stock_details_pp_dd))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ViewPPDDListOutstandingFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewPPDDListOutstandingFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.outstanding_details_pp_dd))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.OrderListFragment -> {

                if (enableFragGeneration) {
                    mFragment = OrderListFragment()
                }
                setTopBarTitle(getString(R.string.orders))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.NewOrderListFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewOrderListFragment()
                }
                setTopBarTitle(getString(R.string.orders))
                setTopBarVisibility(TopBarConfig.ORDERLIST)
            }
            /*21-12-2021*/
            FragType.NewReturnListFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewReturnListFragment()
                }
                setTopBarTitle(getString(R.string.returns))
// setTopBarVisibility(TopBarConfig.RETURNLIST)
            }

            FragType.NewDateWiseOrderListFragment -> {

                if (enableFragGeneration) {
                    mFragment = NewDateWiseOrderListFragment() //OrderListFragment()
                }
                setTopBarTitle(getString(R.string.orders))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddAttendanceFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddAttendanceFragment()
                }
                setTopBarTitle(getString(R.string.add_attendance))

                if (isAddAttendaceAlert) {
                    setTopBarVisibility(TopBarConfig.ADDATTENDANCE)
                } else
                    setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ReportFragment -> {

                if (enableFragGeneration) {
                    mFragment = ReportFragment()
                }
                setTopBarTitle(getString(R.string.mis_reports))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.AverageShopFragment -> {

                if (enableFragGeneration) {
                    mFragment = AverageShopFragment()
                }
                setTopBarTitle(getString(R.string.visits_to_shops_new))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AvgTimespentShopListFragment -> {

                if (enableFragGeneration) {
                    mFragment = AvgTimespentShopListFragment()
                }
                setTopBarTitle(getString(R.string.time_spent_on_each_shop_new))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.LocalShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = LocalShopListFragment()
                }

// setTopBarTitle("Nearby Located Party" + /*Pref.shopText +*/ " List")
                setTopBarTitle("Nearby Parties")
                /*if (Pref.isReplaceShopText)
 setTopBarTitle(getString(R.string.nearby_customer))
 else
 setTopBarTitle(getString(R.string.nearby_shops))*/

                setTopBarVisibility(TopBarConfig.LOCALSHOP)
                //isChatBotLocalShop = false
                try {
                    if (getFragment() != null && getFragment() is LocalShopListFragment)
                        (getFragment() as LocalShopListFragment).updateUI("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            FragType.LeadFrag -> {
                if (enableFragGeneration) {
                    mFragment = LeadFrag()
                }
                setTopBarTitle("Assigned Lead")
                setTopBarVisibility(TopBarConfig.HOME)
                setTopBarVisibility(TopBarConfig.LEADFR)
            }
            // start REv 20.0 DashboardActivity AppV 4.0.8 saheli mantis 0026024 :under the 'Assigned Lead' page
            FragType.TaskManagementFrag -> {
                if (enableFragGeneration) {
                    mFragment = TaskManagementFrag()
                }
                setTopBarTitle("Task Management")
                setTopBarVisibility(TopBarConfig.HOME)
                setTopBarVisibility(TopBarConfig.LEADFR)
            }
            // end REv 20.0 DashboardActivity AppV 4.0.8 saheli mantis 0026024 :under the 'Assigned Lead' page

            FragType.ViewLeadFrag -> {
                if (enableFragGeneration) {
                    mFragment = ViewLeadFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Activity Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewTaskManagementFrag -> {
                if (enableFragGeneration) {
                    mFragment = ViewTaskManagementFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Task Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.SurveyFrag -> {
                if (enableFragGeneration) {
                    mFragment = SurveyFrag.getInstance(initializeObject)
                }
// setTopBarTitle("Survey")
                setTopBarTitle(Pref.surveytext)
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.TeamBeatListFragment -> {
                if (enableFragGeneration) {
                    mFragment = TeamBeatListFragment.getInstance(initializeObject)
                }
                setTopBarTitle("Team Beat List")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MultipleImageFragment -> {
                if (enableFragGeneration) {
                    mFragment = MultipleImageFragment.newInstance(initializeObject)
                }
                setTopBarTitle("Attach Image")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.PendingOutLocationFrag -> {
                if (enableFragGeneration) {
                    mFragment = PendingOutLocationFrag()
                }
                setTopBarTitle("Pending Out Location")
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.SurveyViewFrag -> {
                if (enableFragGeneration) {
                    mFragment = SurveyViewFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Survey Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.SurveyViewDtlsFrag -> {
                if (enableFragGeneration) {
                    mFragment = SurveyViewDtlsFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Survey Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ShopFeedbackHisFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopFeedbackHisFrag.newInstance(initializeObject)
                }
                setTopBarTitle("History Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ShopDamageProductListFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopDamageProductListFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Damage Product")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ShopDamageProductSubmitFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopDamageProductSubmitFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Add Breakage")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DistributorwiseorderlistFragment -> {
                if (enableFragGeneration) {
                    mFragment = DistributorwiseorderlistFragment()
                }
                setTopBarTitle("DISTRIBUTOR WISE ORDER LIST")
                setTopBarVisibility(TopBarConfig.DISTWISEORDER)
            }

            FragType.NearByShopsMapFragment -> {
                if (enableFragGeneration) {
                    mFragment = NearByShopsMapFragment()
                }
                setTopBarTitle(getString(R.string.map_view))
                setTopBarVisibility(TopBarConfig.MAPVIEW)
            }

            FragType.CollectionNotiViewPagerFrag -> {
                if (enableFragGeneration) {
                    mFragment = CollectionNotiViewPagerFrag()
                }
                setTopBarTitle("General")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionPendingTeamFrag -> {
                if (enableFragGeneration) {
                    mFragment = CollectionPendingTeamFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Collenction Pending")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionPendingTeamDtlsFrag -> {
                if (enableFragGeneration) {
                    mFragment = CollectionPendingTeamDtlsFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Collenction Pending Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }


            FragType.CollectionNotiViewPagerFrag1 -> {
                if (enableFragGeneration) {
                    mFragment = CollectionNotiViewPagerFrag1()
                }
                setTopBarTitle("General")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionNotiViewPagerFrag2 -> {
                if (enableFragGeneration) {
                    mFragment = CollectionNotiViewPagerFrag2()
                }
                setTopBarTitle("General")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionPendingDtlsFrag -> {
                if (enableFragGeneration) {
                    mFragment = CollectionPendingDtlsFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Collection Pending Details")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.RepeatOrderFrag -> {
                if (enableFragGeneration) {
                    mFragment = RepeatOrderFrag()
                }
                setTopBarTitle("Repeat Order")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.TeamRepeatOrderFrag -> {
                if (enableFragGeneration) {
                    mFragment = TeamRepeatOrderFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Repeat Order")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AverageOrderFragment -> {

                if (enableFragGeneration) {
                    mFragment = AverageOrderFragment()
                }
                setTopBarTitle(getString(R.string.average_order_visited))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MyProfileFragment -> {

                if (enableFragGeneration) {
                    mFragment = MyProfileFragment()
                }
                setTopBarTitle(getString(R.string.my_profile))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.MarketingPagerFragment -> {
                if (enableFragGeneration) {
                    mFragment = MarketingPagerFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.marketing_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.GpsStatusFragment -> {
                if (enableFragGeneration) {
                    mFragment = GpsStatusFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.gps_status))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = CollectionDetailsFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.collection_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OrderTypeListFragment -> {
                if (enableFragGeneration) {
                    mFragment = OrderTypeListFragment.newInstance(initializeObject)
                }
                Handler().postDelayed(
                    Runnable {
                        setTopBarTitle(getString(R.string.search_product) + " ")
                        setTopBarVisibility(TopBarConfig.CART)
// }, 2500)
                    },
                    1500
                ) // 2500 delay 1500 10.0 DashboardActivity AppV 4.0.7 order rate issue mantis 25666
            }

            FragType.ReturnTypeListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ReturnTypeListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.search_product))
                setTopBarVisibility(TopBarConfig.CART)
            }

            FragType.CartFragment -> {
                if (enableFragGeneration) {
                    mFragment = CartFragment.newInstance(initializeObject)
                }
                Handler().postDelayed(Runnable {
                    if (AppUtils.stockStatus == 0) {
                        setTopBarTitle(getString(R.string.cart_details))
                        tv_confirm_btn.text = "Place Order"
                    } else if (AppUtils.stockStatus == 1) {
                        setTopBarTitle(getString(R.string.opening_stock))
                        tv_confirm_btn.text = "Place Stock"
                    }
                    setTopBarVisibility(TopBarConfig.CARTDETAILS)
                }, 2500)

            }

            FragType.CartReturnFragment -> {
                if (enableFragGeneration) {
                    mFragment = CartReturnFragment.newInstance(initializeObject)
                }
                if (AppUtils.stockStatus == 2) {
                    setTopBarTitle(getString(R.string.cart_details))
                    tv_confirm_btn.text = "Place Return"
                }
                setTopBarVisibility(TopBarConfig.CARTDETAILS)
            }

            FragType.ViewStockFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewStockFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.stock_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewCartFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewCartFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.order_detail))
                setTopBarVisibility(TopBarConfig.BACK)
            }
            /*17-12-2021*/
            FragType.ViewCartReturnFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewCartReturnFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.return_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }


            FragType.GpsDisableFragment -> {
                if (enableFragGeneration) {
                    mFragment = GpsDisableFragment()
                }

                setTopBarTitle(AppUtils.hiFirstNameText())
                setTopBarVisibility(TopBarConfig.GPS)
            }

            FragType.LocationListFragment -> {
                if (enableFragGeneration) {
                    mFragment = LocationListFragment()
                }
                setTopBarTitle("Location List")
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.LogoutSyncFragment -> {
                if (enableFragGeneration) {
                    mFragment = LogoutSyncFragment()
                }
                setTopBarTitle("Sync Data")

                if (isChangedPassword)
                    setTopBarVisibility(TopBarConfig.GPS)
                else {
                    if (!isForceLogout) {
                        if (isClearData)
                            setTopBarVisibility(TopBarConfig.GPS)
                        else
                            setTopBarVisibility(TopBarConfig.BACK)
                    } else
                        setTopBarVisibility(TopBarConfig.GPS)
                }
                if (Pref.IsAutoLogoutFromBatteryCheck) {
                    setTopBarVisibility(TopBarConfig.NONE)
                }
            }

            FragType.ReimbursementFragment -> {
                if (enableFragGeneration) {
                    mFragment = ReimbursementFragment()
                }
                setTopBarTitle(getString(R.string.reimbursement))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ReimbursementNFrag -> {
                if (enableFragGeneration) {
                    mFragment = ReimbursementNFrag()
                }
                setTopBarTitle(getString(R.string.reimbursement))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ReimbursementListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ReimbursementListFragment()
                }
                setTopBarTitle(getString(R.string.reimbursement_list))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ReimbursementDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = ReimbursementDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.reimbursement_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ReimbursDtlsNFrag -> {
                if (enableFragGeneration) {
                    mFragment = ReimbursDtlsNFrag.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.reimbursement_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditReimbursementFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditReimbursementFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.reimbursement_edit))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditReimbNFrag -> {
                if (enableFragGeneration) {
                    mFragment = EditReimbNFrag.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.reimbursement_edit))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AchievementFragment -> {
                if (enableFragGeneration) {
                    mFragment = AchievementFragment()
                }
                setTopBarTitle(getString(R.string.my_performance))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.NewCollectionListFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewCollectionListFragment()
                }
                setTopBarTitle(getString(R.string.collection))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.BillingListFragment -> {
                if (enableFragGeneration) {
                    mFragment = BillingListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.billing_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddBillingFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddBillingFragment.newInstance(initializeObject)
                }
                tv_confirm_btn.text = "Confirm"
                setTopBarTitle(getString(R.string.update_billing_details))
                setTopBarVisibility(TopBarConfig.CARTDETAILS)
            }

            FragType.AttendanceReportFragment -> {
                if (enableFragGeneration) {
                    mFragment = AttendanceReportFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.attendance_report))

                if (isAttendanceFromAlarm)
                    setTopBarVisibility(TopBarConfig.GPS)
                else {
                    if (isAttendanceReportFromDrawer)
                        setTopBarVisibility(TopBarConfig.HOME)
                    else
                        setTopBarVisibility(TopBarConfig.BACK)
                }
                //setTopBarVisibility(TopBarConfig.GPS)

            }

            FragType.PerformanceReportFragment -> {
                if (enableFragGeneration) {
                    mFragment = PerformanceReportFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.performance_report))
                if (isPerformanceFromAlarm)
                    setTopBarVisibility(TopBarConfig.GPS)
                else {
                    if (isPerformanceReportFromDrawer)
                        setTopBarVisibility(TopBarConfig.HOME)
                    else
                        setTopBarVisibility(TopBarConfig.BACK)
                }

                //setTopBarVisibility(TopBarConfig.GPS)
            }

            FragType.VisitReportFragment -> {
                if (enableFragGeneration) {
                    mFragment = VisitReportFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.visit_report))
                if (isVisitFromAlarm)
                    setTopBarVisibility(TopBarConfig.GPS)
                else {
                    if (isVisitReportFromDrawer)
                        setTopBarVisibility(TopBarConfig.HOME)
                    else
                        setTopBarVisibility(TopBarConfig.BACK)
                }

                //setTopBarVisibility(TopBarConfig.GPS)
            }

            FragType.VisitReportDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = VisitReportDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.visit_report_details))
                setTopBarVisibility(TopBarConfig.VISITREPORTDETAILS)
            }

            FragType.NotificationFragment -> {
                if (enableFragGeneration) {
                    mFragment = NotificationFragment()
                }
                setTopBarTitle(getString(R.string.notification))
                setTopBarVisibility(TopBarConfig.NOTIFICATION)
            }

            FragType.BookmarkFrag -> {
                if (enableFragGeneration) {
                    mFragment = BookmarkFrag()
                }
                setTopBarTitle("Saved Contents")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.BookmarkPlayFrag -> {
                if (enableFragGeneration) {
                    mFragment = BookmarkPlayFrag()
                }
                setTopBarTitle("Saved Contents")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.PerformanceInsightPage -> {
                if (enableFragGeneration) {
                    mFragment = PerformanceInsightPage()
                }
                setTopBarTitle("Performance Insights")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.BillingDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = BillingDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.bill))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.KnowYourStateFragment -> {
                if (enableFragGeneration) {
                    mFragment = KnowYourStateFragment()
                }
                setTopBarTitle(getString(R.string.report_on_state))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.DailyPlanListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DailyPlanListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(Pref.dailyPlanListHeaderText)

                if (!isDailyPlanFromAlarm) {
                    if (!AppUtils.isFromAttendance)
                        setTopBarVisibility(TopBarConfig.TARGETPLAN)
                    else
                        setTopBarVisibility(TopBarConfig.TARGETPLANBACK)
                } else
                    setTopBarVisibility(TopBarConfig.TARGETPLANGPS)
            }

            FragType.PlanDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = PlanDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.plan_details))

                if (!isDailyPlanFromAlarm)
                    setTopBarVisibility(TopBarConfig.BACK)
                else
                    setTopBarVisibility(TopBarConfig.TARGETPLANDETAILSBACK)
            }

            FragType.AllShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = AllShopListFragment()
                }
                setTopBarTitle(Pref.allPlanListHeaderText)
                setTopBarVisibility(TopBarConfig.ALLSHOPLIST)
            }

            FragType.MeetingListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MeetingListFragment()
                }
                setTopBarTitle(getString(R.string.meeting_list))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.MemberListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MemberListFragment.newInstance(initializeObject)
                }

                if (!isAllTeam)
                    setTopBarTitle(getString(R.string.team_details))
                else
                    setTopBarTitle(getString(R.string.all_team_details))

                /*if (!isAddBackStack)
 setTopBarVisibility(TopBarConfig.HOME)
 else*/
                setTopBarVisibility(TopBarConfig.ONLINEMEMBERLIST)
            }

            FragType.MemberShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MemberShopListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_shop_details))
                setTopBarVisibility(TopBarConfig.MEMBERSHOPLIST)
            }

            FragType.MemberAllShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MemberAllShopListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_shop_details))
                setTopBarVisibility(TopBarConfig.MEMBERSHOPLIST)
            }

            FragType.ActivityMapFragment -> {
                if (enableFragGeneration) {
                    mFragment = ActivityMapFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.map))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MemberActivityFragment -> {
                if (enableFragGeneration) {
                    mFragment = MemberActivityFragment.newInstance(initializeObject)
                }
                isMemberMap = true
                setTopBarTitle(getString(R.string.traveling_history))
                setTopBarVisibility(TopBarConfig.MEMBERACTIVITYMAP)
            }

            FragType.MemberPJPListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MemberPJPListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.journey_plan_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddPJPFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddPJPFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_pjp))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditPJPFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditPJPFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_pjp))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddPJPLocationFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddPJPLocationFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.map))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.LocalShopListMapFragment -> {
                if (enableFragGeneration) {
                    mFragment = LocalShopListMapFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.map))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.TimeSheetListFragment -> {
                if (enableFragGeneration) {
                    mFragment = TimeSheetListFragment()
                }
                setTopBarTitle(getString(R.string.timesheet_list))
                setTopBarVisibility(TopBarConfig.TIMESHEETLIST)
            }

            FragType.AddTimeSheetFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddTimeSheetFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.timesheet_add))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditTimeSheetFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditTimeSheetFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.timesheet_edit))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AreaListFragment -> {
                if (enableFragGeneration) {
                    mFragment = AreaListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.area_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddQuotationFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddQuotationFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_quo))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.QuotationListFragment -> {
                if (enableFragGeneration) {
                    mFragment = QuotationListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.quot_list))

                if (isBack)
                    setTopBarVisibility(TopBarConfig.BACK)
                else
                    setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.QuotationDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = QuotationDetailsFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.quot_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DateWiseQuotationList -> {
                if (enableFragGeneration) {
                    mFragment = DateWiseQuotationList()
                }
                setTopBarTitle(getString(R.string.quot_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditQuotationFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditQuotationFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_quot))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OfflineMemberListFragment -> {
                if (enableFragGeneration) {
                    mFragment = OfflineMemberListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_details))
                setTopBarVisibility(TopBarConfig.OFFLINEMEMBERLIST)
            }

            FragType.OfflineAllShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = OfflineAllShopListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_shop_details))
                setTopBarVisibility(TopBarConfig.OFFLINESHOPLIST)
            }

            FragType.OfflineShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = OfflineShopListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_shop_details))
                setTopBarVisibility(TopBarConfig.OFFLINESHOPLIST)
            }

            FragType.OfflineAreaListFragment -> {
                if (enableFragGeneration) {
                    mFragment = OfflineAreaListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.area_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.UpdateWorkTypeFragment -> {
                if (enableFragGeneration) {
                    mFragment = UpdateWorkTypeFragment()
                }
                setTopBarTitle(getString(R.string.update_work_type))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.AchievementReportFragment -> {
                if (enableFragGeneration) {
                    mFragment = AchievementReportFragment()
                }
                setTopBarTitle(getString(R.string.achv_report))

                if (isAchvFromDrawer)
                    setTopBarVisibility(TopBarConfig.HOME)
                else
                    setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AchievementDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = AchievementDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.achv_report_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.TargetVsAchvFragment -> {
                if (enableFragGeneration) {
                    mFragment = TargetVsAchvFragment()
                }
                setTopBarTitle(getString(R.string.targ_achv_report))

                if (isTargAchvFromDrawer)
                    setTopBarVisibility(TopBarConfig.HOME)
                else
                    setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.TargetVsAchvDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = TargetVsAchvDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.targ_achv_report_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.LeaveListFragment -> {
                if (enableFragGeneration) {
                    mFragment = LeaveListFragment()
                }
                setTopBarTitle(getString(R.string.leave_list))
                setTopBarVisibility(TopBarConfig.LEAVELIST)
            }

            FragType.ApplyLeaveFragment -> {
                if (enableFragGeneration) {
                    mFragment = ApplyLeaveFragment()
                }
                setTopBarTitle(getString(R.string.apply_leave))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.TaskListFragment -> {
                if (enableFragGeneration) {
                    mFragment = TaskListFragment()
                }
                setTopBarTitle(getString(R.string.task_list))
                setTopBarVisibility(TopBarConfig.TASKLIST)
            }

            FragType.AddTaskFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddTaskFragment()
                }
                setTopBarTitle(getString(R.string.add_task))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditTaskFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditTaskFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_task))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddDynamicFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddDynamicFragment.newInstance(initializeObject)
                }
                setTopBarTitle("Add $dynamicScreen")
                setTopBarVisibility(TopBarConfig.BACK)
            }
            // Rev 21.0 DashboardActivity AppV 4.0.8 saheli 15/05/2023 mantis 26103
            FragType.MultipleImageFileUploadonStock -> {
                if (enableFragGeneration) {
                    mFragment = MultipleImageFileUploadonStock.getInstance(initializeObject)
                }
                setTopBarTitle("Attach document")
                setTopBarVisibility(TopBarConfig.BACK)
            }
            // end Rev 21.0 DashboardActivity AppV 4.0.8 saheli 15/05/2023 mantis 26103


            FragType.AllDynamicListFragment -> {
                if (enableFragGeneration) {
                    mFragment = AllDynamicListFragment()
                }
                setTopBarTitle(getString(R.string.dynamic_list))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.DynamicListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DynamicListFragment.newInstance(initializeObject)
                }
                setTopBarTitle("$dynamicScreen List")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditDynamicFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditDynamicFragment.newInstance(initializeObject)
                }
                setTopBarTitle("Edit $dynamicScreen")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddActivityFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddActivityFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_activity))

                if (isFromMenu)
                    setTopBarVisibility(TopBarConfig.HOME)
                else
                    setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.EditActivityFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditActivityFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_activity))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ActivityDetailsListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ActivityDetailsListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activities))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ActivityShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ActivityShopListFragment()
                }
                setTopBarTitle(getString(R.string.activity_party))
                setTopBarVisibility(TopBarConfig.ACTIVITSHOP)
            }

            FragType.DateWiseActivityListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DateWiseActivityListFragment()
                }
                setTopBarTitle(getString(R.string.date_wise_activities))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ShopBillingListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ShopBillingListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.billing_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ShopBillingDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = ShopBillingDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.bill))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionDetailsStatusFragment -> {
                if (enableFragGeneration) {
                    mFragment = CollectionDetailsStatusFragment()
                }
                setTopBarTitle(getString(R.string.collection_details))

                if (isCollectionStatusFromDrawer)
                    setTopBarVisibility(TopBarConfig.HOME)
                else
                    setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CollectionShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = CollectionShopListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.collection_details))
                //setTopBarVisibility(TopBarConfig.BACK)
                setTopBarVisibility(TopBarConfig.PHOTOREG)
            }

            FragType.ChemistActivityListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChemistActivityListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activity_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DoctorActivityListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DoctorActivityListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activity_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddChemistFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddChemistFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_activity))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditChemistActivityFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditChemistActivityFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_activity))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ChemistDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChemistDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activity_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.AddDoctorFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddDoctorFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_activity))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.EditDoctorActivityFragment -> {
                if (enableFragGeneration) {
                    mFragment = EditDoctorActivityFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.edit_activity))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DoctorDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = DoctorDetailsFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.activity_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.WeatherFragment -> {
                if (enableFragGeneration) {
                    mFragment = WeatherFragment()
                }
                setTopBarTitle(getString(R.string.weather))
                setTopBarVisibility(TopBarConfig.WEATHERMAP)
            }

            FragType.DocumentTypeListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DocumentTypeListFragment()
                }
                setTopBarTitle(getString(R.string.doc_repo))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.DocumentListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DocumentListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.doc_repo))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ChatBotFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChatBotFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.chat_bot))
                setTopBarVisibility(TopBarConfig.CHATBOT)
            }

            FragType.CalenderTaskFragment -> {
                if (enableFragGeneration) {
                    mFragment = CalenderTaskFragment()
                }
                setTopBarTitle(getString(R.string.calendar))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ReportsFragment -> {
                if (enableFragGeneration) {
                    mFragment = ReportsFragment()
                }
                setTopBarTitle(getString(R.string.reports))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ChatUserListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChatUserListFragment()
                }
                setTopBarTitle(getString(R.string.chat))
                setTopBarVisibility(TopBarConfig.CHATUSER)
            }

            FragType.ChatListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChatListFragment.newInstance(initializeObject)
                }
                setTopBarTitle(AppUtils.decodeEmojiAndText(userName))
                setTopBarVisibility(TopBarConfig.CHAT)
            }

            FragType.AddGroupFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddGroupFragment()
                }
                setTopBarTitle(getString(R.string.new_grp))
                setTopBarVisibility(TopBarConfig.NEWGROUP)
            }

            FragType.AddPeopleFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddPeopleFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_people))
                setTopBarVisibility(TopBarConfig.NEWGROUP)
            }

            FragType.ShowPeopleFragment -> {
                if (enableFragGeneration) {
                    mFragment = ShowPeopleFragment.newInstance(initializeObject)
                }
                setTopBarTitle(userName)
                setTopBarVisibility(TopBarConfig.NEWGROUP)
            }

            FragType.AddNewMsgFragment -> {
                if (enableFragGeneration) {
                    mFragment = AddNewMsgFragment()
                }
                setTopBarTitle(getString(R.string.new_conversation))
                setTopBarVisibility(TopBarConfig.NEWGROUP)
            }

            FragType.TimeLineFragment -> {

                if (enableFragGeneration) {
                    mFragment = TimeLineFragment()
                }
                isMemberMap = false
                setTopBarTitle(getString(R.string.traveling_history))
                setTopBarVisibility(TopBarConfig.ACTIVITYMAP)
            }

            FragType.ScanImageFragment -> {
                if (enableFragGeneration) {
                    mFragment = ScanImageFragment()
                }
                setTopBarTitle(getString(R.string.scan_visiting_card))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CodeScannerFragment -> {
                if (enableFragGeneration) {
                    mFragment = CodeScannerFragment()
                }

                if (!isForRevisit)
                    setTopBarTitle(getString(R.string.code_scanner))
                else
                    setTopBarTitle("Scan QR for ${Pref.shopText} Revisit")

                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.NearbyUserListFragment -> {
                if (enableFragGeneration) {
                    mFragment = NearbyUserListFragment()
                }
                setTopBarTitle(getString(R.string.nearby_user))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ChatBotShopListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ChatBotShopListFragment.getInstance(initializeObject)
                }

                try {
                    val isVisit = initializeObject as Boolean
                    if (isVisit)
                        setTopBarTitle("Visit Analysis ${Pref.shopText}wise")
                    else
                        setTopBarTitle("Order Analysis ${Pref.shopText}wise")
                    setTopBarVisibility(TopBarConfig.CHATBOTSHOP)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            FragType.HomeLocationFragment -> {
                if (enableFragGeneration) {
                    mFragment = HomeLocationFragment()
                }
                setTopBarTitle(AppUtils.hiFirstNameText())
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.HomeLocationMapFragment -> {
                if (enableFragGeneration) {
                    mFragment = HomeLocationMapFragment()
                }
                setTopBarTitle(getString(R.string.map))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.BeatListFragment -> {
                if (enableFragGeneration) {
                    mFragment = BeatListFragment()
                }
                setTopBarTitle(getString(R.string.beat_list))
                setTopBarVisibility(TopBarConfig.BEATLIST)
            }

            FragType.DeviceInfoListFragment -> {
                if (enableFragGeneration) {
                    mFragment = DeviceInfoListFragment()
                }
                setTopBarTitle(getString(R.string.device_info))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ViewPermissionFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewPermissionFragment()
                }
                setTopBarTitle(getString(R.string.permission_info))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.MicroLearningListFragment -> {
                if (enableFragGeneration) {
                    mFragment = MicroLearningListFragment()
                }
                setTopBarTitle(getString(R.string.training_contents))
                setTopBarVisibility(TopBarConfig.MICROLEARNING)
            }

            FragType.MyLearningFragment -> {
                if (enableFragGeneration) {
                    mFragment = MyLearningFragment()
                }
                setTopBarTitle("Dashboard")
                setTopBarVisibility(TopBarConfig.MyLearning)
            }

            FragType.MyLearningTopicList -> {
                if (enableFragGeneration) {
                    mFragment = MyLearningTopicList.getInstance(initializeObject)
                }
                setTopBarTitle("My Learning")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.SearchLmsFrag -> {
                if (enableFragGeneration) {
                    mFragment = SearchLmsFrag()
                }
                setTopBarTitle("My Topics")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.LmsQuestionAnswerSet -> {
                if (enableFragGeneration) {
                    mFragment = LmsQuestionAnswerSet.getInstance(initializeObject)
                }
                //setTopBarTitle("Question Set")
                setTopBarVisibility(TopBarConfig.QUESTION_ANSWER_SET)
            }

            FragType.MyLearningVideoPlay -> {
                if (enableFragGeneration) {
                    mFragment = MyLearningVideoPlay.getInstance(initializeObject)
                }
                setTopBarTitle("")
                setTopBarVisibility(TopBarConfig.LMS_VIDEO)
            }

            FragType.NotificationLMSFragment -> {
                if (enableFragGeneration) {
                    mFragment = NotificationLMSFragment()
                }
                setTopBarTitle("Notifications")
                setTopBarVisibility(TopBarConfig.LMS_Notification)
            }

            FragType.SearchLmsLearningFrag -> {
                if (enableFragGeneration) {
                    mFragment = SearchLmsLearningFrag.getInstance(initializeObject)
                }
                setTopBarTitle("My Learning")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.MyTopicsWiseContents -> {
                if (enableFragGeneration) {
                    mFragment = MyTopicsWiseContents.getInstance(initializeObject)
                }
                setTopBarTitle("My Topics")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.AllTopicsWiseContents -> {
                if (enableFragGeneration) {
                    mFragment = AllTopicsWiseContents.getInstance(initializeObject)
                }
                setTopBarTitle("All Topics")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
            }

            FragType.SearchLmsKnowledgeFrag -> {
                if (enableFragGeneration) {
                    mFragment = SearchLmsKnowledgeFrag()
                }
                setTopBarTitle("All Topics")
                setTopBarVisibility(TopBarConfig.LMS_SEARCH)
                //setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.MyPerformanceFrag -> {
                if (enableFragGeneration) {
                    mFragment = MyPerformanceFrag()
                }
                setTopBarTitle("My Performance")
                setTopBarVisibility(TopBarConfig.Performance_LMS)
            }

            FragType.MyLearningAllVideoList -> {
                if (enableFragGeneration) {
                    mFragment = MyLearningAllVideoList.getInstance(initializeObject)
                }
                setTopBarTitle("My Learning")
                setTopBarVisibility(TopBarConfig.Performance_LMS)
            }

            FragType.KnowledgeHubAllVideoList -> {
                if (enableFragGeneration) {
                    mFragment = KnowledgeHubAllVideoList.getInstance(initializeObject)
                }
                setTopBarTitle("All Topics")
                setTopBarVisibility(TopBarConfig.Performance_LMS)
            }

            FragType.VideoPlayLMS -> {
                if (enableFragGeneration) {
                    tv_noti_count.visibility = View.GONE
                    mFragment = VideoPlayLMS.getInstance(initializeObject)
                }
                setTopBarTitle("")
                setTopBarVisibility(TopBarConfig.LMS_VIDEO)
            }

            FragType.LeaderboardLmsFrag -> {
                if (enableFragGeneration) {
                    mFragment = LeaderboardLmsFrag()
                }
                setTopBarTitle("Learners Leaderboard")
                setTopBarVisibility(TopBarConfig.LEADERBOARD_LMS)
            }

            FragType.PerformanceAppFragment -> {
                if (enableFragGeneration) {
                    mFragment = PerformanceAppFragment()
                }
                setTopBarTitle("My Performance")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.PrivacypolicyWebviewFrag -> {
                if (enableFragGeneration) {
                    mFragment = PrivacypolicyWebviewFrag()
                }
                setTopBarTitle("Privacy Policy")
                setTopBarVisibility(TopBarConfig.GENERAL_HOME_MENU)
            }

            FragType.MicroLearningWebViewFragment -> {
                if (enableFragGeneration) {
                    mFragment = MicroLearningWebViewFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.training_contents))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OpenFileWebViewFragment -> {
                if (enableFragGeneration) {
                    mFragment = OpenFileWebViewFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.attach_document))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MyJobsFragment -> {
                if (enableFragGeneration) {
                    //mFragment = MyJobsFragment()
                    mFragment = MyJobsFragment.setUserID(initializeObject)
                }
                setTopBarTitle(getString(R.string.myjobs))
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.JobsCustomerFragment -> {
                if (enableFragGeneration) {
                    mFragment = JobsCustomerFragment.newInstance(initializeObject)
                }
                setTopBarTitle(Pref.shopText)
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.WorkInProgressFragment -> {
                if (enableFragGeneration) {
                    mFragment = WorkInProgressFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.wip))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.WorkOnHoldFragment -> {
                if (enableFragGeneration) {
                    mFragment = WorkOnHoldFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.woh))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.WorkCompletedFragment -> {
                if (enableFragGeneration) {
                    mFragment = WorkCompletedFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.work_completed))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.WorkCancelledFragment -> {
                if (enableFragGeneration) {
                    mFragment = WorkCancelledFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.work_cancelled))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.UpdateReviewFragment -> {
                if (enableFragGeneration) {
                    mFragment = UpdateReviewFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.update_review))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.CustomerListFragment -> {
                if (enableFragGeneration) {
                    mFragment = CustomerListFragment()
                }
                setTopBarTitle(getString(R.string.customer) + "(s)")
                setTopBarVisibility(TopBarConfig.CUSTOMER)
            }

            FragType.ServiceHistoryFragment -> {
                if (enableFragGeneration) {
                    mFragment = ServiceHistoryFragment.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.service_history))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ProtoRegistrationFragment -> {
                if (enableFragGeneration) {
                    mFragment = ProtoRegistrationFragment.getInstance(initializeObject)
                    //mFragment = BaseFragment()
                }
                setTopBarTitle(getString(R.string.photo_registration))
                setTopBarVisibility(TopBarConfig.PHOTOREG)
            }

            FragType.ContactsFrag -> {
                if (enableFragGeneration) {
                    mFragment = ContactsFrag()
                }
                setTopBarTitle("CRM")
                setTopBarVisibility(TopBarConfig.CONTACT)
            }

            FragType.ContactsAddFrag -> {
                if (enableFragGeneration) {
                    mFragment = ContactsAddFrag.getInstance(initializeObject)
                }
                //setTopBarTitle("Add Contact")
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.SchedulerViewFrag -> {
                if (enableFragGeneration) {
                    mFragment = SchedulerViewFrag.getInstance(initializeObject)
                }
                //setTopBarTitle("Add Contact")
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
                setTopBarTitle("Scheduler(s)")
            }

            FragType.TemplateViewFrag -> {
                if (enableFragGeneration) {
                    mFragment = TemplateViewFrag()
                }
                setTopBarTitle("Template")
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.TemplateAddFrag -> {
                if (enableFragGeneration) {
                    mFragment = TemplateAddFrag()
                }
                setTopBarTitle("Add Template")
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.SchedulerAddFormFrag -> {
                if (enableFragGeneration) {
                    mFragment = SchedulerAddFormFrag.getInstance(initializeObject)
                }
                //setTopBarTitle("Add Contact")
                setTopBarVisibility(TopBarConfig.SCHEDULER)
                //setTopBarTitle("Add Schedule")
            }

            FragType.ShopCallHisFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopCallHisFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Call Log History")
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.LeaderBoardFrag -> {
                if (enableFragGeneration) {
                    mFragment = LeaderBoardFrag()
                }

                setTopBarTitle("Leaderboard")
                setTopBarVisibility(TopBarConfig.LEADERBOARD)
            }

            FragType.LeaveHome -> {
                if (enableFragGeneration) {
                    mFragment = LeaveHome.getInstance(initializeObject)
                    //mFragment = BaseFragment()
                }
                setTopBarTitle("Leave Approval")
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.PhotoAttendanceFragment -> {
                if (enableFragGeneration) {
                    mFragment = PhotoAttendanceFragment.getInstance(initializeObject)
                    //mFragment = BaseFragment()
                }
                setTopBarTitle(getString(R.string.team_attendance))
                setTopBarVisibility(TopBarConfig.HOME)
                setTopBarVisibility(TopBarConfig.PHOTOREG)
            }

            FragType.RegisTerFaceFragment -> {
                if (enableFragGeneration) {
                    mFragment = RegisTerFaceFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.photo_registration))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.PhotoRegAadhaarFragment -> {
                if (enableFragGeneration) {
                    mFragment = PhotoRegAadhaarFragment.getInstance(initializeObject)
                }
                if (CustomStatic.IsAadhaarForPhotoReg)
                    setTopBarTitle(getString(R.string.aadhaar_registration))
                else if (CustomStatic.IsVoterForPhotoReg)
                    setTopBarTitle(getString(R.string.voter_registration))
                else if (CustomStatic.IsPanForPhotoReg)
                    setTopBarTitle(getString(R.string.pan_registration))
                setTopBarVisibility(TopBarConfig.BACK)
            }
            // 5.0 DashboardActivity AppV 4.0.6 MenuBeatFrag
            FragType.MenuBeatFrag -> {
                if (enableFragGeneration) {
                    mFragment = MenuBeatFrag()
                }
                setTopBarTitle("Beat")
                setTopBarVisibility(TopBarConfig.GENERAL_HOME_MENU)
            }

            FragType.TeamAttendanceFragment -> {
                if (enableFragGeneration) {
                    mFragment = TeamAttendanceFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.team_attendance))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.DocumentRepoFeatureNewFragment -> {
                if (enableFragGeneration) {
                    mFragment = DocumentRepoFeatureNewFragment()
                }
                setTopBarTitle(getString(R.string.doc_repo))
                setTopBarVisibility(TopBarConfig.HOME)
                setTopBarVisibility(TopBarConfig.NOTIFICATION)
            }

            FragType.NewOrderScrActiFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewOrderScrActiFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.add_order_text))
                setTopBarVisibility(TopBarConfig.NEWORDERCART)
// setTopBarVisibility(TopBarConfig.CART)
            }

            FragType.OrderProductListFrag -> {
                if (enableFragGeneration) {
                    mFragment = OrderProductListFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.sel_product))
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.ShopListMarketAssistFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopListMarketAssistFrag()
                }
                setTopBarTitle("Market Assistant")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.MarketAssistTabFrag -> {
                if (enableFragGeneration) {
                    mFragment = MarketAssistTabFrag()
                }
                setTopBarTitle("Market Assistant")
                setTopBarVisibility(TopBarConfig.GENERAL_HOME_MENU)
            }

            FragType.ShopDtlsMarketAssistFrag -> {
                if (enableFragGeneration) {
                    mFragment = ShopDtlsMarketAssistFrag.getInstance(initializeObject)
                }
                setTopBarTitle("Analysis")
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.OrderProductCartFrag -> {
                if (enableFragGeneration) {
                    mFragment = OrderProductCartFrag.getInstance(initializeObject)
                }
                if (Pref.savefromOrderOrStock) {
                    setTopBarTitle(getString(R.string.view_cart))
                } else {
                    setTopBarTitle(getString(R.string.opening_stock))
                }

                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.NeworderScrCartFragment -> {
                if (enableFragGeneration) {
                    mFragment = NeworderScrCartFragment.getInstance(initializeObject)
                }
// tv_confirm_btn.text = "Place Order"
                setTopBarVisibility(TopBarConfig.NEWORDERSCRCART)
                if (CustomStatic.IsFromViewNewOdrScr == true) {
                    setTopBarTitle("Order Details")
                } else {
                    setTopBarTitle(getString(R.string.new_order_scr_cart))
                }

// setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.NewOrderScrOrderDetailsFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewOrderScrOrderDetailsFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.new_order_scr_list))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            FragType.ViewCrmOpptFrag -> {
                if (enableFragGeneration) {
                    mFragment = ViewCrmOpptFrag.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.new_oppt_scr_list))
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK)
            }

            FragType.AddOpptFrag -> {
                if (enableFragGeneration) {
                    mFragment = AddOpptFrag.getInstance(initializeObject)
                }
                //setTopBarTitle(getString(R.string.new_oppt_scr_add))
                setTopBarVisibility(TopBarConfig.MENU_ONLY_BACK_WITH_REFRESH)
            }

            FragType.NewOdrScrListFragment -> {
                if (enableFragGeneration) {
                    mFragment = NewOdrScrListFragment()
                }
                setTopBarTitle(getString(R.string.orders))
                // setTopBarVisibility(TopBarConfig.ORDERLIST)
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.MapViewForTeamFrag -> {
                if (enableFragGeneration) {
                    mFragment = MapViewForTeamFrag.newInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.map_view))
                setTopBarVisibility(TopBarConfig.TEAMMAP)
            }

            FragType.LeaveHome -> {
                if (enableFragGeneration) {
                    mFragment = LeaveHome()
                }
                setTopBarVisibility(TopBarConfig.HOME)
            }

            FragType.ViewAllReturnListFragment -> {
                if (enableFragGeneration) {
                    mFragment = ViewAllReturnListFragment.getInstance(initializeObject)
                }
                setTopBarTitle(getString(R.string.return_details))
                setTopBarVisibility(TopBarConfig.BACK)
            }

            else -> {
                if (enableFragGeneration) {
                    mFragment = DashboardFragment().getInstance(initializeObject)

                }
                setTopBarTitle(getString(R.string.blank))
                setTopBarVisibility(TopBarConfig.HOME)
            }


        }

// FragType.DocumentRepoFeatureNewFragment->{
//
// }

        /*if (getFragment() != null && getFragment() is ReimbursementListFragment) {
 if ((getFragment() as ReimbursementListFragment).mPopupWindow != null && (getFragment() as ReimbursementListFragment).mPopupWindow!!.isShowing)
 (getFragment() as ReimbursementListFragment).mPopupWindow?.dismiss()
 }*/

        searchView.closeSearch()
        return mFragment
    }

    fun setTopBarTitle(title: String) {
        headerTV.text = title + " "
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public fun setTopBarVisibility(mTopBarConfig: TopBarConfig) {
        tv_noti_count.visibility = View.GONE
        add_bookmark.visibility = View.GONE
        tv_saved_count.visibility = View.GONE
        when (mTopBarConfig) {
            TopBarConfig.NONE -> {
                iv_home_icon.visibility = View.GONE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
            }

            TopBarConfig.HOME -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.CUSTOMER -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.MICROLEARNING -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.MyLearning -> {
                iv_home_icon.visibility = View.GONE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LMS_SEARCH -> {
                iv_home_icon.visibility = View.VISIBLE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LMS_Notification -> {
                iv_home_icon.visibility = View.GONE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LMS_VIDEO -> {
                tv_noti_count.visibility = View.GONE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.BEATLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.CHATUSER -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                /*// Show back button
 supportActionBar!!.setDisplayHomeAsUpEnabled(true)
 mDrawerToggle.isDrawerIndicatorEnabled = false
 mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
 drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)*/
            }

            TopBarConfig.CHATBOT -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.VISIBLE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (isChatFromDrawer) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.LEAVELIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)

                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.ATTENDENCELIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.TASKLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.VISIBLE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LOCALSHOP -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.VISIBLE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (!isChatBotLocalShop) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.ORDERLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                if (isOrderFromChatBot) {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                } else {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }

            TopBarConfig.DASHBOARD -> {
                iv_home_icon.visibility = View.GONE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE

                if (!Pref.isAttendanceFeatureOnly)
                    logo.visibility = View.VISIBLE
                else
                    logo.visibility = View.GONE

                if (Pref.NotiCountFlag) {
                    tv_noti_count.visibility = View.VISIBLE
                } else {
                    tv_noti_count.visibility = View.GONE
                }

                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.VISIBLE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.VISIBLE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (Pref.isScanQrForRevisit)
                    iv_scan.visibility = View.VISIBLE
                else
                    iv_scan.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)

                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.SHOPLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                if (isShopFromChatBot /*|| Pref.isShowShopBeatWise*/) {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                } else {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }

            TopBarConfig.SHOPLISTV1 -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.VISIBLE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.MAPVIEW -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.VISIBLE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (isMapFromDrawer) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.GPS -> {
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_home_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                mDrawerToggle.setHomeAsUpIndicator(null)
                //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.CART -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.VISIBLE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.CHATBOTSHOP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.NEWORDERCART -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.VISIBLE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.CARTDETAILS -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.VISIBLE
                iv_settings.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.ADDATTENDANCE -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.LOGOUTSYNC -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                mDrawerToggle.setHomeAsUpIndicator(null)
            }

            TopBarConfig.VISITREPORTDETAILS -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_settings.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.NOTIFICATION -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.SEARCHLOCATION -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.SEARCHLOCATIONFROMADDSHOP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TARGETPLAN -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.TARGETPLANBACK -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TARGETPLANDETAILSBACK -> {
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TARGETPLANGPS -> {
                /*mDrawerToggle.isDrawerIndicatorEnabled = false
 iv_search_icon.visibility = View.VISIBLE
 iv_home_icon.visibility = View.GONE
 rl_cart.visibility = View.GONE
 rl_confirm_btn.visibility = View.GONE
 iv_filter_icon.visibility = View.GONE
 logo.visibility = View.GONE
 // Show back button
 supportActionBar!!.setDisplayHomeAsUpEnabled(true)
 mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
 drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)*/


                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_home_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                mDrawerToggle.setHomeAsUpIndicator(null)
            }

            TopBarConfig.ALLSHOPLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.MEMBERACTIVITYMAP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_settings.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_map.visibility = View.VISIBLE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (!isMemberMap) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.ACTIVITYMAP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_settings.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_map.visibility = View.VISIBLE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                if (!isMemberMap) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.ADDSHOP -> {
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                if (Pref.willScanVisitingCard) {
                    iv_scan.visibility = View.VISIBLE
                    iv_view_text.visibility = View.VISIBLE
                    iv_home_icon.visibility = View.GONE
                    logo.visibility = View.GONE
                } else {
                    iv_scan.visibility = View.GONE
                    iv_view_text.visibility = View.GONE
                    iv_home_icon.visibility = View.VISIBLE
                    logo.visibility = View.VISIBLE
                }
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.PHOTOREG -> {
                mDrawerToggle.isDrawerIndicatorEnabled = true
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                /* if (Pref.willScanVisitingCard) {
 iv_scan.visibility = View.VISIBLE
 iv_view_text.visibility = View.VISIBLE
 iv_home_icon.visibility = View.GONE
 logo.visibility = View.GONE
 } else {
 iv_scan.visibility = View.GONE
 iv_view_text.visibility = View.GONE
 iv_home_icon.visibility = View.VISIBLE
 logo.visibility = View.VISIBLE
 }*/

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.LEADFR -> {
                mDrawerToggle.isDrawerIndicatorEnabled = true
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

// if (Pref.willScanVisitingCard) {
// iv_scan.visibility = View.VISIBLE
// iv_view_text.visibility = View.VISIBLE
// iv_home_icon.visibility = View.GONE
// logo.visibility = View.GONE
// }
// else {
// iv_scan.visibility = View.GONE
// iv_view_text.visibility = View.GONE
// iv_home_icon.visibility = View.VISIBLE
// logo.visibility = View.VISIBLE
// }

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.SHOPDETAILS -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.MEMBERSHOPLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.OFFLINESHOPLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.OFFLINEMEMBERLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TIMESHEETLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.ONLINEMEMBERLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.ACTIVITSHOP -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE

                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.WEATHERMAP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_settings.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_map.visibility = View.VISIBLE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (isWeatherFromDrawer) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    // Show hamburger
                    mDrawerToggle.isDrawerIndicatorEnabled = true
                    toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    // Show back button
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }

            TopBarConfig.ATTENDENCEBACKLIST -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (Pref.isChatBotShow)
                    ic_chat_bot.visibility = View.VISIBLE
                else
                    ic_chat_bot.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.NEWGROUP -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.CHAT -> {
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                if (isGrp)
                    iv_people.visibility = View.VISIBLE
                else
                    iv_people.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.BACK -> {
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.Performance_LMS -> {
                iv_home_icon.visibility = View.VISIBLE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.QUESTION_ANSWER_SET -> {
                iv_home_icon.visibility = View.VISIBLE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.LEADERBOARD -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_leaderboard.visibility = View.INVISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LEADERBOARD_OWN -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LEADERBOARD_LMS -> {
                iv_home_icon.visibility = View.VISIBLE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.LEADERBOARD_OWN_LMS -> {
                iv_home_icon.visibility = View.VISIBLE
                add_bookmark.visibility = View.VISIBLE
                if (Pref.CurrentBookmarkCount > 0) {
                    tv_saved_count.visibility = View.VISIBLE
                } else {
                    tv_saved_count.visibility = View.GONE
                }
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.GENERAL_HOME_BACK -> {
                iv_home_icon.visibility = View.VISIBLE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.GENERAL_HOME_MENU -> {
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.MENU_ONLY_BACK -> {
                iv_home_icon.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TEAMMAP -> {
                iv_home_icon.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.DISTWISEORDER -> {
                iv_home_icon.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = true
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.NEWORDERSCRCART -> {
                /* if(CustomStatic.IsFromViewNewOdrScr==false){
 rl_confirm_btn.visibility = View.VISIBLE
 }
 else{
 rl_confirm_btn.visibility = View.GONE
 }*/
                rl_confirm_btn.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.SCHEDULER_LIST -> {
                /* if(CustomStatic.IsFromViewNewOdrScr==false){
 rl_confirm_btn.visibility = View.VISIBLE
 }
 else{
 rl_confirm_btn.visibility = View.GONE
 }*/
                iv_leaderboard.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.HOME_BACK -> {
                /* if(CustomStatic.IsFromViewNewOdrScr==false){
 rl_confirm_btn.visibility = View.VISIBLE
 }
 else{
 rl_confirm_btn.visibility = View.GONE
 }*/
                iv_leaderboard.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.TEMPLATE_LIST -> {
                /* if(CustomStatic.IsFromViewNewOdrScr==false){
 rl_confirm_btn.visibility = View.VISIBLE
 }
 else{
 rl_confirm_btn.visibility = View.GONE
 }*/
                iv_leaderboard.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.VISIBLE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                logo.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.VISIBLE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.CONTACT -> {
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                // Show hamburger
                mDrawerToggle.isDrawerIndicatorEnabled = true
                toolbar.setNavigationIcon(R.drawable.ic_header_menu_icon)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            TopBarConfig.SCHEDULER -> {
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.VISIBLE
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                iv_list_party.visibility = View.GONE
                logo.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE

                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }

            TopBarConfig.MENU_ONLY_BACK -> {
                iv_home_icon.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            TopBarConfig.MENU_ONLY_BACK_WITH_REFRESH -> {
                iv_home_icon.visibility = View.GONE
                iv_leaderboard.visibility = View.GONE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.VISIBLE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.GONE
                logo.clearAnimation()
                logo.animate().cancel()
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE
                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }

            else -> {
                iv_leaderboard.visibility = View.GONE
                iv_home_icon.visibility = View.VISIBLE
                mDrawerToggle.isDrawerIndicatorEnabled = false
                iv_search_icon.visibility = View.GONE
                iv_sync_icon.visibility = View.GONE
                rl_cart.visibility = View.GONE
                iv_filter_icon.visibility = View.GONE
                rl_confirm_btn.visibility = View.GONE
                logo.visibility = View.VISIBLE
                iv_list_party.visibility = View.GONE
                iv_map.visibility = View.GONE
                iv_settings.visibility = View.GONE
                ic_calendar.visibility = View.GONE
                ic_chat_bot.visibility = View.GONE
                iv_cancel_chat.visibility = View.GONE
                iv_people.visibility = View.GONE
                iv_scan.visibility = View.GONE
                iv_view_text.visibility = View.GONE
                fl_net_status.visibility = View.GONE
                add_scheduler_email_verification.visibility = View.GONE
                add_template.visibility = View.GONE

                // Show back button
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_header_back_arrow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }
        }


    }

    var isShowAlert = true
    var qrCodeText = ""

    @SuppressLint("NewApi")
    override fun onBackPressed() {
        try {
            (this as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            showToolbar()
            callmoduleEnd()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (Pref.IsUserWiseLMSFeatureOnly){
            //statusColorPortrait()
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            statusColorPortrait()
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            statusColorPortraitWithFsm()
        }

        val fm = supportFragmentManager
        // code start by puja 23.03.2024 mantis id - 27333
        if (!fm.isDestroyed())
        // code end by puja 23.03.2024 mantis id - 27333
            fm.executePendingTransactions()
        //TODO Hide Soft Keyboard
        AppUtils.hideSoftKeyboard(this)

        Timber.e("Current Fragment========> " + getFragment())

        var tt = getFragment().toString()
        var ttt = fm.backStackEntryCount
        var cf = getFragment()
        println("tag_kali begin ${getFragment()}")
        if (getFragment() != null && getFragment() is NotificationLMSFragment) {
            loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)
        } else if (getFragment() != null && getFragment() is MyLearningFragment && Pref.IsUserWiseLMSFeatureOnly == true) {
            println("tag_kali hinning for MyLearningFragment")
            if (backpressed + 2000 > System.currentTimeMillis()) {
                finish()
                super.onBackPressed()
            } else {
                showSnackMessage(getString(R.string.alert_exit))
            }
            backpressed = System.currentTimeMillis()
        } else if (fm.backStackEntryCount == 0 && getFragment() != null && (getFragment() is PerformanceReportFragment || getFragment() is AttendanceReportFragment
                    || getFragment() is VisitReportFragment || getFragment() is DailyPlanListFragment)
        ) {
            if (isConfirmed) {
                Log.d("login_test_calling5", "")

                loadFragment(FragType.DashboardFragment, false, Any())
                isConfirmed = false
            } else {
                if (getFragment() is DailyPlanListFragment) {
                    Log.d("login_test_calling6", "")

                    loadFragment(FragType.DashboardFragment, false, Any())

                    Handler().postDelayed(Runnable {
                        try {
                            (getFragment() as DashboardFragment).updateItem()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, 350)
                }
            }
        } else if (fm.backStackEntryCount == 0 && getFragment() != null && getFragment() !is DashboardFragment) {
            if (getFragment() != null && getFragment() is MicroLearningListFragment) {
                if ((getFragment() as MicroLearningListFragment).isFilterSelected)
                    (getFragment() as MicroLearningListFragment).showAllList()
                else {
                    Log.d("login_test_calling7", "")

                    loadFragment(FragType.DashboardFragment, false, Any())
                    Handler().postDelayed(Runnable {
                        (getFragment() as DashboardFragment).updateItem()
                    }, 500)
                }
            } else if (getFragment() != null && getFragment() is LeaveHome) {
                //loadFragment(FragType.DashboardFragment, false, Any())
                //loadFragment(FragType.MemberListFragment, false, Pref.user_id!!)
                Handler().postDelayed(Runnable {
                    //(getFragment() as DashboardFragment).updateItem()
                    loadFragment(FragType.MemberListFragment, false, Pref.user_id!!)
                }, 500)
            } else {
                loadFragment(FragType.DashboardFragment, false, Any())
                //Added Saheli 27-07-21
                AppUtils.changeLanguage(this, "en")
                Handler().postDelayed(Runnable {
                    (getFragment() as DashboardFragment).updateItem()
                }, 500)
            }

        }

        /*else if (getFragment() != null && getFragment() is MyLearningFragment) {
 if (backpressed + 2000 > System.currentTimeMillis()) {
 finish()
 super.onBackPressed()
 } else {
 showSnackMessage(getString(R.string.alert_exit))
 }
 backpressed = System.currentTimeMillis()
 }*/
        else if (getFragment() != null && getFragment() is DashboardFragment) {
            if (backpressed + 2000 > System.currentTimeMillis()) {
                finish()
                super.onBackPressed()
            } else {
                showSnackMessage(getString(R.string.alert_exit))
            }
            backpressed = System.currentTimeMillis()

        } else if (getFragment() != null && getFragment() is SearchLocationFragment) {

            super.onBackPressed()

            if (getFragment() != null) {
                if (getFragment() is DashboardFragment) {
                    if (locationInfoModel != null)
                        (getFragment() as DashboardFragment).sendHomeLoc(locationInfoModel)
                    else
                        checkToShowHomeLocationAlert()
                } else if (getFragment() is WeatherFragment) {
                    if (locationInfoModel != null) {
                        (getFragment() as WeatherFragment).getLocationFromMap(locationInfoModel)
                        locationInfoModel = null
                    }
                } else {
                    super.onBackPressed()
                    if (locationInfoModel != null) {
                        loadFragment(FragType.AddShopFragment, true, locationInfoModel!!)
                    }
                }
            }

        } else if (getFragment() != null && getFragment() is OrderTypeListFragment) {

            if (isShowAlert)
                showAlert()
            else {
                super.onBackPressed()
                isShowAlert = true

                AppUtils.clearPreferenceKey(this, "PRODUCT_RATE_LIST")

                if (getFragment() != null && getFragment() is ViewAllOrderListFragment)
                    (getFragment() as ViewAllOrderListFragment).updateList()
                else if (getFragment() != null && getFragment() is StockListFragment)
                    (getFragment() as StockListFragment).updateList()

                qtyList.clear()
                rateList.clear()
                totalPrice.clear()
                totalScPrice.clear()

                schemaqtyList.clear()
                schemarateList.clear()
                mrpList.clear()


            }
        } else if (getFragment() != null && getFragment() is OrderProductListFrag) {
            if (getFragment() != null && getFragment() is OrderProductListFrag) {
                if ((getFragment() as OrderProductListFrag).checkCartSize() != 0) {
                    if (isShowAlert)
                        showAlert()
                    else {
                        super.onBackPressed()
                        isShowAlert = true
                    }
                } else
                    super.onBackPressed()
            }
        } else if (getFragment() != null && getFragment() is ReturnTypeListFragment) {
            if (isShowAlert)
                showAlert()
            else {
                super.onBackPressed()
                isShowAlert = true

                AppUtils.clearPreferenceKey(this, "PRODUCT_RATE_LIST")

                if (getFragment() != null && getFragment() is ViewAllReturnListFragment)
                    (getFragment() as ViewAllReturnListFragment).updateList()

                qtyList.clear()
                rateList.clear()
                totalPrice.clear()
                totalScPrice.clear()
                schemaqtyList.clear()
                schemarateList.clear()
                mrpList.clear()

            }
        } else if (getFragment() != null && getFragment() is AddBillingFragment) {

            schemaqtyList.clear()

            qtyList.clear()
            rateList.clear()
            totalPrice.clear()
            totalScPrice.clear()
            schemarateList.clear()
            mrpList.clear()

            super.onBackPressed()

            when {
                getFragment() is BillingListFragment -> (getFragment() as BillingListFragment).updateItem()
                getFragment() is NewDateWiseOrderListFragment -> (getFragment() as NewDateWiseOrderListFragment).updateItem()
                getFragment() is NewOrderListFragment -> (getFragment() as NewOrderListFragment).updateItem()
            }
        }
        /*else if (getFragment() != null && getFragment() is CartFragment) {

 super.onBackPressed()

 totalPrice.clear()
 }*/
        else if (getFragment() != null && getFragment() is GpsDisableFragment) {
            if (!isGpsDisabled)
                super.onBackPressed()
        } else if (getFragment() != null && (getFragment() is AttendanceReportFragment || getFragment() is PerformanceReportFragment ||
                    getFragment() is VisitReportFragment)
        ) {
            if (isConfirmed) {
                super.onBackPressed()
                isConfirmed = false
            }
        } else if (getFragment() != null && getFragment() is AddAttendanceFragment) {
            if (Pref.IsPendingColl && Pref.ShowZeroCollectioninAlert) {
                SendBrod.sendBrodColl(this)
                tv_noti_count.visibility = View.VISIBLE
            } else {
                tv_noti_count.visibility = View.GONE
            }

            if (Pref.IsZeroOrder && Pref.IsShowRepeatOrderinNotification) {
                SendBrod.sendBrodZeroOrder(this)
                tv_noti_count.visibility = View.VISIBLE
            } else {
                tv_noti_count.visibility = View.GONE
            }

            /*if(Pref.IsTodayDOBDOA){
 //SendBrod.sendBrodDOBDOA(this)
 tv_noti_count.visibility=View.VISIBLE
 }else{
 tv_noti_count.visibility=View.GONE
 }*/

            isAddAttendaceAlert = false
            AppUtils.isFromAttendance = false
            super.onBackPressed()

            Timber.e("isAddAttendence========> " + Pref.isAddAttendence)

            Handler().postDelayed(Runnable {

                if (!Pref.isAddAttendence)
                    checkToShowAddAttendanceAlert()
                else {
                    isAttendanceAlertPresent = false
                    if (getFragment() != null && getFragment() is DashboardFragment)
                        (getFragment() as DashboardFragment).updateBottomList()
                }

            }, 500)

        } else if (getFragment() != null && getFragment() is ReimbursementFragment) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ReimbursementListFragment)
                (getFragment() as ReimbursementListFragment).callApi()
        } else if (getFragment() != null && getFragment() is ReimbursementNFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ReimbursementListFragment)
                (getFragment() as ReimbursementListFragment).callApi()
        } else if (getFragment() != null && getFragment() is EditReimbursementFragment) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ReimbursementListFragment)
                (getFragment() as ReimbursementListFragment).callApi()
        } else if (getFragment() != null && getFragment() is MemberListFragment) {
            super.onBackPressed()
            if (getFragment() != null) {
                if (getFragment() !is MemberListFragment)
                    teamHierarchy.clear()
                else
                    (getFragment() as MemberListFragment).updateTeamHierarchy()

                if (getFragment() is DashboardFragment)
                    (getFragment() as DashboardFragment).updateItem()
            }
        } else if (getFragment() != null && getFragment() is ShopDamageProductSubmitFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ShopDamageProductListFrag)
                (getFragment() as ShopDamageProductListFrag).updatePage()
        } else if (getFragment() != null && getFragment() is SurveyFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is SurveyViewFrag)
                (getFragment() as SurveyViewFrag).updatePage()
        } else if (getFragment() != null && (getFragment() is MemberAllShopListFragment || getFragment() is MemberShopListFragment || getFragment() is AreaListFragment)) {

            if (getFragment() is MemberAllShopListFragment) {
                if ((getFragment() as MemberAllShopListFragment).shopIdList.isNotEmpty()) {
                    (getFragment() as MemberAllShopListFragment).updateListOnBackPress()
                    return
                }
            }

            super.onBackPressed()
            if (getFragment() != null) {
                if (getFragment() is MemberListFragment)
                    (getFragment() as MemberListFragment).updateMemberTeamHierarchy()
            }
        } else if (getFragment() != null && (getFragment() is AddPJPFragment || getFragment() is EditPJPFragment)) {
            super.onBackPressed()

            if (getFragment() != null && getFragment() is MemberPJPListFragment && isAddedEdited) {
                isAddedEdited = false
                (getFragment() as MemberPJPListFragment).updateList()
            }

        } else if (getFragment() != null && getFragment() is LogoutSyncFragment) {
            if (!Pref.IsAutoLogoutFromBatteryCheck) {
                try {
                    if (!isForceLogout) {
                        super.onBackPressed()
                        if (getFragment() != null && getFragment() is ChatBotFragment)
                            (getFragment() as ChatBotFragment).update()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        } else if (getFragment() != null && getFragment() is AddPJPLocationFragment) {

            val lat = (getFragment() as AddPJPLocationFragment).selectedLat
            val lng = (getFragment() as AddPJPLocationFragment).selectedLong
            val address = (getFragment() as AddPJPLocationFragment).selectedAddress
            val radius = (getFragment() as AddPJPLocationFragment).radius

            super.onBackPressed()

            if (getFragment() != null && getFragment() is AddPJPFragment)
                (getFragment() as AddPJPFragment).updateAddress(lat, lng, address, radius)
            else if (getFragment() != null && getFragment() is EditPJPFragment)
                (getFragment() as EditPJPFragment).updateAddress(lat, lng, address, radius)
        } else if (getFragment() != null && (getFragment() is AddTimeSheetFragment || getFragment() is EditTimeSheetFragment)) {
            super.onBackPressed()

            if (getFragment() != null && getFragment() is TimeSheetListFragment && isTimesheetAddedEdited) {
                isTimesheetAddedEdited = false
                (getFragment() as TimeSheetListFragment).updateList()
            }

        }
        /*else if (getFragment() != null && getFragment() is DailyPlanListFragment) {
 super.onBackPressed()
 if (getFragment() != null && getFragment() is AddAttendanceFragment)
 super.onBackPressed()

 }*/
        else if (getFragment() != null && getFragment() is OfflineMemberListFragment) {
            super.onBackPressed()
            if (getFragment() != null) {
                if (getFragment() !is OfflineMemberListFragment)
                    teamHierarchy.clear()
                else
                    (getFragment() as OfflineMemberListFragment).updateTeamHierarchy()

                if (getFragment() is DashboardFragment)
                    (getFragment() as DashboardFragment).updateItem()
            }
        } else if (getFragment() != null && (getFragment() is OfflineAllShopListFragment || getFragment() is OfflineShopListFragment || getFragment() is OfflineAreaListFragment)) {

            if (getFragment() is OfflineAllShopListFragment) {
                (getFragment() as OfflineAllShopListFragment).isAddressUpdated = false
                if ((getFragment() as OfflineAllShopListFragment).shopIdList.isNotEmpty()) {
                    (getFragment() as OfflineAllShopListFragment).updateListOnBackPress()
                    return
                }
            }

            super.onBackPressed()
            if (getFragment() != null) {
                if (getFragment() is OfflineMemberListFragment)
                    (getFragment() as OfflineMemberListFragment).updateMemberTeamHierarchy()
            }
        } else if (getFragment() != null && (getFragment() is AddDynamicFragment || getFragment() is EditDynamicFragment)) {

            super.onBackPressed()
            if (isDynamicFormUpdated) {
                isDynamicFormUpdated = false
                if (getFragment() != null && getFragment() is DynamicListFragment)
                    (getFragment() as DynamicListFragment).updateList()
            }
        } else if (getFragment() != null && (getFragment() is AddGroupFragment || getFragment() is AddNewMsgFragment ||
                    getFragment() is ChatListFragment)
        ) {

            if (getFragment() is AddNewMsgFragment) {
                super.onBackPressed()

                if (newUserModel != null) {

                    newUserModel?.also {
                        if (it.name.contains("(")) {
                            val name = it.name.substring(0, it.name.indexOf("("))
                            userName = name
                        } else
                            userName = it.name

                        loadFragment(FragType.ChatListFragment, true, it)
                    }
                    newUserModel = null
                } else {
                    if (getFragment() != null && getFragment() is ChatUserListFragment)
                        (getFragment() as ChatUserListFragment).updateList()
                }
            } else {
                super.onBackPressed()
                if (getFragment() != null && getFragment() is ChatUserListFragment)
                    (getFragment() as ChatUserListFragment).updateList()
            }
        } else if (getFragment() != null && getFragment() is ScanImageFragment) {

            val picTexts = (getFragment() as ScanImageFragment).stringArrays
            val isCopy = (getFragment() as ScanImageFragment).isCopy

            if (isCopy)
                AppUtils.saveSharedPreferencesImageText(this, picTexts)

            super.onBackPressed()

            if (getFragment() != null && getFragment() is AddShopFragment) {
                (getFragment() as AddShopFragment).processImage(/*File(resultUri.path!!)*/picTexts,
                    isCopy
                )
            }
        } else if (getFragment() != null && getFragment() is CodeScannerFragment) {
            super.onBackPressed()

            if (!isCodeScaneed)
                return

            isCodeScaneed = false
            if (!isForRevisit) {
                if (!TextUtils.isEmpty(qrCodeText))
                    CodeScannerTextDialog.newInstance(qrCodeText).show(supportFragmentManager, "")
                else
                    showSnackMessage("Scan QR Code has been cancelled.")
            } else {
                if (!Pref.isAddAttendence) {
                    checkToShowAddAttendanceAlert()
                    return
                }

                if (Pref.isOnLeave.equals("true", ignoreCase = true)) {
                    showSnackMessage(getString(R.string.error_you_are_in_leave))
                    return
                }

                try {
                    val shopId = qrCodeText.substring(0, qrCodeText.indexOf("\n"))

                    val userId = shopId.substring(0, shopId.indexOf("_"))
                    if (userId != Pref.user_id) {
                        //showSnackMessage("Scanned QR is not your ${Pref.shopText}. Revisit not possible. Thanks")
                        CommonDialogSingleBtn.getInstance(AppUtils.hiFirstNameText() + "!",
                            "Scanned QR is not your ${Pref.shopText}. Revisit not possible. Thanks",
                            "Ok",
                            object : OnDialogClickListener {
                                override fun onOkClick() {
                                }
                            }).show(supportFragmentManager, "")
                        return
                    }

                    val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(shopId)

                    val distance = LocationWizard.getDistance(
                        shop?.shopLat!!,
                        shop.shopLong!!,
                        Pref.current_latitude.toDouble(),
                        Pref.current_longitude.toDouble()
                    )

                    if (distance * 1000 > Pref.gpsAccuracy.toDouble()) {
                        //showSnackMessage("Hi, you are not at the nearby location. Be there and try to scan for Revisit.")
                        CommonDialogSingleBtn.getInstance(AppUtils.hiFirstNameText() + "!",
                            "Hi, you are not at the nearby location. Please be there & scan QR to revisit for today.",
                            "Ok",
                            object : OnDialogClickListener {
                                override fun onOkClick() {
                                }
                            }).show(supportFragmentManager, "")
                    } else {
                        mAddShopDBModelEntity = shop
                        terminateOtherShopVisit(1, shop, shop.shopName, shopId, null, null)

                        if (!Pref.isShowShopVisitReason) {
                            shopName = mStoreName
                            contactNumber = shop.ownerContactNumber

                            startOwnShopRevisit(shop, shop.shopName, shopId)

                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showSnackMessage("Invalid QR Code")
                }
            }
        } else if (getFragment() != null && getFragment() is MicroLearningListFragment) {
            if ((getFragment() as MicroLearningListFragment).isFilterSelected)
                (getFragment() as MicroLearningListFragment).showAllList()
            else
                super.onBackPressed()
        } else if (getFragment() != null && getFragment() is JobsCustomerFragment) {
            if ((getFragment() as JobsCustomerFragment).isUpdateStatusClicked) {
                (getFragment() as JobsCustomerFragment).isUpdateStatusClicked = false
                val mCustomerdata = (getFragment() as JobsCustomerFragment).customerdata
                super.onBackPressed()
                loadFragment(FragType.JobsCustomerFragment, true, mCustomerdata!!)
            } else {
                super.onBackPressed()

                if (getFragment() != null && getFragment() is MyJobsFragment) {
                    if (isCalledJobApi) {
                        isCalledJobApi = false
                        (getFragment() as MyJobsFragment).getCustomerListApi()
                    }
                }
            }
        } else if (getFragment() != null && getFragment() is PhotoRegAadhaarFragment) {
            println("PhotoRegAadhaarFragment backpressed");
            super.onBackPressed()
        } else if (getFragment() != null && getFragment() is ViewAllOrderListFragment && (ShopDetailFragment.isOrderEntryPressed || AddShopFragment.isOrderEntryPressed) && AppUtils.getSharedPreferenceslogOrderStatusRequired(
                this
            )
        ) {

            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_yes_no)
            val dialogYes =
                simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
            val dialogNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
                if (true) {
                    val dialog = Dialog(mContext)
                    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setContentView(R.layout.dialog_cancel_order_status)

                    val user_name =
                        dialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                    val order_status =
                        dialog.findViewById(R.id.tv_cancel_order_status) as AppCustomTextView
                    val cancel_remarks =
                        dialog.findViewById(R.id.et_cancel_order_remarks) as AppCustomEditText
                    val submitRemarks =
                        dialog.findViewById(R.id.tv_cancel_order_submit_remarks) as AppCustomTextView

                    order_status.text = "Failure"
                    user_name.text = "Hi " + Pref.user_name + "!"

                    submitRemarks.setOnClickListener(View.OnClickListener { view ->
                        if (!TextUtils.isEmpty(cancel_remarks.text.toString().trim())) {
                            Toast.makeText(
                                mContext,
                                cancel_remarks.text.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            val obj = OrderStatusRemarksModelEntity()
                            //obj.shop_id= mShopId

                            obj.shop_id = ViewAllOrderListFragment.mSShopID_Str.toString()
                            obj.user_id = Pref.user_id
                            obj.order_status = order_status.text.toString()
                            obj.order_remarks = cancel_remarks!!.text!!.toString()
                            obj.visited_date_time = AppUtils.getCurrentDateTime()
                            obj.visited_date = AppUtils.getCurrentDateForShopActi()
                            obj.isUploaded = false


                            var shopAll =
                                AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityAll()
                            if (shopAll.size == 1) {
                                obj.shop_revisit_uniqKey = shopAll.get(0).shop_revisit_uniqKey
                            } else if (shopAll.size != 0) {
                                obj.shop_revisit_uniqKey =
                                    shopAll.get(shopAll.size - 1).shop_revisit_uniqKey
                            }

                            if (shopAll.size != 0)
                                AppDatabase.getDBInstance()?.shopVisitOrderStatusRemarksDao()!!
                                    .insert(obj)
                            dialog.dismiss()

                            if (ShopDetailFragment.isOrderEntryPressed) {
                                ShopDetailFragment.isOrderEntryPressed = false
                            }
                            if (AddShopFragment.isOrderEntryPressed) {
                                AddShopFragment.isOrderEntryPressed = false
                            }


                            super.onBackPressed()
                            when {
                                getFragment() is ShopDetailFragment -> (getFragment() as ShopDetailFragment).updateItem()
                            }
                        } else {
                            submitRemarks.setError("Enter Remarks")
                            submitRemarks.requestFocus()
                        }

                    })
                    dialog.show()
                }
            })
            dialogNo.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()

        } else if (getFragment() != null && getFragment() is ViewAllOrderListFragment && CustomStatic.IsBackFromNewOptiCart) {
            if (CustomStatic.IsOrderLoadFromCRM) {
                CustomStatic.IsOrderLoadFromCRM = false
                loadFragment(FragType.ContactsFrag, false, "")
            } else if (CustomStatic.IsOrderLoadFromShop) {
                CustomStatic.IsOrderLoadFromShop = false
                loadFragment(FragType.NearByShopsListFragment, false, "")
            } else {
                CustomStatic.IsBackFromNewOptiCart = false
                Log.d("login_test_calling9", "")

                loadFragment(FragType.DashboardFragment, false, "")
            }

        }
        // start 24.0 DashboardACtivity v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli
        else if (getFragment() != null && getFragment() is StockListFragment && CustomStatic.IsBackFromNewOptiCart) {
            CustomStatic.IsBackFromNewOptiCart = false
            Log.d("login_test_calling10", "")

            loadFragment(FragType.DashboardFragment, false, DashboardType.Home)
        }
        // end 24.0 DashboardACtivity v 4.1.6 stock optmization mantis 0026391 20-06-2023 saheli
        /*Date 14-09-2021*/
        else if (getFragment() != null && getFragment() is NewOrderScrOrderDetailsFragment) {
            Log.d("login_test_calling11", "")

            loadFragment(FragType.DashboardFragment, false, DashboardType.Home)
        } else if (getFragment() != null && getFragment() is NewOrderScrActiFragment && CustomStatic.NewOrderTotalCartItem > 0) {
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_yes_no)
            val dialogHeader =
                simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
            val dialog_yes_no_headerTV =
                simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
            dialog_yes_no_headerTV.text = "Hi " + Pref.user_name!! + "!"
            dialogHeader.text = "Click Yes to clear the cart and back to the list to start again."
            val dialogYes =
                simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
            val dialogNo = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
                CustomStatic.NewOrderTotalCartItem = 0
                super.onBackPressed();
                //(mContext as DashboardActivity).loadFragment(FragType.NewOrderScrOrderDetailsFragment, false, NewOrderScrOrderDetailsFragment.shop_id)
            })
            dialogNo.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()

        } else if (getFragment() != null && getFragment() is AddQuotFormFragment) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ViewAllQuotListFragment) {
                (getFragment() as ViewAllQuotListFragment).updateView()
            }

        } else if (getFragment() != null && getFragment() is ViewDetailsQuotFragment) {
            super.onBackPressed()
            CustomStatic.IsNewQuotEdit = false
        } else if (getFragment() != null && getFragment() is ViewLeadFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is LeadFrag && CustomStatic.IsViewLeadAddUpdate) {
                (getFragment() as LeadFrag).updateView()
            }
        }
        // mantis 26028
        else if (getFragment() != null && getFragment() is ViewTaskManagementFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is TaskManagementFrag && CustomStatic.IsViewTaskAddUpdate) {
                (getFragment() as TaskManagementFrag).updateView()
            }
        } else if (getFragment() != null && getFragment() is CollectionPendingDtlsFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is CollectionNotiViewPagerFrag1)
                (getFragment() as CollectionNotiViewPagerFrag1).updateView()
            if (getFragment() != null && getFragment() is CollectionNotiViewPagerFrag)
                (getFragment() as CollectionNotiViewPagerFrag).updateView()
            if (getFragment() != null && getFragment() is CollectionNotiViewPagerFrag2)
                (getFragment() as CollectionNotiViewPagerFrag2).updateView()
        } else if (getFragment() != null && getFragment() is MapViewForTeamFrag) {
            if (getFragment() != null && getFragment() is MapViewForTeamFrag)
                MapViewForTeamFrag.timer!!.cancel()
            super.onBackPressed()
        } else if (getFragment() != null && getFragment() is NewOdrScrListFragment) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is DashboardFragment) {
                (getFragment() as DashboardFragment).updateOrdAmtForNewOrd()
            }
        } else if (getFragment() != null && getFragment() is OrderProductCartFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is OrderProductListFrag) {
                (getFragment() as OrderProductListFrag).updateCartSize()
            }
        } else if (getFragment() != null && getFragment() is ContactsAddFrag) {
            if (!ContactsAddFrag.editShopID.equals("")) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_yes_no)

                val tv_header =
                    simpleDialog.findViewById(R.id.dialog_yes_no_headerTV) as AppCustomTextView
                val tv_body =
                    simpleDialog.findViewById(R.id.dialog_cancel_order_header_TV) as AppCustomTextView
                val tv_ok =
                    simpleDialog.findViewById(R.id.tv_dialog_yes_no_yes) as AppCustomTextView
                val tv_no = simpleDialog.findViewById(R.id.tv_dialog_yes_no_no) as AppCustomTextView

                tv_header.text = AppUtils.hiFirstNameText()
                tv_body.text =
                    "Are you sure you want to exit from modify mode? Any unsaved changes will be lost."
                tv_ok.setOnClickListener {
                    simpleDialog.dismiss()
                    super.onBackPressed()
                    if (getFragment() != null && getFragment() is ContactsFrag) {
                        (getFragment() as ContactsFrag).shopContactList("")
                    }
                }

                tv_no.setOnClickListener {
                    simpleDialog.dismiss()
                }
                simpleDialog.show()

            } else {
                super.onBackPressed()
                if (getFragment() != null && getFragment() is ContactsFrag) {
                    (getFragment() as ContactsFrag).shopContactList("")
                }
            }

        } else if (getFragment() != null && getFragment() is AddOpptFrag) {

            if (!AddOpptFrag.editOprtntyID.equals("")) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialogfragment_common_two)

                val dialog_header_TV_ =
                    simpleDialog.findViewById(R.id.dialog_header_TV_) as AppCustomTextView
                val dialog_content_TV_ =
                    simpleDialog.findViewById(R.id.dialog_content_TV_) as AppCustomTextView
                val cancel_TV_ = simpleDialog.findViewById(R.id.cancel_TV_) as AppCustomTextView
                val ok_TV_ = simpleDialog.findViewById(R.id.ok_TV_) as AppCustomTextView

                cancel_TV_.setOnClickListener {
                    simpleDialog.dismiss()
                }

                dialog_header_TV_.text = AppUtils.hiFirstNameText()
                dialog_content_TV_.text =
                    "Your input has not been saved. Would you like to discard?"
                ok_TV_.setOnClickListener {
                    simpleDialog.dismiss()
                    super.onBackPressed()
                    if (getFragment() != null && getFragment() is ViewCrmOpptFrag) {
                        (getFragment() as ViewCrmOpptFrag).opportunityList("")
                    }
                }
                simpleDialog.show()

            } else {

                if ((getFragment() as AddOpptFrag).checkIsEdited() == true) {


                    val simpleDialog = Dialog(mContext)
                    simpleDialog.setCancelable(false)
                    simpleDialog.getWindow()!!
                        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    simpleDialog.setContentView(R.layout.dialogfragment_common_two)

                    val dialog_header_TV_ =
                        simpleDialog.findViewById(R.id.dialog_header_TV_) as AppCustomTextView
                    val dialog_content_TV_ =
                        simpleDialog.findViewById(R.id.dialog_content_TV_) as AppCustomTextView
                    val cancel_TV_ = simpleDialog.findViewById(R.id.cancel_TV_) as AppCustomTextView
                    val ok_TV_ = simpleDialog.findViewById(R.id.ok_TV_) as AppCustomTextView

                    cancel_TV_.setOnClickListener {
                        simpleDialog.dismiss()
                    }

                    dialog_header_TV_.text = AppUtils.hiFirstNameText()
                    dialog_content_TV_.text =
                        "Your input has not been saved. Would you like to discard?"
                    ok_TV_.setOnClickListener {
                        simpleDialog.dismiss()
                        super.onBackPressed()
                        if (getFragment() != null && getFragment() is ViewCrmOpptFrag) {
                            (getFragment() as ViewCrmOpptFrag).opportunityList("")
                        }
                    }
                    simpleDialog.show()
                } else {
                    super.onBackPressed()
                    if (getFragment() != null && getFragment() is ViewCrmOpptFrag) {
                        (getFragment() as ViewCrmOpptFrag).opportunityList("")
                    }
                }

            }
        } else if (getFragment() != null && getFragment() is TemplateAddFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is TemplateViewFrag) {
                (getFragment() as TemplateViewFrag).setData()
            }
        } else if (getFragment() != null && getFragment() is SchedulerAddFormFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is SchedulerViewFrag) {
                (getFragment() as SchedulerViewFrag).callSchedulerList()

            }
        } else if (getFragment() != null && getFragment() is AddActivityFragment) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is ContactsFrag) {
                (getFragment() as ContactsFrag).updateToolbar()
            }
            if (getFragment() != null && getFragment() is ActivityDtlsFrag) {
                (getFragment() as ActivityDtlsFrag).updateList()
            }
        } else if (getFragment() != null && getFragment() is BookmarkPlayFrag) {
            super.onBackPressed()
            if (getFragment() != null && getFragment() is BookmarkFrag) {
                (getFragment() as BookmarkFrag).updateToolbar()
            }
            if (getFragment() != null && getFragment() is ActivityDtlsFrag) {
                (getFragment() as ActivityDtlsFrag).updateList()
            }
        } else if (getFragment() != null && getFragment() is ContactsFrag) {
            Log.d("login_test_calling12", "")

            loadFragment(FragType.DashboardFragment, false, DashboardType.Home)
        } else if (getFragment() != null && getFragment() is LmsQuestionAnswerSet) {
            var k1 = getFragment()
            super.onBackPressed()
            var k2 = getFragment()
            var k3 = k2
        } else if (getFragment() != null && getFragment() is VideoPlayLMS) {
            (getFragment() as VideoPlayLMS).callDestroy()
            if (VideoPlayLMS.loadedFrom.equals("SearchLmsKnowledgeFrag")) {
                loadFragment(FragType.SearchLmsKnowledgeFrag, false, "")
            }
            else if (VideoPlayLMS.loadedFrom.equals("AllTopicsWiseContents")) {
                try {
                    loadFragment(
                        FragType.AllTopicsWiseContents,
                        false,
                        AllTopicsWiseContents.topic_id + "~" + AllTopicsWiseContents.topic_name
                    )
                } catch (e: Exception) {
                    loadFragment(FragType.AllTopicsWiseContents, false, "")
                }
            }
            /*else if (VideoPlayLMS.loadedFrom.equals("MyTopicsWiseContents")) {
                loadFragment(FragType.MyTopicsWiseContents, false, "")
            }*/
            /*else if (VideoPlayLMS.loadedFrom.equals("SearchLmsFrag")) {
                loadFragment(FragType.SearchLmsFrag, false, "")
            }*/ else if (VideoPlayLMS.loadedFrom.equals("SearchLmsLearningFrag")) {
                try {
                    loadFragment(
                        FragType.SearchLmsLearningFrag,
                        false,
                        SearchLmsLearningFrag.topic_id + "~" + SearchLmsLearningFrag.topic_name
                    )
                } catch (e: Exception) {
                    loadFragment(FragType.SearchLmsLearningFrag, false, "")
                }
            }
            else if (VideoPlayLMS.loadedFrom.equals("MyTopicsWiseContents")) {
                try {
                    loadFragment(
                        FragType.MyTopicsWiseContents,
                        false,
                        MyTopicsWiseContents.topic_id + "~" + MyTopicsWiseContents.topic_name
                    )
                } catch (e: Exception) {
                    loadFragment(FragType.MyTopicsWiseContents, false, "")
                }
            }
            else if (VideoPlayLMS.loadedFrom.equals("LMSDASHBOARD")) {
                loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)
            } else {
                loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)
            }
        } else if (getFragment() != null && getFragment() is SearchLmsLearningFrag) {
            loadFragment(FragType.MyLearningTopicList, false, "")
        }

        else if (getFragment() != null && getFragment() is MyTopicsWiseContents) {
            loadFragment(FragType.SearchLmsFrag, false, "")
        }
        else if (getFragment() != null && getFragment() is AllTopicsWiseContents) {
            loadFragment(FragType.SearchLmsKnowledgeFrag, false, "")
        }

        /*else if(getFragment() != null && getFragment() is VideoPlayLMS){
 super.onBackPressed()
 var frgg = getFragment()
 if (getFragment() != null && getFragment() is SearchLmsFrag){
 (getFragment() as SearchLmsFrag).updateToolbar()
 }else{
 var frgge = getFragment()
 }
 }
 else if(getFragment() != null && getFragment() is MyLearningVideoPlay){
 (getFragment() as MyLearningVideoPlay).onAPICalling()
 super.onBackPressed()
 if (getFragment() != null && getFragment() is SearchLmsLearningFrag){
 (getFragment() as SearchLmsLearningFrag).updateList()
 }
 }*/
        /*else if(getFragment() != null && getFragment() is LmsQuestionAnswerSet){

 }*/
        /*else if (Pref.IsUserWiseLMSFeatureOnly && getFragment() != null ){
 super.onBackPressed()
 }*/
        else {
            var k1 = getFragment()
            super.onBackPressed()
            var k2 = getFragment()
            var k3 = k2

            println("tag_fr \n$k1\n$k2")
            when {
                getFragment() is MemberListFragment -> (getFragment() as MemberListFragment).updateItem()
                getFragment() is LeaveListFragment -> (getFragment() as LeaveListFragment).updateItem()
                getFragment() is ViewPPDDListFragment -> (getFragment() as ViewPPDDListFragment).updateItem()
                getFragment() is ShopDetailFragment -> (getFragment() as ShopDetailFragment).updateItem()
                getFragment() is OrderListFragment -> (getFragment() as OrderListFragment).updateItem()
                getFragment() is DashboardFragment -> (getFragment() as DashboardFragment).updateItem()
                getFragment() is BillingListFragment -> (getFragment() as BillingListFragment).updateItem()
                getFragment() is NewDateWiseOrderListFragment -> (getFragment() as NewDateWiseOrderListFragment).updateItem()
                getFragment() is NewOrderListFragment -> (getFragment() as NewOrderListFragment).updateItem()
                getFragment() is ReimbursementListFragment -> (getFragment() as ReimbursementListFragment).updateFloatingButton()
                getFragment() is MemberShopListFragment -> (getFragment() as MemberShopListFragment).updateAdapter()
                getFragment() is QuotationListFragment -> (getFragment() as QuotationListFragment).updateList()
                getFragment() is OfflineShopListFragment -> (getFragment() as OfflineShopListFragment).updateAdapter()
                getFragment() is TaskListFragment -> (getFragment() as TaskListFragment).updateList()
                getFragment() is ChemistActivityListFragment -> (getFragment() as ChemistActivityListFragment).updateItem()
                getFragment() is DoctorActivityListFragment -> (getFragment() as DoctorActivityListFragment).updateItem()
                getFragment() is DateWiseActivityListFragment -> (getFragment() as DateWiseActivityListFragment).updateList()
                getFragment() is ActivityDetailsListFragment -> (getFragment() as ActivityDetailsListFragment).updateList()
                getFragment() is ChatBotFragment -> (getFragment() as ChatBotFragment).update()
                getFragment() is BeatListFragment -> (getFragment() as BeatListFragment).update()

                getFragment() is UpdateShopStockFragment -> (getFragment() as UpdateShopStockFragment).update()
                getFragment() is CompetetorStockFragment -> (getFragment() as CompetetorStockFragment).update()

                getFragment() is NewOrderScrActiFragment -> (getFragment() as NewOrderScrActiFragment).updateCartQty()

                getFragment() is MicroLearningListFragment -> {
                    val intent = Intent(this, FileOpeningTimeIntentService::class.java)
                    intent.also {
                        it.putExtra(
                            "id",
                            (getFragment() as MicroLearningListFragment).selectedFile?.id
                        )
                        it.putExtra(
                            "start_time",
                            (getFragment() as MicroLearningListFragment).openingDateTime
                        )
                        startService(it)
                    }
                }

                getFragment() is JobsCustomerFragment -> {
                    if (isSubmit) {
                        isSubmit = false
                        isCalledJobApi = true
                        (getFragment() as JobsCustomerFragment).getStatusApi()
                    }
                }


                getFragment() is SearchLmsFrag || getFragment() is SearchLmsKnowledgeFrag ||
                        getFragment() is MyPerformanceFrag || getFragment() is LeaderboardLmsFrag || getFragment() is MyLearningTopicList -> {
                    loadFragment(FragType.MyLearningFragment, false, DashboardType.Home)
                }


                /*getFragment() is VideoPlayLMS -> {
                if(VideoPlayLMS.previousFrag.equals(FragType.KnowledgeHubAllVideoList.toString())){
                loadFragment(FragType.KnowledgeHubAllVideoList, false, "")
                }else if(VideoPlayLMS.previousFrag.equals(FragType.SearchLmsFrag.toString())){
                loadFragment(FragType.SearchLmsFrag, false, "")
                }
                else if(VideoPlayLMS.previousFrag.equals(FragType.LmsQuestionAnswerSet.toString())){
                loadFragment(FragType.LmsQuestionAnswerSet, false, "")
                }
                }
                getFragment() is MyLearningAllVideoList -> {
                loadFragment(FragType.SearchLmsLearningFrag, false, "")
                }
                getFragment() is KnowledgeHubAllVideoList -> {
                loadFragment(FragType.SearchLmsKnowledgeFrag, false, "")
                }
                getFragment() is LmsQuestionAnswerSet -> {
                loadFragment(FragType.LmsQuestionAnswerSet, false, "")
                }*/

            }
        }
// searchView.closeSearch()
    }

    /*fun showAlertInOprtnty(f:Fragment) {

 CommonDialog.getInstance(AppUtils.hiFirstNameText() + "!", "Your input has not been saved. Would you like to discard?", getString(R.string.cancel), getString(R.string.ok), object : CommonDialogClickListener {
 override fun onLeftClick() {
 }

 override fun onRightClick(editableData: String) {
 (f as AddOpptFrag).setISEdited(false)
 onBackPressed()
 }

 }).show((mContext as DashboardActivity).supportFragmentManager, "")
 }*/

    private fun showAlert() {

        if (tv_cart_count.visibility == View.GONE) {
            isShowAlert = false
            AppUtils.isAllSelect = false
            onBackPressed()
            return
        }

        CommonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            "Click Ok to clear the cart and back to the list to start again.",
            getString(R.string.cancel),
            getString(R.string.ok),
            object : CommonDialogClickListener {
                override fun onLeftClick() {
                }

                override fun onRightClick(editableData: String) {
                    isShowAlert = false
                    AppUtils.isAllSelect = false
                    onBackPressed()
                }

            }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private var locationInfoModel: locationInfoModel? = null
    fun getLocationInfoModel(mlocationInfoModel: locationInfoModel) {
        locationInfoModel = mlocationInfoModel
    }

    private fun initBackStackActionSet() {
        supportFragmentManager.addOnBackStackChangedListener {
            getFragInstance(getCurrentFragType(), "", false)
        }
    }

    private fun performLogout() {
        CommonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            getString(R.string.confirm_logout),
            getString(R.string.cancel),
            getString(R.string.ok),
            object : CommonDialogClickListener {
                override fun onLeftClick() {

                }

                override fun onRightClick(editableData: String) {
                    if (AppUtils.isOnline(this@DashboardActivity)) {

                        if (Pref.isShowLogoutReason && !TextUtils.isEmpty(Pref.approvedOutTime)) {
                            val currentTimeInLong =
                                AppUtils.convertTimeWithMeredianToLong(AppUtils.getCurrentTimeWithMeredian())
                            val approvedOutTimeInLong =
                                AppUtils.convertTimeWithMeredianToLong(Pref.approvedOutTime)

                            if (currentTimeInLong < approvedOutTimeInLong)
                                showLogoutLocReasonDialog()
                            else
                                calllogoutApi(Pref.user_id!!, Pref.session_token!!)
                        } else
                            calllogoutApi(Pref.user_id!!, Pref.session_token!!)


                        /*val list = AppDatabase.getDBInstance()!!.gpsStatusDao().getDataSyncStateWise(false)

 if (list != null && list.isNotEmpty()) {
 i = 0
 callUpdateGpsStatusApi(list)
 } else {
 calllogoutApi(Pref.user_id!!, Pref.session_token!!)
 }*/

                        // loadFragment(FragType.LogoutSyncFragment, false, "")

                    } else
                        showSnackMessage("Good internet must required to logout, please switch on the internet and proceed. Thanks.")

                    /*AppUtils.isLoginLoaded = false
 val serviceLauncher = Intent(this@DashboardActivity, LocationFuzedService::class.java)
 stopService(serviceLauncher)

 startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
 overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
 finish()*/
                }

            }).show(supportFragmentManager, "")
    }

    private fun showLogoutLocReasonDialog() {
        reasonDialog = null
        val body =
            "You applicable out time is: ${Pref.approvedOutTime}!. You are doing early logout. Please write below the reason."
        reasonDialog = ReasonDialog.getInstance(AppUtils.hiFirstNameText() + "!", body, reason) {
            if (!AppUtils.isOnline(this))
                Toaster.msgShort(this, getString(R.string.no_internet))
            else {
                reasonDialog?.dismiss()
                submitLogoutReason(it)
            }
        }
        reasonDialog?.show(supportFragmentManager, "")
    }

    private fun submitLogoutReason(mReason: String) {
        progress_wheel.spin()
        val repository = DashboardRepoProvider.provideDashboardRepository()
        BaseActivity.compositeDisposable.add(
            repository.submiLogoutReason(mReason)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        reason = ""
                        calllogoutApi(Pref.user_id!!, Pref.session_token!!)
                    } else {
                        reason = mReason
                        showLogoutLocReasonDialog()
                        Toaster.msgShort(mContext, result.message!!)
                    }

                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    reason = mReason
                    showLogoutLocReasonDialog()
                    Toaster.msgShort(mContext, getString(R.string.something_went_wrong))
                })
        )
    }

    private fun callUpdateGpsStatusApi(list: List<GpsStatusEntity>) {

        val updateGps = UpdateGpsInputParamsModel()
        updateGps.date = list[i].date
        updateGps.gps_id = list[i].gps_id
        updateGps.gps_off_time = list[i].gps_off_time
        updateGps.gps_on_time = list[i].gps_on_time
        updateGps.user_id = Pref.user_id
        updateGps.session_token = Pref.session_token
        updateGps.duration = AppUtils.getTimeInHourMinuteFormat(list[i].duration?.toLong()!!)

        progress_wheel.spin()
        val repository = UpdateGpsStatusRepoProvider.updateGpsStatusRepository()
        BaseActivity.compositeDisposable.add(
            repository.updateGpsStatus(updateGps)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val gpsStatusResponse = result as BaseResponse
                    Timber.d(
                        "GPS SYNC : " + "RESPONSE : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name
                                + ",MESSAGE : " + gpsStatusResponse.message
                    )
                    if (gpsStatusResponse.status == NetworkConstant.SUCCESS) {
                        AppDatabase.getDBInstance()!!.gpsStatusDao()
                            .updateIsUploadedAccordingToId(true, list[i].id)
                    }

                    i++
                    if (i < list.size) {
                        callUpdateGpsStatusApi(list)
                    } else {
                        i = 0
                        progress_wheel.stopSpinning()
                        calllogoutApi(Pref.user_id!!, Pref.session_token!!)
                    }

                }, { error ->
                    //
                    Timber.d("GPS SYNC : " + "RESPONSE ERROR: " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                    error.printStackTrace()
                    i++
                    if (i < list.size) {
                        callUpdateGpsStatusApi(list)
                    } else {
                        i = 0
                        progress_wheel.stopSpinning()
                        calllogoutApi(Pref.user_id!!, Pref.session_token!!)
                    }
                })
        )
    }


    fun syncShopList() {

        Pref.logout_time = AppUtils.getCurrentTimeWithMeredian()

        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        super.clearData()
    }

    fun openLocationWithTrack() {
        startActivity(Intent(this@DashboardActivity, MapActivity::class.java))
        overridePendingTransition(0, 0)
    }

    fun openLocationMap(customerData: CustomerDataModel, isCurrentLocShow: Boolean) {
        val mapIntent = Intent(this@DashboardActivity, MapActivityWithoutPath::class.java)
        mapIntent.also {
            it.putExtra("latitude", customerData.latitude)
            it.putExtra("longitude", customerData.longitude)
            it.putExtra("shopname", customerData.name)
            it.putExtra("address", customerData.address)
            it.putExtra("isCurrentLocShow", isCurrentLocShow)
            it.putExtra("isOrderLoc", false)
            it.putExtra("orderLat", "0.0")
            it.putExtra("orderLong", "0.0")
            it.putExtra("orderNo", "")
            it.putExtra("orderAddress", "")
            startActivity(it)
        }
        overridePendingTransition(0, 0)

    }

    fun openLocationMap(mAddShopDBModelEntity: AddShopDBModelEntity, isCurrentLocShow: Boolean) {
        val mapIntent: Intent = Intent(this@DashboardActivity, MapActivityWithoutPath::class.java)
        mapIntent.putExtra("latitude", mAddShopDBModelEntity.shopLat.toString())
        mapIntent.putExtra("longitude", mAddShopDBModelEntity.shopLong.toString())
        mapIntent.putExtra("shopname", mAddShopDBModelEntity.shopName)
        mapIntent.putExtra("address", mAddShopDBModelEntity.address)
        mapIntent.putExtra("isCurrentLocShow", isCurrentLocShow)
        mapIntent.putExtra("isOrderLoc", false)
        mapIntent.putExtra("orderLat", "0.0")
        mapIntent.putExtra("orderLong", "0.0")
        mapIntent.putExtra("orderNo", "")
        mapIntent.putExtra("orderAddress", "")
        startActivity(mapIntent)
        overridePendingTransition(0, 0)

    }

    /*20-12-2021*/
    fun openLocationMap(returnObj: ReturnDetailsEntity, isCurrentLocShow: Boolean) {
        val mapIntent = Intent(this@DashboardActivity, MapActivityWithoutPath::class.java)
        val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(returnObj.shop_id)

        mapIntent.putExtra("latitude", shop?.shopLat.toString())
        mapIntent.putExtra("longitude", shop?.shopLong.toString())
        mapIntent.putExtra("shopname", shop?.shopName)
        mapIntent.putExtra("address", shop?.address)
        mapIntent.putExtra("orderLat", returnObj.return_lat)
        mapIntent.putExtra("orderLong", returnObj.return_long)
        mapIntent.putExtra("orderNo", returnObj.return_id)
        mapIntent.putExtra(
            "orderAddress",
            LocationWizard.getLocationName(
                this,
                returnObj.return_lat?.toDouble()!!,
                returnObj.return_long?.toDouble()!!
            )
        )
        mapIntent.putExtra("isCurrentLocShow", isCurrentLocShow)
        mapIntent.putExtra("isOrderLoc", true)
        startActivity(mapIntent)
        overridePendingTransition(0, 0)
    }


    fun openLocationMap(order: OrderDetailsListEntity, isCurrentLocShow: Boolean) {
        val mapIntent = Intent(this@DashboardActivity, MapActivityWithoutPath::class.java)
        val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(order.shop_id)

        mapIntent.putExtra("latitude", shop?.shopLat.toString())
        mapIntent.putExtra("longitude", shop?.shopLong.toString())
        mapIntent.putExtra("shopname", shop?.shopName)
        mapIntent.putExtra("address", shop?.address)
        mapIntent.putExtra("orderLat", order.order_lat)
        mapIntent.putExtra("orderLong", order.order_long)
        mapIntent.putExtra("orderNo", order.order_id)
        mapIntent.putExtra(
            "orderAddress",
            LocationWizard.getLocationName(
                this,
                order.order_lat?.toDouble()!!,
                order.order_long?.toDouble()!!
            )
        )
        mapIntent.putExtra("isCurrentLocShow", isCurrentLocShow)
        mapIntent.putExtra("isOrderLoc", true)
        startActivity(mapIntent)
        overridePendingTransition(0, 0)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 7009) {
                try {
                    val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etSearchMenu.setText(result!![0].toString())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            if (requestCode == PermissionHelper.REQUEST_CODE_CAMERA) {
                //Timber.d("DashboardActivity : " + " , " + " Camera Image FilePath :" + FTStorageUtils.IMG_URI)
                if (AppUtils.isRevisit!!) {

                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {

                        Timber.e("===========RevisitShop Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }


                        //revisitShop()

                        /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 20)
 newFile = processImage.ProcessImage()

 uiThread {
 progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 addShopVisitPic(newFile!!.length(), imageUpDateTime)
 }
 else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 addShopVisitPic(fileSize, imageUpDateTime)
 }

 val shopDetail = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(mShopId)
 if (shopDetail.is_otp_verified.equals("false", ignoreCase = true)) {
 if (AppUtils.isOnline(this@DashboardActivity))
 showShopVerificationDialog()
 else
 loadFragment(FragType.ShopDetailFragment, true, mShopId)
 } else
 loadFragment(FragType.ShopDetailFragment, true, mShopId)
 }
 }*/

                        /*OTPVerificationDialog.getInstance(object : OTPVerificationDialog.OnOTPButtonClickListener {
 override fun onOkButtonClick() {
 }
 }).show((mContext as DashboardActivity).supportFragmentManager, "OTPVerificationDialog")*/
                    }
                } else if (Pref.isSefieAlarmed) {
                    getCameraImage(data)
                    val fileSize = AppUtils.getCompressImage(filePath)

                    val fileSizeInKB = fileSize / 1024
                    Log.e(
                        "Dashboard",
                        "image file size after compression==========> $fileSizeInKB KB"
                    )

                    uploadSelfie(File(filePath))

                } else if (isVisitCardScan) {
                    getCameraImage(data)
                    /*val fileSize = AppUtils.getCompressImage(filePath)

 val fileSizeInKB = fileSize / 1024
 Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

 isVisitCardScan = false
 if (getFragment() != null && getFragment() is AddShopFragment)
 (getFragment() as AddShopFragment).processImage(File(filePath))*/

                    if (!TextUtils.isEmpty(filePath)) {

                        Timber.e("===========Visiting Card Scan Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.AddShopFragment /*&& FTStorageUtils.IMG_URI != null*/) {
                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/


                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Add Shop Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")
                        /*14-12-2021*/
                        if (Pref.IsnewleadtypeforRuby) {
                            try {
                                CropImage.activity(contentURI)
                                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                                    .setMinCropWindowSize(500, 500)
                                    .setAspectRatio(1, 1)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setOutputCompressQuality(100)
                                    .start(this)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Timber.e("Error: " + e.localizedMessage)
                            }
                        } else {
                            try {
                                CropImage.activity(contentURI)
                                    .setAspectRatio(40, 21)
                                    .start(this)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Timber.e("Error: " + e.localizedMessage)
                            }
                        }
                    }

                } else if (getCurrentFragType() == FragType.ProtoRegistrationFragment) {
                    // request for camera image
                    getCameraImage(data)
                    if (!TextUtils.isEmpty(filePath)) {
                        //val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, filePath)
                        //Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")
                        //val fileSize = AppUtils.getCompressBillingImage(contentURI.toString(), this)
                        //updatePhotoRegAadhaarCroppedImg(fileSize, contentURI)

                        (getFragment() as ProtoRegistrationFragment).setImage(filePath)

                    }

                } else if (getCurrentFragType() == FragType.MultipleImageFragment) {
                    // request for camera image
                    getCameraImage(data)
                    if (!TextUtils.isEmpty(filePath)) {
// AppUtils.getCompressImage(filePath.toString())
                        AppUtils.getCompressOldImage(filePath.toString(), this)
                        (getFragment() as MultipleImageFragment).setImagecapture(filePath)

                    }
                }
                // start Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
                else if (getCurrentFragType() == FragType.MultipleImageFileUploadonStock) {
                    // request for camera image
                    getCameraImage(data)
                    if (!TextUtils.isEmpty(filePath)) {
// AppUtils.getCompressImage(filePath.toString())
                        AppUtils.getCompressOldImage(filePath.toString(), this)
                        println("stock_img set img 5 hit")
                        (getFragment() as MultipleImageFileUploadonStock).setImagecapture(filePath)

                    }
                }
                //end Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
                else if (getCurrentFragType() == FragType.MyProfileFragment /*&& FTStorageUtils.IMG_URI != null*/) {
                    /*AppUtils.getCompressContentImage(FTStorageUtils.IMG_URI, this)
 (getFragment() as MyProfileFragment).setImage(FTStorageUtils.IMG_URI)*/

                    getCameraImage(data)
                    /*val fileSize = AppUtils.getCompressImage(filePath)
 editProfilePic(fileSize)*/

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Profile Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                //.setAspectRatio(40, 21)//mantis id 0027192 Suman date 18-01-2024
                                .setAspectRatio(40, 40)//mantis id 0027192 Suman date 18-01-2024
                                .setCropShape(CropImageView.CropShape.OVAL)//mantis id 0027192 Suman date 18-01-2024
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }


                } else if (getCurrentFragType() == FragType.ReimbursementFragment) {
                    /*AppUtils.getCompressContentImage(FTStorageUtils.IMG_URI, this)
 (getFragment() as MyProfileFragment).setImage(FTStorageUtils.IMG_URI)*/

                    getCameraImage(data)

                    /* val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 //progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementPic(fileSize)
 }
 }
 }*/

                    (getFragment() as ReimbursementFragment).setImage(filePath)

                } else if (getCurrentFragType() == FragType.ReimbursementNFrag) {
                    /*AppUtils.getCompressContentImage(FTStorageUtils.IMG_URI, this)
 (getFragment() as MyProfileFragment).setImage(FTStorageUtils.IMG_URI)*/

                    getCameraImage(data)

                    /* val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 //progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementPic(fileSize)
 }
 }
 }*/

                    (getFragment() as ReimbursementNFrag).setImage(filePath)

                } else if (getCurrentFragType() == FragType.EditReimbursementFragment) {
                    /*AppUtils.getCompressContentImage(FTStorageUtils.IMG_URI, this)
 (getFragment() as MyProfileFragment).setImage(FTStorageUtils.IMG_URI)*/

                    getCameraImage(data)

                    /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 //progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementEditPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementEditPic(fileSize)
 }
 }
 }*/

                    (getFragment() as EditReimbursementFragment).setImage(filePath)

                } else if (getCurrentFragType() == FragType.MarketingPagerFragment && FTStorageUtils.IMG_URI != null) {
                    AppUtils.getCompressImage(FTStorageUtils.IMG_URI.toString())
                    (getFragment() as MarketingPagerFragment).setImage(FTStorageUtils.IMG_URI)
                } else if (getCurrentFragType() == FragType.ViewAllTAListFragment && FTStorageUtils.IMG_URI != null) {

                    AppUtils.getCompressImage(FTStorageUtils.IMG_URI.toString())
                    (getFragment() as ViewAllTAListFragment).getCaptureImage(FTStorageUtils.IMG_URI)

                } else if (getCurrentFragType() == FragType.AddBillingFragment) {

                    getCameraImage(data)

                    /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 addBillingPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 addBillingPic(fileSize)
 }
 }
 }*/

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Add Billing Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            /*CropImage.activity(contentURI)
 .setAspectRatio(40, 21)
 .start(this)*/
                            CropImage.activity(contentURI)
                                //.setAspectRatio(40, 21)
                                //.setAspectRatio(AppUtils.getScreenWidth()*2,AppUtils.getScreenHeight()*2)
                                .setMinCropWindowSize(
                                    AppUtils.getScreenWidth() * 2,
                                    AppUtils.getScreenHeight() * 2
                                )
                                //.setAspectRatio(2, 6)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }


                } else if (getCurrentFragType() == FragType.NearByShopsListFragment || getCurrentFragType() == FragType.NewDateWiseOrderListFragment ||
                    getCurrentFragType() == FragType.NewOrderListFragment || getCurrentFragType() == FragType.ShopBillingListFragment ||
                    getCurrentFragType() == FragType.ViewAllOrderListFragment
                ) {

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Add Collection Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }


                } else if (getCurrentFragType() == FragType.AddDynamicFragment) {

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Add Dynamic form Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.EditDynamicFragment) {

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Edit Dynamic form Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.AddActivityFragment) {

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Add Activity form Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.EditActivityFragment) {

                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Edit Activity form Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.ShopDetailFragment /*&& FTStorageUtils.IMG_URI != null*/) {
                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/


                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/


                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Edit Shop Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }


                } else if (getCurrentFragType() == FragType.AddAttendanceFragment /*&& FTStorageUtils.IMG_URI != null*/) {
                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/


                    /*getCameraImage(data)

 if (!TextUtils.isEmpty(filePath)) {
 Timber.e("===========Add Attendance Image (DashboardActivity)===========")
 Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

 val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, File(Uri.parse(filePath).path).absolutePath)

 Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

 try {
 CropImage.activity(contentURI)
 .setAspectRatio(40, 21)
 .start(this)
 } catch (e: Exception) {
 e.printStackTrace()
 Timber.e("Error: " + e.localizedMessage)
 }
 }*/

                    getCameraImage(data)
                    val fileSize = AppUtils.getCompressImage(filePath)
                    addAttendanceImg(fileSize, true)

                } else if (getCurrentFragType() == FragType.DailyPlanListFragment /*&& FTStorageUtils.IMG_URI != null*/) {
                    /*CropImage.activity(FTStorageUtils.IMG_URI)
 .setAspectRatio(40, 21)
 .start(this)*/


                    /*getCameraImage(data)

 if (!TextUtils.isEmpty(filePath)) {
 Timber.e("===========Add Attendance Image (DashboardActivity)===========")
 Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

 val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, File(Uri.parse(filePath).path).absolutePath)

 Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

 try {
 CropImage.activity(contentURI)
 .setAspectRatio(40, 21)
 .start(this)
 } catch (e: Exception) {
 e.printStackTrace()
 Timber.e("Error: " + e.localizedMessage)
 }
 }*/

                    getCameraImage(data)
                    val fileSize = AppUtils.getCompressImage(filePath)
                    addAttendanceImg(fileSize, false)

                } else if (getCurrentFragType() == FragType.AddTimeSheetFragment) {
                    getCameraImage(data)
                    (getFragment() as AddTimeSheetFragment).setImage(filePath)

                } else if (getCurrentFragType() == FragType.EditTimeSheetFragment) {
                    getCameraImage(data)
                    (getFragment() as EditTimeSheetFragment).setImage(filePath)

                } else if (getCurrentFragType() == FragType.DocumentListFragment) {
                    getCameraImage(data)
                    (getFragment() as DocumentListFragment).setImage(filePath)
                } else if (getCurrentFragType() == FragType.SchedulerAddFormFrag) {
                    getCameraImage(data)
                    (getFragment() as SchedulerAddFormFrag).setImage(filePath)
                } else if (getCurrentFragType() == FragType.WorkInProgressFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Work in Progress Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.WorkOnHoldFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Work in Hold Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.WorkCompletedFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Work Completed Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.WorkCancelledFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Work Cancelled Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.UpdateReviewFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Update Review Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setAspectRatio(40, 21)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.ShopDamageProductSubmitFrag) {

                    getCameraImage(data)
                    val fileSize = AppUtils.getCompressImage(filePath)
                    val fileSizeInKB = fileSize / 1024
                    val file = File(filePath)
                    (getFragment() as ShopDamageProductSubmitFrag).setImage(file)


                    /*getCameraImage(data)
 if (!TextUtils.isEmpty(filePath)) {
 Timber.e("===========Update Review Image (DashboardActivity)===========")
 Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

 val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, File(Uri.parse(filePath).path).absolutePath)

 Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

 try {
 CropImage.activity(contentURI)
 .setCropShape(CropImageView.CropShape.RECTANGLE)
 .setMinCropWindowSize(500, 500)
 .setAspectRatio(1, 1)
 .setGuidelines(CropImageView.Guidelines.ON)
 .setOutputCompressQuality(100)
 .start(this)
 } catch (e: Exception) {
 e.printStackTrace()
 Timber.e("Error: " + e.localizedMessage)
 }
 }*/
                } else if (getCurrentFragType() == FragType.SurveyFrag) {
                    getCameraImage(data)
                    val fileSize = AppUtils.getCompressImage(filePath)
                    val fileSizeInKB = fileSize / 1024
                    val file = File(filePath)
                    (getFragment() as SurveyFrag).setImage(file)
                } else if (getCurrentFragType() == FragType.RegisTerFaceFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Update Review Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setMinCropWindowSize(500, 500)
                                .setAspectRatio(1, 1)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setOutputCompressQuality(100)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.PhotoRegAadhaarFragment) {
                    getCameraImage(data)

                    if (!TextUtils.isEmpty(filePath)) {
                        Timber.e("===========Update Review Image (DashboardActivity)===========")
                        Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")

                        val contentURI = FTStorageUtils.getImageContentUri(
                            this@DashboardActivity,
                            File(Uri.parse(filePath).path).absolutePath
                        )

                        Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")

                        try {
                            CropImage.activity(contentURI)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                //.setMinCropWindowSize(500, 400)
                                .setAspectRatio(40, 30)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setOutputCompressQuality(100)
                                .start(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: " + e.localizedMessage)
                        }
                    }
                } else if (getCurrentFragType() == FragType.DashboardFragment) {
                    getCameraImage(data)
                    if (!TextUtils.isEmpty(filePath)) {
                        //30-08-2021
// Timber.e("===========Update Review Image (DashboardActivity)===========")
// Timber.e("DashboardActivity : , Camera Image FilePath : $filePath")
//
// val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, File(Uri.parse(filePath).path).absolutePath)

                        (getFragment() as DashboardFragment).setImage(filePath)


// Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")
//
// try {
// CropImage.activity(contentURI)
// .setAspectRatio(40, 21)
// .start(this)
// } catch (e: Exception) {
// e.printStackTrace()
// Timber.e("Error: " + e.localizedMessage)
// }
                    }
                }
            }

            // 0025683 start
            else if (requestCode == MaterialSearchView.REQUEST_VOICE) {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t = result!![0]
                try {
                    searchView.setQuery(t, false)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


// tv_search_frag_order_type_list.setText(t)
// tv_search_frag_order_type_list.setSelection(t.length);
            } else if (ImagePickerManager.REQUEST_GET_GALLERY_PHOTO == requestCode && null != data) {
                //val filePath = ImagePickerManager.getImagePathFromData(data, this)
                if (getCurrentFragType() == FragType.ViewAllTAListFragment) {
                    if (data != null)
                        (getFragment() as ViewAllTAListFragment).showPickedFileFromGalleryFetch(data)
                }

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    val resultUri = result.uri


                    if (feedBackDialogCompetetorImg) {
                        val fileSize = AppUtils.getCompressOldImage(resultUri.toString(), this)
                        feedbackDialog?.setImage(resultUri, fileSize / 1024)
                        feedBackDialogCompetetorImg = false
                        return
                    }

                    if (AppUtils.isRevisit!!) {
                        callFeedbackDialog(resultUri.toString())
                        //revisitShop(resultUri.toString())
                    } else if (isVisitCardScan) {
                        isVisitCardScan = false
                        if (getFragment() != null && getFragment() is AddShopFragment) {
                            //(getFragment() as AddShopFragment).processImage(/*File(resultUri.path!!)*/)
                        }
                    } else {
                        when {
                            getCurrentFragType() == FragType.RegisTerFaceFragment -> {
                                val fileSize =
                                    AppUtils.getCompressOldImage(resultUri.toString(), this)
                                //(getFragment() as RegisTerFaceFragment).setImageData(result!!)
                                getAddFacePic(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.ShopDamageProductSubmitFrag -> {
                                val fileSize =
                                    AppUtils.getCompressOldImage(resultUri.toString(), this)
                                getDamagedPic(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.PhotoRegAadhaarFragment -> {
                                val fileSize =
                                    AppUtils.getCompressOldImageForFace(resultUri.toString(), this)
                                getAddAadhaarVerifyPic(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.AddShopFragment -> {
                                var fileSize =
                                    AppUtils.getCompressOldImage(resultUri.toString(), this)

                                //new image compress
                                var qlty = 30
                                while ((fileSize / 1024) > 50) {
                                    /* qlty=qlty-5
 if(qlty<5){
 break
 }*/
                                    fileSize = AppUtils.getCompressOldImagev1(
                                        resultUri.toString(),
                                        this,
                                        qlty
                                    )
                                }

                                getAddShopPic(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.ShopDetailFragment -> {
                                //AppUtils.getCompressImage(resultUri.toString())
                                val fileSize =
                                    AppUtils.getCompressOldImage(resultUri.toString(), this)
                                getEditShopPic(fileSize, resultUri)
                                //(getFragment() as ShopDetailFragment).setImage(resultUri)
                            }

                            getCurrentFragType() == FragType.AddBillingFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addBillingCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.AddAttendanceFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addAttendanceCroppedImg(fileSize, resultUri, true)
                            }

                            getCurrentFragType() == FragType.DailyPlanListFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addAttendanceCroppedImg(fileSize, resultUri, false)
                            }

                            getCurrentFragType() == FragType.DashboardFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addDashboardStartCroppedImg(fileSize, resultUri, false)
                            }

                            getCurrentFragType() == FragType.MyProfileFragment -> {
                                var fileSize =
                                    AppUtils.getCompressOldImage(resultUri.toString(), this)

                                //new image compress
                                var qlty = 30
                                while ((fileSize / 1024) > 50) {
                                    /*qlty=qlty-5
 if(qlty<5){
 break
 }*/
                                    fileSize = AppUtils.getCompressOldImagev1(
                                        resultUri.toString(),
                                        this,
                                        qlty
                                    )
                                }

                                editProfilePic(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.AddDynamicFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addDynamicFormCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.EditDynamicFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                editDynamicFormCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.AddActivityFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                addActivityFormCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.EditActivityFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                editActivityFormCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.WorkInProgressFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                wipCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.WorkOnHoldFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                wohCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.WorkCompletedFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                workCompletedCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.WorkCancelledFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                workCancelledCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.UpdateReviewFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                updateReviewCroppedImg(fileSize, resultUri)
                            }

                            getCurrentFragType() == FragType.ProtoRegistrationFragment -> {
                                val fileSize =
                                    AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                updatePhotoRegAadhaarCroppedImg(fileSize, resultUri)
                            }

                            else -> {
                                if (getCurrentFragType() == FragType.NearByShopsListFragment || getCurrentFragType() == FragType.NewDateWiseOrderListFragment ||
                                    getCurrentFragType() == FragType.NewOrderListFragment || getCurrentFragType() == FragType.ShopBillingListFragment ||
                                    getCurrentFragType() == FragType.ViewAllOrderListFragment
                                ) {
                                    val fileSize =
                                        AppUtils.getCompressBillingImage(resultUri.toString(), this)
                                    addCollectionCroppedImg(fileSize, resultUri)
                                }
                            }
                        }
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    var error = result.error
                }
//
            } else if (requestCode == PermissionHelper.REQUEST_CODE_STORAGE) {
                if (getCurrentFragType() == FragType.MultipleImageFragment) {
                    /*AppUtils.getCompressImage(data!!.data.toString())
 var newUri = data.data!!
 //new image compress
 var qlty=30
 var fileSize : Long = 0
 while((fileSize/1024)>50) {
 fileSize = AppUtils.getCompressOldImagev1(newUri.toString(), this, qlty)
 }
 (getFragment() as MultipleImageFragment).setImage(newUri,fileSize)
 //getAddMultiPic(fileSize, resultUri)

 //AppUtils.getCompressImage(data!!.data.toString())
 //(getFragment() as MultipleImageFragment).setImageFromPath(data.data!!)*/

                    getGalleryImage(this, data)
                    (getFragment() as MultipleImageFragment).setImageFromPath(filePath)

                }
                // start Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
                if (getCurrentFragType() == FragType.MultipleImageFileUploadonStock) {
                    getGalleryImage(this, data)
                    println("stock_img set img 1 hit")
                    (getFragment() as MultipleImageFileUploadonStock).setImageFromPath(filePath)

                }
                // Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101

                else if (getCurrentFragType() == FragType.MyProfileFragment) {
                    //AppUtils.getCompressContentImage(data!!.data, this)

                    /*getGalleryImage(this, data)
 val fileSize = AppUtils.getCompressImage(filePath)
 editProfilePic(fileSize)*/

                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)

                    //(getFragment() as MyProfileFragment).setImage(data.data)
                } else if (getCurrentFragType() == FragType.MarketingPagerFragment) {
                    AppUtils.getCompressImage(data!!.data.toString())
                    (getFragment() as MarketingPagerFragment).setImage(data.data!!)
                } else if (getCurrentFragType() == FragType.AddShopFragment) {

                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    /*14-12-2021*/
                    if (Pref.IsnewleadtypeforRuby) {
                        CropImage.activity(data.data)
                            .setMinCropWindowSize(500, 500)
                            .setAspectRatio(1, 1)
                            .start(this)
                    } else {
                        CropImage.activity(data.data)
                            .setAspectRatio(40, 21)
                            .start(this)
                    }

                } else if (getCurrentFragType() == FragType.ShopDetailFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.AddBillingFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    /*CropImage.activity(data.data)
 .setAspectRatio(40, 21)
 .start(this)*/

                    try {
                        CropImage.activity(data.data)
                            //.setAspectRatio(40, 21)
                            //.setAspectRatio(AppUtils.getScreenWidth()*2,AppUtils.getScreenHeight()*2)
                            .setMinCropWindowSize(
                                AppUtils.getScreenWidth() * 2,
                                AppUtils.getScreenHeight() * 2
                            )
                            //.setAspectRatio(2, 6)
                            .start(this)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                } else if (getCurrentFragType() == FragType.AddDynamicFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.EditDynamicFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.AddActivityFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.EditActivityFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.NearByShopsListFragment || getCurrentFragType() == FragType.NewDateWiseOrderListFragment ||
                    getCurrentFragType() == FragType.NewOrderListFragment || getCurrentFragType() == FragType.ShopBillingListFragment ||
                    getCurrentFragType() == FragType.ViewAllOrderListFragment
                ) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.ReimbursementFragment) {
                    //AppUtils.getCompressContentImage(data!!.data, this)

                    getGalleryImage(this, data)
                    /*val fileSize = AppUtils.getCompressImage(filePath)
 editProfilePic(fileSize)*/

                    /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 //progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Gallery Image from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementPic(fileSize)
 }
 }
 }*/

                    (getFragment() as ReimbursementFragment).setImage(filePath)

                    //(getFragment() as MyProfileFragment).setImage(data.data)
                } else if (getCurrentFragType() == FragType.ReimbursementNFrag) {
                    //AppUtils.getCompressContentImage(data!!.data, this)

                    getGalleryImage(this, data)
                    /*val fileSize = AppUtils.getCompressImage(filePath)
 editProfilePic(fileSize)*/

                    /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 //progress_wheel.stopSpinning()
 if (newFile != null) {
 Timber.e("=========Gallery Image from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementPic(fileSize)
 }
 }
 }*/

                    (getFragment() as ReimbursementNFrag).setImage(filePath)

                    //(getFragment() as MyProfileFragment).setImage(data.data)
                } else if (getCurrentFragType() == FragType.EditReimbursementFragment) {
                    //AppUtils.getCompressContentImage(data!!.data, this)

                    getGalleryImage(this, data)
                    /*val fileSize = AppUtils.getCompressImage(filePath)
 editProfilePic(fileSize)*/

                    /*val file = File(filePath)
 var newFile: File? = null

 progress_wheel.spin()
 doAsync {

 val processImage = ProcessImageUtils_v1(this@DashboardActivity, file, 50)
 newFile = processImage.ProcessImage()

 uiThread {
 if (newFile != null) {
 Timber.e("=========Image Capture from new technique==========")
 filePath = newFile?.absolutePath!!
 reimbursementEditPic(newFile!!.length())
 } else {
 // Image compression
 val fileSize = AppUtils.getCompressImage(filePath)
 reimbursementEditPic(fileSize)
 }
 }
 }*/

                    (getFragment() as EditReimbursementFragment).setImage(filePath)

                    //(getFragment() as MyProfileFragment).setImage(data.data)
                } else if (getCurrentFragType() == FragType.DocumentListFragment) {
                    getGalleryImage(this, data)
                    (getFragment() as DocumentListFragment).setImage(filePath)
                } else if (getCurrentFragType() == FragType.SchedulerAddFormFrag) {
                    getGalleryImage(this, data)
                    (getFragment() as SchedulerAddFormFrag).setImage(filePath)
                } else if (getCurrentFragType() == FragType.WorkInProgressFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.WorkOnHoldFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.WorkCompletedFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.WorkCancelledFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.UpdateReviewFragment) {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)
                } else if (getCurrentFragType() == FragType.RegisTerFaceFragment) {
                    CropImage.activity(data?.data)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setMinCropWindowSize(400, 400)
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowRotation(false)
                        .setOutputCompressQuality(100)
                        .start(this)
                } else if (getCurrentFragType() == FragType.ProtoRegistrationFragment) {
                    // for gallary image
                    getGalleryImage(this, data)
                    if (!TextUtils.isEmpty(filePath)) {
                        //val contentURI = FTStorageUtils.getImageContentUri(this@DashboardActivity, filePath)
                        //Timber.e("DashboardActivity : , contentURI FilePath : $contentURI")
                        //val fileSize = AppUtils.getCompressBillingImage(contentURI.toString(), this)
                        //updatePhotoRegAadhaarCroppedImg(fileSize, contentURI)

                        (getFragment() as ProtoRegistrationFragment).setImage(filePath)

                    }
                } else if (getCurrentFragType() == FragType.SurveyFrag) {
                    getGalleryImage(this, data)
                    (getFragment() as SurveyFrag).setImageFromPath(filePath)
                } else {
                    Timber.d("DashboardActivity : " + " , " + " Gallery Image FilePath :" + data!!.data)
                    CropImage.activity(data.data)
                        .setAspectRatio(40, 21)
                        .start(this)

                }

            } else if (requestCode == REQUEST_CODE_DOCUMENT) {
                try {
                    if (data != null && data.data != null) {
                        // filePath = NewFileUtils.getRealPath(this@DashboardActivity, data.data)
                        filePath =
                            NewFileUtils.getFilePathFromUri(this@DashboardActivity, data.data)


                        //file chooser path begin
                        /* var customPath = data!!.data!!.path
 var customFinalPath = customPath!!.substring(customPath!!.indexOf(":") + 1);
 Pref.scheduler_file = customFinalPath*/
                        //file chooser path end


                        if (filePath.contains("_.*_")) {
                            showSnackMessage("Invalid file path")
                            return
                        }

                        if (filePath.contains("google")) {
                            showSnackMessage("Can not select document from google drive")
                            return
                        }

                        val file = File(filePath)

                        val extension = getExtension(file)

                        try {
                            Log.e("Dashboard", "extension======> $extension")
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                        if (extension.contains("msword") || extension.contains("doc") || extension.contains(
                                "docx"
                            ) ||
                            extension.contains("xls") || extension.contains("xlsx") || extension.contains(
                                "pdf"
                            ) ||
                            extension.contains("jpg") || extension.contains("jpeg") || extension.contains(
                                "png"
                            )
                        ) {

                            if (extension.contains("jpg") || extension.contains("jpeg") || extension.contains(
                                    "png"
                                )
                            ) {
                                if (getCurrentFragType() == FragType.DocumentListFragment) {
                                    getGalleryImage(this, data)
                                    (getFragment() as DocumentListFragment).setImage(filePath)
                                } else {
                                    CropImage.activity(data.data)
                                        .setAspectRatio(40, 21)
                                        .start(this)
                                }
                            } else {
                                when {
                                    getCurrentFragType() == FragType.AddBillingFragment -> addBillingPic(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.AddDynamicFragment -> addDynamicFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.EditDynamicFragment -> editDynamicFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.AddActivityFragment -> addActivityFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.EditActivityFragment -> editActivityFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.DocumentListFragment -> addEditDocFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.SchedulerAddFormFrag -> addInAddSchedulerFormImageDocFormDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.WorkInProgressFragment -> wipDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.WorkOnHoldFragment -> wohDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.WorkCompletedFragment -> workCompletedDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.WorkCancelledFragment -> workCancelledDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.UpdateReviewFragment -> updateReviewDocument(
                                        file.length()
                                    )

                                    getCurrentFragType() == FragType.ProtoRegistrationFragment -> updatePhotoAadhaarDocument(
                                        file.length()
                                    )

                                    else -> {
                                        if (getCurrentFragType() == FragType.NearByShopsListFragment || getCurrentFragType() == FragType.NewDateWiseOrderListFragment ||
                                            getCurrentFragType() == FragType.NewOrderListFragment || getCurrentFragType() == FragType.ShopBillingListFragment ||
                                            getCurrentFragType() == FragType.ViewAllOrderListFragment
                                        ) {
                                            addCollectionDocument(file.length())
                                        }
                                    }
                                }
                            }
                        } else
                            showSnackMessage("File is corrupted. Can not choose file.")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            // Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
            else if (requestCode == REQUEST_CODE_DOCUMENT_PDF) {
                try {
                    if (data != null && data.data != null) {
                        filePath = NewFileUtils.getRealPath(this@DashboardActivity, data.data)

                        if (filePath.contains("_.*_")) {
                            showSnackMessage("Invalid file path")
                            return
                        }

                        if (filePath.contains("google")) {
                            showSnackMessage("Can not select document from google drive")
                            return
                        }

                        val file = File(filePath)

                        val extension = getExtension(file)

                        try {
                            Log.e("Dashboard", "extension======> $extension")
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                        if (extension.contains("pdf") || extension.contains("jpg") || extension.contains(
                                "jpeg"
                            ) || extension.contains("png")
                        ) {
                            if (extension.contains("jpg") || extension.contains("jpeg") || extension.contains(
                                    "png"
                                )
                            ) {
                                if (getCurrentFragType() == FragType.MultipleImageFileUploadonStock) {
                                    getGalleryImage(this, data)
                                    println("stock_img set img 2 hit")
                                    (getFragment() as MultipleImageFileUploadonStock).setImage(
                                        File(
                                            filePath
                                        )
                                    )
                                } else {
                                    CropImage.activity(data.data)
                                        .setAspectRatio(40, 21)
                                        .start(this)
                                }
                            } else {
                                when {
                                    getCurrentFragType() == FragType.MultipleImageFileUploadonStock -> addPDFPic(
                                        file.length()
                                    )
                                }
                            }
                        } else
                            showSnackMessage("File is corrupted. Can not choose file.")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }
            // end Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
            /*else if (requestCode == PermissionHelper.REQUEST_CODE_GET_FILE) {
 val selectedImageUri = data?.data
 //OI FILE Manager
 val uriPath = selectedImageUri?.path
 getGalleryImage(this, data)

 if (getCurrentFragType() == FragType.DocumentListFragment)
 (getFragment() as DocumentListFragment).shareLocalFile(filePath, uriPath)
 }*/
            else if (requestCode == PermissionHelper.REQUEST_CODE_AUDIO) {
                if (!AppUtils.isRevisit!!) {
                    if (getFragment() != null && getFragment() is AddShopFragment)
                        (getFragment() as AddShopFragment).saveAudio()
                } else
                    feedbackDialog?.setAudio()
            } else if (requestCode == PermissionHelper.REQUEST_CODE_AUDIO_REC_NW) {
                try {
                    if (AppUtils.isRevisit!!) {
                        feedbackDialog?.setAudioNW()
                    } else {
                        if (getFragment() != null && getFragment() is AddShopFragment)
                            (getFragment() as AddShopFragment).saveNewAudio()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == PermissionHelper.REQUEST_CODE_EXO_PLAYER) {
                if (getFragment() != null && getFragment() is MicroLearningListFragment) {
                    (getFragment() as MicroLearningListFragment).getLearningList()

                    val intent = Intent(this, FileOpeningTimeIntentService::class.java)
                    intent.also {
                        it.putExtra(
                            "id",
                            (getFragment() as MicroLearningListFragment).selectedFile?.id
                        )
                        it.putExtra(
                            "start_time",
                            (getFragment() as MicroLearningListFragment).openingDateTime
                        )
                        startService(it)
                    }
                }
            } else if (CustomStatic.IsCameraFacingFromTeamAttdCametaStatus) {
                CustomStatic.IsCameraFacingFromTeamAttdCametaStatus = false
            } else {
                if (data?.action.equals("com.google.zxing.client.android.SCAN")) {
                    /*val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
 val contents = result.contents
 if (!TextUtils.isEmpty(contents))
 CodeScannerTextDialog.newInstance(contents).show(supportFragmentManager, "")
 else
 showSnackMessage("QrCode scan cancel")*/
                } else {
                    checkLocationMode()
                    try {
                        if (getFragment() != null && getFragment() is GpsDisableFragment) {
                            (getFragment() as GpsDisableFragment).onFragmentActivityResult(
                                requestCode,
                                resultCode,
                                data
                            )

                            if (getFragment() != null && getFragment() !is AddAttendanceFragment && !isGpsDisabled)
                                checkToShowAddAttendanceAlert()
                        } else {
                            if (getFragment() != null && getFragment() !is AddAttendanceFragment && !isGpsDisabled)
                                checkToShowAddAttendanceAlert()

                            if (intent != null && intent.extras != null && /*!isAttendanceAlertPresent &&*/ !isGpsDisabled) {
                                if (Pref.isAddAttendence)
                                    callShopVisitConfirmationDialog(
                                        intent.extras!!.get("NAME") as String,
                                        intent.extras!!.get("ID") as String
                                    )
                                else
                                    checkToShowAddAttendanceAlert()
                                intent = null
                            } else {
                                if (intent != null && intent.hasExtra("TYPE")) {
                                    //logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
                                    if (intent.getStringExtra("TYPE")
                                            .equals("PUSH", ignoreCase = true)
                                    ) {
                                        if (forceLogoutDialog == null)
                                            loadFragment(FragType.NotificationFragment, true, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("DUE", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is NearByShopsListFragment)
                                            loadFragment(
                                                FragType.NearByShopsListFragment,
                                                false,
                                                ""
                                            )
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("ACTIVITYDUE", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is LeadFrag)
                                            loadFragment(FragType.LeadFrag, false, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("TASK", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is TaskListFragment)
                                            loadFragment(FragType.TaskListFragment, false, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("Msg", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() is ChatListFragment)
                                            onBackPressed()
                                        val chatUser =
                                            intent.getSerializableExtra("chatUser") as ChatUserDataModel
                                        userName = chatUser.name
                                        loadFragment(FragType.ChatListFragment, true, chatUser)
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("TIMESHEET", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is TimeSheetListFragment)
                                            loadFragment(FragType.TimeSheetListFragment, false, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("REIMBURSEMENT", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is ReimbursementListFragment)
                                            loadFragment(
                                                FragType.ReimbursementListFragment,
                                                false,
                                                ""
                                            )
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("VIDEO", ignoreCase = true)
                                    ) {
                                        if (getFragment() != null && getFragment() !is MicroLearningListFragment)
                                            loadFragment(
                                                FragType.MicroLearningListFragment,
                                                false,
                                                ""
                                            )
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("lead_work", ignoreCase = true)
                                    ) {
                                        loadFragment(FragType.LeadFrag, false, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("lms_content_assign", ignoreCase = true)
                                    ) {
                                        loadFragment(FragType.NotificationLMSFragment, false, "")
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("clearData", ignoreCase = true)
                                    ) {
                                        isClearData = true
                                        Handler().postDelayed(Runnable {
                                            if (getFragment() != null && getFragment() !is LogoutSyncFragment) {
                                                if (AppUtils.isOnline(this))
                                                    loadFragment(
                                                        FragType.LogoutSyncFragment,
                                                        false,
                                                        ""
                                                    )
                                                else
                                                    showSnackMessage(getString(R.string.no_internet))
                                            }
                                        }, 500)
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("LEAVE_APPLY", ignoreCase = true)
                                    ) {
                                        var usrID = intent.getStringExtra("USER_ID")!!
                                        Handler().postDelayed(Runnable {

                                            loadFragment(FragType.LeaveHome, false, usrID)
                                        }, 300)
                                        //loadFragment(FragType.LeaveHome, false, intent.getStringExtra("USER_ID")!!)
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("LEAVE_STATUS", ignoreCase = true)
                                    ) {
                                        var usrID = intent.getStringExtra("USER_ID")!!
                                        Handler().postDelayed(Runnable {
                                            //loadFragment(FragType.LeaveHome, false, usrID)
                                            loadFragment(FragType.LeaveListFragment, false, "")
                                        }, 300)
                                        //loadFragment(FragType.LeaveHome, false, intent.getStringExtra("USER_ID")!!)
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("quotation_approval", ignoreCase = true)
                                    ) {
                                        Handler().postDelayed(Runnable {
                                            if (getFragment() != null && getFragment() !is ViewAllQuotListFragment)
                                                loadFragment(
                                                    FragType.MemberListFragment,
                                                    false,
                                                    Pref.user_id!!
                                                )
                                        }, 700)
                                    } else if (intent.getStringExtra("TYPE")
                                            .equals("ZERO_COLL_STATUS", ignoreCase = true)
                                    ) {
                                        Handler().postDelayed(Runnable {
                                            if (getFragment() != null && getFragment() !is CollectionNotiViewPagerFrag1 && getFragment() !is CollectionNotiViewPagerFrag) {
                                                SendBrod.stopBrodColl(this)
                                                SendBrod.stopBrodZeroOrder(this)
                                                tv_noti_count.visibility = View.GONE

                                                if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification == false) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification == false) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag1,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert == false && Pref.IsShowRepeatOrderinNotification) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag1,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert == false && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag1,
                                                        true,
                                                        ""
                                                    )
                                                } else if (Pref.ShowCollectionAlert && Pref.ShowZeroCollectioninAlert && Pref.IsShowRepeatOrderinNotification) {
                                                    loadFragment(
                                                        FragType.CollectionNotiViewPagerFrag2,
                                                        true,
                                                        ""
                                                    )
                                                }
                                            }
                                        }, 500)
                                    } else
                                        showForceLogoutPopup()

                                    intent = null
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            //mGpsStatusDetector?.checkOnActivityResult(requestCode, resultCode)

            if (requestCode == PermissionHelper.REQUEST_CODE_AUDIO) {

            } else {
                if (isGpsDisabled)
                    loadFragment(FragType.GpsDisableFragment, true, "")
            }
        }
    }


    private fun terminateOtherShopVisit(
        revisitStatus: Int, addShopDBModelEntity: AddShopDBModelEntity?, storeName: String,
        shopId: String, onlineTeamShop: TeamShopListDataModel?, offlineTeamShop: MemberShopEntity?
    ) {

        if (AppUtils.isAutoRevisit) {
            showSnackMessage("Auto Revisit just started")
            return
        }

        try {
            val list = AppDatabase.getDBInstance()!!.addMeetingDao().durationAvailable(false)
            if (list != null) {
                for (i in list.indices) {
                    val endTimeStamp = System.currentTimeMillis().toString()
                    val duration =
                        AppUtils.getTimeFromTimeSpan(list[i].startTimeStamp!!, endTimeStamp)
                    val totalMinute =
                        AppUtils.getMinuteFromTimeStamp(list[i].startTimeStamp!!, endTimeStamp)

                    AppDatabase.getDBInstance()!!.addMeetingDao().updateEndTimeOfMeeting(
                        endTimeStamp,
                        list[i].id,
                        AppUtils.getCurrentDateForShopActi()
                    )
                    AppDatabase.getDBInstance()!!.addMeetingDao().updateTimeDurationForDayOfMeeting(
                        list[i].id,
                        duration,
                        AppUtils.getCurrentDateForShopActi()
                    )
                    AppDatabase.getDBInstance()!!.addMeetingDao().updateDurationAvailable(
                        true,
                        list[i].id,
                        AppUtils.getCurrentDateForShopActi()
                    )

                    //If duration is greater than 20 hour then stop incrementing
                    /*if (totalMinute.toInt() <= Pref.minVisitDurationSpentTime.toInt()) {

 return
 }*/
                }
            }

            /*Terminate All other Shop Visit*/
            val shopList = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi())
            for (i in shopList.indices) {
                if (/*shopList[i].shopid != mAddShopDBModelEntity?.shop_id &&*/ !shopList[i].isDurationCalculated) {
                    Pref.durationCompletedShopId = shopList[i].shopid!!
                    Pref.durationCompletedStartTimeStamp = shopList[i].startTimeStamp!!
                    val endTimeStamp = System.currentTimeMillis().toString()
                    val duration =
                        AppUtils.getTimeFromTimeSpan(shopList[i].startTimeStamp, endTimeStamp)
                    val totalMinute =
                        AppUtils.getMinuteFromTimeStamp(shopList[i].startTimeStamp, endTimeStamp)
                    //If duration is greater than 20 hour then stop incrementing
                    if (totalMinute.toInt() > 20 * 60) {
                        if (!Pref.isMultipleVisitEnable)
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(
                                true,
                                shopList[i].shopid!!,
                                AppUtils.getCurrentDateForShopActi()
                            )
                        else
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(
                                true,
                                shopList[i].shopid!!,
                                AppUtils.getCurrentDateForShopActi(),
                                shopList[i].startTimeStamp
                            )
                        return
                    }

                    if (!Pref.isMultipleVisitEnable) {
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(
                            endTimeStamp,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTotalMinuteForDayOfShop(
                                shopList[i].shopid!!,
                                totalMinute,
                                AppUtils.getCurrentDateForShopActi()
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTimeDurationForDayOfShop(
                                shopList[i].shopid!!,
                                duration,
                                AppUtils.getCurrentDateForShopActi()
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(
                            true,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                    } else {
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(
                            endTimeStamp,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi(),
                            shopList[i].startTimeStamp
                        )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTotalMinuteForDayOfShop(
                                shopList[i].shopid!!,
                                totalMinute,
                                AppUtils.getCurrentDateForShopActi(),
                                shopList[i].startTimeStamp
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTimeDurationForDayOfShop(
                                shopList[i].shopid!!,
                                duration,
                                AppUtils.getCurrentDateForShopActi(),
                                shopList[i].startTimeStamp
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(
                            true,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi(),
                            shopList[i].startTimeStamp
                        )
                    }
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateOutTime(
                        AppUtils.getCurrentTimeWithMeredian(),
                        shopList[i].shopid!!,
                        AppUtils.getCurrentDateForShopActi(),
                        shopList[i].startTimeStamp
                    )
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateOutLocation(
                        LocationWizard.getNewLocationName(
                            this,
                            Pref.current_latitude.toDouble(),
                            Pref.current_longitude.toDouble()
                        ),
                        shopList[i].shopid!!,
                        AppUtils.getCurrentDateForShopActi(),
                        shopList[i].startTimeStamp
                    )

                    val netStatus = if (AppUtils.isOnline(this))
                        "Online"
                    else
                        "Offline"

                    val netType =
                        if (AppUtils.getNetworkType(this).equals("wifi", ignoreCase = true))
                            AppUtils.getNetworkType(this)
                        else
                            "Mobile ${AppUtils.mobNetType(this)}"

                    if (!Pref.isMultipleVisitEnable) {
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDeviceStatusReason(
                            AppUtils.getDeviceName(),
                            AppUtils.getAndroidVersion(),
                            AppUtils.getBatteryPercentage(this).toString(),
                            netStatus,
                            netType.toString(),
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                    } else {
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDeviceStatusReason(
                            AppUtils.getDeviceName(),
                            AppUtils.getAndroidVersion(),
                            AppUtils.getBatteryPercentage(this).toString(),
                            netStatus,
                            netType.toString(),
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi(),
                            shopList[i].startTimeStamp
                        )
                    }

                    if (Pref.willShowShopVisitReason && totalMinute.toInt() < Pref.minVisitDurationSpentTime.toInt()) {
                        Pref.isShowShopVisitReason = true
                        showRevisitReasonDialog(
                            revisitStatus,
                            addShopDBModelEntity,
                            storeName,
                            shopId,
                            onlineTeamShop,
                            offlineTeamShop
                        )
                    }
                }
            }

            /*if (Pref.isShowShopVisitReason)
 return

 revisitShop(image)*/

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun revisitShop(image: String) {

        var shopName = ""
        var shopLat = 0.0
        var shopLong = 0.0
        var wilStartRevisit = false
        var shopGpsAddress = ""

        val shopActivityEntity = AppDatabase.getDBInstance()!!.shopActivityDao()
            .getShopForDay(mShopId, AppUtils.getCurrentDateForShopActi())
        val imageUpDateTime = AppUtils.getCurrentISODateTime()

        if (Pref.isMultipleVisitEnable)
            wilStartRevisit = true
        else {
            if (shopActivityEntity.isEmpty() || shopActivityEntity[0].date != AppUtils.getCurrentDateForShopActi())
                wilStartRevisit = true
        }

        if (wilStartRevisit) {
            val mShopActivityEntity = ShopActivityEntity()
            mShopActivityEntity.startTimeStamp = System.currentTimeMillis().toString()
            mShopActivityEntity.isUploaded = false
            mShopActivityEntity.isVisited = true
            mShopActivityEntity.shop_name = mStoreName
            mShopActivityEntity.duration_spent = "00:00:00"
            mShopActivityEntity.date = AppUtils.getCurrentDateForShopActi()
            mShopActivityEntity.shop_address = mAddShopDBModelEntity?.address
            mShopActivityEntity.shopid = mAddShopDBModelEntity?.shop_id
            mShopActivityEntity.visited_date = imageUpDateTime //AppUtils.getCurrentISODateTime()
            mShopActivityEntity.isDurationCalculated = false
            if (mAddShopDBModelEntity?.totalVisitCount != null && mAddShopDBModelEntity?.totalVisitCount != "") {
                val visitCount = mAddShopDBModelEntity?.totalVisitCount?.toInt()!! + 1
                AppDatabase.getDBInstance()!!.addShopEntryDao()
                    .updateTotalCount(visitCount.toString(), mShopId)
                AppDatabase.getDBInstance()!!.addShopEntryDao()
                    .updateLastVisitDate(AppUtils.getCurrentDateChanged(), mShopId)
            }

            var distance = 0.0
            Timber.e("======New Distance (At revisit time)=========")

            val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(mShopId)
            if (!TextUtils.isEmpty(shop.actual_address))
                shopGpsAddress = shop.actual_address
            else
                shopGpsAddress = LocationWizard.getNewLocationName(
                    this,
                    shop.shopLat.toDouble(),
                    shop.shopLong.toDouble()
                )

            if (Pref.isOnLeave.equals("false", ignoreCase = true)) {

                Timber.e("=====User is at work (At revisit time)=======")

                /*if (!TextUtils.isEmpty(Pref.current_latitude) && !TextUtils.isEmpty(Pref.current_longitude)) {
 if (!TextUtils.isEmpty(Pref.source_latitude) && !TextUtils.isEmpty(Pref.source_longitude)) {
 distance = LocationWizard.getDistance(Pref.source_latitude.toDouble(), Pref.source_longitude.toDouble(),
 Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())

 Timber.e("=====Both location available=======")
 } else {
 //distance = LocationWizard.getDistance(Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble(), 0.0, 0.0)
 distance = 0.0 //LocationWizard.getDistance(0.0, 0.0, Pref.current_latitude.toDouble(), Pref.current_longitude.toDouble())

 Timber.e("=====Only new location available=======")
 }
 Pref.source_latitude = Pref.current_latitude
 Pref.source_longitude = Pref.current_longitude
 } else {
 if (!TextUtils.isEmpty(Pref.source_latitude) && !TextUtils.isEmpty(Pref.source_longitude)) {
 //distance = LocationWizard.getDistance(Pref.source_latitude.toDouble(), Pref.source_longitude.toDouble(), 0.0, 0.0)
 distance = 0.0 //LocationWizard.getDistance(0.0, 0.0, Pref.source_latitude.toDouble(), Pref.source_longitude.toDouble())

 Timber.e("=====Only old location available=======")
 } else {
 distance = 0.0

 Timber.e("=====No location available=======")
 }
 }*/

                shopName = shop.shopName
                val locationList = AppDatabase.getDBInstance()!!.userLocationDataDao()
                    .getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi())

                shopLat = shop.shopLat
                shopLong = shop.shopLong
                //val distance = LocationWizard.getDistance(shop.shopLat, shop.shopLong, location.latitude, location.longitude)

                val userlocation = UserLocationDataEntity()
                userlocation.latitude = shop.shopLat.toString()
                userlocation.longitude = shop.shopLong.toString()

                var loc_distance = 0.0

                if (locationList != null && locationList.isNotEmpty()) {
                    loc_distance = LocationWizard.getDistance(
                        locationList[locationList.size - 1].latitude.toDouble(),
                        locationList[locationList.size - 1].longitude.toDouble(),
                        userlocation.latitude.toDouble(),
                        userlocation.longitude.toDouble()
                    )
                }
                val finalDistance = (Pref.tempDistance.toDouble() + loc_distance).toString()

                Timber.e("===Distance (At shop revisit time)===")
                Timber.e("Temp Distance====> " + Pref.tempDistance)
                Timber.e("Normal Distance====> $loc_distance")
                Timber.e("Total Distance====> $finalDistance")
                Timber.e("=====================================")

                // start 12.0 DashboardActivity 24-03-2023 room main thread optimizetion location db insertion in dashboardActivity revisitShop(image: String) lifecycleScope introduced mantis 0025753

                val ref = this
                lifecycleScope.launch(Dispatchers.IO) {
                    ref.runOnUiThread {
                        try {
                            userlocation.distance = finalDistance
                            Timber.e("latitute & lontitute ====> ${userlocation.latitude.toDouble()} ${userlocation.longitude.toDouble()}")
                            userlocation.locationName = LocationWizard.getNewLocationName(
                                this@DashboardActivity,
                                userlocation.latitude.toDouble(),
                                userlocation.longitude.toDouble()
                            )
                            Timber.e("location name ====> ${userlocation.locationName}")
                            userlocation.timestamp = LocationWizard.getTimeStamp()
                            userlocation.time = LocationWizard.getFormattedTime24Hours(true)
                            userlocation.meridiem = LocationWizard.getMeridiem()
                            userlocation.hour = LocationWizard.getHour()
                            userlocation.minutes = LocationWizard.getMinute()
                            userlocation.isUploaded = false
                            userlocation.shops = AppDatabase.getDBInstance()!!.shopActivityDao()
                                .getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi()).size.toString()
                            userlocation.updateDate = AppUtils.getCurrentDateForShopActi()
                            userlocation.updateDateTime = AppUtils.getCurrentDateTime()
                            userlocation.meeting = AppDatabase.getDBInstance()!!.addMeetingDao()
                                .getMeetingDateWise(AppUtils.getCurrentDateForShopActi()).size.toString()
                            userlocation.network_status =
                                if (AppUtils.isOnline(this@DashboardActivity)) "Online" else "Offline"
                            userlocation.battery_percentage =
                                AppUtils.getBatteryPercentage(this@DashboardActivity).toString()

                            //negative distance handle Suman 06-02-2024 mantis id 0027225 begin
                            try {
                                var distReftify = userlocation.distance.toDouble()
                                if (distReftify < 0) {
                                    var locL = AppDatabase.getDBInstance()!!.userLocationDataDao()
                                        .getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi()) as ArrayList<UserLocationDataEntity>
                                    var lastLoc = locL.get(locL.size - 1)
                                    var d = LocationWizard.getDistance(
                                        userlocation.latitude.toDouble(),
                                        userlocation.longitude.toDouble(),
                                        lastLoc.latitude.toDouble(),
                                        lastLoc.longitude.toDouble()
                                    )
                                    userlocation.distance = d.toString()
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                userlocation.distance = "0.0"
                            }
                            //negative distance handle Suman 06-02-2024 mantis id 0027225 end

                            AppDatabase.getDBInstance()!!.userLocationDataDao()
                                .insertAll(userlocation)
                            Timber.e("location insert data ====> ${userlocation}")

                        } catch (ex: Exception) {
                            Timber.e("ex revisitShop error : ${ex.localizedMessage} ${ex.printStackTrace()}")
                            ex.printStackTrace()
                        }
                    }
                }
                // end


                Timber.e("=====Shop revisit data added=======")

                Pref.totalS2SDistance =
                    (Pref.totalS2SDistance.toDouble() + userlocation.distance.toDouble()).toString()

                distance = Pref.totalS2SDistance.toDouble()
                Pref.totalS2SDistance = "0.0"
                Pref.tempDistance = "0.0"
            } else {
                Timber.e("=====User is on leave (At revisit time)=======")
                distance = 0.0
            }

            Timber.e("shop to shop distance (At revisit time)=====> $distance")

            mShopActivityEntity.distance_travelled = distance.toString()

// AppUtils.isShopVisited = true
            Pref.isShopVisited = true

            if (!TextUtils.isEmpty(feedback))
                mShopActivityEntity.feedback = feedback

            mShopActivityEntity.next_visit_date = nextVisitDate

            val todaysVisitedShop = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi())

            if (todaysVisitedShop == null || todaysVisitedShop.isEmpty()) {
                mShopActivityEntity.isFirstShopVisited = true

                if (!TextUtils.isEmpty(Pref.home_latitude) && !TextUtils.isEmpty(Pref.home_longitude)) {
                    val distance_ = LocationWizard.getDistance(
                        Pref.home_latitude.toDouble(), Pref.home_longitude.toDouble(),
                        shopLat, shopLong
                    )
                    mShopActivityEntity.distance_from_home_loc = distance_.toString()
                } else
                    mShopActivityEntity.distance_from_home_loc = "0.0"
            } else {
                mShopActivityEntity.isFirstShopVisited = false
                mShopActivityEntity.distance_from_home_loc = ""
            }

            mShopActivityEntity.in_time = AppUtils.getCurrentTimeWithMeredian()
            mShopActivityEntity.in_loc = shopGpsAddress


            var shopAll = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityAll()
            mShopActivityEntity.shop_revisit_uniqKey =
                Pref.user_id + System.currentTimeMillis().toString()

            /*13-12-2021*/
            if (prosId != null && !prosId.equals(""))
                mShopActivityEntity.pros_id = prosId
            else {
                mShopActivityEntity.pros_id = ""
            }

            if (approxValue != null && !approxValue.equals(""))
                mShopActivityEntity.approximate_1st_billing_value = approxValue
            else {
                mShopActivityEntity.approximate_1st_billing_value = ""
            }

            try {
                mShopActivityEntity.agency_name = AppDatabase.getDBInstance()!!.shopActivityDao()
                    .getAgencyName(mShopActivityEntity.shopid!!)
            } catch (ex: Exception) {
                mShopActivityEntity.agency_name = ""
            }
            if (mShopActivityEntity.agency_name == null) {
                mShopActivityEntity.agency_name = ""
            }

            mShopActivityEntity.updated_by = Pref.user_id
            mShopActivityEntity.updated_on = AppUtils.getCurrentDateForShopActi()

            /*if(shopAll==null || shopAll.isEmpty()){
 mShopActivityEntity.shop_revisit_uniqKey = Pref.user_id+AppUtils.getCurrentDateMonth()+"10001"
 }else{
 if(shopAll[shopAll.size-1].shop_revisit_uniqKey != null && (shopAll[shopAll.size-1].shop_revisit_uniqKey?.length!! > 1))
 mShopActivityEntity.shop_revisit_uniqKey=(shopAll[shopAll.size-1].shop_revisit_uniqKey!!.toLong()+1).toString()
 else
 mShopActivityEntity.shop_revisit_uniqKey = Pref.user_id+AppUtils.getCurrentDateMonth()+"10001"
 }*/

            mShopActivityEntity.multi_contact_name = revisit_extraContName
            mShopActivityEntity.multi_contact_number = revisit_extraContPh

            //Begin Rev 17 DashboardActivity AppV 4.0.8 Suman 24/04/2023 distanct+station calculation 25806
            var profileAddr = Location("")
            var shopAddr = Location("")
            var dist: Double = 0.0
            try {
                profileAddr.latitude = Pref.profile_latitude.toDouble()
                profileAddr.longitude = Pref.profile_longitude.toDouble()
                var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao()
                    .getShopByIdN(mShopActivityEntity.shopid)
                shopAddr.latitude = shopObj.shopLat.toDouble()
                shopAddr.longitude = shopObj.shopLong.toDouble()
                var dist = profileAddr.distanceTo(shopAddr) / 1000 //km
                mShopActivityEntity.distFromProfileAddrKms = String.format("%.2f", dist)
                //In Station- 0
                //Ex Station- 1
                //Out Station- 2
                if (dist <= 25.0) {
                    mShopActivityEntity.stationCode = "0"
                } else if (dist > 25 && dist < 80.0) {
                    mShopActivityEntity.stationCode = "1"
                } else if (dist >= 85.0) {
                    mShopActivityEntity.stationCode = "2"
                }

                //Begin Rev 22.0 DashboardActivity AppV 4.1.3 Suman 18-05-2023 mantis 26162
                if (Pref.IsShowReimbursementTypeInAttendance && Pref.isExpenseFeatureAvailable) {
                    if (Pref.selectedVisitStationName.contains("in", ignoreCase = true)) {
                        mShopActivityEntity.stationCode = "0"
                    } else if (Pref.selectedVisitStationName.contains("ex", ignoreCase = true)) {
                        mShopActivityEntity.stationCode = "1"
                    } else if (Pref.selectedVisitStationName.contains("out", ignoreCase = true)) {
                        mShopActivityEntity.stationCode = "2"
                    }
                }
                //End of Rev 22.0 DashboardActivity AppV 4.1.3 Suman 18-05-2023 mantis 26162

                Timber.d("dist_cal ${mShopActivityEntity.distFromProfileAddrKms} loc1 ${profileAddr.latitude} ${profileAddr.longitude} loc2 ${shopAddr.latitude} ${shopAddr.longitude}")
            } catch (ex: Exception) {
                ex.printStackTrace()
                Timber.d("dist_cal ex ${ex.message}")
            }
            //End of Rev 17 DashboardActivity AppV 4.0.8 Suman 24/04/2023 distanct+station calculation 25806

            AppDatabase.getDBInstance()!!.shopActivityDao().insertAll(mShopActivityEntity)

            // shop feedback work
            var feedObj: ShopFeedbackEntity = ShopFeedbackEntity()
            feedObj.shop_id = mShopActivityEntity.shopid
            feedObj.feedback = mShopActivityEntity.feedback
            feedObj.date_time = AppUtils.getCurrentDateTime()

            feedObj.multi_contact_name = revisit_extraContName
            feedObj.multi_contact_number = revisit_extraContPh

            if (feedObj.feedback.equals("") || mShopActivityEntity.feedback == null)
                feedObj.feedback = "N/A"
            AppDatabase.getDBInstance()?.shopFeedbackDao()?.insert(feedObj)

        }

        AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(mShopId)!![0].visited = true

        val performance = AppDatabase.getDBInstance()!!.performanceDao()
            .getTodaysData(AppUtils.getCurrentDateForShopActi())
        if (performance != null) {
            val list = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getDurationCalculatedVisitedShopForADay(AppUtils.getCurrentDateForShopActi(), true)
            AppDatabase.getDBInstance()!!.performanceDao()
                .updateTotalShopVisited(list.size.toString(), AppUtils.getCurrentDateForShopActi())
            var totalTimeSpentForADay = 0
            for (i in list.indices) {
                totalTimeSpentForADay += list[i].totalMinute.toInt()
            }
            AppDatabase.getDBInstance()!!.performanceDao().updateTotalDuration(
                totalTimeSpentForADay.toString(),
                AppUtils.getCurrentDateForShopActi()
            )
        } else {
            val list = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getDurationCalculatedVisitedShopForADay(AppUtils.getCurrentDateForShopActi(), true)
            val performanceEntity = PerformanceEntity()
            performanceEntity.date = AppUtils.getCurrentDateForShopActi()
            performanceEntity.total_shop_visited = list.size.toString()
            var totalTimeSpentForADay = 0
            for (i in list.indices) {
                totalTimeSpentForADay += list[i].totalMinute.toInt()
            }
            performanceEntity.total_duration_spent = totalTimeSpentForADay.toString()
            AppDatabase.getDBInstance()!!.performanceDao().insert(performanceEntity)
        }

        var shopTyByID =
            AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopType(mShopId)!!.toString()
        if (Pref.IsnewleadtypeforRuby && shopTyByID.equals("2")) {
            println("addShopVisitPic shoptype2");
            val fileSize = AppUtils.getCompressOldImage(image, this)
            addShopVisitPic(fileSize, imageUpDateTime, shopName, image)
        } else if (Pref.IsnewleadtypeforRuby && !shopTyByID.equals("2")) {
            println("addShopVisitPic !shoptype2");
        } else if (Pref.isRevisitCaptureImage) {
            println("addShopVisitPic normal");
            // Image compression
            //val fileSize = AppUtils.getCompressImage(image)
            val fileSize = AppUtils.getCompressOldImage(image, this)
            addShopVisitPic(fileSize, imageUpDateTime, shopName, image)
        }


        if (Pref.isRecordAudioEnable) {
            val shopVisitAudio = ShopVisitAudioEntity()
            AppDatabase.getDBInstance()?.shopVisitAudioDao()?.insert(shopVisitAudio.apply {
                shop_id = mShopId
                isUploaded = false
                audio = mFilePath
                visit_datetime = imageUpDateTime
            })
        }
        //Suman 29-07-2024 mantis id 27647
        if (Pref.IsUserWiseRecordAudioEnableForVisitRevisit) {
            try {
                val shopAudio = ShopAudioEntity()
                shopAudio.shop_id = mShopId
                shopAudio.audio_path = mAudioFilePathNW
                shopAudio.isUploaded = false
                shopAudio.datetime = AppUtils.getCurrentDateTime()
                shopAudio.revisitYN = "1"
                AppDatabase.getDBInstance()?.shopAudioDao()?.insert(shopAudio)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val shopDetail = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(mShopId)
        if (shopDetail.is_otp_verified.equals("false", ignoreCase = true)) {
            if (AppUtils.isOnline(this@DashboardActivity)) {
                if (!isOtherUsersShopRevisit) {
                    cancelNotification(mShopId)
                    showShopVerificationDialog()
                } else
                    showOrderCollectionDialog()
            } else
                afterShopRevisit()
        } else
            afterShopRevisit()
    }

    private fun showRevisitReasonDialog(
        revisitStatus: Int, addShopDBModelEntity: AddShopDBModelEntity?, storeName: String,
        shopId: String, onlineTeamShop: TeamShopListDataModel?, offlineTeamShop: MemberShopEntity?
    ) {
        reasonDialog = null
        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao()
            .getShopByIdN(Pref.durationCompletedShopId)
        reasonDialog = ReasonDialog.getInstance(
            shop?.shopName!!,
            "You are revisiting ${Pref.shopText} but the " +
                    "duration spent is less than ${Pref.minVisitDurationSpentTime} minutes. Please write the reason below.",
            reason
        ) {
            reasonDialog?.dismiss()
            Pref.isShowShopVisitReason = false

            if (!Pref.isMultipleVisitEnable)
                AppDatabase.getDBInstance()!!.shopActivityDao().updateEarlyRevisitReason(
                    it,
                    Pref.durationCompletedShopId,
                    AppUtils.getCurrentDateForShopActi()
                )
            else
                AppDatabase.getDBInstance()!!.shopActivityDao().updateEarlyRevisitReason(
                    it,
                    Pref.durationCompletedShopId,
                    AppUtils.getCurrentDateForShopActi(),
                    Pref.durationCompletedStartTimeStamp
                )

            when (revisitStatus) {
                1 -> startOwnShopRevisit(addShopDBModelEntity!!, storeName, shopId)
                2 -> startRevisitOnlineTeamShop(onlineTeamShop!!)
                3 -> startRevisitOfflineTeamShop(offlineTeamShop!!)
            }
        }
        reasonDialog?.show(supportFragmentManager, "")
    }

    private fun afterShopRevisit() {
        if (!isOtherUsersShopRevisit) {
            cancelNotification(mShopId)
            if (Pref.ShopScreenAftVisitRevisit && Pref.ShopScreenAftVisitRevisitGlobal) {
                loadFragment(FragType.ShopDetailFragment, true, mShopId)
            } else {
                AppUtils.isRevisit = false
                Handler().postDelayed(Runnable {
                    Log.d("login_test_calling13", "")

                    loadFragment(FragType.DashboardFragment, true, "")
                    if (getCurrentFragType() == FragType.DashboardFragment)
                        (getFragment() as DashboardFragment).initBottomAdapter()
                }, 300)
            }
        } else {
            /*Team new work*/
            if (Pref.isOrderAvailableForPopup) {
                showOrderCollectionDialog()
            } else {
                if (getFragment() != null && getFragment() is MemberShopListFragment)
                    (getFragment() as MemberShopListFragment).updateAdapter()
                else if (getFragment() != null && getFragment() is OfflineShopListFragment)
                    (getFragment() as OfflineShopListFragment).updateAdapter()
                else if (getFragment() != null && getFragment() is MemberAllShopListFragment) {
                    (getFragment() as MemberAllShopListFragment).updateAdapter()
                }
            }
        }
    }

    private fun showOrderCollectionDialog() {

        val addShopData = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(mShopId)

        var orderText = ""
        orderText = if (Pref.isQuotationPopupShow)
            "Quot. Entry"
        else
            "Order. Entry"

        CommonDialog.getInstance("Action",
            "What you like to do?",
            orderText,
            "Collection Entry",
            false,
            object : CommonDialogClickListener {
                override fun onLeftClick() {
                    if (Pref.isQuotationPopupShow)
                        (mContext as DashboardActivity).loadFragment(
                            FragType.QuotationListFragment,
                            true,
                            addShopData.shop_id
                        )
                    else
                        (mContext as DashboardActivity).loadFragment(
                            FragType.ViewAllOrderListFragment,
                            true,
                            addShopData
                        )
                }

                override fun onRightClick(editableData: String) {
                    (mContext as DashboardActivity).loadFragment(
                        FragType.CollectionDetailsFragment,
                        true,
                        addShopData
                    )
                }

            },
            object : CommonDialog.OnCloseClickListener {
                override fun onCloseClick() {
                    if (getFragment() != null && getFragment() is MemberShopListFragment)
                        (getFragment() as MemberShopListFragment).updateAdapter()
                    else if (getFragment() != null && getFragment() is OfflineShopListFragment)
                        (getFragment() as OfflineShopListFragment).updateAdapter()
                }

            }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun showShopVerificationDialog() {
        if (!Pref.isShowOTPVerificationPopup) {
            if (Pref.ShopScreenAftVisitRevisit && Pref.ShopScreenAftVisitRevisitGlobal) {
                (mContext as DashboardActivity).loadFragment(
                    FragType.ShopDetailFragment,
                    true,
                    mShopId
                )
            } else {
                AppUtils.isRevisit = false
                Handler().postDelayed(Runnable {
                    Log.d("login_test_calling14", "")

                    (mContext as DashboardActivity).loadFragment(
                        FragType.DashboardFragment,
                        true,
                        ""
                    )
                    if (getCurrentFragType() == FragType.DashboardFragment)
                        (getFragment() as DashboardFragment).initBottomAdapter()
                }, 300)
            }
        }
// loadFragment(FragType.ShopDetailFragment, true, mShopId)
        else {
            ShopVerificationDialog.getInstance(
                mShopId,
                object : ShopVerificationDialog.OnOTPButtonClickListener {
                    override fun onEditClick(number: String) {
                        val addShopData =
                            AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(mShopId)
                        if (!addShopData.isUploaded || addShopData.isEditUploaded == 0) {
                            /*if (Pref.isReplaceShopText)
 showSnackMessage("Please sync this customer first.")
 else
 showSnackMessage("Please sync this shop first.")*/

                            showSnackMessage("Please sync this " + Pref.shopText + " first")

                            loadFragment(FragType.NearByShopsListFragment, true, "")
                        } else {
                            saveData(addShopData, number)
                        }

                    }

                    override fun onCancelClick() {
                        //(mContext as DashboardActivity).onBackPressed()
                        if (Pref.ShopScreenAftVisitRevisit && Pref.ShopScreenAftVisitRevisitGlobal) {
                            (mContext as DashboardActivity).loadFragment(
                                FragType.ShopDetailFragment,
                                true,
                                mShopId
                            )
                        } else {
                            AppUtils.isRevisit = false
                            Log.d("login_test_calling15", "")

                            (mContext as DashboardActivity).loadFragment(
                                FragType.DashboardFragment,
                                true,
                                ""
                            )
                        }
// loadFragment(FragType.ShopDetailFragment, true, mShopId)
                    }

                    override fun onOkButtonClick(otp: String) {
                        callOtpSentApi(mShopId)
                    }
                }).show(
                (mContext as DashboardActivity).supportFragmentManager,
                "ShopVerificationDialog"
            )
        }
    }

    private fun saveData(addShopData: AddShopDBModelEntity, number: String) {
        AppDatabase.getDBInstance()?.addShopEntryDao()?.updateContactNo(addShopData.shop_id, number)

        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(mShopId)
        convertToReqAndApiCall(shop)
    }


    private fun convertToReqAndApiCall(addShopData: AddShopDBModelEntity) {
        if (Pref.user_id == null || Pref.user_id == "" || Pref.user_id == " ") {
            (mContext as DashboardActivity).showSnackMessage("Please login again")
            BaseActivity.isApiInitiated = false
            return
        }

        val addShopReqData = AddShopRequestData()
        addShopReqData.session_token = Pref.session_token
        addShopReqData.address = addShopData.address
        addShopReqData.owner_contact_no = addShopData.ownerContactNumber
        addShopReqData.owner_email = addShopData.ownerEmailId
        addShopReqData.owner_name = addShopData.ownerName
        addShopReqData.pin_code = addShopData.pinCode
        addShopReqData.shop_lat = addShopData.shopLat.toString()
        addShopReqData.shop_long = addShopData.shopLong.toString()
        addShopReqData.shop_name = addShopData.shopName.toString()
        addShopReqData.shop_id = addShopData.shop_id
        addShopReqData.added_date = ""
        addShopReqData.user_id = Pref.user_id
        addShopReqData.type = addShopData.type
        addShopReqData.assigned_to_pp_id = addShopData.assigned_to_pp_id
        addShopReqData.assigned_to_dd_id = addShopData.assigned_to_dd_id

        if (!TextUtils.isEmpty(addShopData.dateOfBirth))
            addShopReqData.dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfBirth)

        if (!TextUtils.isEmpty(addShopData.dateOfAniversary))
            addShopReqData.date_aniversary =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.dateOfAniversary)

        addShopReqData.amount = addShopData.amount
        addShopReqData.area_id = addShopData.area_id
        addShopReqData.model_id = addShopData.model_id
        addShopReqData.primary_app_id = addShopData.primary_app_id
        addShopReqData.secondary_app_id = addShopData.secondary_app_id
        addShopReqData.lead_id = addShopData.lead_id
        addShopReqData.stage_id = addShopData.stage_id
        addShopReqData.funnel_stage_id = addShopData.funnel_stage_id
        addShopReqData.booking_amount = addShopData.booking_amount
        addShopReqData.type_id = addShopData.type_id

        addShopReqData.director_name = addShopData.director_name
        addShopReqData.key_person_name = addShopData.person_name
        addShopReqData.phone_no = addShopData.person_no

        if (!TextUtils.isEmpty(addShopData.family_member_dob))
            addShopReqData.family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.family_member_dob)

        if (!TextUtils.isEmpty(addShopData.add_dob))
            addShopReqData.addtional_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_dob)

        if (!TextUtils.isEmpty(addShopData.add_doa))
            addShopReqData.addtional_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.add_doa)

        addShopReqData.specialization = addShopData.specialization
        addShopReqData.category = addShopData.category
        addShopReqData.doc_address = addShopData.doc_address
        addShopReqData.doc_pincode = addShopData.doc_pincode
        addShopReqData.is_chamber_same_headquarter = addShopData.chamber_status.toString()
        addShopReqData.is_chamber_same_headquarter_remarks = addShopData.remarks
        addShopReqData.chemist_name = addShopData.chemist_name
        addShopReqData.chemist_address = addShopData.chemist_address
        addShopReqData.chemist_pincode = addShopData.chemist_pincode
        addShopReqData.assistant_contact_no = addShopData.assistant_no
        addShopReqData.average_patient_per_day = addShopData.patient_count
        addShopReqData.assistant_name = addShopData.assistant_name

        if (!TextUtils.isEmpty(addShopData.doc_family_dob))
            addShopReqData.doc_family_member_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.doc_family_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_dob))
            addShopReqData.assistant_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_dob)

        if (!TextUtils.isEmpty(addShopData.assistant_doa))
            addShopReqData.assistant_doa =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_doa)

        if (!TextUtils.isEmpty(addShopData.assistant_family_dob))
            addShopReqData.assistant_family_dob =
                AppUtils.changeAttendanceDateFormatToCurrent(addShopData.assistant_family_dob)

        addShopReqData.entity_id = addShopData.entity_id
        addShopReqData.party_status_id = addShopData.party_status_id
        addShopReqData.retailer_id = addShopData.retailer_id
        addShopReqData.dealer_id = addShopData.dealer_id
        addShopReqData.beat_id = addShopData.beat_id
        addShopReqData.assigned_to_shop_id = addShopData.assigned_to_shop_id
        addShopReqData.actual_address = addShopData.actual_address

        addShopReqData.GSTN_Number = addShopData.gstN_Number
        addShopReqData.ShopOwner_PAN = addShopData.shopOwner_PAN

        /*val addShop = AddShopRequest()
 addShop.data = addShopReqData*/

        // contact module
        try {
            addShopReqData.address = addShopData!!.address
            addShopReqData.actual_address = addShopData!!.address
            addShopReqData.shop_firstName = addShopData!!.crm_firstName
            addShopReqData.shop_lastName = addShopData!!.crm_lastName
            addShopReqData.crm_companyID =
                if (addShopData.companyName_id.isNullOrEmpty()) "0" else addShopData.companyName_id
            addShopReqData.crm_jobTitle = addShopData.jobTitle
            addShopReqData.crm_typeID =
                if (addShopData.crm_type_ID.isNullOrEmpty()) "0" else addShopData.crm_type_ID
            addShopReqData.crm_statusID =
                if (addShopData.crm_status_ID.isNullOrEmpty()) "0" else addShopData.crm_status_ID
            addShopReqData.crm_sourceID =
                if (addShopData.crm_source_ID.isNullOrEmpty()) "0" else addShopData.crm_source_ID
            addShopReqData.crm_reference = addShopData.crm_reference
            addShopReqData.crm_referenceID =
                if (addShopData.crm_reference_ID.isNullOrEmpty()) "0" else addShopData.crm_reference_ID
            addShopReqData.crm_referenceID_type = addShopData.crm_reference_ID_type
            addShopReqData.crm_stage_ID =
                if (addShopData.crm_stage_ID.isNullOrEmpty()) "0" else addShopData.crm_stage_ID
            addShopReqData.assign_to = addShopData.crm_assignTo_ID
            addShopReqData.saved_from_status = addShopData.crm_saved_from
        } catch (ex: Exception) {
            ex.printStackTrace()
            Timber.d("Logout edit sync err ${ex.message}")
        }

        if (AppUtils.isOnline(mContext)) {

            if (BaseActivity.isApiInitiated)
                return

            BaseActivity.isApiInitiated = true

            callEditShopApi(addShopReqData, addShopData.shopImageLocalPath, addShopData.doc_degree)
        } else {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
        }
    }

    private fun callEditShopApi(
        addShopReqData: AddShopRequestData,
        shopImageLocalPath: String?,
        doc_degree: String?
    ) {
        progress_wheel.spin()

        if (TextUtils.isEmpty(shopImageLocalPath) && TextUtils.isEmpty(doc_degree)) {
            val repository = EditShopRepoProvider.provideEditShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                repository.editShop(addShopReqData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val addShopResult = result as AddShopResponse
                        Timber.d("Edit Shop : " + ", SHOP: " + addShopReqData.shop_name + ", RESPONSE:" + result.message)
                        when (addShopResult.status) {
                            NetworkConstant.SUCCESS -> {
                                AppDatabase.getDBInstance()!!.addShopEntryDao()
                                    .updateIsEditUploaded(1, addShopReqData.shop_id)
                                progress_wheel.stopSpinning()
                                // (mContext as DashboardActivity).showSnackMessage("SUCCESS")
                                (mContext as DashboardActivity).updateFence()

                                showShopVerificationDialog()

                            }

                            NetworkConstant.SESSION_MISMATCH -> {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).clearData()
                                startActivity(
                                    Intent(
                                        mContext as DashboardActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            }

                            else -> {
                                progress_wheel.stopSpinning()
                            }
                        }
                        BaseActivity.isApiInitiated = false
                    }, { error ->
                        error.printStackTrace()
                        BaseActivity.isApiInitiated = false
                        //(mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                    })
            )
        } else {
            val repository = EditShopRepoProvider.provideEditShopRepository()
            BaseActivity.compositeDisposable.add(
                repository.addShopWithImage(
                    addShopReqData,
                    shopImageLocalPath,
                    doc_degree,
                    mContext
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val addShopResult = result as AddShopResponse
                        Timber.d("Edit Shop : " + ", SHOP: " + addShopReqData.shop_name + ", RESPONSE:" + result.message)
                        when (addShopResult.status) {
                            NetworkConstant.SUCCESS -> {
                                AppDatabase.getDBInstance()!!.addShopEntryDao()
                                    .updateIsEditUploaded(1, addShopReqData.shop_id)
                                progress_wheel.stopSpinning()
                                // (mContext as DashboardActivity).showSnackMessage("SUCCESS")
                                (mContext as DashboardActivity).updateFence()

                                showShopVerificationDialog()

                            }

                            NetworkConstant.SESSION_MISMATCH -> {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).clearData()
                                startActivity(
                                    Intent(
                                        mContext as DashboardActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                (mContext as DashboardActivity).overridePendingTransition(0, 0)
                                (mContext as DashboardActivity).finish()
                            }

                            else -> {
                                progress_wheel.stopSpinning()
                            }
                        }
                        BaseActivity.isApiInitiated = false
                    }, { error ->
                        error.printStackTrace()
                        BaseActivity.isApiInitiated = false
                        //(mContext as DashboardActivity).showSnackMessage(getString(R.string.unable_to_sync))
                    })
            )
        }
    }


    private fun callOtpSentApi(shop_id: String) {
        val repository = OtpSentRepoProvider.otpSentRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.otpSent(shop_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val addShopResult = result as BaseResponse
                    progress_wheel.stopSpinning()
                    /*if (addShopResult.status == NetworkConstant.SUCCESS) {

 (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
 showOtpVerificationDialog()

 } else {
 (mContext as DashboardActivity).showSnackMessage("OTP sent failed")
 loadFragment(FragType.ShopDetailFragment, true, mShopId)
 }*/

                    showOtpVerificationDialog(true)
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    /*(mContext as DashboardActivity).showSnackMessage("OTP sent failed")
 loadFragment(FragType.ShopDetailFragment, true, mShopId)*/
                    showOtpVerificationDialog(true)
                })
        )
    }

    private fun showOtpVerificationDialog(isShowTimer: Boolean) {
        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(mShopId)
        OTPVerificationDialog.getInstance(
            shop.ownerContactNumber,
            isShowTimer,
            shop.shopName,
            object : OTPVerificationDialog.OnOTPButtonClickListener {
                override fun onResentClick() {
                    callOtpSentApi(mShopId)
                }

                override fun onCancelClick() {
                    //(mContext as DashboardActivity).onBackPressed()
                    loadFragment(FragType.ShopDetailFragment, true, mShopId)
                }

                override fun onOkButtonClick(otp: String) {
                    //callOtpVerifyApi(otp, mShopId)

                    val distance = LocationWizard.getDistance(
                        shop.shopLat,
                        shop.shopLong,
                        Pref.current_latitude.toDouble(),
                        Pref.current_longitude.toDouble()
                    )

                    if (distance * 1000 <= 20)
                        callOtpVerifyApi(otp, mShopId)
                    else
                        (mContext as DashboardActivity).showSnackMessage("OTP can be verified only from the shop.")
                }
            }).show((mContext as DashboardActivity).supportFragmentManager, "OTPVerificationDialog")
    }

    private fun callOtpVerifyApi(otp: String, shop_id: String) {
        val repository = OtpVerificationRepoProvider.otpVerifyRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.otpVerify(shop_id, otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val addShopResult = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (addShopResult.status == NetworkConstant.SUCCESS) {
                        AppDatabase.getDBInstance()!!.addShopEntryDao()
                            .updateIsOtpVerified("true", shop_id)
                        (mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                        loadFragment(FragType.ShopDetailFragment, true, mShopId)
                    } else {
                        (mContext as DashboardActivity).showSnackMessage("OTP verification failed.")
                        showOtpVerificationDialog(false)
                    }
                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage("OTP verification failed.")
                    showOtpVerificationDialog(false)
                })
        )
    }

    fun getGalleryImage(context: Context, data: Intent?) {
        val selectedImage = data?.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            context.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        filePath = picturePath
        cursor.close()
    }

    private fun reimbursementPic(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200) {
        isProfile = false
        progress_wheel.stopSpinning()
        (getFragment() as ReimbursementFragment).setImage(filePath)
        /*} else {
 editProfilePic(AppUtils.getCompressImage(filePath))
 }*/
    }

    private fun reimbursementEditPic(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200) {
        isProfile = false
        progress_wheel.stopSpinning()
        (getFragment() as EditReimbursementFragment).setImage(filePath)
        /*} else {
 editProfilePic(AppUtils.getCompressImage(filePath))
 }*/
    }

    private fun addBillingPic(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")
        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as AddBillingFragment).setCameraImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun addBillingCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as AddBillingFragment).setCameraImage(file)
    }

    private fun addDynamicFormCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as AddDynamicFragment).setImage(file)
    }

    private fun addDynamicFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as AddDynamicFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    // Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101
    private fun addPDFPic(fileSize: Long) {
// val fileSizeInKB = fileSize / 1024
        val fileSizeInMb = fileSize.toDouble() / (1024 * 1024)
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInMb MB")

        if (!TextUtils.isEmpty("5")) {
            if (fileSizeInMb <= 5.toInt()) {
                val file = File(filePath)
                println("stock_img set img 4 hit")
                (getFragment() as MultipleImageFileUploadonStock).setImage(file)
            } else
                showSnackMessage("More than " + 5 + " MB file is not allowed")
        }
    }
    // end Rev 21.0 DashboardActivity AppV 4.0.8 saheli 12/05/2023 mantis 26101

    private fun addActivityFormCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as AddActivityFragment).setImage(file)
    }

    private fun addActivityFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as AddActivityFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun wipCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as WorkInProgressFragment).setImage(file)
    }

    private fun wipDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as WorkInProgressFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun wohCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as WorkOnHoldFragment).setImage(file)
    }

    private fun wohDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as WorkOnHoldFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun workCompletedCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as WorkCompletedFragment).setImage(file)
    }

    private fun workCompletedDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as WorkCompletedFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun workCancelledCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as WorkCancelledFragment).setImage(file)
    }

    private fun workCancelledDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as WorkCancelledFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun updateReviewCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as UpdateReviewFragment).setImage(file)
    }

    private fun updatePhotoRegAadhaarCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        //(getFragment() as ProtoRegistrationFragment).setImage(file)
    }


    private fun updateReviewDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as UpdateReviewFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun updatePhotoAadhaarDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")
        Pref.maxFileSize = "400"
        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as ProtoRegistrationFragment).setDoc(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun editActivityFormCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as EditActivityFragment).setImage(file)
    }

    private fun addCollectionCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(resultUri.path!!)

                if (getFragment() != null) {
                    when {
                        getFragment() is NearByShopsListFragment -> (getFragment() as NearByShopsListFragment).setImage(
                            file
                        )

                        getFragment() is NewOrderListFragment -> (getFragment() as NewOrderListFragment).setImage(
                            file
                        )

                        getFragment() is NewDateWiseOrderListFragment -> (getFragment() as NewDateWiseOrderListFragment).setImage(
                            file
                        )

                        getFragment() is ShopBillingListFragment -> (getFragment() as ShopBillingListFragment).setImage(
                            file
                        )

                        getFragment() is ViewAllOrderListFragment -> (getFragment() as ViewAllOrderListFragment).setImage(
                            file
                        )
                    }
                }
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun editActivityFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as EditActivityFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }


    private fun addEditDocFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as DocumentListFragment).setDocument(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun addInAddSchedulerFormImageDocFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

    }


    private fun addCollectionDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)

                if (getFragment() != null) {
                    when {
                        getFragment() is NearByShopsListFragment -> (getFragment() as NearByShopsListFragment).setImage(
                            file
                        )

                        getFragment() is NewOrderListFragment -> (getFragment() as NewOrderListFragment).setImage(
                            file
                        )

                        getFragment() is NewDateWiseOrderListFragment -> (getFragment() as NewDateWiseOrderListFragment).setImage(
                            file
                        )

                        getFragment() is ShopBillingListFragment -> (getFragment() as ShopBillingListFragment).setImage(
                            file
                        )

                        getFragment() is ViewAllOrderListFragment -> (getFragment() as ViewAllOrderListFragment).setImage(
                            file
                        )
                    }
                }
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun editDynamicFormCroppedImg(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        (getFragment() as EditDynamicFragment).setImage(file)
    }

    private fun editDynamicFormDocument(fileSize: Long) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        if (!TextUtils.isEmpty(Pref.maxFileSize)) {
            if (fileSizeInKB <= Pref.maxFileSize.toInt()) {
                val file = File(filePath)
                (getFragment() as EditDynamicFragment).setImage(file)
            } else
                showSnackMessage("More than " + Pref.maxFileSize + " KB file is not allowed")
        }
    }

    private fun addAttendanceCroppedImg(
        fileSize: Long,
        resultUri: Uri,
        isFromAddAttendance: Boolean
    ) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
        if (isFromAddAttendance)
            (getFragment() as AddAttendanceFragment).setCameraImage(file)
        else
            (getFragment() as DailyPlanListFragment).setCameraImage(file)
    }

    private fun addDashboardStartCroppedImg(
        fileSize: Long,
        resultUri: Uri,
        isFromAddAttendance: Boolean
    ) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(resultUri.path!!)
// (getFragment() as DashboardFragment).setCameraImage(file)

    }


    private fun addAttendanceImg(fileSize: Long, isFromAddAttendance: Boolean) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression==========> $fileSizeInKB KB")

        val file = File(filePath)
        if (isFromAddAttendance)
            (getFragment() as AddAttendanceFragment).setCameraImage(file)
        else
            (getFragment() as DailyPlanListFragment).setCameraImage(file)
    }

    private fun editProfilePic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        //Toaster.msgShort(this,"Addshop pic size : "+fileSizeInKB.toString()+" kb")
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200) {
        isProfile = false
        (getFragment() as MyProfileFragment).setImage(resultUri.path!!)
        /*} else {
 editProfilePic(AppUtils.getCompressImage(filePath))
 }*/
    }

    private fun getAddAadhaarVerifyPic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200)
        (getFragment() as PhotoRegAadhaarFragment).setImage(resultUri, fileSizeInKB)
        /*else {
 getAddShopPic(AppUtils.getCompressOldImage(resultUri.toString(), this), resultUri)
 }*/
    }

    private fun getAddFacePic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200)
        (getFragment() as RegisTerFaceFragment).setImage(resultUri, fileSizeInKB)
        /*else {
 getAddShopPic(AppUtils.getCompressOldImage(resultUri.toString(), this), resultUri)
 }*/
    }

    private fun getDamagedPic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200)
        (getFragment() as ShopDamageProductSubmitFrag).setImage(resultUri, fileSizeInKB)
        /*else {
 getAddShopPic(AppUtils.getCompressOldImage(resultUri.toString(), this), resultUri)
 }*/
    }

    private fun getAddShopPic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200)
        (getFragment() as AddShopFragment).setImage(resultUri, fileSizeInKB)
        /*else {
 getAddShopPic(AppUtils.getCompressOldImage(resultUri.toString(), this), resultUri)
 }*/
    }

    private fun getEditShopPic(fileSize: Long, resultUri: Uri) {
        val fileSizeInKB = fileSize / 1024
        Log.e("Dashboard", "image file size after compression-----------------> $fileSizeInKB KB")
        //if (fileSizeInKB <= 200)
        (getFragment() as ShopDetailFragment).setImage(resultUri, fileSizeInKB)
        /*else {
 getEditShopPic(AppUtils.getCompressOldImage(resultUri.toString(), this), resultUri)
 }*/
    }

    private fun addShopVisitPic(
        fileSize: Long,
        imageUpDateTime: String,
        shop: String,
        image: String
    ) {
        val fileSizeInKB = fileSize / 1024
        Timber.e("Dashboard: $shop image file size after compression==========> $fileSizeInKB KB")
        if (fileSizeInKB > 200) {
            val newFileSize = AppUtils.getCompressImage(image)
            val newFileSizeInKB = newFileSize / 1024
            Timber.e("Dashboard: $shop new image file size after compression==========> $newFileSizeInKB KB")
        }
        val shopVisit = ShopVisitImageModelEntity()
        shopVisit.shop_id = mShopId
        shopVisit.shop_image = image //Environment.getExternalStorageDirectory().path
        shopVisit.visit_datetime = imageUpDateTime
        AppDatabase.getDBInstance()!!.shopVisitImageDao().insert(shopVisit)

        //AppUtils.isRevisit = false
        //loadFragment(FragType.ShopDetailFragment, true, mShopId)
        /*} else {
 addShopVisitPic(AppUtils.getCompressImage(filePath), imageUpDateTime)
 }*/
    }

    private fun callVisitShopImageUploadApi(
        mShopId: String,
        imageLink: String,
        imageUpDateTime: String
    ) {

        val visitImageShop = ShopVisitImageUploadInputModel()
        visitImageShop.session_token = Pref.session_token
        visitImageShop.user_id = Pref.user_id
        visitImageShop.shop_id = mShopId
        visitImageShop.visit_datetime = imageUpDateTime

        val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.visitShopWithImage(visitImageShop, imageLink, this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    progress_wheel.stopSpinning()
                    val logoutResponse = result as BaseResponse

                    if (logoutResponse.status == NetworkConstant.SUCCESS) {
                        AppDatabase.getDBInstance()!!.shopVisitImageDao()
                            .updateisUploaded(true, mShopId)
                    }
                    AppUtils.isRevisit = false
                    loadFragment(FragType.ShopDetailFragment, true, mShopId)
                    BaseActivity.isApiInitiated = false

                }, { error ->
                    BaseActivity.isApiInitiated = false
                    progress_wheel.stopSpinning()
                    error.printStackTrace()
                    (mContext as DashboardActivity).showSnackMessage(error.localizedMessage)

                    AppUtils.isRevisit = false
                    loadFragment(FragType.ShopDetailFragment, true, mShopId)
                })
        )
    }


    fun getCameraImage(data: Intent?) {

        val isCamera: Boolean
        isCamera = if (!AppUtils.isN) {
            if (data == null) {
                true
            } else {
                val action = data.action
                if (action == null) {
                    false
                } else {
                    action == android.provider.MediaStore.ACTION_IMAGE_CAPTURE
                }
            }
        } else
            true

        var selectedImageUri: Uri?
        if (isCamera) {
            selectedImageUri = Uri.parse(mCurrentPhotoPath) // outputFileUri;
            // outputFileUri = null;
        } else {
            selectedImageUri = data?.data
        }
        if (selectedImageUri == null)
            selectedImageUri = Uri.parse(mCurrentPhotoPath)
        val filemanagerstring = selectedImageUri!!.path

        val selectedImagePath = AppUtils.getPath(mContext as Activity, selectedImageUri)

        when {
            selectedImagePath != null -> filePath = selectedImagePath
            filemanagerstring != null -> filePath = filemanagerstring
            else -> {
                //Toaster.msgShort(baseActivity, "Unknown Path")
                Timber.e("Bitmap", "Unknown Path")
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Pref.isSefieAlarmed)
            captureFrontImage()
        else if (isCodeScan) {
            isCodeScan = false
            loadFragment(FragType.CodeScannerFragment, true, "")
        } else if (getFragment() is MyProfileFragment)
            (getFragment() as MyProfileFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddShopFragment)
            (getFragment() as AddShopFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is ShopDetailFragment)
            (getFragment() as ShopDetailFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is ReimbursementFragment)
            (getFragment() as ReimbursementFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddBillingFragment)
            (getFragment() as AddBillingFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddDynamicFragment)
            (getFragment() as AddDynamicFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is EditDynamicFragment)
            (getFragment() as EditDynamicFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is EditReimbursementFragment)
            (getFragment() as EditReimbursementFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddAttendanceFragment)
            (getFragment() as AddAttendanceFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is DailyPlanListFragment)
            (getFragment() as DailyPlanListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddActivityFragment)
            (getFragment() as AddActivityFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is EditActivityFragment)
            (getFragment() as EditActivityFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddTimeSheetFragment)
            (getFragment() as AddTimeSheetFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is EditTimeSheetFragment)
            (getFragment() as EditTimeSheetFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is DocumentListFragment)
            (getFragment() as DocumentListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is AddTaskFragment)
            (getFragment() as AddTaskFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is EditTaskFragment)
            (getFragment() as EditTaskFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is TaskListFragment)
            (getFragment() as TaskListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is NearByShopsListFragment)
            (getFragment() as NearByShopsListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is NewOrderListFragment)
            (getFragment() as NewOrderListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is NewDateWiseOrderListFragment)
            (getFragment() as NewDateWiseOrderListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is ViewAllOrderListFragment)
            (getFragment() as ViewAllOrderListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is ShopBillingListFragment)
            (getFragment() as ShopBillingListFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is ScanImageFragment)
            (getFragment() as ScanImageFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is WorkInProgressFragment)
            (getFragment() as WorkInProgressFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is WorkOnHoldFragment)
            (getFragment() as WorkOnHoldFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is WorkCompletedFragment)
            (getFragment() as WorkCompletedFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is WorkCancelledFragment)
            (getFragment() as WorkCancelledFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is UpdateReviewFragment)
            (getFragment() as UpdateReviewFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else if (getFragment() is DashboardFragment)
            (getFragment() as DashboardFragment).onRequestPermission(
                requestCode,
                permissions,
                grantResults
            )
        else {
            if (requestCode == PermissionHelper.REQUEST_CODE_CAMERA) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (PermissionHelper.checkStoragePermission(this)) {
                        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (AppUtils.isRevisit!!) {
                            //AppUtils.isRevisit = false
                            intent.putExtra("shopId", mShopId)
                            captureImage()
                            /*val uri = Uri.parse(Environment.getExternalStorageDirectory().absolutePath + System.currentTimeMillis() + "_fts.jpg")
 FTStorageUtils.IMG_URI = uri
 intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri)*/
                        } else if (isProfile)
                            captureImage()
                        else {
                            intent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                getPhotoFileUri(System.currentTimeMillis().toString() + ".png")
                            )
                            startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Camera permission has not been granted, cannot saved images",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (requestCode == PermissionHelper.REQUEST_CODE_STORAGE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (PermissionHelper.checkCameraPermission(this)) {

                        if (AppUtils.isProfile) {
                            val galleryIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                            startActivityForResult(
                                galleryIntent,
                                PermissionHelper.REQUEST_CODE_CAMERA
                            )
                        } else {
                            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                getPhotoFileUri(System.currentTimeMillis().toString() + ".png")
                            )
                            startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "External write permission has not been granted, cannot saved images",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            } else if (requestCode == PermissionHelper.TAG_LOCATION_RESULTCODE) {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (!Pref.isGeoFenceAdded)
                            takeActionOnGeofence()

                    }

                } else {
                    mPendingGeofenceTask = PendingGeofenceTask.NONE
                    PermissionHelper.checkLocationPermission(this, 0)


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                        this,
                        "Location permission has not been granted",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else
                permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun getPhotoFileUri(fileName: String): Uri {
        val folderPath = FTStorageUtils.getFolderPath(this)
        val imageFile = FTStorageUtils.overWriteFile(folderPath, fileName)
        // FTStorageUtils.IMG_URI = Uri.fromFile(imageFile)
        FTStorageUtils.IMG_URI =
            FileProvider.getUriForFile(this, "com.fieldtrackingsystem.provider", imageFile);
        return Uri.fromFile(imageFile)
    }


    fun callShopVisitConfirmationDialog(storeName: String, shopId: String) {
        val addShopEntity = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId)

        Handler().postDelayed(Runnable {
            if (addShopEntity != null && getFragment() != null && getFragment() !is GpsDisableFragment && forceLogoutDialog == null) {
                val userId = shopId.substring(0, shopId.indexOf("_"))
                if (/*userId == Pref.user_id &&*/ !Pref.isAutoLogout) {
                    Timber.e("=====User's shop (Dashboard Activity)========")
                    callDialog(addShopEntity, storeName, shopId)
                } else
                    Timber.e("=====Another user's shop (Dashboard Activity)========")
            }
        }, 350)

    }

    private fun callDialog(
        addShopDBModelEntity: AddShopDBModelEntity,
        storeName: String,
        shopId: String
    ) {

        var popupBody = ""

        shopName = storeName
        mStoreName = storeName
        contactNumber = AppDatabase.getDBInstance()!!.addShopEntryDao().getContactNumber(shopId)

        popupBody = if (Pref.isRevisitCaptureImage) {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer? Take image to complete your visit"
 else
 "Do you want to revisit $storeName($contactNumber) shop? Take image to complete your visit"*/

            "Wish to Revisit the selected ${Pref.shopText} $storeName($contactNumber) now? Take image to complete your visit"

        } else {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer?"
 else
 "Do you want to revisit $storeName($contactNumber) shop?"*/

            "Wish to Revisit the selected ${Pref.shopText} $storeName($contactNumber) now?"
        }

        /*var header = ""

 header = if (Pref.isReplaceShopText)
 "Revisit Customer"
 else
 "Revisit Shop"*/

        CommonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            popupBody,
            "NO",
            "YES",
            object : CommonDialogClickListener {
                override fun onLeftClick() {
                    cancelNotification(shopId)

                }

                override fun onRightClick(editableData: String) {
                    mAddShopDBModelEntity = addShopDBModelEntity
                    terminateOtherShopVisit(1, addShopDBModelEntity, storeName, shopId, null, null)

                    if (Pref.isShowShopVisitReason)
                        return

                    if (Pref.IsmanualInOutTimeRequired) {
                        mShopId = shopId
                        mStoreName = storeName
                        revisitShop("")
                    } else {
                        startOwnShopRevisit(addShopDBModelEntity, storeName, shopId)
                    }

                    /*if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity)) {
 val photo = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
 val uri = Uri.parse(*//*"file:///sdcard/photo.jpg"*//*Environment.getExternalStorageDirectory().absolutePath + System.currentTimeMillis() + "_fts.jpg")
 FTStorageUtils.IMG_URI = uri
 photo.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri)
 startActivityForResult(photo, PermissionHelper.REQUEST_CODE_CAMERA)
 }*/

                }

            }).show(supportFragmentManager, "CommonDialog")
    }

    private fun startOwnShopRevisit(
        addShopDBModelEntity: AddShopDBModelEntity,
        storeName: String,
        shopId: String
    ) {
        cancelNotification(shopId)
        mShopId = shopId
        mStoreName = storeName
        mAddShopDBModelEntity = addShopDBModelEntity

        //loadFragment(FragType.ShopDetailFragment, true, shopId)
        //takePhotoFromCamera(PermissionHelper.REQUEST_CODE_CAMERA)
        AppUtils.isRevisit = true
        //if (PermissionHelper.checkCameraPermission(mContext as DashboardActivity)) {
        /*intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
 //intent.putExtra("shopId", shopId)
 intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(System.currentTimeMillis().toString() + ".png"))
 startActivityForResult(intent, PermissionHelper.REQUEST_CODE_CAMERA)*/
        // captureImage()
        //}
        isOtherUsersShopRevisit = false

        /*28-12-2021*/
        var shopNameByID =
            AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopType(mShopId)!!.toString()

        if (Pref.isRevisitCaptureImage) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /*if(Pref.IsnewleadtypeforRuby && shopNameByID.equals("2")){
 initPermissionCheckRubyCUstomi(shopNameByID)
 }
 else if (Pref.IsnewleadtypeforRuby && !shopNameByID.equals("2")){
 callFeedbackDialog("")
 }
 else{
 initPermissionCheck()
 }*/
                initPermissionCheck()
            } else {
                if (Pref.IsnewleadtypeforRuby && shopNameByID.equals("2")) {
                    captureFrontImage()
                } else {
                    captureImage()
                }
            }
        } else {
            callFeedbackDialog("")
        }
    }

    private fun callFeedbackDialog(image: String) {
        revisitImage = image

        feedbackDialog = AddFeedbackSingleBtnDialog.getInstance(
            shopName + "\n" + contactNumber,
            getString(R.string.confirm_revisit),
            mShopId,
            object : AddFeedbackSingleBtnDialog.OnOkClickListener {

                override fun onOkClick(
                    mFeedback: String,
                    mNextVisitDate: String,
                    filePath: String,
                    mapproxValue: String,
                    mprosId: String,
                    sel_extraContNameStr: String,
                    sel_extraContPhStr: String,
                    filePathNW: String
                ) {
                    /*28-09-2021 For Gupta Power*/
                    mAudioFilePathNW = filePathNW
                    revisit_extraContName = sel_extraContNameStr
                    revisit_extraContPh = sel_extraContPhStr

                    if (Pref.RevisitRemarksMandatory && !TextUtils.isEmpty(mFeedback)) {
                        if (mapproxValue != null || !mapproxValue.equals("") && (mprosId != null || !mprosId.equals(
                                ""
                            ))
                        ) {
                            feedback = mFeedback
                            nextVisitDate = mNextVisitDate
                            mFilePath = filePath
                            approxValue = mapproxValue
                            prosId = mprosId
                        } else {
                            feedback = mFeedback
                            nextVisitDate = mNextVisitDate
                            mFilePath = filePath
                        }

                        if (Pref.isFingerPrintMandatoryForVisit) {
                            if (isFingerPrintSupported) {
                                showFingerprintPopup()
                            } else {
                                revisitShop(image)
                            }
                        } else
                            revisitShop(image)
                    } else {
                        if (!TextUtils.isEmpty(mFeedback))
                            feedback = mFeedback

                        if (mapproxValue != null || !mapproxValue.equals("") && (mprosId != null || !mprosId.equals(
                                ""
                            ))
                        ) {
                            feedback = mFeedback
                            nextVisitDate = mNextVisitDate
                            mFilePath = filePath
                            approxValue = mapproxValue
                            prosId = mprosId
                        } else {
                            feedback = mFeedback
                            nextVisitDate = mNextVisitDate
                            mFilePath = filePath
                        }

                        if (Pref.isFingerPrintMandatoryForVisit) {
                            if (isFingerPrintSupported)
                                showFingerprintPopup()
                            else
                                revisitShop(image)
                        } else
                            revisitShop(image)
                    }
                    /* if (!TextUtils.isEmpty(mFeedback))
 feedback = mFeedback

 nextVisitDate = mNextVisitDate
 mFilePath = filePath

 if (Pref.isFingerPrintMandatoryForVisit) {
 if (isFingerPrintSupported)
 showFingerprintPopup()
 else
 revisitShop(image)
 } else
 revisitShop(image)*/
                }

                override fun onCloseClick(
                    mfeedback: String,
                    sel_extraContNameStr: String,
                    sel_extraContPhStr: String
                ) {
                    // 9.0 DashboardActivity AppV 4.0.6 Suman 24-01-2023 Corss button with multi contact select
                    revisit_extraContName = sel_extraContNameStr
                    revisit_extraContPh = sel_extraContPhStr
                    feedback = mfeedback
                    if (Pref.isFingerPrintMandatoryForVisit) {
                        if (isFingerPrintSupported)
                            showFingerprintPopup()
                        else
                            revisitShop(image)
                    } else
                        revisitShop(image)
                }

                override fun onClickCompetitorImg() {
                    feedBackDialogCompetetorImg = true
                }
            })
        feedbackDialog?.show(
            (mContext as DashboardActivity).supportFragmentManager,
            "AddFeedbackSingleBtnDialog"
        )
    }

    private var fingerprintDialog: FingerprintDialog? = null
    private fun showFingerprintPopup() {
        checkForFingerPrint()

        fingerprintDialog = FingerprintDialog()
        fingerprintDialog?.show(supportFragmentManager, "")
    }

    fun callDialog(teamShop: TeamShopListDataModel) {

        var popupBody = ""

        shopName = teamShop.shop_name
        contactNumber = teamShop.shop_contact

        popupBody = if (Pref.isRevisitCaptureImage) {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer? Take image to complete your visit"
 else
 "Do you want to revisit $storeName($contactNumber) shop? Take image to complete your visit"*/

            "Wish to Revisit the selected ${Pref.shopText} ${teamShop.shop_name}(${teamShop.shop_contact}) now? Take image to complete your visit"

        } else {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer?"
 else
 "Do you want to revisit $storeName($contactNumber) shop?"*/

            "Wish to Revisit the selected ${Pref.shopText} ${teamShop.shop_name}(${teamShop.shop_contact}) now?"
        }

        CommonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            popupBody,
            "NO",
            "YES",
            object : CommonDialogClickListener {
                override fun onLeftClick() {
                    //cancelNotification(shopId)
                }

                override fun onRightClick(editableData: String) {
                    //cancelNotification(shopId)
                    mAddShopDBModelEntity = AddShopDBModelEntity()
                    mAddShopDBModelEntity?.shop_id = teamShop.shop_id
                    terminateOtherShopVisit(2, null, "", "", teamShop, null)

                    if (Pref.isShowShopVisitReason)
                        return

                    callExtraTeamShopListApi(teamShop)
                    //startRevisitOnlineTeamShop(teamShop)
                }

            }).show(supportFragmentManager, "CommonDialog")
    }

    private fun callExtraTeamShopListApi(teamShop: TeamShopListDataModel) {
        try {
            val repository = ShopListRepositoryProvider.provideShopListRepository()
            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                repository.getExtraTeamShopList(Pref.session_token!!, Pref.user_id!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var shopList = result as ShopListResponse
                        if (shopList.status == NetworkConstant.SUCCESS) {
                            progress_wheel.stopSpinning()
                            var obj =
                                shopList.data!!.shop_list!!.filter { it.shop_id.equals(teamShop.shop_id) } as ArrayList<ShopData>
                            if (obj.size > 0) {
                                teamShop.total_visit_count =
                                    obj.get(obj.size - 1).total_visit_count!!
                            }
                            startRevisitOnlineTeamShop(teamShop)

                        } else {
                            progress_wheel.stopSpinning()
                            startRevisitOnlineTeamShop(teamShop)
                        }
                    }, { error ->
                        error.printStackTrace()
                        progress_wheel.stopSpinning()
                        startRevisitOnlineTeamShop(teamShop)
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.stopSpinning()
            startRevisitOnlineTeamShop(teamShop)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun startRevisitOnlineTeamShop(teamShop: TeamShopListDataModel) {
        mShopId = teamShop.shop_id
        mStoreName = teamShop.shop_name

        mAddShopDBModelEntity = AddShopDBModelEntity()
        mAddShopDBModelEntity?.shop_id = teamShop.shop_id
        mAddShopDBModelEntity?.address = teamShop.shop_address
        mAddShopDBModelEntity?.pinCode = teamShop.shop_pincode
        mAddShopDBModelEntity?.shopName = teamShop.shop_name
        mAddShopDBModelEntity?.shopLat = teamShop.shop_lat.toDouble()
        mAddShopDBModelEntity?.shopLong = teamShop.shop_long.toDouble()
        mAddShopDBModelEntity?.isUploaded = true
        mAddShopDBModelEntity?.ownerContactNumber = teamShop.shop_contact
// mAddShopDBModelEntity?.totalVisitCount = teamShop.total_visited
        try {
            /*Team new work*/
            if (teamShop.total_visit_count!!.equals("")) {
                mAddShopDBModelEntity?.totalVisitCount = "1"
            } else {
                mAddShopDBModelEntity?.totalVisitCount = teamShop.total_visit_count!!.toString()
            }
// mAddShopDBModelEntity?.totalVisitCount = teamShop.total_visit_count!!.toString()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        mAddShopDBModelEntity?.lastVisitedDate = teamShop.last_visit_date
        mAddShopDBModelEntity?.type = teamShop.shop_type

        if (teamShop.entity_code == null)
            mAddShopDBModelEntity?.entity_code = ""
        else
            mAddShopDBModelEntity?.entity_code = teamShop.entity_code

        if (teamShop.area_id == null)
            mAddShopDBModelEntity?.area_id = ""
        else
            mAddShopDBModelEntity?.area_id = teamShop.area_id

        if (TextUtils.isEmpty(teamShop.model_id))
            mAddShopDBModelEntity?.model_id = ""
        else
            mAddShopDBModelEntity?.model_id = teamShop.model_id

        if (TextUtils.isEmpty(teamShop.primary_app_id))
            mAddShopDBModelEntity?.primary_app_id = ""
        else
            mAddShopDBModelEntity?.primary_app_id = teamShop.primary_app_id

        if (TextUtils.isEmpty(teamShop.secondary_app_id))
            mAddShopDBModelEntity?.secondary_app_id = ""
        else
            mAddShopDBModelEntity?.secondary_app_id = teamShop.secondary_app_id

        if (TextUtils.isEmpty(teamShop.lead_id))
            mAddShopDBModelEntity?.lead_id = ""
        else
            mAddShopDBModelEntity?.lead_id = teamShop.lead_id

        if (TextUtils.isEmpty(teamShop.stage_id))
            mAddShopDBModelEntity?.stage_id = ""
        else
            mAddShopDBModelEntity?.stage_id = teamShop.stage_id

        if (TextUtils.isEmpty(teamShop.funnel_stage_id))
            mAddShopDBModelEntity?.funnel_stage_id = ""
        else
            mAddShopDBModelEntity?.funnel_stage_id = teamShop.funnel_stage_id

        if (TextUtils.isEmpty(teamShop.booking_amount))
            mAddShopDBModelEntity?.booking_amount = ""
        else
            mAddShopDBModelEntity?.booking_amount = teamShop.booking_amount


        if (TextUtils.isEmpty(teamShop.type_id))
            mAddShopDBModelEntity?.type_id = ""
        else
            mAddShopDBModelEntity?.type_id = teamShop.type_id

        /*Team new work*/
        if (TextUtils.isEmpty(teamShop.owner_name))
            mAddShopDBModelEntity?.ownerName = ""
        else
            mAddShopDBModelEntity?.ownerName = teamShop.owner_name

        /*Team new work*/
        try {
            var shopisExistTblbyThisId = AppDatabase.getDBInstance()!!.addShopEntryDao()
                .getShopByIdN(mAddShopDBModelEntity?.shop_id)
            if (shopisExistTblbyThisId != null)
            //delete by shop id
                AppDatabase.getDBInstance()!!.addShopEntryDao()
                    .deleteShopById(mAddShopDBModelEntity?.shop_id)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        mAddShopDBModelEntity?.isOwnshop = false

        AppDatabase.getDBInstance()!!.addShopEntryDao().insert(mAddShopDBModelEntity)

        AppUtils.isRevisit = true
        val userId = teamShop.shop_id.substring(0, teamShop.shop_id.indexOf("_"))
        isOtherUsersShopRevisit = userId != Pref.user_id

        if (Pref.isRevisitCaptureImage) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                initPermissionCheck()
            else
                captureImage()
        } else {
            callFeedbackDialog("")
        }
    }

    fun callDialog(teamShop: MemberShopEntity) {

        var popupBody = ""

        shopName = teamShop.shop_name!!
        contactNumber = teamShop.shop_contact!!

        popupBody = if (Pref.isRevisitCaptureImage) {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer? Take image to complete your visit"
 else
 "Do you want to revisit $storeName($contactNumber) shop? Take image to complete your visit"*/

            "Wish to Revisit the selected ${Pref.shopText} ${teamShop.shop_name}(${teamShop.shop_contact}) now? Take image to complete your visit"

        } else {
            /*if (Pref.isReplaceShopText)
 "Do you want to revisit $storeName($contactNumber) customer?"
 else
 "Do you want to revisit $storeName($contactNumber) shop?"*/

            "Wish to Revisit the selected ${Pref.shopText} ${teamShop.shop_name}(${teamShop.shop_contact}) now?"
        }

        CommonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            popupBody,
            "NO",
            "YES",
            object : CommonDialogClickListener {
                override fun onLeftClick() {
                    //cancelNotification(shopId)
                }

                override fun onRightClick(editableData: String) {
                    //cancelNotification(shopId)
                    mAddShopDBModelEntity = AddShopDBModelEntity()
                    mAddShopDBModelEntity?.shop_id = teamShop.shop_id
                    terminateOtherShopVisit(3, null, "", "", null, teamShop)

                    if (Pref.isShowShopVisitReason)
                        return

                    startRevisitOfflineTeamShop(teamShop)

                }

            }).show(supportFragmentManager, "CommonDialog")
    }

    private fun startRevisitOfflineTeamShop(teamShop: MemberShopEntity) {
        mShopId = teamShop.shop_id!!
        mStoreName = teamShop.shop_name!!

        mAddShopDBModelEntity = AddShopDBModelEntity()
        mAddShopDBModelEntity?.shop_id = teamShop.shop_id
        mAddShopDBModelEntity?.address = teamShop.shop_address
        mAddShopDBModelEntity?.pinCode = teamShop.shop_pincode
        mAddShopDBModelEntity?.shopName = teamShop.shop_name
        mAddShopDBModelEntity?.shopLat = teamShop.shop_lat?.toDouble()
        mAddShopDBModelEntity?.shopLong = teamShop.shop_long?.toDouble()
        mAddShopDBModelEntity?.isUploaded = true
        mAddShopDBModelEntity?.ownerContactNumber = teamShop.shop_contact
        mAddShopDBModelEntity?.totalVisitCount = teamShop.total_visited
        mAddShopDBModelEntity?.lastVisitedDate = teamShop.last_visit_date
        mAddShopDBModelEntity?.type = teamShop.shop_type

        if (teamShop.entity_code == null)
            mAddShopDBModelEntity?.entity_code = ""
        else
            mAddShopDBModelEntity?.entity_code = teamShop.entity_code


        if (teamShop.area_id == null)
            mAddShopDBModelEntity?.area_id = ""
        else
            mAddShopDBModelEntity?.area_id = teamShop.area_id

        if (TextUtils.isEmpty(teamShop.model_id))
            mAddShopDBModelEntity?.model_id = ""
        else
            mAddShopDBModelEntity?.model_id = teamShop.model_id

        if (TextUtils.isEmpty(teamShop.primary_app_id))
            mAddShopDBModelEntity?.primary_app_id = ""
        else
            mAddShopDBModelEntity?.primary_app_id = teamShop.primary_app_id

        if (TextUtils.isEmpty(teamShop.secondary_app_id))
            mAddShopDBModelEntity?.secondary_app_id = ""
        else
            mAddShopDBModelEntity?.secondary_app_id = teamShop.secondary_app_id

        if (TextUtils.isEmpty(teamShop.lead_id))
            mAddShopDBModelEntity?.lead_id = ""
        else
            mAddShopDBModelEntity?.lead_id = teamShop.lead_id

        if (TextUtils.isEmpty(teamShop.stage_id))
            mAddShopDBModelEntity?.stage_id = ""
        else
            mAddShopDBModelEntity?.stage_id = teamShop.stage_id

        if (TextUtils.isEmpty(teamShop.funnel_stage_id))
            mAddShopDBModelEntity?.funnel_stage_id = ""
        else
            mAddShopDBModelEntity?.funnel_stage_id = teamShop.funnel_stage_id

        if (TextUtils.isEmpty(teamShop.booking_amount))
            mAddShopDBModelEntity?.booking_amount = ""
        else
            mAddShopDBModelEntity?.booking_amount = teamShop.booking_amount


        if (TextUtils.isEmpty(teamShop.type_id))
            mAddShopDBModelEntity?.type_id = ""
        else
            mAddShopDBModelEntity?.type_id = teamShop.type_id


        AppDatabase.getDBInstance()!!.addShopEntryDao().insert(mAddShopDBModelEntity)

        AppUtils.isRevisit = true
        val userId = teamShop.shop_id?.substring(0, teamShop.shop_id?.indexOf("_")!!)
        isOtherUsersShopRevisit = userId != Pref.user_id

        if (Pref.isRevisitCaptureImage) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                initPermissionCheck()
            else
                captureImage()
        } else {
            callFeedbackDialog("")
        }
    }

    private var permissionUtils: PermissionUtils? = null
    private fun initPermissionCheck() {

        //begin mantis id 26741 Storage permission updation Suman 22-08-2023
        var permissionList = arrayOf<String>(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        } else {
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
//end mantis id 26741 Storage permission updation Suman 22-08-2023

        permissionUtils = PermissionUtils(
            mContext as Activity,
            object : PermissionUtils.OnPermissionListener {
                override fun onPermissionGranted() {
                    if (!isCodeScan)
                        captureImage()
                    else {
                        isCodeScan = false
                        loadFragment(FragType.CodeScannerFragment, true, "")
                    }
                }

                override fun onPermissionNotGranted() {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
                }
// mantis id 26741 Storage permission updation Suman 22-08-2023
            },
            permissionList
        )// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun initPermissionCheckRubyCUstomi(shopNameByID: String) {

        //begin mantis id 26741 Storage permission updation Suman 22-08-2023
        var permissionList = arrayOf<String>(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        } else {
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
//end mantis id 26741 Storage permission updation Suman 22-08-2023

        permissionUtils = PermissionUtils(
            mContext as Activity,
            object : PermissionUtils.OnPermissionListener {
                override fun onPermissionGranted() {
                    if (!isCodeScan)
                        if (Pref.IsnewleadtypeforRuby && shopNameByID.equals("2")) {
                            captureFrontImage()
                        } else {
                            captureImage()
                        }
                    else {
                        isCodeScan = false
                        loadFragment(FragType.CodeScannerFragment, true, "")
                    }
                }

                override fun onPermissionNotGranted() {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
                }
// mantis id 26741 Storage permission updation Suman 22-08-2023
            },
            permissionList
        )// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun captureImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = AppUtils.createImageFile()
                // Save a file: path for use with ACTION_VIEW intents
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI: Uri = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                            mContext,
                            mContext.packageName + ".provider",
                            photoFile
                        )
                    } else
                        Uri.fromFile(photoFile)
                    mCurrentPhotoPath = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 0)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    (mContext as DashboardActivity).startActivityForResult(
                        takePictureIntent,
                        PermissionHelper.REQUEST_CODE_CAMERA
                    )
                }
            } catch (ex: Exception) {
                // Error occurred while creating the File
                ex.printStackTrace()
                return
            }
        }
    }

    fun captureFrontImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = AppUtils.createImageFile()
                // Save a file: path for use with ACTION_VIEW intents
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI: Uri = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                            mContext,
                            mContext.packageName + ".provider",
                            photoFile
                        )
                    } else
                        Uri.fromFile(photoFile)
                    mCurrentPhotoPath = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    (mContext as DashboardActivity).startActivityForResult(
                        takePictureIntent,
                        PermissionHelper.REQUEST_CODE_CAMERA
                    )
                }
            } catch (ex: Exception) {
                // Error occurred while creating the File
                ex.printStackTrace()
                return
            }
        }
    }

    fun openFileManager() {
// val intent = Intent(Intent.ACTION_GET_CONTENT)
// intent.type = "*/*"
// startActivityForResult(intent, REQUEST_CODE_DOCUMENT)

        browseDocuments(this@DashboardActivity, REQUEST_CODE_DOCUMENT)
    }

    // rev start 21.0 AppV 4.0.8 saheli 12/05/2023 mantis 26101
    fun openPDFFileManager() {
// val intent = Intent(Intent.ACTION_GET_CONTENT)
// intent.type = "*/*"
// startActivityForResult(intent, REQUEST_CODE_DOCUMENT)

        browsePDFDocuments(this@DashboardActivity, REQUEST_CODE_DOCUMENT_PDF)
    }
    // end rev start 21.0 AppV 4.0.8 saheli 12/05/2023 mantis 26101


    fun takePhotoFromCamera(selectPicture: Int) {

        val filePath =
            AppUtils.getPhotoFilePath(mContext, "Image_" + System.currentTimeMillis().toString())
        AppUtils.sImagePath = filePath
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(filePath)))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        this.startActivityForResult(intent, selectPicture)
    }

    fun cancelNotification(shopId: String) {
        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(shopId.hashCode())
    }

    private fun calllogoutApi(user_id: String, session_id: String) {
        if (Pref.latitude == null || Pref.longitude == null) {
            showSnackMessage("Can't fetch location.Please wait for some time ")
            return
        }
        if (BaseActivity.isApiInitiated)
            return
        BaseActivity.isApiInitiated = true

        var location = ""

        if (Pref.latitude != "0.0" && Pref.longitude != "0.0") {
            location = LocationWizard.getAdressFromLatlng(
                this,
                Pref.latitude?.toDouble()!!,
                Pref.longitude?.toDouble()!!
            )

            if (location.contains("http"))
                location = "Unknown"
        }

        Timber.d("LOGOUT : " + "REQUEST : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name)
        Timber.d("==============================LOGOUT INPUT PARAMS==============================")
        Timber.d("LOGOUT : USER ID======> $user_id")
        Timber.d("LOGOUT : SESSION ID======> $session_id")
        Timber.d("LOGOUT : LAT=========> " + Pref.latitude)
        Timber.d("LOGOUT : LONG==========> " + Pref.longitude)
        Timber.d("LOGOUT : LOGOUT TIME========> " + AppUtils.getCurrentDateTime())
        Timber.d("LOGOUT : IS AUTO LOGOUT=======> 0")
        Timber.d("LOGOUT : LOCATION=========> $location")
        Timber.d("===============================================================================")

        val repository = LogoutRepositoryProvider.provideLogoutRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.logout(
                user_id,
                session_id,
                Pref.latitude!!,
                Pref.longitude!!,
                AppUtils.getCurrentDateTime(),
                "0.0",
                "0",
                location
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    progress_wheel.stopSpinning()
                    var logoutResponse = result as BaseResponse
                    Timber.d(
                        "LOGOUT : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() +
                                ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message
                    )
                    if (logoutResponse.status == NetworkConstant.SUCCESS) {
                        syncShopList()
                    } else if (logoutResponse.status == NetworkConstant.SESSION_MISMATCH) {
// clearData()
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finishAffinity()
                    } else {
                        //progress_wheel.stopSpinning()
                        showSnackMessage("Failed to logout")
                    }
                    BaseActivity.isApiInitiated = false


                }, { error ->
                    BaseActivity.isApiInitiated = false
                    progress_wheel.stopSpinning()
                    error.printStackTrace()
                    Timber.d(
                        "LOGOUT : " + "RESPONSE ERROR: " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name +
                                ",MESSAGE : " + error.localizedMessage
                    )
                    (mContext as DashboardActivity).showSnackMessage(error.localizedMessage)
                })
        )


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount == 0 || getCurrentFragType() == FragType.DashboardFragment || getCurrentFragType() == FragType.MyLearningFragment /*|| getCurrentFragType() == FragType.NotificationLMSFragment*/) {
                    AppUtils.hideSoftKeyboard(this)
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    onBackPressed()
                }

                return true
            }

            R.id.action_search -> {
                // Open the search view on the menu item click.
// search
                return true
            }
//
//

        }
        return super.onOptionsItemSelected(item)
    }

// inner class AutoStart : BroadcastReceiver() {
// override fun onReceive(context: Context, intent: Intent) {
// if (intent.action == "android.intent.action.BOOT_COMPLETED") {
// Toast.makeText(this@DashboardActivity, "LOGOUT", Toast.LENGTH_LONG).show()
// }
// }
// }

    fun showProgress() {
        if (!progress_wheel.isSpinning)
            progress_wheel.spin()
    }

    fun hideProgress() {
        progress_wheel.stopSpinning()
    }

    fun fetchActivityList() {
        if (!Pref.isLocationActivitySynced) {
            Timber.d("fetchActivityList")
            val fetchLocReq = FetchLocationRequest()
            fetchLocReq.user_id = Pref.user_id
            fetchLocReq.session_token = Pref.session_token
            fetchLocReq.date_span = ""
            fetchLocReq.from_date = AppUtils.getCurrentDate()
            fetchLocReq.to_date = AppUtils.getCurrentDate()
            callFetchLocationApi(fetchLocReq)

            /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }*/


        } /*else {
 if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }
 }*/
    }

    private fun callFetchLocationApi(fetchLocReq: FetchLocationRequest) {
        val repository = LocationFetchRepositoryProvider.provideLocationFetchRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.fetchLocationUpdate(fetchLocReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val shopList = result as FetchLocationResponse
                    if (shopList.status == NetworkConstant.SUCCESS) {
                        convertToModelAndSave(shopList.location_details, shopList.visit_distance)

                        /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 }
 else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }*/

                    } else if (shopList.status == NetworkConstant.SESSION_MISMATCH) {
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).clearData()
                        startActivity(
                            Intent(
                                mContext as DashboardActivity,
                                LoginActivity::class.java
                            )
                        )
                        (mContext as DashboardActivity).overridePendingTransition(0, 0)
                        (mContext as DashboardActivity).finish()
                    } else if (shopList.status == NetworkConstant.NO_DATA) {
                        progress_wheel.stopSpinning()

                        /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }*/

                    } else {
                        progress_wheel.stopSpinning()
                        /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }*/
                    }

                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()

                    /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 } else {
 if (!Pref.isSeenTermsConditions)
 showTermsConditionsPopup()
 }*/
                })
        )
    }


    private fun convertToModelAndSave(
        location_details: List<LocationData>?,
        visitDistance: String
    ) {
        if (location_details!!.isEmpty())
            return

        doAsync {
            for (i in 0 until location_details.size) {
                var localData = UserLocationDataEntity()
                if (location_details[i].latitude == null)
                    continue
                else
                    localData.latitude = location_details[i].latitude!!

                if (location_details[i].longitude == null)
                    continue
                else
                    localData.longitude = location_details[i].longitude!!

                if (location_details[i].date == null)
                    continue
                else {
                    localData.updateDate =
                        AppUtils.changeAttendanceDateFormatToCurrent(location_details[i].date!!)
                    localData.updateDateTime = location_details[i].date!!
                }
                if (location_details[i].last_update_time == null)
                    continue
                else {
                    val str = location_details[i].last_update_time
                    localData.time = str.split(" ")[0]
                    localData.meridiem = str.split(" ")[1]
                }
                localData.isUploaded = true
                localData.minutes = "0"
                localData.hour = "0"
                if (location_details[i].distance_covered == null)
                    continue
                else
                    localData.distance = location_details[i].distance_covered!!

                if (location_details[i].shops_covered == null)
                    continue
                else
                    localData.shops = location_details[i].shops_covered!!
                if (location_details[i].location_name == null)
                    continue
                else
                    localData.locationName = location_details[i].location_name!!

                if (location_details[i].date == null)
                    continue
                else
                    localData.timestamp = AppUtils.getTimeStampFromDate(location_details[i].date!!)

                if (location_details[i].meeting_attended == null)
                    continue
                else
                    localData.meeting = location_details[i].meeting_attended!!

                if (visitDistance == null)
                    continue
                else
                    localData.visit_distance = visitDistance

                if (location_details[i].network_status == null)
                    continue
                else
                    localData.network_status = location_details[i].network_status

                if (location_details[i].battery_percentage == null)
                    continue
                else
                    localData.battery_percentage = location_details[i].battery_percentage

                Timber.d("====================Current location (Dashboard)=====================")
                Timber.d("distance=====> " + localData.distance)
                Timber.d("lat====> " + localData.latitude)
                Timber.d("long=====> " + localData.longitude)
                Timber.d("location=====> " + localData.locationName)
                Timber.d("date time=====> " + localData.updateDateTime)
                Timber.d("meeting_attended=====> " + localData.meeting)
                Timber.d("visit_distance=====> " + localData.visit_distance)
                Timber.d("network_status=====> " + localData.network_status)
                Timber.d("battery_percentage=====> " + localData.battery_percentage)

                //negative distance handle Suman 06-02-2024 mantis id 0027225 begin
                try {
                    var distReftify = localData.distance.toDouble()
                    if (distReftify < 0) {
                        var locL = AppDatabase.getDBInstance()!!.userLocationDataDao()
                            .getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi()) as ArrayList<UserLocationDataEntity>
                        var lastLoc = locL.get(locL.size - 1)
                        var d = LocationWizard.getDistance(
                            localData.latitude.toDouble(),
                            localData.longitude.toDouble(),
                            lastLoc.latitude.toDouble(),
                            lastLoc.longitude.toDouble()
                        )
                        localData.distance = d.toString()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    localData.distance = "0.0"
                }
                //negative distance handle Suman 06-02-2024 mantis id 0027225 end

                AppDatabase.getDBInstance()!!.userLocationDataDao().insert(localData)

                Timber.d("=====================location added to db (Dashboard)======================")
            }

            uiThread {

                progress_wheel.stopSpinning()

                /*if (isTermsAndConditionsPopShow) {
 callTermsAndConditionsdApi()
 }*/
            }
        }
    }


    fun shouldFetchLocationActivity(): Boolean {
        Timber.d("Dash_Acti_loc ${AppDatabase.getDBInstance()!!.userLocationDataDao().all.size}")
        try {
            Timber.d(
                "Dash_Acti_loc ${
                    AppDatabase.getDBInstance()!!.userLocationDataDao().all.get(0).locationName
                }"
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Timber.d("Dash_Acti_loc ex")
        }
        return (AppDatabase.getDBInstance()!!.userLocationDataDao().all.size == 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun updateFence() {
        println("tag_service_call_check DashboardActivity inside updateFence inside")
        if (Pref.IsLeavePressed == true && Pref.IsLeaveGPSTrack == false) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*// Rev 25.0 DashboardACtivity v 4.2.6 stock optmization mantis 0027421 06-05-2024 Suman begin
 if (FTStorageUtils.isMyServiceRunning(LocationJobService::class.java, this)) {
 println("tag_service_call_check LocationJobService service running returning")
 }else{// Rev 25.0 DashboardACtivity v 4.2.6 stock optmization mantis 0027421 06-05-2024 Suman end
 LocationJobService.updateFence("UPDATE_FENCE")
 println("tag_service_call_check DashboardActivity inside updateFence LocationJobService starting")
 val componentName = ComponentName(this, LocationJobService::class.java)
 val jobInfo = JobInfo.Builder(12, componentName)
 //.setRequiresCharging(true)
 .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
 //.setRequiresDeviceIdle(true)
 .setOverrideDeadline(1000)
 .build()

 val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
 Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")
 val resultCode = jobScheduler.schedule(jobInfo)

 if (resultCode == JobScheduler.RESULT_SUCCESS) {
 Timber.d("==============================Job scheduled (Dashboard Activity)===============================")
 } else {
 Timber.d("===========================Job not scheduled (Dashboard Activity)==============================")
 }
 }*/


            LocationJobService.updateFence("UPDATE_FENCE")

            val componentName = ComponentName(this, LocationJobService::class.java)
            val jobInfo = JobInfo.Builder(12, componentName)
                //.setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                //.setRequiresDeviceIdle(true)
                .setOverrideDeadline(1000)
                .build()

            val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = jobScheduler.schedule(jobInfo)

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Timber.d("==============================Job scheduled (Dashboard Activity)===============================")
            } else {
                Timber.d("===========================Job not scheduled (Dashboard Activity)==============================")
            }
        } else {
            val myIntent = Intent(this, LocationFuzedService::class.java)
            Timber.d("TAG_CHECK_LOC_SERVICE_STATUS")
            val bundle = Bundle();
            bundle.putString("ACTION", "UPDATE_FENCE")
            myIntent.putExtras(bundle)
            startService(myIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
// return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public fun getShopDummyImageFile(): File {
        var bm: Bitmap? = null
        if (bm == null) {
            val bitmap = (iv_shopImage.drawable as BitmapDrawable).bitmap
            bm = bitmap
        }
        val bytes = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        var destination =
        //File(Environment.getExternalStorageDirectory(),
            //27-09-2021
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                System.currentTimeMillis().toString() + ".jpg"
            )
        val camera_image_path = destination?.absolutePath
        val fo: FileOutputStream
        try {
            destination?.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return destination
    }

    fun generateNoteOnSD(context: Context?, sFileName: String?, sBody: String?) {
        try {
            val root = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "xbreezefieldnationalplasticlogsample/log" + ".txt"
            )
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, sFileName)
            val writer = FileWriter(gpxfile)
            writer.append(sBody)
            writer.flush()
            writer.close()
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    public fun getDummyFile(): File {
// var bm: Bitmap? = null
// if (bm == null) {
// val bitmap = (iv_shopImage.drawable as BitmapDrawable).bitmap
// bm = bitmap
// }
        val bytes = ByteArrayOutputStream()
// bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        var destination = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "xbreezefieldnationalplasticlogsample/log"
        )
        val fo: FileOutputStream
        val fo1: FileWriter
        try {
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return destination
    }

// @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// // Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.menu_main, menu);
// return true;
// }
//
// @Override
// public boolean onOptionsItemSelected(MenuItem item) {
// // Handle toolbar item clicks here. It'll
// // automatically handle clicks on the Home/Up button, so long
// // as you specify a parent activity in AndroidManifest.xml.
// int id = item.getItemId();
//
// switch (id) {
// case R.id.action_search:
// // Open the search view on the menu item click.
//
// searchView.openSearch();
// return true;
// }
//
// return super.onOptionsItemSelected(item);
// }


    //Code by wasim
    private val mAlarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onFireAlarm(intent)
        }
    }

    private fun onFireAlarm(intent: Intent) {
        if (intent != null) {
            if (intent.getParcelableExtra<AlarmData>("ALARM_DATA") != null) {
                //isFromAlarm = true
                val alaramData = intent.getParcelableExtra<AlarmData>("ALARM_DATA")
                alarmCofifDataModel = AlarmConfigDataModel()
                alarmCofifDataModel.requestCode = alaramData!!.requestCode
                alarmCofifDataModel.id = alaramData!!.id
                alarmCofifDataModel.report_id = alaramData!!.report_id
                alarmCofifDataModel.report_title = alaramData!!.report_title
                alarmCofifDataModel.alarm_time_hours = alaramData!!.alarm_time_hours
                alarmCofifDataModel.alarm_time_mins = alaramData!!.alarm_time_mins

                navigateFragmentByReportId(alarmCofifDataModel, true)

            }
        }
    }

    private fun navigateFragmentByReportId(armData: AlarmConfigDataModel, isaddedToTask: Boolean) {
        //Report ID "1:attendance,2:Shop Visit,3:performance" Please change fragment name according to requirement
        selfieDialog?.dismiss()
        when (armData.report_id?.toInt()) {
            1 -> {
                isAttendanceFromAlarm = true
                loadFragment(FragType.AttendanceReportFragment, isaddedToTask, alarmCofifDataModel)
            }

            2 -> {
                isVisitFromAlarm = true
                loadFragment(FragType.VisitReportFragment, isaddedToTask, alarmCofifDataModel)
            }

            3 -> {
                isPerformanceFromAlarm = true
                isTodaysPerformance = true
                loadFragment(FragType.PerformanceReportFragment, isaddedToTask, alarmCofifDataModel)
            }

            4 -> {
                isPerformanceFromAlarm = true
                isTodaysPerformance = false
                loadFragment(FragType.PerformanceReportFragment, isaddedToTask, alarmCofifDataModel)
            }

            5 -> {

                try {
                    Handler().postDelayed(Runnable {

                        val currentFragment =
                            supportFragmentManager.findFragmentById(R.id.frame_layout_container)

                        if (currentFragment != null && currentFragment is DailyPlanListFragment) {
                            onBackPressed()
                        }
                    }, 200)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                Handler().postDelayed(Runnable {
                    isDailyPlanFromAlarm = true
                    loadFragment(FragType.DailyPlanListFragment, isaddedToTask, alarmCofifDataModel)
                }, 450)
            }

            6 -> {
                Timber.e("=================Show selfie dialog (DashboardActivity)=================")

                if (isFromAlarm) {
                    val currentFragment =
                        supportFragmentManager.findFragmentById(R.id.frame_layout_container)
                    Log.d("login_test_calling16", "")
                    if (currentFragment != null && currentFragment !is DashboardFragment)
                        loadFragment(FragType.DashboardFragment, isaddedToTask, DashboardType.Home)
                    else if (currentFragment == null)

                        loadFragment(FragType.DashboardFragment, isaddedToTask, DashboardType.Home)
                }

                Pref.isSefieAlarmed = true
                Pref.reportId = armData.report_id!!
                showSelfieDialog()
            }
        }

        //cancelNotification(armData.report_id!!)
    }

    private fun showSelfieDialog() {
        selfieDialog = SelfieDialog.getInstance({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                initCameraPermissionCheck()
            else {
                captureFrontImage()
            }
        }, true)
        selfieDialog?.show(supportFragmentManager, "")
    }

    private fun initCameraPermissionCheck() {

        //begin mantis id 26741 Storage permission updation Suman 22-08-2023
        var permissionList = arrayOf<String>(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        } else {
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
//end mantis id 26741 Storage permission updation Suman 22-08-2023

        permissionUtils = PermissionUtils(
            this,
            object : PermissionUtils.OnPermissionListener {
                override fun onPermissionGranted() {
                    captureFrontImage()
                }

                override fun onPermissionNotGranted() {
                    showSnackMessage(getString(R.string.accept_permission))
                }
// mantis id 26741 Storage permission updation Suman 22-08-2023
            },
            permissionList
        )// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun uploadSelfie(file: File) {
        if (!AppUtils.isOnline(this)) {
            Toaster.msgShort(this, getString(R.string.no_internet))
            return
        }

        selfieDialog?.dismiss()


        val repository = DashboardRepoProvider.provideDashboardImgRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.alarmWithSelfie(file.absolutePath, this, Pref.reportId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result_ ->
                    val response = result_ as BaseResponse
                    showSnackMessage(response.message!!)
                    progress_wheel.stopSpinning()

                    if (response.status == NetworkConstant.SUCCESS) {
                        Pref.isSefieAlarmed = false
                    } else {
                        showSelfieDialog()
                    }


                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    showSnackMessage(getString(R.string.something_went_wrong))
                    showSelfieDialog()
                })
        )
    }

    private val autoRevisit = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            try {

                if (getFragment() != null && getFragment() is DashboardFragment) {
                    (getFragment() as DashboardFragment).updateUi()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val offlineShopReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            try {
                if (getFragment() != null) {
                    if (getFragment() is OfflineAllShopListFragment)
                        (getFragment() as OfflineAllShopListFragment).updateUi()
                    else if (getFragment() is OfflineShopListFragment)
                        (getFragment() as OfflineShopListFragment).updateUi()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val idealLocReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            try {

                if (isForceLogout || !Pref.isSeenTermsConditions || !Pref.isAddAttendence || !Pref.isHomeLocAvailable)
                    return

                val startTime =
                    AppUtils.getMeredianTimeFromDateTime(intent.getStringExtra("startTime")!!)
                val endTime =
                    AppUtils.getMeredianTimeFromDateTime(intent.getStringExtra("endTime")!!)

                if (idealLocAlertDialog != null) {
                    idealLocAlertDialog?.dismissAllowingStateLoss()
                    idealLocAlertDialog = null
                }

                idealLocAlertDialog = CommonDialogSingleBtn.getInstance(
                    AppUtils.hiFirstNameText() + "!",
                    "It seeems that you are at the same nearby locations from $startTime to $endTime. Thanks.",
                    getString(R.string.ok),
                    object : OnDialogClickListener {
                        override fun onOkClick() {
                            if (isOrderDialogShow)
                                showOrderCollectionAlert(isOrderAdded, isCollectionAdded)
                        }
                    })//.show(supportFragmentManager, "CommonDialogSingleBtn")

                idealLocAlertDialog?.show(supportFragmentManager, "CommonDialogSingleBtn")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val attendNotiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (attendNotiAlertDialog != null) {
                    attendNotiAlertDialog?.dismissAllowingStateLoss()
                    attendNotiAlertDialog = null
                }

                var msg: String = intent.getStringExtra("data_msg").toString()
                if (msg == null || msg.length == 0)
                    return

                attendNotiAlertDialog = CommonDialogSingleBtn.getInstance(
                    AppUtils.hiFirstNameText(),
                    msg!!,
                    getString(R.string.ok),
                    object : OnDialogClickListener {
                        override fun onOkClick() {

                        }
                    })

                attendNotiAlertDialog?.show(supportFragmentManager, "CommonDialogSingleBtn")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val fcmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
        }
    }

    private val fcmReceiver_leave = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
        }
    }

    private val fcmReceiver_leave_status = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
        }
    }

    private val fcmReceiver_quotation_approval = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
            // 8.0 DashboardActivity AppV 4.0.6 Suman 23-01-2023 Auto mail from notification flow of quotation 25614
            Timber.d("quto_mail auto mail block receiver...")
            //Begin Rev 3.0 MyFirebaseMessagingService AppV 4.0.8 Suman 26/04/2023 mail repetation fix 25923
            //getQutoDtlsBeforePDF(CustomStatic.QutoNoFromNoti)
            if (Pref.prevQutoNoForMail.equals(CustomStatic.QutoNoFromNoti)) {
                return
            } else {
                getQutoDtlsBeforePDF(CustomStatic.QutoNoFromNoti)
            }
            //End of Rev 3.0 MyFirebaseMessagingService AppV 4.0.8 Suman 26/04/2023 mail repetation fix 25923
        }
    }


    private val fcm_ACTION_RECEIVER_LEAD = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logo.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake))
        }
    }

    // 8.0 DashboardActivity AppV 4.0.6 Suman 23-01-2023 Auto mail from notification flow of quotation 25614
    private fun getQutoDtlsBeforePDF(quto_no: String) {
        try {
            //Begin Rev 3.0 MyFirebaseMessagingService AppV 4.0.8 Suman 26/04/2023 mail repetation fix 25923
            Pref.prevQutoNoForMail = quto_no
            //End of Rev 3.0 MyFirebaseMessagingService AppV 4.0.8 Suman 26/04/2023 mail repetation fix 25923
            Timber.d("auto mail quto ${quto_no}")
            val repository = GetQuotRegProvider.provideSaveButton()
            BaseActivity.compositeDisposable.add(
                repository.viewDetailsQuot(quto_no!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val addQuotResult = result as ViewDetailsQuotResponse
                        var addQuotEditResult: ViewDetailsQuotResponse
                        addQuotEditResult = addQuotResult
                        Timber.d("quto_mail getQutoDtlsBeforePDF ${addQuotResult.status}")
                        if (addQuotResult!!.status == NetworkConstant.SUCCESS) {
                            // AutoMail Sended work update Auto mail in dashboardActivity
                            var pdfTemplateName = addQuotResult.sel_quotation_pdf_template!!
                            saveDataAsPdfN(addQuotEditResult, pdfTemplateName)
                            /*if(addQuotResult.sel_quotation_pdf_template!!.contains("General")){
 saveDataAsPdf(addQuotEditResult)
 }
 else{
 saveDataAsGovPdf(addQuotEditResult)
 }*/
// saveDataAsPdf(addQuotEditResult)
                        } else {
                            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        }
                        BaseActivity.isApiInitiated = false
                    }, { error ->
                        //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        BaseActivity.isApiInitiated = false
                        if (error != null) {
                        }
                        Timber.d("quto_mail getQutoDtlsBeforePDF err ${error.message}")
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }
    }
    // 8.0 DashboardActivity AppV 4.0.6 Suman 23-01-2023 Auto mail from notification flow of quotation 25614

    // 8.0 DashboardActivity AppV 4.0.6 Suman 23-01-2023 Auto mail from notification flow of quotation 25614
    // AutoMail Sended work update Auto mail in dashboardActivity

    @SuppressLint("UseRequireInsteadOfGet")
    private fun saveDataAsPdfN(
        addQuotEditResult: ViewDetailsQuotResponse,
        pdfTtemplateName: String
    ) {
        Timber.d("quto_mail auto mail block begin...")
        var document: Document = Document(PageSize.A4, 36f, 36f, 36f, 80f)
        val time = System.currentTimeMillis()
        //val fileName = "QUTO_" + "_" + time
        var fileName = addQuotEditResult.quotation_number!!.toUpperCase() + "_" + time
        fileName = fileName.replace("/", "_")
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/FSMApp/QUTO/"

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }


        try {
            //progress_wheel.spin()
            var pdfWriter: PdfWriter =
                PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            // 1.0 HeaderFooterPageEvent AppV 4.0.7 Suman 27/02/2023 footer image with text mantis 25705
            val event = HeaderFooterPageEvent(
                if (Pref.IsShowQuotationFooterforEurobond) {
                    "EURO PANEL PRODUCTS LIMITED"
                } else {
                    getString(R.string.app_name)
                },
                addQuotEditResult.salesman_name.toString(),
                addQuotEditResult.salesman_designation.toString(),
                addQuotEditResult.salesman_login_id.toString(),
                addQuotEditResult.salesman_email.toString()
            )
            pdfWriter.setPageEvent(event)

            //PdfWriter.getInstance(document, FileOutputStream(path + fileName + ".pdf"))
            document.open()

            var font: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
            var fontB1: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)
            var font1: Font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL)
            var font1Big: Font = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL)
            var font2Big: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL)
            var font2BigBold: Font = Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)
            var font1small: Font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL)
// val grayFront = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.GRAY)
            val grayFront = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)

            //image add
            val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.breezelogo)
            val bitmap = Bitmap.createScaledBitmap(bm, 200, 80, true);
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var img: Image? = null
            val byteArray: ByteArray = stream.toByteArray()
            try {
                img = Image.getInstance(byteArray)
                //img.scaleToFit(155f,90f)
                img.scalePercent(50f)
                img.alignment = Image.ALIGN_RIGHT
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //document.add(img)


            //var quotDate = AppUtils.getFormatedDateNew(addQuotEditResult.quotation_date_selection!!.replace("12:00:00 AM",""),"mm-dd-yyyy","dd-mm-yyyy")

            /*val dateLine = Paragraph("DATE: " + addQuotEditResult.quotation_date_selection!! +
 " " + addQuotEditResult.quotation_number, font)
 dateLine.alignment = Element.ALIGN_LEFT
 dateLine.spacingAfter = 5f
 document.add(dateLine)*/


            val para = Paragraph()
            val glue = Chunk(VerticalPositionMark())
            val ph1 = Phrase()
            val main = Paragraph()
            ph1.add(Chunk("DATE: " + addQuotEditResult.quotation_date_selection!!, font))
            ph1.add(glue) // Here I add special chunk to the same phrase.

            ph1.add(Chunk(addQuotEditResult.quotation_number + " ", font))
            para.add(ph1)
            document.add(para)

            val xxx = Paragraph("", font)
            xxx.spacingAfter = 5f
            document.add(xxx)

            val toLine = Paragraph("To,", font)
            toLine.alignment = Element.ALIGN_LEFT
            toLine.spacingAfter = 2f
            document.add(toLine)

            val cusName = Paragraph(addQuotEditResult.shop_name, font)
            cusName.alignment = Element.ALIGN_LEFT
            cusName.spacingAfter = 2f
            document.add(cusName)


            //// addr test begin
            var finalStr = ""
            try {
                var str = addQuotEditResult.shop_addr.toString().toCharArray()
                //var str = "1602, Marathon Icon,Opp. Peninsula Corporate Park, Off Ganpatrao Kadam Marg,Lower Parel (W),".toCharArray()
                //var str = "Chhatrapati Shivaji Terminus Main Post Office, Borabazar Precinct, Ballard Estate, Fort, Mumbai, Maharashtra 400001, India".toCharArray()
                finalStr = ""
                var isNw = false
                var comCnt = 0
                for (i in 0..str.size - 1) {
                    if (str[i].toString().equals(",")) {
                        comCnt++
                        finalStr = finalStr + ","
                        if (comCnt % 2 == 0) {
                            finalStr = finalStr + "\n"
                            isNw = true
                        }
                    } else {
                        if (isNw && str[i].toString().equals(" ")) {
                            isNw = false
                        } else {
                            finalStr = finalStr + str[i].toString()
                        }
                    }
                }
            } catch (ex: Exception) {
                finalStr = ""
            }


            //// addr test end

// val cusAddress = Paragraph(addQuotEditResult.shop_addr, font)
            val cusAddress = Paragraph(finalStr, font)
            cusAddress.alignment = Element.ALIGN_LEFT
            cusAddress.spacingAfter = 5f
            document.add(cusAddress)

// val cusemail = Paragraph("Email : " + addQuotEditResult.shop_email, font)
// cusemail.alignment = Element.ALIGN_LEFT
// cusemail.spacingAfter = 5f
// document.add(cusemail)

            val shopPincode = Paragraph("Pincode : " + addQuotEditResult.shop_address_pincode, font)
            shopPincode.alignment = Element.ALIGN_LEFT
            shopPincode.spacingAfter = 5f
            try {
                // 3.0 ViewAllQuotListFragment AppV 4.0.7 Suman 14/02/2023 pdf pincode dynamic and rate+qty+color position handle mantis 25670
                if (!finalStr.contains(addQuotEditResult.shop_address_pincode.toString()))
                    document.add(shopPincode)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }


            val projectName = Paragraph("Project Name : " + addQuotEditResult.project_name, font)
            projectName.alignment = Element.ALIGN_LEFT
            projectName.spacingAfter = 5f
            document.add(projectName)

            var emailCollectionStr = ""
            var nameCollectionStr = ""
            var numberCollectionStr = ""
            var finalNamePhStr = ""
            if (addQuotEditResult.extra_contact_list!!.size > 0) {
                for (i in 0..addQuotEditResult.extra_contact_list!!.size - 1) {
                    var ob = addQuotEditResult.extra_contact_list!!.get(i)
                    emailCollectionStr =
                        emailCollectionStr + if (ob.quotation_contact_email == null) "" else ob.quotation_contact_email + ","
                    nameCollectionStr = nameCollectionStr + ob.quotation_contact_person + ","
                    numberCollectionStr = numberCollectionStr + ob.quotation_contact_number + ","
                    finalNamePhStr =
                        finalNamePhStr + ob.quotation_contact_person + " (Mob.No. ${ob.quotation_contact_number} )/"
                }
                nameCollectionStr = nameCollectionStr.dropLast(1)
                emailCollectionStr = emailCollectionStr.dropLast(1)
                numberCollectionStr = numberCollectionStr.dropLast(1)
                finalNamePhStr = finalNamePhStr.dropLast(1)
            } else {
                emailCollectionStr =
                    if (addQuotEditResult.shop_email == null) "" else addQuotEditResult.shop_email!!
                nameCollectionStr = addQuotEditResult.shop_owner_name.toString()
                numberCollectionStr = addQuotEditResult.shop_phone_no.toString()
                finalNamePhStr = nameCollectionStr + " (Mob.No. $numberCollectionStr )"
            }

            val cusemail = Chunk("Email : " + emailCollectionStr, font)
            //cusemail.setUnderline(0.1f, -2f) //0.1 thick, -2 y-location
            document.add(cusemail)


            val xx = Paragraph("", font)
            xx.spacingAfter = 6f
            document.add(xx)


            //val cusowner = Paragraph("Kind Attn. " + addQuotEditResult.shop_owner_name +" "+ "(Mob.No. " + addQuotEditResult.shop_phone_no + ")", font)
            //val cusowner = Chunk("Kind Attn. " + "Mr./Mrs. "+addQuotEditResult.shop_owner_name + " " + "(Mob.No. " + addQuotEditResult.shop_phone_no + ")", font)
            val cusowner = Chunk("Kind Attn. " + "Mr./Mrs. " + finalNamePhStr, font)
            cusowner.setUnderline(0.1f, -2f) //0.1 thick, -2 y-location
            //cusowner.alignment = Element.ALIGN_LEFT
            //cusowner.spacingAfter = 5f
            document.add(cusowner)


            val x = Paragraph("", font)
            //cusemail.setUnderline(0.1f, -2f) //0.1 thick, -2 y-location
            x.spacingAfter = 6f
            document.add(x)

            // Hardcoded for EuroBond
            var sub = Chunk("", font)
            if (Pref.IsShowQuotationFooterforEurobond) {
                sub = Chunk("Sub :-Quotation For Eurobond-ALUMINIUM COMPOSITE PANEL", font)
            } else {
                sub = Chunk("Sub :-Quotation For " + getString(R.string.app_name), font)
            }
            sub.setUnderline(0.1f, -2f) //0.1 thick, -2 y-location
            //sub.alignment = Element.ALIGN_LEFT
            //sub.spacingAfter = 10f
            document.add(sub)

            val body = Paragraph(
                "\nSir,\n" +
                        "In reference to the discusssion held with you regarding the said subject,we are please to quote our most " +
                        "preferred rates & others terms and condition for the same as follows.\n",
                grayFront
            )
            body.alignment = Element.ALIGN_LEFT
            body.spacingAfter = 10f
            document.add(body)

            // table header
// val widths = floatArrayOf(0.07f, 0.40f,0.13f, 0.2f, 0.2f)
            val widths = floatArrayOf(0.07f, 0.44f, 0.13f, 0.18f, 0.18f)

            var tableHeader: PdfPTable = PdfPTable(widths)
            tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER)
            tableHeader.setWidthPercentage(100f)

            val cell1 = PdfPCell(Phrase("Sr.No", font1Big))
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(cell1);

            val cell2 = PdfPCell(Phrase("Description", font1Big))
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(cell2);

            val cell2_1 = PdfPCell(Phrase("Color Code/Series", font1Big))
            cell2_1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(cell2_1);

            val cell3 = PdfPCell(Phrase("Rate/Sq.Mtr (INR)", font1Big))
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(cell3);

            val cell4 = PdfPCell(Phrase("Rate/Sq.Ft (INR)", font1Big))
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(cell4);

            //tableHeader.addCell(PdfPCell(Phrase("Description", font1)))
            //tableHeader.addCell(PdfPCell(Phrase("Rate/Sq.Mtr (INR)", font1)))
            //tableHeader.addCell(PdfPCell(Phrase("Rate/Sq.Ft (INR)", font1)))

            //tableHeader.addCell("Sr. No.")
            //tableHeader.addCell("Description.")
            //tableHeader.addCell("Rate/Sq.Mtr (INR)")
            //tableHeader.addCell("Rate/Sq.Ft (INR)")
            document.add(tableHeader)

            //table body
            var srNo: String = ""
            var desc: String = ""
            var catagory: String = ""
            var colorCode: String = ""
            var rateSqFt: String = ""
            var rateSqMtr: String = ""
            var obj = addQuotEditResult.quotation_product_details_list
            for (i in 0..obj!!.size - 1) {
                srNo = (i + 1).toString()
                desc =
                    obj!!.get(i).product_name.toString() //+ "\n\n"+ "Color Code : "+obj.get(i).color_name
                colorCode = obj.get(i).color_name.toString()
// colorCode = "solid and metalic"
                rateSqFt = "INR - " + obj!!.get(i).rate_sqft.toString()
                rateSqMtr = "INR - " + obj!!.get(i).rate_sqmtr.toString()
                try {
                    catagory = AppDatabase.getDBInstance()?.productListDao()
                        ?.getSingleProduct(obj!!.get(i).product_id!!.toInt()!!)!!.category.toString()
                } catch (ex: Exception) {
                    catagory = ""
                }

                if (pdfTtemplateName.contains("General")) {
                    desc = desc + "\n" + catagory
                } else {
                    desc = desc + "\n" + obj!!.get(i).product_des.toString()
                }


                val tableRows = PdfPTable(widths)
                tableRows.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
                tableRows.setWidthPercentage(100f);

                var cellBodySl = PdfPCell(Phrase(srNo, font1small))
                cellBodySl.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableRows.addCell(cellBodySl)

                var cellBodyDesc = PdfPCell(Phrase(desc, font1small))
                cellBodyDesc.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableRows.addCell(cellBodyDesc)

                // 3.0 ViewAllQuotListFragment AppV 4.0.7 Suman 14/02/2023 pdf pincode dynamic and rate+qty+color position handle mantis 25670
                var cellBodyColor = PdfPCell(Phrase(colorCode, font1small))
                //cellBodyColor.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodyColor.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodyColor.verticalAlignment = Element.ALIGN_MIDDLE
                tableRows.addCell(cellBodyColor)

                // 3.0 ViewAllQuotListFragment AppV 4.0.7 Suman 14/02/2023 pdf pincode dynamic and rate+qty+color position handle mantis 25670
                var cellBodySqMtr = PdfPCell(Phrase(rateSqMtr, font1small))
                //cellBodySqMtr.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodySqMtr.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodySqMtr.verticalAlignment = Element.ALIGN_MIDDLE
                tableRows.addCell(cellBodySqMtr)

                // 3.0 ViewAllQuotListFragment AppV 4.0.7 Suman 14/02/2023 pdf pincode dynamic and rate+qty+color position handle mantis 25670
                var cellBodySqFt = PdfPCell(Phrase(rateSqFt, font1small))
                //cellBodySqFt.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodySqFt.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellBodySqFt.verticalAlignment = Element.ALIGN_MIDDLE
                tableRows.addCell(cellBodySqFt)


                document.add(tableRows)

                document.add(Paragraph())
            }


            val terms = Chunk("Terms & Conditions:-", font)
// terms.alignment = Element.ALIGN_LEFT
// terms.spacingAfter = 5f
            terms.setUnderline(0.1f, -2f) //0.1 thick, -2 y-location
            document.add(terms)


            val taxs = Paragraph("Taxes : " + addQuotEditResult.taxes, font2Big)
            taxs.alignment = Element.ALIGN_LEFT
            taxs.spacingAfter = 2f
            document.add(taxs)


            val freight = Paragraph("Freight : " + addQuotEditResult.Freight, font2Big)
            freight.alignment = Element.ALIGN_LEFT
            freight.spacingAfter = 2f
            document.add(freight)


            val delivery = Paragraph("Delivery Time : " + addQuotEditResult.delivery_time, font2Big)
            delivery.alignment = Element.ALIGN_LEFT
            delivery.spacingAfter = 2f
            document.add(delivery)


            val payment = Paragraph("Payment : " + addQuotEditResult.payment, font2Big)
            payment.alignment = Element.ALIGN_LEFT
            payment.spacingAfter = 2f
            document.add(payment)

            val validity = Paragraph("Validity : " + addQuotEditResult.validity, font2Big)
            validity.alignment = Element.ALIGN_LEFT
            validity.spacingAfter = 2f
            document.add(validity)

            val billing = Paragraph("Billing : " + addQuotEditResult.billing, font2Big)
            billing.alignment = Element.ALIGN_LEFT
            billing.spacingAfter = 2f
            document.add(billing)

            /* val product_tolerance_of_thickness = Paragraph("Product Tolerance of Thickness : " + addQuotEditResult.product_tolerance_of_thickness, font2Big)
 product_tolerance_of_thickness.alignment = Element.ALIGN_LEFT
 product_tolerance_of_thickness.spacingAfter = 2f
 document.add(product_tolerance_of_thickness)

 val product_tolerance_of_coating = Paragraph("Tolerance of Coating Thickness : " + addQuotEditResult.tolerance_of_coating_thickness, font2Big)
 product_tolerance_of_coating.alignment = Element.ALIGN_LEFT
 product_tolerance_of_coating.spacingAfter = 6f
 document.add(product_tolerance_of_coating)*/

            // rev 23.0 DashobaordActivity AppV 4.1.6 Saheli 19/06/2023 pdf remark field mantis 26139

            val remarks = Paragraph("Remarks : " + addQuotEditResult.Remarks, font2Big)
            remarks.alignment = Element.ALIGN_LEFT
            remarks.spacingAfter = 6f
            document.add(remarks)

            // end 23.0 rev mantis 26139 PDF remarks field added saheli v DashobaordActivity AppV 4.1.6 19-06-2023

            // Suman 06-06-2024 mantis id 0027513 begin
            val note =
                Paragraph("Note : Unloading of the material will in scope of Client.", font2BigBold)
            note.alignment = Element.ALIGN_LEFT
            note.spacingAfter = 6f
            document.add(note)
            // Suman 06-06-2024 mantis id 0027513 end


            val end = Paragraph(
                "Anticipating healthy business relation with your esteemed organization.",
                grayFront
            )
            end.alignment = Element.ALIGN_LEFT
            end.spacingAfter = 4f
            document.add(end)


            val thanks = Paragraph("\nThanks & Regards,", fontB1)
            thanks.alignment = Element.ALIGN_LEFT
            thanks.spacingAfter = 4f
            document.add(thanks)

            // Hardcoded for EuroBond
            var companyName = Paragraph()
            if (Pref.IsShowQuotationFooterforEurobond) {
                companyName = Paragraph("EURO PANEL PRODUCTS LIMITED", fontB1)
            } else {
                companyName = Paragraph(getString(R.string.app_name), fontB1)
            }
            companyName.alignment = Element.ALIGN_LEFT
            companyName.spacingAfter = 2f
            document.add(companyName)

            val salesmanName = Paragraph(addQuotEditResult.salesman_name, fontB1)
            salesmanName.alignment = Element.ALIGN_LEFT
            salesmanName.spacingAfter = 2f
            document.add(salesmanName)

            val salesmanDes = Paragraph(addQuotEditResult.salesman_designation, fontB1)
            salesmanDes.alignment = Element.ALIGN_LEFT
            salesmanDes.spacingAfter = 2f
            document.add(salesmanDes)

            //val salesmanphone = Paragraph(addQuotEditResult.salesman_phone_no, fontB1)
            val salesmanphone = Paragraph("Mob : " + addQuotEditResult.salesman_login_id, fontB1)
            salesmanphone.alignment = Element.ALIGN_LEFT
            salesmanphone.spacingAfter = 2f
            document.add(salesmanphone)

            val salesmanemail = Paragraph("Email : " + addQuotEditResult.salesman_email, fontB1)
            salesmanemail.alignment = Element.ALIGN_LEFT
            salesmanemail.spacingAfter = 1f
            document.add(salesmanemail)

            val xxxx = Paragraph("", font)
            xxxx.spacingAfter = 4f
            //document.add(xxxx)

            // Hardcoded for EuroBond
            var euroHead = Paragraph()
            if (Pref.IsShowQuotationFooterforEurobond) {
                euroHead = Paragraph("\nEURO PANEL PRODUCTS LIMITED", font)
            } else {
                euroHead = Paragraph("\n" + getString(R.string.app_name), font)
            }

            euroHead.alignment = Element.ALIGN_LEFT
            //document.add(euroHead)

            //strip_line//bar//ics
            //Hardcoded for EuroBond
            var bm1: Bitmap
            if (Pref.IsShowQuotationFooterforEurobond) {
                bm1 = BitmapFactory.decodeResource(resources, R.drawable.footer_icon_euro)
            } else {
                bm1 = BitmapFactory.decodeResource(resources, R.drawable.ics_image)
            }
// val bm1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bar)
            val bitmap1 = Bitmap.createScaledBitmap(bm1, 850, 120, true)
            val stream1 = ByteArrayOutputStream()
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
            var img1: Image? = null
            val byteArray1: ByteArray = stream1.toByteArray()
            try {
                img1 = Image.getInstance(byteArray1)
                img1.alignment = Image.ALIGN_LEFT
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
// document.add(img1)

            val bm2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bar)
            val bitmap2 = Bitmap.createScaledBitmap(bm2, 50, 50, true)
            val stream2 = ByteArrayOutputStream()
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            var img2: Image? = null
            val byteArray2: ByteArray = stream2.toByteArray()
            try {
                img2 = Image.getInstance(byteArray2)
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
// document.add(img2)


            val companydel = Paragraph(
                "Regd.Off: 702,Aravali Business Centre,Ramadas Sutrale Road,Borivali(West),Mumbai-400 092." +
                        "Factory: Survey No.124/4,Manekpur,Sanjan,Khattalwada,Taluka- Umbergaon,Dist.Valsad,Gujarat - 396120" +
                        "T: +91-22-29686500(30 lines) +91-7666625999 - E: sale@eurobondacp.com + W: www.eurobondacp.com + CIN: U28931MH2013PTC251176" +
                        "", font1
            )
            companydel.alignment = Element.ALIGN_RIGHT
            companydel.spacingAfter = 10f
            //document.add(img1)
            //document.add(img2)
            //img2!!.alignment=Image.ALIGN_CENTER
            //document.add(companydel)


            val tablee = PdfPTable(1)
            tablee.widthPercentage = 100f
            var cell = PdfPCell()
            var p = Paragraph()
            p.alignment = Element.ALIGN_LEFT
            img1!!.scalePercent(50f)
            p.add(Chunk(img1, 0f, 0f, true))
            //p.add(Chunk(img2, 0f, 0f, true))
            //p.add(companydel)
            cell.addElement(p)
            cell.backgroundColor = BaseColor(0, 0, 0, 0)
            cell.borderColor = BaseColor(0, 0, 0, 0)

            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT)
            tablee.addCell(cell)
            //document.add(tablee)


            val bm3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.strip_line)
            val bitmap3 = Bitmap.createScaledBitmap(bm3, 520, 20, true)
            val stream3 = ByteArrayOutputStream()
            bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3)
            var img3: Image? = null
            val byteArray3: ByteArray = stream3.toByteArray()
            try {
                img3 = Image.getInstance(byteArray3)
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //document.add(img3)


            document.close()


            var sendingPath = path + fileName + ".pdf"
            /*if (!TextUtils.isEmpty(sendingPath)) {
 try {
 val shareIntent = Intent(Intent.ACTION_SEND)
 shareIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
 shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("saheli.bhattacharjee@indusnet.co.in","suman.bachar@indusnet.co.in"))
// shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("sales1@eurobondacp.com","sales@eurobondacp.com"))
 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quotation for $shop_name created on dated ${addQuotEditResult.save_date_time}.")
 shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello Team, \n Please find attached Quotation No. ${addQuotEditResult.quotation_number} Dated ${addQuotEditResult.save_date_time} " +
 " for $shop_name \n\n\n" +
 "Regards \n${Pref.user_name}. ")
 shareIntent.type = "message/rfc822"
 val fileUrl = Uri.parse(sendingPath)
 val file = File(fileUrl.path)
 val uri: Uri = FileProvider.getUriForFile(mContext, context!!.applicationContext.packageName.toString() + ".provider", file)
// shareIntent.type = "image/png"
 shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
 startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
 } catch (e: Exception) {
 e.printStackTrace()
 (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
 }
 }*/

            /*if (!TextUtils.isEmpty(sendingPath)) {
 try {
 val shareIntent = Intent(Intent.ACTION_SEND)
 val fileUrl = Uri.parse(sendingPath)
 val file = File(fileUrl.path)
 val uri: Uri = FileProvider.getUriForFile(mContext, context!!.applicationContext.packageName.toString() + ".provider", file)
 shareIntent.type = "image/png"
 shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
 startActivity(Intent.createChooser(shareIntent, "Share pdf using"))
 } catch (e: Exception) {
 e.printStackTrace()
 (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
 }
 }*/
            // Hardcoded for EuroBon

            var m = Mail()
            var toArr = arrayOf("")


            doAsync {
                try {
                    if (!Pref.automail_sending_email.equals("") && !Pref.automail_sending_pass.equals(
                            ""
                        ) && !Pref.recipient_email_ids.equals("")
                    ) {
                        var emailRecpL = Pref.recipient_email_ids.split(",")
                        m = Mail(Pref.automail_sending_email, Pref.automail_sending_pass)
                        toArr = Array<String>(emailRecpL.size) { "" }
                        for (i in 0..emailRecpL.size - 1) {
                            toArr[i] = emailRecpL[i]
                        }

                        m.setTo(toArr)
                        m.setFrom("TEAM");
                        m.setSubject(
                            "Quotation for ${addQuotEditResult.shop_name} created on dated ${
                                addQuotEditResult.save_date_time!!.split(
                                    " "
                                ).get(0)
                            }."
                        )
                        m.setBody(
                            "Hello Team, \n Please find attached Quotation No. ${addQuotEditResult.quotation_number} Dated ${
                                addQuotEditResult.save_date_time!!.split(
                                    " "
                                ).get(0)
                            } for ${addQuotEditResult.shop_name} \n\n\n Regards \n${Pref.user_name}."
                        )
                        //m.send()
                        val fileUrl = Uri.parse(sendingPath)
                        m.send(fileUrl.path)
                    }


                    Timber.d("quto_mail auto mail success")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Timber.d("quto_mail auto mail error ${ex.localizedMessage}")
                }
                uiThread {

                }
            }
        } catch (ex: Exception) {
            //progress_wheel.stopSpinning()
            ex.printStackTrace()
            //Toaster.msgShort(mContext, ex.message.toString())
            //(mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }


    }


    private val fcmClearDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isClearData = true


            Log.e(
                "DashboardActivity",
                "==============Open Logout from broadcast(Dashboard Activity)================"
            )

            Handler().postDelayed(Runnable {
                if (getFragment() != null && getFragment() !is LogoutSyncFragment) {
                    if (AppUtils.isOnline(this@DashboardActivity))
                        loadFragment(FragType.LogoutSyncFragment, true, "")
                    else
                        showSnackMessage(getString(R.string.no_internet))
                }
            }, 500)
        }
    }

    private val collectionAlertReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (Pref.isAttendanceFeatureOnly)
                return

            isOrderAdded = intent.getBooleanExtra("isOrderAdded", false)
            isCollectionAdded = intent.getBooleanExtra("isCollectionAdded", false)

            isOrderDialogShow = true

            if (idealLocAlertDialog == null && Pref.isSeenTermsConditions && Pref.isAddAttendence && Pref.isHomeLocAvailable && forceLogoutDialog == null)
                showOrderCollectionAlert(isOrderAdded, isCollectionAdded)
        }
    }

    private val forceLogoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showForceLogoutPopup()
        }
    }

    private val chatReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("Dashboard", "==================Chat Broadcast===============")
            AppUtils.isBroadCastRecv = true
            try {
                val notification = NotificationUtils(getString(R.string.app_name), "", "", "")
                if (getFragment() != null && getFragment() is ChatListFragment) {
                    val chatUser = intent.getSerializableExtra("chatUser") as ChatUserDataModel
                    if ((getFragment() as ChatListFragment).toID != chatUser.id) {
                        notification.msgNotification(
                            this@DashboardActivity,
                            intent.getStringExtra("body") as String,
                            intent.getSerializableExtra("chatData") as ChatListDataModel,
                            chatUser
                        )
                    } else
                        (getFragment() as ChatListFragment).updateUi(intent)

                } else
                    notification.msgNotification(
                        this@DashboardActivity,
                        intent.getStringExtra("body") as String,
                        intent.getSerializableExtra("chatData") as ChatListDataModel,
                        intent.getSerializableExtra("chatUser") as ChatUserDataModel
                    )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val updateStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (getFragment() != null && getFragment() is ChatListFragment) {
                    (getFragment() as ChatListFragment).updateStatus()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val localeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (getFragment() != null && getFragment() is ChatBotFragment)
                    AppUtils.changeLanguage(
                        this@DashboardActivity,
                        (getFragment() as ChatBotFragment).language
                    )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val homeLocReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                showHomeLocReasonDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val revisitReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                showRevisitReasonDialog(0, null, "", "", null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val updatePJP = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (getCurrentFragType() == FragType.DashboardFragment) {
                    (getFragment() as DashboardFragment).initBottomAdapter()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showHomeLocReasonDialog() {
        reasonDialog = null
        reasonDialog = ReasonDialog.getInstance(
            AppUtils.hiFirstNameText() + "!",
            "You have been detected nearby home location",
            reason
        ) {
            if (!AppUtils.isOnline(this))
                Toaster.msgShort(this, getString(R.string.no_internet))
            else {
                reasonDialog?.dismiss()
                submitHomeLocReason(it)
            }
        }
        reasonDialog?.show(supportFragmentManager, "")
    }

    private fun submitHomeLocReason(mReason: String) {
        progress_wheel.spin()
        val repository = DashboardRepoProvider.provideDashboardRepository()
        BaseActivity.compositeDisposable.add(
            repository.submitHomeLocReason(mReason)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        Pref.isShowHomeLocReason = false
                        reason = ""
                        AppUtils.changeLanguage(this, "en")
                        Pref.homeLocEndTimeStamp = System.currentTimeMillis().toString()

                        if (getFragment() != null && getFragment() is ChatBotFragment)
                            AppUtils.changeLanguage(
                                this,
                                (getFragment() as ChatBotFragment).language
                            )
                    } else {
                        reason = mReason
                        showHomeLocReasonDialog()
                        Toaster.msgShort(this, result.message!!)
                    }

                }, { error ->
                    error.printStackTrace()
                    progress_wheel.stopSpinning()
                    reason = mReason
                    showHomeLocReasonDialog()
                    Toaster.msgShort(this, getString(R.string.something_went_wrong))
                })
        )
    }


    override fun showForceLogoutPopup() {

        if (Pref.isAttendanceFeatureOnly)
            return

        Handler().postDelayed(Runnable {

            if (getFragment() != null && getFragment() !is LogoutSyncFragment && !Pref.isAutoLogout) {

                if (orderCollectionAlertDialog != null) {
                    orderCollectionAlertDialog?.dismissAllowingStateLoss()
                    orderCollectionAlertDialog = null
                }

                if (idealLocAlertDialog != null) {
                    idealLocAlertDialog?.dismissAllowingStateLoss()
                    idealLocAlertDialog = null
                }

                if (forceLogoutDialog != null) {
                    forceLogoutDialog?.dismissAllowingStateLoss()
                    forceLogoutDialog = null
                }

                forceLogoutDialog =
                    CommonDialogSingleBtn.getInstance(AppUtils.hiFirstNameText() + "!",
                        "Final logout time of the day is ${Pref.approvedOutTime}. Click on Ok to " +
                                "logout now & complete the automated data sync. Thanks.",
                        getString(R.string.ok),
                        object : OnDialogClickListener {

                            override fun onOkClick() {
                                if (AppUtils.isOnline(this@DashboardActivity)) {
                                    isForceLogout = true
                                    loadFragment(FragType.LogoutSyncFragment, true, "")
                                } else
                                    Toaster.msgShort(
                                        this@DashboardActivity,
                                        getString(R.string.no_internet)
                                    )
                            }
                        })//.show(supportFragmentManager, "CommonDialogSingleBtn")
                forceLogoutDialog?.show(supportFragmentManager, "CommonDialogSingleBtn")
            }

        }, 200)
    }


    /* override fun HBRecorderOnError(errorCode: Int, reason: String?) {

 }

 override fun HBRecorderOnStart() {

 }

 override fun HBRecorderOnComplete() {

 *//* val frag: DashboardFragment? = supportFragmentManager.findFragmentByTag("DashboardFragment") as DashboardFragment?
 frag!!.timerRecord(false)*//*

 var intent = Intent(mContext, ScreenRecService::class.java)
 intent.action = CustomConstants.STOP_Screen_SERVICE
 mContext.stopService(intent)

 //DashboardFragment.timerRecord(false)

 screen_record_info_TV.text="Start Screen Recorder"
 val path = hbRecorder!!.filePath
 val fileUrl = Uri.parse(path)
 val file = File(fileUrl.path)
 val uri = Uri.fromFile(file)
 val shareIntent = Intent(Intent.ACTION_SEND)
 shareIntent.type = "video/mp4"
 shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
 startActivity(Intent.createChooser(shareIntent, "Share video using"));
 }

 private fun startRecordingScreen() {

 if(hbRecorder == null){
 hbRecorder = HBRecorder(this, this)
 }

 //DashboardFragment.cancelTimer()
 //DashboardFragment.timerRecord(true)

 hbRecorder!!.enableCustomSettings()
 hbRecorder!!.setOutputFormat("MPEG_4")
 hbRecorder!!.isAudioEnabled(false)
 hbRecorder!!.recordHDVideo(false)
 hbRecorder!!.setVideoFrameRate(20)
 hbRecorder!!.setVideoBitrate(1000000)


 val mediaProjectionManager =
 getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
 val permissionIntent =
 mediaProjectionManager?.createScreenCaptureIntent()
 startActivityForResult(permissionIntent, 271)
 }*/


    fun updateScreenRecStatus() {
        Log.e("xcv", "updateScreenRecStatus")
        //code start Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

        /* if(DashboardFragment.isRecordRootVisible){
 screen_record_info_TV.text="Stop Recording"
 }else{
 screen_record_info_TV.text="Screen Recorder"
 }*/
        //code end Mantis- 27419 by puja screen recorder off 07.05.2024 v4.2.7

    }

    private fun apiCallOnClearAttenReject() { // clearing leave if isOnLeave is true
        var req: clearAttendanceonRejectReqModelRejectReqModel =
            clearAttendanceonRejectReqModelRejectReqModel()
        req.user_id = Pref.user_id
        req.leave_apply_date = AppUtils.getCurrentDateForShopActi()
        req.isOnLeave = true
        req.IsLeaveDelete = "1"

        val repository = LeaveTypeRepoProvider.leaveTypeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.clearAttendanceonRejectclick(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        apiCallOnClearAttenReject1()
                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage("ERROR")
                })
        )
    }

    private fun apiCallOnClearAttenReject1() { // clearing attendance if isOnLeave is false
        var req: clearAttendanceonRejectReqModelRejectReqModel =
            clearAttendanceonRejectReqModelRejectReqModel()
        req.user_id = Pref.user_id
        req.leave_apply_date = AppUtils.getCurrentDateForShopActi()
        req.isOnLeave = false
        req.IsLeaveDelete = "1"

        val repository = LeaveTypeRepoProvider.leaveTypeListRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
            repository.clearAttendanceonRejectclick(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    val response = result as BaseResponse
                    progress_wheel.stopSpinning()
                    if (response.status == NetworkConstant.SUCCESS) {
                        Pref.isAddAttendence = false
                        getSupervisorIDInfo()
                    }
                }, { error ->
                    progress_wheel.stopSpinning()
                    (mContext as DashboardActivity).showSnackMessage("ERROR")
                })
        )
    }

    private fun getSupervisorIDInfo() {
        try {
            progress_wheel.spin()
            val repository = AddAttendenceRepoProvider.addAttendenceRepo()
            BaseActivity.compositeDisposable.add(
                repository.getReportToUserID(Pref.user_id.toString(), Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as GetReportToResponse
                        progress_wheel.stopSpinning()
                        if (response.status == NetworkConstant.SUCCESS) {
                            getSupervisorFCMInfo(response.report_to_user_id!!)
                        }

                    }, { error ->
                        Timber.d("Apply Leave Response ERROR=========> " + error.message)
                        BaseActivity.isApiInitiated = false
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.stopSpinning()
        }

    }

    private fun getSupervisorFCMInfo(usrID: String) {
        try {
            progress_wheel.spin()
            val repository = AddAttendenceRepoProvider.addAttendenceRepo()
            BaseActivity.compositeDisposable.add(
                repository.getReportToFCMInfo(usrID, Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as GetReportToFCMResponse
                        progress_wheel.stopSpinning()
                        if (response.status == NetworkConstant.SUCCESS) {
                            sendFCMNotiSupervisor(response.device_token!!)
                        }
                    }, { error ->
                        Timber.d("Apply Leave Response ERROR=========> " + error.message)
                        BaseActivity.isApiInitiated = false
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel.stopSpinning()
        }

    }

    private fun sendFCMNotiSupervisor(superVisor_fcmToken: String) {
        if (superVisor_fcmToken != "") {
            try {
                progress_wheel.spin()
                val jsonObject = JSONObject()
                val notificationBody = JSONObject()
                notificationBody.put("body", "Attendance/Leave cleared by : " + Pref.user_name!!)
                notificationBody.put("flag", "flag_att_leave_clear")
                jsonObject.put("data", notificationBody)
                val jsonArray = JSONArray()
                jsonArray.put(0, superVisor_fcmToken)
                jsonObject.put("registration_ids", jsonArray)
                progress_wheel.stopSpinning()
                sendCustomNotificationForAttLeavClear(jsonObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                progress_wheel.stopSpinning()
            }
        }

    }

    fun sendCustomNotificationForAttLeavClear(notification: JSONObject) {
        progress_wheel.spin()
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notification,
                object : Response.Listener<JSONObject?> {
                    override fun onResponse(response: JSONObject?) {
                        progress_wheel.stopSpinning()
                        AttendClearMsg()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        progress_wheel.stopSpinning()
                        AttendClearMsg()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        Pref.firebase_k.toString()//getString(R.string.firebase_key)
                    params["Content-Type"] = "application/json"
                    return params
                }
            }

        MySingleton.getInstance(mContext)!!.addToRequestQueue(jsonObjectRequest)
    }

    fun AttendClearMsg() {
        drawerLayout.closeDrawers()
        Pref.isAddAttendence = false
        val simpleDialog = Dialog(mContext)
        simpleDialog.setCancelable(false)
        simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        simpleDialog.setContentView(R.layout.dialog_message)
        val dialogHeader =
            simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
        val dialog_yes_no_headerTV =
            simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
        dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText() + "!"
        //code start Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
        //dialogHeader.text = "Attendance/Leave cleared for today."
        dialogHeader.text =
            "You have successfully cleared your attendance/leave for the day.\nRe-login into the application to continue."
        //code start Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
        val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
        //code end Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
        dialogYes.text = "Logout"
        //code end Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
        dialogYes.setOnClickListener({ view ->
            simpleDialog.cancel()
            //code start Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
            if (AppUtils.isOnline(this)) {
                loadFragment(FragType.LogoutSyncFragment, false, "")
            } else {
                showSnackMessage(getString(R.string.no_internet))
            }
            //code end Mantis- 27446 by puja for clear attendance functionality 17.05.2024 v4.2.8
        })
        simpleDialog.show()
    }


    private fun syncGpsNetData() {
        val unSyncData = AppDatabase.getDBInstance()?.newGpsStatusDao()?.getNotUploaded(false)
        if (unSyncData == null || unSyncData.isEmpty()) {
            rectifyUnknownLoc()
        } else {
            progress_wheel.spin()
            val gps_net_status_list = ArrayList<NewGpsStatusEntity>()
            unSyncData.forEach {
                var obj: NewGpsStatusEntity = NewGpsStatusEntity()
                obj.apply {
                    id = it.id
                    date_time = it.date_time
                    gps_service_status = it.gps_service_status
                    network_status = it.network_status
                }
                gps_net_status_list.add(obj)
            }

            var sendObj: GpsNetInputModel = GpsNetInputModel()
            sendObj.user_id = Pref.user_id!!
            sendObj.session_token = Pref.session_token!!
            sendObj.gps_net_status_list = gps_net_status_list

            val repository = LocationRepoProvider.provideLocationRepository()
            compositeDisposable.add(
                repository.gpsNetInfo(sendObj)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as BaseResponse
                        progress_wheel.stopSpinning()
                        if (response.status == NetworkConstant.SUCCESS) {
                            doAsync {
                                unSyncData.forEach {
                                    AppDatabase.getDBInstance()?.newGpsStatusDao()
                                        ?.updateIsUploadedAccordingToId(true, it.id)
                                }
                                uiThread {
                                    rectifyUnknownLoc()
                                }
                            }
                        } else {
                            rectifyUnknownLoc()
                        }
                    }, { error ->
                        if (error == null) {
                            Timber.d("App Info : ERROR : " + "UNEXPECTED ERROR IN LOCATION ACTIVITY API")
                        } else {
                            Timber.d("App Info : ERROR : " + error.localizedMessage)
                            error.printStackTrace()
                        }
                        progress_wheel.stopSpinning()
                        rectifyUnknownLoc()
                    })
            )
        }

    }

    fun rectifyUnknownLoc() {
        try {
            doAsync {

                var unknownList = AppDatabase.getDBInstance()!!.userLocationDataDao()
                    .getUnknownLocation(AppUtils.getCurrentDateForShopActi(), "Unknown", false)
                if (unknownList.size > 0) {
                    for (i in 0..unknownList.size - 1) {
                        var updatedLoc = LocationWizard.getLocationName(
                            this@DashboardActivity,
                            unknownList.get(i).latitude.toDouble(),
                            unknownList.get(i).longitude.toDouble()
                        )
                        if (!updatedLoc.equals("Unknown")) {
                            AppDatabase.getDBInstance()!!.userLocationDataDao()
                                .updateUnknownLocation(
                                    unknownList.get(i).locationId.toString(),
                                    updatedLoc
                                )
                        }
                    }
                }
                uiThread {
                    //callShopDurationApi()
                    checkManualRevisitEnd()
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            //callShopDurationApi()
            checkManualRevisitEnd()
        }
    }

    fun checkManualRevisitEnd() {
        if (Pref.IsmanualInOutTimeRequired) {
            var objL = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getDurationCalculatedVisitedShopForADay(
                    AppUtils.getCurrentDateForShopActi(),
                    false
                )
            if (objL.size > 0) {
                val simpleDialog = Dialog(mContext)
                simpleDialog.setCancelable(false)
                simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                simpleDialog.setContentView(R.layout.dialog_ok)

                try {
                    simpleDialog.setCancelable(true)
                    simpleDialog.setCanceledOnTouchOutside(false)
                    val dialogName =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                    val dialogCross =
                        simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                    dialogName.text = AppUtils.hiFirstNameText()
                    dialogCross.setOnClickListener {
                        simpleDialog.cancel()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val dialogHeader =
                    simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
                dialogHeader.text = "Shop out location is pending."
                val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
                dialogYes.setOnClickListener({ view ->
                    simpleDialog.cancel()
                    loadFragment(FragType.PendingOutLocationFrag, false, "")
                })
                simpleDialog.show()
            } else {

                dialogHeaderProcess.text = "Syncing Important Data. Please wait..."
                val dialogYes =
                    simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView
                // code start by puja 05.04.2024 mantis id - 27333 v4.2.6
                //simpleDialogProcess.show()
                if (!(mContext as Activity).isFinishing) {
                    //show dialog
                    simpleDialogProcess.show()
                }
                // code end by puja 05.04.2024 mantis id - 27333 v4.2.6
                callShopDurationApi()
            }
        } else {
            dialogHeaderProcess.text = "Syncing Important Data. Please wait..."
            val dialogYes =
                simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView
            // code start by puja 05.04.2024 mantis id - 27333 v4.2.6
            //simpleDialogProcess.show()
            if (!(mContext as Activity).isFinishing) {
                //show dialog
                simpleDialogProcess.show()
            }
            // code end by puja 05.04.2024 mantis id - 27333 v4.2.6
            callShopDurationApi()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun callShopDurationApi() {
        //dialogHeaderProcess.text = "Syncing Important Data. Please wait..."
        //val dialogYes = simpleDialogProcess.findViewById(R.id.tv_message_ok) as AppCustomTextView
        //simpleDialogProcess.show()

        // Mantis 25675 duartion calculation issue(multiple visit last data calculation)
        if (Pref.isMultipleVisitEnable) {
            val shopActvityListDurationNotcal = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getShopForDayISDurationcal(AppUtils.getCurrentDateForShopActi(), false, false)
            if (shopActvityListDurationNotcal != null) {
                for (s in 0 until shopActvityListDurationNotcal.size) {
                    val endTimeStamp = System.currentTimeMillis().toString()
                    val totalMinute = AppUtils.getMinuteFromTimeStamp(
                        shopActvityListDurationNotcal.get(s).startTimeStamp,
                        endTimeStamp
                    )
                    val duration = AppUtils.getTimeFromTimeSpan(
                        shopActvityListDurationNotcal.get(s).startTimeStamp,
                        endTimeStamp
                    )
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(
                        shopActvityListDurationNotcal.get(s).shopid.toString(),
                        duration,
                        AppUtils.getCurrentDateForShopActi(),
                        shopActvityListDurationNotcal.get(s).startTimeStamp
                    )
                }
            }
        }
        // mantis 25675 end


        var shopId = ""
        var previousShopVisitDateNumber = 0L
        var shopVisitDate = ""
        var i = 0

        if (Pref.user_id.isNullOrEmpty() || BaseActivity.isShopActivityUpdating) {
            simpleDialogProcess.dismiss()
            (mContext as DashboardActivity).loadFragment(FragType.LogoutSyncFragment, false, "")
        } else {
            val syncedShopList =
                AppDatabase.getDBInstance()!!.addShopEntryDao().getUnSyncedShops(true)
            if (syncedShopList.isEmpty()) {
                simpleDialogProcess.dismiss()
                (mContext as DashboardActivity).loadFragment(FragType.LogoutSyncFragment, false, "")
            } else {
                BaseActivity.isShopActivityUpdating = true

                val shopDataList: MutableList<ShopDurationRequestData> = ArrayList()
                val syncedShop = ArrayList<ShopActivityEntity>()

                val revisitStatusList: MutableList<ShopRevisitStatusRequestData> = ArrayList()

                doAsync {

                    for (k in 0 until syncedShopList.size) {

                        if (!Pref.isMultipleVisitEnable) {
                            /* Get shop activity that has completed time duration calculation*/
                            val shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao()
                                .durationAvailableForShop(syncedShopList[k].shop_id, true, false)

                            if (shopActivity == null) {
                                val shop_activity = AppDatabase.getDBInstance()!!.shopActivityDao()
                                    .durationAvailableForTodayShop(
                                        syncedShopList[k].shop_id, true, true,
                                        AppUtils.getCurrentDateForShopActi()
                                    )
                                if (shop_activity != null)
                                    syncedShop.add(shop_activity)

                            } else {
                                val shopDurationData = ShopDurationRequestData()
                                shopDurationData.shop_id = shopActivity.shopid
                                shopDurationData.spent_duration = shopActivity.duration_spent
                                shopDurationData.visited_date = shopActivity.visited_date
                                shopDurationData.visited_time = shopActivity.visited_date
                                if (AppDatabase.getDBInstance()!!.addShopEntryDao()
                                        .getShopByIdN(shopActivity.shopid) != null
                                )
                                    shopDurationData.total_visit_count =
                                        AppDatabase.getDBInstance()!!.addShopEntryDao()
                                            .getShopByIdN(shopActivity.shopid).totalVisitCount
                                else
                                    shopDurationData.total_visit_count = "1"

                                if (TextUtils.isEmpty(shopActivity.distance_travelled))
                                    shopActivity.distance_travelled = "0.0"
                                shopDurationData.distance_travelled =
                                    shopActivity.distance_travelled

                                val currentShopVisitDateNumber =
                                    AppUtils.getTimeStampFromDateOnly(shopActivity.date!!)

                                if (shopId == shopActivity.shopid && previousShopVisitDateNumber == currentShopVisitDateNumber)
                                    continue

                                shopId = shopActivity.shopid!!
                                shopVisitDate = shopActivity.date!!
                                previousShopVisitDateNumber = currentShopVisitDateNumber

                                if (!TextUtils.isEmpty(shopActivity.feedback))
                                    shopDurationData.feedback = shopActivity.feedback
                                else
                                    shopDurationData.feedback = ""

                                shopDurationData.isFirstShopVisited =
                                    shopActivity.isFirstShopVisited
                                shopDurationData.distanceFromHomeLoc =
                                    shopActivity.distance_from_home_loc

                                shopDurationData.next_visit_date = shopActivity.next_visit_date

                                if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                                    shopDurationData.early_revisit_reason =
                                        shopActivity.early_revisit_reason
                                else
                                    shopDurationData.early_revisit_reason = ""

                                shopDurationData.device_model = shopActivity.device_model
                                shopDurationData.android_version = shopActivity.android_version
                                shopDurationData.battery = shopActivity.battery
                                shopDurationData.net_status = shopActivity.net_status
                                shopDurationData.net_type = shopActivity.net_type
                                shopDurationData.in_time = shopActivity.in_time
                                shopDurationData.out_time = shopActivity.out_time
                                shopDurationData.start_timestamp = shopActivity.startTimeStamp
                                shopDurationData.in_location = shopActivity.in_loc
                                shopDurationData.out_location = shopActivity.out_loc
                                shopDurationData.shop_revisit_uniqKey =
                                    shopActivity.shop_revisit_uniqKey!!

                                shopDurationData.updated_by = Pref.user_id
                                try {
                                    shopDurationData.updated_on = shopActivity.updated_on!!
                                } catch (ex: Exception) {
                                    shopDurationData.updated_on = ""
                                }

                                if (!TextUtils.isEmpty(shopActivity.pros_id) && shopActivity.pros_id != null)
                                    shopDurationData.pros_id = shopActivity.pros_id!!
                                else
                                    shopDurationData.pros_id = ""


                                if (!TextUtils.isEmpty(shopActivity.agency_name) && shopActivity.agency_name != null)
                                    shopDurationData.agency_name = shopActivity.agency_name!!
                                else
                                    shopDurationData.agency_name = ""

                                if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value) && shopActivity.approximate_1st_billing_value != null)
                                    shopDurationData.approximate_1st_billing_value =
                                        shopActivity.approximate_1st_billing_value!!
                                else
                                    shopDurationData.approximate_1st_billing_value = ""

                                //duration garbage fix
                                try {
                                    if (shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8) {
                                        shopDurationData.spent_duration = "00:00:10"
                                    }
                                } catch (ex: Exception) {
                                    shopDurationData.spent_duration = "00:00:10"
                                }

                                //New shop Create issue
                                shopDurationData.isnewShop = shopActivity.isnewShop!!

                                // 4.0 DashboardActivity AppV 4.0.6 multiple contact Data added on Api called
                                shopDurationData.multi_contact_name =
                                    shopActivity.multi_contact_name
                                shopDurationData.multi_contact_number =
                                    shopActivity.multi_contact_number

                                shopDurationData.distFromProfileAddrKms =
                                    shopActivity.distFromProfileAddrKms
                                shopDurationData.stationCode = shopActivity.stationCode

                                // Suman 06-05-2024 Suman SyncActivity update mantis 27335 begin
                                try {
                                    var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao()
                                        .getShopByIdN(shopDurationData.shop_id)
                                    shopDurationData.shop_lat = shopOb.shopLat.toString()
                                    shopDurationData.shop_long = shopOb.shopLong.toString()
                                    shopDurationData.shop_addr = shopOb.address.toString()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                                // Suman 06-05-2024 Suman SyncActivity update mantis 27335 end

                                shopDataList.add(shopDurationData)

                                //////////////////////////
                                var revisitStatusObj = ShopRevisitStatusRequestData()
                                var data =
                                    AppDatabase.getDBInstance()?.shopVisitOrderStatusRemarksDao()!!
                                        .getSingleItem(shopDurationData.shop_revisit_uniqKey.toString())
                                if (data != null) {
                                    revisitStatusObj.shop_id = data.shop_id
                                    revisitStatusObj.order_status = data.order_status
                                    revisitStatusObj.order_remarks = data.order_remarks
                                    revisitStatusObj.shop_revisit_uniqKey =
                                        data.shop_revisit_uniqKey
                                    revisitStatusList.add(revisitStatusObj)
                                }
                            }
                        } else {
                            // Shop duartion Issue mantis 25597
                            // mantis 25675 duartion calculation issue(multiple visit last data calculation) off
// val shopActiList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(syncedShopList[k].shop_id.toString(),AppUtils.getCurrentDateForShopActi()).firstOrNull()

// // start mantis 25675 duartion calculation issue(multiple visit last data calculation)
                            val shopActiList = AppDatabase.getDBInstance()!!.shopActivityDao()
                                .getShopForDayIsupload(
                                    syncedShopList[k].shop_id.toString(),
                                    AppUtils.getCurrentDateForShopActi(),
                                    false,
                                    true
                                ).firstOrNull()
                            //end mantis 25675
                            if (shopActiList != null) {
                                val endTimeStamp = System.currentTimeMillis().toString()
                                val totalMinute = AppUtils.getMinuteFromTimeStamp(
                                    shopActiList.startTimeStamp,
                                    endTimeStamp
                                )
                                val duration = AppUtils.getTimeFromTimeSpan(
                                    shopActiList.startTimeStamp,
                                    endTimeStamp
                                )
// tempDuration = duration
                                AppDatabase.getDBInstance()!!.shopActivityDao()
                                    .updateTimeDurationForDayOfShop(
                                        shopActiList.shopid.toString(),
                                        duration,
                                        AppUtils.getCurrentDateForShopActi(),
                                        shopActiList.startTimeStamp
                                    )
                            }

                            AppDatabase.getDBInstance()!!.shopActivityDao()
                                .updateDurationCalculatedStatusByShopID(
                                    syncedShopList[k].shop_id.toString(),
                                    true,
                                    AppUtils.getCurrentDateForShopActi()
                                )
                            val shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao()
                                .durationAvailableForShopList(
                                    syncedShopList[k].shop_id,
                                    true,
                                    false
                                )

                            shopActivity?.forEach {
                                val shopDurationData = ShopDurationRequestData()
                                shopDurationData.shop_id = it.shopid
                                shopDurationData.spent_duration = it.duration_spent
                                shopDurationData.visited_date = it.visited_date
                                shopDurationData.visited_time = it.visited_date
                                if (AppDatabase.getDBInstance()!!.addShopEntryDao()
                                        .getShopByIdN(it.shopid) != null
                                )
                                    shopDurationData.total_visit_count =
                                        AppDatabase.getDBInstance()!!.addShopEntryDao()
                                            .getShopByIdN(it.shopid).totalVisitCount
                                else
                                    shopDurationData.total_visit_count = "1"

                                if (TextUtils.isEmpty(it.distance_travelled))
                                    it.distance_travelled = "0.0"
                                shopDurationData.distance_travelled = it.distance_travelled

                                if (!TextUtils.isEmpty(it.feedback))
                                    shopDurationData.feedback = it.feedback
                                else
                                    shopDurationData.feedback = ""

                                shopDurationData.isFirstShopVisited = it.isFirstShopVisited
                                shopDurationData.distanceFromHomeLoc = it.distance_from_home_loc

                                shopDurationData.next_visit_date = it.next_visit_date

                                if (!TextUtils.isEmpty(it.early_revisit_reason))
                                    shopDurationData.early_revisit_reason = it.early_revisit_reason
                                else
                                    shopDurationData.early_revisit_reason = ""

                                shopDurationData.device_model = it.device_model
                                shopDurationData.android_version = it.android_version
                                shopDurationData.battery = it.battery
                                shopDurationData.net_status = it.net_status
                                shopDurationData.net_type = it.net_type
                                shopDurationData.in_time = it.in_time
                                shopDurationData.out_time = it.out_time
                                shopDurationData.start_timestamp = it.startTimeStamp
                                shopDurationData.in_location = it.in_loc
                                shopDurationData.out_location = it.out_loc
                                shopDurationData.shop_revisit_uniqKey = it.shop_revisit_uniqKey!!

                                shopDurationData.updated_by = Pref.user_id
                                try {
                                    shopDurationData.updated_on = it.updated_on!!
                                } catch (ex: Exception) {
                                    shopDurationData.updated_on = ""
                                }

                                if (!TextUtils.isEmpty(it.pros_id!!))
                                    shopDurationData.pros_id = it.pros_id!!
                                else
                                    shopDurationData.pros_id = ""

                                if (!TextUtils.isEmpty(it.agency_name!!))
                                    shopDurationData.agency_name = it.agency_name!!
                                else
                                    shopDurationData.agency_name = ""

                                if (!TextUtils.isEmpty(it.approximate_1st_billing_value))
                                    shopDurationData.approximate_1st_billing_value =
                                        it.approximate_1st_billing_value!!
                                else
                                    shopDurationData.approximate_1st_billing_value = ""

                                //duration garbage fix
                                try {
                                    if (shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8) {
                                        shopDurationData.spent_duration = "00:00:10"
                                    }
                                } catch (ex: Exception) {
                                    shopDurationData.spent_duration = "00:00:10"
                                }
                                //New shop Create issue
                                shopDurationData.isnewShop = it.isnewShop!!

                                // 4.0 DashboardActivity AppV 4.0.6 multiple contact Data added on Api called
                                shopDurationData.multi_contact_name = it.multi_contact_name
                                shopDurationData.multi_contact_number = it.multi_contact_number

                                shopDurationData.distFromProfileAddrKms = it.distFromProfileAddrKms
                                shopDurationData.stationCode = it.stationCode

                                // Suman 06-05-2024 Suman SyncActivity update mantis 27335 begin
                                try {
                                    var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao()
                                        .getShopByIdN(shopDurationData.shop_id)
                                    shopDurationData.shop_lat = shopOb.shopLat.toString()
                                    shopDurationData.shop_long = shopOb.shopLong.toString()
                                    shopDurationData.shop_addr = shopOb.address.toString()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                                // Suman 06-05-2024 Suman SyncActivity update mantis 27335 end

                                shopDataList.add(shopDurationData)

                                //////////////////////////
                                var revisitStatusObj = ShopRevisitStatusRequestData()
                                var data =
                                    AppDatabase.getDBInstance()?.shopVisitOrderStatusRemarksDao()!!
                                        .getSingleItem(shopDurationData.shop_revisit_uniqKey.toString())
                                if (data != null) {
                                    revisitStatusObj.shop_id = data.shop_id
                                    revisitStatusObj.order_status = data.order_status
                                    revisitStatusObj.order_remarks = data.order_remarks
                                    revisitStatusObj.shop_revisit_uniqKey =
                                        data.shop_revisit_uniqKey
                                    revisitStatusList.add(revisitStatusObj)
                                }

                            }
                        }
                    }

                    uiThread {
                        if (shopDataList.isEmpty()) {
                            BaseActivity.isShopActivityUpdating = false
                            simpleDialogProcess.dismiss()
                            (mContext as DashboardActivity).loadFragment(
                                FragType.LogoutSyncFragment,
                                false,
                                ""
                            )
                        } else {
                            val hashSet = HashSet<ShopDurationRequestData>()
                            val newShopList = ArrayList<ShopDurationRequestData>()

                            if (!Pref.isMultipleVisitEnable) {
                                for (i in shopDataList.indices) {
                                    if (hashSet.add(shopDataList[i]))
                                        newShopList.add(shopDataList[i])
                                }
                            }

                            val shopDurationApiReq = ShopDurationRequest()
                            shopDurationApiReq.user_id = Pref.user_id
                            shopDurationApiReq.session_token = Pref.session_token
                            if (newShopList.size > 0) {
                                Timber.e("Unique ShopData List size===> " + newShopList.size)
                                shopDurationApiReq.shop_list = newShopList
                            } else
                                shopDurationApiReq.shop_list = shopDataList

                            //new work for old revisit
                            var shopDurationApiReqForOldShop = ShopDurationRequest()
                            shopDurationApiReqForOldShop.user_id = Pref.user_id
                            shopDurationApiReqForOldShop.session_token = Pref.session_token
                            shopDurationApiReqForOldShop.shop_list = ArrayList()
                            shopDurationApiReqForOldShop.shop_list =
                                shopDurationApiReq!!.shop_list!!.filter { it.isnewShop!! == false } as ArrayList<ShopDurationRequestData>
                            if ((shopDurationApiReqForOldShop.shop_list as ArrayList<ShopDurationRequestData>).size > 0) {
                                shopDurationApiReq.shop_list =
                                    shopDurationApiReqForOldShop.shop_list
                                shopDurationApiReq.isnewShop = 0
                                val repository =
                                    ShopDurationRepositoryProvider.provideShopDurationRepository()
                                Timber.d("callShopDurationApi : REQUEST")
                                compositeDisposable.add(
                                    repository.shopDuration(shopDurationApiReq)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe({ result ->
                                            Timber.d("callShopDurationApi : RESPONSE " + result.status)
                                            if (result.status == NetworkConstant.SUCCESS) {

                                                if (!revisitStatusList.isEmpty()) {
                                                    callRevisitStatusUploadApi(revisitStatusList!!)
                                                }
                                                if (newShopList.size > 0) {
                                                    for (i in 0 until newShopList.size) {
                                                        AppDatabase.getDBInstance()!!
                                                            .shopActivityDao().updateisUploaded(
                                                                true,
                                                                newShopList[i].shop_id!!,
                                                                AppUtils.changeAttendanceDateFormatToCurrent(
                                                                    newShopList[i].visited_date!!
                                                                ) /*AppUtils.getCurrentDateForShopActi()*/
                                                            )
                                                    }
                                                    BaseActivity.isShopActivityUpdating = false
                                                    syncShopVisitImage(newShopList)
                                                } else {

                                                    BaseActivity.isShopActivityUpdating = false

                                                    if (!Pref.isMultipleVisitEnable) {
                                                        for (i in 0 until shopDataList.size) {
                                                            AppDatabase.getDBInstance()!!
                                                                .shopActivityDao().updateisUploaded(
                                                                    true,
                                                                    shopDataList[i].shop_id!!,
                                                                    AppUtils.changeAttendanceDateFormatToCurrent(
                                                                        shopDataList[i].visited_date!!
                                                                    ) /*AppUtils.getCurrentDateForShopActi()*/
                                                                )
                                                        }

                                                        syncShopVisitImage(shopDataList)
                                                    } else {
                                                        for (i in 0 until shopDataList.size) {
                                                            AppDatabase.getDBInstance()!!
                                                                .shopActivityDao().updateisUploaded(
                                                                    true,
                                                                    shopDataList[i].shop_id!!,
                                                                    AppUtils.changeAttendanceDateFormatToCurrent(
                                                                        shopDataList[i].visited_date!!
                                                                    ),
                                                                    shopDataList[i].start_timestamp!!
                                                                )
                                                        }

                                                        syncShopVisitImage(shopDataList)
                                                    }
                                                }
                                                BaseActivity.isShopActivityUpdating = false
                                                simpleDialogProcess.dismiss()
                                                (mContext as DashboardActivity).loadFragment(
                                                    FragType.LogoutSyncFragment,
                                                    false,
                                                    ""
                                                )
                                            } else {
                                                BaseActivity.isShopActivityUpdating = false
                                                simpleDialogProcess.dismiss()
                                                (mContext as DashboardActivity).loadFragment(
                                                    FragType.LogoutSyncFragment,
                                                    false,
                                                    ""
                                                )
                                            }
                                            BaseActivity.isShopActivityUpdating = false
                                        }, { error ->
                                            BaseActivity.isShopActivityUpdating = false
                                            simpleDialogProcess.dismiss()
                                            (mContext as DashboardActivity).loadFragment(
                                                FragType.LogoutSyncFragment,
                                                false,
                                                ""
                                            )
                                            if (error == null) {
                                                Timber.d("callShopDurationApi : ERROR " + "UNEXPECTED ERROR IN SHOP ACTIVITY API")
                                            } else {
                                                Timber.d("callShopDurationApi : ERROR " + error.localizedMessage)
                                                error.printStackTrace()
                                            }
                                        })
                                )
                            } else {
                                BaseActivity.isShopActivityUpdating = false
                                simpleDialogProcess.dismiss()
                                (mContext as DashboardActivity).loadFragment(
                                    FragType.LogoutSyncFragment,
                                    false,
                                    ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private var mShopDataList: MutableList<ShopDurationRequestData>? = null
    private fun syncShopVisitImage(shopDataList: MutableList<ShopDurationRequestData>) {
        mShopDataList = shopDataList
        val unSyncedList = ArrayList<ShopVisitImageModelEntity>()
        for (i in shopDataList.indices) {
            val unSyncedData = AppDatabase.getDBInstance()!!.shopVisitImageDao()
                .getTodaysUnSyncedListAccordingToShopId(
                    false,
                    shopDataList[i].shop_id!!,
                    shopDataList[i].visited_date!!
                )

            if (unSyncedData != null && unSyncedData.isNotEmpty()) {
                unSyncedList.add(unSyncedData[0])
            }
        }

        if (unSyncedList.size > 0) {
            i = 0
            callShopVisitImageUploadApi(unSyncedList)
        }
    }

    private fun callShopVisitImageUploadApi(unSyncedList: List<ShopVisitImageModelEntity>) {

        try {
            if (BaseActivity.isShopActivityUpdating)
                return
            BaseActivity.isShopActivityUpdating = true

            val visitImageShop = ShopVisitImageUploadInputModel()
            visitImageShop.session_token = Pref.session_token
            visitImageShop.user_id = Pref.user_id
            visitImageShop.shop_id = unSyncedList[i].shop_id
            visitImageShop.visit_datetime = unSyncedList[i].visit_datetime

            Timber.d("====UPLOAD REVISIT ALL IMAGE INPUT PARAMS (Logout Sync)======")
            Timber.d("USER ID====> " + visitImageShop.user_id)
            Timber.d("SESSION ID====> " + visitImageShop.session_token)
            Timber.d("SHOP ID====> " + visitImageShop.shop_id)
            Timber.d("VISIT DATE TIME=====> " + visitImageShop.visit_datetime)
            Timber.d("IMAGE=====> " + unSyncedList[i].shop_image)
            Timber.d("===============================================================")

            val repository = ShopVisitImageUploadRepoProvider.provideAddShopRepository()

            BaseActivity.compositeDisposable.add(
                repository.visitShopWithImage(
                    visitImageShop,
                    unSyncedList[i].shop_image!!,
                    mContext
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val logoutResponse = result as BaseResponse
                        Timber.d("UPLOAD REVISIT ALL IMAGE : " + "RESPONSE : " + logoutResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + logoutResponse.message)
                        if (logoutResponse.status == NetworkConstant.SUCCESS) {
                            AppDatabase.getDBInstance()!!.shopVisitImageDao()
                                .updateisUploaded(true, unSyncedList.get(i).shop_id!!)
                            BaseActivity.isShopActivityUpdating = false
                            i++
                            if (i < unSyncedList.size)
                                callShopVisitImageUploadApi(unSyncedList)
                            else {
                                i = 0
                                //checkToCallAudioApi()
                            }
                        } else {
                            progress_wheel.stopSpinning()
                            BaseActivity.isShopActivityUpdating = false
                            //checkToCallSyncOrder()
                            //checkToRetryVisitButton()
                        }

                    }, { error ->
                        progress_wheel.stopSpinning()
                        Timber.d("UPLOAD REVISIT ALL IMAGE : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                        error.printStackTrace()
                        BaseActivity.isShopActivityUpdating = false
                        //checkToCallSyncOrder()
                        //checkToRetryVisitButton()
                    })
            )
        } catch (e: Exception) {
            e.printStackTrace()
            progress_wheel.stopSpinning()
            BaseActivity.isShopActivityUpdating = false
            //checkToCallSyncOrder()
        }
    }

    private fun callRevisitStatusUploadApi(revisitStatusList: MutableList<ShopRevisitStatusRequestData>) {
        val revisitStatus = ShopRevisitStatusRequest()
        revisitStatus.user_id = Pref.user_id
        revisitStatus.session_token = Pref.session_token
        revisitStatus.ordernottaken_list = revisitStatusList

        val repository = ShopRevisitStatusRepositoryProvider.provideShopRevisitStatusRepository()
        compositeDisposable.add(
            repository.shopRevisitStatus(revisitStatus)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Timber.d("callRevisitStatusUploadApi : RESPONSE " + result.status)
                    if (result.status == NetworkConstant.SUCCESS) {
                        for (i in revisitStatusList.indices) {
                            AppDatabase.getDBInstance()?.shopVisitOrderStatusRemarksDao()!!
                                .updateOrderStatus(revisitStatusList[i]!!.shop_revisit_uniqKey!!)
                        }
                    }
                }, { error ->
                    if (error == null) {
                        Timber.d("callRevisitStatusUploadApi : ERROR " + "UNEXPECTED ERROR IN SHOP ACTIVITY API")
                    } else {
                        Timber.d("callRevisitStatusUploadApi : ERROR " + error.localizedMessage)
                        error.printStackTrace()
                    }
                })
        )
    }


    ////// all nearby revisit start
    private fun checkAutoRevisitAll() {
        var distance = LocationWizard.getDistance(
            lastLat,
            lastLng,
            Pref.current_latitude.toDouble(),
            Pref.current_longitude.toDouble()
        )
        distance = 0.9

        var autoRevDistance: Double = 0.0
        if (AppUtils.isOnline(this)) {
            autoRevDistance = Pref.autoRevisitDistance.toDouble()
        } else {
            if (Pref.OfflineShopAccuracy.toDouble() > Pref.autoRevisitDistance.toDouble())
                autoRevDistance = Pref.OfflineShopAccuracy.toDouble()
            else
                autoRevDistance = Pref.autoRevisitDistance.toDouble()
        }

        shopCodeListNearby = ArrayList()

        if (distance * 1000 > autoRevDistance) {
            val allShopList = AppDatabase.getDBInstance()!!.addShopEntryDao().all
            if (allShopList != null && allShopList.size > 0) {
                for (i in 0 until allShopList.size) {


                    val shopLat: Double = allShopList[i].shopLat
                    val shopLong: Double = allShopList[i].shopLong
                    if (shopLat != null && shopLong != null) {
                        val shopLocation = Location("")
                        shopLocation.latitude = shopLat
                        shopLocation.longitude = shopLong
                        shop_id = allShopList[i].shop_id
                        var isShopNearby = FTStorageUtils.checkShopPositionWithinRadious(
                            AppUtils.mLocation,
                            shopLocation,
                            autoRevDistance.toInt()
                        )
                        println("autorev ${allShopList[i].shopName} $isShopNearby")



                        if (isShopNearby) {
                            val shopActivityList = AppDatabase.getDBInstance()!!.shopActivityDao()
                                .getShopForDay(
                                    allShopList[i].shop_id,
                                    AppUtils.getCurrentDateForShopActi()
                                )
                            if (shopActivityList == null || shopActivityList.isEmpty()) {

                                shopCodeListNearby.add(shop_id)

                            } else
                                Timber.e("==" + allShopList[i].shopName + " is visiting now normally (Loc Fuzed Service)==")
                        }
                    }
                }
                println("autorev total nearby size ${shopCodeListNearby.size}")
                if (shopCodeListNearby.size > 0) {
                    revisitShopAll()
                } else {
                    progress_wheel.stopSpinning()
                }
            }
        }
    }

    var shopCodeListNearby: ArrayList<String> = ArrayList()

    private fun revisitShopAll() {

        if (shopCodeListNearby.size == 0) {

            revisit_ll.isEnabled = true
            progress_wheel.stopSpinning()
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_message)
            val dialogHeader =
                simpleDialog.findViewById(R.id.dialog_message_header_TV) as AppCustomTextView
            val dialog_yes_no_headerTV =
                simpleDialog.findViewById(R.id.dialog_message_headerTV) as AppCustomTextView
            dialog_yes_no_headerTV.text = AppUtils.hiFirstNameText()
            dialogHeader.text = "Process has been successfully completed."
            val dialogYes = simpleDialog.findViewById(R.id.tv_message_ok) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()
            return
        }


        try {
            shop_id = shopCodeListNearby.get(0)
            if (shopCodeListNearby.size > 0)
                shopCodeListNearby.removeAt(0)
        } catch (ex: Exception) {
            println("autorev error")
            return
        }



        try {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(shop_id.hashCode())

            val shopActivityEntity = AppDatabase.getDBInstance()!!.shopActivityDao()
                .getShopForDay(shop_id, AppUtils.getCurrentDateForShopActi())
            val imageUpDateTime = AppUtils.getCurrentISODateTime()

            val mAddShopDBModelEntity =
                AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

            if (shopActivityEntity.isEmpty() || shopActivityEntity[0].date != AppUtils.getCurrentDateForShopActi()) {
                val mShopActivityEntity = ShopActivityEntity()
                AppUtils.changeLanguage(this, "en")
                mShopActivityEntity.startTimeStamp = System.currentTimeMillis().toString()
                changeLocale()
                mShopActivityEntity.isUploaded = false
                mShopActivityEntity.isVisited = true
                mShopActivityEntity.shop_name = mAddShopDBModelEntity?.shopName
                mShopActivityEntity.duration_spent = "00:00:00"
                mShopActivityEntity.date = AppUtils.getCurrentDateForShopActi()
                mShopActivityEntity.shop_address = mAddShopDBModelEntity?.address
                mShopActivityEntity.shopid = mAddShopDBModelEntity?.shop_id
                mShopActivityEntity.visited_date =
                    imageUpDateTime //AppUtils.getCurrentISODateTime()
                mShopActivityEntity.isDurationCalculated = false
                if (mAddShopDBModelEntity?.totalVisitCount != null && mAddShopDBModelEntity?.totalVisitCount != "") {
                    val visitCount = mAddShopDBModelEntity?.totalVisitCount?.toInt()!! + 1
                    AppDatabase.getDBInstance()!!.addShopEntryDao()
                        .updateTotalCount(visitCount.toString(), shop_id)
                    AppDatabase.getDBInstance()!!.addShopEntryDao()
                        .updateLastVisitDate(AppUtils.getCurrentDateChanged(), shop_id)
                }

                var distance = 0.0
                var address = ""
                Timber.e("======New Distance (At auto revisit time)=========")

                val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shop_id)
                address = if (!TextUtils.isEmpty(shop.actual_address))
                    shop.actual_address
                else
                    LocationWizard.getNewLocationName(
                        this,
                        shop.shopLat.toDouble(),
                        shop.shopLong.toDouble()
                    )

                if (Pref.isOnLeave.equals("false", ignoreCase = true)) {
                    Timber.e("=====User is at work (At auto revisit time)=======")

                    val locationList = AppDatabase.getDBInstance()!!.userLocationDataDao()
                        .getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi())

                    val userlocation = UserLocationDataEntity()
                    userlocation.latitude = shop.shopLat.toString()
                    userlocation.longitude = shop.shopLong.toString()

                    var loc_distance = 0.0

                    if (locationList != null && locationList.isNotEmpty()) {
                        loc_distance = LocationWizard.getDistance(
                            locationList[locationList.size - 1].latitude.toDouble(),
                            locationList[locationList.size - 1].longitude.toDouble(),
                            userlocation.latitude.toDouble(),
                            userlocation.longitude.toDouble()
                        )
                    }
                    val finalDistance = (Pref.tempDistance.toDouble() + loc_distance).toString()

                    Timber.e("===Distance (At auto shop revisit time)===")
                    Timber.e("Temp Distance====> " + Pref.tempDistance)
                    Timber.e("Normal Distance====> $loc_distance")
                    Timber.e("Total Distance====> $finalDistance")
                    Timber.e("===========================================")

                    userlocation.distance = finalDistance
                    userlocation.locationName = LocationWizard.getNewLocationName(
                        this,
                        userlocation.latitude.toDouble(),
                        userlocation.longitude.toDouble()
                    )
                    userlocation.timestamp = LocationWizard.getTimeStamp()
                    userlocation.time = LocationWizard.getFormattedTime24Hours(true)
                    userlocation.meridiem = LocationWizard.getMeridiem()
                    userlocation.hour = LocationWizard.getHour()
                    userlocation.minutes = LocationWizard.getMinute()
                    userlocation.isUploaded = false
                    userlocation.shops = AppDatabase.getDBInstance()!!.shopActivityDao()
                        .getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi()).size.toString()
                    userlocation.updateDate = AppUtils.getCurrentDateForShopActi()
                    userlocation.updateDateTime = AppUtils.getCurrentDateTime()
                    userlocation.network_status =
                        if (AppUtils.isOnline(this)) "Online" else "Offline"
                    userlocation.battery_percentage = AppUtils.getBatteryPercentage(this).toString()

                    //negative distance handle Suman 06-02-2024 mantis id 0027225 begin
                    try {
                        var distReftify = userlocation.distance.toDouble()
                        if (distReftify < 0) {
                            var locL = AppDatabase.getDBInstance()!!.userLocationDataDao()
                                .getLocationUpdateForADay(AppUtils.getCurrentDateForShopActi()) as ArrayList<UserLocationDataEntity>
                            var lastLoc = locL.get(locL.size - 1)
                            var d = LocationWizard.getDistance(
                                userlocation.latitude.toDouble(),
                                userlocation.longitude.toDouble(),
                                lastLoc.latitude.toDouble(),
                                lastLoc.longitude.toDouble()
                            )
                            userlocation.distance = d.toString()
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        userlocation.distance = "0.0"
                    }
                    //negative distance handle Suman 06-02-2024 mantis id 0027225 end

                    AppDatabase.getDBInstance()!!.userLocationDataDao().insertAll(userlocation)

                    Timber.e("=====Shop auto revisit data added=======")

                    Pref.totalS2SDistance =
                        (Pref.totalS2SDistance.toDouble() + userlocation.distance.toDouble()).toString()

                    distance = Pref.totalS2SDistance.toDouble()
                    Pref.totalS2SDistance = "0.0"
                    Pref.tempDistance = "0.0"
                } else {
                    Timber.e("=====User is on leave (At auto revisit time)=======")
                    distance = 0.0
                }

                Timber.e("shop to shop distance (At auto revisit time)=====> $distance")

                mShopActivityEntity.distance_travelled = distance.toString()
                mShopActivityEntity.in_time = AppUtils.getCurrentTimeWithMeredian()
                mShopActivityEntity.in_loc = address

                Pref.isShopVisited = true

                var shopAll = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityAll()
                mShopActivityEntity.shop_revisit_uniqKey =
                    Pref.user_id + System.currentTimeMillis().toString()

                AppDatabase.getDBInstance()!!.shopActivityDao().insertAll(mShopActivityEntity)

                /*Terminate All other Shop Visit*/
                val shopList = AppDatabase.getDBInstance()!!.shopActivityDao()
                    .getTotalShopVisitedForADay(AppUtils.getCurrentDateForShopActi())
                for (i in 0 until shopList.size) {
                    if (shopList[i].shopid != mShopActivityEntity.shopid && !shopList[i].isDurationCalculated) {
                        AppUtils.changeLanguage(this, "en")
                        val endTimeStamp = System.currentTimeMillis().toString()
                        changeLocale()
                        var duration =
                            AppUtils.getTimeFromTimeSpan(shopList[i].startTimeStamp, endTimeStamp)
                        val totalMinute = AppUtils.getMinuteFromTimeStamp(
                            shopList[i].startTimeStamp,
                            endTimeStamp
                        )

                        Timber.d("revisitShop LocFuzedS=> startT: ${shopList[i].startTimeStamp} endTime: $endTimeStamp duration: $duration totalMinute:$totalMinute")
                        if (duration.contains("-")) {
                            duration = "00:00:00"
                        }

                        //If duration is greater than 20 hour then stop incrementing
                        /*if (totalMinute.toInt() > 20 * 60) {
 AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(true, shopList[i].shopid!!, AppUtils.getCurrentDateForShopActi())
 return
 }*/
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(
                            endTimeStamp,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTotalMinuteForDayOfShop(
                                shopList[i].shopid!!,
                                totalMinute,
                                AppUtils.getCurrentDateForShopActi()
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao()
                            .updateTimeDurationForDayOfShop(
                                shopList[i].shopid!!,
                                duration,
                                AppUtils.getCurrentDateForShopActi()
                            )
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(
                            true,
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateOutTime(
                            AppUtils.getCurrentTimeWithMeredian(),
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi(),
                            shopList[i].startTimeStamp
                        )
                        AppDatabase.getDBInstance()!!.shopActivityDao().updateOutLocation(
                            LocationWizard.getNewLocationName(
                                this,
                                Pref.current_latitude.toDouble(),
                                Pref.current_longitude.toDouble()
                            ),
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi(),
                            shopList[i].startTimeStamp
                        )

                        val netStatus = if (AppUtils.isOnline(this))
                            "Online"
                        else
                            "Offline"

                        val netType =
                            if (AppUtils.getNetworkType(this).equals("wifi", ignoreCase = true))
                                AppUtils.getNetworkType(this)
                            else
                                "Mobile ${AppUtils.mobNetType(this)}"

                        AppDatabase.getDBInstance()!!.shopActivityDao().updateDeviceStatusReason(
                            AppUtils.getDeviceName(),
                            AppUtils.getAndroidVersion(),
                            AppUtils.getBatteryPercentage(this).toString(),
                            netStatus,
                            netType.toString(),
                            shopList[i].shopid!!,
                            AppUtils.getCurrentDateForShopActi()
                        )
                    }
                }
            }

            AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shop_id)!![0].visited =
                true

            val performance = AppDatabase.getDBInstance()!!.performanceDao()
                .getTodaysData(AppUtils.getCurrentDateForShopActi())
            if (performance != null) {
                val list = AppDatabase.getDBInstance()!!.shopActivityDao()
                    .getDurationCalculatedVisitedShopForADay(
                        AppUtils.getCurrentDateForShopActi(),
                        true
                    )
                AppDatabase.getDBInstance()!!.performanceDao().updateTotalShopVisited(
                    list.size.toString(),
                    AppUtils.getCurrentDateForShopActi()
                )
                var totalTimeSpentForADay = 0
                for (i in list.indices) {
                    totalTimeSpentForADay += list[i].totalMinute.toInt()
                }
                AppDatabase.getDBInstance()!!.performanceDao().updateTotalDuration(
                    totalTimeSpentForADay.toString(),
                    AppUtils.getCurrentDateForShopActi()
                )
            } else {
                val list = AppDatabase.getDBInstance()!!.shopActivityDao()
                    .getDurationCalculatedVisitedShopForADay(
                        AppUtils.getCurrentDateForShopActi(),
                        true
                    )
                val performanceEntity = PerformanceEntity()
                performanceEntity.date = AppUtils.getCurrentDateForShopActi()
                performanceEntity.total_shop_visited = list.size.toString()
                var totalTimeSpentForADay = 0
                for (i in list.indices) {
                    totalTimeSpentForADay += list[i].totalMinute.toInt()
                }
                performanceEntity.total_duration_spent = totalTimeSpentForADay.toString()
                AppDatabase.getDBInstance()!!.performanceDao().insert(performanceEntity)
            }

            AppUtils.isAutoRevisit = false
            Timber.e("Fuzed Location: auto revisit endes ${AppUtils.getCurrentDateTime()}")
            val intent = Intent()
            intent.action = "AUTO_REVISIT_BROADCAST"
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

            Handler().postDelayed(Runnable {
                revisitShopAll()
            }, 100)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    ////// all nearby revisit end

    private fun changeLocale() {
        val intent = Intent()
        intent.action = "CHANGE_LOCALE_BROADCAST"
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
    }

    private fun startVoiceInput() {
        val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
        try {
            startActivityForResult(intent, MaterialSearchView.REQUEST_VOICE)
        } catch (a: ActivityNotFoundException) {
            a.printStackTrace()
        }
    }

    private fun showInstructionOfTwoStepVerification() {

        var instructionDialog = Dialog(mContext)
        instructionDialog!!.setCancelable(false)
        instructionDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        instructionDialog!!.setContentView(R.layout.dialog_gmail_instruction_only)
        val tvHeader = instructionDialog!!.findViewById(R.id.dialog_contact_gr_header) as TextView
        val tv_instruction = instructionDialog!!.findViewById(R.id.tv_instruction) as TextView
        val tv_save_instruction =
            instructionDialog!!.findViewById(R.id.tv_save_instruction) as TextView
        val iv_dialog_instruction_copy =
            instructionDialog!!.findViewById(R.id.iv_dialog_instruction_copy) as ImageView
        val iv_close =
            instructionDialog!!.findViewById(R.id.iv_dialog_instruction_close_icon) as ImageView


        val tv_ulink = instructionDialog!!.findViewById(R.id.tv_ulink) as TextView
        tv_ulink.setOnClickListener {
            var youtubeID = "dM_DlzyeWW8"
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
            val intentBrowser = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + youtubeID)
            )
            try {
                this.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                this.startActivity(intentBrowser)
            }
        }

        tv_save_instruction.setOnClickListener {
            instructionDialog.dismiss()
        }
        iv_close.setOnClickListener {
            instructionDialog.dismiss()
        }
        iv_dialog_instruction_copy.setOnClickListener {
            val clipboard: ClipboardManager? =
                mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", tv_instruction.text.toString().trim())
            clipboard!!.setPrimaryClip(clip)
            Toaster.msgLong(mContext, "Copied to Clipboard")
        }
        tvHeader.text = "E-mail configuration"
        instructionDialog!!.show()
    }


    private fun trackLMSModuleLoad(mFragType: FragType) {
        try {
            var lmsL = AppDatabase.getDBInstance()!!.lmsUserInfoDao()
                .getAllByDateNotCalculated(AppUtils.getCurrentDateyymmdd()) as ArrayList<LmsUserInfoEntity>
            var lmsModuleObj = LmsUserInfoEntity()

            if (mFragType.name.equals("SearchLmsFrag")) {
                lmsModuleObj.module_name = "SearchLmsFrag"
            } else if (mFragType.name.equals("MyLearningTopicList")) {
                lmsModuleObj.module_name = "MyLearningTopicList"
            } else if (mFragType.name.equals("SearchLmsKnowledgeFrag")) {
                lmsModuleObj.module_name = "SearchLmsKnowledgeFrag"
            } else if (mFragType.name.equals("MyPerformanceFrag")) {
                lmsModuleObj.module_name = "MyPerformanceFrag"
            } else if (mFragType.name.equals("LeaderboardLmsFrag")) {
                lmsModuleObj.module_name = "LeaderboardLmsFrag"
            }

            println("tag_module_check ${lmsModuleObj.module_name}")
            if (!lmsModuleObj.module_name.equals("")) {
                if (lmsL.size > 0 || mFragType.name.equals("MyLearningFragment")) {
                    var lastFilterData = lmsL.last()
                    val endTimeMillis = System.currentTimeMillis()
                    val duration = AppUtils.getTimeFromTimeSpan(
                        lastFilterData.module_startTimeInMilli,
                        endTimeMillis.toString()
                    )
                    AppDatabase.getDBInstance()!!.lmsUserInfoDao().updateEnd(
                        endTimeMillis.toString(), true,
                        lastFilterData.module_startTimeInMilli.toString(), duration.toString()
                    )
                }

                lmsModuleObj.count_of_use = "1"
                lmsModuleObj.time_spend = "0"

                lmsModuleObj.last_current_loc_lat = Pref.current_latitude
                lmsModuleObj.last_current_loc_long = Pref.current_longitude
                lmsModuleObj.last_current_loc_address = Pref.current_address
                lmsModuleObj.date_time = AppUtils.getCurrentDateTime()
                lmsModuleObj.phone_model = ""
                lmsModuleObj.isUploaded = false
                lmsModuleObj.module_startTimeInMilli = System.currentTimeMillis().toString()
                lmsModuleObj.module_endTimeInMilli = ""
                AppDatabase.getDBInstance()!!.lmsUserInfoDao().insertAll(lmsModuleObj)
                //AppUtils.gethhmmssFromSeconds()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun callmoduleEnd() {
        try {
            var lmsL = AppDatabase.getDBInstance()!!.lmsUserInfoDao()
                .getAllByDateNotCalculated(AppUtils.getCurrentDateyymmdd()) as ArrayList<LmsUserInfoEntity>
            if (lmsL.size > 0) {
                var lastFilterData = lmsL.last()
                val endTimeMillis = System.currentTimeMillis()
                val duration = AppUtils.getTimeFromTimeSpan(
                    lastFilterData.module_startTimeInMilli,
                    endTimeMillis.toString()
                )
                AppDatabase.getDBInstance()!!.lmsUserInfoDao().updateEnd(
                    endTimeMillis.toString(), true,
                    lastFilterData.module_startTimeInMilli.toString(), duration.toString()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        apiCallforUserWiseLMSModulesInfoSave()
    }


    data class LMSModule(
        var user_id: String = "",
        var user_lms_info_list: ArrayList<LMSInfo> = ArrayList()
    )

    data class LMSInfo(
        var module_name: String = "",
        var count_of_use: String = "",
        var time_spend: String = "",
        var last_current_loc_lat: String = "",
        var last_current_loc_long: String = "",
        var last_current_loc_address: String = "",
        var date_time: String = "",
        var phone_model: String = ""
    )

    fun apiCallforUserWiseLMSModulesInfoSave() {

        try {
            var dataL = AppDatabase.getDBInstance()!!.lmsUserInfoDao().getAll()
            if (dataL.size > 0) {
                var syncObj = LMSModule()
                syncObj.user_id = Pref.user_id!!.toString()!!
                for (i in 0..dataL.size - 1) {
                    var dtlsObj = LMSInfo()
                    dtlsObj.module_name = dataL.get(i).module_name
                    dtlsObj.count_of_use = "1"
                    dtlsObj.time_spend = dataL.get(i).time_spend
                    dtlsObj.last_current_loc_lat = Pref.current_latitude
                    dtlsObj.last_current_loc_long = Pref.current_longitude
                    dtlsObj.last_current_loc_address =
                        getAddressFromLatLng(Pref.current_latitude, Pref.current_longitude)
                    dtlsObj.date_time = dataL.get(i).date_time
                    dtlsObj.phone_model = AppUtils.getDeviceName()
                    syncObj.user_lms_info_list.add(dtlsObj)
                }

                val repository = OpportunityRepoProvider.opportunityListRepo()
                BaseActivity.compositeDisposable.add(
                    repository.saveLMSModuleInfo(syncObj)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            if (response.status == NetworkConstant.SUCCESS) {
                                println("tag_ssav $response.status")

                                doAsync {
                                    AppDatabase.getDBInstance()!!.lmsUserInfoDao().deleteAll()
                                    uiThread {

                                    }
                                }
                            } else {
                                println("tag_ssav elseee")
                            }
                        }, { error ->
                            error.printStackTrace()
                            println("tag_ssav error")
                        })
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("tag_ssav error ${e.printStackTrace()}")
        }


    }

    private fun getAddressFromLatLng(lat: String, lon: String): String {
        try {//22.6068776, 88.4898951
            var address =
                LocationWizard.getAdressFromLatlng(mContext, lat.toDouble(), lon.toDouble())
//        Timber.e("Shop address (Add Shop)======> $address")
            Timber.e("Shop address (Add Shop)======> $address")

            if (address.contains("http"))
                address = "Unknown"


            return address
        } catch (e: Exception) {
            e.printStackTrace()
            return "Unknown"
        }
    }

    fun updateBookmarkCnt() {
        try {
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getBookmarkedApiCall(Pref.user_id.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        var response = result as BookmarkFetchResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            Pref.CurrentBookmarkCount =
                                response.bookmark_list.distinctBy { it.content_id.toString() }.size
                            tv_saved_count.visibility = View.VISIBLE
                            tv_saved_count.text = Pref.CurrentBookmarkCount.toString()
                        } else {
                            Pref.CurrentBookmarkCount = 0
                            tv_saved_count.visibility = View.GONE
                            //tv_saved_count.text = Pref.CurrentBookmarkCount.toString()
                        }
                    }, { error ->
                        error.printStackTrace()
                        Pref.CurrentBookmarkCount = 0
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Pref.CurrentBookmarkCount = 0
        }
    }

    fun hideToolbar(){
        try {
            Handler().postDelayed(Runnable {
                include_toolbar.visibility = View.GONE
            }, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun statusColorLandScape(){
        try {
            window.setStatusBarColor(resources.getColor(R.color.toolbartransparent_lms))
        } catch (e: Exception) {
            e.printStackTrace()
            window.setStatusBarColor(resources.getColor(R.color.toolbartransparent_lms))
        }
    }

    fun statusColorPortrait(){
        try {
            window.setStatusBarColor(resources.getColor(R.color.toolbar_lms))
        } catch (e: Exception) {
            e.printStackTrace()
            window.setStatusBarColor(resources.getColor(R.color.toolbar_lms))
        }
    }

    fun statusColorPortraitWithFsm(){
        try {
            window.setStatusBarColor(resources.getColor(R.color.colorPrimary))
        } catch (e: Exception) {
            e.printStackTrace()
            window.setStatusBarColor(resources.getColor(R.color.colorPrimary))
        }
    }

    fun showToolbar(){
        try {
            include_toolbar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleToolbar(){
        try {
            if(include_toolbar.visibility == View.VISIBLE){
                include_toolbar.visibility = View.GONE
            }else{
                include_toolbar.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}