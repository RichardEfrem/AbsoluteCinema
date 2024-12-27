package uas.c14220270.absolutecinema.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.R

class ChooseScheduleDateAdapter(private val dateSlots: List<String>) : RecyclerView.Adapter<ChooseScheduleDateAdapter.ChooseScheduleDateViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ChooseScheduleDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val _tvDay = itemView.findViewById<TextView>(R.id.tvDay)
        private val _tvMonth = itemView.findViewById<TextView>(R.id.tvMonth)
        private val _cardView = itemView.findViewById<CardView>(R.id.cardView)

        fun bind(date: String, selectedPosition: Int, position: Int, onClick: (Int) -> Unit) {
            val dateParts = date.split("/")
            if (dateParts.size == 3) {
                _tvDay.text = dateParts[0]
                _tvMonth.text = dateParts[1] + " " + dateParts[2]


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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseScheduleDateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.choose_schedule_date, parent, false)
        return ChooseScheduleDateViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dateSlots.size
    }

    override fun onBindViewHolder(holder: ChooseScheduleDateViewHolder, position: Int) {
        val date = dateSlots[position]

        holder.bind(date, selectedPosition, position) { selectedPos ->
            if (selectedPosition != selectedPos) {
                lastSelectedPosition = selectedPosition
                selectedPosition = selectedPos

                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }
}
