package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity(), HomeAdapter.OnMovieClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var homeAdapter: HomeAdapter
    private val movieList = mutableListOf<Movies>()

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        db = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.playingNowRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        homeAdapter = HomeAdapter(movieList, this)
        recyclerView.adapter = homeAdapter

        fetchAllMovies()
    }

    private fun fetchAllMovies(){
        db.collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                movieList.clear()
                for (document in documents) {
                    val title = document.getString("title") ?: "Unknown Title"
                    val genre = document.getString("genre") ?: "Unknown Genre"
                    val duration = document.getString("duration")?: "Unknown Duration"
                    val producer = document.getString("producer")?: "Unknown Producer"
                    val director = document.getString("director")?: "Unknown Director"
                    val synopsis = document.getString("synopsis")?: "Unknown Synopsis"
                    val actor = document.getString("actor")?: "Unknown Actor"
                    val imageUrl = document.getString("imageUrl")?: "Unknown ImageUrl"
                    val posterUrl = document.getString("posterUrl")?: "Unknown PosterUrl"

                    val movie = Movies(title, synopsis, director, producer, actor, posterUrl, imageUrl, duration, genre)
                    movieList.add(movie)
                }
                homeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching movies", exception)
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