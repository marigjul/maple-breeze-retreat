package booking.ui;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * The {@code SecondaryController} class manages the functionality of the
 * secondary view in the application.
 * This controller handles actions related to switching to tertiary view and
 * closing the application.
 */
public class SecondaryController {
    private App appInstance;
    protected Stage stage;
    @FXML
    protected Button closeButton;

    /**
     * Sets the app instance and stage to this controller.
     */
    protected void setAppInstance(App app, Stage stage) {
        this.appInstance = app;
        this.stage = stage;
    }
    /**
     * Switches the current scene to the tertiary view.
     * 
     * @throws IOException if there is an issue loading the "tertiary" FXML file.
     */
    @FXML
    @SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "appInstance is initialized via FXML injection")
    protected void switchToTertiary() throws IOException{
        this.appInstance.setRoot("tertiary", this.stage);
    }

    /**
     * Closes the application when called.
     * This method exits the JavaFX application by calling
     * {@link javafx.application.Platform#exit()}.
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
}