module booking.persistence {
    // Exports the booking.persistence package for use by other modules
    exports booking.persistence;

    // Opens the booking.persistence package to specific modules for reflection (used for Jackson deserialization and testing frameworks)
    opens booking.persistence to
            com.fasterxml.jackson.databind, // For Jackson deserialization and serialization
            org.junit.platform.commons,    // For JUnit testing (reflection needed for tests)
            org.mockito;                  // For Mockito-based testing (needed for mocking with reflection)

    // Requires essential libraries and modules
    requires booking.core;                    // Core booking logic and data models
    requires java.net.http;                   // HTTP client API for communication (if needed for HTTP-based persistence)
    requires com.fasterxml.jackson.databind;  // Jackson core library for JSON processing
    requires com.fasterxml.jackson.datatype.jsr310; // Jackson module for Java 8 Date & Time API support
    requires com.fasterxml.jackson.core;      // Jackson core functionalities (for streaming, parsing, etc.)
}
