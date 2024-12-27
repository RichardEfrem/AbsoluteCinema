package uas.c14220270.absolutecinema

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleDateAdapter
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleSeatAdapter
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleTimeAdapter
import uas.c14220270.absolutecinema.Models.ChooseScheduleSeatModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChooseScheduleActivity : AppCompatActivity() {
    private lateinit var _seatRecView: RecyclerView
    private lateinit var _dateRecView: RecyclerView
    private lateinit var _timeRecView: RecyclerView
    private lateinit var _tvTotalPrice: TextView
    private var price: Double = 0.0
    private var number: Int = 0

    private var selectedSeat: List<ChooseScheduleSeatModel> = emptyList()
    private var selectedDate: String? = null
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

        _seatRecView = findViewById(R.id.seatRecView)
        _dateRecView = findViewById(R.id.dateRecView)
        _timeRecView = findViewById(R.id.timeRecView)
        _tvTotalPrice = findViewById(R.id.tvTotalPrice)

        initSeatList()
    }

    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(this, 5)
        _seatRecView.layoutManager = gridLayoutManager

        val seatList = mutableListOf<ChooseScheduleSeatModel>()
        val numberSeats = 42

        /*
        TODO: DISESUAIKAN DENGAN DATABASE (SCAN STATUS SETIAP SEAT)
         */
        for (i in 0 until numberSeats) {
            val seatNumber = i + 1
            seatList.add(ChooseScheduleSeatModel(ChooseScheduleSeatModel.SeatStatus.AVAILABLE, seatNumber))
        }

        _seatRecView.adapter = ChooseScheduleSeatAdapter(seatList, this, object : ChooseScheduleSeatAdapter.SelectedSeat {
            override fun Return(selectedName: String, num: Int) {
                val df = DecimalFormat("#,###")
                val formattedPrice = df.format(num * 50000)
                price = (num * 50000).toDouble()

                number = num

                selectedSeat = seatList.filter { it.status == ChooseScheduleSeatModel.SeatStatus.SELECTED }
                Log.d("", selectedSeat.toString())

                _tvTotalPrice.text = "IDR $formattedPrice"
            }
        })

        _timeRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _timeRecView.adapter = ChooseScheduleTimeAdapter(generateTimeSlots()) { selectedTimeSlot ->
            selectedTime = selectedTimeSlot
            Log.d("SelectedTime", "Selected Time: $selectedTime")
        }

        _dateRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _dateRecView.adapter = ChooseScheduleDateAdapter(generateDates()) { selectedDateSlot ->
            selectedDate = selectedDateSlot
            Log.d("SelectedDate", "Selected Date: $selectedDate")
        }
    }


    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        for (i in 8..22 step 2) {
            val time = LocalTime.of(i, 0).format(formatter)
            timeSlots.add(time)
        }
        return timeSlots
    }

    private fun generateDates(): List<String> {
        val dates = mutableListOf<String>()
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM")
        for (i in 0..6) {
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }
        return dates
    }
}

