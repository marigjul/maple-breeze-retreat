package booking.springboot.restserver;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import booking.core.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling booking-related requests.
 * <p>
 * Provides endpoints for creating bookings, retrieving all bookings,
 * and getting bookings by email.
 * </p>
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingRestController {

    private static final Logger logger = Logger.getLogger(BookingRestController.class.getName());
    private final BookingRestService bookingRestService;

    /**
     * Constructor to inject the {@link BookingRestService}.
     * <p>
     * The {@link BookingRestService} is a Spring-managed singleton and can be safely
     * stored within this controller.
     * </p>
     * 
     * @param bookingRestService The service for managing bookings.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "BookingRestService is a Spring-managed singleton and safe to store.")
    public BookingRestController(BookingRestService bookingRestService) {
        this.bookingRestService = bookingRestService;
    }

    /**
     * Endpoint to create a new booking.
     * <p>
     * URL: `http://localhost:8080/api/bookings/add`
     * </p>
     *
     * @param booking The booking to create.
     * @return ResponseEntity with the created booking or error message.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            bookingRestService.addBooking(booking);
            return ResponseEntity.ok(booking);  // Return the created booking
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating booking: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to create booking: " + e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve all bookings.
     * <p>
     * URL: `http://localhost:8080/api/bookings`
     * </p>
     *
     * @return ResponseEntity with the list of all bookings.
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingRestService.loadAllBookingsFromFile();
        return ResponseEntity.ok(bookings);  // Return the list of bookings
    }

    /**
     * Endpoint to retrieve bookings by the email of the last added booking.
     * <p>
     * URL: `http://localhost:8080/api/bookings/email`
     * </p>
     *
     * @return ResponseEntity containing the list of bookings with the specified
     *         email, or a 404 status if no bookings are found.
     */
    @GetMapping("/email")
    public ResponseEntity<?> getBookingsByEmail() {
        List<Booking> bookings = bookingRestService.getBookingsByEmail();
        if (!bookings.isEmpty()) {
            return ResponseEntity.ok(bookings);  // Return the list of bookings for the email
        } else {
            logger.log(Level.WARNING, "No bookings found for the last added email.");
            return ResponseEntity.status(404).body("No bookings found.");
        }
    }
}