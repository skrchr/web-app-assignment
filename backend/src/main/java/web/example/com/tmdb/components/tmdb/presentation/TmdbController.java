package web.example.com.tmdb.components.tmdb.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.example.com.tmdb.components.tmdb.business.boundary.TmdbApiService;
import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.List;

/**
 * REST Controller for TMDB movie operations
 * Exposes endpoints to interact with The Movie Database API
 */
@RestController
@RequestMapping("/api/v1/movies")
@CrossOrigin(origins = "*")
public class TmdbController {

    private final TmdbApiService tmdbApiService;

    public TmdbController(TmdbApiService tmdbApiService) {
        this.tmdbApiService = tmdbApiService;
    }

    /**
     * Get popular movies
     * GET /api/v1/movies/popular
     * 
     * @return List of popular movies
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MovieDto>> getPopularMovies() {
        List<MovieDto> movies = tmdbApiService.getPopularMovies();
        return ResponseEntity.ok(movies);
    }

    /**
     * Search movies by query
     * GET /api/v1/movies/search?query={query}&page={page}&language={language}
     * 
     * @param query    Search query (required)
     * @param page     Page number (optional, default: 1)
     * @param language Language code (optional, default: en)
     * @return Search response with metadata and matching movies
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchMovies(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "en") String language) {

        TmdbSearchRequest searchRequest = TmdbSearchRequest.builder()
                .query(query)
                .page(page)
                .language(language)
                .build();

        SearchResponse response = tmdbApiService.searchMovies(searchRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Get movie details by ID
     * GET /api/v1/movies/{id}
     * 
     * @param id TMDB movie ID
     * @return Movie details
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieDetails(@PathVariable Long id) {
        MovieDto movie = tmdbApiService.getMovieDetails(id);
        return ResponseEntity.ok(movie);
    }
}
