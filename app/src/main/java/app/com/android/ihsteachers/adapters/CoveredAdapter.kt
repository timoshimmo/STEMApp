package app.com.android.ihsteachers.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.models.CoveredClassesModel
import app.com.android.ihsteachers.models.TimeTableItem
import kotlinx.android.synthetic.main.timetable_row_item.view.*
import kotlinx.android.synthetic.main.timetable_single_row_item.view.*


class CoveredAdapter(private val mValues: MutableList<CoveredClassesModel>?) : RecyclerView.Adapter<CoveredAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.timetable_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        holder.mSubjectView.text = item.subject
        holder.mTopicView.text = item.topic
        holder.mClassView.text = item.classes
        holder.mRowLayout.setBackgroundResource(item.infoBoxBackground)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mSubjectView: TextView = mView.txtSubjectName
        val mTopicView: TextView = mView.txtTopicName
        val mClassView: TextView = mView.txtClassName
        val mRowLayout: LinearLayout = mView.itemRowBackground
    }
}