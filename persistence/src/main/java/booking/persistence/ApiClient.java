package booking.persistence;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

public class ApiClient {

    private final HttpClient client;
    private static final String baseUrl = "http://localhost:8080/api/bookings";

    public ApiClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Sends an HTTP request with the specified method, endpoint, and body.
     *
     * @param method   The HTTP method (GET, POST, PUT, DELETE).
     * @param endpoint The API endpoint (e.g., "/add").
     * @param body     The JSON body for POST and PUT requests.
     * @return The response body as a String.
     * @throws IOException          If an I/O error occurs during the request.
     * @throws InterruptedException If the request is interrupted.
     */
    protected String sendRequest(String method, String endpoint, String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json");

        switch (method.toUpperCase(Locale.ROOT)) {
            case "GET":
                requestBuilder.GET();
                break;
            case "POST":
                if (body == null) {
                    throw new IllegalArgumentException("POST request requires a body.");
                }
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "PUT":
                if (body == null) {
                    throw new IllegalArgumentException("PUT request requires a body.");
                }
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "DELETE":
                requestBuilder.DELETE();
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }

        HttpRequest request = requestBuilder
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("HTTP Error: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Sends a GET request to the specified endpoint.
     *
     * @param endpoint The API endpoint.
     * @return The response body as a String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String get(String endpoint) throws IOException, InterruptedException {
        return sendRequest("GET", endpoint, null);
    }

    /**
     * Sends a POST request to the specified endpoint with the given body.
     *
     * @param endpoint The API endpoint.
     * @param body     The JSON body.
     * @return The response body as a String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String post(String endpoint, String body) throws IOException, InterruptedException {
        return sendRequest("POST", endpoint, body);
    }

    /**
     * Sends a PUT request to the specified endpoint with the given body.
     *
     * @param endpoint The API endpoint.
     * @param body     The JSON body.
     * @return The response body as a String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String put(String endpoint, String body) throws IOException, InterruptedException {
        return sendRequest("PUT", endpoint, body);
    }

    /**
     * Sends a DELETE request to the specified endpoint.
     *
     * @param endpoint The API endpoint.
     * @return The response body as a String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String delete(String endpoint) throws IOException, InterruptedException {
        return sendRequest("DELETE", endpoint, null);
    }
}
