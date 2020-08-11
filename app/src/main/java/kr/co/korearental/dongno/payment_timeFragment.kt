package kr.co.korearental.dongno

import android.app.TimePickerDialog
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
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
        var today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Calendar.getInstance().time)

        var select_time: String = ""
        var user_pay_count: Int = 0
        var cono_pay_count: Int = 0
        lateinit var paymethod : String
        lateinit var time : String
        var aHour_won : Int = 0
        var thirtyMin_won : Int = 0
        var total_won : Int = 0


        view.time_choiceButton_fortime.setOnClickListener{
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                reservation_time_fortime.text = SimpleDateFormat("HH시 mm분").format(cal.time)
                select_time=SimpleDateFormat("HH시 mm분").format(cal.time)
                conoRef.child("payment/${today}/${select_time}").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        cono_pay_count= snapshot.childrenCount.toInt()
                    }
                })
            }
            val timePickerDialog : TimePickerDialog
            var hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
            val minute = CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance()[Calendar.MINUTE] + CustomTimePickerDialog.TIME_PICKER_INTERVAL)
            if(minute == 0){
                hour += 1
            }
            timePickerDialog = CustomTimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,timeSetListener, hour, minute,true)

            timePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.setTitle("예약 시간 선택")
            timePickerDialog.show()
        }

        conoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                aHour_won = p0.child("info/charge/time/1시간").value.toString().toInt()
                thirtyMin_won = p0.child("info/charge/time/30분").value.toString().toInt()
            }
        })


        userRef.child("${today}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                user_pay_count = p0.childrenCount.toInt()
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
            }else if(paymethod == "") {
                Toast.makeText(requireContext(), "결제 수단을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if(Calendar.getInstance()[Calendar.HOUR_OF_DAY]>select_time.substring(0, 2).toInt()){
                Toast.makeText(requireContext(), "올바른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if(cono_pay_count >= 10){
                Toast.makeText(requireContext(), "해당 시간은 전부 예약되었습니다.\n다른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                if((Calendar.getInstance()[Calendar.HOUR_OF_DAY]==select_time.substring(0, 2).toInt())&&(Calendar.getInstance()[Calendar.MINUTE]>select_time.substring(4, 6).toInt())){
                    Toast.makeText(requireContext(), "올바른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }else {
                    cono_pay_count++
                    user_pay_count++

                    conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/criteria/time").setValue(time)   //시간 저장
                    userRef.child("${today}/${user_pay_count}/criteria/time").setValue(time)                          //시간 저장

                    conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/paymethod").setValue(paymethod)  //결제 수단 저장
                    userRef.child("${today}/${user_pay_count}/paymethod").setValue(paymethod)                         //결제 수단 저장

                    conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/userid").setValue(GlobalApplication.prefs.getString("userid", "Error"))
                    userRef.child("${today}/${user_pay_count}/cononame").setValue(GlobalApplication.search_cono) // 예약한 코노 이름 저장

                    conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/reserveTime").setValue(select_time) // 선택한 시작시간 저장
                    userRef.child("${today}/${user_pay_count}/reserveTime").setValue(select_time)                        // 선택한 시작시간 저장

                    conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/payTotal").setValue(total_won)   // 결제 금액 저장
                    userRef.child("${today}/${user_pay_count}/payTotal").setValue(total_won)                          // 결제 금액 저장

                    Toast.makeText(requireContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }
        }
        return view
    }

}

