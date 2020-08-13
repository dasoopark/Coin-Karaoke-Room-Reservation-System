package kr.co.korearental.dongno

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import io.realm.Realm
import io.realm.RealmQuery
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.korearental.dongno.AlarmReceiverback.Companion.NOTIFICATION_ID
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var callback: SessionCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    val realm = Realm.getDefaultInstance()
    var isChecked = true

    //현재 시간 알기
    var now: Long = System.currentTimeMillis()
    var mDate = Date(now)
    var simpleDate: SimpleDateFormat = SimpleDateFormat("HH시 mm분")
    var getTime: String = simpleDate.format(mDate)


    @SuppressLint("ShortAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val database= FirebaseDatabase.getInstance()
        val userRef=database.getReference("User/${GlobalApplication.prefs.getString("userid","")}/payment")


        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiverback::class.java)  // 1
        val pendingIntent = PendingIntent.getBroadcast(     // 2
            this, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)




            //realm Db전체 데이터 확인하는 용도
            Realm.getDefaultInstance().use { realm ->
                realm.where(Todo::class.java).findAll().forEach {
                    println("시작")
                    println("${it.id}")
                    println("${it.cono_name}")
                    println("${it.cono_date}")
                    println("${it.cono_time}")
                }
            }



        // 리시버 전달하는 용도  확인하려면 토글 버튼 달아서 체크후 확인.
          if (isChecked) {
                val repeatInterval: Long = 5*1000
                val triggerTime = (SystemClock.elapsedRealtime()    // 1
                        + repeatInterval)
                alarmManager.setInexactRepeating(     // 2
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    pendingIntent
                )

            } else {
                alarmManager.cancel(pendingIntent)    // 3
            }

////


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        //manifests에 globalapplication 추가했는지 꼭 확인할 것!
        //카카오톡 자동 로그인
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

        btn_login.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.login_dialog, null)
            val dialogUserEmail = dialogView.findViewById<EditText>(R.id.useremail)
            val dialogUserPassword = dialogView.findViewById<EditText>(R.id.password)
            val dialogcheckbox = dialogView.findViewById<CheckBox>(R.id.checkBox)
            if(GlobalApplication.prefs.getString("chkbox", "")=="true") {
                dialogcheckbox.isChecked = true
                dialogUserEmail.setText(GlobalApplication.prefs.getString("email_id", ""))
                dialogUserPassword.setText(GlobalApplication.prefs.getString("email_pw", ""))
            }
            builder.setView(dialogView)
                .setPositiveButton("로그인") { dialogInterface, i ->
                    if(dialogUserEmail.text.toString() == ""){
                        Toast.makeText(applicationContext, "Email을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }else if(dialogUserPassword.text.toString() == ""){
                        Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }else{
                        if(dialogcheckbox.isChecked){
                            GlobalApplication.prefs.setString("chkbox", "true")
                            GlobalApplication.prefs.setString("email_id", dialogUserEmail.text.toString())
                            GlobalApplication.prefs.setString("email_pw", dialogUserPassword.text.toString())
                        }else{
                            GlobalApplication.prefs.setString("chkbox", "false")
                            GlobalApplication.prefs.setString("email_id", "")
                            GlobalApplication.prefs.setString("email_pw", "")
                        }
                        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                        mAuth.signInWithEmailAndPassword(dialogUserEmail.text.toString(), dialogUserPassword.text.toString())
                            .addOnCompleteListener(this){
                                if(it.isSuccessful){
                                    val user = mAuth.currentUser
                                    val database = FirebaseDatabase.getInstance()
                                    val userRef = database.getReference("User/${user!!.uid}/info")
                                    userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onCancelled(p0: DatabaseError) {}
                                        override fun onDataChange(p0: DataSnapshot) {
                                            GlobalApplication.account_email = p0.child("email").value.toString()
                                            GlobalApplication.account_name = p0.child("name").value.toString()
                                            GlobalApplication.account_profile = ""
                                            GlobalApplication.prefs.setString("userid", user.uid)
                                            GlobalApplication.prefs.setString("username",GlobalApplication.account_name)
                                            if(p0.child("admin").value.toString() == "true"){
                                                // 관리자 로그인
                                                GlobalApplication.search_area1 = p0.child("area1").value.toString()
                                                GlobalApplication.search_area2 = p0.child("area2").value.toString()
                                                GlobalApplication.search_area3 = p0.child("area3").value.toString()
                                                GlobalApplication.search_cono = p0.child("cono").value.toString()
                                                val intent = Intent(this@MainActivity, AdminActivity::class.java)
                                                intent.putExtra("address", p0.child("address").value.toString())
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                startActivity(intent)
                                            }else{
                                                // 사용자 로그인
                                                getLocationToHome()
                                            }
                                        }
                                    })
                                }else{
                                    Toast.makeText(applicationContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
        }

        account_plus.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            // 로그인 세션이 열렸을 때
            UserManagement.getInstance().me( object : MeV2ResponseCallback() {
                @SuppressLint("MissingPermission")
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
                        // 로그인이 성공했을 때
                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.getReference("User")
                        val userid = result.id.toString()
                        GlobalApplication.account_email = result.kakaoAccount.email
                        GlobalApplication.account_name = result.kakaoAccount.profile.nickname
                        GlobalApplication.account_profile = result.kakaoAccount.profile.thumbnailImageUrl
                        GlobalApplication.prefs.setString("userid", userid)
                        GlobalApplication.prefs.setString("username", GlobalApplication.account_name)
                        userRef.child(userid).child("info").child("email").setValue(GlobalApplication.account_email)
                        userRef.child(userid).child("info").child("name").setValue(GlobalApplication.account_name)
                        userRef.child(userid).child("info").child("thumbnail").setValue(GlobalApplication.account_profile)

                        // 위치 조회 후 HomeActivity로 이동
                        getLocationToHome()
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

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            finish()
            toast.cancel()
        }
    }


    fun getLocationToHome(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if(location == null) {
                    Toast.makeText(applicationContext, "location get fail", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalApplication.latitude = location.latitude
                    GlobalApplication.longitude = location.longitude
                    Thread{
                        val response = StringBuffer()
                        try {
                            val apiurl = URL("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=${GlobalApplication.longitude},${GlobalApplication.latitude}&orders=legalcode&output=json")
                            val con = apiurl.openConnection() as HttpURLConnection
                            con.requestMethod = "GET"
                            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "4cbtm9g0q6")
                            con.setRequestProperty("X-NCP-APIGW-API-KEY", "isgBHvU8rLoXBL9vqdLdhnxmX6ZJg47liwqztbNw")
                            val responseCode = con.responseCode
                            val br: BufferedReader
                            if (responseCode == 200) { // 정상 호출
                                br = BufferedReader(InputStreamReader(con.inputStream))
                            } else {  // 에러 발생
                                br = BufferedReader(InputStreamReader(con.errorStream))
                            }
                            var inputLine: String?
                            while (br.readLine().also { inputLine = it } != null) {
                                response.append(inputLine)
                            }
                            br.close()
                        }catch(e:Exception){
                        }
                        val json = JSONObject(response.toString())
                        val jsonArray = json.getJSONArray("results").getJSONObject(0).getJSONObject("region")
                        GlobalApplication.area1 = jsonArray.getJSONObject("area1").getString("name")
                        GlobalApplication.area2 = jsonArray.getJSONObject("area2").getString("name")
                        GlobalApplication.area3 = jsonArray.getJSONObject("area3").getString("name")
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.start()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(applicationContext, "위치를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
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
}


