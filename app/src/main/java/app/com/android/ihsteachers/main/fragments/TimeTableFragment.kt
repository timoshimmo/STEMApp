package app.com.android.ihsteachers.main.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.models.*
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import java.util.ArrayList

class TimeTableFragment : Fragment() {

    private var param1: Int? = null
    private var listener: TimeTableFragment.OnFragmentInteractionListener? = null
    val CUSTOM_PREF_NAME = "UserID"
    var progressBar: ProgressBar? = null
    private var classList: MutableList<ClassesGroupItem> = ArrayList()

    lateinit var rvListClass: RecyclerView
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_COLUMN_COUNT)
         /*   prmTerm = it.getString(ARG_TERM_PASSED)
            prmSubject = it.getString(ARG_SUBJ_PASSED)
            prmClass = it.getString(ARG_CLS_PASSED)*/
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_table, container, false)

        listener?.onTimeTableFragmentInteraction(param1)
        progressBar = view.findViewById(R.id.progressBarClassesGroup)
        progressBar!!.visibility = View.VISIBLE

        rvListClass = view.findViewById(R.id.rvClassGroup)

        val ctx: Context = requireContext()
        loadClasses(ctx, this.requireActivity())

        return view

    }

    private fun loadClasses(ctx: Context, mActivity: FragmentActivity) {

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
                    val homeData = ClassesGroupItem(count.toString(), allData.getString("class"))

                    classList.add(homeData)

                    System.out.println("Classes List:" + allData.toString())

                }

                System.out.println("All Classes List:" + classList)

                with(rvListClass) {
                    layoutManager = when {
                        columnCount <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                        else -> android.support.v7.widget.GridLayoutManager(context, columnCount)
                    }
                    adapter = app.com.android.ihsteachers.adapters.ClassesGroupAdapter(classList, ctx, mActivity)
                }

            }
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTimeTableFragmentInteraction(item: Int?)
    }


    companion object {
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_TERM_PASSED = "term_passed"
        const val ARG_SUBJ_PASSED = "subject_passed"
        const val ARG_CLS_PASSED = "class_passed"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(columnCount: Int, termPassed: String, subjectPassed: String, classPassed: String) =
                TimeTableFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                     /*   putString(ARG_TERM_PASSED, termPassed)
                        putString(ARG_SUBJ_PASSED, subjectPassed)
                        putString(ARG_CLS_PASSED, classPassed)*/
                    }
                }
    }

}
