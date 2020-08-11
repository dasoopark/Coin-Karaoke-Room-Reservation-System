package kr.co.korearental.dongno



import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.payment_song.*
import kotlinx.android.synthetic.main.payment_song.view.*
import kotlinx.android.synthetic.main.payment_time.*
import kotlinx.android.synthetic.main.payment_time.view.*
import me.kareluo.ui.OptionMenu
import me.kareluo.ui.OptionMenuView.OnOptionMenuClickListener
import me.kareluo.ui.PopupMenuView
import me.kareluo.ui.PopupView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

const val INTERVAL = 5


class payment_songFragment: Fragment() {
     @SuppressLint("RestrictedApi", "ResourceType")
    var guide_1 = 0
    var guide_2 = 0
     @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     @SuppressLint("RestrictedApi")
     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
         val view = inflater.inflate(R.layout.payment_song, container, false)

         val database = FirebaseDatabase.getInstance()
         val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")
         val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")
         var today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Calendar.getInstance().time)

         var select_time: String = ""
         var user_pay_count: Int = 0
         var cono_pay_count: Int = 0
         var paymethod : String = ""
         var time : String = ""
         var songs : String = ""
         var total_won : Int = 0


         // 예약 시간 선택
         view.time_choiceButton.setOnClickListener{
             val cal = Calendar.getInstance()

             val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                 cal.set(Calendar.HOUR_OF_DAY, hour)
                 cal.set(Calendar.MINUTE, minute)
                 reservation_time.text = SimpleDateFormat("HH시 mm분").format(cal.time)
                 select_time=SimpleDateFormat("HH시 mm분").format(cal.time)
                 conoRef.child("payment/${today}/${select_time}").addListenerForSingleValueEvent(object : ValueEventListener{
                     override fun onCancelled(error: DatabaseError) {}
                     override fun onDataChange(snapshot: DataSnapshot) {
                         cono_pay_count= snapshot.childrenCount.toInt()

                     }
                 })
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
         ///
         conoRef.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onCancelled(p0: DatabaseError) {}
             override fun onDataChange(p0: DataSnapshot) {
                 var songs500 = p0.child("info/charge/songs/500").value.toString()
                 guide_1 = songs500.substring(0, 1).toInt()
                 var songs1000 = p0.child("info/charge/songs/1000").value.toString()
                 guide_2 = songs1000.substring(0, 1).toInt()
                 view.song_radiobutton1.text = "${guide_1}곡"
                 view.song_radiobutton2.text = "${guide_2}곡"
                 view.song_radiobutton3.text = "${guide_2*2}곡"
                 view.song_radiobutton4.text = "${guide_2*3}곡"
                 view.song_radiobutton5.text = "${guide_2*4}곡"
             }
         })

         userRef.child("${today}").addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onCancelled(p0: DatabaseError) {}
             override fun onDataChange(p0: DataSnapshot) {
                 user_pay_count = p0.childrenCount.toInt()
             }
         })

         view.song_radiogroup.setOnCheckedChangeListener { radioGroup, i ->
             when (i) {
                 R.id.song_radiobutton1 -> {
                     total_won=0
                     time = "10분"
                     total_won=500
                     view.song_payButton.text = "${total_won}원 결제"
                     songs = song_radiobutton1.text.toString()
                 }
                 R.id.song_radiobutton2 -> {
                     total_won=0
                     time = "20분"
                     total_won=1000
                     view.song_payButton.text = "${total_won}원 결제"
                     songs = song_radiobutton2.text.toString()
                 }
                 R.id.song_radiobutton3 -> {
                     total_won=0
                     if(view.song_radiobutton2.text == "2곡") {
                         time = "20분"
                     }else if(view.song_radiobutton2.text == "3곡") {
                         time = "30분"
                     }else if(view.song_radiobutton2.text == "4곡") {
                         time = "40분"
                     }else{
                         time = "40분"
                     }
                     total_won=2000
                     view.song_payButton.text = "${total_won}원 결제"
                     songs = song_radiobutton3.text.toString()
                 }
                 R.id.song_radiobutton4 -> {
                     total_won=0
                     if(view.song_radiobutton2.text == "2곡") {
                         time = "30분"
                     }else if(view.song_radiobutton2.text == "3곡") {
                         time = "40분"
                     }else if(view.song_radiobutton2.text == "4곡") {
                         time = "50분"
                     }else{
                         time = "60분"
                     }
                     total_won=3000
                     view.song_payButton.text = "${total_won}원 결제"
                     songs = song_radiobutton4.text.toString()
                 }
                 R.id.song_radiobutton5 -> {
                     total_won=0
                     if(view.song_radiobutton2.text == "2곡") {
                         time = "40분"
                     }else if(view.song_radiobutton2.text == "3곡") {
                         time = "50분"
                     }else if(view.song_radiobutton2.text == "4곡") {
                         time = "70분"
                     }else{
                         time = "80분"
                     }
                     total_won=4000
                     view.song_payButton.text = "${total_won}원 결제"
                     songs = song_radiobutton5.text.toString()
                 }
             }
         }

         view.howpay_radiogroup_song.setOnCheckedChangeListener { radioGroup, i ->
             when (i) {
                 R.id.toss_song -> paymethod=toss_song.text.toString()
                 R.id.card_song -> paymethod=card_song.text.toString()
                 R.id.membership_song -> paymethod=card_song.text.toString()
             }
         }

         view.song_payButton.setOnClickListener {
             if(select_time == ""){
                 Toast.makeText(requireContext(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
             }else if(total_won == 0){
                 Toast.makeText(requireContext(), "곡 수를 선택해주세요.", Toast.LENGTH_SHORT).show()
             }else if(paymethod == ""){
                 Toast.makeText(requireContext(), "결제 수단을 선택해주세요.", Toast.LENGTH_SHORT).show()
             }else if(cono_pay_count >= 10){
                 Toast.makeText(requireContext(), "해당 시간은 전부 예약되었습니다.\n다른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
             }else {


                 cono_pay_count++
                 user_pay_count++


                 conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/criteria/songs").setValue(songs)    //곡 수 저장
                 userRef.child("${today}/${user_pay_count}/criteria/songs").setValue(songs)                           //곡 수 저장

                 conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/paymethod").setValue(paymethod)     //결제 수단 저장
                 userRef.child("${today}/${user_pay_count}/paymethod").setValue(paymethod)                            //결제 수단 저장

                 conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/userid").setValue(GlobalApplication.prefs.getString("userid", "Error"))
                 userRef.child("${today}/${user_pay_count}/cononame").setValue(GlobalApplication.search_cono)    // 예약한 코노 이름 저장

                 conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/reserveTime").setValue(select_time)    // 선택한 시작시간 저장
                 userRef.child("${today}/${user_pay_count}/reserveTime").setValue(select_time)                           // 선택한 시작시간 저장

                 conoRef.child("payment/${today}/${select_time}/${cono_pay_count}/payTotal").setValue(total_won)      // 결제 금액 저장
                 userRef.child("${today}/${user_pay_count}/payTotal").setValue(total_won)                             // 결제 금액 저장

                 Toast.makeText(requireContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                 activity?.finish()
             }
         }

        return view
    }


}
