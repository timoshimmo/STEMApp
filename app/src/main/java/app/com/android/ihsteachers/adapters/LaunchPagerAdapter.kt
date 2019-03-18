package app.com.android.ihsteachers.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import app.com.android.ihsteachers.welcome.fragment.FragmentOne
import app.com.android.ihsteachers.welcome.fragment.FragmentThree
import app.com.android.ihsteachers.welcome.fragment.FragmentTwo

class LaunchPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentOne()
            1 -> fragment = FragmentTwo()
            2 -> fragment = FragmentThree()
        }

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}