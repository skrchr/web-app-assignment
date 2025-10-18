import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SkeletonModule } from 'primeng/skeleton';
import { SearchSuggestionsComponent } from '../search-suggestions/search-suggestions.component';
import { TmdbGateway } from '../shared/tmdb.gateway';
import { MovieDto } from '../shared/models';

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    SkeletonModule,
    SearchSuggestionsComponent
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent implements OnInit, AfterViewInit {
  @ViewChild('moviesCarousel') moviesCarousel!: ElementRef<HTMLDivElement>;
  @ViewChild('searchContainer') searchContainer!: ElementRef<HTMLDivElement>;

  movies: MovieDto[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;
  isSearching = false;
  canScrollLeft = false;
  canScrollRight = false;

  // Search suggestions state
  showSuggestions = false;
  suggestions: MovieDto[] = [];
  suggestionsLoading = false;
  suggestionsError = false;
  private searchTimeout: any;

  constructor(
    private router: Router,
    private tmdbGateway: TmdbGateway
  ) {}

  ngOnInit(): void {
    this.loadPopularMovies();
  }

  ngAfterViewInit(): void {
    if (this.moviesCarousel) {
      this.moviesCarousel.nativeElement.addEventListener('scroll', () => this.updateScrollButtons());
    }
  }

  loadPopularMovies(): void {
    this.loading = true;
    this.error = null;
    this.isSearching = false;

    this.tmdbGateway.getPopularMovies().subscribe({
      next: (movies) => {
        this.movies = movies;
        this.loading = false;
        setTimeout(() => this.updateScrollButtons(), 100);
      },
      error: (err) => {
        console.error('Error loading popular movies:', err);
        this.movies = [];
        this.error = 'Failed to load popular movies. Please try again.';
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    if (!this.searchQuery || this.searchQuery.trim().length === 0) {
      return;
    }

    this.loading = true;
    this.error = null;
    this.isSearching = true;
    this.hideSuggestions();

    this.tmdbGateway.searchMovies({ query: this.searchQuery, page: 1 }).subscribe({
      next: (response) => {
        this.movies = response.data;
        this.loading = false;
        setTimeout(() => this.updateScrollButtons(), 100);
      },
      error: (err) => {
        console.error('Error searching movies:', err);
        this.movies = [];
        this.error = 'Failed to search movies. Please try again.';
        this.loading = false;
      }
    });
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.loadPopularMovies();
    this.hideSuggestions();
  }

  onMovieClick(movieId: number): void {
    this.router.navigate(['/movie', movieId]);
  }

  // Search suggestions methods
  onSearchInput(): void {
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }

    if (this.searchQuery.trim().length < 2) {
      this.hideSuggestions();
      return;
    }

    this.showSuggestions = true;
    this.suggestionsLoading = true;

    // Debounce search - wait 300ms after user stops typing
    this.searchTimeout = setTimeout(() => {
      this.searchMoviesForSuggestions();
    }, 300);
  }

  onSearchFocus(): void {
    if (this.searchQuery.trim().length >= 2 && this.suggestions.length > 0) {
      this.showSuggestions = true;
    }
  }

  onSearchBlur(): void {
    // Delay hiding to allow clicking on suggestions
    setTimeout(() => {
      this.hideSuggestions();
    }, 200);
  }

  onSuggestionSelected(movieId: number): void {
    this.hideSuggestions();
    this.onMovieClick(movieId);
  }

  private searchMoviesForSuggestions(): void {
    this.tmdbGateway.searchMovies({ query: this.searchQuery, page: 1 }).subscribe({
      next: (response) => {
        this.suggestions = response.data.slice(0, 8); // Limit to 8 suggestions
        this.suggestionsLoading = false;
        this.suggestionsError = false;
      },
      error: (err) => {
        console.error('Error fetching suggestions:', err);
        this.suggestions = [];
        this.suggestionsLoading = false;
        this.suggestionsError = true;
      }
    });
  }

  private hideSuggestions(): void {
    this.showSuggestions = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (this.searchContainer && !this.searchContainer.nativeElement.contains(event.target as Node)) {
      this.hideSuggestions();
    }
  }

  retry(): void {
    if (this.isSearching) {
      this.onSearch();
    } else {
      this.loadPopularMovies();
    }
  }

  scrollLeft(): void {
    if (this.moviesCarousel) {
      const scrollAmount = this.moviesCarousel.nativeElement.clientWidth * 0.8;
      this.moviesCarousel.nativeElement.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
    }
  }

  scrollRight(): void {
    if (this.moviesCarousel) {
      const scrollAmount = this.moviesCarousel.nativeElement.clientWidth * 0.8;
      this.moviesCarousel.nativeElement.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    }
  }

  private updateScrollButtons(): void {
    if (!this.moviesCarousel) return;

    const element = this.moviesCarousel.nativeElement;
    this.canScrollLeft = element.scrollLeft > 0;
    this.canScrollRight = element.scrollLeft < (element.scrollWidth - element.clientWidth - 10);
  }


}

