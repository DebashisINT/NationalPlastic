package com.breezenationalplasticfsm.features.mylearning

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.breezenationalplasticfsm.R
import com.breezenationalplasticfsm.base.presentation.BaseFragment
import com.breezenationalplasticfsm.features.dashboard.presentation.DashboardActivity
import android.os.Handler
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.breezenationalplasticfsm.app.Pref
import com.bumptech.glide.Glide
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.dialog_multiple_contact.et_dialog_multi_contact_name


class LmsQuestionAnswerSet : BaseFragment() , View.OnClickListener {
    private lateinit var mContext: Context
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var tv_save_qstn_answr_set: LinearLayout
    private lateinit var tv_save_qstn_answr_set_text: TextView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var popupWindow: PopupWindow
    private lateinit var ll_parent_question_answer: LinearLayout
    private lateinit var iv_save_qstn_answr_set_next: ImageView

    private lateinit var tv_question: TextView
    private lateinit var tv_op1: TextView
    private lateinit var tv_op2: TextView
    private lateinit var tv_op3: TextView
    private lateinit var tv_op4: TextView

    private lateinit var card_op1: CardView
    private lateinit var card_op2: CardView
    private lateinit var card_op3: CardView
    private lateinit var card_op4: CardView

    private lateinit var iv_back: ImageView

    private lateinit var progress_wheel: ProgressWheel

    private var opSelection:Int=0

    private var questionSerialPosition = 0
    var finalL :ArrayList<QuestionL> = ArrayList()
    var correct_count = 0
    var incorrect_count = 0
    var total_points = 0
    var lastvideo:Boolean = VideoPlayLMS.lastvideo


    companion object{
        var lastVideo:Boolean = false
        var question_submit:Boolean = false
        var question_submit_content_id:Int = 0
        var questionlist: ArrayList<QuestionL> = ArrayList()
        var previousFrag: String = ""
        var Obj_LMS_CONTENT_INFO:LMS_CONTENT_INFO = LMS_CONTENT_INFO()
        fun getInstance(objects: Any): LmsQuestionAnswerSet {
            val lmsQuestionAnswerSet = LmsQuestionAnswerSet()
            questionlist = ArrayList()
            if (!TextUtils.isEmpty(objects.toString())) {
                val parts = objects.toString().split("~")
                /*questionlist = parts[0] as ArrayList<QuestionL>
                lastvideo = parts[1] .toBoolean()*/
                questionlist=objects as ArrayList<QuestionL>
            }else{
                questionlist = ArrayList()
            }
            println("tag_questionlist"+ questionlist)
            return lmsQuestionAnswerSet
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_lms_question_answer_set, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        println("tag_value_set $lastVideo")
        questionSerialPosition = 0

        iv_save_qstn_answr_set_next = view.findViewById(R.id.iv_save_qstn_answr_set_next)

        Glide.with(mContext)
            .load(R.drawable.icon_pointer_gif)
            .into(iv_save_qstn_answr_set_next)
        iv_save_qstn_answr_set_next.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        tv_save_qstn_answr_set_text = view.findViewById(R.id.tv_save_qstn_answr_set_text)
        tv_question = view.findViewById(R.id.tv_qa_question)
        tv_op1 = view.findViewById(R.id.tv_qa_op1)
        tv_op2 = view.findViewById(R.id.tv_qa_op2)
        tv_op3 = view.findViewById(R.id.tv_qa_op3)
        tv_op4 = view.findViewById(R.id.tv_qa_op4)

        card_op1 = view.findViewById(R.id.card_op1)
        card_op2 = view.findViewById(R.id.card_op2)
        card_op3 = view.findViewById(R.id.card_op3)
        card_op4 = view.findViewById(R.id.card_op4)

        iv_back = view.findViewById(R.id.iv_frag_lms_ques_back)
        progress_wheel = view.findViewById(R.id.progress_wheel_frag_qa)
        progress_wheel.stopSpinning()

        // Initialize adapters and set them to RecyclerViews
        //questionRecyclerView = view.findViewById(R.id.question_answer_recycler_view)
        tv_save_qstn_answr_set = view.findViewById(R.id.tv_save_qstn_answr_set)
        ll_parent_question_answer = view.findViewById(R.id.ll_parent_question_answer)
        //questionRecyclerView.layoutManager = LinearLayoutManager(mContext)

        /*val setUpQuestionAnswer = SetUpQuestionAnswer(
            listOf(
                QuestionForLms(
                    "Question-1",
                    "What is the total combined customer lifetime values of all of the company's customers?",
                    listOf(
                        AnswerOptionForLms("A. Customer equity", false),
                        AnswerOptionForLms("B. Customer-perceived value", false),
                        AnswerOptionForLms("C. Customer Lifetime value", false),
                        AnswerOptionForLms("D. Customer share", false)
                    )
                ),
                QuestionForLms(
                    "Question-2",
                    "Promotion mix includes Sales Promotion, Personal Selling, Advertising and",
                    listOf(
                        AnswerOptionForLms("A. Marketing", false),
                        AnswerOptionForLms("B. Sales", false),
                        AnswerOptionForLms("C. Publicity", false),
                        AnswerOptionForLms("D. None of these", false)
                    )
                )
            )
        )*/

        finalL = ArrayList()
        for(i in 0..questionlist.size-1){
            if(questionlist.get(i).option_list.size>0){
                finalL.add(questionlist.get(i))
            }
        }

        tv_save_qstn_answr_set.setOnClickListener {
            try {
                try {
                        var prevObj = finalL.get(questionSerialPosition-1)
                        var correct1 = prevObj.option_list.get(0).isCorrect_1
                        var correct2 = prevObj.option_list.get(0).isCorrect_2
                        var correct3 = prevObj.option_list.get(0).isCorrect_3
                        var correct4 = prevObj.option_list.get(0).isCorrect_4
                        var isCorrectAnsGiven = false

                        var points = 0
                        //var opSelection = 0
                        var correctAns=""
                        if(opSelection ==1 && correct1){
                            isCorrectAnsGiven = true
                            points = prevObj.option_list.get(0).option_point_1.toInt()
                            opSelection = 0
                        }else if(opSelection ==2 && correct2){
                            isCorrectAnsGiven = true
                            points = prevObj.option_list.get(0).option_point_2.toInt()
                            opSelection = 0
                        }else if(opSelection ==3 && correct3){
                            isCorrectAnsGiven = true
                            points = prevObj.option_list.get(0).option_point_3.toInt()
                            opSelection = 0
                        }else if(opSelection ==4 && correct4){
                            isCorrectAnsGiven = true
                            points = prevObj.option_list.get(0).option_point_4.toInt()
                            opSelection = 0
                        }
                    if(correct1){
                        correctAns = prevObj.option_list.get(0).option_no_1
                    }else if(correct2){
                        correctAns = prevObj.option_list.get(0).option_no_2
                    }else if(correct3){
                        correctAns = prevObj.option_list.get(0).option_no_3
                    }else if(correct4){
                        correctAns = prevObj.option_list.get(0).option_no_4
                    }
                        if(isCorrectAnsGiven) {
                            correct_count = correct_count + 1
                            total_points=total_points+points
                            println("correct_count"+correct_count)
                            println("total_points"+total_points)
                            showPopup(points)
                        }
                        else {
                            incorrect_count = incorrect_count + 1
                            println("incorrect_count"+incorrect_count)
                            showErrorPopup(correctAns, 0)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                /*var question = finalL.get(questionSerialPosition).question
                var option1 = finalL.get(questionSerialPosition).option_list.get(0).option_no_1
                var option2 = finalL.get(questionSerialPosition).option_list.get(0).option_no_2
                var option3 = finalL.get(questionSerialPosition).option_list.get(0).option_no_3
                var option4 = finalL.get(questionSerialPosition).option_list.get(0).option_no_4

                questionSerialPosition++
                println("sl_tag $questionSerialPosition")
                loadQuestionAns(question,option1,option2,option3,option4,if(questionSerialPosition==finalL.size) true else false)*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@setOnClickListener


            var selectedval: ArrayList<QuestionL> = QuestionAdapter.questionOptionsL
            QuestionAdapter.questionOptionsL = ArrayList()
            var pointsListval:Int = 0
            //val correct_sectedval = selectedval.filter { it.isCorrect==true && it.isSelected==true }

            //val pointsListval = correct_sectedval.sumBy { it.points }

            for(i in 0..selectedval.size-1){
                var isCorrect1 = selectedval.get(i).option_list.get(0).isCorrect_1
                var isCorrect2 = selectedval.get(i).option_list.get(0).isCorrect_2
                var isCorrect3 = selectedval.get(i).option_list.get(0).isCorrect_3
                var isCorrect4 = selectedval.get(i).option_list.get(0).isCorrect_4

                var isSelected1 = selectedval.get(i).option_list.get(0).isSelected_1
                var isSelected2 = selectedval.get(i).option_list.get(0).isSelected_2
                var isSelected3 = selectedval.get(i).option_list.get(0).isSelected_3
                var isSelected4 = selectedval.get(i).option_list.get(0).isSelected_4

                var points1 = selectedval.get(i).option_list.get(0).option_point_1
                var points2 = selectedval.get(i).option_list.get(0).option_point_2
                var points3 = selectedval.get(i).option_list.get(0).option_point_3
                var points4 = selectedval.get(i).option_list.get(0).option_point_4

                if(isCorrect1 && isSelected1){
                    pointsListval = pointsListval + points1.toInt()
                }
                if(isCorrect2 && isSelected2){
                    pointsListval = pointsListval + points2.toInt()
                }
                if(isCorrect3 && isSelected3){
                    pointsListval = pointsListval + points3.toInt()
                }
                if(isCorrect4 && isSelected4){
                    pointsListval = pointsListval + points4.toInt()
                }
            }

            val savedContentIds = SavedContentIds()
            savedContentIds.content_id.add(questionlist.get(0).content_id.toInt())


            val sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val contentIdsString = savedContentIds.content_id.joinToString(",")
            editor.putString("saved_content_ids", contentIdsString)
            editor.apply()
            if (pointsListval >0) {
                            //showPopup(view, pointsListval)
                        }
                        else{
                            //showErrorPopup(view, pointsListval)
                        }

        }




        //questionAdapter = QuestionAdapter(/*setUpQuestionAnswer*/ finalL)
        //questionRecyclerView.adapter = questionAdapter


        tv_op1.setOnClickListener(this)
        tv_op2.setOnClickListener(this)
        tv_op3.setOnClickListener(this)
        tv_op4.setOnClickListener(this)
        tv_save_qstn_answr_set.performClick()

        processloadQuestionAns()
        tv_save_qstn_answr_set.visibility = View.GONE
    }

    fun processloadQuestionAns(){
        tv_save_qstn_answr_set.visibility = View.GONE
        try {
            var question = finalL.get(questionSerialPosition).question
            var option1 = finalL.get(questionSerialPosition).option_list.get(0).option_no_1
            var option2 = finalL.get(questionSerialPosition).option_list.get(0).option_no_2
            var option3 = finalL.get(questionSerialPosition).option_list.get(0).option_no_3
            var option4 = finalL.get(questionSerialPosition).option_list.get(0).option_no_4

            questionSerialPosition++
            println("sl_tag $questionSerialPosition")

            if(questionSerialPosition==1)
                iv_back.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_quest))
            else if(questionSerialPosition==2)
                iv_back.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_quest1))
            else if(questionSerialPosition==3)
                iv_back.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_quest2))
            else if(questionSerialPosition==4)
                iv_back.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_quest3))
            else
                iv_back.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.img_quest))

            (mContext as DashboardActivity).setTopBarTitle("Quiz : "+questionSerialPosition+"/"+finalL.size)

            loadQuestionAns(question,option1,option2,option3,option4,if(questionSerialPosition==finalL.size) true else false)
            progress_wheel.stopSpinning()
        } catch (e: Exception) {
            e.printStackTrace()
            summury_popup()
        }
    }



    fun loadQuestionAns(question:String,op1:String,op2:String,op3:String,op4:String,isEnd:Boolean){
        try {
            if(isEnd){
                tv_save_qstn_answr_set_text.text = "Submit"
                iv_save_qstn_answr_set_next.visibility = View.GONE
                question_submit = true
                val question_submit_content_id = questionlist[0].content_id.toInt()
                saveContentId(mContext, question_submit_content_id)

            }else{
                //tv_save_qstn_answr_set_text.text = "Next"
                tv_save_qstn_answr_set_text.text = "Submit"
                iv_save_qstn_answr_set_next.visibility = View.VISIBLE
                question_submit = false
                question_submit_content_id=0
            }

            card_op1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
            card_op2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
            card_op3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
            card_op4.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))

            tv_question.text = question
            tv_op1.text = op1
            tv_op2.text = op2
            tv_op3.text = op3
            tv_op4.text = op4
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun saveArrayToSharedPreferences(context: Context, key: String, values: List<Int>) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val valueString = values.joinToString(",")
        editor.putString(key, valueString)
        editor.apply()
    }

    fun getArrayFromSharedPreferences(context: Context, key: String): List<Int> {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val valueString = sharedPreferences.getString(key, "")
        println("Stored Content IDs: $valueString")
        return if (valueString.isNullOrEmpty()) {
            emptyList()
        } else {
            valueString.split(",").map { it.toInt() }
        }
    }




    private fun saveContentId(mContext: Context, question_submit_content_id: Int) {

        val storedContentIds = getArrayFromSharedPreferences(mContext, "saved_content_ids")
        println("Stored Content IDs:::: $storedContentIds")
        // Retrieve the existing content IDs
        val existingContentIds = getArrayFromSharedPreferences(mContext, "saved_content_ids").toMutableList()

        // Get the new content ID
        val newContentId = question_submit_content_id

        // Add the new content ID to the list if it's not already present
        if (!existingContentIds.contains(newContentId)) {
            existingContentIds.add(newContentId)
        }

        // Save the updated list back to Shared Preferences
        saveArrayToSharedPreferences(mContext, "saved_content_ids", existingContentIds)
    }

    private fun showErrorPopup( correctAns:String,pointsListval: Int) {
        val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.error_popup_layout_congratulation, null)
        popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )
        val close_button: TextView = popupView.findViewById(R.id.close_button)
        val popup_image: LottieAnimationView = popupView.findViewById(R.id.popup_image)
        val popup_title: TextView = popupView.findViewById(R.id.popup_title)
        val popup_message: TextView = popupView.findViewById(R.id.popup_message)
        val popup_message_ans: TextView = popupView.findViewById(R.id.popup_message_ans)
        popup_title.setText("Oops!")
        var typeFace: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.remachinescript_personal_use)
        popup_title.setTypeface(typeFace)
        popup_message.setText("You get $pointsListval points. Correct answer is : ")
        popup_message_ans.text = correctAns
        close_button.setOnClickListener {
            processloadQuestionAns()
            progress_wheel.spin()
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
                popupWindow.dismiss()
            }, 400)
        }
        popup_image.visibility =View.VISIBLE
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = false
        popupWindow.isFocusable = false
        popupWindow.showAtLocation(ll_parent_question_answer, Gravity.CENTER, 0, 0)
    }

    private fun showPopup( pointsListval: Int) {
        val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_layout_congratulation, null)
        popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )
        val close_button: TextView = popupView.findViewById(R.id.close_button)
        val popup_image: LottieAnimationView = popupView.findViewById(R.id.popup_image)
        val popup_title: TextView = popupView.findViewById(R.id.popup_title)
        val popup_message: TextView = popupView.findViewById(R.id.popup_message)
        popup_title.setText("Congratulation "/*+Pref.user_name*/)
        var typeFace: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.remachinescript_personal_use)
        popup_title.setTypeface(typeFace)
        popup_message.setText("You get $pointsListval points")
        close_button.setOnClickListener {

            processloadQuestionAns()
            progress_wheel.spin()
            Handler().postDelayed(Runnable {
                progress_wheel.stopSpinning()
                popupWindow.dismiss()
            }, 400)

        }
        popup_image.visibility =View.VISIBLE
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = false
        popupWindow.isFocusable = false
        popupWindow.showAtLocation(ll_parent_question_answer, Gravity.CENTER, 0, 0)

    }

    private fun summury_popup() {

        val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_layout_summary, null)
        var popupWindowSummary = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )
        val close_button: TextView = popupView.findViewById(R.id.close_button)
        val popup_image: LottieAnimationView = popupView.findViewById(R.id.popup_image)
        val popup_title: TextView = popupView.findViewById(R.id.popup_title)
        val tv_total_no_qstn: TextView = popupView.findViewById(R.id.tv_total_no_qstn)
        tv_total_no_qstn.text = "Total number of question : "+finalL.size
        val tv_total_no_crrct: TextView = popupView.findViewById(R.id.tv_total_no_crrct)
        tv_total_no_crrct.text = "Total number of correct answer : "+correct_count
        val tv_total_no_incrrct: TextView = popupView.findViewById(R.id.tv_total_no_incrrct)
        tv_total_no_incrrct.text = "Total number of incorrect answer : "+incorrect_count
        val tv_total_points: TextView = popupView.findViewById(R.id.tv_total_points)
        tv_total_points.text = "You get total points : "+total_points

        var typeFace: Typeface? = ResourcesCompat.getFont(mContext, R.font.remachinescript_personal_use)
        popup_title.setTypeface(typeFace)

        close_button.setOnClickListener {

            popupWindowSummary.dismiss()

        }
        println("companionlastvideo"+lastvideo)
        popupWindowSummary.setOnDismissListener {
            try {
                Pref.videoCompleteCount = "0"
                for(i in 0..Pref.Question_After_No_Of_Content.toInt()-1){
                    VideoPlayLMS.sequenceQuestionL.removeAt(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (lastvideo==true){
                (mContext as DashboardActivity).onBackPressed()
                //(mContext as DashboardActivity).onBackPressed()
            }else {
                (mContext as DashboardActivity).onBackPressed()
            }
        }
        popup_image.visibility =View.VISIBLE
        popupWindowSummary.setBackgroundDrawable(ColorDrawable())
        popupWindowSummary.isOutsideTouchable = false
        popupWindowSummary.isFocusable = false
        popupWindowSummary.showAtLocation(ll_parent_question_answer, Gravity.CENTER, 0, 0)

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            tv_op1.id ->{
                opSelection=1
                tv_save_qstn_answr_set.visibility = View.VISIBLE
                //tv_op1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa1))
                //tv_op2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))

                card_op1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.tfe_color_primary))
                card_op2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op4.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))

                Glide.with(mContext)
                    .load(R.drawable.icon_pointer_gif)
                    .into(iv_save_qstn_answr_set_next)
                iv_save_qstn_answr_set_next.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }
            tv_op2.id ->{
                opSelection=2
                tv_save_qstn_answr_set.visibility = View.VISIBLE
                //tv_op1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa1))
                //tv_op3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))

                card_op1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.tfe_color_primary))
                card_op3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op4.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))

                Glide.with(mContext)
                    .load(R.drawable.icon_pointer_gif)
                    .into(iv_save_qstn_answr_set_next)
                iv_save_qstn_answr_set_next.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }
            tv_op3.id ->{
                opSelection=3
                tv_save_qstn_answr_set.visibility = View.VISIBLE
                //tv_op1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa1))
                //tv_op4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))

                card_op1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.tfe_color_primary))
                card_op4.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))

                Glide.with(mContext)
                    .load(R.drawable.icon_pointer_gif)
                    .into(iv_save_qstn_answr_set_next)
                iv_save_qstn_answr_set_next.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }
            tv_op4.id ->{
                opSelection=4
                tv_save_qstn_answr_set.visibility = View.VISIBLE
                //tv_op1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa))
                //tv_op4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_back_qa1))

                card_op1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.color_option))
                card_op4.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.tfe_color_primary))

                Glide.with(mContext)
                    .load(R.drawable.icon_pointer_gif)
                    .into(iv_save_qstn_answr_set_next)
                iv_save_qstn_answr_set_next.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }
        }
    }


}