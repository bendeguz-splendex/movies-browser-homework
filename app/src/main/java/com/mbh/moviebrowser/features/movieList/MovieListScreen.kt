package com.mbh.moviebrowser.features.movieList

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.mbh.moviebrowser.domain.Movie

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    onDetailsClicked: (Movie) -> Unit,
) {
    val movies by viewModel.movies.collectAsState()

    if (movies.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

        viewModel.onBottomReached()
        return
    }

    MovieListScreenUI(
        movies,
        viewModel::onBottomReached,
    ) {
        viewModel.storeMovieForNavigation(it)
        onDetailsClicked(it)
    }
}

@Composable
fun MovieListScreenUI(
    movies: List<Movie>,
    onBottomReached: () -> Unit,
    onDetailsClicked: (Movie) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val endOfListReached = !scrollState.canScrollForward

    LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
        items(movies) { item ->
            MovieListItem(
                movie = item,
                onDetailsClicked,
            )
        }
    }

    LaunchedEffect(endOfListReached) {
        onBottomReached()
    }
}

@Composable
private fun MovieListItem(
    movie: Movie,
    onDetailsClicked: (Movie) -> Unit,
) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onDetailsClicked(movie)
            },
    ) {
        Box {
            AsyncImage(
                model = movie.fullCoverUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(80.dp)
                    .zIndex(1.0f),
            )
            val image = if (movie.isFavorite) {
                painterResource(id = android.R.drawable.btn_star_big_on)
            } else {
                painterResource(id = android.R.drawable.btn_star_big_off)
            }
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .zIndex(2.0f)
                    .align(Alignment.TopEnd),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.genres?.joinToString(", ") ?: "N/A",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Popularity: ${movie.popularity}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = movie.popularity!! / 10.0f, modifier = Modifier.fillMaxWidth())
        }
    }
}

fun LinearProgressIndicator(progress: Double, modifier: Modifier) {}

@Composable
@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
fun MovieListScreenUIPreview() {
    MovieListScreenUI(
        listOf(
            Movie(
                id = 455476,
                title = "Knights of the Zodiac",
                genres = emptyList(),
                overview = "When a headstrong street orphan, Seiya, in search of his abducted sister unwittingly taps into hidden powers, he discovers he might be the only person alive who can protect a reincarnated goddess, sent to watch over humanity. Can he let his past go and embrace his destiny to become a Knight of the Zodiac?",
                coverUrl = "/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
                popularity = 6.5,
                isFavorite = true,
                genre_ids = listOf(28, 878),
            ),
            Movie(
                id = 385687,
                title = "Fast X",
                genres = emptyList(),
                overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
                coverUrl = "/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
                popularity = 7.4,
                isFavorite = false,
                genre_ids = listOf(28),
            ),
        ),
        onDetailsClicked = {},
        onBottomReached = {},
    )
}