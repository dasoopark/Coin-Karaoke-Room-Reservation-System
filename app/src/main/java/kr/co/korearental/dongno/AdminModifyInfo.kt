package kr.co.korearental.dongno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_modify_info.*
import java.lang.NumberFormatException

class AdminModifyInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_modify_info)

        val database = FirebaseDatabase.getInstance()
        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}")

        conoRef.child("info").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_name.text=GlobalApplication.search_cono
                for(info in snapshot.children){
                    if(info.key.equals("address")){
                        txt_address.text=info.value.toString()
                    }else if(info.key.equals("tel")){
                        edit_tel.setText(info.value.toString())
                    }else if(info.key.equals("charge")){
                        edit_500.setText(info.child("songs/500").value.toString().substring(0,1))
                        edit_1000.setText(info.child("songs/1000").value.toString().substring(0,1))
                        edit_30min.setText(info.child("time/30분").value.toString())
                        edit_hour.setText(info.child("time/1시간").value.toString())
                    }
                }
            }
        })

        btn_accept.setOnClickListener {
            try {
                if (edit_tel.text == null) {
                    Toast.makeText(this, "수정할 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (edit_500.text == null || edit_1000.text == null || edit_30min.text == null || edit_hour.text == null) {
                    Toast.makeText(this, "입력되지 않은 가격정보가 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    conoRef.child("info/tel").setValue(edit_tel.text.toString())
                    conoRef.child("info/charge/songs/500").setValue(edit_500.text.toString() + "곡")
                    conoRef.child("info/charge/songs/1000")
                        .setValue(edit_1000.text.toString() + "곡")
                    conoRef.child("info/charge/time/1시간")
                        .setValue(edit_hour.text.toString().trim().toInt())
                    conoRef.child("info/charge/time/30분")
                        .setValue(edit_30min.text.toString().trim().toInt())
                    Toast.makeText(this, "정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }catch(e:NumberFormatException){
                Toast.makeText(this,"가격을 정확히 다시 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
        }

    }
}