package com.nationalplasticfsm.features.marketAssist

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.nationalplasticfsm.R
import com.nationalplasticfsm.app.AppDatabase
import com.nationalplasticfsm.app.SearchListener
import com.nationalplasticfsm.app.utils.AppUtils
import com.nationalplasticfsm.app.utils.Toaster
import com.nationalplasticfsm.base.presentation.BaseFragment
import com.nationalplasticfsm.features.dashboard.presentation.DashboardActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.util.Locale

class ShopListMarketAssistFrag : BaseFragment(), View.OnClickListener{

    private lateinit var mContext: Context
    private lateinit var rvShopList:RecyclerView
    private lateinit var etSearchShop:EditText
    private lateinit var llSearch:LinearLayout
    private lateinit var ivMic:ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_shop_list_market_assist, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        rvShopList = view.findViewById(R.id.rv_frag_shop_list_ma_list)
        etSearchShop = view.findViewById(R.id.et_frag_shop_list_ma_list_search)
        llSearch = view.findViewById(R.id.ll_frag_shop_list_ma_list_search)
        ivMic = view.findViewById(R.id.iv_frag_shop_list_ma_mic)
        llSearch.setOnClickListener(this)
        ivMic.setOnClickListener(this)


        setShopData("")
        etSearchShop.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 0) {
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    setShopData("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun setShopData(filter:String){
        var shopL : ArrayList<ShopDtls> = ArrayList()
        try{
            doAsync {
                if(filter.length==0){
                    shopL = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopForMarketAssist() as ArrayList<ShopDtls>
                }else{
                    shopL = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopForMarketAssist()!!.filter { it.shop_name.contains(filter,ignoreCase = true) || it.owner_contact_number.contains(filter) } as ArrayList<ShopDtls>
                }
                uiThread {
                    //rvShopList.adapter = AdapterShopListMarketAssist(mContext,shopL)
                    rvShopList.adapter = AdapterShopListMarketAssist1(mContext,shopL)
                }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            Toaster.msgShort(mContext,"No data found.")
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_frag_shop_list_ma_list_search ->{
                var searchText = etSearchShop.text.toString()
                if(searchText.length>0){
                    AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                    setShopData(searchText)
                }
            }
            R.id.iv_frag_shop_list_ma_mic ->{
                val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                Handler().postDelayed(Runnable {
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi")
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
                }, 1000)
                try {
                    startActivityForResult(intent, 7009)
                    Handler().postDelayed(Runnable {

                    }, 2000)

                } catch (a: ActivityNotFoundException) {
                    a.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try {
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                etSearchShop.setText(result!![0].toString())
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }
}