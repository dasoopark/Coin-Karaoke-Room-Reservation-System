package kr.co.korearental.dongno

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource


class MapSearch: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var mCurrentLatitude: Double = 0.0
    private var mCurrentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapsearch)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        mCurrentLatitude = GlobalApplication.latitude
        mCurrentLongitude = GlobalApplication.longitude
        Toast.makeText(applicationContext, "$mCurrentLatitude , $mCurrentLongitude", Toast.LENGTH_SHORT).show()

        val options = NaverMapOptions().camera(CameraPosition(LatLng(mCurrentLatitude, mCurrentLongitude), 8.0))
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        //실내지도 활성화
        naverMap.isIndoorEnabled = true
        //왼쪽 아래 현재위치 아이콘 표시
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false

        //마커 리스트
        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(applicationContext){
            override fun getText(p0: InfoWindow): CharSequence {
                return "${infoWindow.marker?.tag}" as CharSequence? ?:""
            }
        }
        infoWindow.setOnClickListener {
            /*
            val url ="nmap://route/walk?dlat=${infoWindow.marker!!.position.latitude}&dlng=${infoWindow.marker!!.position.longitude}&dname=${infoWindow.marker!!.tag}&appname=kr.co.korearental.dongno"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            val list =packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list == null || list.isEmpty()) {
                applicationContext.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.nhn.android.nmap")))
            } else {
                applicationContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
            }
             */
            val intent = Intent(this, selecthomeitemActivity::class.java)
            intent.putExtra("cononame",infoWindow.marker!!.tag.toString())
            this.startActivity(intent)
            true
        }
        for(cono in GlobalApplication.listcono){
            val marker = Marker()
            marker.tag = cono.name
            marker.position = LatLng(cono.x, cono.y)
            marker.map = naverMap
            marker.setOnClickListener {
                infoWindow.open(marker)
                true
            }
        }

        //현재위치 표시해주는 점 표시
        naverMap.locationOverlay.isVisible = true
        naverMap.locationOverlay.position = LatLng(mCurrentLatitude, mCurrentLongitude)
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.locationSource = locationSource
        //지도 카메라 위치(현재 위치를 매개변수로 받아와서 설정), zoom은 5~18 사이
        naverMap.cameraPosition = CameraPosition(LatLng(mCurrentLatitude, mCurrentLongitude), 14.0)
        // 지도 클릭 시 정보창 사라지도록
        naverMap.setOnMapClickListener { pointF, latLng ->
            infoWindow.close()
        }
    }
}