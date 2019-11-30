package com.codebrew.encober.ui.splash.adScreen

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.codebrew.encober.R
import kotlinx.android.synthetic.main.item_ad_item.view.*

class DemoScreenViewPager(private val context: Context) :  androidx.viewpager.widget.PagerAdapter() {

    override fun getCount(): Int = 2


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ad_item, container, false)

        if (position == 0) {
            view.tvAdItem.text = boldSpanning(context.getString(R.string.generate_extra_income_label_adscreen), context.getString(
                            R.string.generate_bold_label_adscreen))
            view.ivAdItem.setImageResource(R.drawable.img_encober_ad_1)
        } else {
            view.tvAdItem.text = boldSpanning(context.getString(R.string.you_set_the_schedule_label_adscreen), context.getString(
                            R.string.you_set_bold_label_adscreen))
            view.ivAdItem.setImageResource(R.drawable.img_encober_ad_2)

        }

        container.addView(view)
        return view
    }

    private fun boldSpanning(fullStr: String, boldStr: String): SpannableString {
        val spanStr = SpannableString(fullStr)
        spanStr.setSpan(
            StyleSpan(Typeface.BOLD), spanStr.indexOf(boldStr),
            spanStr.indexOf(boldStr) + boldStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanStr
    }

}