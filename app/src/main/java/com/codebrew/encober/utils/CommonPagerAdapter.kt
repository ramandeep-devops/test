package com.codebrew.encober.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CommonPagerAdapter : FragmentStatePagerAdapter {
    private var fragmentsList: List<Fragment>? = null
    private var titleList:List<String> = ArrayList()

    constructor(fm: FragmentManager, fragmentsList: List<Fragment>) : super(fm) {
        this.fragmentsList = fragmentsList
    }

    constructor(fm: FragmentManager, fragmentsList: List<Fragment>, titleList: List<String>) : super(fm) {
        this.fragmentsList = fragmentsList
        this.titleList = titleList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentsList!![position]
    }

    override fun getCount(): Int {
        return fragmentsList!!.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return if (titleList.isNotEmpty())
            titleList[position]
        else
            ""
    }


}