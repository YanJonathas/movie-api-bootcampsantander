package com.example.movieapi.factory;

import com.example.movieapi.model.Movie;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Method: centraliza e isola a lógica de criação de um Movie
 * a partir dos dados brutos recebidos na requisição (JSON simplificado).
 * Se amanhã o filme puder vir de um XML, de um CSV etc, basta criar
 * outra factory sem mexer no resto do sistema.
 */
public class MovieFactory {

    public Movie createFromJson(String json) {
        Map<String, String> fields = parseFlatJson(json);

        Movie.Builder builder = new Movie.Builder()
                .title(fields.get("title"))
                .genre(fields.getOrDefault("genre", "Não informado"));

        if (fields.containsKey("year")) {
            builder.year(Integer.parseInt(fields.get("year")));
        }
        if (fields.containsKey("rating")) {
            builder.rating(Double.parseDouble(fields.get("rating")));
        }

        return builder.build();
    }

    /**
     * Parser bem simples só para JSON "chave: valor" de um nível
     * (suficiente para o escopo didático do projeto, sem precisar
     * de uma lib externa tipo Jackson/Gson).
     */
    private Map<String, String> parseFlatJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;

        String content = json.trim();
        if (content.startsWith("{")) content = content.substring(1);
        if (content.endsWith("}")) content = content.substring(0, content.length() - 1);

        String[] pairs = content.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replaceAll("^\"|\"$", "");
                String value = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, value);
            }
        }
        return map;
    }
}
