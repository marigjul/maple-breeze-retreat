package booking.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * A test class for {@link PrimaryController}.
 */
public class PrimaryControllerTest extends ApplicationTest {

    private PrimaryController controller;
    private Stage testStage;
    private App appInstance;

    /**
     * Starts the JavaFX application for testing.
     * This method is called before each test is executed.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an exception occurs during loading
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.testStage = stage;

        appInstance = new App();
        appInstance.start(stage);

        // Load primary.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        // Create scene and add stylesheet
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Set the scene directly to testStage without using getStage()
        testStage.setScene(scene);

        // Show the stage
        testStage.show();
    }

    /**
     * Resets the invalidInputLabel before each test.
     * This ensures that the error message label is cleared before each test.
     *
     * @throws Exception if an exception occurs during reset
     */
    @BeforeEach
    public void resetInvalidInputLabel() throws Exception {
        // Reset invalidInputLabel before each test
        Platform.runLater(() -> controller.invalidInputLabel.setText(""));
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Tests that an invalid name shows an error message.
     *
     * @throws Exception if an exception occurs during the test
     */
    @Test
    public void testInvalidNameShowsErrorMessage() throws Exception {
        waitFor(5, TimeUnit.SECONDS, () -> !controller.treatmentPicker.getItems().isEmpty());

        // Enter invalid name and valid email
        clickOn("#nameLabel").write("");
        clickOn("#emailLabel").write("per.person@icloud.com");

        // Select a treatment
        clickOn("#treatmentPicker");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // Set a valid date
        Platform.runLater(() -> controller.datePicker.setValue(LocalDate.now().plusDays(1)));
        WaitForAsyncUtils.waitForFxEvents();

        // Click the order button
        clickOn("#orderButton");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the error message is displayed
        assertEquals("Invalid name", controller.invalidInputLabel.getText());

        // Verify that we are still on the primary view
        Platform.runLater(() -> {
            Node orderButton = testStage.getScene().lookup("#orderButton");
            assertNotNull(orderButton, "Should be on primary view.");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Tests that an invalid email shows an error message.
     *
     * @throws Exception if an exception occurs during the test
     */
    @Test
    public void testInvalidEmailShowsErrorMessage() throws Exception {
        waitFor(5, TimeUnit.SECONDS, () -> !controller.treatmentPicker.getItems().isEmpty());

        // Enter valid name and invalid email
        clickOn("#nameLabel").write("Trine Tristor");
        clickOn("#emailLabel").write("invalid-email");

        // Select a treatment
        clickOn("#treatmentPicker");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // Set a valid date
        Platform.runLater(() -> controller.datePicker.setValue(LocalDate.now().plusDays(1)));
        WaitForAsyncUtils.waitForFxEvents();

        // Click the order button
        clickOn("#orderButton");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the error message is displayed
        assertEquals("Invalid email", controller.invalidInputLabel.getText());

        // Verify that we are still on the primary view
        Platform.runLater(() -> {
            Node orderButton = testStage.getScene().lookup("#orderButton");
            assertNotNull(orderButton, "Should be on primary view.");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Tests that no treatment selected shows an error message.
     *
     * @throws Exception if an exception occurs during the test
     */
    @Test
    public void testNoTreatmentSelectedShowsErrorMessage() throws Exception {
        // Enter valid name and email
        clickOn("#nameLabel").write("John Doe");
        clickOn("#emailLabel").write("john.doe@example.com");

        // Set a valid date
        Platform.runLater(() -> controller.datePicker.setValue(LocalDate.now().plusDays(1)));
        WaitForAsyncUtils.waitForFxEvents();

        // Click the order button without selecting a treatment
        clickOn("#orderButton");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the error message is displayed
        assertEquals("Select a treatment", controller.invalidInputLabel.getText());

        // Verify that we are still on the primary view
        Platform.runLater(() -> {
            Node orderButton = testStage.getScene().lookup("#orderButton");
            assertNotNull(orderButton, "Should be on primary view.");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Tests that an invalid or missing date shows an error message.
     *
     * @throws Exception if an exception occurs during the test
     */
    @Test
    public void testInvalidDateShowsErrorMessage() throws Exception {
        waitFor(5, TimeUnit.SECONDS, () -> !controller.treatmentPicker.getItems().isEmpty());

        // Enter valid name and email
        clickOn("#nameLabel").write("Mario Lugi");
        clickOn("#emailLabel").write("mariolugi@lili.com");

        // Select a treatment
        clickOn("#treatmentPicker");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        // Do not set a date

        // Click the order button
        clickOn("#orderButton");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the error message is displayed
        assertEquals("Invalid or missing date", controller.invalidInputLabel.getText());

        // Verify that we are still on the primary view
        Platform.runLater(() -> {
            Node orderButton = testStage.getScene().lookup("#orderButton");
            assertNotNull(orderButton, "Should be on primary view.");
        });

        WaitForAsyncUtils.waitForFxEvents();
    }
}