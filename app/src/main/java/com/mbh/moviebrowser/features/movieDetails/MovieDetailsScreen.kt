package com.mbh.moviebrowser.features.movieDetails

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.mbh.moviebrowser.domain.Movie

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel) {
    val movieState = viewModel.movie.collectAsState(null).value
    val isLoading = movieState == null

    MovieDetailsScreenUI(
        movie = movieState,
        isLoading = isLoading,
        onFavoriteClicked = viewModel::onFavoriteClicked,
    )
}

@Composable
fun MovieDetailsScreenUI(
    movie: Movie?,
    isLoading: Boolean,
    onFavoriteClicked: (Boolean) -> Unit,
) {
    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (movie == null) {
        // Handle the case where the movie is null but not loading
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = movie.coverUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(4.dp))
        val image = if (movie.isFavorite) {
            painterResource(id = android.R.drawable.btn_star_big_on)
        } else {
            painterResource(id = android.R.drawable.btn_star_big_off)
        }
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.clickable {
                Modifier.scale(1.5f)
                onFavoriteClicked(!movie.isFavorite)
            },
        )
        AsyncImage(
            model = movie.fullCoverUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(280.dp)
                .zIndex(1.0f),
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.genres?.joinToString(", ") ?: "N/A",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Popularity: ${movie.popularity}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
    }
}

@Composable
@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
fun MovieDetailsScreenUIPreview() {
    MovieDetailsScreenUI(
        movie = Movie(
            id = 123L,
            title = "Example Movie",
            genres = listOf("Action", "Adventure", "Sci-Fi"),
            overview = "This is an overview of the example movie. It's full of action, adventure and sci-fi elements.",
            coverUrl = "https://image.tmdb.org/t/p/w300/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
            popularity = 7.5,
            isFavorite = false,
            genre_ids = listOf(28, 12, 878),
        ),
        isLoading = false,
        onFavoriteClicked = {},
    )
}
