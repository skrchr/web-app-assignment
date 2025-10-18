import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { TmdbGateway } from '../shared/tmdb.gateway';
import { MovieDto } from '../shared/models';

@Component({
  selector: 'app-movie-details',
  standalone: true,
  imports: [CommonModule, ButtonModule, SkeletonModule],
  templateUrl: './movie-details.component.html',
  styleUrl: './movie-details.component.scss'
})
export class MovieDetailsComponent implements OnInit {
  movie: MovieDto | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tmdbGateway: TmdbGateway
  ) {}

  ngOnInit(): void {
    const movieId = this.route.snapshot.paramMap.get('id');
    if (movieId) {
      this.loadMovieDetails(+movieId);
    } else {
      this.error = 'Invalid movie ID';
      this.loading = false;
    }
  }

  loadMovieDetails(id: number): void {
    this.loading = true;
    this.error = null;

    this.tmdbGateway.getMovieDetails(id).subscribe({
      next: (movie) => {
        this.movie = movie;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading movie details:', err);
        this.error = 'Failed to load movie details. Please try again.';
        this.loading = false;
      }
    });
  }

  getPosterUrl(posterPath: string | null): string {
    if (posterPath) {
      return `https://image.tmdb.org/t/p/w500${posterPath}`;
    }
    return '';
  }

  getBackdropUrl(backdropPath: string | null): string {
    if (backdropPath) {
      return `https://image.tmdb.org/t/p/original${backdropPath}`;
    }
    return '';
  }

  getRuntime(minutes: number | null | undefined): string {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  retry(): void {
    const movieId = this.route.snapshot.paramMap.get('id');
    if (movieId) {
      this.loadMovieDetails(+movieId);
    }
  }
}
