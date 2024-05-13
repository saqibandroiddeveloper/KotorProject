package com.saqiii.kotorproject.ktor

import android.util.Log
import com.saqiii.kotorproject.ktor.components.NetworkResponse
import com.saqiii.kotorproject.ktor.components.RequestTypes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


object NetworkClient {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            val json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
            json(json)
        }
    }

    fun String.logable(): String {
        return this.replace("htt", "")
    }

    suspend inline fun <reified T> makeNetworkRequest(
        url: String,
        requestType: RequestTypes,
        headers: Map<String, String>? = null,
    ): NetworkResponse<T> {
        Log.d("makeNetworkRequest", "makeNetworkRequest: ${url.logable()}")
        return withContext(Dispatchers.IO) {
            try {
                val response: String = requestType.getHttpBuilder(url) {
                    Log.d("makeNetworkRequest", "request buildser${url.logable()}")

                    if (requestType is RequestTypes.Post) {
                        it.setBody(requestType.body)
                    }
                    headers?.let { headers ->
                        headers.forEach { (key, value) ->
                            it.header(key, value)
                        }
                    }
                    Log.d("cvrr", "${it.body}")
                }.body()
                Log.d("makeNetworkRequest", "Network Response:$response")


                val newResponse: T = Json.decodeFromString(response)
                (NetworkResponse.Success(newResponse))

            } catch (e: ClientRequestException) {
                Log.d("makeNetworkRequest", "Network ClientRequestException:${e.message}")

                (NetworkResponse.Failure(e.message))
            } catch (e: ServerResponseException) {
                Log.d("makeNetworkRequest", "Network ServerResponseException:${e.message}")

                (NetworkResponse.Failure(e.message))
            } catch (e: Exception) {
                Log.d("makeNetworkRequest", "Network Exception:${e.message}")

                (NetworkResponse.Failure(e.message ?: "Unknown error"))
            }
        }
    }

    suspend inline fun makeStringNetworkRequest(
        url: String,
        requestType: RequestTypes,
        headers: Map<String, String>? = null,
    ): NetworkResponse<String> {
        Log.d("makeNetworkRequest", "hittig =$url")
        return withContext(Dispatchers.IO) {
            try {
                val response: String = requestType.getHttpBuilder(url) {
                    if (requestType is RequestTypes.Post) {
                        it.setBody(requestType.body)
                    }
                    headers?.let { headers ->
                        headers.forEach { (key, value) ->
                            it.header(key, value)
                        }
                    }
                    Log.d("makeNetworkRequest", "${it.body}")
                }.body()
                Log.d("makeNetworkRequest", "Network Response:$response")


                val newResponse: String = response
                (NetworkResponse.Success(newResponse))

            } catch (e: ClientRequestException) {
                (NetworkResponse.Failure(e.message))
            } catch (e: ServerResponseException) {
                (NetworkResponse.Failure(e.message))
            } catch (e: Exception) {
                (NetworkResponse.Failure(e.message ?: "Unknown error"))
            } finally {
            }
        }
    }


    suspend fun RequestTypes.getHttpBuilder(
        url: String,
        callback: (HttpRequestBuilder) -> Unit
    ): HttpResponse {
        return when (this) {
            RequestTypes.Get -> {
                Log.d("cvrr", "Request Get")

                client.get(url) {
                    Log.d("cvrr", "Request result=${this.body}")

                    callback.invoke(this)
                }
            }

            is RequestTypes.Post -> {
                Log.d("cvrr", "Request Post")

                client.post(url) {
                    Log.d("cvrr", "Request ${this.body}")

                    callback.invoke(this)
                }
            }

            RequestTypes.Get -> TODO()
        }
    }


}