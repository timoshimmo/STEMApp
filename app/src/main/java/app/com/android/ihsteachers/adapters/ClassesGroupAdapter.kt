package app.com.android.ihsteachers.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.GroupedClassesActivity
import app.com.android.ihsteachers.providers.PreferencesHelper.classId
import app.com.android.ihsteachers.models.ClassesGroupItem
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import kotlinx.android.synthetic.main.grouped_classes_row.view.*

class ClassesGroupAdapter(private val mValues: List<ClassesGroupItem>,
                          private val mContext: Context,
                          private val mActivity: FragmentActivity) : RecyclerView.Adapter<ClassesGroupAdapter.ViewHolder>() {

    val CUSTOM_PREF_CLASS = "ClassID"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mActivity)
                .inflate(R.layout.grouped_classes_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.content
        holder.btnOpenClass.setOnClickListener {
            val intent = Intent(mContext, GroupedClassesActivity::class.java)
            intent.putExtra("SELECTED_CLASS_HOME", holder.mContentView.text!!)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    holder.btnOpenClass, "classGroupRow")

            val prefs = customPreference(mContext, CUSTOM_PREF_CLASS)
            prefs.classId = holder.mContentView.text as String

            mActivity.startActivity(intent, options.toBundle())
           // mActivity.finish()
        }
    }


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.txtClassGroupTitle
        val btnOpenClass: LinearLayout = mView.classGroupRow as LinearLayout
    }
}