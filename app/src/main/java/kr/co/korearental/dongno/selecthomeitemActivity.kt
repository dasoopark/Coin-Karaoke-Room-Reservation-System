package kr.co.korearental.dongno

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.selecthomeitem.*

class selecthomeitemActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.reviewtab -> {
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.reviewdialog, null)
                builder.setView(dialogView)
                    .setPositiveButton("작성") { dialogInterface, i ->
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