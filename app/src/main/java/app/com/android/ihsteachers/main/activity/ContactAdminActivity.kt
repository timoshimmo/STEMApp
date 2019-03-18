package app.com.android.ihsteachers.main.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.models.ChatModel
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.ctx
import java.util.*
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.chat_row_layout.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ContactAdminActivity : AppCompatActivity()  {

    val CUSTOM_PREF_NAME = "UserID"
  //  private var mAdapter: FirebaseRecyclerAdapter<ChatModel>? = null
    private var mAdapter: FirebaseRecyclerAdapter<ChatModel, ChatHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_admin)

        val chatMsg: EditText = findViewById(R.id.editChatMsg)
        val btnSendMsg: ImageButton = findViewById(R.id.btnSendMessage)
        val rvChatMessages: RecyclerView = findViewById(R.id.rvChatArea)

        var currentDate: String

        val prefs = customPreference(ctx, CUSTOM_PREF_NAME)
        val userPref = prefs.userId

        val query = FirebaseDatabase.getInstance()
                .reference

        val options = FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel::class.java)
                .setLifecycleOwner(this)
                .build()

         mAdapter = object : FirebaseRecyclerAdapter<ChatModel, ChatHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
                return ChatHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_row_layout, parent, false), ctx)
            }

            override fun onBindViewHolder(holder: ChatHolder, position: Int, model: ChatModel) {

                    holder.bind(model)
            }

            override fun onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
            }

        }

        rvChatMessages.layoutManager = LinearLayoutManager(this.applicationContext)
        rvChatMessages.adapter = mAdapter

        btnSendMsg.setOnClickListener {

            currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val curDate = LocalDateTime.now()
                curDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))
            } else {
                val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
                sdf.format(Date())
            }

         //   val millis = Date().time

            FirebaseDatabase.getInstance()
                    .reference
                    .push()
                    .setValue(ChatModel(userPref, chatMsg.text.toString(), currentDate , "Admin"))

            chatMsg.setText("")
        }

    }

    class ChatHolder(private val customView: View, val context: Context) : RecyclerView.ViewHolder(customView) {

        val CUSTOM_PREF_NAME = "UserID"
     //   val ctx: Context = this.a

      /*  private fun getDate(time: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = time
            return DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString()
        } */

        fun bind(chatModel: ChatModel) {
            with(chatModel) {
                customView.txtChatMessageContent.text = chatModel.message
                customView.txtChatDateTimeValue.text = chatModel.timeSent
                customView.txtChatUser.text = chatModel.sender

                val prefs = customPreference(context, CUSTOM_PREF_NAME)
                val userPref = prefs.userId

                when {
                    chatModel.sender.equals("Admin") -> customView.chatMessageContainer.setCardBackgroundColor(Color.parseColor("#004777"))
                    chatModel.recipient.equals(userPref) -> customView.chatMessageContainer.setCardBackgroundColor(Color.parseColor("#009688"))
                    else -> return
                }

            }
        }
    }

    override fun onStop() {
        super.onStop()
        mAdapter!!.stopListening()
    }

    override fun onStart() {
        super.onStart()
        mAdapter!!.startListening()
    }

}
