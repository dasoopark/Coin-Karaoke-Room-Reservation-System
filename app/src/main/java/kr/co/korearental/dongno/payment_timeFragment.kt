package kr.co.korearental.dongno

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.payment_time.*
import kotlinx.android.synthetic.main.payment_time.view.*

class payment_timeFragment : Fragment() {

   
    override fun onCreateView( inflater: LayoutInflater,  container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.payment_time, container, false)


            view.room_choiceButton_fortime.setOnClickListener {
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



        // 예약 시간 선택
        view.time_choiceButton_fortime.setOnClickListener{
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                reservation_time_fortime.text = SimpleDateFormat("HH시 mm분").format(cal.time)
            }

            val timePickerDialog = CustomTimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,timeSetListener, Calendar.getInstance()[Calendar.HOUR],
                CustomTimePickerDialog.getRoundedMinute(
                    Calendar.getInstance()[Calendar.MINUTE] + CustomTimePickerDialog.TIME_PICKER_INTERVAL
                ),
                false
            )

            timePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.setTitle("예약 시간 선택")
            timePickerDialog.show()
        }


        return view
    }

}

