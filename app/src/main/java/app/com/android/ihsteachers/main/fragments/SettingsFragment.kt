package app.com.android.ihsteachers.main.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.widget.Button
import android.widget.TextView

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.ContactAdminActivity
import app.com.android.ihsteachers.main.activity.EditProfileActivity
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import android.support.v4.view.MenuItemCompat
import android.view.*
import android.widget.ProgressBar
import app.com.android.ihsteachers.login.activity.LoginActivity
import app.com.android.ihsteachers.main.activity.ChangePasswordActivity
import app.com.android.ihsteachers.providers.PreferencesHelper.countVal


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


class SettingsFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {


    val CUSTOM_PREF_NAME = "UserID"
    val CUSTOM_PREF_COUNT = "CountVal"
    private var param1: String? = null

    var fullname: TextView? = null
    var username: TextView? = null
    var phonenum: TextView? = null
    var email: TextView? = null
    var gender: TextView? = null

    var msgBadgeText: TextView? = null

    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        progressBar = view.findViewById(R.id.progressBarSettings)
        progressBar!!.visibility = View.VISIBLE

        val ctx: Context = this.requireContext()

        val navigationView: NavigationView = view.findViewById(R.id.nav_settings)
        val headerView: View = navigationView.getHeaderView(0)
        val btnGoToEditProfile: Button = headerView.findViewById(R.id.btnGoToEditProfile)

        fullname = headerView.findViewById(R.id.txtProfileFullname)
        username = headerView.findViewById(R.id.txtProfileUserName)
        phonenum = headerView.findViewById(R.id.txtProfileMobile)
        email = headerView.findViewById(R.id.txtProfileEmail)
        gender = headerView.findViewById(R.id.txtGender)

        msgBadgeText = MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.nav_contact_admin)) as TextView?


        loadSettingsItems(ctx)

        btnGoToEditProfile.setOnClickListener {

            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra("PROFILE_USERNAME", username!!.text)
            intent.putExtra("PROFILE_FNAME", fullname!!.text.split("\\s".toRegex())[0])
            intent.putExtra("PROFILE_LNAME", fullname!!.text.split("\\s".toRegex())[1])
            intent.putExtra("PROFILE_EMAIL", email!!.text)
            intent.putExtra("PROFILE_PHONENUM", phonenum!!.text)
            intent.putExtra("PROFILE_GENDER", gender!!.text)
            activity!!.startActivity(intent)
        }

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this)

        initializeCountDrawer()

        return view
    }

    private fun loadSettingsItems(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getProfile.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        params.put("username", userPref)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE
            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val settingsObj = obj.getJSONObject("result")

                username!!.text = settingsObj.getString("username")
                fullname!!.text = settingsObj.getString("firstname") + " " + settingsObj.getString("lastname")
                email!!.text = settingsObj.getString("email")
                phonenum!!.text = settingsObj.getString("phoneno")
                gender!!.text = settingsObj.getString("gender")

            }


        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_contact_admin -> {
                val intent = Intent(activity, ContactAdminActivity::class.java)
                activity!!.startActivity(intent)

                val prefs = PreferencesHelper.customPreference(this.requireContext(), CUSTOM_PREF_COUNT)
                prefs.countVal = 0
            }

            R.id.nav_change_password -> {
                val intent = Intent(activity, ChangePasswordActivity::class.java)
                activity!!.startActivity(intent)
            }

            R.id.nav_logout -> {
                activity!!.finishAffinity()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

        return true
    }

    private fun initializeCountDrawer() {
        msgBadgeText!!.gravity = Gravity.CENTER_VERTICAL
        msgBadgeText!!.setTypeface(null, Typeface.BOLD)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            msgBadgeText!!.setTextColor(activity!!.resources.getColor(R.color.colorAccent, null))
        }
        else {
            msgBadgeText!!.setTextColor(activity!!.resources.getColor(R.color.colorAccent))
        }

        val prefs = PreferencesHelper.customPreference(this.requireContext(), CUSTOM_PREF_COUNT)
        val countValue = prefs.countVal

        if(countValue == 0) {
            msgBadgeText!!.text = ""
        }
        else {
            msgBadgeText!!.text = countValue.toString()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
