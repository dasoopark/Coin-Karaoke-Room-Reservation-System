package kr.co.korearental.dongno

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.internal.SignInButtonCreator.createView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.payment.*
import kotlinx.android.synthetic.main.selecthomeitem.*

class selecthomeitemActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    private lateinit var mContext : Context
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
                val intent = Intent(this, paymentActivity::class.java)
                startActivity(intent)
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