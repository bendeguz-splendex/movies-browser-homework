package com.mbh.moviebrowser.domain

import kotlinx.serialization.Serializable

@Serializable
class MovieResponse {
    val page: Int = 0
    val total_pages: Int = 0
    val total_results: Int = 0
    val results: List<Movie> = emptyList()
}