module booking.springboot.restserver {
    // Export the 'restserver' package to make it accessible to other modules
    exports booking.springboot.restserver;

    // Jackson dependencies for JSON processing
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    
    // Application core and persistence modules
    requires booking.core;
    requires booking.persistence;
    
    // Spring Framework dependencies
    requires spring.web;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    
    // SpotBugs annotations for static analysis
    requires static com.github.spotbugs.annotations;
    
    // Java logging dependencies
    requires java.logging;

    // Open the 'restserver' package to Spring and Jackson for reflection
    opens booking.springboot.restserver
            to spring.beans,
            spring.context,
            spring.web,
            spring.core,
            com.fasterxml.jackson.databind,
            spring.boot.autoconfigure,
            org.mockito;
}