package kr.co.korearental.dongno

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Insets.add
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.view.OneShotPreDrawListener.add
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.karn.notify.Notify
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiverback : BroadcastReceiver() {
    var alarmlogg = arrayListOf<Alarmlog>()
    val realm = Realm.getDefaultInstance()

    //현재 시간 알기
    val now: Long = System.currentTimeMillis()
    val mDate = Date(now)
    //val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val simpleDate = SimpleDateFormat("HH시 mm분")
    val getTime: String = simpleDate.format(mDate)

    //현재 날짜 알기
    val now2: Long = System.currentTimeMillis()
    val mDate2 = Date(now2)
    val simpleDate2 = SimpleDateFormat("yyyy-MM-dd")
    val getTime2: String = simpleDate2.format(mDate)

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        val database= FirebaseDatabase.getInstance()
        val userRef=database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment/${getTime2}")


        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {

                    for(info in p0.children){
                        alarmlogg.add(Alarmlog(info.child("cononame").value.toString(),info.child("reserveTime").value.toString(),
                        getTime2))

                        /* realm 데이터 전부 삭제 시키기 == 디버깅용
                        realm.beginTransaction()
                        var mTodo = realm.where<Todo>().findAll()
                        mTodo.deleteAllFromRealm()
                        realm.commitTransaction() //트랜젝션 종료
                        데이터 전부 삭제 시키기
                         */
                    }

                //데이터 집어넣고, 인덱스 확인
                alarmlogg.forEach{ i ->
                    Log.d("리시버용","노래방이름:${i.cononame}, 시간:${i.time}, 날짜:${i.date}")

                    //예약 시간 비교 일치 확인
                    if(getTime.equals(i.time)){

                        //realmdb에 예약정보 데이터 넣는 과정
                        realm.beginTransaction() //트렌젝션 시작
                        val todo = realm.createObject<Todo>(nextId()) //새 객체 생성
                        todo.cono_name = i.cononame //값 설정
                        todo.cono_time = i.time
                        todo.cono_date = i.date
                        realm.commitTransaction() //트랜젝션 종료


                        createNotificationChannel()
                        val contentIntent = Intent(context, MainActivity::class.java)
                        val contentPendingIntent = PendingIntent.getActivity(
                            context,
                            NOTIFICATION_ID,
                            contentIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val builder =
                            context?.let {
                                NotificationCompat.Builder(it, PRIMARY_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_map)
                                    .setContentTitle("예약 알림 : ${i.cononame}")
                                    .setContentText("예약한 시간 ${i.time}이 되었습니다.")
                                    .setContentIntent(contentPendingIntent)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                            }
                        if (builder != null) {
                            notificationManager.notify(NOTIFICATION_ID, builder.build())
                        }

                        return@forEach
                    } //조건문(예약 시간 비교)

                } //foreach문
                }
        }) //파이어베이스


        Log.d(TAG, "Received intent : $intent")
        if (context != null) {
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        /*
      디버깅용 notify =>1분마다 알림 찍어줌
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
                    text = "샤샤샤샤샤샤샤샤"
                }
                .show()
        }
        */
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

    //realmdb 이동용
    private fun nextId(): Int { //다음 id를 반환
        val maxId = realm.where<Todo>().max("id")
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }


}