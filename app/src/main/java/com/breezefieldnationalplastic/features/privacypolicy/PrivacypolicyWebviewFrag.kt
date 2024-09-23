package com.breezefieldnationalplastic.features.privacypolicy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.app.NewFileUtils
import com.breezefieldnationalplastic.app.utils.AppUtils
import com.breezefieldnationalplastic.app.utils.Toaster
import com.breezefieldnationalplastic.base.presentation.BaseFragment
import com.breezefieldnationalplastic.features.dashboard.presentation.DashboardActivity
import com.pnikosis.materialishprogress.ProgressWheel
import java.io.File
/*Created by Saheli mantis 0025783 */
class PrivacypolicyWebviewFrag: BaseFragment() {
    private lateinit var mContext: Context
    private lateinit var webview: WebView
    private lateinit var rl_webview_main: RelativeLayout
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var ll_loader: LinearLayout
    private var isOnPageStarted = false
    private lateinit var wait_for_preview: TextView
    private lateinit var no_internet: TextView



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_privacy_policy_webview, container, false)
        initView(view)

        return view
    }

    @SuppressLint("NewApi")
    private fun initView(view: View) {
        view.apply {
            webview = findViewById(R.id.webview)
            rl_webview_main = findViewById(R.id.rl_webview_main)
            progress_wheel = findViewById(R.id.progress_wheel)
            ll_loader = findViewById(R.id.ll_loader)
            wait_for_preview = findViewById(R.id.wait_for_preview)
            no_internet = findViewById(R.id.no_internet)

        }
        progress_wheel.stopSpinning()

        if (AppUtils.isOnline(mContext)) {
            webview.visibility = View.VISIBLE

            webview.settings.run {
                javaScriptEnabled = true
                setSupportZoom(true)
                domStorageEnabled = true
                pluginState = WebSettings.PluginState.ON
                builtInZoomControls = true
                displayZoomControls = false
                webview
            }.let {
                it.webChromeClient = WebChromeClient()
                it.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                //val extension = NewFileUtils.getExtension(File("https://breezefsm.in/privacy-policy/"))
                val extension =
                    NewFileUtils.getExtension(File("https://breezefsm.in/privacy-policy/index.html"))
                if (extension.equals("doc", ignoreCase = true) || extension.equals(
                        "docx",
                        ignoreCase = true
                    ) || extension.equals("pdf", ignoreCase = true)
                )
                //it.loadUrl("https://breezefsm.in/privacy-policy/")
                    it.loadUrl("https://breezefsm.in/privacy-policy/index.html")
                else if (extension.equals("pptx", ignoreCase = true) || extension.equals(
                        "ppt",
                        ignoreCase = true
                    )
                )
                //it.loadUrl("https://breezefsm.in/privacy-policy/")
                    it.loadUrl("https://breezefsm.in/privacy-policy/index.html")
                else
                //it.loadUrl("https://breezefsm.in/privacy-policy/")
                    it.loadUrl("https://breezefsm.in/privacy-policy/index.html")
                it.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        progress_wheel.spin()
                        super.onPageStarted(view, url, favicon)
                        Log.e("Webview", "======================page started===================")
                        isOnPageStarted = true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (isOnPageStarted) {
                            progress_wheel.stopSpinning()
                            ll_loader.visibility = View.GONE
                        } else
                            view?.loadUrl(url!!)
                        Log.e("Webview", "======================page finished===================")
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {

                        val url = "https://api.whatsapp.com/send?phone=+919674476953"

                        try {
                            val pm = mContext.packageManager
                            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            startActivity(i)
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage("Whatsapp app not installed in your phone.")
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage("This is not whatsApp no.")
                        }
                        //view?.loadUrl("https://breezefsm.in/privacy-policy/")
                        view?.loadUrl("https://breezefsm.in/privacy-policy/index.html")
                        return
                        view?.loadUrl("about:blank")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.e("LearningWebView", error?.description.toString())
                        }
                        //(mContext as DashboardActivity).showSnackMessage("Sorry, we are unable to load file.")
                    }
                }

                it.setOnTouchListener { view, motionEvent ->
                    it.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                    false
                }
            }
        }
        else{
            wait_for_preview.visibility = View.GONE
            no_internet.visibility = View.VISIBLE
        }
        rl_webview_main.setOnClickListener(null)
    }
}