package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.enums.TicketStatus;
import org.example.models.TicketRequest;
import org.example.models.Token;
import org.example.models.UserCreateRequest;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> void executeRequest(ClassicHttpRequest request, Consumer<JsonNode> onSuccess, Consumer<String> onError) {
        new Thread(() -> {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                Token token = TokenHolder.getInstance().getToken();
                if (token != null) {
                    request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAuthToken());
                }

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                    if (response.getCode() >= 200 && response.getCode() < 300) {
                        SwingUtilities.invokeLater(() -> onSuccess.accept(jsonNode));
                    } else {
                        SwingUtilities.invokeLater(() -> onError.accept("Error " + response.getCode() + ": " + jsonResponse));
                    }
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            }
        }).start();
    }

    public void login(String email, String password, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws ProtocolException, IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(URI.create(BASE_URL + "/login"));
            String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                Header accessToken = response.getHeader("access_token");
                if (accessToken != null) {
                    TokenHolder.getInstance().setToken(new Token(accessToken.getValue()));
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                    SwingUtilities.invokeLater(() -> onSuccess.accept(jsonNode));
                }
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void register(UserCreateRequest request, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws ProtocolException, IOException {
        HttpPost httpPost = new HttpPost(URI.create(BASE_URL + "/register"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = Map.of(
                "name", request.getName(),
                "email", request.getEmail(),
                "password", request.getPassword(),
                "confirm_password", request.getConfirmPassword(),
                "role", request.getRole().toString()
        );
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String json = objectMapper.writeValueAsString(jsonMap);
        httpPost.setEntity(new StringEntity
                (json, StandardCharsets.UTF_8));

        executeRequest(httpPost, onSuccess, onError);
    }

    public void getTickets(TicketStatus ticketStatus, Consumer<JsonNode> onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/api/tickets";
        if (ticketStatus != TicketStatus.ALL) {
            url += "?ticket_status=" + ticketStatus.toString();
        }
        HttpGet httpGet = new HttpGet(URI.create(url));
        executeRequest(httpGet, onSuccess, onError);
    }

    public void createTickets(TicketRequest ticketRequest, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws JsonProcessingException {
        HttpPost httpPost = new HttpPost(URI.create(BASE_URL + "/api/tickets"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = Map.of(
                "title", ticketRequest.getTitle(),
                "description", ticketRequest.getDescription(),
                "priority", ticketRequest.getPriority().toString(),
                "category", ticketRequest.getCategory().toString()
        );
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String json = objectMapper.writeValueAsString(jsonMap);
        httpPost.setEntity(new StringEntity
                (json, StandardCharsets.UTF_8));
        executeRequest(httpPost, onSuccess, onError);
    }

    public void getTicketById(Long ticketId, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws JsonProcessingException {
        String url = BASE_URL + "/api/tickets/" + ticketId;
        executeRequest(new HttpGet(URI.create(url)), onSuccess, onError);
    }

    public void changeStatus(Long ticketId, TicketStatus ticketStatus, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws JsonProcessingException {
        HttpPut httpPut = new HttpPut(URI.create(BASE_URL + "/api/tickets/" + ticketId));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = Map.of(
                "ticket_status", ticketStatus.toString()
        );
        httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String json = objectMapper.writeValueAsString(jsonMap);
        httpPut.setEntity(new StringEntity
                (json, StandardCharsets.UTF_8));
        executeRequest(httpPut, onSuccess, onError);
    }


    public void addComments(Long ticketId, String commentText, Consumer<JsonNode> onSuccess, Consumer<String> onError) throws JsonProcessingException {
        HttpPost httpPost = new HttpPost(URI.create(BASE_URL + "/api/tickets/" + ticketId + "/comments"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = Map.of(
                "text", commentText
        );
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String json = objectMapper.writeValueAsString(jsonMap);
        httpPost.setEntity(new StringEntity
                (json, StandardCharsets.UTF_8));
        executeRequest(httpPost, onSuccess, onError);
    }

}
