package com.example.movieapi.strategy;

import com.example.movieapi.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class GenreFilterStrategy implements FilterStrategy {

    @Override
    public List<Movie> apply(List<Movie> movies, String value) {
        if (value == null || value.isBlank()) return movies;
        return movies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(value))
                .collect(Collectors.toList());
    }
}
