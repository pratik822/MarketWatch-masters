package marketwatch.com.app.marketwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Pratik on 08-03-2018.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    var intent:Intent?=null;
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(!remoteMessage.data.get("body").isNullOrBlank()){
            sendNotification(remoteMessage.data.get("body"));
        }

        Log.d("firebase---", "Message data payload: " + remoteMessage.data.get("body"))
     }

    private fun sendNotification(msg: String?) {
        val requestID = System.currentTimeMillis().toInt()

        if(msg==("New tip added!")){
             intent = Intent(this, MainActivity::class.java)
             intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
             intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }else{
             intent = Intent(this, Notifications::class.java)
             intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }


        val pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent,
            PendingIntent.FLAG_MUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentText(msg)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}