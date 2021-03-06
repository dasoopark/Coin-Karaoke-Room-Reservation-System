package kr.co.korearental.dongno

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminReviewAdapter(val context : Context, val reviewList : ArrayList<AdmReview>) :
    // AdminReview와 AdmReview의 list 정보를 잇기 위한 Adapter
    RecyclerView.Adapter<AdminReviewAdapter.Holder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminReviewAdapter.Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_adminreview, parent,false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return reviewList.size
        }

        override fun onBindViewHolder(holder: AdminReviewAdapter.Holder, position: Int) {
            holder.bind(reviewList[position],context)
        }

        inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
            val conoName = itemView?.findViewById<TextView>(R.id.admin_txtname)
            val rating = itemView?.findViewById<RatingBar>(R.id.admin_ratingBar)
            val date = itemView?.findViewById<TextView>(R.id.admin_txtdate)
            val content = itemView?.findViewById<TextView>(R.id.admin_txtcontent)

            fun bind(review : AdmReview, context : Context){
                val tmp_name=review.name
                // 사용자의 이름을 전부 표출하지 않기 위해 중간 글자는 자른다.
                if (tmp_name.length == 2){
                    conoName?.text = tmp_name.substring(0,1)+"*"
                }else {
                    conoName?.text = tmp_name.substring(0, 1) + "*" + tmp_name.substring(2, 3)
                }
                rating?.rating=review.rating
                date?.text = review.date
                content?.text=review.content
            }
        }
}