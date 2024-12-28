package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MoviePage : AppCompatActivity(), MovieAdapter.OnMovieClickListener {
    private val db = Firebase.firestore
    private val movieList = mutableListOf<Movies>()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.rvMovie)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        movieAdapter = MovieAdapter(movieList, this)
        recyclerView.adapter = movieAdapter

        fetchMovieData()

        val _homeBtn = findViewById<ImageButton>(R.id.homeButton)
        val _profileBtn = findViewById<ImageButton>(R.id.profileButton)
        val _ticketBtn = findViewById<ImageButton>(R.id.ticketButton)
        val _movieBtn = findViewById<ImageButton>(R.id.movieButton)

        _homeBtn.setOnClickListener{
            val intent = Intent(this@MoviePage, HomeActivity::class.java)
            startActivity(intent)
        }

        _profileBtn.setOnClickListener{
            val intent = Intent(this@MoviePage, ProfilePage::class.java)
            startActivity(intent)
        }

        _ticketBtn.setOnClickListener{
            val intent = Intent(this@MoviePage, myTicket::class.java)
            startActivity(intent)
        }

        _movieBtn.setOnClickListener{
            val intent = Intent(this@MoviePage, MoviePage::class.java)
            startActivity(intent)
        }

    }

    private fun fetchMovieData() {
        db.collection("movies")
            .get()
            .addOnSuccessListener { result ->
                movieList.clear()
                for (document in result) {
                    val movie = document.toObject(Movies::class.java)
                    movieList.add(movie)
                }
                movieAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MoviePage", "Error getting movie data", exception)
            }
    }

    override fun onMovieClick(movie: Movies) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra("MOVIE_TITLE", movie.title)
        intent.putExtra("MOVIE_DURATION", movie.duration)
        intent.putExtra("MOVIE_GENRE", movie.genre)
        intent.putExtra("MOVIE_IMAGE", movie.imageUrl)
        intent.putExtra("MOVIE_PRODUCER", movie.producer)
        intent.putExtra("MOVIE_DIRECTOR", movie.director)
        intent.putExtra("MOVIE_ACTOR", movie.actor)
        intent.putExtra("MOVIE_SYNOPSIS", movie.synopsis)
        startActivity(intent)
    }
}