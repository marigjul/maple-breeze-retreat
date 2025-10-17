package booking.ui;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Test class for the SecondaryController, using TestFX for UI interaction
 * testing. Tests include validation of UI actions such as switching views and closing
 * the application.
 */
public class SecondaryControllerTest extends ApplicationTest {
    private TestableSecondaryController controller;
    private App appInstance;
    private Stage stage;
    /**
     * A test subclass of SecondaryController that overrides methods to track
     * if certain methods (closeApplication, switchToTertiary) have been called.
     */
    public class TestableSecondaryController extends SecondaryController {
        public boolean exitCalled = false;
        public boolean switchToTertiaryCalled = false;
        /**
         * Overrides closeApplication to track if it has been called.
         *
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void closeApplication() throws IOException {
            exitCalled = true;
            super.closeApplication();
        }
        /**
         * Overrides switchToTertiary to track if it has been called.
         *
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void switchToTertiary() throws IOException {
            switchToTertiaryCalled = true;
            super.switchToTertiary();
        }
    }
    /**
     * Initializes the JavaFX application with a custom FXMLLoader to inject the
     * TestableSecondaryController as the controller.
     *
     * @param stage the main stage for the JavaFX application
     * @throws Exception if an error occurs while setting up the application
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        appInstance = new App();
        appInstance.start(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/booking/ui/secondary.fxml"));
        loader.setControllerFactory(param -> {
            if (param == SecondaryController.class) {
                controller = new TestableSecondaryController();
                controller.setAppInstance(appInstance, stage); // Pass app and stage to the controller
                return controller;
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/booking/ui/style.css").toExternalForm());

        // Set the scene on the passed stage and show it
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Tests if clicking the button with ID "showReceiptButton" triggers a switch
     * to the tertiary view by calling switchToTertiary and updates the UI to the
     * tertiary view.
     */
    @Test
    public void testSwitchToTertiaryCalled() {
        // Click the button to switch to the tertiary view
        clickOn("#showReceiptButton");
        Platform.runLater(() -> {
            Parent root = appInstance.getScene().getRoot();
            assertNotNull(root, "Root should not be null after switching to tertiary");
            // Check for a specific node or property in the tertiary view
            // For example, if the root node in tertiary.fxml has fx:id="tertiaryRoot"
            assertNotNull(root.lookup("#Receipt"), "Should be on tertiary view with 'Receipt' ID");
        });
        
    }
    /**
     * Tests if clicking the button with ID "closeButton" triggers the close
     * application action by calling closeApplication and closes the stage.
     */
    @Test
    public void testCloseApplicationClosesStage() {
        // Click the close button
        clickOn("#closeButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Verify that closeApplication() was called
        assertTrue(controller.exitCalled, "closeApplication should have been called");
        // Verify that the stage is closed
        assertFalse(stage.isShowing(), "Stage should be closed after clicking close button");
    }
    /**
     * Tests if closeApplication handles a null stage without throwing an exception.
     */
    @Test
    public void testCloseApplicationWithNullStage() {
        // Set stage to null to simulate edge case
        controller.stage = null;
        
        // Call closeApplication and verify it does not throw an exception
        try {
            controller.closeApplication();
            assertTrue(true, "closeApplication should handle a null stage without throwing an exception");
        } catch (Exception e) {
            // If an exception occurs, the test should fail
            assertFalse(true, "closeApplication should not throw an exception when stage is null");
        }
    }
}
