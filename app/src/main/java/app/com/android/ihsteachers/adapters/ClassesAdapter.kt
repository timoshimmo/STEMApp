package app.com.android.ihsteachers.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.GroupedClassesActivity
import app.com.android.ihsteachers.main.activity.MainActivity
import app.com.android.ihsteachers.models.ClassesItem
import kotlinx.android.synthetic.main.classes_row_layout.view.*

class ClassesAdapter(private val mValues: List<ClassesItem>,
                     private val mContext: Context,
                     private val mActivity: Activity) : RecyclerView.Adapter<ClassesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.classes_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.content
        holder.mBackgroundView.setBackgroundResource(item.backgroundImage)
        holder.btnOpenClass.setOnClickListener {
            val intent = Intent(mContext, GroupedClassesActivity::class.java)
            intent.putExtra("SELECTED_TERM", "")
            intent.putExtra("SELECTED_SUBJECT", "")
            intent.putExtra("SELECTED_CLASS", holder.mContentView.text!!)
            intent.putExtra("SELECTED_CLASS_HOME", "")
            mActivity.startActivity(intent)
            mActivity.finish()
        }
    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.txtContentClass
        val mBackgroundView: LinearLayout = mView.bkgClassRow
        val btnOpenClass: CardView = mView.homeRow
    }
}