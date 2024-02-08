package com.mbh.moviebrowser.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long,
    val title: String,
    var genres: List<String>? = null,
    val overview: String,
    @SerialName("poster_path")
    val coverUrl: String?,
    val popularity: Double,
    val isFavorite: Boolean = false,
    val genre_ids: List<Int>,
) {
    val fullCoverUrl: String
        get() = "https://image.tmdb.org/t/p/w500$coverUrl"
}
