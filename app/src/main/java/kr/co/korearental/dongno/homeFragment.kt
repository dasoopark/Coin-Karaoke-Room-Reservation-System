package kr.co.korearental.dongno

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.homefragment.view.*
import kotlinx.coroutines.delay


open class homeFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var mCurrentLatitude: Double = 0.0
    private var mCurrentLongitude: Double = 0.0

    var listcono = arrayListOf<Cono>()
    val database = FirebaseDatabase.getInstance()
    val conoRef = database.getReference("Cono")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.homefragment, container, false)
        val mRecyclerView=view.findViewById(R.id.conoRV) as RecyclerView
        lateinit var name : String
        lateinit var address : String
        lateinit var imgUrl : String
        var index : Int=0
        val mapurl = "https://map.naver.com/v5/search/%EC%BD%94%EC%9D%B8%EB%85%B8%EB%9E%98%EB%B0%A9?c=14107361.2597401,4487288.2959382,14,0,0,0,dh"
        val USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36"
        var loadingFinished = true
        var redirect = false

        val webView = WebView(requireContext())
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = USER_AGENT
        webView.addJavascriptInterface(MyJavascriptInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, urlNewString: String? ): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                webView.loadUrl(urlNewString)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loadingFinished = false
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!redirect) {
                    loadingFinished = true;
                    Handler().postDelayed({
                        view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
                    }, 2500) // 2.5 초 후에 실행
                } else {
                    redirect = false;
                }
            }
        }

        conoRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children){
                    name=snapshot.child("info/name").value.toString()
                    address=snapshot.child("info/address").value.toString()
                    imgUrl=snapshot.child("info/image").value.toString()
                    index++ //임시 키
                    listcono.add(Cono(index,imgUrl,name,address,3.1.toFloat(),126))
                }
                mRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                mRecyclerView.adapter=ListConoAdapter(requireContext(), listcono)
                mRecyclerView.setHasFixedSize(true)
            }
        })

        view.searchButton.setOnClickListener {
            mCurrentLatitude = GlobalApplication.prefs.getString("latitude", "0.0").toDouble()
            mCurrentLongitude = GlobalApplication.prefs.getString("longitude", "0.0").toDouble()

            webView.loadUrl(mapurl)

            val intent = Intent(this.activity, MapSearch::class.java)
            startActivity(intent)
        }
        return view
    }

}