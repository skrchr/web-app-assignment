import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { MovieDto } from '../shared/models';

@Component({
  selector: 'app-search-suggestions',
  standalone: true,
  imports: [CommonModule, SkeletonModule],
  templateUrl: './search-suggestions.component.html',
  styleUrl: './search-suggestions.component.scss'
})
export class SearchSuggestionsComponent {
  @Input() movies: MovieDto[] = [];
  @Input() loading = false;
  @Input() query = '';
  @Input() hasError = false;
  @Output() movieSelected = new EventEmitter<number>();

  onMovieClick(movieId: number): void {
    this.movieSelected.emit(movieId);
  }

  getPosterUrl(poster_path: string | null): string {
    if (poster_path) {
      return `https://image.tmdb.org/t/p/w92${poster_path}`;
    }
    return '';
  }

  getYear(release_date: string): string {
    if (!release_date) return '';
    return new Date(release_date).getFullYear().toString();
  }
}
