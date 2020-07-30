package kr.co.korearental.dongno

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val database = FirebaseDatabase.getInstance()
        val userid = activity?.intent?.getStringExtra("userid")
        val userRef = database.getReference("User/${userid}/info")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    if (snapshot.key.equals("email")) {
                        user_profile_email.text = snapshot.value.toString()
                    }else if (snapshot.key.equals("name")){
                        user_profile_name.text = snapshot.value.toString()
                    }else if (snapshot.key.equals("thumbnail")){
                        Glide.with(requireActivity()).load(snapshot.value.toString()).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(), RoundedCorners(200)))).into(user_profile_photo)
                    }
                }
            }
        })

        val view: View = inflater.inflate(R.layout.mypagefragment, container, false)

        view.member_logout.setOnClickListener {
            UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                override fun onCompleteLogout() {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            })
        }

        view.member_delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("연결 끊기").setMessage("탈퇴하시겠습니까?")
            alertDialog.setPositiveButton("네") { dialog: DialogInterface?, which: Int ->
                UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                    override fun onFailure(errorResult: ErrorResult?) {
                        val result = errorResult?.errorCode
                        if (result== ApiErrorCode.CLIENT_ERROR_CODE) {
                            Toast.makeText(requireContext(), "네트워크 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "회원탈퇴에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onSessionClosed(errorResult: ErrorResult?) {
                        Toast.makeText(requireContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }

                    override fun onNotSignedUp() {
                        Toast.makeText(requireContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }

                    override fun onSuccess(result: Long?) {
                        Toast.makeText(requireContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()
                        userRef.removeValue()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                })
            }
            alertDialog.setNegativeButton("아니요") { dialog, which ->
                dialog.dismiss()
            }
            alertDialog.show()
        }

        return view
    }
}