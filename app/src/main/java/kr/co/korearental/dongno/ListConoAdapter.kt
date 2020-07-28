package kr.co.korearental.dongno

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ListConoAdapter(val context: Context, val ListCono : ArrayList<Cono> ) :
    RecyclerView.Adapter<ListConoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListConoAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cono, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return ListCono.size
    }

    override fun onBindViewHolder(holder: ListConoAdapter.Holder, position: Int) {
        holder?.bind(ListCono[position],context)
        holder.itemView.setOnClickListener {
            Toast.makeText(context,"Clicked: ${ListCono.get(position).name}", Toast.LENGTH_SHORT).show()
        }
    }
    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!){
        val conoImg = itemView?.findViewById<ImageView>(R.id.imgcono)
        val conoName= itemView?.findViewById<TextView>(R.id.txtname)
        val conoAddress= itemView?.findViewById<TextView>(R.id.txtaddress)
        val conoRating= itemView?.findViewById<RatingBar>(R.id.rating)
        val conoDistance= itemView?.findViewById<TextView>(R.id.txtdistance)

        fun bind(cono:Cono, context:Context){
            conoImg?.setImageDrawable(cono.img)
            conoName?.text=cono.name
            conoAddress?.text=cono.address
            conoRating?.rating=cono.rating
            conoDistance?.text = cono.distance.toString()+"m"
        }
    }
}

