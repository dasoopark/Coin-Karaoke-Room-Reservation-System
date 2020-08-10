package kr.co.korearental.dongno

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.reviewdialog.*
import kotlinx.android.synthetic.main.reviewdialog.view.*
import kotlinx.android.synthetic.main.selecthomeitem.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class selecthomeitemActivity : AppCompatActivity(){

    val manager = supportFragmentManager
    private lateinit var mContext : Context
    val database=FirebaseDatabase.getInstance()
    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.reviewtab -> {
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.reviewdialog, null)
                builder.setView(dialogView)
                    .setPositiveButton("작성") { dialogInterface, i ->
                        val userid=GlobalApplication.prefs.getString("userid","error")
                        if(userid.equals("error")){
                            Toast.makeText(this,"get userID error", Toast.LENGTH_SHORT).show()
                        }else{
                            val now = SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.KOREA).format(Calendar.getInstance().time)
                            val item_cononame=intent.getStringExtra("cononame")
                            val userRef=database.getReference("User/${userid}/Review/${item_cononame}")
                            val conoRef=database.getReference("Cono/${GlobalApplication.area1}/${GlobalApplication.area2}/" +
                                    "${GlobalApplication.area3}/${intent.getStringExtra("cononame")}/Review")
                            //유저 DB에 삽입
                            userRef.child("content").setValue(dialogView.reviewContent.text.toString())
                            userRef.child("rating").setValue(dialogView.ratingBar.rating)
                            userRef.child("Time").setValue(now)
                            //코노 DB에 삽입
                            conoRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onCancelled(error: DatabaseError) {}
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var index = 0
                                    var avg : Double = 0.0
                                    if (snapshot.childrenCount.toInt() != 0){
                                        index = snapshot.child("index").value.toString().toInt()+1
                                        avg = snapshot.child("rating_avg").value.toString().toDouble()
                                    }else{
                                        //index가 0인 경우
                                        index = 1
                                    }
                                    avg = (avg*(index-1) + dialogView.ratingBar.rating)/(index)
                                    conoRef.child("rating_avg").setValue(avg)
                                    conoRef.child("index").setValue(index)
                                    conoRef.child("${index}/userid").setValue(userid)
                                    conoRef.child("${index}/name").setValue(GlobalApplication.prefs.getString("username","Anonymous"))
                                    conoRef.child("${index}/review_content").setValue(dialogView.reviewContent.text.toString())
                                    conoRef.child("${index}/rating").setValue(dialogView.ratingBar.rating)
                                    conoRef.child("${index}/Time").setValue(now)
                                }
                            })
                        }
                    }
                    .setNegativeButton("취소") { dialogInterface, i ->
                        /* 취소일 때 아무 액션이 없으므로 빈칸 */
                    }
                    .show()
                // Dialog 사이즈 조절 하기
                return@OnNavigationItemSelectedListener true
            }

            R.id.paymenttab -> {
                val intent2 = Intent(this, paymentActivity::class.java)
                intent2.putExtra("cononame","${intent.getStringExtra("cononame")}")
                startActivity(intent2)
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selecthomeitem)

        //뒤로가기버튼 => HomeActivity
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeButtonEnabled(true)
        }

        initView()
    }

    private fun initView() {
        bottomNavigationView2.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        // set navigation Listener
        supportFragmentManager.beginTransaction().replace(R.id.main_layout2, selecthomeitemFragment()).commit()
    }

    //뒤로가기 버튼 => 홈액티비티로 돌아감
    override fun  onOptionsItemSelected(item: MenuItem) : Boolean{
        when (item.itemId){
            android.R.id.home->{ //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}