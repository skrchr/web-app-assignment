package web.example.com.tmdb.components.tmdb.business.boundary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for TmdbApiService
 * These tests call the REAL TMDB API to verify end-to-end functionality
 * 
 * Requirements:
 * - Set TMDB_API_KEY environment variable
 * - Internet connection required
 * - Tests may be slow due to real API calls
 */
@SpringBootTest
@ActiveProfiles("integration")
class TmdbApiServiceIntegrationTest {

    @Autowired
    private TmdbApiService tmdbApiService;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Test
    void verifyApiKeyIsLoaded() {
        // This test helps debug if API key is being loaded
        System.out.println("API Key loaded: "
                + (apiKey != null && !apiKey.isEmpty() ? "YES (length: " + apiKey.length() + ")" : "NO - EMPTY!"));
        assertThat(apiKey).isNotNull();
        assertThat(apiKey).isNotEmpty();
    }

    @Test
    void getPopularMovies_ShouldReturnMovies_WhenApiKeyIsValid() {
        // Given - Real API call with valid API key

        // When - Call the real service
        List<MovieDto> movies = tmdbApiService.getPopularMovies();

        // Then - Verify we get real data from TMDB
        assertThat(movies).isNotNull();
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isGreaterThan(0);

        // Verify first movie has expected structure
        MovieDto firstMovie = movies.get(0);
        assertThat(firstMovie.getId()).isNotNull();
        assertThat(firstMovie.getTitle()).isNotBlank();
        assertThat(firstMovie.getOverview()).isNotNull();
        assertThat(firstMovie.getReleaseDate()).isNotNull();
        assertThat(firstMovie.getVoteAverage()).isNotNull();
        assertThat(firstMovie.getPosterPath()).isNotNull();

    }

    @Test
    void searchMovies_ShouldReturnResults_WhenSearchingForPopularMovie() {
        // Given - Search for a well-known movie
        TmdbSearchRequest searchRequest = TmdbSearchRequest.builder()
                .query("The Matrix")
                .page(1)
                .language("en")
                .build();

        // When - Call the real search service
        SearchResponse response = tmdbApiService.searchMovies(searchRequest);

        // Then - Verify we get search results
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getMeta()).isNotNull();

        // Verify we found The Matrix
        boolean foundMatrix = response.getData().stream()
                .anyMatch(movie -> movie.getTitle().toLowerCase().contains("matrix"));
        assertThat(foundMatrix).isTrue();

    }

    @Test
    void getMovieDetails_ShouldReturnFullDetails_WhenMovieExists() {
        // Given - Use a well-known movie ID (The Matrix = 603)
        Long matrixMovieId = 603L;

        // When - Call the real movie details service
        MovieDto movieDetails = tmdbApiService.getMovieDetails(matrixMovieId);

        // Then - Verify we get complete movie details
        assertThat(movieDetails).isNotNull();
        assertThat(movieDetails.getId()).isEqualTo(matrixMovieId);
        assertThat(movieDetails.getTitle()).isEqualTo("The Matrix");
        assertThat(movieDetails.getOverview()).isNotBlank();
        assertThat(movieDetails.getReleaseDate()).isEqualTo("1999-03-31");
        assertThat(movieDetails.getVoteAverage()).isNotNull();
        assertThat(movieDetails.getVoteCount()).isNotNull();
        assertThat(movieDetails.getRuntime()).isNotNull();
        assertThat(movieDetails.getGenres()).isNotNull();
        assertThat(movieDetails.getGenres()).isNotEmpty();

    }

    @Test
    void searchMovies_ShouldReturnEmptyResults_WhenSearchingForNonExistentMovie() {
        // Given - Search for a movie that doesn't exist
        TmdbSearchRequest searchRequest = TmdbSearchRequest.builder()
                .query("ThisMovieDefinitelyDoesNotExist12345")
                .page(1)
                .language("en")
                .build();

        // When - Call the real search service
        SearchResponse response = tmdbApiService.searchMovies(searchRequest);

        // Then - Verify we get empty results
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEmpty();

    }

    @Test
    void getMovieDetails_ShouldThrowException_WhenMovieNotFound() {
        // Given - Use a non-existent movie ID
        Long nonExistentMovieId = 999999999L;

        // When/Then - Verify exception is thrown
        try {
            tmdbApiService.getMovieDetails(nonExistentMovieId);
            // If we get here, the test should fail
            assertThat(false).as("Expected exception for non-existent movie").isTrue();
        } catch (RuntimeException e) {
            // This is expected
            assertThat(e.getMessage()).contains("Failed to fetch movie details");
        }
    }
}
