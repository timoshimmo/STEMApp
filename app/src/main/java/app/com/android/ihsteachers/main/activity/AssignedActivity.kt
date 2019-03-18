package app.com.android.ihsteachers.main.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.AssignedAdapter
import app.com.android.ihsteachers.main.dialogs.TimeTableDialogFragment
import app.com.android.ihsteachers.models.AssignedContent
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_assigned.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AssignedActivity : AppCompatActivity(), TimeTableDialogFragment.OnTimeTableDialogFragmentInteractionListener {


    override fun onTimeTableDialogFragmentInteraction(item: Int?) {

    }

    lateinit var rvTermsTable: RecyclerView
    private var columnCount = 1
    private val assignedList: MutableList<AssignedContent.AssignedItem> = ArrayList()

    var progressBar: ProgressBar? = null
    var sessionYear: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned)

        val bundle = intent.extras
        var strData: Array<String>? = null
        var inteData: IntArray? = null
        if(bundle!=null)
        {
            strData = bundle.getStringArray("SELECTED_STRING_DATA")
            inteData = bundle.getIntArray("SELECTED_INT_DATA")
        }

        val ctx: Context = applicationContext

        sessionYear = findViewById(R.id.txtAcademicSessionYear)
        progressBar = findViewById(R.id.assignedProgressBar)

        txtTitle.text = strData!![0]
        txtContent.text = strData[1]
        txtSubContent.text = strData[2]
        imgHomeRowIcon.setImageDrawable(ContextCompat.getDrawable(this, inteData!![0]))
        colorBackground.setBackgroundResource(inteData[1])

        progressBar!!.visibility = View.VISIBLE

        rvTermsTable = findViewById(R.id.rvTerms)

        loadTerms()

    }

    private fun loadTerms() {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getTerms.php"
        val params = JSONObject()

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            progressBar!!.visibility = View.GONE
            assignedList.clear()

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                val contentValues: Array<String> = arrayOf("Ongoing Term", "Next Term", "Final Term")
                val termsIcons: Array<Int> = arrayOf(R.drawable.read_hand_book, R.drawable.computer, R.drawable.grad_cert)

                sessionYear!!.text = arrayAll.getJSONObject(0).getString("session")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)
                    val count: Int = i + 1

                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val convertedStartDate: Date = formatter.parse(allData.getString("start"))

                    val sdf = SimpleDateFormat("MMM d")
                    val newStartDate = sdf.format(convertedStartDate)

                  //  val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val convertedEndDate: Date = formatter.parse(allData.getString("end"))

                    val sdff = SimpleDateFormat("MMM d")
                    val newEndDate = sdff.format(convertedEndDate)

                    val assignedData = AssignedContent.AssignedItem(count.toString(), allData.getString("name"),
                            contentValues[i], resources.getString(R.string.str_term_duration, newStartDate, newEndDate), termsIcons[i])

                    assignedList.add(assignedData)

                }

                with(rvTermsTable) {
                    layoutManager = when {
                        columnCount <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                        else -> android.support.v7.widget.GridLayoutManager(context, columnCount)
                    }
                    adapter = AssignedAdapter(assignedList, applicationContext, this@AssignedActivity,
                            txtContent.text.toString(), txtSubContent.text.toString())
                }

            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }
}
