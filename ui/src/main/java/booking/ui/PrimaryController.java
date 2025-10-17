package booking.ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import booking.core.Booking;
import booking.core.InputValidation;
import booking.persistence.ApiClient;
import booking.springboot.restserver.BookingRestService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The {@code PrimaryController} class is the controller for the primary view in
 * the application.
 * It handles user inputs, validates them, and sends booking data via HTTP
 * requests.
 */
public class PrimaryController {

    private static final Logger logger = Logger.getLogger(PrimaryController.class.getName());

    private InputValidation validation;
    private final ApiClient apiClient = new ApiClient();
    private ObjectMapper objectMapper;
    private BookingRestService service;

    private App appInstance;
    private Stage stage;

    @FXML
    private TextField nameLabel;

    @FXML
    private TextField emailLabel;

    @FXML
    ComboBox<String> treatmentPicker;

    @FXML
    DatePicker datePicker;

    @FXML
    Label invalidInputLabel;

    /**
     * Initializes the controller by populating the treatment options and setting up
     * the input validation.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    protected void initialize() {
        // Populate treatment options
        treatmentPicker.getItems().addAll(
                "Full body massage",
                "Spa Facial",
                "Manicure",
                "Pedicure",
                "Hot stone massage");

        // Initialize input validation
        validation = new InputValidation();

        // Initialize and configure ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Injects the app instance and stage into the controller.
     */
    protected void setAppInstance(App app, Stage stage) {
        this.appInstance = app;
        this.stage = stage;
    }

    /**
     * Validates the user input and sends the booking data via an HTTP POST request
     * if the input is valid.
     * If any of the input fields are invalid, an error message is displayed in
     * {@code invalidInputLabel}.
     *
     * @throws IOException If there is an issue switching to the secondary view.
     */
    @FXML
    @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD", justification = "Method is called via FXML")
    private void switchToSecondary() throws IOException {
        String name = nameLabel.getText().trim();
        String email = emailLabel.getText().trim();
        String treatment = treatmentPicker.getValue();
        LocalDate date = datePicker.getValue();

        logger.info("Trying to create booking with" + name + " " + email + " " + treatment + " " + date);

        // Validate name
        if (!validation.nameValidation(name)) {
            invalidInputLabel.setText("Invalid name");
            return;
        }
        // Validate email
        if (!validation.emailValidation(email)) {
            invalidInputLabel.setText("Invalid email");
            return;
        }
        // Validate treatment selection
        if (treatment == null || !validation.treatmentValidation(treatment)) {
            invalidInputLabel.setText("Select a treatment");
            return;
        }
        // Validate date
        if (date == null || !validation.dateValidation(date)) {
            invalidInputLabel.setText("Invalid or missing date");
            return;
        }

        // Create a Booking object
        service = new BookingRestService();
        List<Booking> bookings = service.loadAllBookingsFromFile();
        Booking booking = new Booking(bookings.get(bookings.size() - 1).getBookingNumber() + 1, name, email, treatment, date);

        // Serialize Booking to JSON
        String bookingToJson;
        try {
            bookingToJson = objectMapper.writeValueAsString(booking);
        } catch (Exception e) {
            logger.severe("JSON serialization error: " + e.getMessage());
            invalidInputLabel.setText("Internal error: Unable to process booking");
            return;
        }

        // Send POST request synchronously
        try {
            String response = apiClient.post("/add", bookingToJson);
            if (response != null) {
                // Successfully created booking
                if (this.appInstance != null && this.stage != null) {
                    this.appInstance.setRoot("secondary", this.stage);
                }
            } else {
                invalidInputLabel.setText("Failed to create booking: No response from server");
            }
        } catch (IOException e) {
            logger.severe("HTTP request failed: " + e.getMessage());
            invalidInputLabel.setText("Failed to create booking: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            logger.severe("Operation was interrupted: " + e.getMessage());
            invalidInputLabel.setText("Operation was interrupted.");
        }
    }
}
