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
import app.com.android.ihsteachers.main.activity.ClassDetailsActivity
import app.com.android.ihsteachers.models.TimeTableItem
import kotlinx.android.synthetic.main.timetable_row_item.view.*

class TimeTableModelRecyclerView(private  val mContext: Context,
                                 private val mValues: MutableList<TimeTableItem>?,
                                 private val mActivity: FragmentActivity)
    : RecyclerView.Adapter<TimeTableModelRecyclerView.ViewHolder>(){

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
        holder.mCardRow.setOnClickListener {
            val intent = Intent(mContext, ClassDetailsActivity::class.java)
            intent.putExtra("SELECTED_TOPIC", it.txtTopicName.text)
            intent.putExtra("SELECTED_SUBJECT", it.txtSubjectName.text)
            mActivity.startActivity(intent)
        }
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mSubjectView: TextView = mView.txtSubjectName
        val mTopicView: TextView = mView.txtTopicName
        val mClassView: TextView = mView.txtClassName
        val mRowLayout: LinearLayout = mView.itemRowBackground
        val mCardRow: CardView = mView.cardClassDataBox
    }
}