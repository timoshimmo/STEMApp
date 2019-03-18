package app.com.android.ihsteachers.welcome.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.login.activity.LoginActivity
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgThree
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentThree : Fragment() {

    val CUSTOM_PREF_BKGTHREE = "BKGIMG_THREE"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_three, container, false)

        val btnGoToLogin: Button = view.findViewById(R.id.btnGoToLogin)
        val imgBackground : ImageView = view.findViewById(R.id.imgLaunchThreeBack)

        val prefs = customPreference(this.requireContext(), CUSTOM_PREF_BKGTHREE)
        val bkgImgSecond = prefs.bkgImgThree

        Picasso.get()
                .load(bkgImgSecond)
                .into(imgBackground)

        btnGoToLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finish()

        }
        return view
    }


}
