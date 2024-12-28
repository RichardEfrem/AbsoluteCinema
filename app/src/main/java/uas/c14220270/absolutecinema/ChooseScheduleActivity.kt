package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleSeatAdapter
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleTimeAdapter
import uas.c14220270.absolutecinema.Models.ChooseScheduleSeatModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class ChooseScheduleActivity : AppCompatActivity() {
    private lateinit var _seatRecView: RecyclerView
    private lateinit var _timeRecView: RecyclerView
    private lateinit var _tvTotalPrice: TextView
    private var price: Double = 0.0
    private var number: Int = 0
    private lateinit var filmName: String
    private lateinit var _buyTicketButton: TextView
    private lateinit var auth: FirebaseAuth

    private var selectedSeat: List<ChooseScheduleSeatModel> = emptyList()
    private var selectedTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _backBtn = findViewById<ImageButton>(R.id.backButton)
        _backBtn.setOnClickListener{
            finish()
        }


        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()

        _seatRecView = findViewById(R.id.seatRecView)
        _timeRecView = findViewById(R.id.timeRecView)
        _tvTotalPrice = findViewById(R.id.tvTotalPrice)
        _buyTicketButton = findViewById(R.id.buyTicketButton)

        filmName = intent.getStringExtra("MOVIE_TITLE") ?: "Default Movie Title"

        selectedTime = intent.getStringExtra("SELECTED_TIME")
        Log.d("SELECTED TIME", selectedTime.toString())

        initTimeList()

        _buyTicketButton.setOnClickListener {
            buyTicket(email)
        }
    }

    private fun buyTicket(email :String) {
        val userId = email // TODO :
        val seatsSelected = selectedSeat.filter { it.status == ChooseScheduleSeatModel.SeatStatus.SELECTED }
        val seatNumbers = seatsSelected.joinToString(",") { it.seatNumber.toString() }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd") // Define your desired format
        val currentDate = dateFormat.format(Date()).toString()

        val ticketData = hashMapOf(
            "movie_title" to filmName,
            "price" to price.toString(),
            "seats" to seatNumbers,
            "time" to selectedTime,
            "user_id" to userId,
            "status" to "booked",
            "date" to currentDate
        )

        val db = Firebase.firestore
        db.collection("tickets")
            .add(ticketData)
            .addOnSuccessListener { documentReference ->
                val ticketId = documentReference.id
                Log.d("Firestore", "Ticket added with ID: ${documentReference.id}")

                updateSeatsToBooked(seatsSelected)

                val intent = Intent(this, TicketPage::class.java)
                intent.putExtra("TICKET_ID", ticketId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding ticket: ", e)
            }
    }

    private fun updateSeatsToBooked(seatsSelected: List<ChooseScheduleSeatModel>) {
        val db = Firebase.firestore
        val formattedTime = selectedTime?.replace(".", "") ?: ""
        val showId = "${filmName.replace(" ", "")}@$formattedTime"

        db.collection("shows").document(showId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val seats = document.get("seats") as? Map<String, String> ?: emptyMap()

                    val updatedSeats = seats.toMutableMap()
                    seatsSelected.forEach { seat ->
                        updatedSeats[seat.seatNumber.toString()] = "Booked"
                    }

                    db.collection("shows").document(showId)
                        .update("seats", updatedSeats)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Seats updated to BOOKED")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating seats: ", e)
                        }
                } else {
                    Log.w("Firestore", "No such show found!")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching seats: ", exception)
            }
    }


    private fun initTimeList() {
        val db = Firebase.firestore
        _timeRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        db.collection("shows")
            .whereEqualTo("movieTitle", filmName)
            .get()
            .addOnSuccessListener { documents ->
                val timeSlots = documents.mapNotNull { doc ->
                    doc.getString("time")
                }

                if (selectedTime == null && timeSlots.isNotEmpty()) {
                    selectedTime = timeSlots.first()
                    Log.d("DEFAULT TIME", "Using first time slot: $selectedTime")
                }

                _timeRecView.adapter = ChooseScheduleTimeAdapter(timeSlots) { selectedTimeSlot ->
                    val intent = Intent(this, ChooseScheduleActivity::class.java)
                    intent.putExtra("MOVIE_TITLE", filmName)
                    intent.putExtra("SELECTED_TIME", selectedTimeSlot)
                    startActivity(intent)
                }

                selectedTime?.let {
                    fetchSeatsForShow(it)
                    Log.d("ENTER FETCH SEATS", "Fetching seats for $it")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching time slots: ", exception)
            }
    }

    private fun fetchSeatsForShow(time: String) {
        val db = Firebase.firestore
        val formattedTime = time.replace(".", "")
        val showId = "${filmName.replace(" ", "")}@$formattedTime"
        val seatList = mutableListOf<ChooseScheduleSeatModel>()
        Log.d("SHOW ID", showId)

        db.collection("shows").document(showId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val seats = document.get("seats") as? Map<String, String> ?: emptyMap()

                    if (seats.isEmpty()) {
                        Log.w("Firestore", "No seats found for this show")
                    } else {
                        for ((seatNumber, status) in seats) {
                            val seatStatus = when (status) {
                                "Available" -> ChooseScheduleSeatModel.SeatStatus.AVAILABLE
                                "Booked" -> ChooseScheduleSeatModel.SeatStatus.BOOKED
                                else -> ChooseScheduleSeatModel.SeatStatus.AVAILABLE
                            }
                            seatList.add(ChooseScheduleSeatModel(seatStatus, seatNumber.toInt()))
                        }

                        // Sort the seats by their number
                        val sortedSeatList = seatList.sortedBy { it.seatNumber }

                        Log.d("SORTED SEAT LIST", sortedSeatList.toString())

                        _seatRecView.layoutManager = GridLayoutManager(this, 5)
                        _seatRecView.adapter = ChooseScheduleSeatAdapter(sortedSeatList, this, object : ChooseScheduleSeatAdapter.SelectedSeat {
                            override fun Return(selectedName: String, num: Int) {
                                val df = DecimalFormat("#,###")
                                price = num * 50000.0
                                _tvTotalPrice.text = "IDR ${df.format(price)}"
                                number = num
                                selectedSeat = sortedSeatList.filter { it.status == ChooseScheduleSeatModel.SeatStatus.SELECTED }
                            }
                        })
                    }
                } else {
                    Log.w("Firestore", "No such show found!")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching seats: ", exception)
            }
    }

}

