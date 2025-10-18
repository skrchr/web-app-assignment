package web.example.com.tmdb.components.tmdb.business.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import web.example.com.tmdb.components.tmdb.business.boundary.TmdbApiService;
import web.example.com.tmdb.components.tmdb.business.entity.MovieDto;
import web.example.com.tmdb.components.tmdb.business.entity.SearchMetadata;
import web.example.com.tmdb.components.tmdb.business.entity.SearchResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbMovieResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbPopularMoviesResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchMoviesResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbMovieDetailsResponse;
import web.example.com.tmdb.components.tmdb.business.entity.TmdbSearchRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TMDB API service
 * Handles communication with The Movie Database API
 */
@Service
public class TmdbApiServiceImpl implements TmdbApiService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public TmdbApiServiceImpl(RestTemplate restTemplate,
            @Value("${tmdb.api.key}") String apiKey,
            @Value("${tmdb.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<MovieDto> getPopularMovies() {
        try {
            String url = String.format("%s/movie/popular?api_key=%s", baseUrl, apiKey);
            TmdbPopularMoviesResponse response = restTemplate.getForObject(url, TmdbPopularMoviesResponse.class);

            if (response == null || response.getResults() == null) {
                return List.of();
            }

            return response.getResults().stream()
                    .map(this::transformToMovieDto)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch popular movies", e);
        }
    }

    @Override
    public SearchResponse searchMovies(TmdbSearchRequest request) {
        try {
            String url = String.format("%s/search/movie?api_key=%s&query=%s&page=%d&language=%s&include_adult=%s",
                    baseUrl, apiKey, request.getQuery(), request.getPage(),
                    request.getLanguage(), request.getIncludeAdult());

            TmdbSearchMoviesResponse response = restTemplate.getForObject(url, TmdbSearchMoviesResponse.class);

            if (response == null) {
                return SearchResponse.builder()
                        .meta(SearchMetadata.builder()
                                .page(request.getPage())
                                .totalPages(0)
                                .totalResults(0)
                                .build())
                        .data(List.of())
                        .build();
            }

            List<MovieDto> movies = response.getResults() != null
                    ? response.getResults().stream()
                            .map(this::transformToMovieDto)
                            .collect(Collectors.toList())
                    : List.of();

            return SearchResponse.builder()
                    .meta(SearchMetadata.builder()
                            .page(response.getPage())
                            .totalPages(response.getTotalPages())
                            .totalResults(response.getTotalResults())
                            .build())
                    .data(movies)
                    .build();

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to search movies", e);
        }
    }

    @Override
    public MovieDto getMovieDetails(Long movieId) {
        try {
            String url = String.format("%s/movie/%d?api_key=%s", baseUrl, movieId, apiKey);
            TmdbMovieDetailsResponse response = restTemplate.getForObject(url, TmdbMovieDetailsResponse.class);

            if (response == null) {
                throw new RuntimeException("Movie not found");
            }

            return transformMovieDetailsToDto(response);

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch movie details", e);
        }
    }

    /**
     * Transform TMDB movie response to our MovieDto
     */
    private MovieDto transformToMovieDto(TmdbMovieResponse movieResponse) {
        return MovieDto.builder()
                .id(movieResponse.getId().longValue())
                .title(movieResponse.getTitle())
                .overview(movieResponse.getOverview())
                .releaseDate(movieResponse.getReleaseDate())
                .posterPath(movieResponse.getPosterPath())
                .backdropPath(movieResponse.getBackdropPath())
                .voteAverage(movieResponse.getVoteAverage())
                .voteCount(movieResponse.getVoteCount())
                .popularity(movieResponse.getPopularity())
                .adult(movieResponse.getAdult())
                .originalLanguage(movieResponse.getOriginalLanguage())
                .originalTitle(movieResponse.getOriginalTitle())
                .video(movieResponse.getVideo())
                .build();
    }

    /**
     * Transform TMDB movie details response to our MovieDto
     */
    private MovieDto transformMovieDetailsToDto(TmdbMovieDetailsResponse movieDetails) {
        return MovieDto.builder()
                .id(movieDetails.getId().longValue())
                .title(movieDetails.getTitle())
                .overview(movieDetails.getOverview())
                .releaseDate(movieDetails.getReleaseDate())
                .posterPath(movieDetails.getPosterPath())
                .backdropPath(movieDetails.getBackdropPath())
                .voteAverage(movieDetails.getVoteAverage())
                .voteCount(movieDetails.getVoteCount())
                .popularity(movieDetails.getPopularity())
                .adult(movieDetails.getAdult())
                .originalLanguage(movieDetails.getOriginalLanguage())
                .originalTitle(movieDetails.getOriginalTitle())
                .video(movieDetails.getVideo())
                .runtime(movieDetails.getRuntime())
                .genres(movieDetails.getGenres() != null ? movieDetails.getGenres().stream()
                        .map(genre -> MovieDto.GenreDto.builder()
                                .id(genre.getId())
                                .name(genre.getName())
                                .build())
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
