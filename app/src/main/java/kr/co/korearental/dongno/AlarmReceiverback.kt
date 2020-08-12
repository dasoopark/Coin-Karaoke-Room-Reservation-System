package kr.co.korearental.dongno

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import io.karn.notify.Notify
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiverback : BroadcastReceiver() {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")
    val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")
    var today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Calendar.getInstance().time)

    //현재 시간 알기
    val now: Long = System.currentTimeMillis()
    val mDate = Date(now)
    //val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val simpleDate = SimpleDateFormat("HH시 mm분")
    val getTime: String = simpleDate.format(mDate)

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received intent : $intent")
        if (context != null) {
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        createNotificationChannel()
        if (context != null) {
            deliverNotification(context)
        }

        if (context != null) {
            Notify
                .with(context)
                .meta { //// Launch the MainActivity once the notification is clicked.
                    clickIntent = PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java),
                        0
                    )
                    // Start a service which clears the badge count once the notification is dismissed.
                }
                .content { // this: Payload.Content.Default
                    title = "예약 알람"
                    text = "샤샤샤샤샤"
                }
                .show()
        }
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_map)
                .setContentTitle("Alert")
                .setContentText("$getTime")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(
                notificationChannel)
        }

    }

}