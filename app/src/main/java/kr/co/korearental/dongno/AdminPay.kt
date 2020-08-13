package kr.co.korearental.dongno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_review.*

class AdminPay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pay)
        title = "일별 매출 조회"

        val database = FirebaseDatabase.getInstance()
        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}/payment")
        val mRecyclerView=findViewById<RecyclerView>(R.id.admin_payRV)
        val payList = arrayListOf<AdmPay>()

        conoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for(snapshot in p0.children){
                    var bill : Int = 0
                    for(time in snapshot.children){
                        for(index in time.children){
                            bill += index.child("payTotal").value.toString().toInt()
                        }
                    }
                    payList.add(0, AdmPay(snapshot.key.toString(), bill))
                }
                mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                mRecyclerView.adapter = AdminPayAdapter(applicationContext, payList)
                mRecyclerView.setHasFixedSize(true)
            }
        })
    }
}