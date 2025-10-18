package web.example.com.tmdb.components.tmdb.business.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Raw movie data from TMDB API response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("adult")
    private Boolean adult;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("video")
    private Boolean video;

    // Additional fields for movie details
    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("genres")
    private List<TmdbGenre> genres;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbGenre {
        @JsonProperty("id")
        private Integer id;

        @JsonProperty("name")
        private String name;
    }
}
