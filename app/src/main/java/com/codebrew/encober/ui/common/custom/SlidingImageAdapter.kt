package com.codebrew.encober.ui.common.custom


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codebrew.encober.R
import com.codebrew.encober.models.responseModel.ImageUrl
import kotlinx.android.synthetic.main.item_image.view.*


class SlidingImageAdapter(private val context: Context,private val images: ArrayList<ImageUrl>) : PagerAdapter() {
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, container, false)

        Glide.with(context)
            .load(images[position].original)
            .apply(RequestOptions().placeholder(R.color.colorAccent))
            .into(view.ivItemImage)

        container.addView(view)

        return view
    }

}