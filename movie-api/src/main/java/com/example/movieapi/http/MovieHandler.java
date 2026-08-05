package com.example.movieapi.http;

import com.example.movieapi.factory.MovieFactory;
import com.example.movieapi.model.Movie;
import com.example.movieapi.repository.MovieRepository;
import com.example.movieapi.strategy.FilterStrategy;
import com.example.movieapi.strategy.GenreFilterStrategy;
import com.example.movieapi.strategy.RatingSortStrategy;
import com.example.movieapi.strategy.YearFilterStrategy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HttpHandler responsável pelas rotas de /movies:
 *   GET    /movies              -> lista (com filtros opcionais via query string)
 *   GET    /movies/{id}         -> busca por id
 *   POST   /movies              -> cria um filme
 *   DELETE /movies/{id}         -> remove um filme
 */
public class MovieHandler implements HttpHandler {

    private final MovieRepository repository = MovieRepository.getInstance();
    private final MovieFactory factory = new MovieFactory();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method) && path.equals("/movies")) {
                handleList(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.startsWith("/movies/")) {
                handleGetById(exchange, extractId(path));
            } else if ("POST".equalsIgnoreCase(method) && path.equals("/movies")) {
                handleCreate(exchange);
            } else if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/movies/")) {
                handleDelete(exchange, extractId(path));
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Rota não encontrada\"}");
            }
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"Erro interno: " + e.getMessage() + "\"}");
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        Map<String, String> query = parseQuery(exchange.getRequestURI());
        List<Movie> movies = repository.findAll();

        // Strategy Pattern em ação: escolhe a estratégia de acordo com o parâmetro recebido
        if (query.containsKey("genre")) {
            FilterStrategy strategy = new GenreFilterStrategy();
            movies = strategy.apply(movies, query.get("genre"));
        }
        if (query.containsKey("year")) {
            FilterStrategy strategy = new YearFilterStrategy();
            movies = strategy.apply(movies, query.get("year"));
        }
        if (query.containsKey("sortByRating")) {
            FilterStrategy strategy = new RatingSortStrategy();
            movies = strategy.apply(movies, query.get("sortByRating"));
        }

        String json = movies.stream().map(Movie::toJson).collect(Collectors.joining(",", "[", "]"));
        sendResponse(exchange, 200, json);
    }

    private void handleGetById(HttpExchange exchange, String id) throws IOException {
        var movie = repository.findById(id);
        if (movie.isPresent()) {
            sendResponse(exchange, 200, movie.get().toJson());
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Filme não encontrado\"}");
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Movie movie = factory.createFromJson(body);
        repository.save(movie);
        sendResponse(exchange, 201, movie.toJson());
    }

    private void handleDelete(HttpExchange exchange, String id) throws IOException {
        boolean removed = repository.deleteById(id);
        if (removed) {
            sendResponse(exchange, 200, "{\"message\":\"Filme removido com sucesso\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Filme não encontrado\"}");
        }
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private Map<String, String> parseQuery(URI uri) {
        String query = uri.getQuery();
        if (query == null || query.isBlank()) return Map.of();
        return java.util.Arrays.stream(query.split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv.length > 1 ? kv[1] : ""));
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
