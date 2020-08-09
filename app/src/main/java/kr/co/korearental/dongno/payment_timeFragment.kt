package kr.co.korearental.dongno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mohamedabulgasem.datetimepicker.DateTimePicker
import kotlinx.android.synthetic.main.payment_song.*
import kotlinx.android.synthetic.main.payment_song.view.*
import kotlinx.android.synthetic.main.payment_song.view.reservation_time
import kotlinx.android.synthetic.main.payment_song.view.room_choiceButton
import kotlinx.android.synthetic.main.payment_time.*
import kotlinx.android.synthetic.main.payment_time.view.*
import kotlinx.android.synthetic.main.payment_time.view.reservation_time_fortime
import java.text.SimpleDateFormat
import java.util.*

class payment_timeFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.payment_time, container, false)

        //방 예약하기
        view.room_choiceButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.roomchoice_dialog, null)
            builder.setView(dialogView)
                .setPositiveButton("예약") { dialogInterface, i ->

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
            // Dialog 사이즈 조절 하기
        }

        view.time_choiceButton_fortime.setOnClickListener{
            val cal = Calendar.getInstance()

            // Pass activity reference to Builder and set your OnDateTimeSetListener
            DateTimePicker.Builder(requireActivity())
                .onDateTimeSetListener { year, month, dayOfMonth, hourOfDay, minute ->
                    // Use selected date and time values
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)

                    // 변수 => year, month, dayofmonth, hoursOfDay, minute 으로 쓰면됨
                    reservation_time_fortime.text = SimpleDateFormat("YY년 MM월 dd일 HH시 mm분").format(cal.time)
                }
                .build()
                .show()
        }


        return view
    }
}
