package kr.co.korearental.dongno

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.kakao.auth.ApiErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.OptionalBoolean
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {

    private lateinit var callback: SessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //manifests에 globalapplication 추가했는지 꼭 확인할 것!
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

        btn_login.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.login_dialog, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.username)
            val dialogRatingBar = dialogView.findViewById<EditText>(R.id.password)
            builder.setView(dialogView)
                .setPositiveButton("로그인") { dialogInterface, i ->
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
                // Dialog 사이즈 조절 하기
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }


    // 앱의 해쉬 키 얻는 함수
    private fun getAppKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        }
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            // 로그인 세션이 열렸을 때
            UserManagement.getInstance().me( object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    var needsScopeAutority = ""
                    if(result!!.kakaoAccount.emailNeedsAgreement()==OptionalBoolean.TRUE) {
                        needsScopeAutority += "이메일"
                    }
                    if(needsScopeAutority.isNotEmpty()) {
                        Toast.makeText(applicationContext, needsScopeAutority+"에 대한 권한이 허용되지 않았습니다. 개인정보 제공에 동의해주세요.", Toast.LENGTH_SHORT).show()

                        UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback(){
                            override fun onFailure(errorResult: ErrorResult?) {
                                val result = errorResult?.errorCode
                                if (result== ApiErrorCode.CLIENT_ERROR_CODE) {
                                    Toast.makeText(applicationContext, "네트워크 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "회원탈퇴에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onSessionClosed(errorResult: ErrorResult?) {
                                Toast.makeText(applicationContext, "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onNotSignedUp() {
                                Toast.makeText(applicationContext, "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onSuccess(result: Long?) {
                            }
                        })
                    } else {
                        //DB

                        val database = FirebaseDatabase.getInstance()
                        var userRef = database.getReference("User")

                        val userid = result.id.toString()
                        val usernickname = result.kakaoAccount.profile.nickname
                        val userthumbnail = result.kakaoAccount.profile.thumbnailImageUrl
                        val useremail = result.kakaoAccount.email

                        userRef.child(userid).child("info").child("name").setValue(usernickname)
                        userRef.child(userid).child("info").child("email").setValue(useremail)
                        userRef.child(userid).child("info").child("thumbnail").setValue(userthumbnail)

                        // 로그인이 성공했을 때
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    Toast.makeText(applicationContext,"세션이 닫혔습니다. 다시 시도해주세요 : ${errorResult.toString()}",Toast.LENGTH_SHORT).show()
                }
            })
        }
        override fun onSessionOpenFailed(exception: KakaoException?) {
            // 로그인 세션이 정상적으로 열리지 않았을 때
            if (exception != null) {
                com.kakao.util.helper.log.Logger.e(exception)
                Toast.makeText(applicationContext,"로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요 : $exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectSignupActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
