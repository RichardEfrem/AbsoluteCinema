package uas.c14220270.absolutecinema

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
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
    private lateinit var _dateRecView : RecyclerView
    private lateinit var _timeRecView : RecyclerView

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
        _dateRecView = findViewById<RecyclerView>(R.id.dateRecView)
        _timeRecView = findViewById<RecyclerView>(R.id.timeRecView)
        initSeatList()
        generateTimeSlots()
        generateDates()


    }

    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(this, 5)

        _seatRecView.layoutManager = gridLayoutManager

        val spacing = 16
        _seatRecView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })

        val seatList = mutableListOf<ChooseScheduleSeatModel>()
        val numberSeats = 42

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

        _timeRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _timeRecView.adapter = ChooseScheduleTimeAdapter(generateTimeSlots())

        _dateRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _dateRecView.adapter = ChooseScheduleDateAdapter(generateDates())
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