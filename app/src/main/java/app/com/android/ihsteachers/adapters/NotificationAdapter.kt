package app.com.android.ihsteachers.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.fragments.NotificationsFragment
import app.com.android.ihsteachers.models.NotificationItem
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import kotlinx.android.synthetic.main.notification_row_layout.view.*
import org.json.JSONObject

class NotificationAdapter(private val mValues: List<NotificationItem>,
                          private  val mContext: Context,
                          private val mListener: NotificationsFragment.OnFragmentNotificationsInteractionListener?) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.title
        holder.mContentView.text = item.message
        holder.mTimeCreatedView.text = item.dateCreated
        holder.mTxtNotificationId.text = item.id

        if(item.readStatus == 0) {
            holder.mRowBody.setBackgroundResource(R.color.colorSuperLightGray)
        }
        else {
            holder.mRowBody.setBackgroundResource(android.R.color.white)
        }

        holder.mRowBody.setOnClickListener {
            mListener?.onFragmentNotificationInteraction(item.type)
            editNotification(mContext, holder.mTxtNotificationId.text.toString())
        }

    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.txtNotificationTitle
        val mContentView: TextView = mView.txtNotificationContent
        val mTimeCreatedView: TextView = mView.txtTimeAdded
        val mTxtNotificationId: TextView = mView.txtNotificationId
        val mRowBody: ConstraintLayout = mView.notification_row


    }

    private fun editNotification(ctx: Context, notificationID: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "editNotification.php"
        val params = JSONObject()

        params.put("notificationid", notificationID)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val success = obj.getString("msg")
                Toast.makeText(ctx, success, Toast.LENGTH_LONG).show()

            }
            else {
                val unsuccessful = obj.getString("msg")
                Toast.makeText(ctx, unsuccessful, Toast.LENGTH_LONG).show()
            }

        }

    }

}