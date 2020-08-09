package kr.co.korearental.dongno

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class paymentViewAdapter (fm : FragmentManager) : FragmentStatePagerAdapter(fm){

    //뷰 페이저와 연동시킬 fragment들을 모아둔 곳
    private var fragments : ArrayList<paymentActivity> = ArrayList()

    //position에 위치한 프래그먼트를 반환하는 함수다. 우리는 fragments라는 ArrayList에 담아뒀으므로 position을 인덱스 삼아 반환시켜주면 된다.
    override fun getItem(position: Int): Fragment{
        return if(position == 0){
            payment_songFragment()
        }else{
            payment_timeFragment()
        }
    }

    //getCount()는 page의 개수를 반환한다. fragments 배열의 크기가 곧 page의 개수가 될 것이다.
    override fun getCount(): Int{
        return 2
    }



    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "곡"
            1 -> "시간"
            else -> {return "탭메뉴3"}
        }
    }
}


