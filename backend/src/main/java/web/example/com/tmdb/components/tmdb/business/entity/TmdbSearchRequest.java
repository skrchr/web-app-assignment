package web.example.com.tmdb.components.tmdb.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for TMDB movie search
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbSearchRequest {

    private String query;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private String language = "en-US";

    @Builder.Default
    private Boolean includeAdult = false;

    private String region;
}
