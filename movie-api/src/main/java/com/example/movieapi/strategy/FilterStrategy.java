package com.example.movieapi.strategy;

import com.example.movieapi.model.Movie;

import java.util.List;

/**
 * Strategy Pattern: define um contrato comum para diferentes formas
 * de filtrar/ordenar a lista de filmes. Cada implementação concreta
 * decide "a sua forma" de organizar os filmes.
 */
public interface FilterStrategy {
    List<Movie> apply(List<Movie> movies, String value);
}
