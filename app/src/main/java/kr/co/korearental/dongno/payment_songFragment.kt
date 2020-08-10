package kr.co.korearental.dongno

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.payment_song.*
import kotlinx.android.synthetic.main.payment_song.view.*

class payment_songFragment: Fragment() {

    //val database = FirebaseDatabase.getInstance()
    //val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")
    //val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}/payment")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.payment_song, container, false)
        var cono_pay_count : Long = 0
        var user_pay_count : Long = 0
        /*view.song_payButton.setOnClickListener{
            userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    for(snapshot in p0.children)
                        user_pay_count=snapshot.childrenCount
                }
            })
            conoRef.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    for(snapshot in p0.children)
                        cono_pay_count=snapshot.childrenCount
                }
            })
            user_pay_count++
            cono_pay_count++

            userRef.child("${user_pay_count}/cononame").setValue("${GlobalApplication.search_cono}")
            //userRef.child("${user_pay_count}/reserveTime").setValue("")     -> 예약 시간 저장
            //conoRef.child("${cono_pay_count}/reserveTime").setValue("")     -> 예약 시간 저장

            //userRef.child("${user_pay_count}/roomNum").setValue("")     -> 방 번호
            //conoRef.child("${cono_pay_count}/roomNum").setValue("")     -> 방 번호

            //userRef.child("${user_pay_count}/totalPay").setValue("")     -> 결제 금액
            //conoRef.child("${cono_pay_count}/totalPay").setValue("")     -> 결제 금액

            //userRef.child("${user_pay_count}/criteria/songs").setValue("")     -> 총 곡수
            //conoRef.child("${cono_pay_count}/criteria/songs").setValue("")     -> 총 곡수

           if(howpay_radiogroup_song.checkedRadioButtonId==R.id.toss_song){
                userRef.child("${user_pay_count}/method").setValue("토스")
                conoRef.child("${cono_pay_count}/method").setValue("토스")
            }else if(howpay_radiogroup_song.checkedRadioButtonId==R.id.card_song){
                userRef.child("${user_pay_count}/method").setValue("카드")
                conoRef.child("${cono_pay_count}/method").setValue("카드")
            }else{
                userRef.child("${user_pay_count}/method").setValue("멤버쉽")
                conoRef.child("${cono_pay_count}/method").setValue("멤버쉽")
            }
            //Toast.makeText(requireContext(), "결제가 완료되었습니다.",Toast.LENGTH_SHORT)

        }*/
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
        return view
    }
}
