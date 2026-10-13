package app.adk.demo.toolcalling.tools

import com.google.adk.tools.Annotations
import org.slf4j.LoggerFactory

class BookingTool {
    companion object {
        val log = LoggerFactory.getLogger(BookingTool::class.java)

        val BOOKINGS = mapOf(
            "TL1001" to mapOf(
                "name" to "John Doe",
                "age" to "30",
                "pnr" to "TL1001",
                "route" to "PUN to GOA",
                "status" to "CONFIRMED",
                "seats" to "12A",
            ),
            "TL1002" to mapOf(
                "name" to "Jane Smith",
                "age" to "28",
                "pnr" to "TL1002",
                "route" to "BOM to DEL",
                "status" to "CANCELLED",
                "seats" to "14B",
            ),
        )

        @Annotations.Schema(name = "getBooking", description = "Get booking details by PNR")
        @JvmStatic
         fun getBooking(
            @Annotations.Schema(
                name = "pnr",
                description = "six character PNR code starts with T"
            ) pnr: String
        ): Map<String, String>? {
            val key = pnr.uppercase()
            val booking = BOOKINGS[key]
            if (booking == null) {
                log.error("Booking not found for PNR: $pnr")
                return mapOf(
                    "found" to "false",
                    "pnr" to pnr,
                    "message" to "Booking not found for PNR: $pnr"
                )
            } else {
                log.info("Booking details for PNR $pnr: $booking")
                return booking.apply {
                    "found" to "true"
                }
            }
        }

        @Annotations.Schema(description = "Calculate Cancellation fee for a booking. Returns the fee in euro and the refund the passenger would receive.")
        @JvmStatic
        fun cancellationFee(
            @Annotations.Schema(name = "pnr", description = "six character PNR code") pnr: String,
            @Annotations.Schema(name = "hoursBeforeDeparture", description = "Number of hours remaining before departure")
            hoursBeforeDeparture: Int
        ): Map<String, String>? {
            val key = pnr.uppercase()
            val booking = BOOKINGS[key]
            val notfound = mapOf(
                "found" to "false",
                "pnr" to pnr,
                "message" to "Booking not found for PNR: $pnr"
            )
            val fare = 4500
            val cancellationFee = 100

            if (booking != null) {
                val found = booking["found"] ?: "UNKNOWN"

                if (found == "true") {
                    return mapOf(
                        "pnr" to pnr.uppercase(),
                        "farePaid" to fare.toString(),
                        "cancellationFee" to cancellationFee.toString(),
                        "refund" to if (hoursBeforeDeparture >= 24) fare.toString() else (fare - (fare * 0.25)).toString(),
                        "reason" to if (hoursBeforeDeparture >= 24) "hours before departure >= 24 hours" else " hours before departure <= 24 hours"
                    )
                } else {
                    return notfound
                }
            } else {
                return notfound
            }
        }
    }

}