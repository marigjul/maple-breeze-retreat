package booking.ui;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

/**
 * Tests the main functionality of the App class, including view transitions and
 * exception handling during controller instantiation.
 */
public class AppTest extends ApplicationTest {
    private App app;
    private Stage stage;

    /**
     * Starts the JavaFX application for testing.
     * This method is called before each test is executed.
     *
     * @param stage the primary stage for JavaFX.
     * @throws Exception if initialization fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        app = new App();
        app.start(stage);
    }

    /**
     * Tests that the application starts correctly and the initial scene is set up.
     * It verifies that the scene, stage, and key UI elements are correctly initialized.
     *
     * @throws IOException if loading resources fails.
     */
    @Test
    public void testStart() throws IOException {
        assertTrue(stage.isShowing(), "Stage should be showing after app start");

        // Verify that the scene is set
        Scene scene = stage.getScene();
        assertNotNull(scene, "Scene should not be null after app start");

        // Verify that the scene root is loaded
        Parent root = scene.getRoot();
        assertNotNull(root, "Scene root should not be null after app start");

        // Verify that specific UI elements are present
        assertNotNull(root.lookup("#orderButton"), "Order button should exist in the primary view");
    }

    /**
     * Tests switching to the secondary view and verifies UI elements are present.
     *
     * @throws IOException if loading resources fails.
     */
    @Test
    public void testSwitchToSecondary() throws IOException {
        app.setRoot("secondary", stage);
        WaitForAsyncUtils.waitForFxEvents();
        Parent root = app.getScene().getRoot();
        assertNotNull(root, "Scene root should not be null after switching to secondary view");
        assertNotNull(root.lookup("#showReceiptButton"), "Show Receipt button should exist in the secondary view");
        clickOn("#showReceiptButton");
    }

    /**
     * Tests switching to the tertiary view and verifies UI elements are present.
     *
     * @throws IOException if loading resources fails.
     */
    @Test
    public void testSwitchToTertiary() throws IOException {
        app.setRoot("tertiary", stage);
        WaitForAsyncUtils.waitForFxEvents();
        Parent root = app.getScene().getRoot();
        assertNotNull(root, "Scene root should not be null after switching to tertiary view");
        assertNotNull(root.lookup("#Receipt"), "Receipt should exist in the tertiary view");
    }

    /**
     * Verifies that the controller factory does not throw exceptions when loading
     * views.
     */
    @Test
    public void testControllerFactoryExceptions() {
        assertDoesNotThrow(() -> {
            app.setRoot("primary", stage);
            app.setRoot("secondary", stage);
            app.setRoot("tertiary", stage);
        }, "Controller factory should handle instantiation without throwing exceptions");
    }

    /**
     * Tests error handling for loading a non-existent view, expecting an exception.
     */
    @Test
    public void testControllerFactoryErrorHandling() {
        assertThrows(Exception.class, () -> {
            app.setRoot("nonExistentView", stage); // Simulate failure for non-existent view
        });
    }

    /**
     * Verifies elements are present in the tertiary view when accessed directly.
     *
     * @throws IOException if loading resources fails.
     */
    @Test
    public void testTertiaryController() throws IOException {
        app.setRoot("tertiary", stage);
        WaitForAsyncUtils.waitForFxEvents();
        Parent root = app.getScene().getRoot();
        assertNotNull(root, "Scene root should not be null after switching to tertiary view");
        assertNotNull(root.lookup("#Receipt"), "Receipt should exist in the tertiary view");
    }

    /**
     * Tests exception handling when controller instantiation fails.
     */
    @Test
    public void testControllerInstantiationErrorHandling() {
        assertThrows(RuntimeException.class, () -> {
            app.setRoot("invalidControllerView", stage);
        }, "Expected a RuntimeException due to a failed controller instantiation");
    }
}
