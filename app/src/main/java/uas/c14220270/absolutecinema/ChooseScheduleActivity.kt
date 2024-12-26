package uas.c14220270.absolutecinema

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleDateAdapter
import uas.c14220270.absolutecinema.Adapters.ChooseScheduleSeatAdapter
import uas.c14220270.absolutecinema.Models.ChooseScheduleSeatModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChooseScheduleActivity : AppCompatActivity() {
    private lateinit var _seatRecView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _seatRecView = findViewById<RecyclerView>(R.id.seatRecView)
        initSeatList()

    }

    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(this, 7)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 1
                    else -> 2
                }
            }
        }

        _seatRecView.layoutManager = gridLayoutManager

        val seatList = mutableListOf<ChooseScheduleSeatModel>()
        val numberSeats = 81

        for (i in 0 until numberSeats) {
            val seatNumber = i + 1
            seatList.add(ChooseScheduleSeatModel(ChooseScheduleSeatModel.SeatStatus.AVAILABLE, seatNumber))
        }

        val ChooseScheduleSeatAdapter = ChooseScheduleSeatAdapter(seatList, this, object : ChooseScheduleSeatAdapter.SelectedSeat {
            override fun Return(selectedName: String, num: Int) {

                val df = DecimalFormat("#,###")

            }

        })

        _seatRecView.adapter = ChooseScheduleSeatAdapter
        _seatRecView.isNestedScrollingEnabled = false
    }

    private fun generateTimeSlots() : List<String> {
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        for (i in 0..23 step 2) {
            val time = formatter.format(LocalTime.of(i, 0))
            timeSlots.add(time)
        }

        return timeSlots
    }

    private fun generateDates() : List<String> {
        val dates = mutableListOf<String>()
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM")
        for (i in 0..6) {
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }

        return dates
    }
}