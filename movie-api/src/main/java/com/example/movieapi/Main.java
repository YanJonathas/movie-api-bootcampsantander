package com.example.movieapi;

import com.example.movieapi.http.MovieHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/movies", new MovieHandler());

        server.setExecutor(null); // usa o executor padrão
        server.start();

        System.out.println("🎬 Movie API rodando em http://localhost:" + port + "/movies");
    }
}
