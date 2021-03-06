package kr.co.korearental.dongno

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.auth.ApiErrorCode
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.mypagefragment.*
import kotlinx.android.synthetic.main.mypagefragment.view.*

class mypageFragment : Fragment(){

    //결제 내역 누를때 화면전환 되는거
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        member_paymentinfo.setOnClickListener{
            activity?.let{
                val intent = Intent(context, paylogActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.mypagefragment, container, false)
        val database = FirebaseDatabase.getInstance()
        val userid = GlobalApplication.prefs.getString("userid", "Error")
        val userRef = database.getReference("User/${userid}")
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        view.user_profile_email.text = GlobalApplication.account_email
        view.user_profile_name.text = GlobalApplication.account_name
        // 프로필 정보가 있을 경우에만 이미지 src 변환
        if (GlobalApplication.account_profile!="") {
            Glide.with(requireActivity()).load(GlobalApplication.account_profile).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),RoundedCorners(200)))).into(view.user_profile_photo)
        }

        view.member_logout.setOnClickListener {
            if(userid.length <= 10) {
                UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                    override fun onCompleteLogout() {
                        startActivity(intent)
                    }
                })
            }else{
                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signOut()
                startActivity(intent)
            }
        }

        view.member_delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("연결 끊기").setMessage("탈퇴하시겠습니까?")
            alertDialog.setPositiveButton("네") { dialog: DialogInterface?, which: Int ->
                if(userid.length <= 10) {
                    UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                        override fun onFailure(errorResult: ErrorResult?) {
                            val result = errorResult?.errorCode
                            if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                Toast.makeText(requireContext(),"네트워크 연결이 불안정합니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(),"회원탈퇴에 실패했습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onSessionClosed(errorResult: ErrorResult?) {
                            Toast.makeText(requireContext(),"로그인 세션이 닫혔습니다. 다시 로그인해 주세요.",Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }

                        override fun onNotSignedUp() {
                            Toast.makeText(requireContext(),"가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }

                        override fun onSuccess(result: Long?) {
                            Toast.makeText(requireContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            userRef.removeValue()
                            startActivity(intent)
                        }
                    })
                }else{
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.delete()?.addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(requireContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            userRef.removeValue()
                            startActivity(intent)
                        }else{
                            Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT) .show()
                        }
                    }
                }
            }
            alertDialog.setNegativeButton("아니요") { dialog, which ->
                dialog.dismiss()
            }
            alertDialog.show()
        }

        return view
    }
}