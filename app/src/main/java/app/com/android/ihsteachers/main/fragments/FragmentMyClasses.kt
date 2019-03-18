package app.com.android.ihsteachers.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.ClassesActivity
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMyClasses.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentMyClasses : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val CUSTOM_PREF_NAME = "UserID"
    var progressBar: ProgressBar? = null
    var resultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_classes, container, false)

        progressBar = view.findViewById(R.id.progressBarHomeMyClasses)
        resultView = view.findViewById(R.id.txtMyClassesResult)

        progressBar!!.visibility = View.VISIBLE

        val btnMyClasses: FloatingActionButton = view.findViewById(R.id.fbMyClasses)
        btnMyClasses.setOnClickListener {
            val intent = Intent(this.requireContext(), ClassesActivity::class.java)
            this.requireActivity().startActivity(intent)
            this.requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
        }

        loadClasses(this.requireContext())

        return view
    }

    private fun loadClasses(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getClassList.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        params.put("teacherid", userPref)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")
                resultView!!.text = resources.getString(R.string.str_my_class_result, arrayAll.length().toString(), "Classes")

            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMyClasses.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragmentMyClasses().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
