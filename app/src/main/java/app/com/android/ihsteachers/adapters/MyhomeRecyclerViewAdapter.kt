package app.com.android.ihsteachers.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.AssignedActivity
import app.com.android.ihsteachers.main.activity.ClassDetailsActivity
import app.com.android.ihsteachers.main.activity.CoveredSoFarActivity

import app.com.android.ihsteachers.main.fragments.HomeFragment.OnListFragmentInteractionListener
import app.com.android.ihsteachers.models.HomeContent.HomeItem
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * [RecyclerView.Adapter] that can display a [HomeItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyhomeRecyclerViewAdapter(
        private val mValues: List<HomeItem>,
        private val mContext: Context,
        private val mActivity: Activity)
    : RecyclerView.Adapter<MyhomeRecyclerViewAdapter.ViewHolder>() {

 //   private val mOnClickListener: View.OnClickListener

   /* init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as HomeItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener!!.onListFragmentInteraction()
        }
    } */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
     //   holder.mTitleView.text = item.title
        holder.mContentView.text = item.content
        holder.mSubContentView.text = item.subContent
        holder.mSubjectView.text = item.subject
        holder.mContentImgView.setImageDrawable(ContextCompat.getDrawable(mContext, item.imgName))
        holder.mBackgroundView.setBackgroundResource(item.backgroundImage)

        holder.btnOpenssignedSubjects.setOnClickListener {

            val getData: Array<String>?
            val getImg: IntArray?

            if(mValues[position].id == "ASGN") {
                getData = arrayOf(mValues[position].title, mValues[position].content, mValues[position].subContent)
                getImg = intArrayOf(mValues[position].imgName, mValues[position].backgroundImage)

                val intent = Intent(mContext, AssignedActivity::class.java)
                intent.putExtra("SELECTED_STRING_DATA", getData)
                intent.putExtra("SELECTED_INT_DATA", getImg)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, holder.btnOpenssignedSubjects, "homeRowClicked")
                mActivity.startActivity(intent, options.toBundle())
            }


       /*     if(mValues[position].id == "NXCL") {
                val intent = Intent(mContext, ClassDetailsActivity::class.java)
                intent.putExtra("SELECTED_TOPIC",  it.txtContent.text)
                intent.putExtra("SELECTED_SUBJECT", it.txtHiddenSubject.text)

                mActivity.startActivity(intent)
            }

            if(mValues[position].id == "CSR") {
                val intent = Intent(mContext, CoveredSoFarActivity::class.java)
                mActivity.startActivity(intent)
            } */

        }

    }

    override fun getItemCount(): Int = mValues.size


    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
      //  val mTitleView: TextView = mView.txtTitle
        val mContentView: TextView = mView.txtContent
        val mSubContentView: TextView = mView.txtSubContent
        val mSubjectView: TextView = mView.txtHiddenSubject
        val mContentImgView: ImageView = mView.imgHomeRowIcon
        val mBackgroundView: LinearLayout = mView.colorBackground
        val btnOpenssignedSubjects: CardView = mView.homeRow

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

}
