package kr.co.korearental.dongno

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
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

        mCurrentLatitude = GlobalApplication.prefs.getString("latitude", "0.0").toDouble()
        mCurrentLongitude = GlobalApplication.prefs.getString("longitude", "0.0").toDouble()
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
        //현재위치 표시해주는 점 표시
        naverMap.locationOverlay.isVisible = true
        naverMap.locationOverlay.position = LatLng(mCurrentLatitude, mCurrentLongitude)
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.locationSource = locationSource
        //지도 카메라 위치(현재 위치를 매개변수로 받아와서 설정)
        naverMap.cameraPosition = CameraPosition(LatLng(mCurrentLatitude, mCurrentLongitude), 14.0)
        // 5~18 사이
    }
}