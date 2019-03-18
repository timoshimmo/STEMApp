package app.com.android.ihsteachers.main.activity

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.ClassesAdapter
import app.com.android.ihsteachers.models.ClassesItem
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import java.util.ArrayList

class ClassesActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "UserID"
    private lateinit var rvListClass: RecyclerView
    private var classList: MutableList<ClassesItem> = ArrayList()

    var progressBar: ProgressBar? = null
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classes)

        progressBar = findViewById(R.id.progressBarClasses)
        progressBar!!.visibility = View.VISIBLE

        rvListClass = findViewById(R.id.listClasses)

        val ctx: Context = applicationContext

        loadClasses(ctx, this)
    }

    private val myBackgroundList : List<Int> = listOf (
            R.drawable.light_green_gradient_curve,
            R.drawable.light_blue_gradient_curve,
            R.drawable.fiery_rose_gradient_curve,
            R.drawable.blue_cerulean_gradient_curve
    )


    private fun loadClasses(ctx: Context, mActivity: Activity) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getClassList.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        params.put("teacherid", userPref)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE
            classList.clear()

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)
                    val count: Int = i + 1
                    val homeData = ClassesItem(count.toString(), allData.getString("class"), myBackgroundList.shuffled().take(1)[0])

                    classList.add(homeData)

                }

                with(rvListClass) {
                    layoutManager = when {
                        columnCount <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                        else -> android.support.v7.widget.GridLayoutManager(context, columnCount)
                    }
                    adapter = ClassesAdapter(classList, ctx, mActivity)
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
    }

}
