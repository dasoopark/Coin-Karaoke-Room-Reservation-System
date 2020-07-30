package kr.co.korearental.dongno

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.RatingBar
import com.google.firebase.database.FirebaseDatabase

class reviewActivity(context : Context) {
    private val dlg = Dialog(context)
    private val database = FirebaseDatabase.getInstance()
    private lateinit var reviewcontent : EditText
    private lateinit var ratingbar : RatingBar
    fun start(content : String){
        dlg.setContentView(R.layout.reviewdialog)
        dlg.setCancelable(false)

        reviewcontent= dlg.findViewById(R.id.reviewContent)
        ratingbar=dlg.findViewById(R.id.ratingBar)


    }
}