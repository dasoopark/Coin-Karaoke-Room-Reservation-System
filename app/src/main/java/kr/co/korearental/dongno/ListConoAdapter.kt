package kr.co.korearental.dongno

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.mypagefragment.view.*

class ListConoAdapter(val context : Context, val ListCono: ArrayList<Cono> ) :
    RecyclerView.Adapter<ListConoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListConoAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cono, parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return ListCono.size
    }

    override fun onBindViewHolder(holder: ListConoAdapter.Holder, position: Int) {
        holder.bind(ListCono[position],context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, selecthomeitemActivity::class.java)
            intent.putExtra("cononame", ListCono[position].name)
            GlobalApplication.search_cono=ListCono[position].name
            intent.putExtra("x", ListCono[position].x)
            intent.putExtra("y", ListCono[position].y)
            context.startActivity(intent)
        }
    }
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        val conoImg = itemView.findViewById<ImageView>(R.id.imgcono)
        val conoName= itemView.findViewById<TextView>(R.id.txtname)
        val conoAddress= itemView.findViewById<TextView>(R.id.txtaddress)
        val conoRating= itemView.findViewById<RatingBar>(R.id.rating)

        fun bind(cono:Cono, context:Context){
            Glide.with(context).load(cono.img).apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop()))).into(conoImg)
            conoName.text=cono.name
            conoAddress.text=cono.address
            conoRating.rating=cono.rating
        }
    }
}

