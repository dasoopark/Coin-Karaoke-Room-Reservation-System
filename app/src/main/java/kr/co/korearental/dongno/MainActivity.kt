package kr.co.korearental.dongno

import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.login_dialog, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.username)
            val dialogRatingBar = dialogView.findViewById<EditText>(R.id.password)
            builder.setView(dialogView)
                .setPositiveButton("취소") { dialogInterface, i ->

                }
                .setNegativeButton("로그인") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */

                }

                .show()
                // Dialog 사이즈 조절 하기
            
        }
    }
}
