package booking.core;

import java.time.LocalDate;

/**
 * The {@code InputValidation} class provides methods to validate user inputs
 * such as name, email, treatment, and date.
 * This class ensures that user-provided data meets required format
 * and constraints before processing.
 */
public class InputValidation {

    /**
     * Validates the customer's name using a regular expression.
     * The name should consist of one or more words containing only letters, 
     * and can include spaces or hyphens between words.
     *
     * @param name The customer's name to validate.
     * @return {@code true} if the name is valid, otherwise {@code false}.
     */
    public boolean nameValidation(String name) {
        String regex = "^[a-zA-Z]+([\\s-][a-zA-Z]+)*$"; // Regex to validate name format
        return name != null && name.matches(regex); // Returns true if name matches regex
    }

    /**
     * Validates the customer's email address using a regular expression.
     * The email should follow the standard format with a local part, domain, and
     * top-level domain.
     *
     * @param email The customer's email address to validate.
     * @return {@code true} if the email is valid, otherwise {@code false}.
     */
    public boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; // Regex for email format
        return email != null && email.matches(emailRegex); // Returns true if email matches regex
    }

    /**
     * Validates that a treatment has been selected.
     * Ensures the treatment is not null, indicating a valid selection.
     *
     * @param treatment The selected treatment to validate.
     * @return {@code true} if a treatment is selected, otherwise {@code false}.
     */
    public boolean treatmentValidation(String treatment) {
        return treatment != null; // Returns true if treatment is not null
    }

    /**
     * Validates the booking date.
     * The booking date must be today or a future date (it cannot be in the past).
     *
     * @param date The booking date to validate.
     * @return {@code true} if the date is today or in the future, otherwise {@code false}.
     */
    public boolean dateValidation(LocalDate date) {
        LocalDate todaysDate = LocalDate.now();
        return date != null && !date.isBefore(todaysDate); // Returns true if date is today or in the future
    }
}