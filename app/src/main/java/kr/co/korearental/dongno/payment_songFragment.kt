package kr.co.korearental.dongno



import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.payment_song.*
import kotlinx.android.synthetic.main.payment_song.view.*
import java.text.SimpleDateFormat
import java.util.*

const val INTERVAL = 5


class payment_songFragment: Fragment() {
    val database = FirebaseDatabase.getInstance()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     @SuppressLint("RestrictedApi", "ResourceType")
     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
         val view = inflater.inflate(R.layout.payment_song, container, false)


         //방 선택하기
         view.room_choiceButton.setOnClickListener{
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
        view.time_choiceButton.setOnClickListener{
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                reservation_time.text = SimpleDateFormat("HH시 mm분").format(cal.time)
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

