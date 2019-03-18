package app.com.android.ihsteachers.welcome.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.LaunchPagerAdapter
import com.viewpagerindicator.CirclePageIndicator

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var cp: CirclePageIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        viewPager = findViewById(R.id.launch_pager)

        val pagerAdapter = LaunchPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        cp = findViewById(R.id.indicator)
        cp.setViewPager(viewPager)

    }
}
