package web.example.com.tmdb.components.tmdb.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import web.example.com.tmdb.components.tmdb.business.boundary.TmdbApiService;
import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchMetadata;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TmdbController
 * Tests the REST endpoints using MockMvc
 */
@WebMvcTest(TmdbController.class)
class TmdbControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TmdbApiService tmdbApiService;

    @Test
    void getPopularMovies_ShouldReturnMovieList() throws Exception {
        // Given
        List<MovieDto> movies = Arrays.asList(
                createMovieDto(1L, "Movie 1"),
                createMovieDto(2L, "Movie 2"));
        when(tmdbApiService.getPopularMovies()).thenReturn(movies);

        // When & Then
        mockMvc.perform(get("/api/v1/movies/popular")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Movie 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Movie 2"));

        verify(tmdbApiService).getPopularMovies();
    }

    @Test
    void searchMovies_WithQueryOnly_ShouldUseDefaults() throws Exception {
        // Given
        SearchResponse searchResponse = SearchResponse.builder()
                .meta(SearchMetadata.builder()
                        .page(1)
                        .totalPages(5)
                        .totalResults(100)
                        .build())
                .data(Arrays.asList(createMovieDto(1L, "Search Result")))
                .build();
        when(tmdbApiService.searchMovies(any(TmdbSearchRequest.class))).thenReturn(searchResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/movies/search")
                .param("query", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.totalPages").value(5))
                .andExpect(jsonPath("$.meta.totalResults").value(100))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Search Result"));

        verify(tmdbApiService).searchMovies(any(TmdbSearchRequest.class));
    }

    @Test
    void searchMovies_WithAllParameters_ShouldPassThemToService() throws Exception {
        // Given
        SearchResponse searchResponse = SearchResponse.builder()
                .meta(SearchMetadata.builder()
                        .page(2)
                        .totalPages(10)
                        .totalResults(200)
                        .build())
                .data(Arrays.asList(createMovieDto(1L, "French Movie")))
                .build();
        when(tmdbApiService.searchMovies(any(TmdbSearchRequest.class))).thenReturn(searchResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/movies/search")
                .param("query", "inception")
                .param("page", "2")
                .param("language", "fr")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.page").value(2))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1));

        verify(tmdbApiService).searchMovies(any(TmdbSearchRequest.class));
    }

    @Test
    void searchMovies_WithEmptyResults_ShouldReturnEmptyArray() throws Exception {
        // Given
        SearchResponse searchResponse = SearchResponse.builder()
                .meta(SearchMetadata.builder()
                        .page(1)
                        .totalPages(0)
                        .totalResults(0)
                        .build())
                .data(List.of())
                .build();
        when(tmdbApiService.searchMovies(any(TmdbSearchRequest.class))).thenReturn(searchResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/movies/search")
                .param("query", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.totalResults").value(0))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getMovieDetails_WithValidId_ShouldReturnMovie() throws Exception {
        // Given
        MovieDto movie = createMovieDto(603L, "The Matrix");
        when(tmdbApiService.getMovieDetails(603L)).thenReturn(movie);

        // When & Then
        mockMvc.perform(get("/api/v1/movies/603")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(603))
                .andExpect(jsonPath("$.title").value("The Matrix"));

        verify(tmdbApiService).getMovieDetails(603L);
    }

    // Helper method to create test MovieDto objects
    private MovieDto createMovieDto(Long id, String title) {
        return MovieDto.builder()
                .id(id)
                .title(title)
                .overview("Test overview")
                .releaseDate("2024-01-01")
                .voteAverage(7.5)
                .voteCount(1000)
                .popularity(100.0)
                .posterPath("/poster.jpg")
                .backdropPath("/backdrop.jpg")
                .adult(false)
                .originalLanguage("en")
                .originalTitle(title)
                .build();
    }
}
