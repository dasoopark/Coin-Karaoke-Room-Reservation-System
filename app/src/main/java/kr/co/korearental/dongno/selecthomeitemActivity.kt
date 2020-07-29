package kr.co.korearental.dongno

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.selecthomeitem.*

class selecthomeitemActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selecthomeitem)

        initView()
    }

    private fun initView() {
        // set navigation Listener
        supportFragmentManager.beginTransaction().replace(R.id.main_layout2, selecthomeitemFragment()).commit()
    }



}