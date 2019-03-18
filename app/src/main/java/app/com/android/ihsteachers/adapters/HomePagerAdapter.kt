package app.com.android.ihsteachers.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import app.com.android.ihsteachers.main.fragments.FragmentAssigned
import app.com.android.ihsteachers.main.fragments.FragmentMyClasses
import app.com.android.ihsteachers.main.fragments.FragmentMyCovered

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentAssigned.newInstance("", "")
            1 -> fragment = FragmentMyClasses.newInstance("", "")
            2 -> fragment = FragmentMyCovered.newInstance("", "")
        }

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}