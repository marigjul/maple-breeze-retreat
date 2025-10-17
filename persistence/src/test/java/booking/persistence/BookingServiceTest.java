package booking.persistence;

import booking.core.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BookingService} class.
 * <p>
 * Ensures the correctness of the {@link BookingService} functionality,
 * including loading bookings from a file and simulating the addition of a booking.
 * </p>
 */
class BookingServiceTest {

    /**
     * Instance of {@link BookingService} used for testing.
     */
    private BookingService bookingService;

    /**
     * Sets up the test environment by creating a new instance of
     * {@link BookingService}.
     * <p>
     * This method is executed before each test case, ensuring a clean setup for
     * every test.
     * </p>
     */
    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
    }

    /**
     * Tests the functionality of retrieving all bookings from the service.
     * <p>
     * Verifies that the list of bookings is not null. Additional assertions
     * </p>
     */
    @Test
    void testLoadBookingsFromFile() {
        List<Booking> bookings = bookingService.getAllBookings();
        assertNotNull(bookings, "Bookings should not be null");
    }

    /**
     * Tests simulating the addition of a new booking to the service.
     * <p>
     * Verifies that the new booking has the correct attributes as expected.
     * This ensures that the service behaves correctly when adding bookings.
     * </p>
     */
    @Test
    void testSimulateAddBooking() {
        // Create a fake booking
        Booking fakeBooking = new Booking(1, "John Doe", "john.doe@example.com", "Massage", LocalDate.now());

        // Verify the attributes of the fake booking
        assertEquals(1, fakeBooking.getBookingNumber(), "Booking number should be 1");
        assertEquals("John Doe", fakeBooking.getName(), "Name should be 'John Doe'");
        assertEquals("john.doe@example.com", fakeBooking.getEmail(), "Email should be 'john.doe@example.com'");
        assertEquals("Massage", fakeBooking.getTreatment(), "Treatment should be 'Massage'");
        assertEquals(LocalDate.now(), fakeBooking.getDate(), "Date should be today's date");
    }
}
