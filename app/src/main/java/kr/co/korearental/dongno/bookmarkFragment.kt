package kr.co.korearental.dongno

import android.os.Bundle
import android.renderscript.Sampler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.*
import kr.co.korearental.dongno.bookmark
import java.lang.ref.Reference

class bookmarkFragment  : Fragment(){

    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","0")}/bookmark" )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.bookmarkfragment, container, false)
        val mRecyclerView=view.findViewById(R.id.bookmarkRV) as RecyclerView
        lateinit var name : String
        lateinit var address : String
        lateinit var imgUrl : String
        lateinit var conoRef : DatabaseReference
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                GlobalApplication.listbookmark.clear()
                for (area1 in p0.children) {
                    for (area2 in area1.children) {
                        for (area3 in area2.children) {
                            for (cono in area3.children) {
                                conoRef=database.getReference("${cono.value.toString()}")
                                name=cono.key.toString()
                                conoRef.addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onCancelled(p1: DatabaseError) {}
                                    override fun onDataChange(p1: DataSnapshot) {
                                        for(snapshot in p1.children){
                                            if(snapshot.key.equals("info")){
                                                address=snapshot.child("address").value.toString()
                                                imgUrl=snapshot.child("image").value.toString()
                                                GlobalApplication.listbookmark.add(bookmark(imgUrl,name,address,3.1.toFloat()))
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    }
                }
                mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                mRecyclerView.adapter=bookmarkAdapter(requireContext(),GlobalApplication.listbookmark)
                mRecyclerView.setHasFixedSize(true)
            }
            
        })


        return view
    }
}