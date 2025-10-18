package web.example.com.tmdb.components.tmdb.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Search response with metadata and results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private SearchMetadata meta;
    private List<MovieDto> data;
}
