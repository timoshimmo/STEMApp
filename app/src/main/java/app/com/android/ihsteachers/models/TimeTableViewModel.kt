package app.com.android.ihsteachers.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.providers.PreferencesHelper.classId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import io.reactivex.Completable
import org.json.JSONObject


class TimeTableViewModel: ViewModel() {

    private var timeTableAllList: MutableList<TimeTableItem> = mutableListOf()
    private lateinit var timeTableList: MutableLiveData<MutableList<TimeTableItem>>

    val CUSTOM_PREF_NAME = "UserID"
    val CUSTOM_PREF_CLASS = "ClassID"

    private val myBackgroundList : List<Int> = listOf (
            R.drawable.light_green_gradient_curve,
            R.drawable.light_blue_gradient_curve,
            R.drawable.fiery_rose_gradient_curve,
            R.drawable.blue_cerulean_gradient_curve
    )

    fun getTimeTable(ctx: Context, classes: String): LiveData<MutableList<TimeTableItem>> {
        if (!::timeTableList.isInitialized) {
            timeTableList = MutableLiveData()
            loadTimeTableItems(ctx, classes)
        }
        return timeTableList
    }

    private fun loadTimeTableItems(ctx: Context, classes: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path: String
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        path = "getUserClasses.php"

        params.put("teacherid", userId)
        params.put("classid", classes)

        timeTableList = MutableLiveData()

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)

                    val timeTableData = TimeTableItem(i.toString(), allData.getString("term"),
                            allData.getString("subject"), allData.getString("topicId"),
                            allData.getString("className"), myBackgroundList.shuffled().take(1)[0])

                    timeTableAllList.add(i, timeTableData)

                }

                timeTableList.value = timeTableAllList
                oldFilteredPosts.addAll(timeTableAllList)

            }
        }
    }


    val filteredPosts: MutableList<TimeTableItem> = mutableListOf()
    val oldFilteredPosts: MutableList<TimeTableItem> = mutableListOf()

    init {

    }

    fun searchSubjects(query: String): Completable = Completable.create {

        val wanted = timeTableAllList.filter {
           it.subject == query
        }.toList()

        filteredPosts.clear()
        filteredPosts.addAll(wanted)
        it.onComplete()
    }

    fun searchTerms(query: String): Completable = Completable.create {
        val wanted = timeTableAllList.filter {
            it.schoolTerm == query
        }.toList()

        filteredPosts.clear()
        filteredPosts.addAll(wanted)
        it.onComplete()
    }

    fun searchClass(query: String): Completable = Completable.create {

        val wanted = timeTableAllList.filter {
            it.classes == query
        }.toList()

        filteredPosts.clear()
        filteredPosts.addAll(wanted)
        it.onComplete()
    }

    fun searchTermsSubject(query: String, subjQuery: String): Completable = Completable.create {
        val wanted = timeTableAllList.filter {
            it.subject == query
        }.filter {
            it.schoolTerm == subjQuery
        }.toList()

        filteredPosts.clear()
        filteredPosts.addAll(wanted)
        it.onComplete()
    }
}