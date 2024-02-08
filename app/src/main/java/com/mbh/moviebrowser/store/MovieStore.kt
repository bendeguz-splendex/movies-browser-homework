package com.mbh.moviebrowser.store

import com.mbh.moviebrowser.domain.Genre
import com.mbh.moviebrowser.domain.Movie
import kotlinx.coroutines.flow.MutableStateFlow

class MovieStore {
    val genres: MutableStateFlow<List<Genre>> = MutableStateFlow(emptyList())
    val movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val detailsId: MutableStateFlow<Long> = MutableStateFlow(-1)
}