package kr.co.korearental.dongno

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home.*


class HomeActivity : AppCompatActivity(){

    enum class FragmentType {
        home, bookmark, alarm, mypage
    }

    val manager = supportFragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        initView()
    }

    private fun initView() {
        // set navigation Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // start from Home
        changeFragmentTo(FragmentType.home)
    }



    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.tab1 -> {
                title = "홈"
                changeFragmentTo(FragmentType.home)
                return@OnNavigationItemSelectedListener true
            }

            R.id.tab2 -> {
                title = "즐겨찾기"
                changeFragmentTo(FragmentType.bookmark)
                return@OnNavigationItemSelectedListener true
            }

            R.id.tab3 -> {
                title = "알림"
                changeFragmentTo(FragmentType.alarm)
                return@OnNavigationItemSelectedListener true
            }
            R.id.tab4 -> {
                title = "마이 페이지"
                changeFragmentTo(FragmentType.mypage)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changeFragmentTo(type: FragmentType) {
        val transaction = manager.beginTransaction()
        when(type) {
            FragmentType.home -> {
                title = "홈"
                val homeFragment = homeFragment()
                transaction.replace(R.id.main_layout, homeFragment)
            }

            FragmentType.bookmark -> {
                val bookrmarkfragment = bookmarkFragment()
                transaction.replace(R.id.main_layout, bookrmarkfragment)
            }

            FragmentType.alarm -> {
                val alarmfragment = alarmFragment()
                transaction.replace(R.id.main_layout, alarmfragment)
            }

            FragmentType.mypage -> {
                val mypagefragment = mypageFragment()
                transaction.replace(R.id.main_layout, mypagefragment)
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
