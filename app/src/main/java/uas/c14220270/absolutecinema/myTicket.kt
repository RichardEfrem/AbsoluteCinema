package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class myTicket : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myTicketAdapter: myTicketAdapter
    private val ticketList: MutableList<MutableMap<String, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        myTicketAdapter = myTicketAdapter(ticketList)
        recyclerView.adapter = myTicketAdapter

        fetchAllTickets()

        val _homeBtn = findViewById<ImageButton>(R.id.homeButton)
        val _profileBtn = findViewById<ImageButton>(R.id.profileButton)
        val _ticketBtn = findViewById<ImageButton>(R.id.ticketButton)

        _homeBtn.setOnClickListener {
            val intent = Intent(this@myTicket, HomeActivity::class.java)
            startActivity(intent)
        }

        _profileBtn.setOnClickListener {
            val intent = Intent(this@myTicket, ProfilePage::class.java)
            startActivity(intent)
        }

        _ticketBtn.setOnClickListener{
            val intent = Intent(this@myTicket, myTicket::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAllTickets() {
        // Initialize Firebase Auth and Firestore if not already done
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get the current user's email
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            Log.d("FetchTickets", "Fetching tickets for user: $currentUserEmail")
            db.collection("tickets")
                .whereEqualTo("user_id", currentUserEmail)
                .get()
                .addOnSuccessListener { documents ->
                    Log.d("FetchTickets", "Tickets fetched: ${documents.size()} documents")
                    ticketList.clear() // Clear the list before adding new data
                    for (document in documents) {
                        val ticketMap = mutableMapOf<String, String>()
                        ticketMap["ticketId"] = document.id
                        ticketMap["movie_title"] = document.getString("movie_title") ?: "Unknown"
                        ticketMap["date"] = document.getString("date") ?: "Unknown"
                        ticketMap["time"] = document.getString("time") ?: "Unknown"
                        ticketMap["seats"] = document.getString("seats") ?: "Unknown"

                        db.collection("movies")
                            .whereEqualTo("title", ticketMap["movie_title"])
                            .get()
                            .addOnSuccessListener { movies ->
                                Log.d("FetchTickets", "Movies fetched: ${movies.size()} movies")
                                if (!movies.isEmpty) {
                                    val movie = movies.documents.first().toObject(Movies::class.java)
                                    if (movie != null) {
                                        ticketMap["posterUrl"] = movie.posterUrl
                                        ticketList.add(ticketMap)
                                        Log.d("Success", "Ticket Added to List: ${ticketMap["ticketId"]}")
                                    }
                                } else {
                                    Log.d("FetchTickets", "No movie found for title: ${ticketMap["movie_title"]}")
                                }

                                // Notify adapter after adding each ticket (to update UI immediately)
                                myTicketAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener { e ->
                                Log.e("FetchTickets", "Error fetching movie details", e)
                            }
                    }
                    // Final notify after all tickets are processed (to ensure adapter updates only once)
                    myTicketAdapter.notifyDataSetChanged()
                    Log.d("FetchTickets", "Ticket list updated with ${ticketList.size} tickets.")
                }
                .addOnFailureListener { e ->
                    Log.e("FetchTickets", "Error fetching tickets", e)
                }
        } else {
            Log.e("FetchTickets", "User not logged in!")
        }
    }



}