package kr.co.korearental.dongno

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.selecthomeitemfragment.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.view.*

class selecthomeitemFragment : Fragment(){

    var listreview = arrayListOf<infoReview>()
    var check : Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.selecthomeitemfragment, container, false)
        val storage = FirebaseStorage.getInstance()
        val database = FirebaseDatabase.getInstance()
        val idx = activity?.intent?.getStringExtra("cononame")
        val conoRef = database.getReference("Cono/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}")
        val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","0")}")
        val mRecyclerView=view.findViewById<RecyclerView>(R.id.reviewRV)
        //코인노래방 정보 입력
        conoRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children){
                    if(snapshot.key.equals("info")){
                        Glide.with(requireContext()).load(snapshot.child("image").value.toString()).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop()))).into(view.cono_image)
                        cono_name.text = p0.key.toString()
                        cono_address.text = snapshot.child("address").value.toString()
                        cono_callnum.text = snapshot.child("tel").value.toString()
                        cono_payinfotxt.text = snapshot.child("charge/songs/500").value.toString()+" 500원, "+snapshot.child("charge/songs/1000").value.toString()+" 1000원"
                        cono_payinfotxt2.text = "30분 "+snapshot.child("charge/time/30분").value.toString()+"원, 1시간 "+snapshot.child("charge/time/1시간").value.toString()+"원"
                    }else if(snapshot.key.equals("Review")) {
                        for(every in snapshot.children){
                            listreview.add(infoReview(every.child("name").value.toString(),every.child("review_content").value.toString(),every.child("rating").value.toString().toFloat()))
                        }
                        mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                        mRecyclerView.adapter=infoReviewAdapter(requireContext(),listreview)
                        mRecyclerView.setHasFixedSize(true)
                    }
                }
            }
        })

        // 즐겨찾기 표시
        view.bookmark.setOnClickListener {
            if(check==false){
                conoRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        for(snapshot in p0.children){
                            if(snapshot.key.equals("info")){
                                userRef.child("bookmark/${idx}").setValue(snapshot.value)
                            }
                        }
                    }
                })
                check = true
                bookmark.setImageResource(R.drawable.star2)
            }
            else{
                check = false
                bookmark.setImageResource(R.drawable.star)
                userRef.child("bookmark/${idx}").removeValue()
            }
        }

        return view
    }

}