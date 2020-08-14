package kr.co.korearental.dongno

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import io.realm.RealmCollection
import kotlinx.android.synthetic.main.alarmfragment.*

class AlarmAdapter(realmResult: OrderedRealmCollection<Todo>) :
    RealmBaseAdapter<Todo>(realmResult) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_alarm, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        if (adapterData != null) {
            val item = adapterData!![position]
            vh.cononame.text= item.cono_name
            vh.conotime.text= item.cono_time
            vh.conodate.text= item.cono_date
        }

        return view
    }

    override fun getItemId(position: Int): Long {
        if (adapterData != null) {
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }

}

class ViewHolder(view: View) {
    val cononame:  TextView = view.findViewById(R.id.cononame_text)
    val conotime: TextView = view.findViewById(R.id.cono_time)
    val conodate: TextView = view.findViewById(R.id.conodate_text)
}
