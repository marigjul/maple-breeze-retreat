/**
 * The {@code booking.ui} module serves as the user interface layer for the
 * booking application. It relies on the core module for its functionality
 * and provides a graphical user interface (GUI) using JavaFX.
 */
module booking.ui {
    // Exports the booking.ui package so it can be accessed by other modules
    exports booking.ui;

    // JavaFX libraries for UI functionality
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Core application modules that are required for business logic and data handling
    requires booking.persistence;
    requires booking.core;
    requires booking.springboot.restserver;

    // Libraries for JSON processing
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // For logging functionality
    requires java.logging;

    // Static analysis library (SpotBugs annotations)
    requires static com.github.spotbugs.annotations;

    // For JavaFX base functionalities (e.g., base classes)
    requires javafx.base;

    // Allow JavaFX to access the booking.ui package for FXML and controls
    opens booking.ui to javafx.fxml, javafx.controls, javafx.graphics;
}