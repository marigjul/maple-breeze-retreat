package booking.core;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * The {@code BookingTest} class contains unit tests for the {@link Booking} class,
 * testing its constructor, getters, and {@code toString()} method.
 */
public class BookingTest {

  /**
   * Tests the {@code toString()} method to verify that it returns a correctly formatted string
   * representation of booking details.
   */
  @Test
  public void testToString() {
    int bookingNumber = 1;
    String name = "Kari Hansen";
    String email = "kari@hansen.no";
    String treatment = "Pedicure";
    LocalDate date = LocalDate.now().plusDays(10);

    Booking newBooking = new Booking(bookingNumber, name, email, treatment, date);

    String expectedToString = "1 ; Kari Hansen ; kari@hansen.no ; Pedicure ; " 
                               + LocalDate.now().plusDays(10).toString();

    assertEquals(expectedToString, newBooking.toString());
  }

  /**
   * Tests the parameterized constructor of the {@code Booking} class to ensure
   * that attributes are initialized as expected.
   */
  @Test
  public void testConstructor() {
    int bookingNumber = 1;
    String name = "Per Persen";
    String email = "per@gmail.com";
    String treatment = "Full body massage";
    LocalDate date = LocalDate.now().plusDays(5);

    Booking newBooking = new Booking(bookingNumber, name, email, treatment, date);

    assertEquals(bookingNumber, newBooking.getBookingNumber());
    assertEquals(name, newBooking.getName());
    assertEquals(email, newBooking.getEmail());
    assertEquals(treatment, newBooking.getTreatment());
    assertEquals(date, newBooking.getDate());
  }

  /**
   * Tests the default constructor, verifying that booking number is initialized to 1
   * and that other fields have default values.
   */
  @Test
  public void testDefaultConstructor() {
    Booking booking = new Booking();

    assertEquals(1, booking.getBookingNumber());
    assertNull(booking.getName());
    assertNull(booking.getEmail());
    assertNull(booking.getTreatment());
    assertNull(booking.getDate());
  }
}
