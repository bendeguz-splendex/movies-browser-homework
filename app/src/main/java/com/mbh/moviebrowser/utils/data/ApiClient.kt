import android.util.Log
import com.mbh.moviebrowser.BuildConfig
import com.mbh.moviebrowser.domain.GenreResponse
import com.mbh.moviebrowser.domain.MovieResponse
import io.ktor.client.*
import io.ktor.client.call.receive
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.util.InternalAPI
import kotlinx.serialization.Serializable


@InternalAPI object ApiClient {

    sealed class ApiEndpoint {
        data class Movies(val timeWindow: String, val page: Int, val language: String? = "en-US") : ApiEndpoint()
        data class Genres(val language: String? = "en-US") : ApiEndpoint()
    }

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getGenres(language: String? = "en-US"): GenreResponse {
        return httpClient.get {
            apiUrl(ApiEndpoint.Genres(language))
        }
    }

    suspend fun getMovies(timeWindow: String, page: Int, language: String? = "en-US"): MovieResponse {
        return httpClient.get {
            apiUrl(ApiEndpoint.Movies(timeWindow, page, language))
        }
    }

    private fun HttpRequestBuilder.apiUrl(apiEndpoint: ApiEndpoint) {
        url {
            takeFrom(BuildConfig.BASE_URL)
            when (apiEndpoint) {
                is ApiEndpoint.Movies -> {
                    path( "3", "trending", "movie", apiEndpoint.timeWindow)
                    parameters.append("api_key", BuildConfig.API_KEY)
                    parameters.append("page", apiEndpoint.page.toString())
                    parameters.append("language", apiEndpoint.language ?: "en-US")
                    parameters.append("include_adult", "false")
                }
                is ApiEndpoint.Genres -> {
                    path( "3", "genre", "movie", "list")
                    parameters.append("api_key", BuildConfig.API_KEY)
                    parameters.append("language", apiEndpoint.language ?: "en-US")
                }

                else -> {
                    throw IllegalArgumentException("Unknown endpoint")
            }
        }
        }
    }

    fun closeHttpClient() {
        httpClient.close()
    }
}
