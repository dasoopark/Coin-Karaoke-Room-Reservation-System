package kr.co.korearental.dongno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPayInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pay_info)

        val database = FirebaseDatabase.getInstance()
        val intent_date = intent.getStringExtra("date")
        title = intent_date.substring(2, 4)+"년 "+intent_date.substring(5, 7)+"월 "+intent_date.substring(8, 10)+"일"

        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}/payment/${intent_date}")
        val mRecyclerView = findViewById<RecyclerView>(R.id.admin_pay_infoRV)
        val payInfoList = arrayListOf<AdmPayInfo>()

        conoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for(time in p0.children){
                    for(index in time.children){
                        // criteria의 속성이 songs 또는 time이기 때문에 songs의 value 값이 없는 경우에는 time의 value를,
                        // time의 value 값이 없는 경우에는 songs의 value를 값으로 가진다.
                        var criteria = ""
                        if(index.child("criteria/songs").value.toString() == "null"){
                            criteria = index.child("criteria/time").value.toString()
                        }else{
                            criteria = index.child("criteria/songs").value.toString()
                        }
                        payInfoList.add(AdmPayInfo(time.key.toString(),
                        index.child("payTotal").value.toString().toInt(),
                        criteria, index.child("paymethod").value.toString()))
                    }
                }
                mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                mRecyclerView.adapter = AdminPayInfoAdapter(applicationContext, payInfoList)
                mRecyclerView.setHasFixedSize(true)
            }
        })
    }
}