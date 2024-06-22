package com.dewa.sea.admin.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dewa.sea.R
import com.dewa.sea.admin.ui.reservation.AccAdminFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_admin_1,
    R.string.tab_admin_2,
    R.string.tab_admin_3
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> AccAdminFragment()
            1 -> AccAdminFragment()
            2 -> AccAdminFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}