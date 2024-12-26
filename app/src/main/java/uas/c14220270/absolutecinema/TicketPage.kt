package uas.c14220270.absolutecinema

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class TicketPage : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ticketId = intent.getStringExtra("ticketId") ?: return

        val _tvTitle = findViewById<TextView>(R.id.tvTitle)
        val _tvDuration = findViewById<TextView>(R.id.tvDuration)
        val _tvGenre = findViewById<TextView>(R.id.tvGenre)
        val _tvTime = findViewById<TextView>(R.id.tvTime)
        val _tvDate = findViewById<TextView>(R.id.tvDate)
        val _tvSeat = findViewById<TextView>(R.id.tvSeat)
        val _tvPrice = findViewById<TextView>(R.id.tvPrice)
        val _tvLocation = findViewById<TextView>(R.id.tvLocation)
        val _ivMovie = findViewById<ImageView>(R.id.ivMovie)

        loadData(ticketId, _tvTitle, _tvDuration, _tvGenre, _tvTime, _tvDate, _tvSeat, _tvPrice, _tvLocation, _ivMovie)

        val _btnBack = findViewById<FloatingActionButton>(R.id.btnBack)
        _btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadData(
        ticketId: String,
        tvTitle: TextView,
        tvDuration: TextView,
        tvGenre: TextView,
        tvTime: TextView,
        tvDate: TextView,
        tvSeat: TextView,
        tvPrice: TextView,
        tvLocation: TextView,
        ivMovie: ImageView
    ) {
        db.collection("tickets").document(ticketId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvTitle.text = document.getString("title")
                    tvDuration.text = document.getString("duration")
                    tvGenre.text = document.getString("genre")
                    tvTime.text = document.getString("time")
                    tvDate.text = document.getString("date")
                    tvSeat.text = document.getString("seat")
                    tvPrice.text = document.getString("price")
                    tvLocation.text = document.getString("location")
                    val uri = Uri.parse(document.getString("imageUri"))
                    ivMovie.setImageURI(uri)
                } else {
                    Toast.makeText(this, "Ticket not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("TicketPage", "Error getting ticket data", exception)
                Toast.makeText(this, "Error: $exception", Toast.LENGTH_SHORT).show()
            }
    }
}