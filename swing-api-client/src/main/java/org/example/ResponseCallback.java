package org.example;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface ResponseCallback {
    void onResponse(JsonNode response);
}