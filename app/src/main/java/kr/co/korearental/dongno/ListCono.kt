package kr.co.korearental.dongno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_cono.*

class ListCono : AppCompatActivity() {

    var listcono = arrayListOf<Cono>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_cono)

        val Cadapter = ListConoAdapter(this,listcono)
        conoRV.adapter=Cadapter

        val lm= LinearLayoutManager(this)
        conoRV.layoutManager=lm
        conoRV.setHasFixedSize(true)

    }
}