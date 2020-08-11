package kr.co.korearental.dongno

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.item_cono.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.view.*

class selecthomeitemFragment : Fragment(){

    var listreview = arrayListOf<infoReview>()
    var chk = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.selecthomeitemfragment, container, false)
        val database = FirebaseDatabase.getInstance()
        val idx = activity?.intent?.getStringExtra("cononame")
        val conoRef = database.getReference("Cono/${GlobalApplication.search_area1}/${GlobalApplication.search_area2}/${GlobalApplication.search_area3}/${idx}")
        val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","0")}")
        val mRecyclerView=view.findViewById<RecyclerView>(R.id.reviewRV)
        lateinit var imageurl : String
        var rating_avg : String = "0.0"

        //코인노래방 정보 입력
        conoRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children){
                    if(snapshot.key.equals("info")){
                        imageurl = snapshot.child("image").value.toString()
                        Glide.with(requireContext()).load(imageurl).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop()))).into(view.cono_image)
                        cono_name.text = p0.key.toString()
                        cono_address.text = snapshot.child("address").value.toString()
                        cono_callnum.text = snapshot.child("tel").value.toString()
                        cono_payinfotxt.text = snapshot.child("charge/songs/500").value.toString()+" 500원, "+snapshot.child("charge/songs/1000").value.toString()+" 1000원"
                        cono_payinfotxt2.text = "30분 "+snapshot.child("charge/time/30분").value.toString()+"원, 1시간 "+snapshot.child("charge/time/1시간").value.toString()+"원"
                    }else if(snapshot.key.equals("Review")) {
                        reviewtext.text = "리뷰 > (${snapshot.childrenCount-2})"
                        for(every in snapshot.children){
                            if(every.key.equals("rating_avg")){
                                rating_avg = every.value.toString()
                                if(rating_avg == "null"){
                                    rating_avg = "0.0"
                                }
                            }else if(every.key.equals("index")){
                                continue
                            }else {
                                listreview.add(0, infoReview(
                                        every.child("name").value.toString(),
                                        every.child("review_content").value.toString(),
                                        every.child("rating").value.toString().toFloat()
                                    )
                                )
                            }
                        }
                        mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                        mRecyclerView.adapter=infoReviewAdapter(requireContext(),listreview)
                        mRecyclerView.setHasFixedSize(true)
                    }
                }
            }
        })

        userRef.child("bookmark").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for(area1 in p0.children) {
                    for(area2 in area1.children){
                        for (area3 in area2.children){
                            for(name in area3.children){
                                if (area1.key.equals(GlobalApplication.area1) && area2.key.equals(GlobalApplication.area2) && area3.key.equals(GlobalApplication.area3) && name.key.equals(idx)) {
                                    chk = true
                                    userRef.child("bookmark/${area1.key.toString()}/${area2.key.toString()}/${area3.key.toString()}/${name.key.toString()}/rating_avg").setValue("$rating_avg")
                                    bookmark.setImageResource(R.drawable.star2)
                                }
                            }
                        }
                    }
                }
            }
        })

        view.bookmark.setOnClickListener {
            if(!chk) {
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/address").setValue("${cono_address.text}")
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/image").setValue("$imageurl")
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/tel").setValue("${cono_callnum.text}")
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/rating_avg").setValue("$rating_avg")
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/x").setValue("${activity?.intent?.getDoubleExtra("x", 0.0).toString()}")
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}/y").setValue("${activity?.intent?.getDoubleExtra("y", 0.0).toString()}")
                chk = true
                bookmark.setImageResource(R.drawable.star2)
                Toast.makeText(requireContext(), "즐겨찾기 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }else {
                userRef.child("bookmark/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}/${idx}").removeValue()
                chk = false
                bookmark.setImageResource(R.drawable.star)
                Toast.makeText(requireContext(), "즐겨찾기 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        view.nmap.setOnClickListener {
            val url ="nmap://route/walk?dlat=${activity?.intent?.getDoubleExtra("x", 0.0).toString()}&dlng=${activity?.intent?.getDoubleExtra("y", 0.0)}&dname=$idx&appname=kr.co.korearental.dongno"
            println(url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            val list: List<ResolveInfo> =
                requireContext().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.isEmpty()) {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")))
            } else {
                requireContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        return view
    }
}