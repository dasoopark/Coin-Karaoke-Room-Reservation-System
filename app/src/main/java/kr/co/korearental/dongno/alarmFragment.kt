package kr.co.korearental.dongno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.alarmfragment.*
import kotlinx.android.synthetic.main.alarmfragment.view.*


class alarmFragment : Fragment() {
    val realm = Realm.getDefaultInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view:View =inflater.inflate(R.layout.alarmfragment, container, false)
        val realmResult = realm.where<Todo>().findAll().sort("id", Sort.DESCENDING)
        val adapter = AlarmAdapter(realmResult)
        view.alarmLv.adapter = adapter

        view.deleteall_button.setOnClickListener{
            realm.beginTransaction()
            val deletetodo = realm.where<Todo>().findAll()
            deletetodo.deleteAllFromRealm()// deleteFromRealm 메서드로 삭제
            realm.commitTransaction()
            Toast.makeText(requireContext(), "알림 내역이 모두 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }

        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }

        return view
    }
}