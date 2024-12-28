package uas.c14220270.absolutecinema

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.google.zxing.qrcode.QRCodeWriter
import org.w3c.dom.Text


class TicketPage : AppCompatActivity() {
    private val db = Firebase.firestore
    private val ticketDataMap = mutableMapOf<String, String>() // Map to store ticket data
//    private var movie: Movies? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ticketId = intent.getStringExtra("TICKET_ID") ?: return
        var movieTitle = "None"

        val _tvTitle = findViewById<TextView>(R.id.tvTitle)
        val _tvDuration = findViewById<TextView>(R.id.tvDuration)
        val _tvGenre = findViewById<TextView>(R.id.tvGenre)
        val _tvDateTime = findViewById<TextView>(R.id.tvDateTime)
        val _tvSeat = findViewById<TextView>(R.id.tvSeat)
        val _tvPrice = findViewById<TextView>(R.id.tvPrice)
        val _tvLocation = findViewById<TextView>(R.id.tvLocation)
        val _ivMovie = findViewById<ImageView>(R.id.ivMovie)
        val _tvTime = findViewById<TextView>(R.id.tvTime)



        db.collection("tickets").document(ticketId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    movieTitle = document.getString("movie_title") ?: "Unknown Movie"
                    val seat = document.getString("seats").toString()
                    val date = document.getString("date").toString()
                    val time = document.getString("time").toString()
                    val price = document.getString("price").toString()
                    _tvTitle.text = movieTitle
                    _tvSeat.text = seat
                    _tvDateTime.text = date
                    _tvTime.text = time
                    _tvPrice.text = "Rp. $price"
                    db.collection("movies")
                        .whereEqualTo("title", movieTitle)
                        .get()
                        .addOnSuccessListener { movies ->
                            if (!movies.isEmpty) {
                                val movie = movies.documents.first().toObject(Movies::class.java)
                                if (movie != null){
                                    _tvGenre.setText(movie.genre.toString())
                                    _tvDuration.setText(movie.duration.toString())
                                    // Set ImageView based on posterUrl (which should be a drawable resource reference)
                                    val posterResourceId = resources.getIdentifier(
                                        movie.posterUrl, // Example: "image" if the value is "@drawable/image"
                                        "drawable",
                                        packageName
                                    )
                                    if (posterResourceId != 0) {
                                        _ivMovie.setImageResource(posterResourceId)
                                    } else {
                                        // If the drawable is not found, use a placeholder or show an error
                                        _ivMovie.setImageResource(R.drawable.sonic3) // Add a placeholder drawable in your resources
                                    }
                                }
                            }
                        }
                } else {
                    Toast.makeText(this, "Ticket not found!", Toast.LENGTH_SHORT).show()
                }
            }

        displayQRCode(ticketId)

        val _btnBack = findViewById<FloatingActionButton>(R.id.btnBack)
        _btnBack.setOnClickListener {
            finish()
        }
    }

    private fun generateQRCode(ticketId: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix = qrCodeWriter.encode(ticketId, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun displayQRCode(ticketId: String) {
        val qrCodeBitmap = generateQRCode(ticketId)
        val qrCodeImageView = findViewById<ImageView>(R.id.ivQr)
        qrCodeImageView.setImageBitmap(qrCodeBitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedTicketId = result.contents
                updateTicketStatus(scannedTicketId)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateTicketStatus(ticketId: String) {
        db.collection("tickets").document(ticketId)
            .update("status", "printed")
            .addOnSuccessListener {
                Toast.makeText(this, "Ticket status updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("TicketPage", "Error updating ticket status", e)
            }
    }

//    private fun fetchMovieData(movieTitle: String, callback: (Movies?) -> Unit) {
//        db.collection("movies")
//            .whereEqualTo("title", movieTitle)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
//                    val movieDocument = querySnapshot.documents.first()
//                    callback(movieDocument.toObject(Movies::class.java))
//                } else {
//                    Toast.makeText(this, "Movie not found!", Toast.LENGTH_SHORT).show()
//                    callback(null)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TicketPage", "Error getting movie data", exception)
//                Toast.makeText(this, "Error fetching movie data: $exception", Toast.LENGTH_SHORT).show()
//                callback(null)
//            }
//    }

}