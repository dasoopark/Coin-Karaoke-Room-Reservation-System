package kr.co.korearental.dongno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.payment_song.view.*

class payment_songFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.payment_song, container, false)

        view.room_choiceButton.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.roomchoice_dialog, null)
            builder.setView(dialogView)
                .setPositiveButton("예약") { dialogInterface, i ->

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
            // Dialog 사이즈 조절 하기
        }
        return view
    }



}
