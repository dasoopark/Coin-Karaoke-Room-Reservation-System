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
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.homefragment.view.*


class homeFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var mCurrentLatitude: Double = 0.0
    private var mCurrentLongitude: Double = 0.0

    var listcono = arrayListOf<Cono>(
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"B코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"C코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"D코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126),
        Cono(0,"A코인노래방","경기도 시흥시 정왕1동",3.1.toFloat(),126)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.homefragment, container, false)
        val mRecyclerView=view.findViewById(R.id.conoRV) as RecyclerView
        mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mRecyclerView.adapter=ListConoAdapter(requireContext(), listcono)
        mRecyclerView.setHasFixedSize(true)

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