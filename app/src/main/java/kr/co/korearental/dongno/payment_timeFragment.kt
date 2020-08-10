package kr.co.korearental.dongno

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
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
import me.kareluo.ui.OptionMenu
import me.kareluo.ui.OptionMenuView
import me.kareluo.ui.PopupMenuView
import me.kareluo.ui.PopupView

class payment_timeFragment : Fragment() {

    val database = FirebaseDatabase.getInstance()
    val userRef =
        database.getReference("User/${GlobalApplication.prefs.getString("userid", "0")}/payment")
    val conoRef =
        database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")
    //var today = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(Calendar.getInstance().time)

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.payment_time, container, false)
        var select_date: String = ""
        var select_time: String = ""
        var user_pay_count: Long = 0
        var cono_pay_count: Long = 0
        lateinit var paymethod : String
        lateinit var time : String
        var aHour_won : Int = 0
        var thirtyMin_won : Int = 0
        var total_won : Int = 0

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

        view.time_choiceButton_fortime.setOnClickListener {
            val cal = Calendar.getInstance()
            // Pass activity reference to Builder and set your OnDateTimeSetListener
            DateTimePicker.Builder(requireActivity())
                .onDateTimeSetListener { year, month, dayOfMonth, hourOfDay, minute ->
                    // Use selected date and time values
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)

                    // 변수 => year, month, dayofmonth, hoursOfDay, minute 으로 쓰면됨
                    reservation_time_fortime.text = SimpleDateFormat("YY년 MM월 dd일 HH시 mm분").format(cal.time)
                    select_date = SimpleDateFormat("yyyy-MM-dd").format(cal.time).toString()
                    select_time = SimpleDateFormat("HH:mm").format(cal.time).toString()
                }
                .build()
                .show()
        }

        userRef.child("${select_date}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children)
                    user_pay_count = snapshot.childrenCount
            }
        })
        conoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children)
                    if(snapshot.key.equals("payment")) {
                        cono_pay_count = snapshot.child("${select_date}").childrenCount
                    }else if(snapshot.key.equals("info")){
                        aHour_won = snapshot.child("charge/time/1시간").value.toString().toInt()
                        thirtyMin_won = snapshot.child("charge/time/30분").value.toString().toInt()
                    }
            }
        })

        view.time_radiogroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.thirty_timebutton -> {
                    total_won=0
                    time=thirty_timebutton.text.toString()
                    total_won=thirtyMin_won
                }
                R.id.hour_timebutton -> {
                    total_won=0
                    time=hour_timebutton.text.toString()
                    total_won=aHour_won
                }
                R.id.hourthirty_timebutton -> {
                    total_won=0
                    time=hourthirty_timebutton.text.toString()
                    total_won=thirtyMin_won+aHour_won
                }
                R.id.twhour_button -> {
                    total_won=0
                    time=twhour_button.text.toString()
                    total_won=aHour_won*2
                }
            }
        }
        view.howpay_radiogroup_time.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.toss_time -> paymethod=toss_time.text.toString()
                R.id.card_time -> paymethod=card_time.text.toString()
                R.id.membership_time -> paymethod=card_time.text.toString()
            }
        }

        view.time_payButton.setOnClickListener {

            user_pay_count++
            cono_pay_count++

            conoRef.child("payment/${select_date}/${cono_pay_count}/criteria/time").setValue(time)  //시간 저장
            userRef.child("${select_date}/${user_pay_count}/criteria/time").setValue(time)          //시간 저장

            conoRef.child("payment/${select_date}/${cono_pay_count}/paymethod").setValue(paymethod)     //결제 수단 저장
            userRef.child("${select_date}/${user_pay_count}/paymethod").setValue(paymethod)             //결제 수단 저장

            userRef.child("${select_date}/${user_pay_count}/cononame").setValue("${GlobalApplication.search_cono}")     // 예약한 코노 이름 저장


            userRef.child("${select_date}/${user_pay_count}/reserveTime").setValue("${select_time}")                // 선택한 시작시간 저장
            conoRef.child("payment/${select_date}/${cono_pay_count}/reserveTime").setValue("${select_time}")       // 선택한 시작시간 저장

            userRef.child("${select_date}/${user_pay_count}/payTotal").setValue(total_won)                // 결제 금액 저장
            conoRef.child("payment/${select_date}/${cono_pay_count}/payTotal").setValue(total_won)       // 결제 금액 저장

        }
        return view
    }

}

