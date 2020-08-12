package kr.co.korearental.dongno

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminPayInfoAdapter (val context : Context, val payInfoList : ArrayList<AdmPayInfo>) :
    RecyclerView.Adapter<AdminPayInfoAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminPayInfoAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adminpayinfo, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return payInfoList.size
    }

    override fun onBindViewHolder(holder: AdminPayInfoAdapter.Holder, position: Int) {
        holder.bind(payInfoList[position], context)
    }

    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
        val date = itemView?.findViewById<TextView>(R.id.admin_pay_info_date)
        val content = itemView?.findViewById<TextView>(R.id.admin_pay_info_content)
        val bill = itemView?.findViewById<TextView>(R.id.admin_pay_info_price)

        fun bind(payInfo : AdmPayInfo, context : Context){
            date?.text = payInfo.time
            content?.text = payInfo.criteria+", "+payInfo.paymethod+" 결제"
            bill?.text = payInfo.bill.toString()+"원"
        }
    }
}