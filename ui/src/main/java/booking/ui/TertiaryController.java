package booking.ui;

import booking.core.Booking;
import booking.persistence.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

/**
 * The {@code TertiaryController} class manages the functionality of the
 * tertiary view in the application. This controller handles actions related
 * to displaying user booking data and closing the application.
 */
public class TertiaryController {

    @FXML
    protected ListView<String> bookingList; // List view to display bookings

    private Stage stage;

    @FXML
    protected Button closeButton;

    @FXML
    protected Button loadBookingButton; // Button to trigger booking load

    protected ApiClient apiClient = new ApiClient();
    protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * A helper class that extends {@link TypeReference} to represent a {@link List} of {@link Booking} objects.
     * <p>
     * This class is used to facilitate the deserialization of JSON into a {@link List} of {@link Booking} objects,
     * particularly in contexts where generic types need to be preserved during deserialization (e.g., with libraries like Jackson).
     * </p>
     * <p>
     * It is used to provide type information for a {@link List} of {@link Booking} objects to Jackson's {@link ObjectMapper}
     * when parsing JSON data.
     * </p>
     */
    private static class BookingListType extends TypeReference<List<Booking>> {
        // empty static class
    }

    /**
     * Initializes the controller by setting up initial configurations.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    protected void initialize() {
        loadBooking();
    }

    /**
     * Injects the app instance and stage into the controller.
     */
    protected void setAppInstance(App app, Stage stage) {
        this.stage = stage;
    }

    /**
     * Closes the application when called.
     * This method exits the JavaFX application by closing the stage associated with
     * the closeButton.
     *
     * @throws IOException If an I/O error occurs (although in this case, it is
     *                     unlikely to throw).
     */
    @FXML
    protected void closeApplication() throws IOException {
        if (this.stage != null) {
            this.stage.close(); // safely close the stage
        }
    }

    /**
     * Loads booking information from the REST server and displays it in the
     * ListView.
     */
    @FXML
    protected void loadBooking() {
        try {
            // Fetch booking data from the Spring Boot server using ApiClient
            String response = apiClient.get("/email"); // Assuming GET /api/bookings returns all bookings

            // Deserialize response into Booking objects
            List<Booking> bookings = objectMapper.readValue(response, new BookingListType());
            System.out.println(bookings);

            // Convert Booking objects to a readable string format for display
            ObservableList<String> bookingDetails = FXCollections.observableArrayList();

            String emailToUse = bookings.get(bookings.size() - 1).getEmail();
            for (Booking booking : bookings) {
                if (booking.getEmail().equals(emailToUse)) {
                    String detail = String.format("Name: %s%nEmail: %s%nTreatment: %s%nDate: %s",
                            booking.getName(),
                            booking.getEmail(),
                            booking.getTreatment(),
                            booking.getDate());
                    bookingDetails.add(detail);
                }
            }
            // Set the items in the ListView
            bookingList.setItems(bookingDetails);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Handle the error
            System.err.println("Error fetching booking data: " + e.getMessage());
        }
    }
}
