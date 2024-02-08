package com.mbh.moviebrowser.features.movieList

import ApiClient
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.domain.MovieResponse
import com.mbh.moviebrowser.store.MovieStore
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode
import io.ktor.util.InternalAPI
import kotlinx.coroutines.launch

@OptIn(InternalAPI::class)
class MovieListViewModel(
    private val movieStore: MovieStore,
    private val onError: (Throwable) -> Unit,
) : ViewModel() {
    private val apiRepository = ApiClient;
    val movies = movieStore.movies
    private val localeName: String = "en-US"

    private var currentPage: Int = 1
    private var hasMorePages: Boolean = false

    private val _isLoading = mutableStateOf(false);
    val isLoading: State<Boolean> = _isLoading;

    private suspend fun getGenres() {
        try {
            val response = apiRepository.getGenres()

            if (response.genres.isEmpty()) {
                _isLoading.value = false;
            }


            movieStore.genres.value = response.genres

            getMovies()
            _isLoading.value = false;
        } catch (e: Throwable) {
            _isLoading.value = false;
            onError(e)
        }
    }

    private suspend fun getMovies() {
        if (movieStore.genres.value.isEmpty()) {
            Log.d("MovieListViewModel", "Loading genre list first")
            getGenres()
            return
        }

        try {
            Log.d("MovieListViewModel", "Loading movie list")
            val response: MovieResponse = apiRepository.getMovies(
                "day",
                currentPage,
                localeName,
            )
            var genreStrings: List<String> = emptyList()

            response.results.forEach { movie ->
                genreStrings = movie.genre_ids?.map { genreId ->
                    movieStore.genres.value.find { it.id.toInt().toString() == genreId.toString() }?.name ?: "N/A"
                } ?: emptyList()
                movie.genres = genreStrings
            }

            movieStore.movies.value = mutableStateListOf<Movie>().apply {
                addAll(movieStore.movies.value)
                addAll(response.results)
            }

            hasMorePages = true;
            _isLoading.value = false;



        } catch (e: NoTransformationFoundException) {
            Log.d("MovieListViewModel", "NoTransformationFoundException: $e")
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                Log.d("MovieListViewModel", "ClientRequestException: Unauthorized")
            }
        }
    }


    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsId.value = movie.id
    }

    fun onBottomReached() {
        if (!hasMorePages) {
            return
        }


        viewModelScope.launch {
            getMovies()
        }
    }

    init {
        viewModelScope.launch {
            getGenres()
        }
    }
}
