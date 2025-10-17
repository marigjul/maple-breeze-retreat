package booking.ui;

import booking.core.Booking;
import booking.persistence.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TertiaryController}, which manages the tertiary view of
 * the application. This class uses Mockito for dependency injection to simulate
 * interactions with external components. It verifies the correct behavior of 
 * methods in the controller.
 */
@ExtendWith(MockitoExtension.class)
public class TertiaryControllerTest {

    @Mock
    private ApiClient apiClient; // Mocked instance of ApiClient

    @Mock
    private Stage stage; // Mocked instance of Stage

    @InjectMocks
    private TertiaryController controller; // Inject mocked dependencies into controller

    private ObjectMapper objectMapper; // ObjectMapper for deserialization

    private ListView<String> bookingList; // ListView to display booking details

    private Button closeButton; // Close button for the view
    private Button loadBookingButton; // Button to load booking details

    /**
     * Sets up the test environment before each test.
     * Initializes the controller and injects mocks.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        controller = Mockito.spy(new TertiaryController());
        controller.apiClient = apiClient;
        controller.objectMapper = objectMapper;

        bookingList = new ListView<>();
        controller.bookingList = bookingList;

        closeButton = new Button();
        loadBookingButton = new Button();
        controller.closeButton = closeButton;
        controller.loadBookingButton = loadBookingButton;
    }

    /**
     * Verifies that the {@code initialize} method calls {@code loadBooking} 
     * during controller initialization.
     */
    @Test
    public void testInitializeCallsLoadBooking() {
        doNothing().when(controller).loadBooking();

        controller.initialize();

        // Verify that loadBooking() was called exactly once during initialization
        verify(controller, times(1)).loadBooking();
    }

    /**
     * Tests that {@code loadBooking} correctly displays booking details in the
     * ListView.
     * Verifies the correct number of items and that each item contains the
     * booking name.
     */
    @Test
    public void testLoadBookingDisplaysCorrectBookings() throws Exception {
        Booking booking1 = new Booking(1, "Kari Hansen", "kari@hansen.com", "Hot stone massage", LocalDate.now());
        Booking booking2 = new Booking(10, "Kari Hansen", "kari@hansen.com", "Pedicure", LocalDate.now());
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(apiClient.get("/email")).thenReturn(objectMapper.writeValueAsString(bookings));

        controller.loadBooking();

        // Verify that the ListView contains the correct number of items
        assertEquals(2, bookingList.getItems().size());

        // Ensure that the ListView contains correct booking details
        assertTrue(bookingList.getItems().get(0).contains("Name: Kari"));
        assertTrue(bookingList.getItems().get(1).contains("Name: Kari"));
    }

    /**
     * Verifies that {@code loadBooking} handles {@link IOException} gracefully 
     * without throwing exceptions.
     */
    @Test
    public void testLoadBookingHandlesApiClientException() throws Exception {
        when(apiClient.get("/email")).thenThrow(new IOException("API error"));

        // Verify that IOException is handled and does not cause an exception
        assertDoesNotThrow(() -> controller.loadBooking(), "Expected loadBooking to handle IOException gracefully.");
    }

    /**
     * Tests that {@code closeApplication} closes the stage if it has been 
     * initialized.
     * Verifies that {@code stage.close()} is called once when the stage is set.
     */
    @Test
    public void testCloseApplicationClosesStage() throws IOException {
        controller.setAppInstance(null, stage);
        controller.closeApplication();

        // Verify that close() is called once on the stage
        verify(stage, times(1)).close();
    }

    /**
     * Tests that {@code closeApplication} does not attempt to close a null 
     * stage.
     * Verifies that if the stage is {@code null}, the method does not call 
     * {@code close()}.
     */
    @Test
    public void testCloseApplicationDoesNotCloseWhenStageIsNull() throws IOException {
        controller.setAppInstance(null, null);

        controller.closeApplication();

        // Verify that close() is not called when the stage is null
        verify(stage, never()).close();
    }

    /**
     * Tests that {@code loadBooking} filters the booking list by email and 
     * displays only bookings with the specified email address.
     */
    @Test
    public void testLoadBookingDisplaysOnlyMatchingEmailBookings() throws Exception {
        String targetEmail = "kari@hansen.com";
        Booking booking1 = new Booking(1, "Kari Hansen", targetEmail, "Hot stone massage", LocalDate.now());
        Booking booking2 = new Booking(2, "Kari Hansen", "another@example.com", "Pedicure", LocalDate.now());
        Booking booking3 = new Booking(3, "Kari Hansen", targetEmail, "Facial", LocalDate.now());
        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        when(apiClient.get("/email")).thenReturn(objectMapper.writeValueAsString(bookings));

        controller.loadBooking();

        // Verify that only bookings with the target email are displayed
        assertEquals(2, bookingList.getItems().size());
        bookingList.getItems().forEach(item -> assertTrue(item.contains(targetEmail)));
        bookingList.getItems().forEach(item -> assertFalse(item.contains("another@example.com")));
    }
}
