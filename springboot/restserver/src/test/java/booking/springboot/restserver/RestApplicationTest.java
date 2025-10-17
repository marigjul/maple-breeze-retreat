package booking.springboot.restserver;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RestApplication}.
 * <p>
 * This test ensures that the Spring application context loads successfully.
 * </p>
 */
class RestApplicationTest {

    /**
     * Test to ensure that the Spring Boot application context loads correctly.
     * <p>
     * This test calls the main method of the application, which triggers the
     * Spring Boot application context loading.
     * </p>
     */
    @Test
    void contextLoads() {
        RestApplication.main(new String[] {}); // Ensure the application context loads without errors
    }

}
