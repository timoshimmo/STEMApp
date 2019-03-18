package app.com.android.ihsteachers.main.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.CoveredAdapter
import app.com.android.ihsteachers.models.CoveredClassesModel
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject

class CoveredSoFarActivity : AppCompatActivity() {

   // private var classDetailList: List<ClassDetailsItem>? = null
    private var timeTableAllList: MutableList<CoveredClassesModel> = mutableListOf()

    lateinit var rvTimeTable: RecyclerView
    val CUSTOM_PREF_NAME = "UserID"
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covered_so_far)

        progressBar = findViewById(R.id.progressBarCovered)
        progressBar!!.visibility = View.VISIBLE

        rvTimeTable = findViewById(R.id.rvCoveredList)

        loadTimeTableItems(applicationContext)
    }

    private val myBackgroundList : List<Int> = listOf (
            R.drawable.light_green_gradient_curve,
            R.drawable.light_blue_gradient_curve,
            R.drawable.fiery_rose_gradient_curve,
            R.drawable.blue_cerulean_gradient_curve
    )

    private fun loadTimeTableItems(ctx: Context) {

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

                progressBar!!.visibility = View.GONE

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)
                   // val classData = allData.getJSONObject("class")

                  /*  classDetailList = listOf(ClassDetailsItem(classData.getString("duration"),
                            classData.getString("subject"), classData.getString("topicId"), classData.getString("className"),
                            myBackgroundList.shuffled().take(1)[0]))

                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val convertedDate: Date = formatter.parse(allData.getString("DayofWeek"))

                    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
                    val dayOfTheWeek = sdf.format(convertedDate) */

                    val timeTableData = CoveredClassesModel(i.toString(), allData.getString("term"),
                            allData.getString("subject"), allData.getString("topicId"), allData.getString("className"),
                            myBackgroundList.shuffled().take(1)[0])

                    timeTableAllList.add(i, timeTableData)

                }

                rvTimeTable.layoutManager = LinearLayoutManager(this.applicationContext)
                rvTimeTable.adapter = CoveredAdapter(timeTableAllList)

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
    }

}
