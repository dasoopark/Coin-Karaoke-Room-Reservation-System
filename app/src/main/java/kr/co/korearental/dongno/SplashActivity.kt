package kr.co.korearental.dongno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        try{
            Thread.sleep(1000)
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        catch(e:Exception){
            return
        }
    }
}