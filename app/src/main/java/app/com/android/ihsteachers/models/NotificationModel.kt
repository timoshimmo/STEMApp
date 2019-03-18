package app.com.android.ihsteachers.models

data class NotificationItem(val id: String, val title: String, val message: String,
                             val dateCreated: String, val readStatus: Int, val type: String)