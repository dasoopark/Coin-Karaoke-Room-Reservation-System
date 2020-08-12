package kr.co.korearental.dongno

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.karn.notify.Notify
import kotlinx.android.synthetic.main.home.*


class HomeActivity : AppCompatActivity(){

    enum class FragmentType {
        home, bookmark, alarm, mypage
    }

    val manager = supportFragmentManager
    var selected = 1

    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast

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
        selected = 1

    }

    private fun notifyinitView() {
        // set navigation Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // start from Home
        changeFragmentTo(FragmentType.alarm)
        selected = 3
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.tab1 -> {
                if(selected==1){
                    return@OnNavigationItemSelectedListener true
                }
                title = "홈"
                selected=1
                changeFragmentTo(FragmentType.home)
                return@OnNavigationItemSelectedListener true
            }

            R.id.tab2 -> {
                if(selected==2){
                    return@OnNavigationItemSelectedListener true
                }
                title = "즐겨찾기"
                selected=2
                changeFragmentTo(FragmentType.bookmark)
                return@OnNavigationItemSelectedListener true
            }

            R.id.tab3 -> {
                if(selected==3){
                    return@OnNavigationItemSelectedListener true
                }
                title = "알림"
                selected=3
                changeFragmentTo(FragmentType.alarm)
                return@OnNavigationItemSelectedListener true
            }
            R.id.tab4 -> {
                if(selected==4){
                    return@OnNavigationItemSelectedListener true
                }
                title = "마이 페이지"
                selected=4
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.addToBackStack(null)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            finish()
            toast.cancel()
        }
    }

}
