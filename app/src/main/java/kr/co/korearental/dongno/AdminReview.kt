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

class AdminReview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_review)
        title = "리뷰 조회"

        val database = FirebaseDatabase.getInstance()
        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${GlobalApplication.search_cono}/Review")
        val mRecyclerView=findViewById<RecyclerView>(R.id.admin_daysalesRV)
        val reviewList = arrayListOf<AdmReview>()

        conoRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for(snapshot in p0.children){
                    if(snapshot.key.equals("index")){
                        continue
                    }else if(snapshot.key.equals("rating_avg")){
                        var rating_avg = snapshot.value.toString()
                        if(rating_avg.length > 4){
                            rating_avg = rating_avg.substring(0, 4)
                        }
                        admin_reviewtext.text = "리뷰 > $rating_avg (${p0.childrenCount-2})"
                    }else{
                        reviewList.add(0, AdmReview(snapshot.child("name").value.toString(),
                        snapshot.child("review_content").value.toString(),
                        snapshot.child("rating").value.toString().toFloat(),
                        snapshot.child("Time").value.toString()))
                    }
                }
                mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                mRecyclerView.adapter = AdminReviewAdapter(applicationContext, reviewList)
                mRecyclerView.setHasFixedSize(true)
            }
        })
    }
}