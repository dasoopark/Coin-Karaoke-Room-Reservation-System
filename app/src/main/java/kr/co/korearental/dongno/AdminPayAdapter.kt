package kr.co.korearental.dongno

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminPayAdapter (val context : Context, val payList : ArrayList<AdmPay>) :
    RecyclerView.Adapter<AdminPayAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminPayAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adminpay, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return payList.size
    }

    override fun onBindViewHolder(holder: AdminPayAdapter.Holder, position: Int) {
        holder.bind(payList[position], context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AdminPayInfo::class.java)
            intent.putExtra("date", payList[position].date)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
        val date = itemView?.findViewById<TextView>(R.id.admin_pay_date)
        val bill = itemView?.findViewById<TextView>(R.id.admin_pay_price)

        fun bind(pay : AdmPay, context : Context){
            date?.text = pay.date.substring(2, 4)+"년 "+pay.date.substring(5, 7)+"월 "+pay.date.substring(8, 10)+"일"
            bill?.text = pay.bill.toString()+"원"
        }
    }
}