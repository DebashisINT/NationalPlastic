package com.breezefieldnationalplastic

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDayDecorator1() : DayViewDecorator {

    private var highlightDrawable: Drawable? = null
    private var context: Context? = null
    private var dates: HashSet<CalendarDay>? = null

    constructor(context: Context?) : this() {
        this.context = context
        highlightDrawable = this.context?.getResources()?.getDrawable(R.drawable.circle_red)
    }

    constructor(mContext: Context?, mEventDays: ArrayList<CalendarDay>) : this() {
        this.context = mContext
        highlightDrawable = this.context?.getResources()?.getDrawable(R.drawable.circle_red)
        dates = HashSet(mEventDays)

    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates?.contains(day) == true
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(highlightDrawable!!)
        view.addSpan(ForegroundColorSpan(Color.WHITE))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RelativeSizeSpan(1.1f))
    }
}