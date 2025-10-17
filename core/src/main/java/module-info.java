/**
 * This module is the core of the booking system.
 * 
 * It contains the main logic for handling bookings, such as validating
 * input, managing booking information, and other core functionalities.
 * 
 * The {@code booking.core} package is shared with other parts of the
 * application so that the UI and Spring Boot/REST server layers can use 
 * the booking logic.
 * 
 * The Jackson library (used for converting data to/from JSON) is also 
 * allowed access to the {@code booking.core} package for reading and writing 
 * booking data.
 */
module booking.core {

    // Makes the booking.core package available to other modules like UI and persistence
    exports booking.core;

    // Allows the Jackson library to access classes inside booking.core for JSON handling
    opens booking.core to com.fasterxml.jackson.databind;

    requires transitive com.fasterxml.jackson.databind;
}
