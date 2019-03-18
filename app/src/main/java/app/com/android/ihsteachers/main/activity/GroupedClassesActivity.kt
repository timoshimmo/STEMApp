package app.com.android.ihsteachers.main.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.adapters.TimeTableModelRecyclerView
import app.com.android.ihsteachers.models.PostDiffUtilCallBack
import app.com.android.ihsteachers.models.TimeTableItem
import app.com.android.ihsteachers.models.TimeTableViewModel
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_grouped_classes.*
import org.json.JSONObject

class GroupedClassesActivity : AppCompatActivity() {

    private var columnCount = 1

    lateinit var rvTimeTable: RecyclerView

    private var subjectsList: MutableList<String> = mutableListOf()
    private var classList: MutableList<String> = mutableListOf()
    private val termsList = arrayOf("Term", "1st Term", "2nd Term", "3rd Term")

    val CUSTOM_PREF_NAME = "UserID"


    var progressBar: ProgressBar? = null
    var spinnerSubjects: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grouped_classes)

        progressBar = findViewById(R.id.progressBarTimeTable)
        progressBar!!.visibility = View.VISIBLE

        rvTimeTable = findViewById(R.id.rvTimeTableList)
        spinnerSubjects = findViewById(R.id.spnFilterSubjects)
        val spinnerSchoolTerms: Spinner = findViewById(R.id.spnFilterTerm)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val viewModel = ViewModelProviders.of(this).get(TimeTableViewModel::class.java)

        val bundle = intent.extras
        if(bundle!=null)
        {

            val strTerm = bundle.getString("SELECTED_TERM")
            val strSubject = bundle.getString("SELECTED_SUBJECT")
            val strClass = bundle.getString("SELECTED_CLASS")

            if(bundle.getString("SELECTED_CLASS_HOME").isNotEmpty() || bundle.getString("SELECTED_CLASS_HOME") != "") {

                val strClassHome = bundle.getString("SELECTED_CLASS_HOME")
                txtClassGroupTitle.text = strClassHome

                val tAdapter = ArrayAdapter(this, R.layout.spinner_text, termsList)
                tAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                spinnerSchoolTerms.adapter = tAdapter

                loadSubjects(this, "", this)
                //    loadClasses(this, "", this)

                viewModel.getTimeTable(this, strClassHome).observe(this, Observer<MutableList<TimeTableItem>> {
                    with(rvTimeTable) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = TimeTableModelRecyclerView(this@GroupedClassesActivity, viewModel.oldFilteredPosts, this@GroupedClassesActivity)
                    }
                    progressBar!!.visibility = View.GONE

                })

            }
            else {

                if(strClass != "" && strSubject != "") {

                    txtClassGroupTitle.text = strClass
                    loadSubjects(this, strSubject!!, this)

                    val tAdapter = ArrayAdapter(this, R.layout.spinner_text, termsList)
                    tAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                    spinnerSchoolTerms.adapter = tAdapter

                    val spinnerPosition = tAdapter.getPosition(strTerm)
                    spinnerSchoolTerms.setSelection(spinnerPosition)

                    // loadClasses(this, "", this)

                    viewModel.getTimeTable(this, strClass).observe(this, Observer<MutableList<TimeTableItem>> {
                        with(rvTimeTable) {
                            layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            adapter = TimeTableModelRecyclerView(this@GroupedClassesActivity, viewModel.oldFilteredPosts, this@GroupedClassesActivity)
                        }

                        if(viewModel.oldFilteredPosts.isNotEmpty()) {
                            viewModel.searchTermsSubject(strSubject, strTerm!!)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        val diffResult = DiffUtil.calculateDiff(PostDiffUtilCallBack(viewModel.oldFilteredPosts,
                                                viewModel.filteredPosts))

                                        viewModel.oldFilteredPosts.clear()
                                        viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)

                                        diffResult.dispatchUpdatesTo(rvTimeTable.adapter)
                                        if(rvTimeTable.adapter != null) {
                                            rvTimeTable.adapter.notifyDataSetChanged()
                                        }
                                    }

                        }
                        progressBar!!.visibility = View.GONE

                    })

                }

                else {

                    txtClassGroupTitle.text = strClass
                    loadSubjects(this, "", this)

                    val tAdapter = ArrayAdapter(this, R.layout.spinner_text, termsList)
                    tAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                    spinnerSchoolTerms.adapter = tAdapter

                    viewModel.getTimeTable(this, strClass).observe(this, Observer<MutableList<TimeTableItem>> {
                        with(rvTimeTable) {
                            layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            adapter = TimeTableModelRecyclerView(this@GroupedClassesActivity, viewModel.oldFilteredPosts, this@GroupedClassesActivity)
                        }

                        if(viewModel.oldFilteredPosts.isNotEmpty()) {
                            viewModel.searchClass(strClass!!)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        val diffResult = DiffUtil.calculateDiff(PostDiffUtilCallBack(viewModel.oldFilteredPosts,
                                                viewModel.filteredPosts))

                                        viewModel.oldFilteredPosts.clear()
                                        viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)

                                        diffResult.dispatchUpdatesTo(rvTimeTable.adapter)
                                        if(rvTimeTable.adapter != null) {
                                            rvTimeTable.adapter.notifyDataSetChanged()
                                        }
                                    }

                        }
                        progressBar!!.visibility = View.GONE

                    })

                }
            }

        }

        spinnerSubjects!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position > 0) {
                    viewModel.searchSubjects(subjectsList[position])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                val diffResult = DiffUtil.calculateDiff(PostDiffUtilCallBack(viewModel.oldFilteredPosts,
                                        viewModel.filteredPosts))

                                viewModel.oldFilteredPosts.clear()
                                viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)

                                diffResult.dispatchUpdatesTo(rvTimeTable.adapter)

                                if(rvTimeTable.adapter != null) {
                                    rvTimeTable.adapter.notifyDataSetChanged()
                                }

                            }

                }
            }
        }

        spinnerSchoolTerms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0) {
                    viewModel.searchTerms(termsList[position])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                val diffResultTerms = DiffUtil.calculateDiff(PostDiffUtilCallBack(viewModel.oldFilteredPosts,
                                        viewModel.filteredPosts))

                                viewModel.oldFilteredPosts.clear()
                                viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)

                                diffResultTerms.dispatchUpdatesTo(rvTimeTable.adapter)
                            }
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun loadSubjects(ctx: Context, sentSubj: String, mActivity: FragmentActivity) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getSubjectsList.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("teacherid", userId)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                subjectsList.clear()
                subjectsList.add("Subject")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)
                    val subjData = allData.getString("subject")

                    subjectsList.add(subjData)

                }

                val sAdapter = ArrayAdapter(mActivity, R.layout.spinner_text, subjectsList)
                sAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                spinnerSubjects!!.adapter = sAdapter

                if(sentSubj != "") {
                    val spinnerPosition = sAdapter.getPosition(sentSubj)
                    spinnerSubjects!!.setSelection(spinnerPosition)
                }
                else {
                  sAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                  spinnerSubjects!!.adapter = sAdapter


                }
            }
        }
    }
}
