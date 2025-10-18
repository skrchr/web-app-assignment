import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { MovieDto, SearchResponse } from './models';

const environment = {
  apiUrl: 'http://localhost:8080'
};

export interface SearchMoviesParams {
  query: string;
  page?: number;
  language?: string;
}

@Injectable({
  providedIn: 'root'
})
export class TmdbGateway {
  private readonly baseEndpoint = `${environment.apiUrl}/api/v1/movies`;

  constructor(private http: HttpClient) {}

  /**
   * Get popular movies
   * GET /api/v1/movies/popular
   */
  getPopularMovies(): Observable<MovieDto[]> {
    return this.http.get<MovieDto[]>(`${this.baseEndpoint}/popular`);
  }

  /**
   * Search movies by query
   * GET /api/v1/movies/search
   */
  searchMovies(params: SearchMoviesParams): Observable<SearchResponse> {
    let httpParams = new HttpParams()
      .set('query', params.query);

    if (params.page) {
      httpParams = httpParams.set('page', params.page.toString());
    }

    if (params.language) {
      httpParams = httpParams.set('language', params.language);
    }

    return this.http.get<SearchResponse>(`${this.baseEndpoint}/search`, { params: httpParams });
  }

  /**
   * Get movie details by ID
   * GET /api/v1/movies/{id}
   */
  getMovieDetails(id: number): Observable<MovieDto> {
    return this.http.get<MovieDto>(`${this.baseEndpoint}/${id}`);
  }
}

