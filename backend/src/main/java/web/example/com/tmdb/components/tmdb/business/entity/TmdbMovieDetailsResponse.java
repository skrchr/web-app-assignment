package web.example.com.tmdb.components.tmdb.business.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response for TMDB Movie Details endpoint: GET /movie/{movie_id}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieDetailsResponse {

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

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("genres")
    private List<TmdbGenre> genres;

    @JsonProperty("production_companies")
    private List<TmdbProductionCompany> productionCompanies;

    @JsonProperty("production_countries")
    private List<TmdbProductionCountry> productionCountries;

    @JsonProperty("spoken_languages")
    private List<TmdbSpokenLanguage> spokenLanguages;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbProductionCompany {
        @JsonProperty("id")
        private Integer id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("logo_path")
        private String logoPath;

        @JsonProperty("origin_country")
        private String originCountry;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbProductionCountry {
        @JsonProperty("iso_3166_1")
        private String iso31661;

        @JsonProperty("name")
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TmdbSpokenLanguage {
        @JsonProperty("iso_639_1")
        private String iso6391;

        @JsonProperty("name")
        private String name;

        @JsonProperty("english_name")
        private String englishName;
    }
}
