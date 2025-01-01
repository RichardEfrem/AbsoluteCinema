package uas.c14220270.absolutecinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.HomeAdapter.OnMovieClickListener

class MovieAdapter(private val movieList: List<Movies>, private val listener: OnMovieClickListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder> () {

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movies)
    }

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
        val resourceId = holder.itemView.context.resources.getIdentifier(
            movie.posterUrl.replace("@drawable/", ""),
            "drawable",
            holder.itemView.context.packageName
        )

        if (resourceId != 0) {
            holder._ivMovie.setImageResource(resourceId)
        } else {
            holder._ivMovie.setImageResource(R.drawable.sonic3)
        }
        holder._tvTitle.text = movie.title
        holder._tvDuration.text = movie.duration
        holder._tvGenre.text = movie.genre

        holder._ivMovie.setOnClickListener{
            listener.onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}