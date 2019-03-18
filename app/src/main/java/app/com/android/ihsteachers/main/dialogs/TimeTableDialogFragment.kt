package app.com.android.ihsteachers.main.dialogs

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.models.PostDiffUtilCallBack
import app.com.android.ihsteachers.models.TimeTableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TimeTableDialogFragment : DialogFragment() {

    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private var param3: String? = null

    private var columnCount = 1
    private var listener: OnTimeTableDialogFragmentInteractionListener? = null

    lateinit var rvDTimeTable: RecyclerView

    private val subjectsList = arrayOf("Select Subject", "Agriculture", "Biology")
    private val termsList = arrayOf("Select Term", "1st Term", "2nd Term", "3rd Term")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_COLUMN_COUNT)
            param2 = it.getString(ARG_TERM_PASSED)
            param3 = it.getString(ARG_SUBJ_PASSED)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_table_dialog, container, false)

        listener?.onTimeTableDialogFragmentInteraction(param1)

        rvDTimeTable = view.findViewById(R.id.rvDTimeTable)

        val spinnerSubjects: Spinner = view.findViewById(R.id.spnDFilterSubjects)
        val spinnerSchoolTerms: Spinner = view.findViewById(R.id.spnDFilterTerm)

        val viewModel = ViewModelProviders.of(this).get(TimeTableViewModel::class.java)

        val ctx: Context = this.context!!

        with(rvDTimeTable) {
            layoutManager = when {
                columnCount <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                else -> android.support.v7.widget.GridLayoutManager(context, columnCount)
            }
            adapter = app.com.android.ihsteachers.adapters.TimeTableModelRecyclerView(ctx, viewModel.oldFilteredPosts, this@TimeTableDialogFragment.requireActivity())
        }

        if(!param2.equals("")) {
            viewModel.searchTermsSubject(param2!!, param3!!)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val diffResult = DiffUtil.calculateDiff(PostDiffUtilCallBack(viewModel.oldFilteredPosts,
                                viewModel.filteredPosts))

                        viewModel.oldFilteredPosts.clear()
                        viewModel.oldFilteredPosts.addAll(viewModel.filteredPosts)

                        diffResult.dispatchUpdatesTo(rvDTimeTable.adapter)
                        rvDTimeTable.adapter.notifyDataSetChanged()
                    }
        }

        val sAdapter = ArrayAdapter(activity, R.layout.spinner_text, subjectsList)
        sAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinnerSubjects.adapter = sAdapter

        val tAdapter = ArrayAdapter(activity, R.layout.spinner_text, termsList)
        tAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinnerSchoolTerms.adapter = tAdapter

        spinnerSubjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                                diffResult.dispatchUpdatesTo(rvDTimeTable.adapter)
                                rvDTimeTable.adapter.notifyDataSetChanged()
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

                                diffResultTerms.dispatchUpdatesTo(rvDTimeTable.adapter)
                            }
                }

            }

        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(item: Int?) {
        listener?.onTimeTableDialogFragmentInteraction(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTimeTableDialogFragmentInteractionListener) {
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnTimeTableDialogFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTimeTableDialogFragmentInteraction(item: Int?)
    }

    companion object {
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_TERM_PASSED = "term_passed"
        const val ARG_SUBJ_PASSED = "subject_passed"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(columnCount: Int, termPassed: String, subjPassed: String) =
                TimeTableDialogFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putString(ARG_TERM_PASSED, termPassed)
                        putString(ARG_SUBJ_PASSED, subjPassed)
                    }
                }
    }

}
