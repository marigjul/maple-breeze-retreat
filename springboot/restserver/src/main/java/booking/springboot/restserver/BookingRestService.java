package booking.springboot.restserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import booking.core.Booking;
import booking.persistence.BookingService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Service;

/**
 * Service class for managing bookings.
 * <p>
 * Provides methods to load bookings from a file, add new bookings, and retrieve
 * bookings filtered by email.
 * </p>
 */
@Service
public class BookingRestService {

    private final BookingService bookingService;

    /**
     * Parameterized constructor for testing purposes.
     * <p>
     * This constructor allows injecting a custom {@link BookingService} instance, typically used
     * in testing scenarios to control the service behavior.
     * </p>
     * 
     * @param bookingService The {@link BookingService} instance to use.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Controlled usage of BookingService in test context, minimal risk of unexpected mutation.")
    public BookingRestService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Default constructor for production use.
     * <p>
     * Initializes the {@link BookingService} internally for production scenarios.
     * </p>
     */
    public BookingRestService() {
        this(new BookingService());
    }

    /**
     * Loads all bookings from the file and returns an unmodifiable list.
     * <p>
     * This method interacts with the {@link BookingService} to fetch all bookings
     * and returns them in an unmodifiable list to prevent external modifications.
     * </p>
     * 
     * @return Unmodifiable list of all bookings.
     */
    public List<Booking> loadAllBookingsFromFile() {
        return Collections.unmodifiableList(bookingService.loadBookingsFromFile());
    }

    /**
     * Adds a new booking to the system.
     * <p>
     * This method checks if the provided booking is null and throws an exception if so.
     * It delegates the actual adding of the booking to the {@link BookingService}.
     * </p>
     * 
     * @param booking The booking to be added.
     * @throws NullPointerException if the booking is null.
     */
    public void addBooking(Booking booking) {
        if (booking == null) {
            throw new NullPointerException("Booking is null");
        }
        bookingService.addBooking(booking);
    }

    /**
     * Retrieves all bookings filtered by email.
     * <p>
     * This method retrieves the list of bookings from the {@link BookingService}
     * that match a specific email filter. The returned list is wrapped in an
     * {@link ArrayList} to ensure it is mutable.
     * </p>
     * 
     * @return List of bookings matching the email filter.
     */
    public List<Booking> getBookingsByEmail() {
        return new ArrayList<>(bookingService.getBookingsByEmail());
    }
}
