package booking.springboot.restserver;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import booking.core.Booking;
import booking.persistence.BookingService;

/**
 * Unit tests for the {@link BookingRestService} class.
 * <p>
 * These tests ensure the correctness of the BookingRestService methods, which
 * include loading bookings, adding bookings, and filtering bookings by email.
 * </p>
 */
public class BookingRestServiceTest {

    @Mock
    private BookingService bookingService; // Mocked dependency

    @InjectMocks
    private BookingRestService bookingRestService; // Class under test

    private Booking booking1;
    private Booking booking2;

    private List<Booking> mockBookings;

    /**
     * Sets up the test environment by initializing mock data for bookings.
     * <p>
     * This method is executed before each test case to ensure a clean setup.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize sample Booking objects
        booking1 = new Booking(1, "John Doe", "john@example.com", "Manicure", LocalDate.of(2024, 11, 15));
        booking2 = new Booking(2, "Jane Smith", "jane@example.com", "Hot stone massage", LocalDate.of(2024, 11, 20));

        // Initialize mock bookings list
        mockBookings = Arrays.asList(booking1, booking2); // Assign to class-level field
    }

    /**
     * Tests that the list returned by loadAllBookingsFromFile is unmodifiable.
     * <p>
     * Verifies that the list of bookings cannot be modified after being returned
     * by the service.
     * </p>
     */
    @Test
    public void testLoadAllBookingsFromFile_UnmodifiableList() {
        // Arrange
        when(bookingService.loadBookingsFromFile()).thenReturn(mockBookings);

        // Act
        List<Booking> result = bookingRestService.loadAllBookingsFromFile();

        // Assert
        assertEquals(mockBookings, result, "Returned bookings should match the mock data");
        assertThrows(UnsupportedOperationException.class, () -> result.add(new Booking()),
                "List should be unmodifiable");
    }

    /**
     * Tests that an empty list returned by loadAllBookingsFromFile is unmodifiable.
     * <p>
     * Verifies that even an empty list cannot be modified after being returned
     * by the service.
     * </p>
     */
    @Test
    public void testLoadAllBookingsFromFile_EmptyList() {
        // Arrange
        when(bookingService.loadBookingsFromFile()).thenReturn(Collections.emptyList());

        // Act
        List<Booking> result = bookingRestService.loadAllBookingsFromFile();

        // Assert
        assertTrue(result.isEmpty(), "Returned list should be empty");
        assertThrows(UnsupportedOperationException.class, () -> result.add(new Booking()),
                "List should be unmodifiable even if empty");
    }

    /**
     * Tests that addBooking successfully adds a new booking.
     * <p>
     * Verifies that a new booking is added to the service and that the service's
     * state is correctly updated.
     * </p>
     */
    @Test
    public void testAddBooking_Success() {
        // Arrange
        Booking newBooking = new Booking(3, "Alice Wonderland", "alice@example.com", "Pedicure",
                LocalDate.of(2024, 12, 1));

        // Create a spy to monitor interactions with the bookingService
        BookingService realBookingService = new BookingService();
        BookingService bookingServiceSpy = Mockito.spy(realBookingService);

        // Prevent the spy from calling the real writeToJSONFile method
        doNothing().when(bookingServiceSpy).writeToJSONFile();

        // Inject the spy into the BookingRestService
        bookingRestService = new BookingRestService(bookingServiceSpy);

        // Act
        bookingRestService.addBooking(newBooking);

        // Assert
        // Verify that the addBooking method was called on the spy
        verify(bookingServiceSpy, times(1)).addBooking(newBooking);

        // Assert that the booking was added correctly
        List<Booking> bookings = bookingRestService.getBookingsByEmail();
        assertTrue(bookings.contains(newBooking), "The returned bookings should contain the booking that was added");
    }

    /**
     * Tests that addBooking throws a NullPointerException when the provided
     * booking is null.
     * <p>
     * Verifies that the correct exception is thrown when attempting to add a null
     * booking.
     * </p>
     */
    @Test
    public void testAddBooking_NullBooking() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> bookingRestService.addBooking(null),
                "Adding a null booking should throw NullPointerException");

        assertEquals("Booking is null", exception.getMessage(), "Exception message should match");
        verify(bookingService, never()).addBooking(any());
    }

    /**
     * Tests that getBookingsByEmail returns a filtered list based on email.
     * <p>
     * Verifies that the service correctly filters bookings by email and returns
     * the expected list.
     * </p>
     */
    @Test
    public void testGetBookingsByEmail_ReturnsFilteredList() {
        // Arrange
        List<Booking> mockFilteredBookings = Arrays.asList(
                new Booking(1, "John Doe", "john@example.com", "Massage", LocalDate.of(2024, 11, 15)));
        when(bookingService.getBookingsByEmail()).thenReturn(mockFilteredBookings);

        // Act
        List<Booking> result = bookingRestService.getBookingsByEmail();

        // Assert
        assertEquals(mockFilteredBookings, result, "Returned bookings should match the mock data");
        assertNotSame(mockFilteredBookings, result, "Returned list should be a new instance");
    }

    /**
     * Tests that getBookingsByEmail returns an empty list when no bookings match.
     * <p>
     * Verifies that the service returns an empty list when no bookings match the
     * provided email.
     * </p>
     */
    @Test
    public void testGetBookingsByEmail_NoMatches() {
        // Arrange
        when(bookingService.getBookingsByEmail()).thenReturn(Collections.emptyList());

        // Act
        List<Booking> result = bookingRestService.getBookingsByEmail();

        // Assert
        assertTrue(result.isEmpty(), "Returned list should be empty when no bookings match");
        assertNotNull(result, "Returned list should not be null");
        assertNotSame(Collections.emptyList(), result, "Returned list should be a new instance");
    }
}
