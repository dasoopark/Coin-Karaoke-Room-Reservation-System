package kr.co.korearental.dongno

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PaylogAdapter(val context : Context, val Listpaylog : ArrayList<Paylog>) :
    RecyclerView.Adapter<PaylogAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaylogAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_paylog,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return Listpaylog.size
    }

    override fun onBindViewHolder(holder: PaylogAdapter.Holder, position: Int) {
        holder.bind(Listpaylog[position],context)
    }

    inner class Holder(itemView : View): RecyclerView.ViewHolder(itemView){
        val cononame = itemView.findViewById<TextView>(R.id.text_cono_name)
        val date = itemView.findViewById<TextView>(R.id.text_date)
        val price = itemView.findViewById<TextView>(R.id.text_price)

        fun bind(paylog : Paylog,context : Context){
            cononame.text=paylog.cononame
            date.text=paylog.paydate
            price.text=paylog.price
        }
    }

}