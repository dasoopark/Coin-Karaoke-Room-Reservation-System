package kr.co.korearental.dongno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        title = "관리자"

        admin_cononame.text = GlobalApplication.search_cono
        admin_conoaddress.text = intent.getStringExtra("address")

        cardView_day.setOnClickListener {
            val intent = Intent(this, AdminPay::class.java)
            startActivity(intent)
        }

        cardView_review.setOnClickListener {
            val intent = Intent(this, AdminReview::class.java)
            startActivity(intent)
        }

        cardView_info.setOnClickListener{
            val intent=Intent(this,AdminModifyInfo::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            finish()
            toast.cancel()
        }
    }
}