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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mohamedabulgasem.datetimepicker.DateTimePicker
import kotlinx.android.synthetic.main.payment_song.*
import kotlinx.android.synthetic.main.payment_song.view.*
import me.kareluo.ui.OptionMenu
import me.kareluo.ui.OptionMenuView.OnOptionMenuClickListener
import me.kareluo.ui.PopupMenuView
import me.kareluo.ui.PopupView
import java.text.SimpleDateFormat
import java.util.*


class payment_songFragment: Fragment() {
    var guide_1 = 0
    var guide_2 = 0
     @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     @SuppressLint("RestrictedApi")
     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
         val view = inflater.inflate(R.layout.payment_song, container, false)

         val database = FirebaseDatabase.getInstance()
         val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")
         val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")

         conoRef.child("info/charge/songs").addListenerForSingleValueEvent(object : ValueEventListener{
             override fun onCancelled(p0: DatabaseError) {}
             override fun onDataChange(p0: DataSnapshot) {
                 var songs500 = p0.child("500").value.toString()
                 guide_1 = songs500.substring(0, 1).toInt()
                 var songs1000 = p0.child("1000").value.toString()
                 guide_2 = songs1000.substring(0, 1).toInt()
                 view.song_radiobutton1.text = "${guide_1}곡"
                 view.song_radiobutton2.text = "${guide_2}곡"
                 view.song_radiobutton3.text = "${guide_2*2}곡"
                 view.song_radiobutton4.text = "${guide_2*3}곡"
                 view.song_radiobutton5.text = "${guide_2*4}곡"
             }
         })

        // 예약 시간 선택
        view.time_choiceButton.setOnClickListener{
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
                    reservation_time.text = SimpleDateFormat("YY년 MM월 dd일 HH시 mm분").format(cal.time)

                }
                .build()
                .show()
        }

        //곡 선택하기
        view.select_song.showSoftInputOnFocus = false
        view.select_song.setOnClickListener {
            val menuView = PopupMenuView(requireContext(), R.menu.menu_pop, MenuBuilder(context))
            menuView.setOnMenuClickListener(object : OnOptionMenuClickListener {
                override fun onOptionMenuClick(position: Int, menu: OptionMenu): Boolean {
                    Toast.makeText(requireContext(), menu.title, Toast.LENGTH_SHORT).show()
                    select_song.setText(menu.title)
                    return true
                }
            })
            menuView.setSites(PopupView.SITE_BOTTOM, PopupView.SITE_LEFT, PopupView.SITE_TOP, PopupView.SITE_RIGHT)
            menuView.show(select_song)
        }


        return view
    }

}
