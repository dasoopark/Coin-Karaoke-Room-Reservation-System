package kr.co.korearental.dongno

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.payment.*

class paymentActivity  : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)

        val fragmentAdapter = paymentViewAdapter(supportFragmentManager)
        main_viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(main_viewPager)
    }

}
