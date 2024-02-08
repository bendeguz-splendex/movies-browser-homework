package com.mbh.moviebrowser.domain


import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<Genre>
)
