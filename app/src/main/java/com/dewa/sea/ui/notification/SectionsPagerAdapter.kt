package com.dewa.sea.ui.notification

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dewa.sea.R
import com.dewa.sea.ui.notification.history.HistoryFragment
import com.dewa.sea.ui.notification.reservation.ReservationFragment
import com.dewa.sea.ui.notification.review.ReviewFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> ReservationFragment()
            1 -> ReviewFragment()
            2 -> HistoryFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}