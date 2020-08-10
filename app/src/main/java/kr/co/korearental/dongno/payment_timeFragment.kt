package kr.co.korearental.dongno

import android.app.TimePickerDialog
import android.annotation.SuppressLint
import android.app.Activity
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

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.payment_time, container, false)

        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid", "0")}/payment")
        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")
        //var today = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(Calendar.getInstance().time)

        var select_date: String = ""
        var select_time: String = ""
        var user_pay_count: Int = 0
        var cono_pay_count: Int = 0
        lateinit var paymethod : String
        lateinit var time : String
        var aHour_won : Int = 0
        var thirtyMin_won : Int = 0
        var total_won : Int = 0

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

                    conoRef.child("payment/${select_date}/${select_time}").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onDataChange(p0: DataSnapshot) {
                            cono_pay_count = p0.childrenCount.toInt()
                        }
                    })
                }
                .build()
                .show()
        }

        userRef.child("${select_date}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children)
                    user_pay_count = snapshot.childrenCount.toInt()
            }
        })

        conoRef.child("info").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                aHour_won = p0.child("charge/time/1시간").value.toString().toInt()
                thirtyMin_won = p0.child("charge/time/30분").value.toString().toInt()
            }
        })

        view.time_radiogroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.thirty_timebutton -> {
                    total_won=0
                    time=thirty_timebutton.text.toString()
                    total_won=thirtyMin_won
                    view.time_payButton.text = "${total_won}원 결제"
                }
                R.id.hour_timebutton -> {
                    total_won=0
                    time=hour_timebutton.text.toString()
                    total_won=aHour_won
                    view.time_payButton.text = "${total_won}원 결제"
                }
                R.id.hourthirty_timebutton -> {
                    total_won=0
                    time=hourthirty_timebutton.text.toString()
                    total_won=thirtyMin_won+aHour_won
                    view.time_payButton.text = "${total_won}원 결제"
                }
                R.id.twhour_button -> {
                    total_won=0
                    time=twhour_button.text.toString()
                    total_won=aHour_won*2
                    view.time_payButton.text = "${total_won}원 결제"
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
            if(select_time == ""){
                Toast.makeText(requireContext(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if(total_won == 0){
                Toast.makeText(requireContext(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if(paymethod == ""){
                Toast.makeText(requireContext(), "결제 수단을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if(cono_pay_count >= 10){
                Toast.makeText(requireContext(), "해당 시간은 전부 예약되었습니다.\n다른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                user_pay_count++
                cono_pay_count++

                conoRef.child("payment/${select_date}/${select_time}/${cono_pay_count}/criteria/time").setValue(time)   //시간 저장
                userRef.child("${select_date}/${user_pay_count}/criteria/time").setValue(time)                          //시간 저장

                conoRef.child("payment/${select_date}/${select_time}/${cono_pay_count}/paymethod").setValue(paymethod)  //결제 수단 저장
                userRef.child("${select_date}/${user_pay_count}/paymethod").setValue(paymethod)                         //결제 수단 저장

                conoRef.child("payment/${select_date}/${select_time}/${cono_pay_count}/userid").setValue(GlobalApplication.prefs.getString("userid", "Error"))
                userRef.child("${select_date}/${user_pay_count}/cononame").setValue(GlobalApplication.search_cono) // 예약한 코노 이름 저장

                conoRef.child("payment/${select_date}/${select_time}/${cono_pay_count}/reserveTime").setValue(select_time) // 선택한 시작시간 저장
                userRef.child("${select_date}/${user_pay_count}/reserveTime").setValue(select_time)                        // 선택한 시작시간 저장

                conoRef.child("payment/${select_date}/${select_time}/${cono_pay_count}/payTotal").setValue(total_won)   // 결제 금액 저장
                userRef.child("${select_date}/${user_pay_count}/payTotal").setValue(total_won)                          // 결제 금액 저장

                Toast.makeText(requireContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
        return view
    }

}

