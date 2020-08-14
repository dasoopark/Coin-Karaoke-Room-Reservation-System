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
    // AdminPay와 AdmPay의 list 정보를 잇기 위한 Adapter
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
            // 리사이클러뷰의 아이템 중 하나를 눌렀을 경우 해당 item의 date 정보를 intent로 넘기고
            // 해당 일자의 상세 결제 내역을 알려주는 액티비티로 이동시킨다
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
            // pay.date가 yyyy-mm-dd 형식이기 때문에 yy년 mm월 dd일로 사용자에게 표시하기 위하여 아래와 같이 수정
            date?.text = pay.date.substring(2, 4)+"년 "+pay.date.substring(5, 7)+"월 "+pay.date.substring(8, 10)+"일"
            bill?.text = pay.bill.toString()+"원"
        }
    }
}