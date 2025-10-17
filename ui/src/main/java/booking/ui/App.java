package booking.ui;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.InstantiationException;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@code App} class is the main entry point for the JavaFX application.
 * It extends {@link javafx.application.Application} and is responsible for
 * setting up and displaying the initial stage (window) of the application.
 */
public class App extends Application {

    // setting a value for scene so it gets recognized by spotbugs
    private Scene scene = new Scene(new Group(), 640, 480); // Set an initial default value

    /**
     * This method is called when the application is launched. It sets up the
     * primary stage and scene, and loads the initial FXML layout.
     *
     * @param primaryStage The primary stage for this application, onto which the
     *                     application scene is set.
     * @throws IOException If there is an issue loading the FXML resource.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize the scene with the primary FXML layout
        this.scene = new Scene(loadFXML("primary", primaryStage), 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Clear focus after the stage is shown
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    /**
     * Sets the root of the current scene to the FXML file specified by the
     * {@code fxml} parameter.
     *
     * @param fxml The name of the FXML file to be loaded (without the .fxml
     *             extension).
     * @throws IOException If there is an issue loading the FXML resource.
     */
    @SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Scene is initialized in start() method before use.")
    public void setRoot(String fxml, Stage stage) throws IOException {
        Parent root = loadFXML(fxml, stage);
        scene.setRoot(root);
    }

    // Read-only accessors to prevent external modification
    Scene getScene() {
        return scene;
    }

    /**
     * Loads an FXML file from the resources folder.
     *
     * @param fxml The name of the FXML file to be loaded (without the .fxml
     *             extension).
     * @return The loaded {@link Parent} node representing the root of the FXML
     *         layout.
     * @throws IOException If there is an issue loading the FXML resource.
     */
    private Parent loadFXML(String fxml, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        // Set the controller factory to inject `App` and `Stage`
        fxmlLoader.setControllerFactory(controllerClass -> {
            Object controller = null;
            try {
                controller = controllerClass.getDeclaredConstructor().newInstance();
                // Inject the App instance into the controller
                if (controller instanceof PrimaryController) {
                    ((PrimaryController) controller).setAppInstance(this, stage);
                } else if (controller instanceof SecondaryController) {
                    ((SecondaryController) controller).setAppInstance(this, stage);
                } else if (controller instanceof TertiaryController) {
                    ((TertiaryController) controller).setAppInstance(this, stage);
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }

            return controller;
        });

        return fxmlLoader.load();
    }

    /**
     * The main method is the entry point of the JavaFX application. It launches the
     * application.
     *
     * @param args Command line arguments passed to the application (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}