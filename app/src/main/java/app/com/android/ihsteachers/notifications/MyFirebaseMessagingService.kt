package app.com.android.ihsteachers.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.main.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "Service"
    var intent: Intent? = null

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Log.e("NEW_TOKEN", s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "Data 1: " + remoteMessage!!.data["title"])

        if(remoteMessage.data["title"] != "Message") {

            if(!NotificationUtils.isAppIsInBackground(applicationContext)) {
                val i = Intent(Config.PUSH_NOTIFICATION)
                i.putExtra("message", remoteMessage.data!!["message"])
                i.putExtra("SELECTED_TERM", "")
                i.putExtra("SELECTED_SUBJECT", "")
                i.putExtra("SELECTED_CLASS", "")
                i.putExtra("SELECTED_CLASS_HOME", "")
                LocalBroadcastManager.getInstance(this).sendBroadcast(i)
            }

            else {

            }
            sendNotification(remoteMessage)
            //intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent!!.putExtra("message", remoteMessage.data!!["message"])
            intent!!.putExtra("SELECTED_TERM", "")
            intent!!.putExtra("SELECTED_SUBJECT", "")
            intent!!.putExtra("SELECTED_CLASS", "")
            intent!!.putExtra("SELECTED_CLASS_HOME", "")



          //  startActivity(intent)
        }

    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        intent = Intent(this, MainActivity::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT) //FLAG_ONE_SHOT
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + applicationContext.getPackageName() + "/raw/notification")
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentText(remoteMessage.data["message"])
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ihs_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ihs_app_icon))
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        playNotificationSound()
    }

    // Playing notification sound
    fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + applicationContext.getPackageName() + "/raw/notification")
            val r = RingtoneManager.getRingtone(applicationContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}