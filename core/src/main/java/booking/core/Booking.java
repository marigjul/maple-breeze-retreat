package booking.core;

import java.time.LocalDate;

/**
 * The {@code Booking} class represents a booking made by a customer.
 * It stores the customer's name, email, the selected treatment, the booking
 * date, and a unique ID for the booking.
 * The class automatically assigns a unique ID to each booking based on the
 * highest assigned ID.
 */
public class Booking {

    private int bookingNumber;
    private String name;
    private String email;
    private String treatment;
    private LocalDate date;

    /**
     * Constructs a new {@code Booking} with the specified name, email, treatment,
     * and date.
     * Each booking is automatically assigned a unique ID.
     *
     * @param name      The name of the customer.
     * @param email     The email of the customer.
     * @param treatment The treatment selected by the customer.
     * @param date      The date of the booking.
     */
    public Booking(int bookingNumber, String name, String email, String treatment, LocalDate date) {
        this.bookingNumber = bookingNumber;
        this.name = name;
        this.email = email;
        this.treatment = treatment;
        this.date = date;
    }

    /**
     * Default constructor for creating an empty {@code Booking} instance.
     * 
     * Jackson requires a no-argument constructor to create instances
     * of objects when deserializing JSON into Java objects.
     */
    public Booking() {
        bookingNumber = 1;
    }

    /**
     * Gets the unique booking number.
     *
     * @return The unique booking number.
     */
    public int getBookingNumber() {
        return bookingNumber;
    }

    /**
     * Gets the name of the customer who made the booking.
     *
     * @return The customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email of the customer who made the booking.
     *
     * @return The customer's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the treatment selected by the customer for the booking.
     *
     * @return The treatment selected.
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Gets the date of the booking.
     *
     * @return The date of the booking.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns a string representation of the booking details, including the ID,
     * customer's name, email, selected treatment, and the date of the booking.
     *
     * @return A string representing the booking.
     */
    @Override
    public String toString() {
        return bookingNumber + " ; " + name + " ; " + email + " ; " + treatment + " ; " + date.toString();
    }
}