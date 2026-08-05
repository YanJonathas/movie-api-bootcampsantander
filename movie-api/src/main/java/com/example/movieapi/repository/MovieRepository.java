package com.example.movieapi.repository;

import com.example.movieapi.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton Pattern: garante que exista APENAS uma instância do
 * repositório de filmes em toda a aplicação, evitando estados
 * duplicados/conflitantes na "base de dados" em memória.
 */
public final class MovieRepository {

    private static volatile MovieRepository instance;

    private final List<Movie> movies = new CopyOnWriteArrayList<>();

    private MovieRepository() {
        seed();
    }

    public static MovieRepository getInstance() {
        if (instance == null) {
            synchronized (MovieRepository.class) {
                if (instance == null) {
                    instance = new MovieRepository();
                }
            }
        }
        return instance;
    }

    public Movie save(Movie movie) {
        movies.add(movie);
        return movie;
    }

    public List<Movie> findAll() {
        return new ArrayList<>(movies);
    }

    public Optional<Movie> findById(String id) {
        return movies.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public boolean deleteById(String id) {
        return movies.removeIf(m -> m.getId().equals(id));
    }

    private void seed() {
        movies.add(new Movie.Builder().title("O Poderoso Chefão").genre("Drama").year(1972).rating(9.2).build());
        movies.add(new Movie.Builder().title("De Volta para o Futuro").genre("Ficção").year(1985).rating(8.5).build());
        movies.add(new Movie.Builder().title("Toy Story").genre("Animação").year(1995).rating(8.3).build());
    }
}
