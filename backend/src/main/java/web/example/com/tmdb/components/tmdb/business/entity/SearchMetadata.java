package web.example.com.tmdb.components.tmdb.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadata for search results pagination
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMetadata {
    private Integer page;
    private Integer totalPages;
    private Integer totalResults;
}
