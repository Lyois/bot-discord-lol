package br.lyois.config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpConnection {

    public static HttpResponse<String> connection (String url){
    HttpResponse<String> response;
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
