package booking.springboot.restserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import booking.core.Booking;

/**
 * Unit tests for the {@link BookingRestController} class.
 * <p>
 * This class ensures that the Booking REST API behaves as expected by testing its various endpoints.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BookingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRestService bookingRestService;

    private Booking newBooking;

    /**
     * Initializes the test environment by creating a new booking for testing.
     * <p>
     * This method is executed before each test case to ensure a clean setup.
     * </p>
     */
    @BeforeEach
    public void setup() {
        int bookingNumber = 1;
        String name = "Per Persen";
        String email = "per@gmail.com";
        String treatment = "Full body massage";
        LocalDate date = LocalDate.now().plusDays(5);
        newBooking = new Booking(bookingNumber, name, email, treatment, date);
    }

    /**
     * Tests the creation of a new booking.
     * <p>
     * Verifies that the new booking is successfully created and the response is as expected.
     * </p>
     */
    @Test
    public void testCreateBooking() throws Exception {
        // Mock the addBooking method
        doNothing().when(bookingRestService).addBooking(any(Booking.class));

        this.mockMvc.perform(post("/api/bookings/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("per@gmail.com"));
    }

    /**
     * Tests retrieving all bookings from the service.
     * <p>
     * Verifies that the service returns all bookings in the expected format.
     * </p>
     */
    @Test
    public void testGetAllBookings() throws Exception {
        when(bookingRestService.loadAllBookingsFromFile()).thenReturn(List.of(newBooking));

        this.mockMvc.perform(get("/api/bookings"))
                .andDo(print()) // For debugging purposes
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("per@gmail.com"));
    }

    /**
     * Tests retrieving bookings by email.
     * <p>
     * Verifies that bookings are returned when querying by email.
     * </p>
     */
    @Test
    public void testGetBookingsByEmail() throws Exception {
        // Mock the getBookingsByEmail method to return a list with the new booking
        when(bookingRestService.getBookingsByEmail()).thenReturn(List.of(newBooking));

        this.mockMvc.perform(get("/api/bookings/email"))
                .andDo(print()) // For debugging purposes
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("per@gmail.com"));
    }

    /**
     * Tests the scenario where no bookings are found by email.
     * <p>
     * Verifies that a 404 status is returned with an appropriate message when no bookings are found.
     * </p>
     */
    @Test
    public void testGetBookingsByEmailNotFound() throws Exception {
        // Simulate no bookings found by returning an empty list
        when(bookingRestService.getBookingsByEmail()).thenReturn(List.of());

        this.mockMvc.perform(get("/api/bookings/email"))
                .andDo(print()) // For debugging purposes
                .andExpect(status().isNotFound())
                .andExpect(content().string("No bookings found."));
    }

    /**
     * Tests the creation of a new booking with an exception scenario.
     * <p>
     * Simulates an exception in the service layer and verifies the correct response.
     * </p>
     */
    @Test
    public void testCreateBookingWithException() throws Exception {
        // Simulate an exception being thrown by the service
        doThrow(new RuntimeException("Database error")).when(bookingRestService).addBooking(any(Booking.class));

        this.mockMvc.perform(post("/api/bookings/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newBooking)))
                .andDo(print()) // For debugging purposes
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to create booking: Database error"));
    }

    /**
     * Helper method to convert objects to JSON strings.
     * <p>
     * Uses Jackson ObjectMapper to serialize the provided object into a JSON string.
     * </p>
     *
     * @param obj the object to convert to JSON
     * @return the JSON string representation of the object
     * @throws RuntimeException if the conversion fails
     */
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Register the JavaTimeModule to handle LocalDate serialization
            mapper.registerModule(new JavaTimeModule());
            // Disable writing dates as timestamps
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
