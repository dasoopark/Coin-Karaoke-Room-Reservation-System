package kr.co.korearental.dongno

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.selecthomeitemfragment.*

class bookmarkAdapter(val context : Context, val Listbookmark : ArrayList<bookmark>) :
    RecyclerView.Adapter<bookmarkAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookmarkAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return Listbookmark.size
    }

    override fun onBindViewHolder(holder: bookmarkAdapter.Holder, position: Int) {
        holder.bind(Listbookmark[position],context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, selecthomeitemActivity::class.java)
            intent.putExtra("cononame", Listbookmark[position].name)
            intent.putExtra("x", Listbookmark[position].x)
            intent.putExtra("y", Listbookmark[position].y)
            GlobalApplication.search_area1 = Listbookmark[position].area1
            GlobalApplication.search_area2 = Listbookmark[position].area2
            GlobalApplication.search_area3 = Listbookmark[position].area3
            context.startActivity(intent)
        }
    }
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        val conoImg = itemView.findViewById<ImageView>(R.id.imgcono)
        val conoName= itemView.findViewById<TextView>(R.id.txtname)
        val conoAddress= itemView.findViewById<TextView>(R.id.txtaddress)
        val conoRating= itemView.findViewById<RatingBar>(R.id.rating)

        fun bind(bm:bookmark, context:Context){
            Glide.with(context).load(bm.img).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop()))).into(conoImg)
            conoName.text=bm.name
            conoAddress.text=bm.address
            conoRating.rating=bm.rating
        }
    }
}