package kr.co.korearental.dongno

import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class selecthomeitemActivity : AppCompatActivity(){

    val manager = supportFragmentManager
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
                            val userRef=database.getReference("User/${userid}")
                            val conoRef=database.getReference("Cono")
                            userRef.child("${intent.getStringExtra("cononame")}").child("content").setValue(dialogView.reviewContent.text.toString())
                            userRef.child("${intent.getStringExtra("cononame")}").child("rating").setValue(dialogView.ratingBar.rating)
                            userRef.child("${intent.getStringExtra("cononame")}").child("Time").setValue(now)
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
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.paymentdialog, null)
                builder.setView(dialogView)
                    .setPositiveButton("결제") { dialogInterface, i ->
                    }
                    .setNegativeButton("취소") { dialogInterface, i ->
                        /* 취소일 때 아무 액션이 없으므로 빈칸 */
                    }
                    .show()
                // Dialog 사이즈 조절 하기
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selecthomeitem)

        initView()

    }

    private fun initView() {
        bottomNavigationView2.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        // set navigation Listener
        supportFragmentManager.beginTransaction().replace(R.id.main_layout2, selecthomeitemFragment()).commit()
    }



}