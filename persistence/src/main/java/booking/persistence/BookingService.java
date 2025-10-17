package booking.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import booking.core.Booking;

/**
 * Service class for managing Booking data.
 * Handles serialization and deserialization of Booking objects to/from JSON files.
 */
public class BookingService {
    private static final Path PROJECT_ROOT_PATH = getProjectRootPath("booking");
    private static final Path DATA_FILE_PATH = PROJECT_ROOT_PATH.resolve("persistence/data/booking.json");
    private final File filepath;
    private final List<Booking> bookings = new ArrayList<>();
    private final ObjectMapper objectMapper;

    private static Path getProjectRootPath(String projectDirName) {
        Path path = Paths.get("").toAbsolutePath();
        while (path != null) {
            Path fileNamePath = path.getFileName();
            if (fileNamePath != null && fileNamePath.toString().equals(projectDirName)) {
                break;
            }
            path = path.getParent();
        }
        if (path == null) {
            throw new RuntimeException("Project root directory '" + projectDirName + "' not found in path "
                    + Paths.get("").toAbsolutePath());
        }
        return path;
    }

    public BookingService() {
        this.filepath = DATA_FILE_PATH.toFile();
        System.out.println("Booking file absolute path: " + this.filepath.getAbsolutePath());

        File dataDir = this.filepath.getParentFile();
        if (!dataDir.exists()) {
            boolean dirsCreated = dataDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("Failed to create data directory at: " + dataDir.getAbsolutePath());
            }
        }

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        loadBookingsFromFile();
    }

    /**
     * Deserializes Booking objects from the JSON file.
     *
     * @return A list of existing Bookings.
     */
    public final List<Booking> loadBookingsFromFile() {
        bookings.clear();
        if (filepath.exists()) {
            try {
                List<Booking> existingBookings = objectMapper.readValue(filepath, new BookingListTypeReference());
                if (!existingBookings.isEmpty()) {
                    bookings.addAll(existingBookings);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Booking file not found at: " + filepath.getAbsolutePath());
        }
        return Collections.unmodifiableList(new ArrayList<>(bookings));
    }

    /**
     * Adds a new Booking and serializes the updated list to the JSON file.
     *
     * @param booking The Booking to add.
     * @return The added Booking.
     */
    public Booking addBooking(Booking booking) {
        if (booking != null) {
            bookings.add(booking);
            writeToJSONFile();
        }
        return booking;
    }

    /**
     * Retrieves all Bookings.
     *
     * @return A list of all Bookings.
     */
    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(new ArrayList<>(bookings));
    }

    /**
     * Retrieves a list of bookings that match the email of the last added booking.
     * <p>
     * This method filters the list of bookings to find all entries where the email address 
     * matches the email of the most recently added booking (i.e., the last booking in the list).
     * The comparison is case-insensitive.
     * </p>
     * 
     * @return A new {@link List} containing all bookings with the email address 
     *         of the most recent booking. If no bookings match the email, 
     *         an empty list will be returned.
     */
    public List<Booking> getBookingsByEmail() {
        String emailToUse = bookings.get(bookings.size() - 1).getEmail();
        return new ArrayList<>(bookings.stream()
                .filter(b -> b.getEmail()
                        .equalsIgnoreCase(emailToUse))
                .toList());
    }

    /**
     * Serializes the current list of Bookings to the JSON file.
     */
    public void writeToJSONFile() {
        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(filepath, bookings); // Overwrite the file with updated content
            System.out.println("Successfully wrote bookings to " + filepath.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write bookings to JSON file: " + filepath.getAbsolutePath());
        }
    }

    /**
     * Static nested class to provide type information for deserialization.
     */
    private static class BookingListTypeReference extends TypeReference<List<Booking>> {
        // No additional implementation needed
    }
}
