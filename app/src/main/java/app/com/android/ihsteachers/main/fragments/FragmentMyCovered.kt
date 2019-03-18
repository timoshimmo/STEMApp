package app.com.android.ihsteachers.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.CoveredSoFarActivity
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
 * Use the [FragmentMyCovered.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentMyCovered : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val CUSTOM_PREF_NAME = "UserID"

    var progressBar: ProgressBar? = null
    var resultSmiley: ImageView? = null

    var progressReportView: TextView? = null
    var resultView: TextView? = null

    var progressBarAchievement: ProgressBar? = null

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
        val view = inflater.inflate(R.layout.fragment_my_covered, container, false)
        progressBar = view.findViewById(R.id.progressBarCoveredSubjects)
        resultSmiley = view.findViewById(R.id.imgCoveredSmiley)
        progressReportView = view.findViewById(R.id.txtProgressReport)
        resultView = view.findViewById(R.id.txtPercentValue)
        progressBarAchievement = view.findViewById(R.id.progressBarRateCovered)
        val btnMyCovered: FloatingActionButton = view.findViewById(R.id.fbCovered)

        progressBar!!.visibility = View.VISIBLE

        loadCovered(this.requireContext())

        btnMyCovered.setOnClickListener {
            val intent = Intent(this.requireContext(), CoveredSoFarActivity::class.java)
            this.requireActivity().startActivity(intent)
            this.requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
        }

        return view

    }

    private fun loadCovered(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getCovered.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("teacherid", userId)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")
                val resCovered: Int = arrayAll.length()

                loadAllCourses(ctx, resCovered)

            }
        }
    }

    private fun loadAllCourses(ctx: Context, resCovered: Int) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getUserClasses.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("teacherid", userId)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                progressBar!!.visibility = View.GONE

                val arrayAll = obj.getJSONArray("result")
                val resAll:Double = arrayAll.length().toDouble()
                val resFractiion = resCovered / resAll
                val resPercentageVal = resFractiion * 100

                val rndResult = Math.round(resPercentageVal).toInt()

                resultView!!.text = rndResult.toString()+"%"
                progressBarAchievement!!.progress = rndResult

                if(resCovered > 5) {

                    if(resPercentageVal < 60) {
                        progressReportView!!.text = "Not good enough"
                    }
                    else {
                        progressReportView!!.text = "Progress is good"
                    }
                }
                else {
                    progressReportView!!.text = "Progress is good"
                }

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
         * @return A new instance of fragment FragmentMyCovered.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragmentMyCovered().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
