package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MovieDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _ivImage = findViewById<ImageView>(R.id.ivImage)
        val _tvTitle = findViewById<TextView>(R.id.tvMovieTitle)
        val _tvDuration = findViewById<TextView>(R.id.tvMovieDuration)
        val _tvGenre = findViewById<TextView>(R.id.tvMovieGenre)

        val _tvStoryline = findViewById<TextView>(R.id.tvStoryline)
        val _tvDirector = findViewById<TextView>(R.id.tvDirector)
        val _tvProducer = findViewById<TextView>(R.id.tvProducer)
        val _tvActor = findViewById<TextView>(R.id.tvActor)

        val _chooseSeatsBtn = findViewById<Button>(R.id.bottom_button)
        val _btnBack = findViewById<FloatingActionButton>(R.id.btnBack)
        val _bottomButton = findViewById<Button>(R.id.bottom_button)

        _btnBack.setOnClickListener{
            val intent = Intent(this@MovieDetail, HomeActivity::class.java)
            startActivity(intent)
        }

        _bottomButton.setOnClickListener {
            val intent = Intent(this@MovieDetail, ChooseScheduleActivity::class.java)
            intent.putExtra("MOVIE_TITLE", _tvTitle.text)
            startActivity(intent)
        }

        // Retrieve data from Intent
        val title = intent.getStringExtra("MOVIE_TITLE") ?: "Unknown Title"
        val duration = intent.getStringExtra("MOVIE_DURATION") ?: "Unknown Duration"
        val genre = intent.getStringExtra("MOVIE_GENRE") ?: "Unknown Genre"
        val imageUrl = intent.getStringExtra("MOVIE_IMAGE")
        val producer = intent.getStringExtra("MOVIE_PRODUCER") ?: "Unknown Producer"
        val director = intent.getStringExtra("MOVIE_DIRECTOR") ?: "Unknown Director"
        val actor = intent.getStringExtra("MOVIE_ACTOR") ?: "Unknown Actor"
        val synopsis = intent.getStringExtra("MOVIE_SYNOPSIS") ?: "Synopsis not available"

        if (imageUrl != null) {
            val resourceId = resources.getIdentifier(imageUrl, "drawable", packageName)
            if (resourceId != 0) {
                _ivImage.setImageResource(resourceId)
            } else {
                _ivImage.setImageResource(R.drawable.venom3theme) // Fallback in case the resource is not found
            }
        } else {
            _ivImage.setImageResource(R.drawable.venom3theme) // Fallback for null imageUrl
        }
        _tvTitle.setText(title)
        _tvDuration.setText(duration)
        _tvGenre.setText(genre)
        _tvStoryline.setText(synopsis)
        _tvDirector.setText(director)
        _tvProducer.setText(producer)
        _tvActor.setText(actor)


    }
}