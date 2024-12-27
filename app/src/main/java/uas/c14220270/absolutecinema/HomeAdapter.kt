package uas.c14220270.absolutecinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(
    private val movies: List<Movies>,
    private val listener: OnMovieClickListener
) : RecyclerView.Adapter<HomeAdapter.MovieViewHolder>() {

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movies)
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImage: ImageView = view.findViewById(R.id.movieImage)
        val movieTitle: TextView = view.findViewById(R.id.movieTitle)
        val movieDuration: TextView = view.findViewById(R.id.movieDuration)
        val movieGenre: TextView = view.findViewById(R.id.movieGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        val resourceId = holder.itemView.context.resources.getIdentifier(
            movie.posterUrl.replace("@drawable/", ""),
            "drawable",
            holder.itemView.context.packageName
        )

        if (resourceId != 0) {
            holder.movieImage.setImageResource(resourceId)
        } else {
            holder.movieImage.setImageResource(R.drawable.sonic3)
        }

        holder.movieTitle.text = movie.title
        holder.movieDuration.text = movie.duration
        holder.movieGenre.text = movie.genre

        // Set OnClickListener on movieImage only
        holder.movieImage.setOnClickListener {
            listener.onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}
