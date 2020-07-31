package kr.co.korearental.dongno

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.selecthomeitemfragment.*
import kotlinx.android.synthetic.main.selecthomeitemfragment.view.*

class selecthomeitemFragment : Fragment(){
    var check  = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.selecthomeitemfragment, container, false)

       view.bookmark.setOnClickListener {
           if(check==false){
               check = true
               bookmark.setImageResource(R.drawable.star2)
           }
           else{
               check = false
              bookmark.setImageResource(R.drawable.star)
           }
       }

        return view
    }

}