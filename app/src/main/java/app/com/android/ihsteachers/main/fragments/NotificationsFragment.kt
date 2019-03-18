package app.com.android.ihsteachers.main.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.models.NotificationItem
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.providers.PreferencesHelper.notificationData
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.json.JSONObject
import android.support.v4.content.ContextCompat
import app.com.android.ihsteachers.providers.ListDividerItemDecoration

private const val COLUMN_COUNT = "param1"

class NotificationsFragment : Fragment() {

    private var param1: Int? = null
    private var listener: OnFragmentNotificationsInteractionListener? = null

    lateinit var rvNotifications: RecyclerView
    var progressBar: ProgressBar? = null

    val CUSTOM_PREF_NAME = "UserID"
    val CUSTOM_PREF_DATA = "NotificationData"
    var allData: String = ""

    private var notificationList: MutableList<NotificationItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        progressBar = view.findViewById(R.id.progressBarNotification)
        progressBar!!.visibility = View.VISIBLE

        val ctx: Context = this.requireContext()

        rvNotifications = view.findViewById(R.id.rvNotifications)

        val dividerDrawable = ContextCompat.getDrawable(activity!!, R.drawable.divider_style)
        val itemDecoration = ListDividerItemDecoration(dividerDrawable)

        rvNotifications.addItemDecoration(itemDecoration)

        loadNotification(ctx)

        return view
    }

    private fun loadNotification(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getNotifications.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("teacherid", userId)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())
            allData = response.toString()

            val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_DATA)
            prefs.notificationData = allData

            progressBar!!.visibility = View.GONE

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val allData = arrayAll.getJSONObject(i)

                    val id: String = allData.getString("id")
                    val title: String = allData.getString("title")
                    val message: String = allData.getString("message")
                    val created: String = allData.getString("created")
                    val readStatus: String = allData.getString("readStatus")
                    val type: String = allData.getString("type")

                    val notificationData = NotificationItem(id, title, message, created,
                            Integer.valueOf(readStatus), type)

                    notificationList.add(notificationData)

                }

                with(rvNotifications) {
                    layoutManager = when {
                        param1!! <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                        else -> android.support.v7.widget.GridLayoutManager(context, param1!!)
                    }
                    adapter = app.com.android.ihsteachers.adapters.NotificationAdapter(notificationList, ctx, listener)
                }
            }
        }
    }

    fun onButtonPressed(ty: String) {
        listener?.onFragmentNotificationInteraction(ty)
    }

 /*   fun populateRecycler(data: String, ctx: Context) {

        val obj = JSONObject(data)
        if(obj.getInt("status") == 1) {

            val arrayAll = obj.getJSONArray("result")

            for(i in 0 until arrayAll.length()) {

                val allData = arrayAll.getJSONObject(i)

                val id: String = allData.getString("id")
                val title: String = allData.getString("title")
                val message: String = allData.getString("message")
                val created: String = allData.getString("created")
                val readStatus: String = allData.getString("readStatus")
                val type: String = allData.getString("type")

                val notificationData = NotificationItem(id, title, message, created,
                        Integer.valueOf(readStatus), type)

                notificationList.add(notificationData)

            }

            with(rvNotifications) {
                layoutManager = when {
                    param1!! <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                    else -> android.support.v7.widget.GridLayoutManager(context, param1!!)
                }

            //    val dividerDrawable = ContextCompat.getDrawable(activity!!, R.drawable.divider_style)
            //    val itemDecoration = ListDividerItemDecoration(dividerDrawable)

             //   rvNotifications.addItemDecoration(itemDecoration)
                adapter = app.com.android.ihsteachers.adapters.NotificationAdapter(notificationList, ctx, listener)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
          super.onSaveInstanceState(outState)

          if(allData != "") {
              outState.putString("RESNOTIFICATION", allData)
          }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
          super.onActivityCreated(savedInstanceState)

          if(savedInstanceState != null) {
              val myData = savedInstanceState.getString("RESNOTIFICATION")
              populateRecycler(myData, this.requireContext())
          }
          else {
              loadNotification(this.requireContext())
          }
    }
*/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentNotificationsInteractionListener) {
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
    interface OnFragmentNotificationsInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentNotificationInteraction(type: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment NotificationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
                NotificationsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(COLUMN_COUNT, param1)
                    }
                }
    }
}
