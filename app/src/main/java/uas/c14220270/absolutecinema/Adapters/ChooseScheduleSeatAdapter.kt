package uas.c14220270.absolutecinema.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.Models.ChooseScheduleSeatModel
import uas.c14220270.absolutecinema.R

class ChooseScheduleSeatAdapter(
    private val seatList : List<ChooseScheduleSeatModel>,
    private val context : Context,
    private val selectedSeat : SelectedSeat
) : RecyclerView.Adapter<ChooseScheduleSeatAdapter.ChooseScheduleSeatViewHolder>() {

    private val selectedSeatNumber = ArrayList<Int>()

    class ChooseScheduleSeatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val seatLayout: ConstraintLayout = itemView.findViewById(R.id.seat)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseScheduleSeatViewHolder {
        return ChooseScheduleSeatViewHolder(LayoutInflater.from(context).inflate(R.layout.choose_schedule_seats, parent, false))
    }

    override fun getItemCount(): Int {
        return seatList.size
    }

    override fun onBindViewHolder(holder: ChooseScheduleSeatViewHolder, position: Int) {
        val seat = seatList[position]

        val seatLayout = holder.itemView.findViewById<ConstraintLayout>(R.id.seat)
        val seatNumberText = holder.itemView.findViewById<TextView>(R.id.seatNumberText)

        seatNumberText.text = seat.seatNumber.toString()

        when (seat.status) {
            ChooseScheduleSeatModel.SeatStatus.AVAILABLE -> {
                seatLayout.setBackgroundResource(R.drawable.ic_seat_available)
                seatLayout.isEnabled = true
            }
            ChooseScheduleSeatModel.SeatStatus.BOOKED -> {
                seatLayout.setBackgroundResource(R.drawable.ic_seat_reserved)
                seatLayout.isEnabled = false
            }
            ChooseScheduleSeatModel.SeatStatus.SELECTED -> {
                seatLayout.setBackgroundResource(R.drawable.ic_seat_selected)
                seatLayout.isEnabled = true
            }
        }

        seatLayout.setOnClickListener {
            if (seat.status == ChooseScheduleSeatModel.SeatStatus.AVAILABLE) {
                seat.status = ChooseScheduleSeatModel.SeatStatus.SELECTED
                selectedSeatNumber.add(seat.seatNumber)
                seatLayout.setBackgroundResource(R.drawable.ic_seat_selected)
                notifyItemChanged(position)
            } else if (seat.status == ChooseScheduleSeatModel.SeatStatus.SELECTED) {
                seat.status = ChooseScheduleSeatModel.SeatStatus.AVAILABLE
                selectedSeatNumber.remove(seat.seatNumber)
                seatLayout.setBackgroundResource(R.drawable.ic_seat_available)
                notifyItemChanged(position)
            }

            val selected = selectedSeatNumber.joinToString(",")
            selectedSeat.Return(selected, selectedSeatNumber.size)
        }
    }

    interface SelectedSeat {
        fun Return(selectedName : String, num : Int)
    }
}