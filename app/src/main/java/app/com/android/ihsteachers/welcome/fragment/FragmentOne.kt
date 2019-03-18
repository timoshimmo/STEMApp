package app.com.android.ihsteachers.welcome.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgOne
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentOne : Fragment() {

    val CUSTOM_PREF_BKGONE = "BKGIMG_ONE"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_one, container, false)

        val imgBackground : ImageView = view.findViewById(R.id.imgLaunchOneBack)

        val prefs = customPreference(this.requireContext(), CUSTOM_PREF_BKGONE)
        val bkgImgFirst = prefs.bkgImgOne

        Picasso.get()
                .load(bkgImgFirst)
                .into(imgBackground)

        return view
    }


}
