package com.example.movieapi.strategy;

import com.example.movieapi.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class YearFilterStrategy implements FilterStrategy {

    @Override
    public List<Movie> apply(List<Movie> movies, String value) {
        if (value == null || value.isBlank()) return movies;
        int year = Integer.parseInt(value);
        return movies.stream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
    }
}
