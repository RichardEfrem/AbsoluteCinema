package uas.c14220270.absolutecinema

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder> () {
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val _ivMovie = itemView.findViewById<ImageView>(R.id.ivMovie)
        val _tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val _tvDuration = itemView.findViewById<TextView>(R.id.tvDuration)
        val _tvGenre = itemView.findViewById<TextView>(R.id.tvGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_recycler, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = movieList[position]
        val uri = Uri.parse(movie.image)
        holder._ivMovie.setImageURI(uri)
        holder._tvTitle.text = movie.title
        holder._tvDuration.text = movie.duration
        holder._tvGenre.text = movie.genre
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}