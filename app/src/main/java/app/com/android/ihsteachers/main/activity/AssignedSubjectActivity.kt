package app.com.android.ihsteachers.main.activity

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.MyhomeRecyclerViewAdapter
import app.com.android.ihsteachers.models.HomeContent
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import java.util.ArrayList

class AssignedSubjectActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "UserID"
    private lateinit var rvListHome: RecyclerView
    private var homeList: MutableList<HomeContent.HomeItem> = ArrayList()

    var progressBar: ProgressBar? = null
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_subject)

        progressBar = findViewById(R.id.progressBarHome)
        progressBar!!.visibility = View.VISIBLE

        rvListHome = findViewById(R.id.list)

        val ctx: Context = applicationContext

        loadAssignedSubjects(ctx, this)

    }

    private val myBackgroundList : List<Int> = listOf (
            R.drawable.light_green_gradient_curve,
            R.drawable.light_blue_gradient_curve,
            R.drawable.fiery_rose_gradient_curve,
            R.drawable.blue_cerulean_gradient_curve
    )

    private fun loadAssignedSubjects(ctx: Context, mActivity: Activity) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getAssigned.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        params.put("teacherid", userPref)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE
            homeList.clear()

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)
                    val count: Int = i + 1
                    val homeData = HomeContent.HomeItem("ASGN", "ASSIGNED SUBJECT" + " " + count.toString(), allData.getString("subject"), allData
                            .getString("classes"), "", R.drawable.ic_appointment, myBackgroundList.shuffled().take(1)[0])

                    homeList.add(homeData)

                }

                with(rvListHome) {
                    layoutManager = when {
                        columnCount <= 1 -> LinearLayoutManager(context)
                        else -> GridLayoutManager(context, columnCount)
                    }
                    adapter = MyhomeRecyclerViewAdapter(homeList, applicationContext , mActivity)
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
    }

}
