package uas.c14220270.absolutecinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImage: ImageView = view.findViewById(R.id.movieImage)
        val movieTitle: TextView = view.findViewById(R.id.movieTitle)
        val movieDescription: TextView = view.findViewById(R.id.movieDescription)
        val movieDuration: TextView = view.findViewById(R.id.movieDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieImage.setImageResource(movie.imageResId)
        holder.movieTitle.text = movie.title
        holder.movieDescription.text = movie.description
        holder.movieDuration.text = movie.duration
    }

    override fun getItemCount(): Int = movies.size
}
