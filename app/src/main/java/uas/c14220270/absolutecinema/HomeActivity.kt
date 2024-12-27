package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import uas.c14220270.absolutecinema.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playingNowMovies = listOf(
            Movie(
                "Avengers: Endgame",
                "The culmination of over a decade of storytelling, Earth's mightiest heroes band together one last time to undo the catastrophic effects of Thanos' snap. With the fate of the universe at stake, the Avengers face their greatest challenge yet.",
                "3 hours 2 minutes",
                R.drawable.movie1
            ),
            Movie(
                "Doctor Strange",
                "When a brilliant but arrogant neurosurgeon loses the use of his hands in a car accident, he embarks on a journey of healing and discovers the hidden world of mystic arts and parallel dimensions.",
                "1 hour 55 minutes",
                R.drawable.movie2
            ),
            Movie(
                "Interstellar",
                "A team of explorers ventures beyond our galaxy through a wormhole in search of a new home for humanity, uncovering profound truths about time, space, and love along the way.",
                "1 hour 55 minutes",
                R.drawable.movie3
            ),
        )

        val comingSoonMovies = listOf(
            Movie(
                "Inception",
                "Dom Cobb, a skilled thief specializing in extracting secrets from within the subconscious, is tasked with planting an idea into someone's mind in exchange for his freedom. But the mission is more dangerous than it seems.",
                "2h 30m",
                R.drawable.coming1
            ),
            Movie(
                "The Dark Knight",
                "Gotham City faces its darkest hour when the Joker, a criminal mastermind, unleashes chaos. It's up to Batman to walk the fine line between hero and vigilante to stop him.",
                "1h 45m",
                R.drawable.coming2
            ),
            Movie(
                "Guardians of the Galaxy",
                "A band of misfits comes together to save the galaxy from a powerful enemy while discovering the true meaning of family and friendship.",
                "1h 45m",
                R.drawable.coming3
            ),
        )

        // Playing Now
        val playingNowRecyclerView = findViewById<RecyclerView>(R.id.playingNowRecyclerView)
        playingNowRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        playingNowRecyclerView.adapter = MovieAdapter(playingNowMovies)

        val comingSoonRecyclerView = findViewById<RecyclerView>(R.id.comingSoonRecyclerView)
        comingSoonRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        comingSoonRecyclerView.adapter = MovieAdapter(comingSoonMovies)

        val snapHelperPlayingNow = PagerSnapHelper()
        snapHelperPlayingNow.attachToRecyclerView(playingNowRecyclerView)

        val snapHelperComingSoon = PagerSnapHelper()
        snapHelperComingSoon.attachToRecyclerView(comingSoonRecyclerView)


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Stay on Home
                    true
                }
                R.id.tickets -> {
                    // Navigate to Tickets Activity
                    startActivity(Intent(this, TicketsActivity::class.java))
                    true
                }
                R.id.profile -> {
                    // Navigate to Profile Activity
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
