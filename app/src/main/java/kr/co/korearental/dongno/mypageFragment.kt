package kr.co.korearental.dongno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.mypagefragment.*

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

        return inflater.inflate(R.layout.mypagefragment, container, false)
    }
}