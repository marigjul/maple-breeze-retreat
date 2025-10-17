package booking.core;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The {@code ValidationTest} class contains unit tests for the {@link InputValidation} class,
 * testing validation methods for email, name, treatment, and booking date.
 */
public class ValidationTest {

    private InputValidation validation;

    /**
     * Initializes a new {@code InputValidation} instance before each test to ensure
     * a fresh instance for each test case.
     */
    @BeforeEach
    public void setUp() {
        validation = new InputValidation();
    }

    /**
     * Tests {@code emailValidation()} to ensure that valid emails return {@code true} 
     * and invalid emails return {@code false}.
     */
    @Test
    public void testEmailValidation() {
        assertTrue(validation.emailValidation("valid.email@gmail.com"));
        assertFalse(validation.emailValidation("2"));
        assertFalse(validation.emailValidation("hey hey"));
        assertFalse(validation.emailValidation("Per Persen"));
        assertFalse(validation.emailValidation(" "));
        assertFalse(validation.emailValidation("valid@"));
        assertFalse(validation.emailValidation(null));
    }

    /**
     * Tests {@code nameValidation()} to ensure that valid names return {@code true} 
     * and invalid names return {@code false}.
     */
    @Test
    public void testNameValidation() {
        assertTrue(validation.nameValidation("per persen"));
        assertTrue(validation.nameValidation("Per Persen"));
        assertTrue(validation.nameValidation("per per-per"));
        assertFalse(validation.nameValidation("per2"));
        assertFalse(validation.nameValidation("per+ persen"));
        assertFalse(validation.nameValidation(""));
        assertFalse(validation.nameValidation(null));
    }

    /**
     * Tests {@code treatmentValidation()} to confirm that non-null treatments return
     * {@code true} and null treatments return {@code false}.
     */
    @Test
    public void testTreatmentValidation() {
        assertTrue(validation.treatmentValidation(""));
        assertFalse(validation.treatmentValidation(null));
    }

    /**
     * Tests {@code dateValidation()} to ensure that valid dates (today or future) return 
     * {@code true} and past dates or null return {@code false}.
     */
    @Test
    public void testDateValidation() {
        LocalDate today = LocalDate.now();
        LocalDate twoDaysAfter = LocalDate.now().plusDays(2);
        LocalDate twoDaysBefore = LocalDate.now().minusDays(2);

        assertTrue(validation.dateValidation(today));
        assertTrue(validation.dateValidation(twoDaysAfter));
        assertFalse(validation.dateValidation(twoDaysBefore));
        assertFalse(validation.dateValidation(null));
    }
}
