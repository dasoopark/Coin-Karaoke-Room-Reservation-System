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
import com.google.firebase.storage.FirebaseStorage

class ListConoAdapter(val context: Context, val ListCono: ArrayList<Cono> ) :
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
            val intent = Intent(context, selecthomeitemActivity::class.java)
            intent.putExtra("cononame",ListCono.get(position).name)
            context.startActivity(intent)
            //Toast.makeText(context,"Clicked: ${ListCono.get(position).name}", Toast.LENGTH_SHORT).show()
        }
    }
    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!){
        val conoImg = itemView?.findViewById<ImageView>(R.id.imgcono)
        val conoName= itemView?.findViewById<TextView>(R.id.txtname)
        val conoAddress= itemView?.findViewById<TextView>(R.id.txtaddress)
        val conoRating= itemView?.findViewById<RatingBar>(R.id.rating)
        val conoDistance= itemView?.findViewById<TextView>(R.id.txtdistance)

        fun bind(cono:Cono, context:Context){
            val storage = FirebaseStorage.getInstance()
            val ref = storage.getReferenceFromUrl(cono.img)
            ref.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes->
                val bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                conoImg?.setImageBitmap(bmp)
            }.addOnFailureListener{}
            conoName?.text=cono.name
            conoAddress?.text=cono.address
            conoRating?.rating=cono.rating
            conoDistance?.text = cono.distance.toString()+"m"
        }
    }
}

