package com.example.movieapi.model;

import java.util.UUID;

/**
 * Representa um filme.
 * O construtor é privado; use o Movie.Builder para criar instâncias
 * (padrão Builder).
 */
public class Movie {

    private final String id;
    private final String title;
    private final String genre;
    private final int year;
    private final double rating;

    private Movie(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.genre = builder.genre;
        this.year = builder.year;
        this.rating = builder.rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    /**
     * Converte o filme para uma linha JSON simples (sem libs externas).
     */
    public String toJson() {
        return String.format(
                "{\"id\":\"%s\",\"title\":\"%s\",\"genre\":\"%s\",\"year\":%d,\"rating\":%.1f}",
                id, escape(title), escape(genre), year, rating);
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return toJson();
    }

    // ---------- Builder Pattern ----------
    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String title;
        private String genre;
        private int year;
        private double rating;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public Movie build() {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("O filme precisa de um título.");
            }
            return new Movie(this);
        }
    }
}
