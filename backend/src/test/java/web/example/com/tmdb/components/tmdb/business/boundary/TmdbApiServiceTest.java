package web.example.com.tmdb.components.tmdb.business.boundary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbMovieResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbPopularMoviesResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchMoviesResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbMovieDetailsResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TmdbApiService
 * Following TDD approach - these tests will FAIL initially (RED phase)
 * Testing data transformation from external API response to our DTOs
 */
@SpringBootTest
@ActiveProfiles("test")
class TmdbApiServiceTest {

        @MockBean
        private RestTemplate restTemplate;

        @Autowired
        private TmdbApiService tmdbApiService;

        @Test
        void getPopularMovies_ShouldTransformApiResponse_WhenApiCallSucceeds() {
                // Given - Mock the TMDB Popular Movies API response
                TmdbMovieResponse movieResponse = TmdbMovieResponse.builder()
                                .id(1)
                                .title("Test Movie")
                                .overview("Test overview")
                                .releaseDate("2023-01-01")
                                .voteAverage(8.5)
                                .posterPath("/test-poster.jpg")
                                .backdropPath("/test-backdrop.jpg")
                                .voteCount(1000)
                                .popularity(75.5)
                                .adult(false)
                                .originalLanguage("en")
                                .originalTitle("Test Movie")
                                .video(false)
                                .build();

                TmdbPopularMoviesResponse apiResponse = TmdbPopularMoviesResponse.builder()
                                .results(List.of(movieResponse))
                                .page(1)
                                .totalPages(10)
                                .totalResults(200)
                                .build();

                // Mock RestTemplate to return API response
                when(restTemplate.getForObject(anyString(), eq(TmdbPopularMoviesResponse.class)))
                                .thenReturn(apiResponse);

                // When - Call our service
                List<MovieDto> result = tmdbApiService.getPopularMovies();

                // Then - Test that our service correctly transformed the data
                assertThat(result).isNotNull();
                assertThat(result).hasSize(1);

                MovieDto movie = result.get(0);
                assertThat(movie.getId()).isEqualTo(1L);
                assertThat(movie.getTitle()).isEqualTo("Test Movie");
                assertThat(movie.getOverview()).isEqualTo("Test overview");
                assertThat(movie.getReleaseDate()).isEqualTo("2023-01-01");
                assertThat(movie.getVoteAverage()).isEqualTo(8.5);
                assertThat(movie.getPosterPath()).isEqualTo("/test-poster.jpg");
                assertThat(movie.getBackdropPath()).isEqualTo("/test-backdrop.jpg");
                assertThat(movie.getVoteCount()).isEqualTo(1000);
                assertThat(movie.getPopularity()).isEqualTo(75.5);
                assertThat(movie.getAdult()).isFalse();
                assertThat(movie.getOriginalLanguage()).isEqualTo("en");
                assertThat(movie.getOriginalTitle()).isEqualTo("Test Movie");
                assertThat(movie.getVideo()).isFalse();
        }

        @Test
        void getPopularMovies_ShouldHandleEmptyResults_WhenApiReturnsEmptyList() {
                // Given - Mock empty results from API
                TmdbPopularMoviesResponse emptyApiResponse = TmdbPopularMoviesResponse.builder()
                                .results(List.of())
                                .page(1)
                                .totalPages(0)
                                .totalResults(0)
                                .build();

                when(restTemplate.getForObject(anyString(), eq(TmdbPopularMoviesResponse.class)))
                                .thenReturn(emptyApiResponse);

                // When
                List<MovieDto> result = tmdbApiService.getPopularMovies();

                // Then - Test that our service handles empty results correctly
                assertThat(result).isNotNull();
                assertThat(result).isEmpty();
        }

        @Test
        void getPopularMovies_ShouldThrowException_WhenApiCallFails() {
                // Given - Mock API failure
                when(restTemplate.getForObject(anyString(), eq(TmdbPopularMoviesResponse.class)))
                                .thenThrow(new RestClientException("Connection failed"));

                // When & Then - Test error handling
                assertThatThrownBy(() -> tmdbApiService.getPopularMovies())
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Failed to fetch popular movies");
        }

        @Test
        void searchMovies_ShouldTransformSearchResponse_WhenSearchSucceeds() {
                // Given
                TmdbSearchRequest searchRequest = TmdbSearchRequest.builder()
                                .query("batman")
                                .page(1)
                                .language("en-US")
                                .build();

                // Mock search API response
                TmdbMovieResponse movieResponse = TmdbMovieResponse.builder()
                                .id(2)
                                .title("Batman")
                                .overview("The Dark Knight")
                                .releaseDate("2022-03-04")
                                .voteAverage(9.0)
                                .posterPath("/batman-poster.jpg")
                                .build();

                TmdbSearchMoviesResponse searchResponse = TmdbSearchMoviesResponse.builder()
                                .results(List.of(movieResponse))
                                .page(1)
                                .totalPages(5)
                                .totalResults(100)
                                .build();

                when(restTemplate.getForObject(anyString(), eq(TmdbSearchMoviesResponse.class)))
                                .thenReturn(searchResponse);

                // When
                SearchResponse result = tmdbApiService.searchMovies(searchRequest);

                // Then - Test data transformation and metadata
                assertThat(result).isNotNull();
                assertThat(result.getData()).hasSize(1);
                assertThat(result.getMeta()).isNotNull();
                assertThat(result.getMeta().getPage()).isEqualTo(1);
                assertThat(result.getMeta().getTotalPages()).isEqualTo(5);
                assertThat(result.getMeta().getTotalResults()).isEqualTo(100);

                MovieDto movie = result.getData().get(0);
                assertThat(movie.getId()).isEqualTo(2L);
                assertThat(movie.getTitle()).isEqualTo("Batman");
                assertThat(movie.getOverview()).isEqualTo("The Dark Knight");
                assertThat(movie.getReleaseDate()).isEqualTo("2022-03-04");
                assertThat(movie.getVoteAverage()).isEqualTo(9.0);
                assertThat(movie.getPosterPath()).isEqualTo("/batman-poster.jpg");
        }

        @Test
        void getMovieDetails_ShouldTransformMovieDetails_WhenMovieExists() {
                // Given
                Long movieId = 123L;

                // Mock movie details API response
                TmdbMovieDetailsResponse movieDetails = TmdbMovieDetailsResponse.builder()
                                .id(123)
                                .title("Inception")
                                .overview("A mind-bending thriller")
                                .releaseDate("2010-07-16")
                                .voteAverage(8.8)
                                .voteCount(25000)
                                .popularity(85.5)
                                .posterPath("/inception-poster.jpg")
                                .backdropPath("/inception-backdrop.jpg")
                                .adult(false)
                                .originalLanguage("en")
                                .originalTitle("Inception")
                                .video(false)
                                .runtime(148)
                                .genres(List.of(
                                                TmdbMovieDetailsResponse.TmdbGenre.builder()
                                                                .id(28)
                                                                .name("Action")
                                                                .build(),
                                                TmdbMovieDetailsResponse.TmdbGenre.builder()
                                                                .id(878)
                                                                .name("Science Fiction")
                                                                .build()))
                                .build();

                when(restTemplate.getForObject(anyString(), eq(TmdbMovieDetailsResponse.class)))
                                .thenReturn(movieDetails);

                // When
                MovieDto result = tmdbApiService.getMovieDetails(movieId);

                // Then - Test data transformation
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(movieId);
                assertThat(result.getTitle()).isEqualTo("Inception");
                assertThat(result.getOverview()).isEqualTo("A mind-bending thriller");
                assertThat(result.getReleaseDate()).isEqualTo("2010-07-16");
                assertThat(result.getVoteAverage()).isEqualTo(8.8);
                assertThat(result.getVoteCount()).isEqualTo(25000);
                assertThat(result.getPopularity()).isEqualTo(85.5);
        }

        @Test
        void getMovieDetails_ShouldThrowException_WhenMovieNotFound() {
                // Given
                Long movieId = 999L;

                when(restTemplate.getForObject(anyString(), eq(TmdbMovieDetailsResponse.class)))
                                .thenThrow(new RestClientException("Not Found"));

                // When & Then
                assertThatThrownBy(() -> tmdbApiService.getMovieDetails(movieId))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Failed to fetch movie details");
        }
}