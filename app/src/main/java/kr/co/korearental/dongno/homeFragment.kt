package kr.co.korearental.dongno

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.homefragment.*


class homeFragment : Fragment() {
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
        mRecyclerView.adapter=ListConoAdapter(requireContext(),listcono)
        mRecyclerView.setHasFixedSize(true)

        return view
    }

}