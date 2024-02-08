package com.mbh.moviebrowser.features.movieDetails

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.store.MovieStore
import kotlinx.coroutines.flow.map

class MovieDetailsViewModel(
    val movieStore: MovieStore,
) : ViewModel() {

    val movie = movieStore.movies.map { it.firstOrNull { it.id == movieStore.detailsId.value } }

    fun onFavoriteClicked(isFavorite: Boolean) {
        val clickedMovie = movieStore.movies.value.first { it.id == movieStore.detailsId.value }
        movieStore.movies.value = movieStore.movies.value.map {
            if (it.id == clickedMovie.id) {
                it.copy(isFavorite = isFavorite)
            } else {
                it
            }
        }
    }

    fun onOpen() {
        // create an animation effect where the
    }
}
