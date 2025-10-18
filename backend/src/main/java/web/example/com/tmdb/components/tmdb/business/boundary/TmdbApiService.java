package web.example.com.tmdb.components.tmdb.business.boundary;

import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.List;

/**
 * Service interface for TMDB API operations
 * Based on TMDB API documentation: https://developer.themoviedb.org/reference/
 * 
 * Endpoints:
 * - GET /movie/popular - Popular movies
 * - GET /search/movie - Search movies
 * - GET /movie/{movie_id} - Movie details
 */
public interface TmdbApiService {

    /**
     * Get popular movies from TMDB API
     * Endpoint: GET /movie/popular
     * 
     * @return List of popular movies
     */
    List<MovieDto> getPopularMovies();

    /**
     * Search movies by query
     * Endpoint: GET /search/movie
     * 
     * @param request Search request containing query parameters
     * @return Search response with metadata and movie results
     */
    SearchResponse searchMovies(TmdbSearchRequest request);

    /**
     * Get movie details by ID
     * Endpoint: GET /movie/{movie_id}
     * 
     * @param movieId The TMDB movie ID
     * @return Movie details
     */
    MovieDto getMovieDetails(Long movieId);
}
