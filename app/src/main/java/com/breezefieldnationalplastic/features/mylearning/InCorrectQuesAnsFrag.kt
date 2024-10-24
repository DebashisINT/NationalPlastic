package com.breezefieldnationalplastic.features.mylearning

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldnationalplastic.R
import com.breezefieldnationalplastic.base.presentation.BaseFragment

class InCorrectQuesAnsFrag : BaseFragment() , View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RetryInCorrectQuestionAnswerAdapter
    private lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_in_correct_ques_ans, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.rv_incorrect_answer_tab)
        recyclerView.layoutManager = LinearLayoutManager(mContext)

/*        val sampleQuestionAnswers = listOf(
            CorrectQuestionAnswer(
                questionId = 1,
                question = "What is a major factor driving changes in consumer behavior in the CPG market?",
                optionList = listOf(
                    Option(
                        optionId = 1,
                        optionNo1 = "Decreased product availability",
                        optionPoint1 = 0,
                        isCorrect1 = false,
                        optionNo2 = "Increased health consciousness",
                        optionPoint2 = 5,
                        isCorrect2 = true,
                        optionNo3 = "Reduced marketing efforts",
                        optionPoint3 = 0,
                        isCorrect3 = false,
                        optionNo4 = "Limited access to information",
                        optionPoint4 = 0,
                        isCorrect4 = false
                    )
                )
            ),
            CorrectQuestionAnswer(
                questionId = 2,
                question = "Which of the following is a common trend in the CPG industry?",
                optionList = listOf(
                    Option(
                        optionId = 2,
                        optionNo1 = "Increased online shopping",
                        optionPoint1 = 5,
                        isCorrect1 = true,
                        optionNo2 = "Decreased product variety",
                        optionPoint2 = 0,
                        isCorrect2 = false,
                        optionNo3 = "Reduced consumer loyalty",
                        optionPoint3 = 0,
                        isCorrect3 = false,
                        optionNo4 = "Higher prices across the board",
                        optionPoint4 = 0,
                        isCorrect4 = false
                    )
                )
            ),
            CorrectQuestionAnswer(
                questionId = 3,
                question = "What influences consumer decisions the most?",
                optionList = listOf(
                    Option(
                        optionId = 3,
                        optionNo1 = "Brand reputation",
                        optionPoint1 = 5,
                        isCorrect1 = true,
                        optionNo2 = "Product packaging",
                        optionPoint2 = 0,
                        isCorrect2 = false,
                        optionNo3 = "Celebrity endorsements",
                        optionPoint3 = 0,
                        isCorrect3 = false,
                        optionNo4 = "Store location",
                        optionPoint4 = 0,
                        isCorrect4 = false
                    )
                )
            ),
            CorrectQuestionAnswer(
                questionId = 4,
                question = "What is a key driver of sustainability in the CPG sector?",
                optionList = listOf(
                    Option(
                        optionId = 4,
                        optionNo1 = "Use of biodegradable packaging",
                        optionPoint1 = 5,
                        isCorrect1 = true,
                        optionNo2 = "Increased advertising",
                        optionPoint2 = 0,
                        isCorrect2 = false,
                        optionNo3 = "Higher production costs",
                        optionPoint3 = 0,
                        isCorrect3 = false,
                        optionNo4 = "Limited product range",
                        optionPoint4 = 0,
                        isCorrect4 = false
                    )
                )
            ),
            CorrectQuestionAnswer(
                questionId = 5,
                question = "What is a significant factor affecting pricing in the CPG market?",
                optionList = listOf(
                    Option(
                        optionId = 5,
                        optionNo1 = "Supply chain efficiency",
                        optionPoint1 = 5,
                        isCorrect1 = true,
                        optionNo2 = "Consumer age",
                        optionPoint2 = 0,
                        isCorrect2 = false,
                        optionNo3 = "Store layout",
                        optionPoint3 = 0,
                        isCorrect3 = false,
                        optionNo4 = "Brand color",
                        optionPoint4 = 0,
                        isCorrect4 = false
                    )
                )
            )
        )
        adapter = RetryInCorrectQuestionAnswerAdapter(questionAnswers) { position ->
            // Handle the retry action here, e.g., show a Toast
            Toast.makeText(mContext, "Retry clicked for question ${position + 1}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter*/
    }


    override fun onClick(v: View?) {


    }
}