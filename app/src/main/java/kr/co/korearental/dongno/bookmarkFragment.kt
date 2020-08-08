package kr.co.korearental.dongno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class bookmarkFragment  : Fragment(){

    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("User/${GlobalApplication.prefs.getString("userid","0")}/bookmark")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.homefragment, container, false)
        val mRecyclerView=view.findViewById(R.id.bookmarkRV) as RecyclerView
        lateinit var name : String
        lateinit var address : String
        lateinit var imgUrl : String

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                GlobalApplication.listbookmark.clear()
                for (snapshot in p0.children){
                    name=snapshot.key.toString()
                    address=snapshot.child("info/address").value.toString()
                    imgUrl=snapshot.child("info/image").value.toString()
                    GlobalApplication.listbookmark.add(bookmark(imgUrl,name,address,3.1.toFloat()))
                }
                mRecyclerView.layoutManager= LinearLayoutManager(requireContext())
                mRecyclerView.adapter=bookmarkAdapter(requireContext(), GlobalApplication.listbookmark)
                mRecyclerView.setHasFixedSize(true)
            }
        })
        return view
    }
}