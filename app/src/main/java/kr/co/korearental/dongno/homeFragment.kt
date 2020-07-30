package kr.co.korearental.dongno

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class homeFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var mCurrentLatitude: Double = 0.0
    private var mCurrentLongitude: Double = 0.0

    var listcono = arrayListOf<Cono>()
    val database = FirebaseDatabase.getInstance()
    val storage = FirebaseStorage.getInstance()
    val conoRef = database.getReference("Cono")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.homefragment, container, false)
        val mRecyclerView=view.findViewById(R.id.conoRV) as RecyclerView
        lateinit var name : String
        lateinit var address : String
        lateinit var imgUrl : String

        conoRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children){
                    name=snapshot.child("info/name").value.toString()
                    address=snapshot.child("info/address").value.toString()
                    imgUrl=snapshot.child("info/image").value.toString()
                    listcono.add(Cono(imgUrl,name,address,3.1.toFloat(),126))
                }
                mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                mRecyclerView.adapter=ListConoAdapter(requireContext(), listcono)
                mRecyclerView.setHasFixedSize(true)
            }
        })

        view.searchButton.setOnClickListener {
            mCurrentLatitude = activity?.intent?.getDoubleExtra("latitude", 0.0)!!
            mCurrentLongitude = activity?.intent?.getDoubleExtra("longitude", 0.0)!!

            val intent = Intent(this.activity, MapSearch::class.java)
            intent.putExtra("latitude", mCurrentLatitude)
            intent.putExtra("longitude", mCurrentLongitude)
            startActivity(intent)
        }

        return view
    }

}