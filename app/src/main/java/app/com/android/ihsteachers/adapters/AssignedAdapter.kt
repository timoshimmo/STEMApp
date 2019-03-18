package app.com.android.ihsteachers.adapters

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.GroupedClassesActivity
import app.com.android.ihsteachers.main.activity.MainActivity
import app.com.android.ihsteachers.models.AssignedContent
import kotlinx.android.synthetic.main.assigned_row_item.view.*

class AssignedAdapter (private val mValues: List<AssignedContent.AssignedItem>,
                       private val mContext: Context,
                       private val mActivity: AppCompatActivity,
                       private val mSubjectName: String,
                       private val mClassName: String) : RecyclerView.Adapter<AssignedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.assigned_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.title
        holder.mContentView.text = item.content
        holder.mContentImgView.setImageDrawable(ContextCompat.getDrawable(mContext, item.iconImage))
        holder.mRowButton.setOnClickListener {
            val intent = Intent(mContext, GroupedClassesActivity::class.java)
            intent.putExtra("SELECTED_TERM", it.txtTermName.text)
            intent.putExtra("SELECTED_SUBJECT", mSubjectName)
            intent.putExtra("SELECTED_CLASS", mClassName)
            intent.putExtra("SELECTED_CLASS_HOME", "")
            mActivity.startActivity(intent)
            mActivity.finish()
        }

    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
          val mTitleView: TextView = mView.txtTermName
          val mContentView: TextView = mView.txtTermStatus
      //    val mDateView: TextView = mView.txtDuration
          val mContentImgView: ImageView = mView.imgAssignRowIcon
          val mRowButton: ConstraintLayout = mView.rowButtonAssign

    }

}