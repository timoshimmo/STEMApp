package app.com.android.ihsteachers.main.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.fragments.HomeFragment
import app.com.android.ihsteachers.main.fragments.SettingsFragment
import app.com.android.ihsteachers.main.fragments.TimeTableFragment
import android.util.Log
import app.com.android.ihsteachers.main.fragments.NotificationsFragment
import app.com.android.ihsteachers.providers.PreferencesHelper
import com.google.firebase.iid.FirebaseInstanceId
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.providers.PreferencesHelper.countVal
import app.com.android.ihsteachers.providers.PreferencesHelper.countNotification
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.MenuItem
import app.com.android.ihsteachers.providers.BadgeDrawable


class MainActivity : AppCompatActivity(), HomeFragment.OnListFragmentInteractionListener,
        TimeTableFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentNotificationsInteractionListener {

    val CUSTOM_PREF_NAME = "UserID"
    val CUSTOM_PREF_COUNT = "CountVal"
    val CUSTOM_PREF_COUNT_NOTIFICATION = "CountNotification"
    var bottomNavBar: BottomNavigationView? = null

    override fun onTimeTableFragmentInteraction(item: Int?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val newToken: String = it.token

            Log.v("SUCCESS NEW TOKEN", newToken)
            loadNotificationId(newToken)
        }

        bottomNavBar = findViewById(R.id.bottomNavBar)

        val bundle = intent.extras
        if(bundle!=null)
        {

            val message = bundle.getString("message")

            if(message.isNotEmpty()) {

                val prefsNotify = PreferencesHelper.customPreference(this.baseContext, CUSTOM_PREF_COUNT_NOTIFICATION)
                val countNtf = prefsNotify.countNotification

                val cntNotify: Int = countNtf + 1

                prefsNotify.countNotification = cntNotify

                val notificationItem: MenuItem = bottomNavBar!!.menu.findItem(R.id.nav_notifications)
                val bdgIcon: LayerDrawable = notificationItem.icon as LayerDrawable
                if(cntNotify > 0) {
                    setBadgeCount(this, bdgIcon, cntNotify.toString())
                }

                /* bottomNavBar!!.selectedItemId = R.id.nav_notifications
                 supportActionBar!!.title = "Notifications"
                 replaceFragment(NotificationsFragment.newInstance(1)) */

             }

        }

        else {
            bottomNavBar!!.menu.getItem(0).isChecked = true
            replaceFragment(HomeFragment.newInstance(1))
        }


        bottomNavBar!!.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {

                R.id.nav_home -> {

                    replaceFragment(HomeFragment.newInstance(1))
                    return@setOnNavigationItemSelectedListener true

                }

                R.id.nav_time_table -> {

                    replaceFragment(TimeTableFragment.newInstance(1, "", "", ""))
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_notifications -> {

                    replaceFragment(NotificationsFragment.newInstance(1))

                    val prefsNotify = PreferencesHelper.customPreference(this.baseContext, CUSTOM_PREF_COUNT_NOTIFICATION)
                    prefsNotify.countNotification = 0

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_profile_settings -> {
                    replaceFragment(SettingsFragment.newInstance("Profile Settings"))
                    return@setOnNavigationItemSelectedListener true
                }

            }

            false
        }

        val prefsNotify = PreferencesHelper.customPreference(this.baseContext, CUSTOM_PREF_COUNT_NOTIFICATION)
        val countNtf = prefsNotify.countNotification

        val cntNotify: Int = countNtf

        val notificationItem: MenuItem = bottomNavBar!!.menu.findItem(R.id.nav_notifications)
        val bdgIcon: LayerDrawable = notificationItem.icon as LayerDrawable
        if(cntNotify > 0) {
            setBadgeCount(this, bdgIcon, cntNotify.toString())
        }


    }

    override fun onFragmentNotificationInteraction(type: String) {

        if(type == "ASSIGNED") {
            replaceFragment(HomeFragment.newInstance(1))
            bottomNavBar!!.selectedItemId = R.id.nav_home

        }
        if(type == "CONTENT") {
            replaceFragment(TimeTableFragment.newInstance(1, "", "", ""))
            bottomNavBar!!.selectedItemId = R.id.nav_time_table

        }

        if(type == "MESSAGE") {

            val prefs = PreferencesHelper.customPreference(this.baseContext, CUSTOM_PREF_COUNT)
            val countValue = prefs.countVal

            val myCount: Int = countValue + 1
            prefs.countVal = myCount

            replaceFragment(SettingsFragment.newInstance("Profile Settings"))
            bottomNavBar!!.selectedItemId = R.id.nav_profile_settings

        }

    }


    private fun loadNotificationId(nid: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "insertRegId.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(this.baseContext, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        params.put("userid", userPref)
        params.put("notificationid", nid)

        Log.v("RegId Values", "$userPref - $nid")

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1 && obj.getString("msg").equals("Update Successful!")) {
                Log.v("RegId Status:", obj.getString("msg"))

            }
            else {
                Log.v("RegId Status:", obj.getString("msg"))
            }
        }
    }

    override fun onListFragmentInteraction() {

    }

    override fun onBackPressed() {
       // super.onBackPressed()
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }


    private fun replaceFragment(frag: Fragment) {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit()
    }


    private fun setBadgeCount(context: Context, icon: LayerDrawable, count: String) {

        val badge: BadgeDrawable

        // Reuse drawable if possible
        val reuse: Drawable = icon.findDrawableByLayerId(R.id.ic_badge)
        badge = reuse as? BadgeDrawable ?: BadgeDrawable(context)

        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_badge, badge)
    }

 /*   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        supportFragmentManager.putFragment(outState, "TIMETABLEVIEW", TimeTableFragment.newInstance(1, "", "", ""))
        supportFragmentManager.putFragment(outState, "NOTIFICATIONVIEW", NotificationsFragment.newInstance(1))
    } */

}
