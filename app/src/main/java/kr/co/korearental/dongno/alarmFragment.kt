package kr.co.korearental.dongno

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import java.util.*

class alarmFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    /*
        val test: String = "23시 02분"
        var range = IntRange(0, 1)
        var range2 = IntRange(4, 5)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = test.slice(range).toInt()
        calendar[Calendar.MINUTE] = test.slice(range2).toInt()
        calendar[Calendar.SECOND] = 0
        //  Preference에 설정한 값 저장

        diaryNotification(calendar)
        //시간설정

     */

        return inflater.inflate(R.layout.alarmfragment, container, false)
    }


    fun diaryNotification(calendar: Calendar) {
        val dailyNotify = true // 무조건 알람을 사용
        val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, 0)
        val alarmManager =  activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }
    //함수///

}