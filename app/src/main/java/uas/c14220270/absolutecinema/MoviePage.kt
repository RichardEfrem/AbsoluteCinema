package uas.c14220270.absolutecinema

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MoviePage : AppCompatActivity() {
    private val db = Firebase.firestore
    private val movieList = mutableListOf<Movie>()
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
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(movieList)
        recyclerView.adapter = movieAdapter

        fetchMovieData()

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = movieAdapter

    }

    private fun fetchMovieData() {
        db.collection("movies")
            .get()
            .addOnSuccessListener { result ->
                movieList.clear()
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val duration = document.getString("duration") ?: ""
                    val genre = document.getString("genre") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    val movie = Movie(title, duration, genre, imageUrl)
                    movieList.add(movie)
                }
                movieAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MoviePage", "Error getting movie data", exception)
            }
    }
}