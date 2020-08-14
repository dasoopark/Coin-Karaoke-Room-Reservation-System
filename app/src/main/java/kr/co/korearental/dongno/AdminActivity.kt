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

        // 상단에 노래방 이름과 주소 출력
        admin_cononame.text = GlobalApplication.search_cono
        admin_conoaddress.text = intent.getStringExtra("address")

        cardView_day.setOnClickListener {
            // 일별 매출 조회 화면으로 이동
            val intent = Intent(this, AdminPay::class.java)
            startActivity(intent)
        }

        cardView_review.setOnClickListener {
            // 리뷰 조회 화면으로 이동
            val intent = Intent(this, AdminReview::class.java)
            startActivity(intent)
        }

        cardView_info.setOnClickListener{
            // 정보 수정 화면으로 이동
            val intent=Intent(this,AdminModifyInfo::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // 2번 뒤로 버튼을 눌러서 종료
        // 1.5초 내로 2번 누른 경우에 종료
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