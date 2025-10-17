package booking.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ApiClient} class.
 * These tests use Mockito to mock HTTP interactions and verify the
 * functionality of the ApiClient methods.
 */
class ApiClientTest {

    private ApiClient apiClient;
    private HttpClient mockHttpClient;
    private HttpResponse<String> mockResponse;

    /**
     * Sets up the test environment before each test.
     * Initializes mocked instances of {@link HttpClient} and {@link HttpResponse},
     * and injects the mocked client into the {@link ApiClient}.
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Mock HttpClient and HttpResponse
        mockHttpClient = mock(HttpClient.class);
        mockResponse = mock(HttpResponse.class);

        // Create new ApiClient instance and inject mocked HttpClient via reflection
        apiClient = new ApiClient();

        // Inject mock HttpClient using reflection
        Field clientField = ApiClient.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(apiClient, mockHttpClient);
    }

    /**
     * Tests {@link ApiClient#get(String)} to ensure it correctly processes an HTTP
     * GET request and returns the expected response.
     * 
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testGetRequestSuccess() throws IOException, InterruptedException {
        // Mock the HTTP GET response
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("Success");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Execute the method and verify the result
        String result = apiClient.get("/test");
        assertEquals("Success", result, "The response should be 'Success'");
    }

    /**
     * Tests {@link ApiClient#post(String, String)} to ensure it correctly processes
     * an HTTP POST request and returns the expected response.
     *
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testPostRequestSuccess() throws IOException, InterruptedException {
        // Mock the HTTP POST response
        when(mockResponse.statusCode()).thenReturn(201);
        when(mockResponse.body()).thenReturn("Created");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Execute and verify the result
        String result = apiClient.post("/test", "{\"key\":\"value\"}");
        assertEquals("Created", result, "The response should be 'Created'");
    }

    /**
     * Tests {@link ApiClient#put(String, String)} to ensure it correctly processes
     * an HTTP PUT request and returns the expected response.
     *
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testPutRequestSuccess() throws IOException, InterruptedException {
        // Mock the HTTP PUT response
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("Updated");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Execute and verify the result
        String result = apiClient.put("/test", "{\"key\":\"newValue\"}");
        assertEquals("Updated", result, "The response should be 'Updated'");
    }

    /**
     * Tests {@link ApiClient#delete(String)} to ensure it correctly processes an
     * HTTP DELETE request and returns the expected response.
     *
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testDeleteRequestSuccess() throws IOException, InterruptedException {
        // Mock the HTTP DELETE response
        when(mockResponse.statusCode()).thenReturn(204);
        when(mockResponse.body()).thenReturn("");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Execute and verify the result
        String result = apiClient.delete("/test");
        assertEquals("", result, "The response should be empty for a successful DELETE request");
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown for an invalid HTTP
     * method.
     */
    @Test
    void testSendRequestInvalidMethod() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            apiClient.sendRequest("INVALID", "/test", null);
        });

        // Verify the exception message
        assertEquals("Invalid HTTP method: INVALID", exception.getMessage());
    }

    /**
     * Tests that a {@link RuntimeException} is thrown for a non-2xx HTTP response
     * code.
     *
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testSendRequestHttpError() throws IOException, InterruptedException {
        // Mock HTTP response with non-2xx status code
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn("Internal Server Error");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            apiClient.get("/test");
        });

        // Verify the exception message
        assertEquals("HTTP Error: 500 - Internal Server Error", exception.getMessage());
    }

    /**
     * Tests that a redirection response (status 300) results in a RuntimeException.
     */
    @Test
    void testSendRequestRedirectionResponse() throws IOException, InterruptedException {
        // Mock HTTP response with a redirection status code
        when(mockResponse.statusCode()).thenReturn(300);
        when(mockResponse.body()).thenReturn("Redirected");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            apiClient.get("/test");
        });

        // Verify the exception message
        assertEquals("HTTP Error: 300 - Redirected", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when a POST request
     * has no body.
     */
    @Test
    void testPostRequestWithoutBody() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            apiClient.post("/test", null);
        });

        assertEquals("POST request requires a body.", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when a PUT request
     * has no body.
     */
    @Test
    void testPutRequestWithoutBody() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            apiClient.put("/test", null);
        });

        assertEquals("PUT request requires a body.", exception.getMessage());
    }

    /**
     * Tests that status code 199 triggers a RuntimeException (boundary case).
     */
    @Test
    void testSendRequestBoundarySuccessLower() throws IOException, InterruptedException {
        // Mock HTTP response with status code 199
        when(mockResponse.statusCode()).thenReturn(199);
        when(mockResponse.body()).thenReturn("Boundary Test");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            apiClient.get("/test");
        });

        assertEquals("HTTP Error: 199 - Boundary Test", exception.getMessage());
    }

    /**
     * Tests that status code 300 triggers a RuntimeException (boundary case).
     */
    @Test
    void testSendRequestBoundarySuccessUpper() throws IOException, InterruptedException {
        // Mock HTTP response with status code 300
        when(mockResponse.statusCode()).thenReturn(300);
        when(mockResponse.body()).thenReturn("Boundary Test");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            apiClient.get("/test");
        });

        assertEquals("HTTP Error: 300 - Boundary Test", exception.getMessage());
    }

    /**
     * Tests that HTTP response codes in the successful range (200-299) return the
     * expected result.
     * 
     * @throws IOException          if an I/O error occurs during the test.
     * @throws InterruptedException if the test operation is interrupted.
     */
    @Test
    void testSendRequestSuccessfulRange() throws IOException, InterruptedException {
        // Mock HTTP response with status code 299
        when(mockResponse.statusCode()).thenReturn(299);
        when(mockResponse.body()).thenReturn("Success");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Execute and verify the result
        String result = apiClient.get("/test");
        assertEquals("Success", result, "The response should be 'Success' for status code 299");
    }
}
