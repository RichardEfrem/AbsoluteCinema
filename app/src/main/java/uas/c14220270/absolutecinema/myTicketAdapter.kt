package uas.c14220270.absolutecinema

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myTicketAdapter(private val ticketList: MutableList<MutableMap<String, String>>) : RecyclerView.Adapter<myTicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val movieDate: TextView = itemView.findViewById(R.id.movieDate)
        val movieTime: TextView = itemView.findViewById(R.id.movieTime)
        val movieSeats: TextView = itemView.findViewById(R.id.movieSeats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]

        val title = ticket["movie_title"] ?: "Unknown"
        val date = ticket["date"] ?: "Unknown"
        val time = ticket["time"] ?: "Unknown"
        val seats = ticket["seats"] ?: "Unknown"
        // Bind data to views
        holder.movieTitle.text = title
        holder.movieDate.text = "Date :  $date"
        holder.movieTime.text = "Time : $time"
        holder.movieSeats.text = "Seats : $seats"

        // Map posterUrl to drawable resource ID
        val posterUrl = ticket["posterUrl"] ?: ""
        val context = holder.moviePoster.context
        val resourceId = context.resources.getIdentifier(posterUrl, "drawable", context.packageName)

        if (resourceId != 0) {
            // If a valid drawable resource is found
            holder.moviePoster.setImageResource(resourceId)
        } else {
            // Use a default placeholder image if resource is not found
            holder.moviePoster.setImageResource(R.drawable.sonic3)
        }
        holder.moviePoster.setOnClickListener {
            val ticketId = ticket["ticketId"]
            val intent = Intent(context, TicketPage::class.java)
            intent.putExtra("TICKET_ID", ticketId)
            context.startActivity(intent)
        }

    }
}
