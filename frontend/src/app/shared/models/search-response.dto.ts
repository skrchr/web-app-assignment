import { MovieDto } from './movie.dto';

export interface SearchMetadata {
  page: number;
  totalPages: number;
  totalResults: number;
}

export interface SearchResponse {
  meta: SearchMetadata;
  data: MovieDto[];
}

