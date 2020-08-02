package kr.co.korearental.dongno

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class infoReviewAdapter(val context : Context, val ListReview : ArrayList<infoReview>) :
    RecyclerView.Adapter<infoReviewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): infoReviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_conoreview,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return ListReview.size
    }

    override fun onBindViewHolder(holder: infoReviewAdapter.Holder, position: Int) {
        holder?.bind(ListReview[position],context)
    }

    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
        val conoName = itemView?.findViewById<TextView>(R.id.txtname)
        val rating = itemView?.findViewById<RatingBar>(R.id.ratingBar)
        val content = itemView?.findViewById<TextView>(R.id.txtcontent)

        fun bind(review : infoReview, context : Context){
            conoName?.text=review.name
            rating?.rating=review.rating
            content?.text=review.content
        }
    }
}