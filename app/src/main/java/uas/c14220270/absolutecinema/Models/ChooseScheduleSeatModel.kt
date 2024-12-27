package uas.c14220270.absolutecinema.Models

data class ChooseScheduleSeatModel(var status : SeatStatus, var seatNumber: Int) {
    enum class SeatStatus {
        AVAILABLE,
        BOOKED,
        SELECTED
    }
}
