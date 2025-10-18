package web.example.com.tmdb.components.tmdb.business.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response for TMDB Popular Movies endpoint: GET /movie/popular
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbPopularMoviesResponse {

    @JsonProperty("results")
    private List<TmdbMovieResponse> results;

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;
}
