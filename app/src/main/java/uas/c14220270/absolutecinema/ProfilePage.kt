package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ProfilePage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPreferencesManager.init(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser: FirebaseUser? = auth.currentUser
        val email = currentUser?.email
        val Username = SharedPreferencesManager.getString("username")
        val role = SharedPreferencesManager.getString("role")

        val _tvProfileName = findViewById<TextView>(R.id.profile_name)
        val _tvEmail = findViewById<TextView>(R.id.email_address)

        _tvProfileName.text = Username.toString()
        _tvEmail.text = email.toString()

        val _btnMyTicket = findViewById<LinearLayout>(R.id.my_ticket)

        val _btnQrScanner = findViewById<LinearLayout>(R.id.qr_scanner)
        if (role != "admin") {
            _btnQrScanner.visibility = Button.GONE
        } else {
            _btnQrScanner.setOnClickListener {
                IntentIntegrator(this).initiateScan()
            }
        }

        val _btnLogout = findViewById<Button>(R.id.logout_button)
        _btnLogout.setOnClickListener {
            SharedPreferencesManager.clearAll()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val _homeBtn = findViewById<ImageButton>(R.id.homeButton)
        val _profileBtn = findViewById<ImageButton>(R.id.profileButton)
        val _ticketBtn = findViewById<ImageButton>(R.id.ticketButton)

        _homeBtn.setOnClickListener {
            val intent = Intent(this@ProfilePage, HomeActivity::class.java)
            startActivity(intent)
        }

        _profileBtn.setOnClickListener {
            val intent = Intent(this@ProfilePage, ProfilePage::class.java)
            startActivity(intent)
        }

        _ticketBtn.setOnClickListener {
            val intent = Intent(this@ProfilePage, myTicket::class.java)
            startActivity(intent)
        }

        val _generateButton = findViewById<Button>(R.id.generateShows)
        _generateButton.setOnClickListener {
            updateShowsDateAndResetSeats()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val ticketId = result.contents
                updateTicketStatus(ticketId)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateTicketStatus(ticketId: String) {
        firestore.collection("tickets").document(ticketId)
            .update("status", "printed")
            .addOnSuccessListener {
                Toast.makeText(this, "Ticket status updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("ProfilePage", "Error updating ticket status", e)
            }
    }

    private fun updateShowsDateAndResetSeats() {
        val db = FirebaseFirestore.getInstance()
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        db.collection("shows")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    val emptySeats = generateSeats(45)
                    db.collection("shows").document(documentId)
                        .update(
                            mapOf(
                                "date" to currentDate,
                                "seats" to emptySeats
                            )
                        )
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Error updating show: $documentId",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }

                db.collection("tickets")
                    .get()
                    .addOnSuccessListener { ticketDocuments ->
                        for (ticket in ticketDocuments) {
                            val ticketId = ticket.id
                            db.collection("tickets").document(ticketId).delete()
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Ticket $ticketId deleted successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Firestore", "Error deleting ticket: $ticketId", exception)
                                }
                        }
                        Toast.makeText(
                            this,
                            "Successfully updated date, reset seats for shows, and deleted all tickets.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error fetching tickets: ", exception)
                        Toast.makeText(
                            this,
                            "Error deleting tickets from the collection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching shows: ", exception)
            }
    }

    private fun generateSeats(count: Int): Map<String, String> {
        val seats = mutableMapOf<String, String>()
        for (i in 1..count) {
            seats[i.toString()] = "Available"
        }
        return seats
    }
}