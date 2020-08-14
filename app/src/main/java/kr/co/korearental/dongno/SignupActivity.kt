package kr.co.korearental.dongno

import android.os.Bundle
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.sign.*
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign)
        title = "회원가입"


        bSignUp.setOnClickListener {
            if(etEmail.text.toString() == ""){
                Toast.makeText(applicationContext,"Email을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if(etPassword.text.toString() == ""){
                Toast.makeText(applicationContext,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if(etUsername.text.toString() == ""){
                Toast.makeText(applicationContext,"사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener(this){
                    if (it.isSuccessful){
                        val user = mAuth.currentUser
                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.getReference("User/${user!!.uid}/info")
                        userRef.child("email").setValue(etEmail.text.toString())
                        userRef.child("name").setValue(etUsername.text.toString())
                        Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(applicationContext, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        etEmail.setText("")
                        etPassword.setText("")
                        etUsername.setText("")
                    }
                }

            }
        }

        tvHaveAnAccount.setOnClickListener {
            finish()
        }

        tvSignIn.setOnClickListener {
            finish()
        }
    }
}