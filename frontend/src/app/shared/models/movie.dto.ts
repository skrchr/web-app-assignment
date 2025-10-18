export interface GenreDto {
  id: number;
  name: string;
}

export interface MovieDto {
  id: number;
  title: string;
  overview: string;
  release_date: string;
  poster_path: string | null;
  backdrop_path: string | null;
  vote_average: number;
  vote_count?: number;
  popularity?: number;
  adult?: boolean;
  original_language?: string;
  original_title?: string;
  video?: boolean;
  runtime?: number;
  genres?: GenreDto[];
}

