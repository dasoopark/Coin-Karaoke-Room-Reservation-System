package kr.co.korearental.dongno

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.homefragment.view.*
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


open class homeFragment : Fragment() {

    val database = FirebaseDatabase.getInstance()
    val conoRef = database.getReference("Cono/${GlobalApplication.area1}/${GlobalApplication.area2}/${GlobalApplication.area3}")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.homefragment, container, false)
        val mRecyclerView=view.findViewById(R.id.conoRV) as RecyclerView
        // 지도로 특정 위치를 불러왔을 때의 경우로 코드 수정 필요
        GlobalApplication.search_area1 = GlobalApplication.area1
        GlobalApplication.search_area2 = GlobalApplication.area2
        GlobalApplication.search_area3 = GlobalApplication.area3
        view.homefrag_addresstxt.text = "${GlobalApplication.search_area1} ${GlobalApplication.search_area2} ${GlobalApplication.search_area3}"

        conoRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                GlobalApplication.listcono.clear()
                for (snapshot in p0.children){
                    val name=snapshot.key.toString()
                    val address=snapshot.child("info/address").value.toString()
                    val imgUrl=snapshot.child("info/image").value.toString()
                    val x=snapshot.child("info/x").value.toString()
                    val y=snapshot.child("info/y").value.toString()
                    var rating=snapshot.child("Review/rating_avg").value.toString()
                    if(rating == "null"){
                        rating = "0.0"
                    }
                    GlobalApplication.listcono.add(Cono(imgUrl, name, address, rating.toFloat(), x.toDouble(), y.toDouble()))
                }
                mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                mRecyclerView.adapter=ListConoAdapter(requireContext(), GlobalApplication.listcono)
                mRecyclerView.setHasFixedSize(true)
            }
        })

        view.searchmap.setOnClickListener {
            val intent = Intent(this.activity, MapSearch::class.java)
            startActivity(intent)
        }

        return view

    }

}
