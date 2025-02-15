package com.hxgfk.cwm.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hxgfk.cwm.main.Config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;

public class ModelAPI {
    public static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static RequestResult sendMessage(LinkedList<MessageEntry> all, float temperature) throws IOException, InterruptedException {
        JsonObject base = new JsonObject();
        JsonArray messages = new JsonArray();
        for (MessageEntry entry : all) {
            JsonObject msgObj = new JsonObject();
            msgObj.addProperty("role", entry.type().type());
            msgObj.addProperty("content", entry.content());
            messages.add(msgObj);
        }
        base.add("messages", messages);
        base.addProperty("model", Config.model);
        base.addProperty("temperature", temperature);
        Gson gson = new Gson();
        String stringJson = gson.toJson(base);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.base_url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Config.key)
                .POST(HttpRequest.BodyPublishers.ofString(stringJson))
                .build();
        HttpResponse response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return new RequestResult(response.statusCode(), (String) response.body());
    }
}
