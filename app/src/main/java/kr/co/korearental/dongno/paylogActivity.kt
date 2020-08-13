package kr.co.korearental.dongno

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.mypagefragment.view.*
import kotlinx.android.synthetic.main.paylog.*
import kotlinx.android.synthetic.main.payment.*

class paylogActivity : AppCompatActivity() {
    var listpaylog = arrayListOf<Paylog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paylog)

        val database= FirebaseDatabase.getInstance()
        val userRef=database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")
        val mRecyclerView =findViewById<RecyclerView>(R.id.paylogRV)
        var total_price : Int = 0

        if (GlobalApplication.account_profile!="") {
            Glide.with(applicationContext).load(GlobalApplication.account_profile).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),RoundedCorners(200)))).into(imageView)
        }

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for(snapshot in p0.children){
                    for(info in snapshot.children){
                        listpaylog.add(0, Paylog(info.child("cononame").value.toString(),snapshot.key.toString(),info.child("payTotal").value.toString()+"원"))
                        total_price+=info.child("payTotal").value.toString().toInt()
                    }
                }
                txttotal.text=total_price.toString()+"원"
                mRecyclerView.layoutManager=LinearLayoutManager(baseContext)
                mRecyclerView.adapter=PaylogAdapter(baseContext,listpaylog)
                mRecyclerView.setHasFixedSize((true))
            }
        })


        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeButtonEnabled(true)
        }
    }

    //뒤로가기 버튼 => 마이페이지로 돌아감
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