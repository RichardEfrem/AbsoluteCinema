package uas.c14220270.absolutecinema.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.R

class ChooseScheduleTimeAdapter(private val timeSlots: List<String>) : RecyclerView.Adapter<ChooseScheduleTimeAdapter.ChooseScheduleTimeViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ChooseScheduleTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val _tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        private val _cardView = itemView.findViewById<CardView>(R.id.cardView)

        fun bind(time: String, selectedPosition: Int, position: Int, onClick: (Int) -> Unit) {
            _tvTime.text = time

            if (selectedPosition == position) {
                _cardView.setCardBackgroundColor(itemView.context.getColor(R.color.primaryButton))
            } else {
                _cardView.setCardBackgroundColor(itemView.context.getColor(R.color.secondaryButton))
            }

            itemView.setOnClickListener {
                onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseScheduleTimeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.choose_schedule_time, parent, false)
        return ChooseScheduleTimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    override fun onBindViewHolder(holder: ChooseScheduleTimeViewHolder, position: Int) {
        val time = timeSlots[position]

        holder.bind(time, selectedPosition, position) { selectedPos ->
            if (selectedPosition != selectedPos) {
                lastSelectedPosition = selectedPosition
                selectedPosition = selectedPos

                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }
}
