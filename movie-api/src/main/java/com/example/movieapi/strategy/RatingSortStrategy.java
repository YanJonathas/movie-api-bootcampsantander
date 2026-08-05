package com.example.movieapi.strategy;

import com.example.movieapi.model.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RatingSortStrategy implements FilterStrategy {

    @Override
    public List<Movie> apply(List<Movie> movies, String value) {
        boolean descending = !"asc".equalsIgnoreCase(value);
        Comparator<Movie> comparator = Comparator.comparingDouble(Movie::getRating);
        if (descending) comparator = comparator.reversed();
        return movies.stream().sorted(comparator).collect(Collectors.toList());
    }
}
